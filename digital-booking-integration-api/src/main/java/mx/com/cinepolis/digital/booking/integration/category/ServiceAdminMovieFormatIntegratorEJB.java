package mx.com.cinepolis.digital.booking.integration.category;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface que define los sevricios para la adminitracion
 * de movie formats
 * @author agustin.ramirez
 *
 */
public interface ServiceAdminMovieFormatIntegratorEJB {
	
	/**
	   * Salva una Movie Format
	   * @param category
	   */
		void saveMovieFormat(CatalogTO movieFormat);
	  
	  /**
	   * Elimina una categoria
	   * @param category
	   */
	  void deleteMovieFormat( CatalogTO movieFormat );

	  /**
	   * Actualiza una movie format
	   * @param category
	   */
	  void updateMovieFormat( CatalogTO movieFormat );

	  /**
	   * Realiza la busqueda
	   * @param pagingRequestTO
	   * @return
	   */
	  PagingResponseTO<CatalogTO> getCatalogMovieFormatSumary( PagingRequestTO pagingRequestTO);

	  /**
	   * Obtiene todas los movie formats
	   * @param pagingRequestTO
	   * @return
	   */
	  //TODO se necesita implementar el NeedsPaging para su correcto funcionamiento 
	  PagingResponseTO<CatalogTO> getMovieFormatAll();

}
