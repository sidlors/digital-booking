package mx.com.cinepolis.digital.booking.commons.utils;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Clase de utiler&iacute;a para la clase de AbstractTO
 * 
 * @author jchavez
 * @since 0.1.0
 */
public final class AbstractTOUtils
{
  /**
   * Se hace privado el contructor para que no se inicialice la clase
   */
  private AbstractTOUtils()
  {
    // Clase de utiler&iacute;a
  }

  /**
   * M&eacute;todo que copia la firma electr&oacute;nica de objetos AbstractTO a otro.
   * 
   * @param source TO con la informaci&oacute;n de la firma electr&oacute;nica
   * @param destination TO al cual se quiere pasar la firma
   * @author jchavez
   * @since 0.1.0
   */
  public static void copyElectronicSign( AbstractTO source, AbstractTO destination )
  {
    destination.setTimestamp( source.getTimestamp() );
    destination.setUserId( source.getUserId() );
    destination.setUsername( source.getUsername() );
  }

  /**
   * M&eacute;todo que copia la firma electr&oacute;nica de objetos AbstractTO a una lista de AbstractTO.
   * 
   * @param source TO con la informaci&oacute;n de la firma electr&oacute;nica
   * @param destinations Lista de TOs al cual se quiere pasar la firma
   * @author jchavez
   * @since 0.1.0
   */
  public static void copyElectronicSign( AbstractTO source, List<? extends AbstractTO> destinations )
  {
    if( CollectionUtils.isNotEmpty( destinations ) )
    {
      for( AbstractTO abstractTO : destinations )
      {
        copyElectronicSign( source, abstractTO );
      }
    }
  }
}
