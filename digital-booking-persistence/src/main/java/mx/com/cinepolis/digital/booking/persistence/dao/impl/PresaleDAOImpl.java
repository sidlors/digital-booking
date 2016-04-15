package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.PresaleTOToPresaleDOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;

import org.apache.commons.lang.time.DateUtils;

@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class PresaleDAOImpl extends AbstractBaseDAO<PresaleDO> implements PresaleDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private BookingWeekScreenDAO bookingWeekScreenDAO;

  @EJB
  private BookingSpecialEventScreenDAO bookingSpecialEventScreenDAO;

  private static final String TODAY = "today";

  public PresaleDAOImpl()
  {
    super( PresaleDO.class );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  public PresaleDO save( PresaleTO presaleTO )
  {
    PresaleDO presaleDO = null;
    if( this.validatePresale( presaleTO ) && !this.isPresaleToEdit( presaleTO ) )
    {
      presaleDO = (PresaleDO) new PresaleTOToPresaleDOTransformer().transform( presaleTO );
      if( presaleDO.getIdBookingWeekScreen() != null )
      {
        BookingWeekScreenDO bookingWeekScreenDO = bookingWeekScreenDAO.find( presaleDO.getIdBookingWeekScreen()
            .getIdBookingWeekScreen() );
        presaleDO.setIdBookingWeekScreen( bookingWeekScreenDO );
      }
      else if( presaleDO.getIdBookingSpecialEventScreen() != null )
      {
        BookingSpecialEventScreenDO bookingSpecialEventScreenDO = bookingSpecialEventScreenDAO.find( presaleDO
            .getIdBookingSpecialEventScreen().getIdBookingSpecialEventScreen() );
        presaleDO.setIdBookingSpecialEventScreen( bookingSpecialEventScreenDO );
      }
      else
      {
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_BOOKING_PRESALE_NOT_ASSOCIATED_AT_BOOKING );
      }
      AbstractEntityUtils.applyElectronicSign( presaleDO, presaleTO );
      this.create( presaleDO );
      this.flush();
    }
    return presaleDO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PresaleDO update( PresaleTO presaleTO )
  {
    PresaleDO presaleDO = null;
    if( this.validatePresale( presaleTO ) && this.isPresaleToEdit( presaleTO ) )
    {
      presaleDO = this.find( presaleTO.getIdPresale() );
      presaleDO.setDtStartDayPresale( presaleTO.getDtStartDayPresale() );
      presaleDO.setDtFinalDayPresale( presaleTO.getDtFinalDayPresale() );
      presaleDO.setDtReleaseDay( presaleTO.getDtReleaseDay() );
      presaleDO.setFgActive( presaleTO.isFgActive() );
      AbstractEntityUtils.applyElectronicSign( presaleDO, presaleTO );
      this.edit( presaleDO );
      this.flush();
    }
    return presaleDO;
  }

  /**
   * Validates whether {@link mx.com.cinepolis.digital.booking.commons.to.PresaleTO} object is valid ro save, update or
   * delete.
   * 
   * @param presaleTO, the presale to validate.
   * @return isValid, with the result of validation.
   */
  private boolean validatePresale( PresaleTO presaleTO )
  {
    return (presaleTO != null);
  }

  /**
   * Validates whether {@link mx.com.cinepolis.digital.booking.commons.to.PresaleTO} object is valid to edit.
   * 
   * @param presaleTO, the presale to validate.
   * @return isValid, with the result of validation.
   */
  private boolean isPresaleToEdit( PresaleTO presaleTO )
  {
    return (presaleTO.getIdPresale() != null && presaleTO.getIdPresale() > 0);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<PresaleDO> findActivePresaleForDeactivate()
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "PresaleDO.findActivePresaleForDeactivate" );
    q.setParameter( TODAY, DateUtils.truncate( new Date(), Calendar.DATE ) );
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countAllActivePresales()
  {
    int result = 0;
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "PresaleDO.countAllActive" );
    result = Integer.parseInt( q.getSingleResult().toString() );
    return result;
  }

}
