package mx.com.cinepolis.digital.booking.integration.security.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB;

/**
 * Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB}
 * 
 * @author gsegura
 */
@Stateless
@Local(value = ServiceSecurityIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceSecurityIntegratorEJBImpl implements ServiceSecurityIntegratorEJB
{

  @EJB
  private ServiceSecurityEJB serviceSecurityEJB;

  @Override
  public void authenticate( UserTO userTO )
  {
    serviceSecurityEJB.authenticate( userTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB#findUserDatail(java.lang.String)
   */
  @Override
  public UserTO findUserDatail( String username )
  {
    return serviceSecurityEJB.findUserDetail( username );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB#findMenuByUser(java.lang.Long)
   */
  @Override
  public SystemMenuTO findMenuByUser( Long idUser )
  {
    return serviceSecurityEJB.findMenuByUser( idUser );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB#isSystemMenuAllowed(java.lang
   * .String, java.util.List)
   */
  @Override
  public boolean isSystemMenuAllowed( String url, List<CatalogTO> userRoles )
  {
    return serviceSecurityEJB.isSystemMenuAllowed( url, userRoles );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB#clearJPACache()
   */
  @Override
  public void clearJPACache()
  {
    serviceSecurityEJB.clearJPACache();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB#hasConfigPage(java.lang.Long)
   */
  @Override
  public boolean hasConfigPage( Long idUser )
  {
    return serviceSecurityEJB.hasConfigPage( idUser );
  }

  /*
   * (non-Javadoc)
   * 
   */
  @Override
  public void logOut( UserTO userTO )
  {
    serviceSecurityEJB.logOut( userTO );
  }
 
}
