package mx.com.cinepolis.digital.booking.web.components;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.web.util.ResourceBundleUtil;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.WidgetBuilder;

public class WaitingComponentRenderer extends CoreRenderer
{
  public void encodeEnd( FacesContext context, UIComponent component ) throws IOException
  {
    WaitingComponent waiting = (WaitingComponent) component;
    encodeScript( context, waiting );
  }

  protected void encodeScript( FacesContext context, WaitingComponent waiting ) throws IOException
  {
    String clientId = waiting.getClientId( context );
    WidgetBuilder wb = getWidgetBuilder( context );
    wb.initWithDomReady( "WaitingDialog", waiting.resolveWidgetVar(), clientId );
    wb.attr( "components", waiting.getComponents() );
    wb.attr( "title", ResourceBundleUtil.getMessageFormBundle( "common.waitingText" ) );
    wb.finish();
  }
}
