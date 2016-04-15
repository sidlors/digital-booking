package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Transfer object for Bookings per screen of a Theater
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class BookingTheaterTO extends AbstractTO implements Comparable<BookingTheaterTO>
{

  private static final long serialVersionUID = 4325965284112952678L;

  private Long id;
  private ScreenTO screenTO;
  private String format;
  private Integer capacity;
  private EventTO eventTO;
  private String distributor;
  private Integer week;
  private Double fridayIncome;
  private Double saturdayIncome;
  private Double sundayIncome;
  private Double totalIncome;
  private Long fridayTickets;
  private Long saturdayTickets;
  private Long sundayTickets;
  private Long totalTickets;
  private CatalogTO statusTO;
  private List<CatalogTO> statusTOs;
  private String image;
  private Object statusIdSelected;
  private String statusName;
  private BookingObservationTO bookingObservationTO;
  private String observationImage;
  private String note;
  private List<CatalogTO> showings;
  private List<Object> selectedShowings;
  private List<ScreenTO> screens;
  private List<Object> selectedScreens;
  private int copies;

  private List<Integer> nuScreens;
  private List<EventTO> events;
  private Long selectedEventId;

  private boolean editable;
  private boolean available;
  private boolean inRemoval;

  private String removalStyle = "color: #FF0000;";
  private String normalStyle = "";
  private Integer previousNuScreen;
  private boolean fgSpecialEvent = false;
  private boolean fgShowDate = false;
  private PresaleTO presaleTO;
  private boolean fgPresaleSelected;
  private int idBookingType;
  private String strDateSpecialEvents;
  private Date startDay;
  private Date finalDay;
  private int copyScreenZero = 0;
  private static final int ID_BOOKING_TYPE_SPECIAL_EVENT = 3;
  private static final int ID_BOOKING_TYPE_PRE_SALE = 2;
  private static final String SPECIAL_EVENT_LABEL = " (Special Event)";
  private static final String PRE_RELEASE_LABEL = " (Pre release)";

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
   * @return the screenTO
   */
  public ScreenTO getScreenTO()
  {
    return screenTO;
  }

  /**
   * @param screenTO the screenTO to set
   */
  public void setScreenTO( ScreenTO screenTO )
  {
    this.screenTO = screenTO;
  }

  /**
   * @return the format
   */
  public String getFormat()
  {
    return format;
  }

  /**
   * @param format the format to set
   */
  public void setFormat( String format )
  {
    this.format = format;
  }

  /**
   * @return the capacity
   */
  public Integer getCapacity()
  {
    return capacity;
  }

  /**
   * @param capacity the capacity to set
   */
  public void setCapacity( Integer capacity )
  {
    this.capacity = capacity;
  }

  /**
   * @return the eventTO
   */
  public EventTO getEventTO()
  {
    return eventTO;
  }

  /**
   * @param eventTO the eventTO to set
   */
  public void setEventTO( EventTO eventTO )
  {
    this.eventTO = eventTO;
  }

  /**
   * @return the distributor
   */
  public String getDistributor()
  {
    return distributor;
  }

  /**
   * @param distributor the distributor to set
   */
  public void setDistributor( String distributor )
  {
    this.distributor = distributor;
  }

  /**
   * @return the week
   */
  public Integer getWeek()
  {
    return week;
  }

  /**
   * @param week the week to set
   */
  public void setWeek( Integer week )
  {
    this.week = week;
  }

  /**
   * @return the fridayIncome
   */
  public Double getFridayIncome()
  {
    return fridayIncome;
  }

  /**
   * @param fridayIncome the fridayIncome to set
   */
  public void setFridayIncome( Double fridayIncome )
  {
    this.fridayIncome = fridayIncome;
  }

  /**
   * @return the saturdayIncome
   */
  public Double getSaturdayIncome()
  {
    return saturdayIncome;
  }

  /**
   * @param saturdayIncome the saturdayIncome to set
   */
  public void setSaturdayIncome( Double saturdayIncome )
  {
    this.saturdayIncome = saturdayIncome;
  }

  /**
   * @return the sundayIncome
   */
  public Double getSundayIncome()
  {
    return sundayIncome;
  }

  /**
   * @param sundayIncome the sundayIncome to set
   */
  public void setSundayIncome( Double sundayIncome )
  {
    this.sundayIncome = sundayIncome;
  }

  /**
   * @return the totalIncome
   */
  public Double getTotalIncome()
  {
    return totalIncome;
  }

  /**
   * @param totalIncome the totalIncome to set
   */
  public void setTotalIncome( Double totalIncome )
  {
    this.totalIncome = totalIncome;
  }

  /**
   * @return the totalTickets
   */
  public Long getTotalTickets()
  {
    return totalTickets;
  }

  /**
   * @param totalTickets the totalTickets to set
   */
  public void setTotalTickets( Long totalTickets )
  {
    this.totalTickets = totalTickets;
  }

  /**
   * @return the statusTO
   */
  public CatalogTO getStatusTO()
  {
    return statusTO;
  }

  /**
   * @param statusTO the statusTO to set
   */
  public void setStatusTO( CatalogTO statusTO )
  {
    this.statusTO = statusTO;
  }

  /**
   * @return the statusTOs
   */
  public List<CatalogTO> getStatusTOs()
  {
    return statusTOs;
  }

  /**
   * @param statusTOs the statusTOs to set
   */
  public void setStatusTOs( List<CatalogTO> statusTOs )
  {
    this.statusTOs = statusTOs;
  }

  /**
   * @return the image
   */
  public String getImage()
  {
    return image;
  }

  /**
   * @param image the image to set
   */
  public void setImage( String image )
  {
    this.image = image;
  }

  /**
   * @return the statusIdSelected
   */
  public Object getStatusIdSelected()
  {
    return statusIdSelected;
  }

  /**
   * @param statusIdSelected the statusIdSelected to set
   */
  public void setStatusIdSelected( Object statusIdSelected )
  {
    this.statusIdSelected = statusIdSelected;
  }

  /**
   * @return the statusName
   */
  public String getStatusName()
  {
    return statusName;
  }

  /**
   * @param statusName the statusName to set
   */
  public void setStatusName( String statusName )
  {
    this.statusName = statusName;
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
   * @return the observationImage
   */
  public String getObservationImage()
  {
    return observationImage;
  }

  /**
   * @param observationImage the observationImage to set
   */
  public void setObservationImage( String observationImage )
  {
    this.observationImage = observationImage;
  }

  /**
   * @return the note
   */
  public String getNote()
  {
    return note;
  }

  /**
   * @param note the note to set
   */
  public void setNote( String note )
  {
    this.note = note;
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
   * @return the selectedShowings
   */
  public List<Object> getSelectedShowings()
  {
    return selectedShowings;
  }

  /**
   * @param selectedShowings the selectedShowings to set
   */
  public void setSelectedShowings( List<Object> selectedShowings )
  {
    this.selectedShowings = selectedShowings;
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
   * @return the events
   */
  public List<EventTO> getEvents()
  {
    return events;
  }

  /**
   * @param events the events to set
   */
  public void setEvents( List<EventTO> events )
  {
    this.events = events;
  }

  /**
   * @return the selectedEventId
   */
  public Long getSelectedEventId()
  {
    return selectedEventId;
  }

  /**
   * @param selectedEventId the selectedEventId to set
   */
  public void setSelectedEventId( Long selectedEventId )
  {
    this.selectedEventId = selectedEventId;
  }

  /**
   * @return the editable
   */
  public boolean isEditable()
  {
    return editable;
  }

  /**
   * @param editable the editable to set
   */
  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

  /**
   * @return the available
   */
  public boolean isAvailable()
  {
    return available;
  }

  /**
   * @param available the available to set
   */
  public void setAvailable( boolean available )
  {
    this.available = available;
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

  /**
   * @return the selectedScreens
   */
  public List<Object> getSelectedScreens()
  {
    return selectedScreens;
  }

  /**
   * @return the copies
   */
  public int getCopies()
  {
    return copies;
  }

  /**
   * @param copies the copies to set
   */
  public void setCopies( int copies )
  {
    this.copies = copies;
  }

  /**
   * @param selectedScreens the selectedScreens to set
   */
  public void setSelectedScreens( List<Object> selectedScreens )
  {
    this.selectedScreens = selectedScreens;
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

  @Override
  public int compareTo( BookingTheaterTO that )
  {
    CompareToBuilder compare = new CompareToBuilder();
    if( this.screenTO != null && that.screenTO != null )
    {
      compare.append( this.screenTO.getNuScreen(), that.screenTO.getNuScreen() );
    }
    if( this.eventTO != null && that.eventTO != null )
    {
      compare.append( this.eventTO.getDsEventName(), that.eventTO.getDsEventName() );
    }
    return compare.toComparison();
  }

  @Override
  public boolean equals( Object object )
  {
    boolean isEquals = false;
    if( this == object )
    {
      isEquals = true;
    }
    else if( object instanceof BookingTheaterTO )
    {
      isEquals = new EqualsBuilder().append( this.id, ((BookingTheaterTO) object).id ).isEquals();
    }
    return isEquals;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  /**
   * @return the previousNuScreen
   */
  public Integer getPreviousNuScreen()
  {
    return previousNuScreen;
  }

  /**
   * @param previousNuScreen the previousNuScreen to set
   */
  public void setPreviousNuScreen( Integer previousNuScreen )
  {
    this.previousNuScreen = previousNuScreen;
  }

  /**
   * @return the fgSpecialEvent
   */
  public boolean isFgSpecialEvent()
  {
    return fgSpecialEvent;
  }

  /**
   * @param fgSpecialEvent the fgSpecialEvent to set
   */
  public void setFgSpecialEvent( boolean fgSpecialEvent )
  {
    this.fgSpecialEvent = fgSpecialEvent;
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
   * @return the strDateSpecialEvents
   */
  public String getStrDateSpecialEvents()
  {
    return strDateSpecialEvents;
  }

  /**
   * @param strDateSpecialEvents the strDateSpecialEvents to set
   */
  public void setStrDateSpecialEvents( String strDateSpecialEvents )
  {
    this.strDateSpecialEvents = strDateSpecialEvents;
  }

  /**
   * @return the copyScreenZero
   */
  public int getCopyScreenZero()
  {
    return copyScreenZero;
  }

  /**
   * @param copyScreenZero the copyScreenZero to set
   */
  public void setCopyScreenZero( int copyScreenZero )
  {
    this.copyScreenZero = copyScreenZero;
  }

  /**
   * @return the fgShowDate
   */
  public boolean isFgShowDate()
  {
    return fgShowDate;
  }

  /**
   * @param fgShowDate the fgShowDate to set
   */
  public void setFgShowDate( boolean fgShowDate )
  {
    this.fgShowDate = fgShowDate;
  }

  /**
   * @return the concatName
   */
  public String getConcatName()
  {
    return this.idBookingType == ID_BOOKING_TYPE_SPECIAL_EVENT ? new StringBuilder( this.eventTO.getDsEventName() )
        .append( SPECIAL_EVENT_LABEL ).toString()
        : (this.idBookingType == ID_BOOKING_TYPE_PRE_SALE ? new StringBuilder( this.eventTO.getDsEventName() ).append(
          PRE_RELEASE_LABEL ).toString() : this.eventTO.getDsEventName());
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
   * @return the fridayTickets
   */
  public Long getFridayTickets()
  {
    return fridayTickets;
  }

  /**
   * @param fridayTickets the fridayTickets to set
   */
  public void setFridayTickets( Long fridayTickets )
  {
    this.fridayTickets = fridayTickets;
  }

  /**
   * @return the saturdayTickets
   */
  public Long getSaturdayTickets()
  {
    return saturdayTickets;
  }

  /**
   * @param saturdayTickets the saturdayTickets to set
   */
  public void setSaturdayTickets( Long saturdayTickets )
  {
    this.saturdayTickets = saturdayTickets;
  }

  /**
   * @return the sundayTickets
   */
  public Long getSundayTickets()
  {
    return sundayTickets;
  }

  /**
   * @param sundayTickets the sundayTickets to set
   */
  public void setSundayTickets( Long sundayTickets )
  {
    this.sundayTickets = sundayTickets;
  }

  /**
   * @return the fgPresaleSelected
   */
  public boolean isFgPresaleSelected()
  {
    return fgPresaleSelected;
  }

  /**
   * @param fgPresaleSelected the fgPresaleSelected to set
   */
  public void setFgPresaleSelected( boolean fgPresaleSelected )
  {
    this.fgPresaleSelected = fgPresaleSelected;
  }

  public PresaleTO getPresaleTO()
  {
    return presaleTO;
  }

  public void setPresaleTO( PresaleTO presaleTO )
  {
    this.presaleTO = presaleTO;
  }

}
