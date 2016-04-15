package mx.com.cinepolis.digital.booking.service.synchronize;

import javax.ejb.Remote;

import mx.com.cinepolis.digital.booking.commons.constants.Language;

/**
 * Interface que define los metodos para la sincronización de los catálogos
 * 
 * @author rgarcia
 */
@Remote
public interface ServiceDataSynchronizerEJB
{

  /**
   * Se obtiene la lista de las distribuidoras
   * 
   * @param date
   */
  void synchronizeDistributors();

  /**
   * Se obtiene la lista de paises
   * 
   * @param language TODO
   */
  void synchronizeCountries( Language language );

  /**
   * Se obtiene la lista de ciudades pertenecientes al pais indicado
   * 
   * @param countryId
   * @param language TODO
   */
  void synchronizeCities( Long countryId, Language language );

  /**
   * Se obtiene la lista de estados pertenecientes al país indicado
   * 
   * @param countryId
   * @param language TODO
   */
  void synchronizeStates( Long countryId, Language language );

  /**
   * Se obtiene la lista de las películas que serán estrenos.
   * 
   * @param language TODO
   * @param date
   */
  void synchronizeEventMovies();

  /**
   * Se obtiene la lista de los cines nuevos por ciudad
   * 
   * @param language TODO
   * @param date
   */
  void synchronizeTheaters( Language language );

}
