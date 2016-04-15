package mx.com.cinepolis.digital.booking.service.income.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.IncomeSettingsType;
import mx.com.cinepolis.digital.booking.commons.constants.Semaphore;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.SummaryTotalIncomesTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.dao.util.WeekDOToWeekTOTransformer;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.TheaterDOToTheaterTOSimpleTransformer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements the IncomeServiceEJB
 * 
 * @author gsegura
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class IncomeServiceEJBImpl implements IncomeServiceEJB
{

  private static final Logger LOG = LoggerFactory.getLogger( IncomeServiceEJBImpl.class );

  @EJB
  private BookingIncomeDAO bookingIncomeDAO;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private IncomeSettingsDAO incomeSettingsDAO;

  private enum IncomeType
  {
    WEEKEND, WEEK
  };
  private Map<IncomeType, Set<Integer>> days;

  public IncomeServiceEJBImpl()
  {
    init();
  }

  private void init()
  {
    days = new HashMap<IncomeServiceEJBImpl.IncomeType, Set<Integer>>();
    days.put(
      IncomeType.WEEK,
      new HashSet<Integer>( Arrays.asList( Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY, Calendar.MONDAY,
        Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY ) ) );
    days.put( IncomeType.WEEKEND,
      new HashSet<Integer>( Arrays.asList( Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY ) ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeIncomes( TheaterTO theater )
  {
    this.bookingIncomeDAO.synchronizeIncomes( theater );
  }

  /**
   * 
   */
  private void getSummaryTotalIncomes( IncomeTreeTO incomeTreeTO, IncomeTO incomeTO )
  {
    SummaryTotalIncomesTO summaryTotalsTO = new SummaryTotalIncomesTO();

    if( CollectionUtils.isNotEmpty( incomeTreeTO.getChildren() ) )
    {
      int totWeekendAdmission = 0;
      int totWeekAdmission = 0;

      int totWeekendShows = 0;
      int totWeekShows = 0;

      double totWeekendGross =0.0;
      double totWeekGross = 0.0;

      double sumWeekendCapacityPerShows = 0.0;
      double sumWeekCapacityPerShows = 0.0;

      for( IncomeTreeTO incomeTree : incomeTreeTO.getChildren() )
      {

        totWeekAdmission += incomeTree.getWeekTickets();
        totWeekendAdmission += incomeTree.getWeekendTickets();

        totWeekendShows += incomeTree.getWeekendQuantityShows();
        totWeekShows += incomeTree.getWeekQuantityShows();

        totWeekendGross += incomeTree.getWeekendIncome();
        totWeekGross += incomeTree.getWeekIncome();

        sumWeekendCapacityPerShows += incomeTree.getWeekendQuantityShows()
            * incomeTree.getBooking().getScreen().getNuCapacity();
        sumWeekCapacityPerShows += incomeTree.getWeekQuantityShows()
            * incomeTree.getBooking().getScreen().getNuCapacity();

      }
      
      double totWeekendAdmissionPerShows = 0.0;
      double totWeekAdmissionPerShows = 0.0;
      if(totWeekendShows!=0)
      {
        totWeekendAdmissionPerShows = (double) (totWeekendAdmission / totWeekendShows);
      }
      if(totWeekShows!=0)
      
      {
        totWeekAdmissionPerShows = (double) (totWeekAdmission / totWeekShows);
      }

      summaryTotalsTO.setTotWeekAdmission( totWeekAdmission );
      summaryTotalsTO.setTotWeekendAdmission( totWeekendAdmission );

      summaryTotalsTO.setTotWeekAdmissionPerShows( totWeekAdmissionPerShows );
      summaryTotalsTO.setTotWeekendAdmissionPerShows( totWeekendAdmissionPerShows );

      summaryTotalsTO.setTotWeekendGross( totWeekendGross );
      summaryTotalsTO.setTotWeekGross( totWeekGross );

      summaryTotalsTO.setTotWeekShows( totWeekShows );
      summaryTotalsTO.setTotWeekendShows( totWeekendShows );

      if(sumWeekendCapacityPerShows!=0)
      {
      summaryTotalsTO.setTotWeekendScreenOccupancy( (double) (totWeekendAdmission / sumWeekendCapacityPerShows ));
      }
      else
      {
        summaryTotalsTO.setTotWeekendScreenOccupancy(0.0);
      }
      if(sumWeekCapacityPerShows!=0)
      {
      summaryTotalsTO.setTotWeekScreenOccupancy( (double)(totWeekAdmission / sumWeekCapacityPerShows ));
      }
      else
      {
        summaryTotalsTO.setTotWeekScreenOccupancy(0.0);
      }

      WeekTO weekTO = this.getLastWeek( incomeTO.getWeek() );

      double totalPrevWeekAdmission = this.bookingIncomeDAO.getTotalWeekAdmissionByTheaterAndWeek( incomeTO
          .getTheater().getId(), weekTO.getIdWeek().longValue() );
      double totalPrevWeekendAdmission = this.bookingIncomeDAO.getTotalWeekendAdmissionByTheaterAndWeek( incomeTO
          .getTheater().getId(), weekTO.getIdWeek().longValue() );

      if(totalPrevWeekAdmission!=0)
      {
      summaryTotalsTO.setTotWeekChgPrewWeek( (double)(totalPrevWeekAdmission - totWeekAdmission) / totalPrevWeekAdmission );
      }
      else
      {
        summaryTotalsTO.setTotWeekChgPrewWeek(0.0);
      }
      if(totalPrevWeekendAdmission!=0)
      {
      summaryTotalsTO.setTotWeekendChgPrewWeek( (double)(totalPrevWeekendAdmission - totWeekendAdmission) / totalPrevWeekendAdmission );
      }
      else
      {
        summaryTotalsTO.setTotWeekendChgPrewWeek(0.0);
      }

      incomeTreeTO.setSummaryTotals( summaryTotalsTO );
    }
  }

  @Override
  public IncomeTreeTO searchIncomesByMovie( IncomeTO income )
  {
    IncomeTreeTO root = new IncomeTreeTO();
    root.setBooking( new BookingTO() );
    root.getBooking().setTheater( income.getTheater() );
    List<IncomeTO> result = this.bookingIncomeDAO.findAllByIdTheaterIdWeek( income );

    // jreyesv - Se inicializan los objetos theater y week para el incomeTreeTO y se puedan visualizar en pantalla sin
    // importar si hay o no ingresos para el cine seleccionado en la semana seleccionada.
    TheaterTO theaterTO = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer().transform( this.theaterDAO
        .find( income.getTheater().getId().intValue() ) );
    root.getBooking().setTheater( theaterTO );
    WeekTO weekTO = this.weekDAO.getWeek( income.getWeek().getIdWeek() );
    root.getBooking().setWeek( weekTO );

    if( CollectionUtils.isNotEmpty( result ) )
    {
      BookingTO booking = new BookingTO();
      IncomeTO first = (IncomeTO) CollectionUtils.find( result, PredicateUtils.notNullPredicate() );
      booking.setTheater( first.getTheater() );
      booking.setWeek( first.getWeek() );
      root.setBooking( booking );
      root.setType( IncomeTreeTO.Type.ROOT );

      for( IncomeTO to : result )
      {
        IncomeTreeTO movie = getMovie( root, to );
        IncomeTreeTO screen = getScreen( movie, to );
        IncomeTreeTO show = getShow( screen, to );
        addIncomesAndTickets( show, to );
      }
      processOccupancy( root );

      WeekTO lastWeek = getLastWeek( income.getWeek() );
      if( lastWeek != null )
      {
        compareWeeklyMovieVariation( root, lastWeek );
      }

      // TODO conectarse a la configuración por cine
      double greenOccupancy = 0.0;
      double yellowOccupancy = 0.0;

      double greenVariation = 0.0;
      double yellowVariation = 0.0;

      List<IncomeSettingsTO> settings = this.incomeSettingsDAO
          .findIncomeSettingsByTheater( income.getTheater().getId() );

      for( IncomeSettingsTO to : settings )
      {
        if( to.getIncomeSettingsType().getId().equals( IncomeSettingsType.SCREEN_OCCUPANCY.getIdLong() ) )
        {
          greenOccupancy = safeAddIncome( to.getGreenSemaphore(), 0.0 ) / 100.0;
          yellowOccupancy = safeAddIncome( to.getYellowSemaphore(), 0.0 ) / 100.0;
        }
        else if( to.getIncomeSettingsType().getId().equals( IncomeSettingsType.CHANGE_PREVIOUS_WEEK.getIdLong() ) )
        {
          greenVariation = safeAddIncome( to.getGreenSemaphore(), 0.0 ) / 100.0;
          yellowVariation = safeAddIncome( to.getYellowSemaphore(), 0.0 ) / 100.0;
        }
      }

      processOccupancySemaphore( root, greenOccupancy, yellowOccupancy );
      processWeekleyVariationSemaphore( root, greenVariation, yellowVariation );
    }

    return root;
  }

  private void processOccupancySemaphore( IncomeTreeTO root, double greenOccupancy, double yellowOccupancy )
  {
    if( root != null && CollectionUtils.isNotEmpty( root.getChildren() ) )
      for( IncomeTreeTO leaf : root.getChildren() )
      {
        double weeklyWeekendOccupancy = safeAddIncome( leaf.getWeekendOccupancy(), 0.0 );
        if( weeklyWeekendOccupancy >= greenOccupancy )
        {
          leaf.setWeekendSemaphoreOccupancy( Semaphore.GREEN.getSemaphore() );
        }
        else if( weeklyWeekendOccupancy >= yellowOccupancy )
        {
          leaf.setWeekendSemaphoreOccupancy( Semaphore.YELLOW.getSemaphore() );
        }
        else
        {
          leaf.setWeekendSemaphoreOccupancy( Semaphore.RED.getSemaphore() );
        }

        double weeklyWeekOccupancy = safeAddIncome( leaf.getWeekOccupancy(), 0.0 );
        if( weeklyWeekOccupancy >= greenOccupancy )
        {
          leaf.setWeekSemaphoreOccupancy( Semaphore.GREEN.getSemaphore() );
        }
        else if( weeklyWeekOccupancy >= yellowOccupancy )
        {
          leaf.setWeekSemaphoreOccupancy( Semaphore.YELLOW.getSemaphore() );
        }
        else
        {
          leaf.setWeekSemaphoreOccupancy( Semaphore.RED.getSemaphore() );
        }

        processOccupancySemaphore( leaf, greenOccupancy, yellowOccupancy );
      }
  }

  private void processWeekleyVariationSemaphore( IncomeTreeTO root, double greenVariation, double yellowVariation )
  {
    if( root != null && CollectionUtils.isNotEmpty( root.getChildren() ) )
      for( IncomeTreeTO leaf : root.getChildren() )
      {
        double weeklyWeekendVariation = safeAddIncome( leaf.getWeekendVariation(), 0.0 );
        if( weeklyWeekendVariation <= greenVariation )
        {
          leaf.setWeekendSemaphoreVariation( Semaphore.GREEN.getSemaphore() );
        }
        else if( weeklyWeekendVariation <= yellowVariation )
        {
          leaf.setWeekendSemaphoreVariation( Semaphore.YELLOW.getSemaphore() );
        }
        else
        {
          leaf.setWeekendSemaphoreVariation( Semaphore.RED.getSemaphore() );
        }

        double weeklyWeekVariation = safeAddIncome( leaf.getWeekVariation(), 0.0 );
        if( weeklyWeekVariation <= greenVariation )
        {
          leaf.setWeekSemaphoreVariation( Semaphore.GREEN.getSemaphore() );
        }
        else if( weeklyWeekVariation <= yellowVariation )
        {
          leaf.setWeekSemaphoreVariation( Semaphore.YELLOW.getSemaphore() );
        }
        else
        {
          leaf.setWeekSemaphoreVariation( Semaphore.RED.getSemaphore() );
        }

        processWeekleyVariationSemaphore( leaf, greenVariation, yellowVariation );
      }
  }

  private void compareWeeklyMovieVariation( IncomeTreeTO root, WeekTO week )
  {
    for( IncomeTreeTO movie : root.getChildren() )
    {
      double weekendIncome = safeAddIncome( movie.getWeekendIncome(), 0.0 );
      double weekIncome = safeAddIncome( movie.getWeekIncome(), 0.0 );

      double lastWeekendIncome = this.bookingIncomeDAO.getWeekendIncomeByMovie( movie, week );
      double lastWeekIncome = this.bookingIncomeDAO.getWeekIncomeByMovie( movie, week );

      if( lastWeekendIncome > 0 )
      {
        double weekendVariation = (lastWeekendIncome - weekendIncome) / lastWeekendIncome;
        if( weekendVariation > 0.0 )
        {
          movie.setWeekendVariation( weekendVariation );
        }
      }

      if( lastWeekIncome > 0 )
      {
        double weekVariation = (lastWeekIncome - weekIncome) / lastWeekIncome;
        if( weekVariation > 0.0 )
        {
          movie.setWeekVariation( weekVariation );
        }
      }
    }
  }

  private void compareWeeklyScreenVariation( IncomeTreeTO root, WeekTO week )
  {
    for( IncomeTreeTO screen : root.getChildren() )
    {
      double weekendIncome = safeAddIncome( screen.getWeekendIncome(), 0.0 );
      double weekIncome = safeAddIncome( screen.getWeekIncome(), 0.0 );

      double lastWeekendIncome = this.bookingIncomeDAO.getWeekendIncomeByScreen( screen, week );
      double lastWeekIncome = this.bookingIncomeDAO.getWeekIncomeByScreen( screen, week );

      if( lastWeekendIncome > 0 )
      {
        double weekendVariation = (lastWeekendIncome - weekendIncome) / lastWeekendIncome;
        if( weekendVariation > 0.0 )
        {
          screen.setWeekendVariation( weekendVariation );
        }
      }

      if( lastWeekIncome > 0 )
      {
        double weekVariation = (lastWeekIncome - weekIncome) / lastWeekIncome;
        if( weekVariation > 0.0 )
        {
          screen.setWeekVariation( weekVariation );
        }
      }
    }
  }

  private void processOccupancy( IncomeTreeTO root )
  {
    // Se revisan los ingresos, tickets y % de ocupacion por los 3 nivel y se agrupan
    for( IncomeTreeTO firstLevel : root.getChildren() )
    {
      // El primer nivel puede ser movie o screen, depende del reporte
      double firstLevelWeekendOccupancy = 0.0;
      double firstLevelWeekOccupancy = 0.0;
      int firstLevelWeekendTickets = 0;
      int firstLevelWeekTickets = 0;
      double firstLevelWeekendIncome = 0.0;
      double firstLevelWeekIncome = 0.0;
      int firstLevelWeekendShows = 0;
      int firstLevelWeekShows = 0;
      double firstLevelWeekendAttendance = 0.0;
      double firstLevelWeekAttendance = 0.0;
      double sumCapacityXShowsWeek = 0.0;
      double sumCapacityXShowsWeekend = 0.0;
      List<Integer> screens = new ArrayList<Integer>();
      List<Integer> screens2 = new ArrayList<Integer>();

      for( IncomeTreeTO secondLevel : firstLevel.getChildren() )
      {
        // El segundo nivel puede ser movie o screen, depende del reporte
        double secondLevelWeekendOccupancy = 0.0;
        double secondLevelWeekOccupancy = 0.0;
        int secondLevelWeekendTickets = 0;
        int secondLevelWeekTickets = 0;
        double secondLevelWeekendIncome = 0.0;
        double secondLevelWeekIncome = 0.0;

        int secondLevelWeekendShows = 0;
        int secondLevelWeekShows = 0;
        double secondLevelWeekendAttendance = 0.0;
        double secondLevelWeekAttendance = 0.0;

        for( IncomeTreeTO show : secondLevel.getChildren() )
        {
          // El tercer nivel siempre es show
          int screenCapacity = show.getBooking().getScreen().getNuCapacity();
          int weekend = safeAddTickets( show.getWeekendTickets(), 0 );
          int week = show.getWeekTickets().intValue();
          int weekendShows = show.getWeekendQuantityShows();
          int weekShows = show.getWeekQuantityShows();
          int weekendTickets = safeAddTickets( show.getWeekendTickets(), 0 );
          int weekTickets = safeAddTickets( show.getWeekTickets(), 0 );

          double weekendOccupancy = 0.0;
          double weekendAttendace = 0.0;
          if( weekendShows != 0 )
          {
            weekendOccupancy = (double) weekend / (weekendShows * screenCapacity);
            weekendAttendace = (double) weekendTickets / weekendShows;
          }
          double weekAttendace = (double) weekTickets / weekShows;
          double weekOccupancy = (double) week / (weekShows * screenCapacity);

          show.setWeekendOccupancy( weekendOccupancy );
          show.setWeekOccupancy( weekOccupancy );

          secondLevelWeekendOccupancy += weekendOccupancy;
          secondLevelWeekOccupancy += weekOccupancy;

          show.setWeekendAttendace( weekendAttendace );
          show.setWeekAttendace( weekAttendace );

          // secondLevelWeekendTickets += safeAddTickets( show.getWeekendTickets(), 0 );
          secondLevelWeekendTickets = secondLevelWeekendTickets + weekend;
          // secondLevelWeekTickets += safeAddTickets( show.getWeekTickets(), 0 );
          secondLevelWeekTickets = secondLevelWeekTickets + week;
          secondLevelWeekendIncome += safeAddIncome( show.getWeekendIncome(), 0.0 );
          secondLevelWeekIncome += safeAddIncome( show.getWeekIncome(), 0.0 );

          secondLevelWeekendShows += weekendShows;
          secondLevelWeekShows += weekShows;

        }

        // processWeekendOccupancy( secondLevel, secondLevelWeekendOccupancy );
        processWeekendOccupancy( secondLevel, secondLevelWeekendOccupancy );
        double showXCapacityWeekend = (double) (secondLevelWeekendShows * secondLevel.getBooking().getScreen()
            .getNuCapacity().intValue());
        double showXCapacityWeek = (double) (secondLevelWeekShows * secondLevel.getBooking().getScreen()
            .getNuCapacity());
        secondLevelWeekendOccupancy = (double) secondLevelWeekendTickets / showXCapacityWeekend;
        secondLevel.setWeekendOccupancy( secondLevelWeekendOccupancy );
        secondLevelWeekOccupancy = (double) secondLevelWeekTickets / showXCapacityWeek;
        secondLevel.setWeekOccupancy( secondLevelWeekOccupancy );

        secondLevel.setWeekendTickets( secondLevelWeekendTickets );
        secondLevel.setWeekTickets( secondLevelWeekTickets );
        secondLevel.setWeekendIncome( secondLevelWeekendIncome );
        secondLevel.setWeekIncome( secondLevelWeekIncome );

        secondLevel.setWeekendQuantityShows( secondLevelWeekendShows );
        secondLevel.setWeekQuantityShows( secondLevelWeekShows );

        secondLevelWeekendAttendance += secondLevelWeekendTickets;
        secondLevelWeekAttendance += secondLevelWeekTickets;

        if( secondLevelWeekendShows != 0 )
        {
          secondLevelWeekendAttendance /= secondLevelWeekendShows;
        }
        secondLevelWeekAttendance /= secondLevelWeekShows;
        secondLevel.setWeekendAttendace( secondLevelWeekendAttendance );
        secondLevel.setWeekAttendace( secondLevelWeekAttendance );

        firstLevelWeekendTickets += secondLevelWeekendTickets;
        firstLevelWeekTickets += secondLevelWeekTickets;

        firstLevelWeekendIncome += secondLevelWeekendIncome;
        firstLevelWeekIncome += secondLevelWeekIncome;

        firstLevelWeekendOccupancy += secondLevelWeekendOccupancy;
        firstLevelWeekOccupancy += secondLevelWeekOccupancy;

        firstLevelWeekendShows += secondLevelWeekendShows;
        firstLevelWeekShows += secondLevelWeekShows;

        sumCapacityXShowsWeek += showXCapacityWeek;
        sumCapacityXShowsWeekend += showXCapacityWeekend;
        if( !screens.contains( secondLevel.getBooking().getScreen().getNuScreen() ) )
        {
          screens.add( secondLevel.getBooking().getScreen().getNuScreen() );
          screens2.add( secondLevel.getBooking().getScreen().getNuCapacity() );
        }
      }

      processWeekendOccupancy( firstLevel, firstLevelWeekendOccupancy );

      firstLevelWeekOccupancy /= firstLevel.getChildren().size();
      firstLevel.setWeekendOccupancy( (double) firstLevelWeekendTickets / sumCapacityXShowsWeekend );

      firstLevel.setWeekOccupancy( (double) firstLevelWeekTickets / sumCapacityXShowsWeek );

      firstLevel.setWeekendTickets( firstLevelWeekendTickets );
      firstLevel.setWeekTickets( firstLevelWeekTickets );
      firstLevel.setWeekendIncome( firstLevelWeekendIncome );
      firstLevel.setWeekIncome( firstLevelWeekIncome );

      firstLevel.setWeekendQuantityShows( firstLevelWeekendShows );
      firstLevel.setWeekQuantityShows( firstLevelWeekShows );

      firstLevelWeekendAttendance += firstLevelWeekendTickets;
      firstLevelWeekAttendance += firstLevelWeekTickets;

      if( firstLevelWeekendShows != 0 )
      {
        firstLevelWeekendAttendance /= firstLevelWeekendShows;
      }
      firstLevelWeekAttendance /= firstLevelWeekShows;
      firstLevel.setWeekendAttendace( firstLevelWeekendAttendance );
      firstLevel.setWeekAttendace( firstLevelWeekAttendance );
    }
  }

  private void processWeekendOccupancy( IncomeTreeTO parentLevel, double accumulatedWeekendOccupancy )
  {
    int hasWeekends = 0;
    for( IncomeTreeTO childLevel : parentLevel.getChildren() )
    {
      if( childLevel.isWeekend() )
      {
        hasWeekends++;
      }
    }
    if( hasWeekends != 0 )
    {
      accumulatedWeekendOccupancy /= hasWeekends;
      parentLevel.setWeekendOccupancy( accumulatedWeekendOccupancy );
      parentLevel.setWeekend( true );
    }

  }

  private void addIncomesAndTickets( IncomeTreeTO show, IncomeTO income )
  {
    // Se agregan los datos de fin de semana
    if( dayBelongs( IncomeType.WEEKEND, income.getDate() ) )
    {
      show.setWeekendOccupancy( 0.0 );
      show.setWeekendVariation( 0.0 );
      show.setWeekendQuantityShows( show.getWeekendQuantityShows() + 1 );
      show.setWeekendIncome( safeAddIncome( show.getWeekendIncome(), income.getIncome() ) );
      show.setWeekendTickets( safeAddTickets( show.getWeekendTickets(), income.getTickets() ) );
      show.setWeekend( true );
    }
    show.setWeekOccupancy( 0.0 );
    show.setWeekVariation( 0.0 );
    show.setWeekQuantityShows( show.getWeekQuantityShows() + 1 );
    show.setWeekIncome( safeAddIncome( show.getWeekIncome(), income.getIncome() ) );
    show.setWeekTickets( safeAddTickets( show.getWeekTickets(), income.getTickets() ) );

  }

  private double safeAddIncome( Double income, double value )
  {
    double sum = 0.0;

    if( income != null )
    {
      sum = income.doubleValue();
    }
    return sum += value;
  }

  private int safeAddTickets( Integer tickets, double value )
  {
    int sum = 0;

    if( tickets != null )
    {
      sum = tickets.intValue();
    }
    return sum += value;
  }

  private IncomeTreeTO getMovie( IncomeTreeTO parent, IncomeTO to )
  {
    IncomeTreeTO movie = null;
    if( parent.getType().equals( IncomeTreeTO.Type.ROOT ) || parent.getType().equals( IncomeTreeTO.Type.SCREEN ) )
    {
      for( IncomeTreeTO child : parent.getChildren() )
      {
        if( child.getBooking().getEvent().equals( to.getEvent() ) )
        {
          movie = child;
          break;
        }
      }
      if( movie == null )
      {
        movie = new IncomeTreeTO();
        movie.setType( IncomeTreeTO.Type.MOVIE );
        movie.setBooking( getSafeBooking( to ) );
        if( parent.getType().equals( IncomeTreeTO.Type.SCREEN ) )
        {
          movie.getBooking().setScreen( to.getScreen() );
        }
        movie.getBooking().setEvent( to.getEvent() );

        movie.getBooking().setExhibitionWeek( to.getExhibitionWeek() );

        parent.getChildren().add( movie );
      }
    }
    return movie;
  }

  private BookingTO getSafeBooking( IncomeTO to )
  {
    BookingTO booking = null;
    if( to.getBooking() != null )
    {
      booking = to.getBooking();
    }
    else
    {
      booking = new BookingTO();
      booking.setTheater( to.getTheater() );
      booking.setWeek( to.getWeek() );
    }
    return booking;
  }

  private IncomeTreeTO getScreen( IncomeTreeTO parent, IncomeTO income )
  {
    IncomeTreeTO screen = null;
    if( parent.getType().equals( IncomeTreeTO.Type.MOVIE ) || parent.getType().equals( IncomeTreeTO.Type.ROOT ) )
    {
      for( IncomeTreeTO child : parent.getChildren() )
      {
        if( child.getBooking().getScreen().getNuScreen().equals( income.getScreen().getNuScreen() ) )
        {
          screen = child;
          break;
        }
      }
      if( screen == null )
      {
        screen = new IncomeTreeTO();
        screen.setType( IncomeTreeTO.Type.SCREEN );
        screen.setBooking( getSafeBooking( income ) );
        screen.getBooking().setEvent( income.getEvent() );
        screen.getBooking().setScreen( income.getScreen() );

        screen.getBooking().setExhibitionWeek( income.getExhibitionWeek() );
        parent.getChildren().add( screen );
      }
    }
    return screen;
  }

  private IncomeTreeTO getShow( IncomeTreeTO parent, IncomeTO income )
  {
    Date date = income.getDate();

    IncomeTreeTO show = null;
    if( parent.getType().equals( IncomeTreeTO.Type.SCREEN ) || parent.getType().equals( IncomeTreeTO.Type.MOVIE ) )
    {
      for( IncomeTreeTO child : parent.getChildren() )
      {
        if( child.getShow().equals( income.getTimeStr() ) )
        {
          show = child;
          break;
        }
      }
      if( show == null )
      {
        show = new IncomeTreeTO();
        show.setType( IncomeTreeTO.Type.SHOW );
        show.setBooking( getSafeBooking( income ) );
        show.getBooking().setEvent( income.getEvent() );
        show.getBooking().setScreen( income.getScreen() );
        show.getBooking().setShow( date );
        show.setShow( income.getTimeStr() );
        show.getBooking().setExhibitionWeek( income.getExhibitionWeek() );
        parent.getChildren().add( show );
      }
    }
    return show;
  }

  @Override
  public IncomeTreeTO searchIncomesByScreen( IncomeTO income )
  {
    IncomeTreeTO root = new IncomeTreeTO();
    root.setBooking( new BookingTO() );
    root.getBooking().setTheater( income.getTheater() );
    List<IncomeTO> result = this.bookingIncomeDAO.findAllByIdTheaterIdWeek( income );

    if( CollectionUtils.isNotEmpty( result ) )
    {
      BookingTO booking = new BookingTO();
      IncomeTO first = (IncomeTO) CollectionUtils.find( result, PredicateUtils.notNullPredicate() );
      booking.setTheater( first.getTheater() );
      booking.setWeek( first.getWeek() );
      root.setBooking( booking );
      root.setType( IncomeTreeTO.Type.ROOT );

      for( IncomeTO to : result )
      {
        IncomeTreeTO screen = getScreen( root, to );
        IncomeTreeTO movie = getMovie( screen, to );
        IncomeTreeTO show = getShow( movie, to );
        addIncomesAndTickets( show, to );
      }
      processOccupancy( root );

      WeekTO week = getLastWeek( income.getWeek() );
      if( week != null )
      {
        compareWeeklyScreenVariation( root, week );
      }

      // TODO conectarse a la configuración por cine
      double greenOccupancy = 0.5;
      double yellowOccupancy = 0.2;

      double greenVariation = 0.1;
      double yellowVariation = 0.3;

      processOccupancySemaphore( root, greenOccupancy, yellowOccupancy );
      processWeekleyVariationSemaphore( root, greenVariation, yellowVariation );

    }
    getSummaryTotalIncomes( root, income );
    return root;
  }

  private boolean dayBelongs( IncomeType incomeType, Date date )
  {
    Calendar cal = DateUtils.toCalendar( date );
    return days.get( incomeType ).contains( cal.get( Calendar.DAY_OF_WEEK ) );
  }

  @Override
  public WeekTO getLastWeek( WeekTO weekTO )
  {
    WeekDO weekIncome = this.weekDAO.find( weekTO.getIdWeek() );

    Calendar cal = DateUtils.toCalendar( weekIncome.getDtStartingDayWeek() );
    cal.add( Calendar.DATE, -1 );

    WeekDO week = null;
    List<WeekDO> weeks = this.weekDAO.findWeeksByStartDayAndFinalDay( cal.getTime(), cal.getTime() );
    for( WeekDO w : weeks )
    {
      if( w.isFgActive() && !w.isFgSpecialWeek() )
      {
        week = w;
        break;
      }
    }

    return (WeekTO) new WeekDOToWeekTOTransformer().transform( week );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IncomeDetailTO getIncomeDetail( IncomeDetailTO request )
  {
    return this.bookingIncomeDAO.findIncomeDetail( request );
  }

}
