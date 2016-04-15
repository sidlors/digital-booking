package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.MovieFormatDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieFormatDAO;

/**
 * Class that implements the methods of the Data Access Object related to MovieFormat. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.MovieFormatDAO}
 * 
 * @author shernandezl
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class MovieFormatDAOImpl extends AbstractBaseDAO<MovieFormatDO> implements MovieFormatDAO
{

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

  public MovieFormatDAOImpl()
  {
    super( MovieFormatDO.class );
  }

}
