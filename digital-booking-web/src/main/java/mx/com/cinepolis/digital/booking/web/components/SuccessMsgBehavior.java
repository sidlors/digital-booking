package mx.com.cinepolis.digital.booking.web.components;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import mx.com.cinepolis.digital.booking.web.util.ResourceBundleUtil;

public class SuccessMsgBehavior extends ClientBehaviorBase
{
  private String successurl;

  public String getScript( ClientBehaviorContext behaviorContext )
  {
    FacesContext context = behaviorContext.getFacesContext();
    UIComponent component = behaviorContext.getComponent();
    String source = component.getClientId( context );

    if( component instanceof Successable )
    {
      String script = "DigitalBookingUtil.showMessageInDialog({source:'" + source + "',url:'"
          + StringUtils.defaultIfBlank( getSuccessurl(), "" ) + "',summary:'"
          + ResourceBundleUtil.getMessageFormBundle( "common.msgInfoTitle" ) + "',detail:'"
          + ResourceBundleUtil.getMessageFormBundle( "common.msgInfoText" ) + "'});";
      ((Successable) component).setSuccessMsgScript( script );

      return null;
    }

    throw new FacesException(
        "Component "
            + source
            + " is not a Successable. SuccessMsgBehavior can only be attached to components that implement mx.com.cinepolis.digital.booking.web.components.Successable interface" );
  }

  public String getSuccessurl()
  {
    return successurl;
  }

  public void setSuccessurl( String successurl )
  {
    this.successurl = successurl;
  }
}
