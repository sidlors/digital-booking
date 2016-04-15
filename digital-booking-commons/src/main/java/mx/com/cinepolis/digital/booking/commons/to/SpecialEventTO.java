package mx.com.cinepolis.digital.booking.commons.to;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author jcarbajal Transfer object for Booking special event in theater
 */
public class SpecialEventTO extends AbstractTO implements Comparable<SpecialEventTO>
{
  private static final String A_TO_DATE = " to ";
  private static final String DE_TO_DATE = " of ";
  /**
   * Serialización
   */
  private static final long serialVersionUID = 1977205924915234348L;
  /**
   * id
   */
  private Long id;
  /**
   * id idBooking associated
   */
  private Long idBooking;
  /**
   * theater
   */
  private TheaterTO theater;
  /**
   * number of copies for special event
   */
  private int copy;
  /**
   * Number of copies of screen zero
   */
  private int copyScreenZero;
  /**
   * Number of copies of screen zero terminated
   */
  private int copyScreenZeroTerminated;
  /**
   * Date to display in grid
   */
  private Date date;
  /**
   * String to show the date in the form( same in the report)
   */
  private String strDate;
  /**
   * first day to booking for special event
   */
  private Date startDay;
  /**
   * final day to booking for special event
   */
  private Date finalDay;
  /**
   * notes about the booking
   */
  private String notes;
  /**
   * var for screens
   */
  private List<ScreenTO> screens;
  /**
   * Screens selected
   */
  private List<Object> screensSelected;
  /**
   * number of screens
   */
  private List<Integer> nuScreens;
  /**
   * vars for shows
   */
  private List<CatalogTO> shows;
  /**
   * shows selected
   */
  private List<Object> showingsSelected;
  /**
   * indicate if the date is displayed in the report
   */
  private boolean showDate;
  /**
   * observation for booking
   */
  private BookingObservationTO bookingObservation;
  /**
   * Booking special event status
   */
  private CatalogTO status;
  /**
   * Special Event
   */
  private EventTO event;
  /**
   * Styles for view
   */
  private String removalStyle = "color: #FF0000;";
  /**
   * Styles for view
   */
  private String normalStyle = "";
  /**
   * indicate if the theater is disabled
   */
  private Boolean disabled;
  /**
   * inRemoval
   */
  private boolean inRemoval;
  /**
   * id for bookingType
   */
  private Long idBookingType;
  /**
   * List of screens for special event booking
   */
  private List<SpecialEventScreenTO> specialEventScreens;
  /**
   * Observation associated
   */
  private BookingObservationTO observation;

  /**
   * The presale associated
   */
  private PresaleTO presaleTO;
  /**
   * screens permitidas o con el mismo formato;
   */
  private boolean screenAvailable; 

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
   * @return the copies
   */
  public int getCopy()
  {
    return copy;
  }

  /**
   * @param copy the copies to set
   */
  public void setCopy( int copy )
  {
    this.copy = copy;
  }

  /**
   * @return the copySreenZero
   */
  public int getCopyScreenZero()
  {
    return copyScreenZero;
  }

  /**
   * @param copySreenZero the copySreenZero to set
   */
  public void setCopyScreenZero( int copySreenZero )
  {
    this.copyScreenZero = copySreenZero;
  }

  /**
   * @return the copySreenZeroTerminated
   */
  public int getCopyScreenZeroTerminated()
  {
    return copyScreenZeroTerminated;
  }

  /**
   * @param copySreenZeroTerminated the copySreenZeroTerminated to set
   */
  public void setCopyScreenZeroTerminated( int copyScreenZeroTerminated )
  {
    this.copyScreenZeroTerminated = copyScreenZeroTerminated;
  }

  /**
   * @return the shows
   */
  public List<CatalogTO> getShows()
  {
    return shows;
  }

  /**
   * @param shows the shows to set
   */
  public void setShows( List<CatalogTO> shows )
  {
    this.shows = shows;
  }

  /**
   * @return the date
   */
  public Date getDate()
  {
    return CinepolisUtils.enhancedClone( date );
  }

