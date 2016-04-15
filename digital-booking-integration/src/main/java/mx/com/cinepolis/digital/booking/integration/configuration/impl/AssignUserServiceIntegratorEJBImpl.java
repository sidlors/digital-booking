package mx.com.cinepolis.digital.booking.integration.configuration.impl;

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
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.configuration.AssignUserServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.configuration.AssignUserServiceEJB;

/**
 * Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.integration.configuration.AssignUserServiceIntegratorEJB}
 * 
 * @author kperez
 */
@Stateless
@Local(value = AssignUserServiceIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AssignUserServiceIntegratorEJBImpl implements AssignUserServiceIntegratorEJB
{

  @EJB
  private AssignUserServiceEJB assignUserServiceEJB;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserTO> getAllUsersActive()
  {
    return assignUserServiceEJB.getAllUsersActive();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getTheatersAvailable( Long regionId, AbstractTO abstractTO )
  {
    return assignUserServiceEJB.getTheatersAvailable( regionId, abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assignRegionsUser( UserTO userTO )
  {
    assignUserServiceEJB.assignRegionsUser( userTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void assignTheatersUser( UserTO userTO )
  {
    assignUserServiceEJB.assignTheatersUser( userTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getRegionsAvailable( Long userId )
  {
    return assignUserServiceEJB.getRegionsAvailable( userId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getRegionsAssociated( Long userId )
  {
    return assignUserServiceEJB.getRegionsAssociated( userId );
  }
  
  @Override
  public List<CatalogTO> getTheatersAssociated( Long regionId, AbstractTO abstractTO  )
  {
    return assignUserServiceEJB.getTheatersAssociated( regionId, abstractTO );
  }
}
