package mx.com.cinepolis.digital.booking.integration.category;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface que define  los metodos para administrar
 * Screen Formats
 * @author kperez
 *
 */
public interface ServiceAdminScreenFormatIntegratorEJB
{

  /**
   * Salva una Screen Format
   * @param category
   */
  void saveScreenFormat(CatalogTO screenFormat);
  
  /**
   * Elimina una categoria de tipo screen format
   * @param category
   */
  void deleteScreenFormat( CatalogTO screenFormat );

  /**
   * Actualiza una screen format
   * @param category
   */
  void updateScreenFormat( CatalogTO screenFormat );

  /**
   * Realiza la busqueda del catalogo de screen formats
   * @param pagingRequestTO
   * @return PagingResponseTO
   */
  PagingResponseTO<CatalogTO> getCatalogScreenFormatSumary( PagingRequestTO pagingRequestTO);

  /**
   * Obtiene todas las categorias de tipo screen formats
   * @return PagingResponseTO
   */
  PagingResponseTO<CatalogTO> getScreenFormatAll();

  
}