  /**
   * TODO por cambiar el locale al lenguaje apropiado
   * 
   * @param date the date to set
   */
  public void setDate( Date date )
  {
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "en", "US" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "en", "US" ) );
    StringBuilder sb = new StringBuilder();
    if( this.validateDates() )
    {
      if( DateUtils.isSameDay( this.startDay, this.finalDay ) )
      {
        sb.append( format.format( this.startDay ) ).append( DE_TO_DATE ).append( format2.format( this.startDay ) )
            .append( ". " );

        this.strDate = sb.toString();
      }
      else
      {
        sb.append( format.format( this.startDay ) ).append( DE_TO_DATE ).append( format2.format( this.startDay ) )
            .append( A_TO_DATE ).append( format.format( this.finalDay ) ).append( DE_TO_DATE )
            .append( format2.format( this.finalDay ) ).append( ". " );
        this.strDate = sb.toString();
      }
    }
    else
    {
      this.strDate = "";
    }

    this.date = CinepolisUtils.enhancedClone( date );
  }

  /**
   * Method that validates whether start and final dates are not null.
   * 
   * @return isValid
   */
  private boolean validateDates()
  {
    return (this.startDay != null && this.finalDay != null);
  }

  /**
   * @return the notes
   */
  public String getNotes()
  {
    return notes;
  }

  /**
   * @param notes the notes to set
   */
  public void setNotes( String notes )
  {
    this.notes = notes;
  }

  /**
   * @return the startDay
   */
  public Date getStartDay()
  {
    return CinepolisUtils.enhancedClone( startDay );
  }

  /**
   * @param startDay the startDay to set
   */
  public void setStartDay( Date startDay )
  {
    this.startDay = CinepolisUtils.enhancedClone( startDay );
  }

  /**
   * @return the finalDay
   */
  public Date getFinalDay()
  {
    return CinepolisUtils.enhancedClone( finalDay );
  }

  /**
   * @param finalDay the finalDay to set
   */
  public void setFinalDay( Date finalDay )
  {
    this.finalDay = CinepolisUtils.enhancedClone( finalDay );
  }

  /**
   * @return the event
   */
  public EventTO getEvent()
  {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEventTO( EventTO event )
  {
    this.event = event;
  }

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * @return the idBooking
   */
  public Long getIdBooking()
  {
    return idBooking;
  }

  /**
   * @param idBooking the idBooking to set
   */
  public void setIdBooking( Long idBooking )
  {
    this.idBooking = idBooking;
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
   * @return the screens
   */
  public List<ScreenTO> getScreens()
  {
    return screens;
  }

  /**
   * @param screens the screens to set
   */
  public void setScreens( List<ScreenTO> screens )
  {
    this.screens = screens;
  }

  /**
   * @return the screensSelected
   */
  public List<Object> getScreensSelected()
  {
    return screensSelected;
  }

  /**
   * @param screensSelected the screensSelected to set
   */
  public void setScreensSelected( List<Object> screensSelected )
  {
    this.screensSelected = screensSelected;
  }

  /**
   * @return the nuScreens
   */
  public List<Integer> getNuScreens()
  {
    return nuScreens;
  }

  /**
   * @param nuScreens the nuScreens to set
   */
  public void setNuScreens( List<Integer> nuScreens )
  {
    this.nuScreens = nuScreens;
  }

  /**
   * @return the showingsSelected
   */
  public List<Object> getShowingsSelected()
  {
    return showingsSelected;
  }

  /**
   * @param showingsSelected the showingsSelected to set
   */
  public void setShowingsSelected( List<Object> showingsSelected )
  {
    this.showingsSelected = showingsSelected;
  }

  /**
   * @return the removalStyle
   */
  public String getRemovalStyle()
  {
    return removalStyle;
  }

  /**
   * @param removalStyle the removalStyle to set
   */
  public void setRemovalStyle( String removalStyle )
  {
    this.removalStyle = removalStyle;
  }

  /**
   * @return the normalStyle
   */
  public String getNormalStyle()
  {
    return normalStyle;
  }

  /**
   * @param normalStyle the normalStyle to set
   */
  public void setNormalStyle( String normalStyle )
  {
    this.normalStyle = normalStyle;
  }

  @Override
  public int compareTo( SpecialEventTO obj )
  {
    // TODO Auto-generated method stub
    CompareToBuilder compare = new CompareToBuilder();
    if( this.event != null && obj.event != null )
    {
      compare.append( this.event.getDsEventName(), obj.event.getDsEventName() );
    }
    return compare.toComparison();
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean isEquals = false;
    if( this == object )
    {
      isEquals = true;
    }
    else if( object instanceof SpecialEventTO )
    {
      isEquals = new EqualsBuilder().append( this.id, ((SpecialEventTO) object).id ).isEquals();
    }
    return isEquals;
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
   * @return the inRemoval
   */
  public boolean isInRemoval()
  {
    return inRemoval;
  }

  /**
   * @param inRemoval the inRemoval to set
   */
  public void setInRemoval( boolean inRemoval )
  {
    this.inRemoval = inRemoval;
  }

  /**
   * @return the strDate
   */
  public String getStrDate()
  {
    return strDate;
  }

  public CatalogTO getStatus()
  {
    return status;
  }

  public void setStatus( CatalogTO status )
  {
    this.status = status;
  }

  /**
   * @param strDate the strDate to set
   */
  public void setStrDate( String strDate )
  {
    this.strDate = strDate;
  }

  /**
   * @return the showDate
   */
  public boolean isShowDate()
  {
    return showDate;
  }

  /**
   * @param showDate the showDate to set
   */
  public void setShowDate( boolean showDate )
  {
    this.showDate = showDate;
  }

  /**
   * @return the idBookingType
   */
  public Long getIdBookingType()
  {
    return idBookingType;
  }

  /**
   * @param idBookingType the idBookingType to set
   */
  public void setIdBookingType( Long idBookingType )
  {
    this.idBookingType = idBookingType;
  }

  /**
   * @param event the event to set
   */
  public void setEvent( EventTO event )
  {
    this.event = event;
  }

  /**
   * @return the specialEventScreens
   */
  public List<SpecialEventScreenTO> getSpecialEventScreens()
  {
    return specialEventScreens;
  }

  /**
   * @param specialEventScreens the specialEventScreens to set
   */
  public void setSpecialEventScreens( List<SpecialEventScreenTO> specialEventScreens )
  {
    this.specialEventScreens = specialEventScreens;
  }

  /**
   * @return the observation
   */
  public BookingObservationTO getObservation()
  {
    return observation;
  }

  /**
   * @param observation the observation to set
   */
  public void setObservation( BookingObservationTO observation )
  {
    this.observation = observation;
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

  /**
   * @return the screenAvailable
   */
  public boolean getScreenAvailable()
  {
    return screenAvailable;
  }

  /**
   * @param screenAvailable the screenAvailable to set
   */
  public void setScreenAvailable( boolean screenAvailable )
  {
    this.screenAvailable = screenAvailable;
  }

}
