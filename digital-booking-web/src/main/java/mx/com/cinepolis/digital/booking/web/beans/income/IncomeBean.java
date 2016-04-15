package mx.com.cinepolis.digital.booking.web.beans.income;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.SummaryTotalIncomesTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.IncomeTreeTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.integration.income.IncomeIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.component.column.Column;
import org.primefaces.event.data.SortEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * ManageBean for the view for incomes
 * 
 * @author gsegura
 */
@ManagedBean(name = "incomeBean")
@ViewScoped
public class IncomeBean extends BaseManagedBean
{
  private static final String WITHOUT_ORDER = "0";

  private static final String DESCENDING_ORDER = "2";

  private static final String ASCENDING_ORDER = "1";

  private static final long serialVersionUID = -4434073080140693076L;

  @EJB
  private IncomeIntegratorEJB incomeIntegratorEJB;

  private TreeNode incomesByMovie;
  private TreeNode incomesByScreen;

  private IncomeTreeTO incomeByMovie;
  private IncomeTreeTO incomeByScreen;

  private boolean displayMovie;
  private boolean displayShow2;

  private boolean displayScreen;
  private boolean displayShow;
  private boolean displaySemaphores;
  private boolean displayIncome;
  private boolean displayWeekend;
  private boolean displayIncomeByMovie;

  private String sortByMovie;
  private String sortByWeek;
  private String sortByAttendants;
  private String sortByShows;
  private String sortByIncomes;
  private String sortByAttPerShow;
  private String sortByOccupancy;
  private String sortByVariation;

  private String sortByMovie2;
  private String sortByWeek2;
  private String sortByAttendants2;
  private String sortByShows2;
  private String sortByIncomes2;
  private String sortByAttPerShow2;
  private String sortByOccupancy2;
  private String sortByVariation2;

  private Long weekId;
  private Long theaterId;

  private List<IncomeTreeTO> incomeTableByMovie;
  private List<IncomeTreeTO> incomeTableByMovieWeekend;
  private List<IncomeTreeTO> incomeTableByScreen;
  private List<IncomeTreeTO> incomeTableByScreenWeekend;

  private SummaryTotalIncomesTO totals;

  private String strWeek;
  
  
  private String totWeekendAdmission;
  private String totWeekendShows;
  private String  totWeekendAdmissionPerShows;
  private String totWeekendScreenOccupancy;
  private String totWeekendChgPrewWeek;
  private String totWeekendGross;
  
  private String totWeekAdmission;
  private String totWeekShows;
  private String  totWeekAdmissionPerShows;
  private String totWeekScreenOccupancy;
  private String totWeekChgPrewWeek;
  private String totWeekGross;

  /**
   * Initializes the semaphores and consults the data
   */
  @PostConstruct
  public void init()
  {
    this.displayScreen = false;
    this.displayShow = false;
    this.displayMovie = false;
    this.displayShow2 = false;
    this.displaySemaphores = true;
    this.displayIncome = false;
    this.displayWeekend = true;
    this.displayIncomeByMovie = true;

    this.sortByMovie = WITHOUT_ORDER;
    this.sortByWeek = WITHOUT_ORDER;
    this.sortByAttendants = ASCENDING_ORDER;
    this.sortByShows = WITHOUT_ORDER;
    this.sortByIncomes = WITHOUT_ORDER;
    this.sortByAttPerShow = WITHOUT_ORDER;
    this.sortByOccupancy = WITHOUT_ORDER;
    this.sortByVariation = WITHOUT_ORDER;

    this.sortByMovie2 = WITHOUT_ORDER;
    this.sortByWeek2 = WITHOUT_ORDER;
    this.sortByAttendants2 = ASCENDING_ORDER;
    this.sortByShows2 = WITHOUT_ORDER;
    this.sortByIncomes2 = WITHOUT_ORDER;
    this.sortByAttPerShow2 = WITHOUT_ORDER;
    this.sortByOccupancy2 = WITHOUT_ORDER;
    this.sortByVariation2 = WITHOUT_ORDER;

    this.weekId = (Long) super.getAttribute( INCOME_WEEK_ID_ATTRIBUTE );
    this.theaterId = (Long) super.getAttribute( INCOME_THEATER_ID_ATTRIBUTE );

    WeekTO lastWeek = incomeIntegratorEJB.getLastWeek( new WeekTO( this.weekId.intValue() ) );

    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( this.theaterId ) );
    income.setWeek( lastWeek );

