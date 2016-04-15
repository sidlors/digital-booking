package mx.com.cinepolis.digital.booking.web.beans.booking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Backing bean for the theater movie booking edition user interface.
 * 
 * @author kperez
 * @author afuentes
 */
@ManagedBean(name = "editTheaterBookingBean")
@ViewScoped
public class EditTheaterBookingBean extends BaseManagedBean
{
  private static final long serialVersionUID = -2497455172700310771L;
  private static final Logger LOG = LoggerFactory.getLogger( EditTheaterBookingBean.class );

  private static final String SELECTION_NOT_FOUND = "Selection was not found";
  private static final String THEATER_BOOKING_URL = "theaterBooking.do";
  private static final String HOME_URL = "../../home/home.do";
  private static final String THEATER_ID_SELECTED_ATTRIBUTE = "theaterIdSelected";
  private static final String WEEK_ID_SELECTED_ATTRIBUTE = "weekIdSelected";
  private static final String REGION_ID_SELECTED_ATTRIBUTE = "regionIdSelected";
  private static final String BOOKINGS_UNEDITED_TEXT = "booking.theater.edit.mesgerror.bookingsUneditedText";

  private static final String VALUE_ATTRIBUTE = "value";
  private static final String SCREEN_NUMBERS_DISPLAY_FACET = "output";
  private static final int INDEX_SCREEN_NUMBERS_COLUMN = 2;
  private static final int INDEX_SCREEN_NUMBERS_CELL_EDITOR = 0;
  private static final String ZERO_STRING = "0";
  private static final int INDEX_COPIES_COLUMN = 1;
  private static final int INDEX_COPIES_CELL_EDITOR = 0;
  private static final String NAME_COPIES_DISPLAY_FACET = "output";

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  private List<BookingTO> bookingTOs;
  private BookingTO bookingTOSelected;
  private Long regionIdSelected;
  private Long weekIdSelected;
  private Long theaterIdSelected;
  private List<String> lastScreenSelection = new ArrayList<String>();
  private List<Object> originalScreenSelection = new ArrayList<Object>();
  private Integer originalNumCopies;

