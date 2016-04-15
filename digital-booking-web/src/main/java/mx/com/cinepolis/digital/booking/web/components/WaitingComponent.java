package mx.com.cinepolis.digital.booking.web.components;

import javax.faces.application.ResourceDependencies;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import org.primefaces.component.api.Widget;

@ResourceDependencies({ @javax.faces.application.ResourceDependency(library = "primefaces", name = "primefaces.css"),
    @javax.faces.application.ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
    @javax.faces.application.ResourceDependency(library = "primefaces", name = "primefaces.js"),
    @javax.faces.application.ResourceDependency(library = "components", name = "waitingdialog.js") })
public class WaitingComponent extends UIComponentBase implements Widget
{
  public static final String COMPONENT_TYPE = "mx.com.cinepolis.digital.booking.components.WaitingComponent";
  public static final String COMPONENT_FAMILY = "mx.com.cinepolis.digital.booking.components";
  private static final String DEFAULT_RENDERER = "mx.com.cinepolis.digital.booking.components.WaitingComponentRenderer";

  public WaitingComponent()
  {
    setRendererType( DEFAULT_RENDERER );
  }

  @Override
  public String getFamily()
  {
    return COMPONENT_FAMILY;
  }

  @Override
  public String resolveWidgetVar()
  {
    FacesContext context = getFacesContext();
    String userWidgetVar = (String) getAttributes().get( "widgetVar" );

    if( userWidgetVar != null )
    {
      return userWidgetVar;
    }
    return "widget_" + getClientId( context ).replaceAll( new StringBuilder().append( "-|" )
        .append( UINamingContainer.getSeparatorChar( context ) )
        .toString(), "_" );
  }

  protected enum PropertyKeys
  {
    widgetVar, components
  }

  public String getWidgetVar()
  {
    return (String) getStateHelper().eval( PropertyKeys.widgetVar, null );
  }

  public void setWidgetVar( String widgetVar )
  {
    getStateHelper().put( PropertyKeys.widgetVar, widgetVar );
  }

  public String getComponents()
  {
    return ((String) getStateHelper().eval( PropertyKeys.components, null ));
  }

  public void setComponents( String components )
  {
    getStateHelper().put( PropertyKeys.components, components );
  }
}
