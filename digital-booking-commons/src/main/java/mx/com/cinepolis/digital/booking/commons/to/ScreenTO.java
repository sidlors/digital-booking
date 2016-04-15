package mx.com.cinepolis.digital.booking.commons.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase que modela la informaión de la sala
 * 
 * @author rgarcia
 */
public class ScreenTO extends CatalogTO implements Cloneable
{

  /**
   * 
   */
  private static final long serialVersionUID = 9137054996732570244L;
  /**
   * Identificador de la sala
   */
  private Integer nuScreen;
  /**
   * Capacidad de la sala
   */
  private Integer nuCapacity;

  /**
   * Movie Format
   */
  private List<CatalogTO> movieFormats;

  /**
   * Sound Format
   */
  private List<CatalogTO> soundFormats;

  private CatalogTO screenFormat;

  private BookingObservationTO bookingObservation;

  private CatalogTO bookingStatus;

  private List<CatalogTO> showings;

  /**
   * Id del Cine
   */
  private Integer idTheater;

  private Boolean selected = false;

  private Boolean disabled = false;

  private Integer originalNuScreen;

  /**
   * Indicates whether exist presale in screen.
   */
  private PresaleTO presaleTO;

  /**
   * Constructor default
   */
  public ScreenTO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param id
   */
  public ScreenTO( Long id )
  {
    super.setId( id );
  }

  /**
   * @return the selected
   */
  public Boolean getSelected()
  {
    return selected;
  }

  /**
   * @param selected the selected to set
   */
  public void setSelected( Boolean selected )
  {
    this.selected = selected;
  }

  /**
   * @return the disabled
   */
  public Boolean getDisabled()
  {
    return disabled;
  }

  /**
   * @param disabled the disabled to set
   */
  public void setDisabled( Boolean disabled )
  {
    this.disabled = disabled;
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
   * @return the movieFormats
   */
  public List<CatalogTO> getMovieFormats()
  {
    return movieFormats;
  }

  /**
   * @param movieFormats the movieFormats to set
   */
  public void setMovieFormats( List<CatalogTO> movieFormats )
  {
    this.movieFormats = movieFormats;
  }

  /**
   * @return the soundFormats
   */
  public List<CatalogTO> getSoundFormats()
  {
    return soundFormats;
  }

  /**
   * @param soundFormats the soundFormats to set
   */
  public void setSoundFormats( List<CatalogTO> soundFormats )
  {
    this.soundFormats = soundFormats;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( Integer idTheater )
  {
    this.idTheater = idTheater;
  }

  /**
   * @return the idTheater
   */
  public Integer getIdTheater()
  {
    return idTheater;
  }

  /**
   * Gets an String that represents the Sound Formats of the screen
   * 
   * @return
   */
  public String getSoundFormatsLegend()
  {
    return getCatalogs( this.soundFormats );
  }

  /**
   * Gets an String that represents the Movie Formats of the screen
   * 
   * @return
   */
  public String getMovieFormatsLegend()
  {
    return getCatalogs( this.movieFormats );
  }

  private String getCatalogs( List<CatalogTO> data )
  {
    StringBuilder sb = new StringBuilder();
    if( CollectionUtils.isNotEmpty( data ) )
    {
      int n = 0;
      for( CatalogTO catalogTO : data )
      {
        sb.append( catalogTO.getName() );
        if( n < data.size() - 1 )
        {
          sb.append( ", " );
        }
        n++;
      }
    }
    return sb.toString();
  }

  /**
   * @return the screenFormat
   */
  public CatalogTO getScreenFormat()
  {
    return screenFormat;
  }

  /**
   * @param screenFormat the screenFormat to set
   */
  public void setScreenFormat( CatalogTO screenFormat )
  {
    this.screenFormat = screenFormat;
  }

  /**
   * @return the bookingObservation
   */
  public BookingObservationTO getBookingObservation()
  {
    return bookingObservation;
  }

  /**
   * @param bookingObservation the bookingObservation to set
   */
  public void setBookingObservation( BookingObservationTO bookingObservation )
  {
    this.bookingObservation = bookingObservation;
  }

  /**
   * @return the bookingStatus
   */
  public CatalogTO getBookingStatus()
  {
    return bookingStatus;
  }

  /**
   * @param bookingStatus the bookingStatus to set
   */
  public void setBookingStatus( CatalogTO bookingStatus )
  {
    this.bookingStatus = bookingStatus;
  }

  /**
   * @return the showings
   */
  public List<CatalogTO> getShowings()
  {
    return showings;
  }

  /**
   * @param showings the showings to set
   */
  public void setShowings( List<CatalogTO> showings )
  {
    this.showings = showings;
  }

  /**
   * @return the originalNuScreen
   */
  public Integer getOriginalNuScreen()
  {
    return originalNuScreen;
  }

  /**
   * @param originalNuScreen the originalNuScreen to set
   */
  public void setOriginalNuScreen( Integer originalNuScreen )
  {
    this.originalNuScreen = originalNuScreen;
  }

  /**
   * @return the presaleTO
   */
  public PresaleTO getPresaleTO()
  {
    return presaleTO;
  }

  /**
   * @param presaleTO the presaleTO to set
   */
  public void setPresaleTO( PresaleTO presaleTO )
  {
    this.presaleTO = presaleTO;
  }

  @Override
  public Object clone()
  {
    ScreenTO screen = new ScreenTO();
    screen.setId( this.getId() );
    screen.setNuScreen( this.nuScreen );
    screen.setNuCapacity( this.nuCapacity );
    if( CollectionUtils.isNotEmpty( this.movieFormats ) )
    {
      screen.setMovieFormats( new ArrayList<CatalogTO>() );
      screen.getMovieFormats().addAll( this.movieFormats );
    }

    if( CollectionUtils.isNotEmpty( this.soundFormats ) )
    {
      screen.setSoundFormats( new ArrayList<CatalogTO>() );
      screen.getSoundFormats().addAll( this.soundFormats );
    }
    if( this.screenFormat != null )
    {
      screen.setScreenFormat( new CatalogTO( this.screenFormat.getId(), this.screenFormat.getName() ) );
    }
    if( this.bookingStatus != null )
    {
      screen.setBookingStatus( new CatalogTO( this.bookingStatus.getId(), this.bookingStatus.getName() ) );
    }
    screen.setShowings( new ArrayList<CatalogTO>() );
    screen.setIdTheater( this.idTheater );
    screen.setSelected( this.selected );
    screen.setDisabled( this.disabled );
    screen.setOriginalNuScreen( this.originalNuScreen );
    return screen;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "id", this.getId() ).append( "nuScreen", nuScreen ).toString();
  }
}
