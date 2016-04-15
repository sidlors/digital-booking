package mx.com.cinepolis.digital.booking.integration.synchronize.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB;

/**
 * Clase que implementa los métodos de sincronización de catálogos.
 * 
 * @author shernandezl
 */
@Stateless
@Local(value=ServiceDataSynchronizerIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceDataSynchronizerIntegratorEJBImpl implements ServiceDataSynchronizerIntegratorEJB
{
  
  @EJB
  private ServiceDataSynchronizerEJB serviceDataSynchronizerEJB;

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeDistributors
   * ()
   */
  @Override
  public void synchronizeDistributors()
  {
    serviceDataSynchronizerEJB.synchronizeDistributors();
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeCountries
   * (mx.com.cinepolis.digital.booking.model.constants.Language)
   */
  @Override
  public void synchronizeCountries( Language language )
  {
    serviceDataSynchronizerEJB.synchronizeCountries( language );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeCities
   * (java.lang.Long, mx.com.cinepolis.digital.booking.model.constants.Language)
   */
  @Override
  public void synchronizeCities( Long countryId, Language language )
  {
    serviceDataSynchronizerEJB.synchronizeCities( countryId, language );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeStates
   * (java.lang.Long, mx.com.cinepolis.digital.booking.model.constants.Language)
   */
  @Override
  public void synchronizeStates( Long countryId, Language language )
  {
    serviceDataSynchronizerEJB.synchronizeStates( countryId, language );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeEventMovies
   * ()
   */
  @Override
  public void synchronizeEventMovies()
  {
    serviceDataSynchronizerEJB.synchronizeEventMovies();
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB#synchronizeTheaters
   * (mx.com.cinepolis.digital.booking.model.constants.Language)
   */
  @Override
  public void synchronizeTheaters( Language language )
  {
    serviceDataSynchronizerEJB.synchronizeTheaters( language );
  }

}
