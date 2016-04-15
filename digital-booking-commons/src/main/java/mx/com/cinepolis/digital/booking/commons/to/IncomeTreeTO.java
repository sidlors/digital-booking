package mx.com.cinepolis.digital.booking.commons.to;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;

/**
 * Transfer objet for displaying the incomes of a given theater, using a composite view so it can be drill downed the
 * data
 * 
 * @author gsegura
 */
public class IncomeTreeTO extends CatalogTO implements Comparable<IncomeTreeTO>
{
  private static final long serialVersionUID = 3100098064892499944L;

  public enum Type
  {
    ROOT, MOVIE, SCREEN, SHOW
  };
  private SummaryTotalIncomesTO summaryTotals;

  private BookingTO booking;
  private Type type;

  private Double weekendIncome;
  private Double weekIncome;

  private Integer weekendTickets;
  private Integer weekTickets;

  private Double weekendAttendace;
  private Double weekAttendace;

  private Double weekendOccupancy;
  private Double weekOccupancy;
  private String weekendSemaphoreOccupancy;
  private String weekSemaphoreOccupancy;

  private Double weekendVariation;
  private Double weekVariation;
  private String weekendSemaphoreVariation;
  private String weekSemaphoreVariation;

  private int weekendQuantityShows;
  private int weekQuantityShows;

  private String show;

  private List<IncomeTreeTO> children;
  private boolean weekend;

  /**
   * Constructor default
   */
  public IncomeTreeTO()
  {
    children = new ArrayList<IncomeTreeTO>();
  }

  /**
   * @return the booking
   */
  public BookingTO getBooking()
  {
    return booking;
  }

  /**
   * @param booking the booking to set
   */
  public void setBooking( BookingTO booking )
  {
    this.booking = booking;
  }

