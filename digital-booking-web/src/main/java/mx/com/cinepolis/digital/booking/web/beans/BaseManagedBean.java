package mx.com.cinepolis.digital.booking.web.beans;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.web.util.ResourceBundleUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.context.RequestContext;

/**
 * @author kperez
 */
public class BaseManagedBean implements Serializable
{

  protected static final String USER_ATTRIBUTE = "user";
  protected static final String INCOME_WEEK_ID_ATTRIBUTE = "INCOME_WINDOW_WEEK_ID";
  protected static final String INCOME_THEATER_ID_ATTRIBUTE = "INCOME_WINDOW_THEATER_ID";
  private static final String VALIDATION_FAILED = "validationFailed";
  private static final String FAIL = "fail";
  private static final String MSG_INFO_TEXT = "common.msgInfoText";
  private static final String MSG_INFO_TITLE = "common.msgInfoTitle";
  private static final String VALIDATION_ERROR_MSG = "common.validationErrorText";
  private static final String VALIDATION_ERROR_TITLE = "common.validationErrorTitle";
  private static final String VALIDATION_WARNING_TITLE  = "common.validationWarningTitle";
  private static final String ERROR_DIALOG_END = "'});";
  private static final String ERROR_DIALOG_DETAIL = "',detail:'";
  private static final String ERROR_DIALOG_SUMMARY = "',summary:'";
  private static final String ERROR_DIALOG_SEVERITY = "DigitalBookingUtil.showErrorDialog({severity:'";
  

  private static final long serialVersionUID = -8255247027930445205L;

  /**
   * Método que valida que se haya seleccionado un registro
   */
  public void createMessageValidationSelection()
  {
    RequestContext.getCurrentInstance().execute(
      CinepolisUtils.buildStringUsingMutable( ERROR_DIALOG_SEVERITY, FacesMessage.SEVERITY_ERROR, ERROR_DIALOG_SUMMARY,
        ResourceBundleUtil.getMessageFormBundle( VALIDATION_ERROR_TITLE ), ERROR_DIALOG_DETAIL,
        StringEscapeUtils.escapeJavaScript( ResourceBundleUtil.getMessageFormBundle( VALIDATION_ERROR_MSG ) ),
        ERROR_DIALOG_END ) );

  }

  /**
   * Método que genera un mensaje de error
   */
  public void createMessageError( String message )
  {
    RequestContext.getCurrentInstance().execute(
      CinepolisUtils.buildStringUsingMutable( ERROR_DIALOG_SEVERITY, FacesMessage.SEVERITY_ERROR, ERROR_DIALOG_SUMMARY,
        ResourceBundleUtil.getMessageFormBundle( VALIDATION_ERROR_TITLE ), ERROR_DIALOG_DETAIL,
        StringEscapeUtils.escapeJavaScript( ResourceBundleUtil.getMessageFormBundle( message ) ), ERROR_DIALOG_END ) );
    RequestContext.getCurrentInstance().addCallbackParam( VALIDATION_FAILED, true );
  }
  
  /**
   * Método que genera un mensaje de error
   */
  public void createMessageWarning( String message )
  {
    RequestContext.getCurrentInstance().execute(
      CinepolisUtils.buildStringUsingMutable( ERROR_DIALOG_SEVERITY, FacesMessage.SEVERITY_WARN, ERROR_DIALOG_SUMMARY,
        ResourceBundleUtil.getMessageFormBundle( VALIDATION_WARNING_TITLE ), ERROR_DIALOG_DETAIL,
        StringEscapeUtils.escapeJavaScript( ResourceBundleUtil.getMessageFormBundle( message ) ), ERROR_DIALOG_END ) );
    RequestContext.getCurrentInstance().addCallbackParam( VALIDATION_FAILED, true );
  }

  /**
   * Método que genera un mensaje de éxito
   */
  public void createMessageSuccess()
  {
    RequestContext.getCurrentInstance().execute(
      CinepolisUtils.buildStringUsingMutable( ERROR_DIALOG_SEVERITY, FacesMessage.SEVERITY_INFO, ERROR_DIALOG_SUMMARY,
        ResourceBundleUtil.getMessageFormBundle( MSG_INFO_TITLE ) + ERROR_DIALOG_DETAIL,
        StringEscapeUtils.escapeJavaScript( ResourceBundleUtil.getMessageFormBundle( MSG_INFO_TEXT ) ),
        ERROR_DIALOG_END ) );
  }

  /**
   * Método que genera un mensaje de éxito
   */
  public void createMessageSuccessWithMsg( String message )
  {
    RequestContext.getCurrentInstance().execute(
      CinepolisUtils.buildStringUsingMutable( ERROR_DIALOG_SEVERITY, FacesMessage.SEVERITY_INFO, ERROR_DIALOG_SUMMARY,
        ResourceBundleUtil.getMessageFormBundle( MSG_INFO_TITLE ) + ERROR_DIALOG_DETAIL,
        StringEscapeUtils.escapeJavaScript( ResourceBundleUtil.getMessageFormBundle( message ) ), ERROR_DIALOG_END ) );
  }

  public void validationFail()
  {
    RequestContext.getCurrentInstance().addCallbackParam( FAIL, true );
  }

  protected HttpSession getSession()
  {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return (HttpSession) facesContext.getExternalContext().getSession( false );
  }

  public void setAttribute( String name, Object value )
  {
    HttpSession session = getSession();
    if( session != null )
    {
      session.setAttribute( name, value );
    }
  }

  public Object getAttribute( String name )
  {
    HttpSession session = getSession();
    Object obj = null;
    if( session != null )
    {
      obj = session.getAttribute( name );
    }
    return obj;
  }

  public void removeAttribute( String name )
  {
    HttpSession session = getSession();
    if( session != null )
    {
      session.removeAttribute( name );
    }
  }

  public void fillSessionData( AbstractTO abstractTO )
  {
    if( abstractTO != null && getAttribute( USER_ATTRIBUTE ) != null )
    {
      UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
      if( userTO != null )
      {
        abstractTO.setUserId( userTO.getId() );
        abstractTO.setUsername( userTO.getUsername() );
        abstractTO.setTimestamp( Calendar.getInstance().getTime() );
      }
    }
  }

  public Long getUserId()
  {
    Long userId = null;
    if( getAttribute( USER_ATTRIBUTE ) != null )
    {
      UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
      if( userTO != null )
      {
        userId = userTO.getId();
      }
    }
    return userId;
  }
  
  public UIComponent getUIComponent( String id )
  {
    return FacesContext.getCurrentInstance().getViewRoot().findComponent( id );
  }

}
