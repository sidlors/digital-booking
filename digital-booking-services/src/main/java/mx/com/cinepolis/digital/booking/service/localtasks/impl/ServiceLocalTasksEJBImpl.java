package mx.com.cinepolis.digital.booking.service.localtasks.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;
import mx.com.cinepolis.digital.booking.service.localtasks.ServiceLocalTasksEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements the methods related to local tasks.
 * 
 * @author jreyesv
 */
@Stateless(name = "ServiceLocalTasksEJB", mappedName = "ejb/ServiceLocalTasksEJB")
@Remote(ServiceLocalTasksEJB.class)
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceLocalTasksEJBImpl implements ServiceLocalTasksEJB
{
  private static final Logger LOG = LoggerFactory.getLogger( ServiceLocalTasksEJBImpl.class );
  private static final String LOG_EVENT_MOVIE_SYNCHRONIZE = "Error to connect database for deactivate presale information";

  @EJB
  private PresaleDAO presaleDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public void deactivatePresales()
  {
    LOG.info( "STARTING: deactivatePresales ... " );
    try
    {
      List<PresaleDO> presaleDOs = this.presaleDAO.findActivePresaleForDeactivate();
      if(CollectionUtils.isNotEmpty( presaleDOs ))
      {
        for(PresaleDO presaleDO : presaleDOs)
        {
          presaleDO.setFgActive( Boolean.FALSE );
          this.presaleDAO.edit( presaleDO );
        }
        this.presaleDAO.flush();
      }
      LOG.info( "ENDING: deactivatePresales ... " );
    }
    catch( Exception ex )
    {
      LOG.error( LOG_EVENT_MOVIE_SYNCHRONIZE, ex.getMessage(), ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_GENERIC, ex );
    }
  }

}