  /**
   * @return the type
   */
  public Type getType()
  {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType( Type type )
  {
    this.type = type;
  }

  /**
   * @return the weekendIncome
   */
  public Double getWeekendIncome()
  {
    return weekendIncome;
  }

  /**
   * @param weekendIncome the weekendIncome to set
   */
  public void setWeekendIncome( Double weekendIncome )
  {
    this.weekendIncome = weekendIncome;
  }

  /**
   * @return the weekIncome
   */
  public Double getWeekIncome()
  {
    return weekIncome;
  }

  /**
   * @param weekIncome the weekIncome to set
   */
  public void setWeekIncome( Double weekIncome )
  {
    this.weekIncome = weekIncome;
  }

  /**
   * @return the weekendTickets
   */
  public Integer getWeekendTickets()
  {
    return weekendTickets;
  }

  /**
   * @param weekendTickets the weekendTickets to set
   */
  public void setWeekendTickets( Integer weekendTickets )
  {
    this.weekendTickets = weekendTickets;
  }

  /**
   * @return the weekTickets
   */
  public Integer getWeekTickets()
  {
    return weekTickets;
  }

  /**
   * @param weekTickets the weekTickets to set
   */
  public void setWeekTickets( Integer weekTickets )
  {
    this.weekTickets = weekTickets;
  }

  /**
   * @return the weekendAttendace
   */
  public Double getWeekendAttendace()
  {
    return weekendAttendace;
  }

  /**
   * @param weekendAttendace the weekendAttendace to set
   */
  public void setWeekendAttendace( Double weekendAttendace )
  {
    this.weekendAttendace = weekendAttendace;
  }

  /**
   * @return the weekAttendace
   */
  public Double getWeekAttendace()
  {
    return weekAttendace;
  }

  /**
   * @param weekAttendace the weekAttendace to set
   */
  public void setWeekAttendace( Double weekAttendace )
  {
    this.weekAttendace = weekAttendace;
  }

  /**
   * @return the weekendOccupancy
   */
  public Double getWeekendOccupancy()
  {
    return weekendOccupancy;
  }

  /**
   * @param weekendOccupancy the weekendOccupancy to set
   */
  public void setWeekendOccupancy( Double weekendOccupancy )
  {
    this.weekendOccupancy = weekendOccupancy;
  }

  /**
   * @return the weekOccupancy
   */
  public Double getWeekOccupancy()
  {
    return weekOccupancy;
  }

  /**
   * @param weekOccupancy the weekOccupancy to set
   */
  public void setWeekOccupancy( Double weekOccupancy )
  {
    this.weekOccupancy = weekOccupancy;
  }

  /**
   * @return the weekendVariation
   */
  public Double getWeekendVariation()
  {
    return weekendVariation;
  }

  /**
   * @param weekendVariation the weekendVariation to set
   */
  public void setWeekendVariation( Double weekendVariation )
  {
    this.weekendVariation = weekendVariation;
  }

  /**
   * @return the weekVariation
   */
  public Double getWeekVariation()
  {
    return weekVariation;
  }

  /**
   * @param weekVariation the weekVariation to set
   */
  public void setWeekVariation( Double weekVariation )
  {
    this.weekVariation = weekVariation;
  }

  /**
   * @return the weekendQuantityShows
   */
  public int getWeekendQuantityShows()
  {
    return weekendQuantityShows;
  }

  /**
   * @param weekendQuantityShows the weekendQuantityShows to set
   */
  public void setWeekendQuantityShows( int weekendQuantityShows )
  {
    this.weekendQuantityShows = weekendQuantityShows;
  }

  /**
   * @return the weekQuantityShows
   */
  public int getWeekQuantityShows()
  {
    return weekQuantityShows;
  }

  /**
   * @param weekQuantityShows the weekQuantityShows to set
   */
  public void setWeekQuantityShows( int weekQuantityShows )
  {
    this.weekQuantityShows = weekQuantityShows;
  }

  /**
   * @return the children
   */
  public List<IncomeTreeTO> getChildren()
  {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren( List<IncomeTreeTO> children )
  {
    this.children = children;
  }

  /**
   * @return the weekendSemaphoreOccupancy
   */
  public String getWeekendSemaphoreOccupancy()
  {
    return weekendSemaphoreOccupancy;
  }

  /**
   * @param weekendSemaphoreOccupancy the weekendSemaphoreOccupancy to set
   */
  public void setWeekendSemaphoreOccupancy( String weekendSemaphoreOccupancy )
  {
    this.weekendSemaphoreOccupancy = weekendSemaphoreOccupancy;
  }

  /**
   * @return the weekSemaphoreOccupancy
   */
  public String getWeekSemaphoreOccupancy()
  {
    return weekSemaphoreOccupancy;
  }

  /**
   * @param weekSemaphoreOccupancy the weekSemaphoreOccupancy to set
   */
  public void setWeekSemaphoreOccupancy( String weekSemaphoreOccupancy )
  {
    this.weekSemaphoreOccupancy = weekSemaphoreOccupancy;
  }

  /**
   * @return the weekendSemaphoreVariation
   */
  public String getWeekendSemaphoreVariation()
  {
    return weekendSemaphoreVariation;
  }

  /**
   * @param weekendSemaphoreVariation the weekendSemaphoreVariation to set
   */
  public void setWeekendSemaphoreVariation( String weekendSemaphoreVariation )
  {
    this.weekendSemaphoreVariation = weekendSemaphoreVariation;
  }

  /**
   * @return the weekSemaphoreVariation
   */
  public String getWeekSemaphoreVariation()
  {
    return weekSemaphoreVariation;
  }

  /**
   * @param weekSemaphoreVariation the weekSemaphoreVariation to set
   */
  public void setWeekSemaphoreVariation( String weekSemaphoreVariation )
  {
    this.weekSemaphoreVariation = weekSemaphoreVariation;
  }

  /**
   * Gets a clone of this object without its children
   * 
   * @return
   */
  public IncomeTreeTO getLeaf()
  {
    IncomeTreeTO leaf = new IncomeTreeTO();
    leaf.setBooking( this.booking );
    leaf.setType( this.type );
    leaf.setWeekAttendace( weekAttendace );
    leaf.setWeekendAttendace( weekendAttendace );
    leaf.setWeekendIncome( weekendIncome );
    leaf.setWeekendOccupancy( weekendOccupancy );
    leaf.setWeekendTickets( weekendTickets );
    leaf.setWeekendVariation( weekendVariation );
    leaf.setWeekIncome( weekIncome );
    leaf.setWeekOccupancy( weekOccupancy );
    leaf.setWeekTickets( weekTickets );
    leaf.setWeekVariation( weekVariation );
    leaf.setWeekendSemaphoreOccupancy( weekendSemaphoreOccupancy );
    leaf.setWeekSemaphoreOccupancy( weekSemaphoreOccupancy );
    leaf.setWeekendSemaphoreVariation( weekendSemaphoreVariation );
    leaf.setWeekSemaphoreVariation( weekSemaphoreVariation );

    leaf.setWeekendQuantityShows( weekendQuantityShows );
    leaf.setWeekQuantityShows( weekQuantityShows );
    leaf.setWeekend( this.weekend );
    return leaf;
  }

  @Override
  public int compareTo( IncomeTreeTO that )
  {
    int compare = 0;
    if( this.type.equals( Type.MOVIE ) )
    {
      compare = new CompareToBuilder().append( this.booking.getEvent().getDsEventName(),
        that.booking.getEvent().getDsEventName() ).toComparison();
    }
    else if( this.type.equals( Type.SCREEN ) )
    {
      compare = new CompareToBuilder().append( this.booking.getScreen().getNuScreen(),
        that.booking.getScreen().getNuScreen() ).toComparison();
    }
    else if( this.type.equals( Type.SHOW ) )
    {
      Calendar cal = Calendar.getInstance();
      Calendar show1 = DateUtils.toCalendar( this.getBooking().getShow() );
      Calendar show2 = DateUtils.toCalendar( that.getBooking().getShow() );
      show1.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), cal.get( Calendar.DATE ) );
      show2.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ), cal.get( Calendar.DATE ) );

