package mx.com.cinepolis.digital.booking.integration.region.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.region.ServiceAdminRegionEJB;

/**
 * Clase que implementa  los servicios de integracion de las regiones
 * @author rgarcia
 *
 */

@Stateless
@Local(value=ServiceAdminRegionIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminRegionIntegratorEJBImpl implements ServiceAdminRegionIntegratorEJB {
	
	@EJB
	private ServiceAdminRegionEJB serviceAdminRegionEJB;

	 /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#saveRegion(mx.com.cinepolis.digital.booking.model.to.RegionTO)
   */
  @Override
  public void saveRegion( RegionTO<CatalogTO, CatalogTO> theaterRegionTO )
  {
    serviceAdminRegionEJB.saveRegion( theaterRegionTO );
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#deleteRegion(mx.com.cinepolis.digital.booking.model.to.RegionTO)
  */
  @Override
  public void deleteRegion( RegionTO<CatalogTO, CatalogTO> theatherRegionTO )
  {
    serviceAdminRegionEJB.deleteRegion( theatherRegionTO );
    
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#updateRegion(mx.com.cinepolis.digital.booking.model.to.RegionTO)
  */
  @Override
  public void updateRegion( RegionTO<CatalogTO, CatalogTO> theatherRegionTO )
  {
    serviceAdminRegionEJB.updateRegion( theatherRegionTO );
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllRegions()
  */
  @Override
  public List<RegionTO<CatalogTO, CatalogTO>> getAllRegions( )
  {
    return serviceAdminRegionEJB.getAllRegions();
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getCatalogRegionSummary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
  */
  @Override
  public PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> getCatalogRegionSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminRegionEJB.getCatalogRegionSummary( pagingRequestTO );
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllTerritories()
  */
  @Override
  public List<CatalogTO> getAllTerritories()
  {
    return serviceAdminRegionEJB.getAllTerritories();
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllCountries()
  */
  @Override
  public List<CatalogTO> getAllCountries()
  {
    return serviceAdminRegionEJB.getAllCountries();
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllStates()
  */
  @Override
  public List<StateTO<CatalogTO, Number>> getAllStates()
  {
    return serviceAdminRegionEJB.getAllStates();
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllRegionsByTerritory(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
  */
  @Override
  public List<RegionTO<CatalogTO, CatalogTO>> getAllRegionsByTerritory( CatalogTO territory )
  {
    return serviceAdminRegionEJB.getAllRegionsByTerritory( territory );
  }

  /*
  * (non-Javadoc)
  * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllCities()
  */
  @Override
  public List<CatalogTO> getAllCities()
  {
    return serviceAdminRegionEJB.getAllCities();
  }

  @Override
  public List<StateTO<CatalogTO, Number>> getAllStatesByCountry( CatalogTO country )
  {
    return serviceAdminRegionEJB.getAllStatesByCountry(country);
  }
  
  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#findAllActiveRegions(mx.com.cinepolis .digital.booking.model.to.AbstractTO)
   */
  @Override
  public List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO )
  {
    return serviceAdminRegionEJB.findAllActiveRegions( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB#getAllCitiesByState(java.lang.Long)
   */
   @Override
   public List<CatalogTO> getAllCitiesByState( Long idState )
   {
     return serviceAdminRegionEJB.getAllCitiesByState( idState );
   }

}
