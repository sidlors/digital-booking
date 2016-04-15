package mx.com.cinepolis.digital.booking.integration.theater;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;

/**
 * Interface que define los servicios de integracion de la administracion del cine
 * 
 * @author rgarcia
 */
public interface ServiceAdminTheaterIntegratorEJB
{

  /**
   * Método que se encarga de registrar la información del cine
   * 
   * @param theaterTO
   */
  void saveTheater( TheaterTO theaterTO );

  /**
   * Método que se encarga de eliminar la información del cine
   * 
   * @param theaterTO
   */
  void deleteTheater( TheaterTO theaterTO );

  /**
   * Método que se encarga de actualizar la información del cine
   * 
   * @param theaterTO
   */
  void updateTheater( TheaterTO theaterTO );

  /**
   * Método que se encarga de obtener el catálogo completo de los cines
   * 
   * @return
   */
  List<TheaterTO> getAllTheaters();

  /**
   * Método que se encarga de obtener secciones por páginas del catálogo de cines
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> getCatalogTheaterSummary( PagingRequestTO pagingRequestTO );

  /**
   * Método que regrega los tipos de formato de película
   * 
   * @return
   */
  List<CatalogTO> getMovieFormats();

  /**
   * Método que regresa los tipos de formato de sonido
   * 
   * @return
   */
  List<CatalogTO> getSoundFormats();

  /**
   * Método que regresa los tipos de formato de sonido
   * 
   * @return
   */
  List<CatalogTO> getScreenFormats();

  /**
   * Método que regresa los cines asociados a una región
   * 
   * @param region
   * @return
   */
  List<TheaterTO> getTheatersByRegionId( CatalogTO region );

  /**
   * Método que obtiene un cine por id
   * 
   * @param theaterIdSelected
   * @return
   */
  TheaterTO getTheater( Long theaterIdSelected );

  /**
   * Method to get an income setting type by id.
   * 
   * @param idIndicatorType with the id of the income setting type requested.
   * @param language with the language requested.
   * @return Object of {@link IncomeSettingsTypeTO} with the income setting type information.
   * @author jreyesv
   */
  IncomeSettingsTypeTO getIndicatorTypeById( int idIndicatorType, Language language );

}
