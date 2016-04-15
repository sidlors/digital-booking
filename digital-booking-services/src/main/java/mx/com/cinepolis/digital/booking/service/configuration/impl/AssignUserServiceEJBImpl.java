package mx.com.cinepolis.digital.booking.service.configuration.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.RegionDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TheaterTOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.UserDOToUserTOTransformer;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.service.configuration.AssignUserServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class that implements the user assignment services.
 * 
 * @author kperez
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class AssignUserServiceEJBImpl implements AssignUserServiceEJB
{

  /**
   * UserDAO
   */
  @EJB
  private UserDAO userDAO;

  /**
   * TheaterDAO
   */
  @EJB
  private TheaterDAO theaterDAO;

  /**
   * RegionDAO
   */
  @EJB
  private RegionDAO regionDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserTO> getAllUsersActive()
  {
    List<UserDO> userDOs = userDAO.findAll();
    List<UserDO> usersActive = new ArrayList<UserDO>();
    for( UserDO userDO : userDOs )
    {
      if( userDO.isFgActive() )
      {
        usersActive.add( userDO );
      }
    }
    List<UserTO> userTOs = new ArrayList<UserTO>();
    CollectionUtils.collect( usersActive, new UserDOToUserTOTransformer(), userTOs );
    Collections.sort( userTOs );
    return userTOs;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getTheatersAvailable( Long regionId, AbstractTO abstractTO )
  {
    List<TheaterTO> theaterTOs = theaterDAO.findByRegionId( new CatalogTO(regionId),
      Language.fromId( abstractTO.getIdLanguage().intValue() ) );
    UserDO userDO = userDAO.find( abstractTO.getUserId().intValue() );
    List<TheaterDO> userTheaterDOs = userDO.getTheaterDOList();
    List<TheaterTO> userTheaterTOs = new ArrayList<TheaterTO>();
    CollectionUtils.collect( userTheaterDOs, new TheaterDOToTheaterTOTransformer(), userTheaterTOs );
    List<TheaterTO> theatersAvailable = (List<TheaterTO>) CollectionUtils.subtract( theaterTOs, userTheaterTOs );
    List<CatalogTO> catalogTheaterTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( theatersAvailable, new TheaterTOToCatalogTOTransformer(), catalogTheaterTOs );
    Collections.sort( catalogTheaterTOs, new CatalogTOComparator( false ) );
    return catalogTheaterTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assignRegionsUser( UserTO userTO )
  {
    if( userTO.getRegions() != null )
    {
      // Se obtiene la información original del usuario
      UserDO userDO = userDAO.find( userTO.getId().intValue() );
      List<CatalogTO> regions = userTO.getRegions();
      UserDOToUserTOTransformer transformer = new UserDOToUserTOTransformer();
      UserTO userTOclone = (UserTO) transformer.transform( userDO );
      // Se le asigna las nuevas regiones
      userTOclone.setRegions( regions );
      // Se actualiza esta información
      userDAO.update( userTOclone );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assignTheatersUser( UserTO userTO )
  {
    if( userTO.getTheaters() != null )
    {
      // Se obtiene la información original del usuario
      UserDO userDO = userDAO.find( userTO.getId().intValue() );
      List<CatalogTO> theaters = userTO.getTheaters();
      UserDOToUserTOTransformer transformer = new UserDOToUserTOTransformer();
      UserTO userTOclone = (UserTO) transformer.transform( userDO );
      // Se le asigna los nuevos cines al usuario
      userTOclone.setTheaters( theaters );
      userTOclone.setRegionSelected( userTO.getRegionSelected() );
      // Se actualiza esta información
      userDAO.update( userTOclone );
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getRegionsAvailable( Long userId )
  {
    UserDO userDO = userDAO.find( userId.intValue() );
    List<RegionDO> userRegionDOs = userDO.getRegionDOList();
    List<RegionDO> regionDOs = regionDAO.findActiveRegions();
    List<RegionDO> regionsAvailable = (List<RegionDO>) CollectionUtils.subtract( regionDOs, userRegionDOs );
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( regionsAvailable, new RegionDOToCatalogTOTransformer(), catalogTOs );
    Collections.sort( catalogTOs, new CatalogTOComparator( false ) );
    return catalogTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getRegionsAssociated( Long userId )
  {
    UserDO userDO = userDAO.find( userId.intValue() );
    List<RegionDO> userRegionDOs = userDO.getRegionDOList();
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( userRegionDOs, new RegionDOToCatalogTOTransformer(), catalogTOs );
    Collections.sort( catalogTOs, new CatalogTOComparator( false ) );
    return catalogTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getTheatersAssociated( Long regionId, AbstractTO abstractTO )
  {
    List<TheaterTO> theaterTOs = theaterDAO.findByRegionId( new CatalogTO( regionId ),
      Language.fromId( abstractTO.getIdLanguage().intValue() ) );
    UserDO userDO = userDAO.find( abstractTO.getUserId().intValue() );
    List<TheaterDO> userTheaterDOs = userDO.getTheaterDOList();
    List<TheaterTO> userTheaterTOs = new ArrayList<TheaterTO>();
    CollectionUtils.collect( userTheaterDOs, new TheaterDOToTheaterTOTransformer(), userTheaterTOs );
    List<TheaterTO> theaterAvailableTOs = new ArrayList<TheaterTO>();
    for( TheaterTO theaterAvailable : theaterTOs )
    {
      for( TheaterTO theaterAssociated : userTheaterTOs )
      {
        if( theaterAvailable.getId().equals( theaterAssociated.getId() ) )
        {
          theaterAvailableTOs.add( theaterAvailable );
        }
      }
    }
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( theaterAvailableTOs, new TheaterTOToCatalogTOTransformer(), catalogTOs );
    Collections.sort( catalogTOs, new CatalogTOComparator( false ) );
    return catalogTOs;
  }

}
