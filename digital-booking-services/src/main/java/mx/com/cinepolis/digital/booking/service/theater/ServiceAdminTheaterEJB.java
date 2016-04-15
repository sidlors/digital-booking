package mx.com.cinepolis.digital.booking.service.theater;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;

/**
 * Interface que define los metodos asociados a la administracion de cines
 * 
 * @author rgarcia
 */
@Local
public interface ServiceAdminTheaterEJB
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
   * Método que regresa los tipos de formato de pantalla/sala
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
   * Método que regresa un cine por id
   * 
   * @param theaterIdSelected
   * @return
   */
  TheaterTO getTheater( Long theaterIdSelected );

  List<TheaterTO> getMyTheaters( AbstractTO abstractTO );

  List<TheaterTO> getMyTheaters( AbstractTO abstractTO, Language language );

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
