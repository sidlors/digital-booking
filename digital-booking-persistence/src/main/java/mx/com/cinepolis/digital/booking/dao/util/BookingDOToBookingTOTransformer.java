package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.cinepolis.digital.booking.commons.constants.BookingShow;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenShowDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.BookingDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO }
 * 
 * @author gsegura
 * @author afuentes
 * @since 0.2.0
 */
public class BookingDOToBookingTOTransformer implements Transformer
{

  private Language language;
  private Integer idWeek;

  /**
   * Constructor default
   */
  public BookingDOToBookingTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor by language
   * 
   * @param language
   */
  public BookingDOToBookingTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object transform( Object object )
  {
    BookingTO bookingTO = null;
    if( object instanceof BookingDO )
    {
      BookingDO bookingDO = (BookingDO) object;
      bookingTO = new BookingTO();
      bookingTO
          .setEvent( (EventTO) new EventDOToEventTOTransformer( this.language ).transform( bookingDO.getIdEvent() ) );

      bookingTO.setFgActive( bookingDO.isFgActive() );

      bookingTO.setId( bookingDO.getIdBooking() );
      bookingTO.setIdBookingType( bookingDO.getIdBookingType().getIdBookingType() );

      bookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer( this.language ).transform( bookingDO
          .getIdTheater() ) );
      bookingTO.setTimestamp( bookingDO.getDtLastModification() );

      bookingTO.setUserId( Long.valueOf( bookingDO.getIdLastUserModifier() ) );

      List<ScreenDO> selectedScreenDOs = new ArrayList<ScreenDO>();
      Map<ScreenDO, List<CatalogTO>> showsPerScreen = new HashMap<ScreenDO, List<CatalogTO>>();
      Map<ScreenDO, BookingObservationTO> observations = new HashMap<ScreenDO, BookingObservationTO>();
      Map<ScreenDO, CatalogTO> statusPerScreen = new HashMap<ScreenDO, CatalogTO>();
      Map<ScreenDO, PresaleTO> presalePerScreen = new HashMap<ScreenDO, PresaleTO>();
      if( this.idWeek != null && CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList() ) )
      {
        for( BookingWeekDO bookingWeek : bookingDO.getBookingWeekDOList() )
        {
          if( bookingWeek.getIdWeek().getIdWeek().equals( this.idWeek ) && bookingWeek.isFgActive() )
          {
            bookingTO.setIdBookingWeek( bookingWeek.getIdBookingWeek() );
            bookingTO.setSent( bookingWeek.isFgSend() );
            bookingTO.setCopy( bookingWeek.getQtCopy() );
            bookingTO.setCopyScreenZero( bookingWeek.getQtCopyScreenZero() );
            bookingTO.setCopyScreenZeroTerminated( bookingWeek.getQtCopyScreenZeroTerminated() );
            bookingTO.setExhibitionWeek( bookingWeek.getNuExhibitionWeek() );
            bookingTO.setStatus( (CatalogTO) new BookingStatusDOToCatalogTOTransformer( this.language )
                .transform( bookingWeek.getIdBookingStatus() ) );
            bookingTO.setExhibitionEnd( bookingWeek.getDtExhibitionEndDate() );
            bookingTO.setWeek( (WeekTO) new WeekDOToWeekTOTransformer().transform( bookingWeek.getIdWeek() ) );
            selectedScreenDOs.addAll( getScreens( bookingWeek.getBookingWeekScreenDOList(), showsPerScreen,
              observations, statusPerScreen, presalePerScreen) );
            break;
          }
        }
      }
      else if( CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList() ) )
      {
        BookingWeekDO bookingWeek = bookingDO.getBookingWeekDOList().get( 0 );
        bookingTO.setIdBookingWeek( bookingWeek.getIdBookingWeek() );
        bookingTO.setCopy( bookingWeek.getQtCopy() );
        bookingTO.setCopyScreenZero( bookingWeek.getQtCopyScreenZero() );
        bookingTO.setCopyScreenZeroTerminated( bookingWeek.getQtCopyScreenZeroTerminated() );
        bookingTO.setExhibitionWeek( bookingWeek.getNuExhibitionWeek() );
        bookingTO.setStatus( (CatalogTO) new BookingStatusDOToCatalogTOTransformer( this.language )
            .transform( bookingWeek.getIdBookingStatus() ) );
        bookingTO.setExhibitionEnd( bookingWeek.getDtExhibitionEndDate() );
        bookingTO.setWeek( (WeekTO) new WeekDOToWeekTOTransformer().transform( bookingWeek.getIdWeek() ) );
        selectedScreenDOs.addAll( getScreens( bookingWeek.getBookingWeekScreenDOList(), showsPerScreen, observations,
          statusPerScreen, presalePerScreen ) );
      }
      if( CollectionUtils.isNotEmpty( bookingDO.getBookingSpecialEventDOList() ) )
      {
        BookingSpecialEventDOToSpecialEventTOTransformer tranformer = new BookingSpecialEventDOToSpecialEventTOTransformer();
        List<SpecialEventTO> specialEventTOList = new ArrayList<SpecialEventTO>();
        for( BookingSpecialEventDO specialEvent : bookingDO.getBookingSpecialEventDOList() )
        {
          specialEventTOList.add( (SpecialEventTO) tranformer.transform( (Object) specialEvent ) );
        }
        bookingTO.setStatus( (CatalogTO) new BookingStatusDOToCatalogTOTransformer( this.language )
            .transform( bookingDO.getBookingSpecialEventDOList().get( 0 ).getIdBookingStatus() ) );
        bookingTO.setSpecialEvents( specialEventTOList );
      }

      bookingTO.setScreens( new ArrayList<ScreenTO>() );
      bookingTO.getScreens().addAll( CollectionUtils.collect( selectedScreenDOs, new ScreenDOToScreenTOTransformer() ) );
      // putPresales(bookingTO, bookingWeek);
      for( ScreenTO screenTO : bookingTO.getScreens() )
      {
        ScreenDO screenDO = new ScreenDO( screenTO.getId().intValue() );
        screenTO.setBookingObservation( observations.get( screenDO ) );
        screenTO.setShowings( showsPerScreen.get( screenDO ) );
        screenTO.setBookingStatus( statusPerScreen.get( screenDO ) );
        screenTO.setPresaleTO( presalePerScreen.get( screenDO ) );
      }

      BookingDAOUtil.markSelectedAndDisabledScreens( bookingTO.getTheater().getScreens(), selectedScreenDOs,
        bookingDO.getIdEvent() );
      BookingDAOUtil.addScreenZero( bookingTO.getTheater().getScreens(),
        CollectionUtils.isEmpty( selectedScreenDOs ) ? 0 : selectedScreenDOs.size() );
    }
    return bookingTO;
  }

  private List<ScreenDO> getScreens( List<BookingWeekScreenDO> bookingWeekScreens,
      Map<ScreenDO, List<CatalogTO>> showsPerScreen, Map<ScreenDO, BookingObservationTO> observations,
      Map<ScreenDO, CatalogTO> statusPerScreen ,Map<ScreenDO, PresaleTO> presalePerScreen)
  {
    List<ScreenDO> selectedScreenDOs = new ArrayList<ScreenDO>();
    if( CollectionUtils.isNotEmpty( bookingWeekScreens ) )
    {
      for( BookingWeekScreenDO bookingWeekScreen : bookingWeekScreens )
      {
        List<CatalogTO> shows = new ArrayList<CatalogTO>();
        if( CollectionUtils.isNotEmpty( bookingWeekScreen.getBookingWeekScreenShowDOList() ) )
        {
          for( BookingWeekScreenShowDO bookingWeekScreenShowDO : bookingWeekScreen.getBookingWeekScreenShowDOList() )
          {
            shows.add( BookingShow.fromId( Long.valueOf( bookingWeekScreenShowDO.getNuShow() ) )
                .getShow( this.language ) );
          }
        }
        if(CollectionUtils.isNotEmpty(bookingWeekScreen.getPresaleDOList()))
        {
        presalePerScreen
        .put(
          bookingWeekScreen.getIdScreen(),
          (PresaleTO) new PresaleDOToPresaleTOTransformer().transform( bookingWeekScreen.getPresaleDOList().get(
            0 ) ) );
        }
        showsPerScreen.put( bookingWeekScreen.getIdScreen(), shows );
        if( bookingWeekScreen.getIdObservation() != null )
        {
          BookingObservationTO observation = getObservation( bookingWeekScreen.getIdBookingWeekScreen(),
            bookingWeekScreen.getIdObservation() );
          observations.put( bookingWeekScreen.getIdScreen(), observation );
        }
        CatalogTO status = (CatalogTO) new BookingStatusDOToCatalogTOTransformer( this.language )
            .transform( bookingWeekScreen.getIdBookingStatus() );
        statusPerScreen.put( bookingWeekScreen.getIdScreen(), status );
        selectedScreenDOs.add( bookingWeekScreen.getIdScreen() );
      }
    }
    return selectedScreenDOs;
  }

  private BookingObservationTO getObservation( Long idBookingWeekScreen, ObservationDO idObservation )
  {
    BookingObservationTO observation = new BookingObservationTO();
    observation.setId( idObservation.getIdObservation() );
    observation.setIdBooking( idBookingWeekScreen );
    observation.setObservation( idObservation.getDsObservation() );

    PersonTO personTO = (PersonTO) new PersonDOToPersonTOTransformer().transform( idObservation.getIdUser()
        .getIdPerson() );
    observation.setPersonTO( personTO );
    observation.setFgActive( idObservation.isFgActive() );
    observation.setUserId( idObservation.getIdUser().getIdUser().longValue() );
    observation.setTimestamp( idObservation.getDtLastModification() );
    return observation;
  }

  /**
   * @param language the language to set
   */
  public void setLanguage( Language language )
  {
    this.language = language;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

}
