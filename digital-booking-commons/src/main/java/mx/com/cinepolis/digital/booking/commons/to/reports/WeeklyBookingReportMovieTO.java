package mx.com.cinepolis.digital.booking.commons.to.reports;

import java.io.Serializable;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;

/**
 * Objeto de transferencia de las peliculas programadas por semana
 * 
 * @author rgarcia
 * @since 0.1.0
 */
public class WeeklyBookingReportMovieTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1691463432407767132L;
  private String dsMovie;
  private String dsDistributor;
  private Integer nuWeek;
  private Integer nuScreen;
  private Integer nuCapacity;
  private String dsNote;
  private String dsStatus;
  private String dsTheater;
  private String dsCity;
  private String dsRegion;
  private BookingStatus bookingStatus;
  private boolean toBeContinue;
  private String dsFunctions;
  private String dsCine;
  private String dsDate;

  public String getDsCine()
  {
    return dsCine;
  }

  public void setDsCine( String dsCine )
  {
    this.dsCine = dsCine;
  }

  /**
   * @return the dsMovie
   */
  public String getDsMovie()
  {
    return dsMovie;
  }

  /**
   * @param dsMovie the dsMovie to set
   */
  public void setDsMovie( String dsMovie )
  {
    this.dsMovie = dsMovie;
  }

  /**
   * @return the dsDistributor
   */
  public String getDsDistributor()
  {
    return dsDistributor;
  }

  /**
   * @param dsDistributor the dsDistributor to set
   */
  public void setDsDistributor( String dsDistributor )
  {
    this.dsDistributor = dsDistributor;
  }

  /**
   * @return the nuWeek
   */
  public Integer getNuWeek()
  {
    return nuWeek;
  }

  /**
   * @param nuWeek the nuWeek to set
   */
  public void setNuWeek( Integer nuWeek )
  {
    this.nuWeek = nuWeek;
  }

  /**
   * @return the nuScreen
   */
  public Integer getNuScreen()
  {
    return nuScreen;
  }

  /**
   * @param nuScreen the nuScreen to set
   */
  public void setNuScreen( Integer nuScreen )
  {
    this.nuScreen = nuScreen;
  }

  /**
   * @return the nuCapacity
   */
  public Integer getNuCapacity()
  {
    return nuCapacity;
  }

  /**
   * @param nuCapacity the nuCapacity to set
   */
  public void setNuCapacity( Integer nuCapacity )
  {
    this.nuCapacity = nuCapacity;
  }

  /**
   * @return the dsNote
   */
  public String getDsNote()
  {
    return dsNote;
  }

  /**
   * @param dsNote the dsNote to set
   */
  public void setDsNote( String dsNote )
  {
    this.dsNote = dsNote;
  }

  /**
   * @return the dsStatus
   */
  public String getDsStatus()
  {
    return dsStatus;
  }

  /**
   * @param dsStatus the dsStatus to set
   */
  public void setDsStatus( String dsStatus )
  {
    this.dsStatus = dsStatus;
  }

  /**
   * @return the dsTheater
   */
  public String getDsTheater()
  {
    return dsTheater;
  }

  /**
   * @param dsTheater the dsTheater to set
   */
  public void setDsTheater( String dsTheater )
  {
    this.dsTheater = dsTheater;
  }

  /**
   * @return the dsCity
   */
  public String getDsCity()
  {
    return dsCity;
  }

  /**
   * @param dsCity the dsCity to set
   */
  public void setDsCity( String dsCity )
  {
    this.dsCity = dsCity;
  }

  /**
   * @return the dsRegion
   */
  public String getDsRegion()
  {
    return dsRegion;
  }

  /**
   * @param dsRegion the dsRegion to set
   */
  public void setDsRegion( String dsRegion )
  {
    this.dsRegion = dsRegion;
  }

  /**
   * @return the bookingStatus
   */
  public BookingStatus getBookingStatus()
  {
    return bookingStatus;
  }

  /**
   * @param bookingStatus the bookingStatus to set
   */
  public void setBookingStatus( BookingStatus bookingStatus )
  {
    this.bookingStatus = bookingStatus;
  }

  public boolean isToBeContinue()
  {
    return toBeContinue;
  }

  public void setToBeContinue( boolean toBeContinue )
  {
    this.toBeContinue = toBeContinue;
  }

  public String getDsFunctions()
  {
    return dsFunctions;
  }

  public void setDsFunctions( String dsFunctions )
  {
    this.dsFunctions = dsFunctions;
  }

  public String getDsDate()
  {
    return dsDate;
  }

  public void setDsDate( String dsDate )
  {
    this.dsDate = dsDate;
  }

}
