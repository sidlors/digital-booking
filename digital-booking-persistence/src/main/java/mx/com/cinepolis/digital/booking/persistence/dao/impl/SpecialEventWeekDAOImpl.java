package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class SpecialEventWeekDAOImpl extends AbstractBaseDAO<SpecialEventWeekDO> implements SpecialEventWeekDAO
{
  @EJB
  private WeekDAO weekDAO;

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @Override
  public void createSpecialEventWeek( BookingSpecialEventDO specialEventDO, SpecialEventTO specialEventTO )
  {
    List<WeekDO> weeks = this.weekDAO.findWeeksByStartDayAndFinalDay( specialEventDO.getDtStartSpecialEvent(),
      specialEventDO.getDtEndSpecialEvent() );
    for( WeekDO week : weeks )
    {
      SpecialEventWeekDO specialEventWeekDO = new SpecialEventWeekDO();
      AbstractEntityUtils.applyElectronicSign( specialEventWeekDO, specialEventTO );
      specialEventWeekDO.setIdBookingSpecialEvent( specialEventDO );
      specialEventWeekDO.setIdWeek( week );
      this.create( specialEventWeekDO );
    }
  }
  
  
  public SpecialEventWeekDAOImpl()
  {
    super( SpecialEventWeekDO.class );
  }

  public SpecialEventWeekDAOImpl( Class<SpecialEventWeekDO> entityClass )
  {
    super( entityClass );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

}
