package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Transfer object for Incomes
 * 
 * @author gsegura
 */
public class IncomeTO extends CatalogTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -5315574981870322377L;

  private TheaterTO theater;
  private WeekTO week;
  private BookingTO booking;
  private EventTO event;
  private ScreenTO screen;
  private String dateStr;
  private String timeStr;
  private Date date;
  private double income;
  private int tickets;
  private int exhibitionWeek;

  /**
   * @return the theater
   */
  public TheaterTO getTheater()
  {
    return theater;
  }

  /**
   * @param theater the theater to set
   */
  public void setTheater( TheaterTO theater )
  {
    this.theater = theater;
  }

  /**
   * @return the movie
   */
  public EventTO getEvent()
  {
    return event;
  }

  /**
   * @param movie the movie to set
   */
  public void setEvent( EventTO event )
  {
    this.event = event;
  }

  /**
   * @return the screen
   */
  public ScreenTO getScreen()
  {
    return screen;
  }

  /**
   * @param screen the screen to set
   */
  public void setScreen( ScreenTO screen )
  {
    this.screen = screen;
  }

  /**
   * @return the dateStr
   */
  public String getDateStr()
  {
    return dateStr;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr( String dateStr )
  {
    this.dateStr = dateStr;
  }

  /**
   * @return the timeStr
   */
  public String getTimeStr()
  {
    return timeStr;
  }

  /**
   * @param timeStr the timeStr to set
   */
  public void setTimeStr( String timeStr )
  {
    this.timeStr = timeStr;
  }

  /**
   * @return the date
   */
  public Date getDate()
  {
    return CinepolisUtils.enhancedClone( date );
  }

  /**
   * @param date the date to set
   */
  public void setDate( Date date )
  {
    this.date = CinepolisUtils.enhancedClone( date );
  }

  /**
   * @return the income
   */
  public double getIncome()
  {
    return income;
  }

  /**
   * @param income the income to set
   */
  public void setIncome( double income )
  {
    this.income = income;
  }

  /**
   * @return the tickets
   */
  public int getTickets()
  {
    return tickets;
  }

  /**
   * @param tickets the tickets to set
   */
  public void setTickets( int tickets )
  {
    this.tickets = tickets;
  }

  /**
   * @return the week
   */
  public WeekTO getWeek()
  {
    return week;
  }

  /**
   * @param week the week to set
   */
  public void setWeek( WeekTO week )
  {
    this.week = week;
  }

  /**
   * @return the booking
   */
  public BookingTO getBooking()
  {
    return booking;
  }

  /**
   * @param booking the booking to set
   */
  public void setBooking( BookingTO booking )
  {
    this.booking = booking;
  }

  /**
   * @return the exhibitionWeek
   */
  public int getExhibitionWeek()
  {
    return exhibitionWeek;
  }

  /**
   * @param exhibitionWeek the exhibitionWeek to set
   */
  public void setExhibitionWeek( int exhibitionWeek )
  {
    this.exhibitionWeek = exhibitionWeek;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "id", this.getId() ).append( "booking", this.booking )
        .append( "theater", this.theater ).append( "screen", this.screen ).append( "week", this.week )
        .append( "event", this.event ).append( "income", this.income ).append( "date", this.dateStr )
        .append( "show", this.timeStr ).append( "tickets", this.tickets ).toString();
  }

}
