package mx.com.cinepolis.digital.booking.web.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;

@FacesConverter("pickListConverter")
public class PickListConverter implements Converter
{

  @Override
  public Object getAsObject( FacesContext context, UIComponent component, String value )
  {
    CatalogTO catalogTO = null;
    if( value != null )
    {
      catalogTO = new CatalogTO();
      catalogTO.setId( Long.valueOf( value ) );
    }
    return catalogTO;
  }

  @Override
  public String getAsString( FacesContext context, UIComponent component, Object value )
  {
    return value != null ? value.toString() : null;
  }
}
