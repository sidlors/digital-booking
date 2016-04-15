package mx.com.cinepolis.digital.booking.integration.category;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface que define  los metodos para administrar
 * Sound Formats
 * @author agustin.ramirez
 *
 */
public interface ServiceAdminSoundFormatIntegratorEJB {
	
	/**
	   * Salva una Sound Format
	   * @param category
	   */
		void saveSoundFormat(CatalogTO soundFormat);
	  
	  /**
	   * Elimina una categoria
	   * @param category
	   */
	  void deleteSoundFormat( CatalogTO soundFormat );

	  /**
	   * Actualiza una movie format
	   * @param category
	   */
	  void updateSoundFormat( CatalogTO soundFormat );

	  /**
	   * Realiza la busqueda
	   * @param pagingRequestTO
	   * @return
	   */
	  PagingResponseTO<CatalogTO> getCatalogSoundFormatSumary( PagingRequestTO pagingRequestTO);

	  /**
	   * Obtiene todas los movie formats
	   * @param pagingRequestTO
	   * @return
	   */
	  //TODO se necesita implementar el NeedsPaging para su correcto funcionamiento 
	  PagingResponseTO<CatalogTO> getSoundFormatAll();

}