    this.totals = new SummaryTotalIncomesTO();

    this.incomeByMovie = incomeIntegratorEJB.searchIncomesByMovie( income );
    this.incomeByScreen = incomeIntegratorEJB.searchIncomesByScreen( income );

    if( incomeByScreen.getSummaryTotals() != null )
    {
      totals = incomeByScreen.getSummaryTotals();
    }
    else
    {
      totals.setTotWeekAdmission( 0 );
      totals.setTotWeekAdmissionPerShows( 0.0D );
      totals.setTotWeekChgPrewWeek( 0.0D );
      totals.setTotWeekendAdmission( 0 );
      totals.setTotWeekendAdmissionPerShows( 0.0D );
      totals.setTotWeekendChgPrewWeek( 0.0D );
      totals.setTotWeekendGross( 0.0D );
      totals.setTotWeekendScreenOccupancy( 0.0D );
      totals.setTotWeekendShows( 0 );
      totals.setTotWeekGross( 0.0D );
      totals.setTotWeekScreenOccupancy( 0.0D );
      totals.setTotWeekShows( 0 );

    }
    this.sortByCriteria( true );
    this.sortByCriteria( false );

    transformMovieTreeToList();
    transformScreenTreeToList();

    Calendar cal = Calendar.getInstance();
    cal.setTime( lastWeek.getStartingDayWeek() );
    StringBuilder sb = new StringBuilder();
    sb.append( cal.get( Calendar.DAY_OF_MONTH ) ).append( "-" );
    sb.append( cal.get( Calendar.MONTH ) ).append( "-" );
    sb.append( cal.get( Calendar.YEAR ) );
    this.strWeek = sb.toString();
    formatTotals();

  }

  double roundTwoDecimals( double d )
  {
    DecimalFormat twoDForm = new DecimalFormat( "##" );
    return Double.valueOf( twoDForm.format( d ) );
  }

  private void formatTotals()
  {
    if(this.totals!=null)
    {
      DecimalFormat numFormat;
      this.totWeekAdmission=totals.getTotWeekAdmission().toString();
      this.totWeekendAdmission=totals.getTotWeekendAdmission().toString();
      
      this.totWeekShows=totals.getTotWeekShows().toString();
      this.totWeekendShows=totals.getTotWeekendShows().toString();
      
      numFormat = new DecimalFormat("##0.00 %");
      //number = numFormat.format(0.15);
      
      this.totWeekAdmissionPerShows= String.valueOf( roundTwoDecimals(totals.getTotWeekAdmissionPerShows()));
      this.totWeekendAdmissionPerShows= String.valueOf(roundTwoDecimals(totals.getTotWeekendAdmissionPerShows()));
      
      this.totWeekScreenOccupancy= numFormat.format(totals.getTotWeekScreenOccupancy());
      this.totWeekendScreenOccupancy= numFormat.format(totals.getTotWeekendScreenOccupancy());
      
      this.totWeekChgPrewWeek= numFormat.format(totals.getTotWeekChgPrewWeek());
      this.totWeekendChgPrewWeek= numFormat.format(totals.getTotWeekendChgPrewWeek());
      
      
      numFormat = new DecimalFormat("'$'###,###,##0.00");
      //number = numFormat.format(15.567);
      this.totWeekGross=numFormat.format(totals.getTotWeekGross());
      this.totWeekendGross=numFormat.format(totals.getTotWeekendGross());
            
      
    }
  }

  /**
   * @return the incomeByMovie
   */
  public IncomeTreeTO getIncomeByMovie()
  {
    return incomeByMovie;
  }

  /**
   * @param incomeByMovie the incomeByMovie to set
   */
  public void setIncomeByMovie( IncomeTreeTO incomeByMovie )
  {
    this.incomeByMovie = incomeByMovie;
  }

  /**
   * @return the incomeByScreen
   */
  public IncomeTreeTO getIncomeByScreen()
  {
    return incomeByScreen;
  }

  /**
   * @param incomeByScreen the incomeByScreen to set
   */
  public void setIncomeByScreen( IncomeTreeTO incomeByScreen )
  {
    this.incomeByScreen = incomeByScreen;
  }

  /**
   * @return the displaySemaphores
   */
  public boolean isDisplaySemaphores()
  {
    return displaySemaphores;
  }

  /**
   * @param displaySemaphores the displaySemaphores to set
   */
  public void setDisplaySemaphores( boolean displaySemaphores )
  {
    this.displaySemaphores = displaySemaphores;
  }

  /**
   * @return the displayIncome
   */
  public boolean isDisplayIncome()
  {
    return displayIncome;
  }

  /**
   * @param displayIncome the displayIncome to set
   */
  public void setDisplayIncome( boolean displayIncome )
  {
    this.displayIncome = displayIncome;
  }

  /**
   * @return the displayWeekend
   */
  public boolean isDisplayWeekend()
  {
    return displayWeekend;
  }

  /**
   * @param displayWeekend the displayWeekend to set
   */
  public void setDisplayWeekend( boolean displayWeekend )
  {
    this.displayWeekend = displayWeekend;
  }

  /**
   * @return the displayIncomeByMovie
   */
  public boolean isDisplayIncomeByMovie()
  {
    return displayIncomeByMovie;
  }

  /**
   * @return the incomeIntegratorEJB
   */
  public IncomeIntegratorEJB getIncomeIntegratorEJB()
  {
    return incomeIntegratorEJB;
  }

  /**
   * @param incomeIntegratorEJB the incomeIntegratorEJB to set
   */
  public void setIncomeIntegratorEJB( IncomeIntegratorEJB incomeIntegratorEJB )
  {
    this.incomeIntegratorEJB = incomeIntegratorEJB;
  }

  /**
   * @return the sortByMovie
   */
  public String getSortByMovie()
  {
    return sortByMovie;
  }

  /**
   * @param sortByMovie the sortByMovie to set
   */
  public void setSortByMovie( String sortByMovie )
  {
    this.sortByMovie = sortByMovie;
  }

  /**
   * @return the sortByWeek
   */
  public String getSortByWeek()
  {
    return sortByWeek;
  }

  /**
   * @param sortByWeek the sortByWeek to set
   */
  public void setSortByWeek( String sortByWeek )
  {
    this.sortByWeek = sortByWeek;
  }

  /**
   * @return the sortByAttendants
   */
  public String getSortByAttendants()
  {
    return sortByAttendants;
  }

  /**
   * @param sortByAttendants the sortByAttendants to set
   */
  public void setSortByAttendants( String sortByAttendants )
  {
    this.sortByAttendants = sortByAttendants;
  }

  /**
   * @return the sortByShows
   */
  public String getSortByShows()
  {
    return sortByShows;
  }

  /**
   * @param sortByShows the sortByShows to set
   */
  public void setSortByShows( String sortByShows )
  {
    this.sortByShows = sortByShows;
  }

  /**
   * @return the sortByIncomes
   */
  public String getSortByIncomes()
  {
    return sortByIncomes;
  }

  /**
   * @param sortByIncomes the sortByIncomes to set
   */
  public void setSortByIncomes( String sortByIncomes )
  {
    this.sortByIncomes = sortByIncomes;
  }

  /**
   * @return the sortByAttPerShow
   */
  public String getSortByAttPerShow()
  {
    return sortByAttPerShow;
  }

  /**
   * @param sortByAttPerShow the sortByAttPerShow to set
   */
  public void setSortByAttPerShow( String sortByAttPerShow )
  {
    this.sortByAttPerShow = sortByAttPerShow;
  }

  /**
   * @return the sortByOccupancy
   */
  public String getSortByOccupancy()
  {
    return sortByOccupancy;
  }

  /**
   * @param sortByOccupancy the sortByOccupancy to set
   */
  public void setSortByOccupancy( String sortByOccupancy )
  {
    this.sortByOccupancy = sortByOccupancy;
  }

  /**
   * @return the sortByVariation
   */
  public String getSortByVariation()
  {
    return sortByVariation;
  }

  /**
   * @param sortByVariation the sortByVariation to set
   */
  public void setSortByVariation( String sortByVariation )
  {
    this.sortByVariation = sortByVariation;
  }

  /**
   * @param displayIncomeByMovie the displayIncomeByMovie to set
   */
  public void setDisplayIncomeByMovie( boolean displayIncomeByMovie )
  {
    this.displayIncomeByMovie = displayIncomeByMovie;
  }

  /**
   * @return the incomesByMovie
   */
  public TreeNode getIncomesByMovie()
  {
    return incomesByMovie;
  }

  /**
   * @param incomesByMovie the incomesByMovie to set
   */
  public void setIncomesByMovie( TreeNode incomesByMovie )
  {
    this.incomesByMovie = incomesByMovie;
  }

  /**
   * @return the incomesByScreen
   */
  public TreeNode getIncomesByScreen()
  {
    return incomesByScreen;
  }

  /**
   * @param incomesByScreen the incomesByScreen to set
   */
  public void setIncomesByScreen( TreeNode incomesByScreen )
  {
    this.incomesByScreen = incomesByScreen;
  }

  /**
   * @return the sortByMovie2
   */
  public String getSortByMovie2()
  {
    return sortByMovie2;
  }

  /**
   * @param sortByMovie2 the sortByMovie2 to set
   */
  public void setSortByMovie2( String sortByMovie2 )
  {
    this.sortByMovie2 = sortByMovie2;
  }

  /**
   * @return the sortByWeek2
   */
  public String getSortByWeek2()
  {
    return sortByWeek2;
  }

  /**
   * @param sortByWeek2 the sortByWeek2 to set
   */
  public void setSortByWeek2( String sortByWeek2 )
  {
    this.sortByWeek2 = sortByWeek2;
  }

  /**
   * @return the sortByAttendants2
   */
  public String getSortByAttendants2()
  {
    return sortByAttendants2;
  }

  /**
   * @param sortByAttendants2 the sortByAttendants2 to set
   */
  public void setSortByAttendants2( String sortByAttendants2 )
  {
    this.sortByAttendants2 = sortByAttendants2;
  }

  /**
   * @return the sortByShows2
   */
  public String getSortByShows2()
  {
    return sortByShows2;
  }

  /**
   * @param sortByShows2 the sortByShows2 to set
   */
  public void setSortByShows2( String sortByShows2 )
  {
    this.sortByShows2 = sortByShows2;
  }

  /**
   * @return the sortByIncomes2
   */
  public String getSortByIncomes2()
  {
    return sortByIncomes2;
  }

  /**
   * @param sortByIncomes2 the sortByIncomes2 to set
   */
  public void setSortByIncomes2( String sortByIncomes2 )
  {
    this.sortByIncomes2 = sortByIncomes2;
  }

  /**
   * @return the sortByAttPerShow2
   */
  public String getSortByAttPerShow2()
  {
    return sortByAttPerShow2;
  }

  /**
   * @param sortByAttPerShow2 the sortByAttPerShow2 to set
   */
  public void setSortByAttPerShow2( String sortByAttPerShow2 )
  {
    this.sortByAttPerShow2 = sortByAttPerShow2;
  }

  /**
   * @return the sortByOccupancy2
   */
  public String getSortByOccupancy2()
  {
    return sortByOccupancy2;
  }

  /**
   * @param sortByOccupancy2 the sortByOccupancy2 to set
   */
  public void setSortByOccupancy2( String sortByOccupancy2 )
  {
    this.sortByOccupancy2 = sortByOccupancy2;
  }

  /**
   * @return the sortByVariation2
   */
  public String getSortByVariation2()
  {
    return sortByVariation2;
  }

  /**
   * @param sortByVariation2 the sortByVariation2 to set
   */
  public void setSortByVariation2( String sortByVariation2 )
  {
    this.sortByVariation2 = sortByVariation2;
  }

  /**
   * @return the displayMovie
   */
  public boolean isDisplayMovie()
  {
    return displayMovie;
  }

  /**
   * @param displayMovie the displayMovie to set
   */
  public void setDisplayMovie( boolean displayMovie )
  {
    this.displayMovie = displayMovie;
  }

  /**
   * @return the displayScreen
   */
  public boolean isDisplayScreen()
  {
    return displayScreen;
  }

  /**
   * @param displayScreen the displayScreen to set
   */
  public void setDisplayScreen( boolean displayScreen )
  {
    this.displayScreen = displayScreen;
  }

  /**
   * @return the displayShow
   */
  public boolean isDisplayShow()
  {
    return displayShow;
  }

  /**
   * @param displayShow the displayShow to set
   */
  public void setDisplayShow( boolean displayShow )
  {
    this.displayShow = displayShow;
  }

  /**
   * @return the displayShow2
   */
  public boolean isDisplayShow2()
  {
    return displayShow2;
  }

  /**
   * @param displayShow2 the displayShow2 to set
   */
  public void setDisplayShow2( boolean displayShow2 )
  {
    this.displayShow2 = displayShow2;
  }

  /**
   * Integrates the current income into a TreeNode
   * 
   * @param income
   * @param root
   * @return
   */
  private TreeNode construct( IncomeTreeTO income, TreeNode root )
  {
    TreeNode branch = new DefaultTreeNode( income.getLeaf(), root );
    for( IncomeTreeTO child : income.getChildren() )
    {
      construct( child, branch );
    }
    return branch;
  }

  public void toogleDisplayScreen()
  {
    this.displayScreen = !this.displayScreen;
    this.sortByCriteria( true );
    this.transformMovieTreeToList();
  }

  public void toogleDisplayShow()
  {
    this.displayShow = !this.displayShow;
    this.sortByCriteria( true );
    this.transformMovieTreeToList();
  }

  public void toogleDisplayMovie()
  {
    this.displayMovie = !this.displayMovie;
    this.sortByCriteria( false );
    this.transformScreenTreeToList();
  }

  public void toogleDisplayShow2()
  {
    this.displayShow2 = !this.displayShow2;
    this.sortByCriteria( false );
    this.transformScreenTreeToList();
  }

  /**
   * Toogles the flag for display Weekend data
   */
  public void toogleDisplayWeekend()
  {
    this.sortByCriteria( true );
    this.sortByCriteria( false );

    transformMovieTreeToList();
    transformScreenTreeToList();

    this.displayWeekend = !this.displayWeekend;
  }

  /**
   * Toogles the flag for display the income
   */
  public void toogleDisplayIncome()
  {
    this.displayIncome = !this.displayIncome;
  }

  /**
   * Toogles the flag for display the semaphore
   */
  public void toogleDisplaySemaphores()
  {
    this.displaySemaphores = !this.displaySemaphores;
  }

  /**
   * @return the incomeTableByMovie
   */
  public List<IncomeTreeTO> getIncomeTableByMovie()
  {
    return this.displayWeekend ? this.incomeTableByMovieWeekend : this.incomeTableByMovie;
  }

  /**
   * @param incomeTableByMovie the incomeTableByMovie to set
   */
  public void setIncomeTableByMovie( List<IncomeTreeTO> incomeTableByMovie )
  {
    this.incomeTableByMovie = incomeTableByMovie;
  }

  /**
   * @return the incomeTableByScreen
   */
  public List<IncomeTreeTO> getIncomeTableByScreen()
  {
    return this.displayWeekend ? this.incomeTableByScreenWeekend : this.incomeTableByScreen;
  }

  /**
   * @param incomeTableByScreen the incomeTableByScreen to set
   */
  public void setIncomeTableByScreen( List<IncomeTreeTO> incomeTableByScreen )
  {
    this.incomeTableByScreen = incomeTableByScreen;
  }

  /**
   * @return the strWeek
   */
  public String getStrWeek()
  {
    return strWeek;
  }

  /**
   * @return the totals
   */
  public SummaryTotalIncomesTO getTotals()
  {
    return totals;
  }

  /**
   * @param totals the totals to set
   */
  public void setTotals( SummaryTotalIncomesTO totals )
  {
    this.totals = totals;
  }

  /**
   * Sorts the trees according the criteria selected
   */
  public void sortByCriteria( boolean movieTab )
  {
    IncomeTreeTOComparator comparator = new IncomeTreeTOComparator();
    comparator.setWeekend( this.displayWeekend );
    if( movieTab )
    {
      comparator.setSortByAttendantsAsc( this.sortByAttendants.equals( ASCENDING_ORDER ) );
      comparator.setSortByAttPerShowAsc( this.sortByAttPerShow.equals( ASCENDING_ORDER ) );
      comparator.setSortByIncomesAsc( this.sortByIncomes.equals( ASCENDING_ORDER ) );
      comparator.setSortByMovieAsc( this.sortByMovie.equals( ASCENDING_ORDER ) );
      comparator.setSortByOccupancyAsc( this.sortByOccupancy.equals( ASCENDING_ORDER ) );
      comparator.setSortByShowsAsc( this.sortByShows.equals( ASCENDING_ORDER ) );
      comparator.setSortByVariationAsc( this.sortByVariation.equals( ASCENDING_ORDER ) );
      comparator.setSortByWeekAsc( this.sortByWeek.equals( ASCENDING_ORDER ) );

      comparator.setSortByAttendantsDesc( this.sortByAttendants.equals( DESCENDING_ORDER ) );
      comparator.setSortByAttPerShowDesc( this.sortByAttPerShow.equals( DESCENDING_ORDER ) );
      comparator.setSortByIncomesDesc( this.sortByIncomes.equals( DESCENDING_ORDER ) );
      comparator.setSortByMovieDesc( this.sortByMovie.equals( DESCENDING_ORDER ) );
      comparator.setSortByOccupancyDesc( this.sortByOccupancy.equals( DESCENDING_ORDER ) );
      comparator.setSortByShowsDesc( this.sortByShows.equals( DESCENDING_ORDER ) );
      comparator.setSortByVariationDesc( this.sortByVariation.equals( DESCENDING_ORDER ) );
      comparator.setSortByWeekDesc( this.sortByWeek.equals( DESCENDING_ORDER ) );
    }
    else
    {
      comparator.setSortByAttendantsAsc( this.sortByAttendants2.equals( ASCENDING_ORDER ) );
      comparator.setSortByAttPerShowAsc( this.sortByAttPerShow2.equals( ASCENDING_ORDER ) );
      comparator.setSortByIncomesAsc( this.sortByIncomes2.equals( ASCENDING_ORDER ) );
      comparator.setSortByMovieAsc( this.sortByMovie2.equals( ASCENDING_ORDER ) );
      comparator.setSortByOccupancyAsc( this.sortByOccupancy2.equals( ASCENDING_ORDER ) );
      comparator.setSortByShowsAsc( this.sortByShows2.equals( ASCENDING_ORDER ) );
      comparator.setSortByVariationAsc( this.sortByVariation2.equals( ASCENDING_ORDER ) );
      comparator.setSortByWeekAsc( this.sortByWeek2.equals( ASCENDING_ORDER ) );

      comparator.setSortByAttendantsDesc( this.sortByAttendants2.equals( DESCENDING_ORDER ) );
      comparator.setSortByAttPerShowDesc( this.sortByAttPerShow2.equals( DESCENDING_ORDER ) );
      comparator.setSortByIncomesDesc( this.sortByIncomes2.equals( DESCENDING_ORDER ) );
      comparator.setSortByMovieDesc( this.sortByMovie2.equals( DESCENDING_ORDER ) );
      comparator.setSortByOccupancyDesc( this.sortByOccupancy2.equals( DESCENDING_ORDER ) );
      comparator.setSortByShowsDesc( this.sortByShows2.equals( DESCENDING_ORDER ) );
      comparator.setSortByVariationDesc( this.sortByVariation2.equals( DESCENDING_ORDER ) );
      comparator.setSortByWeekDesc( this.sortByWeek2.equals( DESCENDING_ORDER ) );
    }

    for( IncomeTreeTO movie : incomeByMovie.getChildren() )
    {
      for( IncomeTreeTO screen : movie.getChildren() )
      {
        Collections.sort( screen.getChildren(), comparator );
      }
      Collections.sort( movie.getChildren(), comparator );
    }
    Collections.sort( incomeByMovie.getChildren(), comparator );
    this.incomesByMovie = construct( incomeByMovie, null );

    for( IncomeTreeTO screen : incomeByScreen.getChildren() )
    {
      for( IncomeTreeTO movie : screen.getChildren() )
      {
        Collections.sort( movie.getChildren(), comparator );
      }
      Collections.sort( screen.getChildren(), comparator );
    }
    Collections.sort( incomeByScreen.getChildren(), comparator );
    this.incomesByScreen = construct( incomeByScreen, null );
  }

  /**
   * Sorter method for the collection incomeTableByMovie
   * 
   * @param event
   */
  public void sortByMovie( AjaxBehaviorEvent event )
  {
    clearSortingByMovie();
    if( event instanceof SortEvent )
    {
      SortEvent sortEvent = (SortEvent) event;
      Column column = (Column) sortEvent.getSortColumn();

      String id = column.getId().replaceAll( "1", "" ).replaceAll( "2", "" ).replaceAll( "3", "" );
      if( sortEvent.isAscending() )
      {
        ReflectionHelper.set( ASCENDING_ORDER, id, this );
      }
      else
      {
        ReflectionHelper.set( DESCENDING_ORDER, id, this );
      }
    }
    this.sortByCriteria( true );
    transformMovieTreeToList();
  }

  /**
   * Sorter method for the collection incomeTableByScreen
   * 
   * @param event
   */
  public void sortByScreen( AjaxBehaviorEvent event )
  {
    clearSortingByScreen();
    if( event instanceof SortEvent )
    {
      SortEvent sortEvent = (SortEvent) event;
      Column column = (Column) sortEvent.getSortColumn();

      String id = column.getId().replaceAll( "3", "2" );
      if( sortEvent.isAscending() )
      {
        ReflectionHelper.set( ASCENDING_ORDER, id, this );
      }
      else
      {
        ReflectionHelper.set( DESCENDING_ORDER, id, this );
      }
    }
    this.sortByCriteria( false );
    transformScreenTreeToList();
  }

  private void clearSortingByMovie()
  {
    this.sortByMovie = WITHOUT_ORDER;
    this.sortByWeek = WITHOUT_ORDER;
    this.sortByAttendants = WITHOUT_ORDER;
    this.sortByShows = WITHOUT_ORDER;
    this.sortByIncomes = WITHOUT_ORDER;
    this.sortByAttPerShow = WITHOUT_ORDER;
    this.sortByOccupancy = WITHOUT_ORDER;
    this.sortByVariation = WITHOUT_ORDER;
  }

  private void clearSortingByScreen()
  {
    this.sortByMovie2 = WITHOUT_ORDER;
    this.sortByWeek2 = WITHOUT_ORDER;
    this.sortByAttendants2 = WITHOUT_ORDER;
    this.sortByShows2 = WITHOUT_ORDER;
    this.sortByIncomes2 = WITHOUT_ORDER;
    this.sortByAttPerShow2 = WITHOUT_ORDER;
    this.sortByOccupancy2 = WITHOUT_ORDER;
    this.sortByVariation2 = WITHOUT_ORDER;
  }

  /**
   * Dummy sorter used to fake the sorting for the tables
   * 
   * @param object1
   * @param object2
   * @return
   */
  public int dummySort( Object object1, Object object2 )
  {
    return 0;
  }

  private void transformMovieTreeToList()
  {
    this.incomeTableByMovie = new ArrayList<IncomeTreeTO>();
    for( TreeNode movieNode : this.incomesByMovie.getChildren() )
    {
      IncomeTreeTO movie = (IncomeTreeTO) movieNode.getData();
      this.incomeTableByMovie.add( movie.getLeaf() );

      if( this.displayScreen )
      {
        for( TreeNode screenNode : movieNode.getChildren() )
        {
          IncomeTreeTO screen = (IncomeTreeTO) screenNode.getData();
          this.incomeTableByMovie.add( screen.getLeaf() );

          if( this.displayShow )
          {
            for( TreeNode showNode : screenNode.getChildren() )
            {
              IncomeTreeTO show = (IncomeTreeTO) showNode.getData();
              this.incomeTableByMovie.add( show.getLeaf() );
            }
          }
        }
      }

    }

    this.incomeTableByMovieWeekend = new ArrayList<IncomeTreeTO>();
    for( IncomeTreeTO income : this.incomeTableByMovie )
    {
      if( income.isWeekend() )
      {
        this.incomeTableByMovieWeekend.add( income );
      }
    }

  }

  private void transformScreenTreeToList()
  {
    this.incomeTableByScreen = new ArrayList<IncomeTreeTO>();
    for( TreeNode screenNode : this.incomesByScreen.getChildren() )
    {
      IncomeTreeTO screen = (IncomeTreeTO) screenNode.getData();
      this.incomeTableByScreen.add( screen.getLeaf() );

      if( this.displayMovie )
      {
        for( TreeNode movieNode : screenNode.getChildren() )
        {
          IncomeTreeTO movie = (IncomeTreeTO) movieNode.getData();
          this.incomeTableByScreen.add( movie.getLeaf() );

          if( this.displayShow2 )
          {
            for( TreeNode showNode : movieNode.getChildren() )
            {
              IncomeTreeTO show = (IncomeTreeTO) showNode.getData();
              this.incomeTableByScreen.add( show.getLeaf() );
            }
          }
        }
      }

    }

    this.incomeTableByScreenWeekend = new ArrayList<IncomeTreeTO>();
    for( IncomeTreeTO income : this.incomeTableByScreen )
    {
      if( income.isWeekend() )
      {
        this.incomeTableByScreenWeekend.add( income );
      }
    }

  }

  /**
   * Synchronizes the incomes for the current theater
   */
  public void synchronizeIncomes()
  {
    TheaterTO theater = new TheaterTO( this.theaterId );
    super.fillSessionData( theater );
    this.incomeIntegratorEJB.synchronizeIncomes( theater );
    init();
  }

  /**
   * @return the totWeekendAdmission
   */
  public String getTotWeekendAdmission()
  {
    return totWeekendAdmission;
  }

  /**
   * @param totWeekendAdmission the totWeekendAdmission to set
   */
  public void setTotWeekendAdmission( String totWeekendAdmission )
  {
    this.totWeekendAdmission = totWeekendAdmission;
  }

  /**
   * @return the totWeekendShows
   */
  public String getTotWeekendShows()
  {
    return totWeekendShows;
  }

  /**
   * @param totWeekendShows the totWeekendShows to set
   */
  public void setTotWeekendShows( String totWeekendShows )
  {
    this.totWeekendShows = totWeekendShows;
  }

  /**
   * @return the totWeekendAdmissionPerShows
   */
  public String getTotWeekendAdmissionPerShows()
  {
    return totWeekendAdmissionPerShows;
  }

  /**
   * @param totWeekendAdmissionPerShows the totWeekendAdmissionPerShows to set
   */
  public void setTotWeekendAdmissionPerShows( String totWeekendAdmissionPerShows )
  {
    this.totWeekendAdmissionPerShows = totWeekendAdmissionPerShows;
  }

  /**
   * @return the totWeekendScreenOccupancy
   */
  public String getTotWeekendScreenOccupancy()
  {
    return totWeekendScreenOccupancy;
  }

  /**
   * @param totWeekendScreenOccupancy the totWeekendScreenOccupancy to set
   */
  public void setTotWeekendScreenOccupancy( String totWeekendScreenOccupancy )
  {
    this.totWeekendScreenOccupancy = totWeekendScreenOccupancy;
  }

  /**
   * @return the totWeekendChgPrewWeek
   */
  public String getTotWeekendChgPrewWeek()
  {
    return totWeekendChgPrewWeek;
  }

  /**
   * @param totWeekendChgPrewWeek the totWeekendChgPrewWeek to set
   */
  public void setTotWeekendChgPrewWeek( String totWeekendChgPrewWeek )
  {
    this.totWeekendChgPrewWeek = totWeekendChgPrewWeek;
  }

  /**
   * @return the totWeekendGross
   */
  public String getTotWeekendGross()
  {
    return totWeekendGross;
  }

  /**
   * @param totWeekendGross the totWeekendGross to set
   */
  public void setTotWeekendGross( String totWeekendGross )
  {
    this.totWeekendGross = totWeekendGross;
  }

  /**
   * @return the totWeekAdmission
   */
  public String getTotWeekAdmission()
  {
    return totWeekAdmission;
  }

  /**
   * @param totWeekAdmission the totWeekAdmission to set
   */
  public void setTotWeekAdmission( String totWeekAdmission )
  {
    this.totWeekAdmission = totWeekAdmission;
  }

  /**
   * @return the totWeekShows
   */
  public String getTotWeekShows()
  {
    return totWeekShows;
  }

  /**
   * @param totWeekShows the totWeekShows to set
   */
  public void setTotWeekShows( String totWeekShows )
  {
    this.totWeekShows = totWeekShows;
  }

  /**
   * @return the totWeekAdmissionPerShows
   */
  public String getTotWeekAdmissionPerShows()
  {
    return totWeekAdmissionPerShows;
  }

  /**
   * @param totWeekAdmissionPerShows the totWeekAdmissionPerShows to set
   */
  public void setTotWeekAdmissionPerShows( String totWeekAdmissionPerShows )
  {
    this.totWeekAdmissionPerShows = totWeekAdmissionPerShows;
  }

  /**
   * @return the totWeekScreenOccupancy
   */
  public String getTotWeekScreenOccupancy()
  {
    return totWeekScreenOccupancy;
  }

  /**
   * @param totWeekScreenOccupancy the totWeekScreenOccupancy to set
   */
  public void setTotWeekScreenOccupancy( String totWeekScreenOccupancy )
  {
    this.totWeekScreenOccupancy = totWeekScreenOccupancy;
  }

  /**
   * @return the totWeekChgPrewWeek
   */
  public String getTotWeekChgPrewWeek()
  {
    return totWeekChgPrewWeek;
  }

  /**
   * @param totWeekChgPrewWeek the totWeekChgPrewWeek to set
   */
  public void setTotWeekChgPrewWeek( String totWeekChgPrewWeek )
  {
    this.totWeekChgPrewWeek = totWeekChgPrewWeek;
  }

  /**
   * @return the totWeekGross
   */
  public String getTotWeekGross()
  {
    return totWeekGross;
  }

  /**
   * @param totWeekGross the totWeekGross to set
   */
  public void setTotWeekGross( String totWeekGross )
  {
    this.totWeekGross = totWeekGross;
  }
}