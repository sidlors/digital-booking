package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.dao.util.EventMovieDOToEventMovieTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventMovieDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;

/**
 * Class that implements the methods of the Data Access Object related to EventMovie. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.EventMovieDAO}
 * 
 * @author afuentes
 */
@Stateless
@SuppressWarnings("unchecked")
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class EventMovieDAOImpl extends AbstractBaseDAO<EventMovieDO> implements EventMovieDAO
{
  private static final String PARAMETER_ID_DISTRBUTOR = "idDistributor";
  private static final String QUERY_FIND_BY_DISTRUBUTOR = "EventMovieDO.findByidDistributor";
  
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

  /**
   * Constructor default
   */
  public EventMovieDAOImpl()
  {
    super( EventMovieDO.class );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventMovieDO> findByIdDistributor( DistributorDO dstributor )
  {
    Query query = em.createNamedQuery( QUERY_FIND_BY_DISTRUBUTOR );
    query.setParameter( PARAMETER_ID_DISTRBUTOR, dstributor );
    return query.getResultList();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public EventMovieTO findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "EventMovieDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    List<EventMovieTO> movieTOs = (List<EventMovieTO>) CollectionUtils.collect( q.getResultList(),
      new EventMovieDOToEventMovieTOTransformer() );
    return (EventMovieTO) CollectionUtils.find( movieTOs, PredicateUtils.notNullPredicate() );
  }
}
