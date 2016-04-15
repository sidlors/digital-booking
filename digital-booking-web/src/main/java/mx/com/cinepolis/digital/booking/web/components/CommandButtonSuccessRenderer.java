package mx.com.cinepolis.digital.booking.web.components;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.commandbutton.CommandButtonRenderer;
import org.primefaces.context.RequestContext;
import org.primefaces.util.CSVBuilder;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

public class CommandButtonSuccessRenderer extends CommandButtonRenderer
{
  public void encodeEnd( FacesContext context, UIComponent component ) throws IOException
  {
    CommandButtonSuccess button = (CommandButtonSuccess) component;
    encodeMarkup( context, button );
    encodeScript( context, button );
  }

  protected void encodeMarkup( FacesContext context, CommandButtonSuccess button ) throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    String clientId = button.getClientId( context );
    String type = button.getType();
    Object value = button.getValue();
    String icon = button.resolveIcon();
    RequestContext requestContext = RequestContext.getCurrentInstance();
    boolean csvEnabled = (requestContext.getApplicationContext()
        .getConfig().isClientSideValidationEnabled()) && (button.isValidateClient());

    StringBuilder onclick = new StringBuilder();
    if( button.getOnclick() != null )
    {
      onclick.append( button.getOnclick() )
          .append( ";" );
    }

    String onclickBehaviors = getEventBehaviors( context, button, "click" );
    if( onclickBehaviors != null )
    {
      onclick.append( onclickBehaviors );
    }

    writer.startElement( "button", button );
    writer.writeAttribute( "id", clientId, "id" );
    writer.writeAttribute( "name", clientId, "name" );
    writer.writeAttribute( "class", button.resolveStyleClass(), "styleClass" );

    if( (!(type.equals( "reset" ))) && (!(type.equals( "button" ))) )
    {
      boolean ajax = button.isAjax();
      String request;
      if( ajax )
      {
        request = buildAjaxRequest( context, button, null );
      }
      else
      {
        UIComponent form = ComponentUtils.findParentForm( context, button );
        if( form == null )
        {
          throw new FacesException( "CommandButton : \"" + clientId + "\" must be inside a form element" );
        }

        request = buildNonAjaxRequest( context, button, form, null, false );
      }

      if( csvEnabled )
      {
        CSVBuilder csvb = requestContext.getCSVBuilder();
        request = csvb.init()
            .source( "this" )
            .ajax( ajax )
            .process( button, button.getProcess() )
            .update( button, button.getUpdate() )
            .command( request )
            .build();
      }

      onclick.append( request );
    }

    if( onclick.length() > 0 )
    {
      if( button.requiresConfirmation() )
      {
        writer.writeAttribute( "data-pfconfirmcommand", onclick.toString(), null );
        writer.writeAttribute( "onclick", button.getConfirmationScript(), "onclick" );
      }
      else if( button.requiresSuccessMsg() )
      {
        writer.writeAttribute( "onclick", button.getSuccessMsgScript() + onclick.toString(), "onclick" );
      }
      else
      {
        writer.writeAttribute( "onclick", onclick.toString(), "onclick" );
      }
    }
    renderPassThruAttributes( context, button, HTML.BUTTON_ATTRS, HTML.CLICK_EVENT );

    if( button.isDisabled() )
      writer.writeAttribute( "disabled", "disabled", "disabled" );
    if( button.isReadonly() )
      writer.writeAttribute( "readonly", "readonly", "readonly" );

    if( (icon != null) && (!(icon.trim().equals( "" ))) )
    {
      String defaultIconClass = (button.getIconPos().equals( "left" )) ? "ui-button-icon-left ui-icon ui-c"
          : "ui-button-icon-right ui-icon ui-c";
      String iconClass = defaultIconClass + " " + icon;

      writer.startElement( "span", null );
      writer.writeAttribute( "class", iconClass, null );
      writer.endElement( "span" );
    }

    writer.startElement( "span", null );
    writer.writeAttribute( "class", "ui-button-text ui-c", null );

    if( value == null )
    {
      writer.write( "ui-button" );
    }
    else if( button.isEscape() )
      writer.writeText( value, "value" );
    else
    {
      writer.write( value.toString() );
    }

    writer.endElement( "span" );

    writer.endElement( "button" );
  }
}
