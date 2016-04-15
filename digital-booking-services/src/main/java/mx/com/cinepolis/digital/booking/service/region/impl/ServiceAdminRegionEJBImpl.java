package mx.com.cinepolis.digital.booking.service.region.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.RegionQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.commons.utils.RegionTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.CityDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CountryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.RegionDOToRegionTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.StateDOToStateTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TerritoryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO;
import mx.com.cinepolis.digital.booking.service.region.ServiceAdminRegionEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.RegionTOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Clase que implementa los metodos asociados a la administración de las regiones de los cines
 * 
 * @author rgarcia
 */
/**
 * @author rgarcia
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminRegionEJBImpl implements ServiceAdminRegionEJB
{

  @EJB
  private RegionDAO regionDAO;
  @EJB
  private TerritoryDAO territoryDAO;
  @EJB
  private CountryDAO countryDAO;
  @EJB
  private StateDAO stateDAO;
  @EJB
  private CityDAO cityDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB
   * #saveRegion(mx.com.cinepolis.digital.booking.model.to.TheaterTO)
   */
  @Override
  public void saveRegion( RegionTO<CatalogTO, CatalogTO> regionTO )
  {
    ValidatorUtil.validateCatalog( regionTO.getCatalogRegion() );
    validateTerritory( regionTO );
    if( CollectionUtils.isEmpty( regionDAO.findByDsNameActive( regionTO.getCatalogRegion().getName() ) ) )
    {
      regionDAO.save( regionTO );
      regionDAO.save( regionTO, Language.SPANISH );
    }
    else
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
    }
  }

  /**
   * Metodo que se encarga de validar que el territorio no sea nulo
   * 
   * @param regionTO
   */
  private void validateTerritory( RegionTO<CatalogTO, CatalogTO> regionTO )
  {
    if( regionTO.getIdTerritory() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_TERRITORY.getId() );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB
   * #deleteRegion(mx.com.cinepolis.digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void deleteRegion( RegionTO<CatalogTO, CatalogTO> regionTO )
  {
    ValidatorUtil.validateCatalog( regionTO.getCatalogRegion() );
    validateTerritory( regionTO );
    RegionDO regionDO = regionDAO.find( regionTO.getCatalogRegion().getId().intValue() );
    if( regionDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_REGION.getId() );
    }
    List<TheaterDO> theathers = regionDO.getTheaterDOList();
    // Si la region tiene zonas
    if( theathers != null && !theathers.isEmpty() )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_DELETE_REGION.getId() );
    }
    regionDAO.delete( regionTO );

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB
   * #updateRegion(mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, Long>)
   */
  @Override
  public void updateRegion( RegionTO<CatalogTO, CatalogTO> regionTO )
  {
    ValidatorUtil.validateCatalog( regionTO.getCatalogRegion() );
    validateTerritory( regionTO );
    List<RegionDO> regions = regionDAO.findByDsNameActive( regionTO.getCatalogRegion().getName() );
    for( RegionDO region : regions )
    {
      if( !region.getIdRegion().equals( regionTO.getCatalogRegion().getId().intValue() ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
      }
    }
    RegionDO regionDO = regionDAO.find( regionTO.getCatalogRegion().getId().intValue() );
    if( regionDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_REGION.getId() );
    }
    regionDAO.update( regionTO );
    regionDAO.update( regionTO, Language.SPANISH );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB #getAllRegions()
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<RegionTO<CatalogTO, CatalogTO>> getAllRegions()
  {
    List<RegionDO> regions = new ArrayList<RegionDO>();
    regions.addAll( CollectionUtils.select(
      regionDAO.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( Boolean.TRUE ) ) ) );

    List<RegionTO<CatalogTO, CatalogTO>> catalogs = new ArrayList<RegionTO<CatalogTO, CatalogTO>>();
    if( regions != null )
    {
      CollectionUtils.collect( regions, new RegionDOToRegionTOTransformer(), catalogs );
    }
    Collections.sort( catalogs, new RegionTOComparator( false ) );
    return catalogs;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB
   * #getCatalogRegionSummary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> getCatalogRegionSummary( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    return regionDAO.findAllByPaging( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.region. ServiceAdminRegionEJB #getAllTerritries()
   */
  @Override
  public List<CatalogTO> getAllTerritories()
  {
    List<TerritoryDO> territoryDOs = territoryDAO.findAll();
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( territoryDOs, new TerritoryDOToCatalogTOTransformer(), catalogTOs );
    return catalogTOs;
  }

  @Override
  public List<CatalogTO> getAllCountries()
  {
    List<CatalogTO> countries = new ArrayList<CatalogTO>();
    List<CountryDO> countriesDO = countryDAO.findAll();
    if( countriesDO != null && !countriesDO.isEmpty() )
    {
      // TODO Quitar el idioma cuando se resuelva el null pointer del constructor sin parametros
      CollectionUtils.collect( countriesDO, new CountryDOToCatalogTOTransformer( Language.ENGLISH ), countries );
    }
    return countries;
  }

  @Override
  public List<StateTO<CatalogTO, Number>> getAllStates()
  {
    List<StateTO<CatalogTO, Number>> states = new ArrayList<StateTO<CatalogTO, Number>>();
    List<StateDO> statesDO = stateDAO.findAll();
    if( statesDO != null && !statesDO.isEmpty() )
    {
      CollectionUtils.collect( statesDO, new StateDOToStateTOTransformer(), states );
    }
    return states;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<RegionTO<CatalogTO, CatalogTO>> getAllRegionsByTerritory( CatalogTO territory )
  {
    List<RegionTO<CatalogTO, CatalogTO>> regions = new ArrayList<RegionTO<CatalogTO, CatalogTO>>();
    List<RegionDO> regionsDO = regionDAO.findAll();
    if( regionsDO != null && !regionsDO.isEmpty() )
    {
      regionsDO = (List<RegionDO>) CollectionUtils.select(
        regionsDO,
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
          PredicateUtils.equalPredicate( true ) ) );
      CollectionUtils.collect( regionsDO, new RegionDOToRegionTOTransformer(), regions );
    }
    List<RegionTO<CatalogTO, CatalogTO>> regionsByTerritory = new ArrayList<RegionTO<CatalogTO, CatalogTO>>();
    for( RegionTO<CatalogTO, CatalogTO> region : regions )
    {
      if( region.getIdTerritory().getId().equals( territory.getId() ) )
      {
        regionsByTerritory.add( region );
      }
    }
    Collections.sort( regionsByTerritory, new RegionTOComparator( false ) );

    return regionsByTerritory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getAllCities()
  {
    List<CatalogTO> cities = new ArrayList<CatalogTO>();
    List<CityDO> citiesDO = new ArrayList<CityDO>();
    citiesDO.addAll( CollectionUtils.select(
      cityDAO.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( true ) ) ) );
    CollectionUtils.collect( citiesDO, new CityDOToCatalogTOTransformer(), cities );
    Collections.sort( cities, new CatalogTOComparator( false ) );
    return cities;
  }

  @Override
  public List<StateTO<CatalogTO, Number>> getAllStatesByCountry( CatalogTO country )
  {
    List<StateTO<CatalogTO, Number>> states = new ArrayList<StateTO<CatalogTO, Number>>();
    List<StateTO<CatalogTO, Number>> allstates = this.getAllStates();
    for( StateTO<CatalogTO, Number> stateTO : allstates )
    {
      if( stateTO.getCatalogCountry().longValue() == country.getId().longValue() )
      {
        states.add( stateTO );
      }
    }
    return states;
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB#findAllActiveRegions(mx.com.cinepolis.digital.booking
   * .model.to.AbstractTO)
   */
  @Override
  public List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO )
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    AbstractTOUtils.copyElectronicSign( abstractTO, pagingRequestTO );
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_ACTIVE, true );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_LANGUAGE_ID, abstractTO.getIdLanguage() );
    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> responseTO = regionDAO.findAllByPaging( pagingRequestTO );
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( responseTO.getElements(), new RegionTOToCatalogTOTransformer(), catalogTOs );
    return catalogTOs;
  }

  @Override
  public List<CatalogTO> getAllCitiesByState( Long idState )
  {
    List<CatalogTO> cities = new ArrayList<CatalogTO>();
    List<CityDO> citiesDO = cityDAO.findByIdState( idState );
    CollectionUtils.collect( citiesDO, new CityDOToCatalogTOTransformer(), cities );
    Collections.sort( cities, new CatalogTOComparator( false ) );
    return cities;
  }

}
