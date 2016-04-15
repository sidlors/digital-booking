package mx.com.cinepolis.digital.booking.commons.to.reports;

import java.io.Serializable;
import java.util.List;

/**
 * Objeto de transferencia de la programacion de un cine
 * 
 * @author rgarcia
 * @since 0.1.0
 */
public class WeeklyBookingReportTheaterTO implements  Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 8362771183235510429L;
  private Long idTheater;
  private String dsTheater;
  private String dsCity;
  private String theaterName;
  private String dsRegion;
  private List<WeeklyBookingReportMovieTO> movies;

  /**
   * @return the idTheater
   */
  public Long getIdTheater()
  {
    return idTheater;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( Long idTheater )
  {
    this.idTheater = idTheater;
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
   * @return the movies
   */
  public List<WeeklyBookingReportMovieTO> getMovies()
  {
    return movies;
  }

  /**
   * @param movies the movies to set
   */
  public void setMovies( List<WeeklyBookingReportMovieTO> movies )
  {
    this.movies = movies;
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
   * @return the theaterName
   */
  public String getTheaterName()
  {
    return theaterName;
  }

  /**
   * @param theaterName the theaterName to set
   */
  public void setTheaterName( String theaterName )
  {
    this.theaterName = theaterName;
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
  
  

}
