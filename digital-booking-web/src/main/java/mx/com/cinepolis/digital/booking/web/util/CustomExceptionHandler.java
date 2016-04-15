package mx.com.cinepolis.digital.booking.web.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.faces.FacesException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler class for exceptions
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper
{
  private static final String PLACEHOLDER_CURLY_RIGHT_BRACE = "}";
  private static final String PLACEHOLDER_CURLY_LEFT_BRACE = "{";
  private static final Logger LOG = LoggerFactory.getLogger( CustomExceptionHandler.class );
  private ExceptionHandler wrapped;

  /**
   * Constructor by {@link javax.faces.context.ExceptionHandler}
   * 
   * @param wrapped
   */
  public CustomExceptionHandler( ExceptionHandler wrapped )
  {
    this.wrapped = wrapped;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExceptionHandler getWrapped()
  {
    return wrapped;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("rawtypes")
  @Override
  public void handle() throws FacesException
  {
    Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();

    while( iterator.hasNext() )
    {
      ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
      ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

      Throwable throwable = context.getException();

      try
      {
        RequestContext.getCurrentInstance().addCallbackParam( "notValid", true );
        
        if( throwable instanceof javax.faces.application.ViewExpiredException )
        {
          FacesContext ctx = FacesContext.getCurrentInstance();
          ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) ctx.getApplication()
              .getNavigationHandler();
          nav.performNavigation( "/views/login/login.do?faces-redirect=true" );
        }

        DigitalBookingException dbe = extractDigitalBookingException( throwable );


        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceBundle msg = fc.getApplication().evaluateExpressionGet( fc, "#{msg}", ResourceBundle.class );
        String description = msg.getString( "digitalbooking.business.error.code." + dbe.getCode() );
        description = applyParameters(description, dbe);
        dbe.setDescription( description );
        
        LOG.error( "----------------------------------------" );
        LOG.error( "Exception code: " + dbe.getCode() );
        LOG.error( "Description:" + dbe.getDescription() );
        LOG.error( "----------------------------------------" );
        LOG.error( dbe.getMessage(), dbe );
        LOG.error( "----------------------------------------" );

        RequestContext.getCurrentInstance().addCallbackParam( "notValid", true );
        RequestContext.getCurrentInstance().addCallbackParam( "code", dbe.getCode() );
        RequestContext.getCurrentInstance().addCallbackParam( "description",
          StringUtils.defaultString( dbe.getDescription() ) );
        RequestContext.getCurrentInstance().addCallbackParam( "id", dbe.getId() );

        Flash flash = fc.getExternalContext().getFlash();

        // Put the exception in the flash scope to be displayed in the error
        // page if necessary ...
        flash.put( "errorDetails", throwable.getMessage() );

        LOG.error( "the error is put in the flash: " + throwable.getMessage() );

        // TODO: gsegura, verificar como manejar los errores de submit actualmente sólo sirve para ajax
        // NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
        // navigationHandler.handleNavigation( fc, null, "error?faces-redirect=true" );
        
        RequestContext.getCurrentInstance().execute( "DigitalBookingUtil.showErrorDialog({severity:'" + FacesMessage.SEVERITY_ERROR + 
                    "',summary:'" + "Message Error" + "',detail:'" + StringEscapeUtils.escapeJavaScript( dbe.getDescription() ) + "'});" );
        
        
      }
      finally
      {
        iterator.remove();
      }
    }

    // Let the parent handle the rest
    getWrapped().handle();
  }

  private String applyParameters( String description, DigitalBookingException dbe )
  {
    String result = description;
    if( dbe.getArgs() != null && dbe.getArgs().length > 0 )
    {
      int n = 0;
      for( Object o : dbe.getArgs() )
      {
        String placeholder = new StringBuilder().append( PLACEHOLDER_CURLY_LEFT_BRACE ).append( n )
            .append( PLACEHOLDER_CURLY_RIGHT_BRACE ).toString();
        result = StringUtils.replace( result, placeholder, o.toString() );
        n++;
      }
    }
    return result;
  }

  private DigitalBookingException extractDigitalBookingException( Throwable throwable )
  {
    DigitalBookingException dbe = null;
    Throwable cause = throwable;
    int n = 0;
    while( n < 10 )
    {
      if( cause == null )
      {
        break;
      }
      if( cause instanceof DigitalBookingException )
      {
        dbe = (DigitalBookingException) cause;
        break;
      }
      cause = cause.getCause();
      n++;
    }

    if( dbe == null )
    {
      LOG.error( throwable.getMessage(), throwable );
      dbe = DigitalBookingExceptionBuilder.build( throwable );
    }

    return dbe;
  }
}