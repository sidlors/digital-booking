package mx.com.cinepolis.digital.booking.commons.to;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Transfer object for Booking
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class BookingTO extends AbstractTO implements Comparable<BookingTO>
{

  private static final long serialVersionUID = -6793101028995736563L;

  /**
   * The Id
   */
  private Long id;

  private Long idBookingWeek;
  /**
   * The event
   */
  private EventTO event;
  /**
   * Exhibition week
   */
  private WeekTO week;
  /**
   * Exhibition theater
   */
  private TheaterTO theater;
  /**
   * Booking status
   */
  private CatalogTO status;
  /**
   * Current exhibition week
   */
  private int exhibitionWeek;
  /**
   * Number of copies
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
   * Date of end Exhibition
   */
  private Date exhibitionEnd;

  /**
   * List of Screen
   */
  private List<ScreenTO> screens;

  /**
   * Screen
   */
  private ScreenTO screen;

  private BookingObservationTO bookingObservationTO;

  private List<Object> screensSelected;

  private Boolean isEditable;

  private Boolean selectedForSaving = false;

  private Boolean disabled;

  private boolean sent;

  private List<Integer> nuScreenToZero;

  /**
   * id for bookingType
   */

  private int idBookingType;
  /**
   * Special events associated to booking
   */
  private List<SpecialEventTO> specialEvents;

  /**
   * Date time of the show
   */
  private Date show;

  /**
   * The presale associated
   */
  private PresaleTO presaleTO;

  /**
   * template for email
   */
  private String template;

  private String subject;

  /**
   * Constructor default
   */
  public BookingTO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param id
   */
  public BookingTO( Long id )
  {
    this.id = id;
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
   * @return the event
   */
  public EventTO getEvent()
  {
    return event;
  }

  /**
   * @param event the event to set
   */
  public void setEvent( EventTO event )
  {
    this.event = event;
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
   * @return the status
   */
  public CatalogTO getStatus()
  {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus( CatalogTO status )
  {
    this.status = status;
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

  /**
   * @return the copy
   */
  public int getCopy()
  {
    return copy;
  }

  /**
   * @param copy the copy to set
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
   * @return the exhibitionEnd
   */
  public Date getExhibitionEnd()
  {
    return CinepolisUtils.enhancedClone( exhibitionEnd );
  }

  /**
   * @param exhibitionEnd the exhibitionEnd to set
   */
  public void setExhibitionEnd( Date exhibitionEnd )
  {
    this.exhibitionEnd = CinepolisUtils.enhancedClone( exhibitionEnd );
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
   * @return the bookingObservationTO
   */
  public BookingObservationTO getBookingObservationTO()
  {
    return bookingObservationTO;
  }

  /**
   * @param bookingObservationTO the bookingObservationTO to set
   */
  public void setBookingObservationTO( BookingObservationTO bookingObservationTO )
  {
    this.bookingObservationTO = bookingObservationTO;
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
   * @return the isEditable
   */
  public Boolean getIsEditable()
  {
    return isEditable;
  }

  /**
   * @param isEditable the isEditable to set
   */
  public void setIsEditable( Boolean isEditable )
  {
    this.isEditable = isEditable;
  }

  /**
   * @return the selectedForSaving
   */
  public Boolean getSelectedForSaving()
  {
    return selectedForSaving;
  }

  /**
   * @param selectedForSaving the selectedForSaving to set
   */
  public void setSelectedForSaving( Boolean selectedForSaving )
  {
    this.selectedForSaving = selectedForSaving;
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
   * @return the sent
   */
  public boolean isSent()
  {
    return sent;
  }

  /**
   * @param sent the sent to set
   */
  public void setSent( boolean sent )
  {
    this.sent = sent;
  }

  /**
   * @return the idBookingWeek
   */
  public Long getIdBookingWeek()
  {
    return idBookingWeek;
  }

  /**
   * @param idBookingWeek the idBookingWeek to set
   */
  public void setIdBookingWeek( Long idBookingWeek )
  {
    this.idBookingWeek = idBookingWeek;
  }

  /**
   * @return the nuScreenToZero
   */
  public List<Integer> getNuScreenToZero()
  {
    return nuScreenToZero;
  }

  /**
   * @param nuScreenToZero the nuScreenToZero to set
   */
  public void setNuScreenToZero( List<Integer> nuScreenToZero )
  {
    this.nuScreenToZero = nuScreenToZero;
  }

  /**
   * @return the idBookingType
   */
  public int getIdBookingType()
  {
    return idBookingType;
  }

  /**
   * @param idBookingType the idBookingType to set
   */
  public void setIdBookingType( int idBookingType )
  {
    this.idBookingType = idBookingType;
  }

  /**
   * @return the specialEvents
   */
  public List<SpecialEventTO> getSpecialEvents()
  {
    return specialEvents;
  }

  /**
   * @param specialEvents the specialEvents to set
   */
  public void setSpecialEvents( List<SpecialEventTO> specialEvents )
  {
    this.specialEvents = specialEvents;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof BookingTO )
    {
      flag = this.id.equals( ((BookingTO) object).id );
    }
    return flag;
  }

  /**
   * @return the show
   */
  public Date getShow()
  {
    return CinepolisUtils.enhancedClone( show );
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
   * @param show the show to set
   */
  public void setShow( Date show )
  {
    this.show = CinepolisUtils.enhancedClone( show );
  }

  public String getShowDateString()
  {
    String showDate = null;
    if( this.show != null )
    {
      DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
      showDate = df.format( show );
    }
    return showDate;
  }

  public String getShowTimeString()
  {
    String showDate = null;
    if( this.show != null )
    {
      DateFormat df = new SimpleDateFormat( "HH:mm" );
      showDate = df.format( show );
    }
    return showDate;
  }

  public String getShowDateTimeString()
  {
    String showDate = null;
    if( this.show != null )
    {
      DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
      showDate = df.format( show );
    }
    return showDate;
  }

  /**
   * @return the template
   */
  public String getTemplate()
  {
    return template;
  }

  /**
   * @param template the template to set
   */
  public void setTemplate( String template )
  {
    this.template = template;
  }

  /**
   * @return the subject
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.id ).hashCode();
  }

  @Override
  public int compareTo( BookingTO that )
  {
    return this.id.compareTo( that.id );
  }

  @Override
  public String toString()
  {
    String str = new ToStringBuilder( this ).append( "event:", this.getEvent() ).append( "theater:", this.getTheater() )
        .append( "week:", this.getWeek() ).toString();
    return str;
  }
}