      compare = new CompareToBuilder().append( show1, show2 ).toComparison();
    }
    return compare;
  }

  @Override
  public String getName()
  {
    String name = null;
    if( this.booking != null )
    {
      if( this.type.equals( Type.MOVIE ) && booking.getEvent() != null )
      {
        name = booking.getEvent().getDsEventName();
      }
      else if( this.type.equals( Type.SCREEN ) && booking.getScreen() != null )
      {
        name = new StringBuilder().append( "Screen " ).append( booking.getScreen().getNuScreen() ).append( " ( " )
            .append( booking.getScreen().getNuCapacity() ).append( " )" ).toString();
      }
      else if( this.type.equals( Type.SHOW ) )
      {
        name = this.booking.getShowTimeString();
      }
    }

    return name;
  }

  /**
   * Method that returns true is the type is {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO.Type.ROOT}
   * 
   * @return
   */
  public boolean isRoot()
  {
    return this.type == null ? false : this.type.equals( Type.ROOT );
  }

  /**
   * Method that returns true is the type is
   * {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO.Type.SCREEN}
   * 
   * @return
   */
  public boolean isScreen()
  {
    return this.type == null ? false : this.type.equals( Type.SCREEN );
  }

  /**
   * Method that returns true is the type is {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO.Type.SHOW}
   * 
   * @return
   */
  public boolean isShow()
  {
    return this.type == null ? false : this.type.equals( Type.SHOW );
  }

  /**
   * Method that returns true is the type is {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO.Type.MOVIE}
   * 
   * @return
   */
  public boolean isMovie()
  {
    return this.type == null ? false : this.type.equals( Type.MOVIE );
  }

  /**
   * @return the show
   */
  public String getShow()
  {
    return show;
  }

  /**
   * @param show the show to set
   */
  public void setShow( String show )
  {
    this.show = show;
  }

  /**
   * @return the weekend
   */
  public boolean isWeekend()
  {
    return weekend;
  }

  /**
   * @param weekend the weekend to set
   */
  public void setWeekend( boolean weekend )
  {
    this.weekend = weekend;
  }

  /**
   * @return the summaryTotals
   */
  public SummaryTotalIncomesTO getSummaryTotals()
  {
    return summaryTotals;
  }

  /**
   * @param summaryTotals the summaryTotals to set
   */
  public void setSummaryTotals( SummaryTotalIncomesTO summaryTotals )
  {
    this.summaryTotals = summaryTotals;
  }

  @Override
  public String toString()
  {

    String s = null;

    if( this.type == null )
    {
      s = super.toString();
    }
    else if( this.type.equals( IncomeTreeTO.Type.ROOT ) )
    {
      s = super.toString();
    }
    else if( this.type.equals( IncomeTreeTO.Type.MOVIE ) )
    {
      s = new ToStringBuilder( this ).append( "movie", this.getName() ).append( "Weekend income", this.weekendIncome )
          .append( "Weekend tickets", this.weekendTickets ).append( "Week income", this.weekIncome )
          .append( "Week tickets", this.weekTickets ).toString();
    }
    else if( this.type.equals( IncomeTreeTO.Type.SCREEN ) )
    {
      s = new ToStringBuilder( this ).append( "screen", this.getName() ).append( "Weekend income", this.weekendIncome )
          .append( "Weekend tickets", this.weekendTickets ).append( "Week income", this.weekIncome )
          .append( "Week tickets", this.weekTickets ).toString();
    }
    else
    {
      s = new ToStringBuilder( this ).append( "show", this.getName() ).append( "Weekend income", this.weekendIncome )
          .append( "Weekend tickets", this.weekendTickets ).append( "Weekend shows", this.weekendQuantityShows )
          .append( "Week income", this.weekIncome ).append( "Week tickets", this.weekTickets )
          .append( "Week shows", this.weekQuantityShows )

          .toString();
    }

    return s;

  }

}
