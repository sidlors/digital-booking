package mx.com.cinepolis.digital.booking.integration.synchronize;

import mx.com.cinepolis.digital.booking.commons.constants.Language;

/**
 * Interface que define los servicios que realizarán la sincronización de los catálogos.
 * 
 * @author shernandez
 */
public interface ServiceDataSynchronizerIntegratorEJB
{
  /**
   * Sincronización del catálogo de Distribuidores.
   * 
   * @param date
   */
  void synchronizeDistributors();

  /**
   * Sincronización de los paises.
   * 
   * @param language TODO
   */
  void synchronizeCountries( Language language );

  /**
   * Sincronización de las ciudades pertenecientes al pais indicado
   * 
   * @param countryId
   * @param language TODO
   */
  void synchronizeCities( Long countryId, Language language );

  /**
   * Sincronización de los estados pertenecientes al país indicado
   * 
   * @param countryId
   * @param language TODO
   */
  void synchronizeStates( Long countryId, Language language );

  /**
   * Sincronización de las películas que serán estrenos.
   * 
   * @param language TODO
   * @param date
   */
  void synchronizeEventMovies();

  /**
   * Sincronización de los cines nuevos por ciudad
   * 
   * @param language TODO
   * @param date
   */
  void synchronizeTheaters( Language language );

}
