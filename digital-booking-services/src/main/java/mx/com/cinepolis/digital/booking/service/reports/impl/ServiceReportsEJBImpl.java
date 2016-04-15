package mx.com.cinepolis.digital.booking.service.reports.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportHeaderTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyDistributorReportHeaderTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyBookingReportTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyDistributorReportTO;
import mx.com.cinepolis.digital.booking.commons.utils.WeeklyBookingReportMovieTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.WeeklyBookingReportTheaterTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.BookingTOToWeeklyBookingReportMovieTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.RegionDOToRegionTOTransformer;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Clase que implementa los metodos asociados a la generacion de los reportes
 * 
 * @author rgarcia
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceReportsEJBImpl implements ServiceReportsEJB
{

  private static final String FILE_EXTENSION = ".xlsx";

  private static final String FILENAME = "Programación Semana ";
  private static final String FILENAME_PRESALE_PART1 = "Programación de Preventas, Semana ";
  private static final String FILENAME_PRESALE_PART2 = " - Zona ";
  private static final String FILENAME_PRESALE_PART3 = " - Theater ";

  private static final String DBS_WORKSHEET_NAME = "Cinepolis";

  private static final String REPORT_THEATER_HEADER_CURRENT_TIME = "Fecha: ";

  private static final String DATE_FORMAT_DD_MM_YYYY_HH_MM_SS_AA = "dd/MM/yyyy hh:mm:ss aa";

  private static final String DATE_FORMAT_DAY_MONTH = "dd/MM";

  private static final String DAYS_SEPARATOR = " - ";

  private static final String COLON_SEPARATOR = ": ";

  private static final String COMMA_SEPARATOR = ", ";

  private static final String REPORT_THEATERS_HEADER_EXHIBITION_WEEK = "DIAS DE EXHIBICIÓN SEMANA: ";

  private static final String REPORT_THEATERS_HEADER_EXHIBITION_WEEK_PRESALE = "DIAS DE PREVENTA SEMANA: ";

  private static final String REPORT_THEATERS_HEADER_TITLE = "HOJA DE TRABAJO DE PROGRAMACIÓN";

  private static final String REPORT_THEATERS_HEADER_PRESALE = "PREVENTA";

  private static final String REPORT_DISTRIBUTORS_HEADER_TITLE = "ESTADO DE PROGRAMACIÓN";

  private static final String REPORT_DISTRIBUTORS_HEADER_DISTRIBUTOR = "Distribuidor: ";

  private static final String REPORT_DISTRIBUTORS_HEADER_ALL_DISTRIBUTORS = "Todos los distribuidores";

  private static final String REPORT_DISTRIBUTORS_HEADER_EXHIBITION_DATE = "Fecha de exhibición: ";

  private static final String DATE_FORMAT_DAY_MONTH_YEAR = "dd/MM/yyyy";

  private static final String DS_FUNCTIONS = "Funciones:";

  private static final String DS_STATUS_CONTINUE = "Continuar";

  @EJB
  private ReportDAO reportDAO;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private DistributorDAO distributorDAO;
  @EJB
  private RegionDAO regionDAO;
  @EJB
  private BookingDAO bookingDAO;

  @Override
  public FileTO getWeeklyBookingReportByTheater( Long idWeek, Long idTheater )
  {
    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    WeeklyBookingReportHeaderTO header = new WeeklyBookingReportHeaderTO();
    header.setTitle( REPORT_THEATERS_HEADER_TITLE );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    header.setStrExhibitionWeek( getExhibitionWeek( weekTO ) );
    header.setStrCurrentDate( getCurrentDate() );
    report.setHeader( header );
    WeeklyBookingReportTheaterTO theater = reportDAO.findWeeklyBookingReportTheaterTO( idWeek, idTheater,
      Language.SPANISH );
    generateDsTheater( theater );
    report.setTheaters( new ArrayList<WeeklyBookingReportTheaterTO>() );
    report.getTheaters().add( theater );

    for( WeeklyBookingReportMovieTO movie : theater.getMovies() )
    {
      this.updateNote( movie );
    }

    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( FILENAME ).append( weekTO.getNuWeek() ).append( "-" )
        .append( theater.getTheaterName() ).append( FILE_EXTENSION ).toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyBookingFromList( report, DBS_WORKSHEET_NAME ) );

    return fileTO;
  }

  private List<BookingTO> findBookingByEventTheaterAndWeek( List<EventTO> movies, Long idWeek, Long idTheater )
  {
    List<BookingTO> bookingsTO = new ArrayList<BookingTO>();
    PagingRequestTO pagingRequestTO = new PagingRequestTO();

    // Sorting
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, idTheater );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    if( CollectionUtils.isNotEmpty( movies ) )
    {
      for( EventTO eventTO : movies )
      {
        pagingRequestTO.setUserId( eventTO.getUserId() );
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventTO.getIdEvent() );
        PagingResponseTO<BookingTO> bookingResponse = this.bookingDAO
            .findPresaleBookingsByIdEventIdRegionAndIdWeek( pagingRequestTO );
        if( CollectionUtils.isNotEmpty( bookingResponse.getElements() ) )
        {
          for( BookingTO bookingTO : bookingResponse.getElements() )
          {
            if( !CollectionUtils.exists(
              bookingsTO,
              PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getId" ),
                PredicateUtils.equalPredicate( bookingTO.getId() ) ) ) )
            {
              bookingsTO.add( bookingTO );
            }
          }
        }
      }

    }

    return bookingsTO;
  }

  @Override
  public FileTO getWeeklyBookingReportPresaleByTheater( List<EventTO> movies, Long idWeek, Long idTheater )
  {

    List<BookingTO> bookings = this.findBookingByEventTheaterAndWeek( movies, idWeek, idTheater );
    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    WeeklyBookingReportHeaderTO header = new WeeklyBookingReportHeaderTO();
    String theaterName = bookings.get( 0 ).getTheater().getName();
    header
        .setTitle( new StringBuilder( REPORT_THEATERS_HEADER_PRESALE ).append( " " ).append( theaterName ).toString() );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    header.setStrExhibitionWeek( getExhibitionWeekPresale( weekTO ) );
    header.setStrCurrentDate( getCurrentDate() );
    report.setHeader( header );

    List<WeeklyBookingReportTheaterTO> theaterList = new ArrayList<WeeklyBookingReportTheaterTO>();
    for( BookingTO booking : bookings )
    {
      WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO = new WeeklyBookingReportTheaterTO();
      weeklyBookingReportTheaterTO.setIdTheater( booking.getTheater().getId() );
      weeklyBookingReportTheaterTO.setTheaterName( booking.getTheater().getName() );
      weeklyBookingReportTheaterTO.setDsCity( booking.getTheater().getCity().getName() );
      weeklyBookingReportTheaterTO.setMovies( this.extractBookingsByScreen( bookings, booking ) );
      if( !CollectionUtils.exists(
        theaterList,
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdTheater" ),
          PredicateUtils.equalPredicate( weeklyBookingReportTheaterTO.getIdTheater() ) ) ) )
      {
        theaterList.add( weeklyBookingReportTheaterTO );
      }
    }
    report.setTheaters( theaterList );
    for( WeeklyBookingReportTheaterTO reportTheater : theaterList )
    {
      if( CollectionUtils.isNotEmpty( reportTheater.getMovies() ) )
      {
        for( WeeklyBookingReportMovieTO reportMovie : reportTheater.getMovies() )
        {
          this.updateNotePresale( reportMovie );
        }
      }
    }
    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( FILENAME_PRESALE_PART1 ).append( weekTO.getNuWeek() )
        .append( FILENAME_PRESALE_PART3 ).append( theaterName ).append( FILE_EXTENSION ).toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyBookingPresaleFromList( report, DBS_WORKSHEET_NAME ) );
    return fileTO;
  }

  private List<BookingTO> findBookingByEevntZoneAndWeek( List<EventTO> movies, Long idWeek, Long idRegion )
  {
    List<BookingTO> bookingsTO = new ArrayList<BookingTO>();
    PagingRequestTO pagingRequestTO = new PagingRequestTO();

    // Sorting
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, idRegion );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    if( CollectionUtils.isNotEmpty( movies ) )
    {
      for( EventTO eventTO : movies )
      {
        pagingRequestTO.setUserId( eventTO.getUserId() );
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventTO.getIdEvent() );
        PagingResponseTO<BookingTO> bookingResponse = this.bookingDAO
            .findPresaleBookingsByIdEventIdRegionAndIdWeek( pagingRequestTO );
        if( CollectionUtils.isNotEmpty( bookingResponse.getElements() ) )
        {
          for( BookingTO bookingTO : bookingResponse.getElements() )
          {
            if( !CollectionUtils.exists(
              bookingsTO,
              PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getId" ),
                PredicateUtils.equalPredicate( bookingTO.getId() ) ) ) )
            {
              bookingsTO.add( bookingTO );
            }
          }
        }
      }

    }

    return bookingsTO;
  }

  /**
   * Method that returns the bookings in presale by idWeek, idRegion and a eventTO list.
   * 
   * @param movies
   * @param idWeek
   * @param idRegion
   * @return bookingTO list
   */
  @Override
  public List<BookingTO> findBookingsInPresaleByEevntZoneAndWeek( List<EventTO> movies, Long idWeek, Long idRegion )
  {
    return this.findBookingByEevntZoneAndWeek( movies, idWeek, idRegion );
  }

  /**
   * crea reporte de programacion de preventa por region
   */
  @Override
  public FileTO getWeeklyBookingReportPresaleByRegion( List<EventTO> movies, Long idWeek, Long idRegion )
  {
    List<BookingTO> bookings = this.findBookingByEevntZoneAndWeek( movies, idWeek, idRegion );
    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    WeeklyBookingReportHeaderTO header = new WeeklyBookingReportHeaderTO();
    String zoneName = bookings.get( 0 ).getTheater().getRegion().getCatalogRegion().getName();
    header.setTitle( new StringBuilder( REPORT_THEATERS_HEADER_PRESALE ).append( " " ).append( zoneName ).toString() );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    header.setStrExhibitionWeek( getExhibitionWeekPresale( weekTO ) );
    header.setStrCurrentDate( getCurrentDate() );
    report.setHeader( header );

    List<WeeklyBookingReportTheaterTO> theaterList = new ArrayList<WeeklyBookingReportTheaterTO>();
    for( BookingTO booking : bookings )
    {
      WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO = new WeeklyBookingReportTheaterTO();
      weeklyBookingReportTheaterTO.setIdTheater( booking.getTheater().getId() );
      weeklyBookingReportTheaterTO.setTheaterName( booking.getTheater().getName() );
      weeklyBookingReportTheaterTO.setDsCity( booking.getTheater().getCity().getName() );
      weeklyBookingReportTheaterTO.setMovies( extractBookingsByScreen( bookings, booking ) );

      if( !CollectionUtils.exists(
        theaterList,
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdTheater" ),
          PredicateUtils.equalPredicate( weeklyBookingReportTheaterTO.getIdTheater() ) ) ) )
      {
        theaterList.add( weeklyBookingReportTheaterTO );
      }
    }
    Collections.sort( theaterList, new WeeklyBookingReportTheaterTOComparator() );
    report.setTheaters( theaterList );
    for( WeeklyBookingReportTheaterTO reportTheater : theaterList )
    {
      if( CollectionUtils.isNotEmpty( reportTheater.getMovies() ) )
        for( WeeklyBookingReportMovieTO reportMovie : reportTheater.getMovies() )
        {
          this.updateNotePresale( reportMovie );
        }
    }
    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( FILENAME_PRESALE_PART1 ).append( weekTO.getNuWeek() )
        .append( FILENAME_PRESALE_PART2 ).append( zoneName ).append( FILE_EXTENSION ).toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyBookingPresaleFromList( report, DBS_WORKSHEET_NAME ) );
    return fileTO;
  }

  /*
   * Se obtiene las programaciones de pelicula por cada sala
   */
  private List<WeeklyBookingReportMovieTO> extractBookingsByScreen( List<BookingTO> bookingsTO, BookingTO booking )
  {
    List<WeeklyBookingReportMovieTO> movies = new ArrayList<WeeklyBookingReportMovieTO>();
    for( BookingTO bookingTO : bookingsTO )
    {
      if( CollectionUtils.isNotEmpty( bookingTO.getScreens() )
          && bookingTO.getTheater().getId().equals( booking.getTheater().getId() ) )
      {
        for( ScreenTO screenTO : bookingTO.getScreens() )
        {
          if( screenTO.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
          {
            BookingTO bookingTO1 = bookingTO;
            bookingTO1.setScreen( screenTO );
            movies.add( (WeeklyBookingReportMovieTO) new BookingTOToWeeklyBookingReportMovieTOTransformer()
                .transform( bookingTO1 ) );
          }
        }
        bookingTO.setScreen( null );
      }
    }
    Collections.sort( movies, new WeeklyBookingReportMovieTOComparator() );
    return movies;
  }

  void updateNotePresale( WeeklyBookingReportMovieTO movie )
  {
    StringBuilder sb = new StringBuilder();
    if( movie.getDsFunctions() != null && !movie.getDsFunctions().isEmpty() )
    {

      sb.append( DS_FUNCTIONS ).append( " " );
      sb.append( movie.getDsFunctions() );
      sb.append( ". " );
    }
    if( movie.getDsDate() != null )
    {
      sb.append( movie.getDsDate() );
    }
    if( movie.getDsNote() != null && !movie.getDsNote().isEmpty() )
    {
      sb.append( movie.getDsNote() );
    }

    movie.setDsNote( sb.toString() );
  }

  void updateNote( WeeklyBookingReportMovieTO movie )
  {
    StringBuilder sb = new StringBuilder();
    if( movie.getDsFunctions() != null && !movie.getDsFunctions().isEmpty() )
    {
      sb.append( DS_FUNCTIONS ).append( " " );
      sb.append( movie.getDsFunctions() );
      sb.append( ". " );
    }
    if( movie.getDsNote() != null && !movie.getDsNote().isEmpty() )
    {
      sb.append( movie.getDsNote() );
    }

    movie.setDsNote( sb.toString() );
  }

  /**
   * Generate String dsTheater by header
   * 
   * @param report
   * @param theater
   */
  private void generateDsTheater( WeeklyBookingReportTheaterTO theater )
  {
    StringBuilder dsTheater = new StringBuilder();
    dsTheater.append( theater.getIdTheater() ).append( " - " ).append( theater.getTheaterName() ).append( ", " )
        .append( theater.getDsCity() );
    theater.setDsTheater( dsTheater.toString() );
  }

  private String getCurrentDate()
  {
    Calendar cal = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat( DATE_FORMAT_DD_MM_YYYY_HH_MM_SS_AA );
    StringBuilder sb = new StringBuilder();
    sb.append( REPORT_THEATER_HEADER_CURRENT_TIME ).append( df.format( cal.getTime() ) );
    return sb.toString();
  }

  private String getExhibitionWeek( WeekTO week )
  {
    DateFormat df = new SimpleDateFormat( DATE_FORMAT_DAY_MONTH );
    StringBuilder sb = new StringBuilder();
    sb.append( REPORT_THEATERS_HEADER_EXHIBITION_WEEK );
    sb.append( week.getNuWeek() ).append( COMMA_SEPARATOR ).append( week.getNuYear() ).append( COLON_SEPARATOR )
        .append( df.format( week.getStartingDayWeek() ) ).append( DAYS_SEPARATOR )
        .append( df.format( week.getFinalDayWeek() ) );
    return sb.toString();
  }

  private String getExhibitionWeekPresale( WeekTO week )
  {
    DateFormat df = new SimpleDateFormat( DATE_FORMAT_DAY_MONTH );
    StringBuilder sb = new StringBuilder();
    sb.append( REPORT_THEATERS_HEADER_EXHIBITION_WEEK_PRESALE );
    sb.append( week.getNuWeek() ).append( COMMA_SEPARATOR ).append( week.getNuYear() ).append( COLON_SEPARATOR )
        .append( df.format( week.getStartingDayWeek() ) ).append( DAYS_SEPARATOR )
        .append( df.format( week.getFinalDayWeek() ) );
    return sb.toString();
  }

  @Override
  public FileTO getWeeklyBookingReportByRegion( Long idWeek, Long idRegion )
  {
    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    WeeklyBookingReportHeaderTO header = new WeeklyBookingReportHeaderTO();
    header.setTitle( REPORT_THEATERS_HEADER_TITLE );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    RegionTO<CatalogTO, CatalogTO> regionTO = this.regionDAO.getRegionById( idRegion.intValue() );
    header.setStrExhibitionWeek( getExhibitionWeek( weekTO ) );
    header.setStrCurrentDate( getCurrentDate() );
    report.setHeader( header );
    List<WeeklyBookingReportTheaterTO> theaters = reportDAO.findAllWeeklyBookingReportTheaterTO( idWeek, idRegion,
      Language.SPANISH );
    // Se llena el encabezado del cine
    for( WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO : theaters )
    {
      generateDsTheater( weeklyBookingReportTheaterTO );
      for( WeeklyBookingReportMovieTO movie : weeklyBookingReportTheaterTO.getMovies() )
      {
        updateNote( movie );
      }
    }
    report.setTheaters( theaters );

    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( FILENAME ).append( weekTO.getNuWeek() ).append( " Zona " )
        .append( regionTO.getCatalogRegion().getName() ).append( FILE_EXTENSION ).toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyBookingFromList( report, DBS_WORKSHEET_NAME ) );
    return fileTO;
  }

  @SuppressWarnings("unchecked")
  @Override
  public FileTO getWeeklyDistributorReportByDistributor( Long idWeek, Long idRegion, Long idDistributor )
  {
    WorkSheetWeeklyDistributorReportTO report = new WorkSheetWeeklyDistributorReportTO();
    WeeklyDistributorReportHeaderTO header = new WeeklyDistributorReportHeaderTO();
    header.setTitle( REPORT_DISTRIBUTORS_HEADER_TITLE );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    header.setStrExhibitionWeek( getExhibitionDate( weekTO ) );

    List<WeeklyBookingReportTheaterTO> theaters = new ArrayList<WeeklyBookingReportTheaterTO>();
    List<RegionDO> regions = getRegions( idRegion );
    DistributorDO distributor = this.distributorDAO.find( idDistributor.intValue() );
    for( RegionDO regionDO : regions )
    {
      // Se obtiene la descripción de la región
      RegionTO<CatalogTO, CatalogTO> regionTo = (RegionTO<CatalogTO, CatalogTO>) new RegionDOToRegionTOTransformer(
          Language.SPANISH ).transform( regionDO );

      header.setStrDistributor( new StringBuilder( REPORT_DISTRIBUTORS_HEADER_DISTRIBUTOR ).append( " " )
          .append( distributor.getDsName() ).toString() );
      report.setHeader( header );
      List<WeeklyBookingReportTheaterTO> theatersByRegion = reportDAO.findWeeklyBookingReportTheaterTOByDistributor(
        idWeek, regionDO.getIdRegion().longValue(), idDistributor, Language.SPANISH );
      // Se llena el encabezado del cine
      for( WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO : theatersByRegion )
      {
        weeklyBookingReportTheaterTO.setDsTheater( weeklyBookingReportTheaterTO.getTheaterName() );
        weeklyBookingReportTheaterTO.setDsRegion( regionTo.getCatalogRegion().getName() );
        for( WeeklyBookingReportMovieTO movie : weeklyBookingReportTheaterTO.getMovies() )
        {
          updateDsStatus( movie, weekTO );
        }
      }
      theaters.addAll( theatersByRegion );
    }
    report.setTheaters( theaters );

    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( "Estado Programación Semana " ).append( distributor.getDsName() )
        .append( weekTO.getNuWeek() ).append( FILE_EXTENSION ).toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyDistributorFromList( report, DBS_WORKSHEET_NAME ) );

    return fileTO;
  }

  @SuppressWarnings("unchecked")
  @Override
  public FileTO getWeeklyDistributorReportByAllDistributors( Long idWeek, Long idRegion )
  {
    WorkSheetWeeklyDistributorReportTO report = new WorkSheetWeeklyDistributorReportTO();
    WeeklyDistributorReportHeaderTO header = new WeeklyDistributorReportHeaderTO();
    header.setTitle( REPORT_DISTRIBUTORS_HEADER_TITLE );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );
    header.setStrExhibitionWeek( getExhibitionDate( weekTO ) );

    List<WeeklyBookingReportTheaterTO> theaters = new ArrayList<WeeklyBookingReportTheaterTO>();
    List<RegionDO> regions = getRegions( idRegion );

    for( RegionDO regionDO : regions )
    {
      // Se obtiene la descripción de la región
      RegionTO<CatalogTO, CatalogTO> regionTo = (RegionTO<CatalogTO, CatalogTO>) new RegionDOToRegionTOTransformer(
          Language.SPANISH ).transform( regionDO );

      header.setStrDistributor( new StringBuilder( REPORT_DISTRIBUTORS_HEADER_DISTRIBUTOR ).append( " " )
          .append( REPORT_DISTRIBUTORS_HEADER_ALL_DISTRIBUTORS ).toString() );
      report.setHeader( header );
      List<WeeklyBookingReportTheaterTO> theatersByRegion = reportDAO
          .findWeeklyBookingReportTheaterTOByAllDistributors( idWeek, regionDO.getIdRegion().longValue(),
            Language.SPANISH );
      // Se llena el encabezado del cine
      for( WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO : theatersByRegion )
      {
        weeklyBookingReportTheaterTO.setDsTheater( weeklyBookingReportTheaterTO.getTheaterName() );
        weeklyBookingReportTheaterTO.setDsRegion( regionTo.getCatalogRegion().getName() );
        for( WeeklyBookingReportMovieTO movie : weeklyBookingReportTheaterTO.getMovies() )
        {
          updateDsStatus( movie, weekTO );
        }
      }
      theaters.addAll( theatersByRegion );
    }
    report.setTheaters( theaters );

    FileTO fileTO = new FileTO();
    fileTO.setName( new StringBuilder( "Estado Programación Semana " )
        .append( REPORT_DISTRIBUTORS_HEADER_ALL_DISTRIBUTORS ).append( weekTO.getNuWeek() ).append( FILE_EXTENSION )
        .toString() );
    XlsxUtils xlsxUtils = new XlsxUtils();
    fileTO.setFile( xlsxUtils.createFileWeeklyDistributorFromList( report, DBS_WORKSHEET_NAME ) );

    return fileTO;
  }

  private List<RegionDO> getRegions( Long idRegion )
  {
    List<RegionDO> regions = new ArrayList<RegionDO>();
    if( idRegion == null )
    {
      for( RegionDO regionDO : regionDAO.findAll() )
      {
        if( regionDO.isFgActive() )
        {
          regions.add( regionDO );
        }
      }
    }
    else
    {
      RegionDO regionDO = regionDAO.find( idRegion.intValue() );
      regions.add( regionDO );
    }
    return regions;
  }

  private String getExhibitionDate( WeekTO weekTO )
  {
    DateFormat df = new SimpleDateFormat( DATE_FORMAT_DAY_MONTH_YEAR );
    StringBuilder sb = new StringBuilder();
    sb.append( REPORT_DISTRIBUTORS_HEADER_EXHIBITION_DATE );
    sb.append( df.format( weekTO.getStartingDayWeek() ) );
    return sb.toString();
  }

  /**
   * Update String strStatus with date
   * 
   * @param report
   * @param theater
   */
  private void updateDsStatus( WeeklyBookingReportMovieTO movie, WeekTO weekTO )
  {
    StringBuilder strDsStatus = new StringBuilder();
    if( movie.getBookingStatus().getId() == BookingStatus.BOOKED.getId() && movie.getNuWeek() > 1 )
    {
      movie.setToBeContinue( true );
      movie.setDsStatus( DS_STATUS_CONTINUE );
    }
    strDsStatus.append( movie.getDsStatus() );
    strDsStatus.append( " " ).append( getChangeStatusDate( weekTO, movie ) );
    movie.setDsStatus( strDsStatus.toString() );
  }

  private String getChangeStatusDate( WeekTO weekTO, WeeklyBookingReportMovieTO movie )
  {
    DateFormat df = new SimpleDateFormat( DATE_FORMAT_DAY_MONTH );
    StringBuilder sb = new StringBuilder();
    sb.append( "(" );
    String strDate = null;
    switch( movie.getBookingStatus() )
    {
      case BOOKED:
        // Si va a continuar se obtiene la fecha de fin de semana
        if( movie.isToBeContinue() )
        {
          strDate = df.format( weekTO.getFinalDayWeek() );
        }
        else
        {// De lo contrario de obtiene la de inicio de semana
          strDate = df.format( weekTO.getStartingDayWeek() );
        }
        break;
      case CONTINUE:
        strDate = df.format( weekTO.getFinalDayWeek() );
        break;
      case TERMINATED:
        Calendar cal = Calendar.getInstance();
        cal.setTime( weekTO.getStartingDayWeek() );
        cal.add( Calendar.DATE, -1 );
        strDate = df.format( cal.getTime() );
        break;
      default:
        break;
    }
    sb.append( strDate );
    sb.append( ")" );
    return sb.toString();
  }

}
