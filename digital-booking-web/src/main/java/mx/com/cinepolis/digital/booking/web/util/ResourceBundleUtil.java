package mx.com.cinepolis.digital.booking.web.util;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Utileria para obtener los mensajes definidos en el bundle
 * 
 * @author agustin.ramirez
 */
public final class ResourceBundleUtil
{
  private ResourceBundleUtil()
  {
  }

  /**
   * Obtiene el mensaje a partir de su definicion en el bundle
   * 
   * @param text
   * @return
   */
  public static String getMessageFormBundle( String text )
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    ResourceBundle msg = fc.getApplication().evaluateExpressionGet( fc, "#{msg}", ResourceBundle.class );
    return msg.getString( "digitalbooking." + text );
  }

}