  /**
   * Method that performs initializations.
   * 
   * @throws IOException
   */
  @PostConstruct
  public void loadData()
  {
    Set<Long> editableBookingStatus = new HashSet<Long>();
    editableBookingStatus.add( BookingStatus.BOOKED.getIdLong() );
    editableBookingStatus.add( BookingStatus.BOOKED.getIdLong() );
    editableBookingStatus.add( BookingStatus.BOOKED.getIdLong() );

    Set<Long> preselectedForSavingBookingStatus = new HashSet<Long>();
    preselectedForSavingBookingStatus.add( BookingStatus.BOOKED.getIdLong() );

    if( isValidSelection() )
    {
      regionIdSelected = (Long) getSession().getAttribute( REGION_ID_SELECTED_ATTRIBUTE );
      weekIdSelected = (Long) getSession().getAttribute( WEEK_ID_SELECTED_ATTRIBUTE );
      theaterIdSelected = (Long) getSession().getAttribute( THEATER_ID_SELECTED_ATTRIBUTE );

      getSession().removeAttribute( REGION_ID_SELECTED_ATTRIBUTE );
      getSession().removeAttribute( WEEK_ID_SELECTED_ATTRIBUTE );
      getSession().removeAttribute( THEATER_ID_SELECTED_ATTRIBUTE );

      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( super.getUserId() );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, theaterIdSelected );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekIdSelected );
      pagingRequestTO.setNeedsPaging( Boolean.FALSE );
      PagingResponseTO<BookingTO> response = bookingServiceIntegratorEJB.findBookingMoviesByTheater( pagingRequestTO );
      bookingTOs = response.getElements();
      for( BookingTO bookingTO : bookingTOs )
      {
        CatalogTO bookingStatus = bookingTO.getStatus();
        bookingTO.setIsEditable( bookingStatus == null || editableBookingStatus.contains( bookingStatus.getId() ) );
        bookingTO.setSelectedForSaving( bookingStatus != null
            && preselectedForSavingBookingStatus.contains( bookingStatus.getId() ) );
      }
    }
    else
    {
      try
      {
        redirectToHome();
      }
      catch( IOException e )
      {
        LOG.debug( SELECTION_NOT_FOUND, e );
      }
    }
  }

  private void redirectToHome() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( HOME_URL );
  }

  private boolean isValidSelection()
  {
    return getSession().getAttribute( REGION_ID_SELECTED_ATTRIBUTE ) != null
        && getSession().getAttribute( WEEK_ID_SELECTED_ATTRIBUTE ) != null
        && getSession().getAttribute( THEATER_ID_SELECTED_ATTRIBUTE ) != null;
  }

  /**
   * Listener method for the theater screens selectManyButton valueChange event.
   * 
   * @param event Object {@link ValueChangeEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void changeValue( ValueChangeEvent event )
  {
    lastScreenSelection = parseStringSelection( (List<Object>) event.getOldValue() );
  }

  private List<String> parseStringSelection( List<Object> oldObjectSelection )
  {
    List<String> parsedStrings = new ArrayList<String>();
    for( Object object : oldObjectSelection )
    {
      if( object != null )
      {
        parsedStrings.add( object.toString() );
      }
    }
    return parsedStrings;
  }

  /**
   * Listener method for the theater screens selectManyButton ajax event.
   * 
   * @param event Object {@link AjaxBehaviorEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void loadScreens( AjaxBehaviorEvent event )
  {
    String zeroString = ZERO_STRING;
    Long zeroLong = Long.valueOf( 0L );
    List<Object> screensSelected = ((List<Object>) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE ));
    List<String> stringsScreensSelected = parseStringSelection( screensSelected );
    if( !lastScreenSelection.contains( zeroString ) && stringsScreensSelected.contains( zeroString ) )
    {
      screensSelected.retainAll( Arrays.asList( zeroString, zeroLong ) );
    }
    else if( lastScreenSelection.contains( zeroString ) && stringsScreensSelected.size() > lastScreenSelection.size() )
    {
      screensSelected.remove( zeroString );
      screensSelected.remove( zeroLong );
    }
    else if( CollectionUtils.isEmpty( stringsScreensSelected ) )
    {
      screensSelected.add( zeroString );
    }
  }

  /**
   * Listener method for the theater list dataTable rowEdit event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  public void onEdit( RowEditEvent event )
  {
    BookingTO bookingTO = (BookingTO) event.getObject();
    Integer copy = bookingTO.getCopy();
    if( copy.equals( Integer.valueOf( 0 ) ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUMBER_COPIES_ZERO );
    }
    List<Object> screenSelected = bookingTO.getScreensSelected();
    if( screenSelected.size() > copy )
    {
      restoreOriginalNumCopies( bookingTO );
      restoreOriginalScreenSelection( screenSelected );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUM_SCREENS_GREATER_NUM_COPIES );
    }
    bookingTO.setSelectedForSaving( true );
  }

  /**
   * Listener method for the theater list dataTable rowEditInit event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void onRowEditInit( RowEditEvent event )
  {
    originalScreenSelection = (List<Object>) event.getComponent().getChildren().get( INDEX_SCREEN_NUMBERS_COLUMN )
        .getChildren().get( INDEX_SCREEN_NUMBERS_CELL_EDITOR ).getFacet( SCREEN_NUMBERS_DISPLAY_FACET ).getAttributes()
        .get( VALUE_ATTRIBUTE );
    originalNumCopies = (Integer) event.getComponent().getChildren().get( INDEX_COPIES_COLUMN ).getChildren()
        .get( INDEX_COPIES_CELL_EDITOR ).getFacet( NAME_COPIES_DISPLAY_FACET ).getAttributes().get( VALUE_ATTRIBUTE );
  }

  /**
   * Listener method for the theater list dataTable rowEditCancel event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void onRowEditCancel( RowEditEvent event )
  {
    List<Object> screensSelected = (List<Object>) event.getComponent().getChildren().get( INDEX_SCREEN_NUMBERS_COLUMN )
        .getChildren().get( INDEX_SCREEN_NUMBERS_CELL_EDITOR ).getFacet( SCREEN_NUMBERS_DISPLAY_FACET ).getAttributes()
        .get( VALUE_ATTRIBUTE );
    restoreOriginalScreenSelection( screensSelected );
  }

  private void restoreOriginalScreenSelection( List<Object> screensSelected )
  {
    List<Object> copyScreensSelected = new ArrayList<Object>();
    copyScreensSelected.addAll( screensSelected );
    for( Object object : copyScreensSelected )
    {
      if( !originalScreenSelection.contains( object ) )
      {
        screensSelected.remove( object );
      }
    }
    for( Object oldObject : originalScreenSelection )
    {
      if( !screensSelected.contains( oldObject ) )
      {
        screensSelected.add( oldObject );
      }
    }
  }

  private void restoreOriginalNumCopies( BookingTO bookingTO )
  {
    bookingTO.setCopy( originalNumCopies );
  }

  public void back() throws IOException
  {
    getSession().setAttribute( WEEK_ID_SELECTED_ATTRIBUTE, weekIdSelected );
    getSession().setAttribute( THEATER_ID_SELECTED_ATTRIBUTE, theaterIdSelected );
    getSession().setAttribute( REGION_ID_SELECTED_ATTRIBUTE, regionIdSelected );

    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( THEATER_BOOKING_URL );
  }

  public void editBookingTheater() throws IOException
  {
    List<BookingTO> bookingsForSaving = new ArrayList<BookingTO>();
    for( BookingTO bookingTO : bookingTOs )
    {
      if( bookingTO.getSelectedForSaving() )
      {
        super.fillSessionData( bookingTO );
        if( bookingTO.getCopy() == 0 )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_COPIES_IS_NULL );
        }
        List<ScreenTO> screensSelected = new ArrayList<ScreenTO>();
        for( Object ob : bookingTO.getScreensSelected() )
        {
          ScreenTO screen = new ScreenTO();
          AbstractTOUtils.copyElectronicSign( bookingTO, screen );
          screen.setId( Long.valueOf( ob.toString() ) );
          screensSelected.add( screen );
        }
        bookingTO.setScreens( screensSelected );
        bookingsForSaving.add( bookingTO );
        bookingTO.setSelectedForSaving( false );
      }
    }

    if( CollectionUtils.isEmpty( bookingsForSaving ) )
    {
      createMessageError( BOOKINGS_UNEDITED_TEXT );
    }
    else
    {
      bookingServiceIntegratorEJB.saveOrUpdateBookings( bookingsForSaving );
    }
  }

  /* Getters and setters */

  /**
   * @return the bookingTOs
   */
  public List<BookingTO> getBookingTOs()
  {
    return bookingTOs;
  }

  /**
   * @param bookingTOs the bookingTOs to set
   */
  public void setBookingTOs( List<BookingTO> bookingTOs )
  {
    this.bookingTOs = bookingTOs;
  }

  /**
   * @return the bookingTOSelected
   */
  public BookingTO getBookingTOSelected()
  {
    return bookingTOSelected;
  }

  /**
   * @param bookingTOSelected the bookingTOSelected to set
   */
  public void setBookingTOSelected( BookingTO bookingTOSelected )
  {
    this.bookingTOSelected = bookingTOSelected;
  }

  /**
   * @return the originalScreenSelection
   */
  public List<Object> getOriginalScreenSelection()
  {
    return originalScreenSelection;
  }

  /**
   * @param originalScreenSelection the originalScreenSelection to set
   */
  public void setOriginalScreenSelection( List<Object> originalScreenSelection )
  {
    this.originalScreenSelection = originalScreenSelection;
  }

  /**
   * @return the lastScreenSelection
   */
  public List<String> getLastScreenSelection()
  {
    return lastScreenSelection;
  }

  /**
   * @param lastScreenSelection the lastScreenSelection to set
   */
  public void setLastScreenSelection( List<String> lastScreenSelection )
  {
    this.lastScreenSelection = lastScreenSelection;
  }

}
