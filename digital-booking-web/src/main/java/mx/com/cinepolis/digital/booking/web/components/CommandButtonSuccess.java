package mx.com.cinepolis.digital.booking.web.components;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.primefaces.component.commandbutton.CommandButton;

@ResourceDependencies({ @ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @ResourceDependency(library = "primefaces", name = "primefaces.js") })
public class CommandButtonSuccess extends CommandButton implements Successable
{
  private String confirmationScript;
  public static final String COMPONENT_TYPE = "mx.com.cinepolis.digital.booking.components.CommandButtonSuccess";
  public static final String COMPONENT_FAMILY = "mx.com.cinepolis.digital.booking.components";
  private static final String DEFAULT_RENDERER = "mx.com.cinepolis.digital.booking.components.CommandButtonSuccessRenderer";

  public CommandButtonSuccess()
  {
    setRendererType( DEFAULT_RENDERER );
  }

  public String getFamily()
  {
    return COMPONENT_FAMILY;
  }

  @Override
  public boolean requiresSuccessMsg()
  {
    return this.confirmationScript != null;
  }

  @Override
  public void setSuccessMsgScript( String paramString )
  {
    this.confirmationScript = paramString;
  }

  @Override
  public String getSuccessMsgScript()
  {
    return this.confirmationScript;
  }

}
