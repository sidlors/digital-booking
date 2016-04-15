package mx.com.cinepolis.digital.booking.service.week.impl;

import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;
import mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB;

/**
 * Clase que implementa los métodos asociados a la administración de semanas.
 * 
 * @author shernandezl
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminWeekEJBImpl implements ServiceAdminWeekEJB
{
  private static final int INCREMENT = 1;
  private static final int WEEK_ONE = 1;
  private static final int YEAR=1;
  /**
   * Weeks DAO
   */
  @EJB
  private WeekDAO weekDAO;
  @EJB
  private ConfigurationDAO configurationDAO;
  @EJB
  private BookingWeekDAO bookingWeekDAO;

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB#getCatalogWeekSummary(
   * mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<WeekTO> getCatalogWeekSummary( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    return weekDAO.findAllByPaging( pagingRequestTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB#getNextWeek()
   */
  @Override
  public WeekTO getNextWeek()
  {
	  Calendar date = Calendar.getInstance();
	  date.set( YEAR, date.get( Calendar.YEAR ) + INCREMENT );
	  WeekTO weekTO = weekDAO.getNextWeek( date.get( Calendar.YEAR ) );
	  if(weekTO==null)
	  {
	    weekTO=weekDAO.getNextWeek( date.get( Calendar.YEAR ) - INCREMENT);
	    if(weekTO==null)
	    {
	      weekTO =new WeekTO();
	      weekTO.setNuWeek( WEEK_ONE );
	      weekTO.setNuYear( date.get(Calendar.YEAR) );
	    }
	    else if(weekTO.getNuWeek() >= numWeeksOfYear() )
	    {
	      weekTO.setNuWeek( WEEK_ONE );
	      weekTO.setNuYear( date.get( Calendar.YEAR ));
	    }
	    else
	    {
	      weekTO.setNuWeek( weekTO.getNuWeek() + INCREMENT );
	    }
	  }
	  else
	  {
	    weekTO.setNuWeek( weekTO.getNuWeek() + INCREMENT );
	  }
    
    int weekStartDay = Integer.parseInt( this.configurationDAO.findByParameterName(
      Configuration.WEEK_START_DAY.getParameter() ).getDsValue() );
    date.setTime( weekTO.getFinalDayWeek() );
    while( date.get( Calendar.DAY_OF_WEEK ) != weekStartDay )
    {
      date.add( Calendar.DATE, INCREMENT );
    }
    weekTO.setStartingDayWeek( date.getTime() );

    int weekFinalDay = Integer.parseInt( this.configurationDAO.findByParameterName(
      Configuration.WEEK_FINAL_DAY.getParameter() ).getDsValue() );
    while( date.get( Calendar.DAY_OF_WEEK ) != weekFinalDay )
    {
      date.add( Calendar.DATE, INCREMENT );
    }
    weekTO.setFinalDayWeek( date.getTime() );
    return weekTO;
  }

 /*
  * {@inheritDoc}
  * @see mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB#saveWeek(
  * mx.com.cinepolis.digital.booking.model.to.WeekTO)
  */
  @Override
  public void saveWeek( WeekTO weekTO )
  {
    ValidatorUtil.validateWeek( weekTO );
    validateWeek( weekTO );
    weekDAO.save( weekTO );
  }

  private void validateWeek( WeekTO weekTO )
  {
    if ( weekTO.getNuWeek() > numWeeksOfYear() )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_INVALID_NUMBER );
    }
    WeekTO existWeekTO = weekDAO.getWeekByDates( weekTO );
    if( existWeekTO != null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_WEEK_ALREADY_REGISTERED );
    }
    // Validar que no se empalme una semana especial con una normal
    if( weekTO.isSpecialWeek() )
    {
      WeekTO w = new WeekTO();
      w.setStartingDayWeek( weekTO.getStartingDayWeek() );
      w.setFinalDayWeek( weekTO.getFinalDayWeek() );
      w.setSpecialWeek( false );
      existWeekTO = weekDAO.getWeekByDates( w );
      if( existWeekTO != null )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_WEEK_ALREADY_REGISTERED );
      }
    }
    
  }

/*
 * {@inheritDoc}
 * @see mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB#updateWeek(
 * mx.com.cinepolis.digital.booking.model.to.WeekTO)
 */
  @Override
  public void updateWeek( WeekTO weekTO )
  {
    ValidatorUtil.validateWeek( weekTO );
    if ( weekTO.getNuWeek() > numWeeksOfYear() )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_INVALID_NUMBER );
    }
    WeekTO existWeekTO = weekDAO.getWeekByDates( weekTO );
    if( existWeekTO != null && !existWeekTO.getIdWeek().equals( weekTO.getIdWeek() ) )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_WEEK_ALREADY_REGISTERED );
    }
    
    // Validar que no se empalme una semana especial con una normal
    if( weekTO.isSpecialWeek() )
    {
      WeekTO w = new WeekTO();
      w.setStartingDayWeek( weekTO.getStartingDayWeek() );
      w.setFinalDayWeek( weekTO.getFinalDayWeek() );
      w.setSpecialWeek( false );
      existWeekTO = weekDAO.getWeekByDates( w );
      if( existWeekTO != null && !existWeekTO.getIdWeek().equals( weekTO.getIdWeek() ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_WEEK_ALREADY_REGISTERED );
      }
    }
    
    weekDAO.update( weekTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB#deleteWeek(
   * mx.com.cinepolis.digital.booking.model.to.WeekTO)
   */
  @Override
  public void deleteWeek( WeekTO weekTO )
  {
    ValidatorUtil.validateDeleteWeek( weekTO );
    WeekDO weekDO = new WeekDO();
    weekDO.setIdWeek( weekTO.getIdWeek() );
    if( 0 == bookingWeekDAO.countBookingWeek( weekDO ) )
    {
      weekDAO.delete( weekTO );
    }
    else
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_DELETE_WEEK );
    }
  }
  
  /**
   * Método que obtiene la configuración del total de semanas del año.
   * 
   * @return
   */
  private int numWeeksOfYear()
  {
    int numWeeksOfYear = Integer.parseInt( this.configurationDAO.findByParameterName(
      Configuration.NUM_WEEKS_OF_YEAR.getParameter() ).getDsValue() );
    return numWeeksOfYear;
  }
}
