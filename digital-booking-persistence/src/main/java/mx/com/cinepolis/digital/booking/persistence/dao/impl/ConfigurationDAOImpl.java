package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO}
 * 
 * @author agustin.ramirez
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class ConfigurationDAOImpl extends AbstractBaseDAO<ConfigurationDO> implements ConfigurationDAO
{

  /**
   * Constructor
   */
  public ConfigurationDAOImpl()
  {
    super( ConfigurationDO.class );
  }

  /**
   * Entity Manager
   */
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO#findByParameterName(java.lang.String)
   */
  @Override
  public ConfigurationDO findByParameterName( String parameterName )
  {
    this.em.getEntityManagerFactory().getCache().evict( ConfigurationDO.class );
    Query q = em.createNamedQuery( "ConfigurationDO.findByDsParameter" );
    q.setParameter( "dsParameter", parameterName );
    return (ConfigurationDO) q.getSingleResult();
  }

}
