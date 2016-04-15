package mx.com.cinepolis.digital.booking.dao.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingIncomeDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;

import org.apache.commons.collections.Transformer;

/**
 * Transformer class for convert an instance of {@link mx.com.cinepolis.digital.booking.model.BookingIncomeDO} into a
 * {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTO}
 * 
 * @author gsegura
 */
public class BookingIncomeDOTOIncomeTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
    DateFormat df2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
    IncomeTO income = null;

    if( object instanceof BookingIncomeDO )
    {
      BookingIncomeDO register = (BookingIncomeDO) object;
      income = new IncomeTO();

      CatalogTO theater = (CatalogTO) new TheaterDOToCatalogTOTransformer().transform( register.getIdTheater() );
      income.setTheater( new TheaterTO( theater.getId(), theater.getName() ) );
      income.setEvent( (EventTO) new EventDOToEventTOTransformer().transform( register.getIdEvent() ) );
      income.setWeek( (WeekTO) new WeekDOToWeekTOTransformer().transform( register.getIdWeek() ) );
      income.setId( register.getIdBookingIncome() );
      income.setIncome( register.getQtIncome() );
      income.setTickets( register.getQtTickets() );
      income.setScreen( (ScreenTO) new ScreenDOToScreenTOTransformer().transform( register.getIdScreen() ) );
      income.setTimeStr( register.getHrShow() );
      income.setDateStr( df.format( register.getDtShow() ) );

      // En caso de que esté relacionado con una programación
      if( register.getIdBooking() != null )
      {
        income.setBooking( new BookingTO( register.getIdBooking().getIdBooking() ) );
        income.getBooking().setTheater( income.getTheater() );
        income.getBooking().setWeek( income.getWeek() );
        income.getBooking().setScreen( income.getScreen() );
        income.getBooking().setEvent( income.getEvent() );
        
        // Se obtiene la semana de programación
        for( BookingWeekDO bw : register.getIdBooking().getBookingWeekDOList() )
        {
          if( bw.getIdWeek().equals( register.getIdWeek() ) )
          {
            income.setExhibitionWeek( bw.getNuExhibitionWeek() );
            break;
          }
        }
      }

      String dateCompact = new StringBuilder().append( income.getDateStr() ).append( " " ).append( income.getTimeStr() )
          .toString();
      try
      {
        income.setDate( df2.parse( dateCompact ) );
      }
      catch( ParseException e )
      {
        income.setDate( register.getDtShow() );
      }
      income.setUserId( Long.valueOf( register.getIdLastUserModifier() ) );
      income.setFgActive( register.isFgActive() );
      income.setTimestamp( register.getDtLastModification() );
    }

    return income;
  }

}
