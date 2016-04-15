package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.time.DateUtils;

import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;

/**
 * Implementation of the {@link java.util.Comparator<IncomeTreeTO>} interface that compares two instances of
 * {@link mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO}, does not validate if the attributes are null
 * 
 * @author gsegura
 */
public class IncomeTreeTOComparator implements Comparator<IncomeTreeTO>, Serializable
{
  private static final long serialVersionUID = 785533686326792300L;
  private boolean sortByMovieAsc;
  private boolean sortByWeekAsc;
  private boolean sortByAttendantsAsc;
  private boolean sortByShowsAsc;
  private boolean sortByIncomesAsc;
  private boolean sortByAttPerShowAsc;
  private boolean sortByOccupancyAsc;
  private boolean sortByVariationAsc;
  private boolean sortByMovieDesc;
  private boolean sortByWeekDesc;
  private boolean sortByAttendantsDesc;
  private boolean sortByShowsDesc;
  private boolean sortByIncomesDesc;
  private boolean sortByAttPerShowDesc;
  private boolean sortByOccupancyDesc;
  private boolean sortByVariationDesc;
  private boolean weekend;

  /**
   * @return the sortByMovieAsc
   */
  public boolean isSortByMovieAsc()
  {
    return sortByMovieAsc;
  }

  /**
   * @param sortByMovieAsc the sortByMovieAsc to set
   */
  public void setSortByMovieAsc( boolean sortByMovieAsc )
  {
    this.sortByMovieAsc = sortByMovieAsc;
  }

  /**
   * @return the sortByWeekAsc
   */
  public boolean isSortByWeekAsc()
  {
    return sortByWeekAsc;
  }

  /**
   * @param sortByWeekAsc the sortByWeekAsc to set
   */
  public void setSortByWeekAsc( boolean sortByWeekAsc )
  {
    this.sortByWeekAsc = sortByWeekAsc;
  }

  /**
   * @return the sortByAttendantsAsc
   */
  public boolean isSortByAttendantsAsc()
  {
    return sortByAttendantsAsc;
  }

  /**
   * @param sortByAttendantsAsc the sortByAttendantsAsc to set
   */
  public void setSortByAttendantsAsc( boolean sortByAttendantsAsc )
  {
    this.sortByAttendantsAsc = sortByAttendantsAsc;
  }

  /**
   * @return the sortByShowsAsc
   */
  public boolean isSortByShowsAsc()
  {
    return sortByShowsAsc;
  }

  /**
   * @param sortByShowsAsc the sortByShowsAsc to set
   */
  public void setSortByShowsAsc( boolean sortByShowsAsc )
  {
    this.sortByShowsAsc = sortByShowsAsc;
  }

  /**
   * @return the sortByIncomesAsc
   */
  public boolean isSortByIncomesAsc()
  {
    return sortByIncomesAsc;
  }

  /**
   * @param sortByIncomesAsc the sortByIncomesAsc to set
   */
  public void setSortByIncomesAsc( boolean sortByIncomesAsc )
  {
    this.sortByIncomesAsc = sortByIncomesAsc;
  }

  /**
   * @return the sortByAttPerShowAsc
   */
  public boolean isSortByAttPerShowAsc()
  {
    return sortByAttPerShowAsc;
  }

  /**
   * @param sortByAttPerShowAsc the sortByAttPerShowAsc to set
   */
  public void setSortByAttPerShowAsc( boolean sortByAttPerShowAsc )
  {
    this.sortByAttPerShowAsc = sortByAttPerShowAsc;
  }

  /**
   * @return the sortByOccupancyAsc
   */
  public boolean isSortByOccupancyAsc()
  {
    return sortByOccupancyAsc;
  }

  /**
   * @param sortByOccupancyAsc the sortByOccupancyAsc to set
   */
  public void setSortByOccupancyAsc( boolean sortByOccupancyAsc )
  {
    this.sortByOccupancyAsc = sortByOccupancyAsc;
  }

  /**
   * @return the sortByVariationAsc
   */
  public boolean isSortByVariationAsc()
  {
    return sortByVariationAsc;
  }

  /**
   * @param sortByVariationAsc the sortByVariationAsc to set
   */
  public void setSortByVariationAsc( boolean sortByVariationAsc )
  {
    this.sortByVariationAsc = sortByVariationAsc;
  }

  /**
   * @return the sortByMovieDesc
   */
  public boolean isSortByMovieDesc()
  {
    return sortByMovieDesc;
  }

  /**
   * @param sortByMovieDesc the sortByMovieDesc to set
   */
  public void setSortByMovieDesc( boolean sortByMovieDesc )
  {
    this.sortByMovieDesc = sortByMovieDesc;
  }

  /**
   * @return the sortByWeekDesc
   */
  public boolean isSortByWeekDesc()
  {
    return sortByWeekDesc;
  }

  /**
   * @param sortByWeekDesc the sortByWeekDesc to set
   */
  public void setSortByWeekDesc( boolean sortByWeekDesc )
  {
    this.sortByWeekDesc = sortByWeekDesc;
  }

  /**
   * @return the sortByAttendantsDesc
   */
  public boolean isSortByAttendantsDesc()
  {
    return sortByAttendantsDesc;
  }

  /**
   * @param sortByAttendantsDesc the sortByAttendantsDesc to set
   */
  public void setSortByAttendantsDesc( boolean sortByAttendantsDesc )
  {
    this.sortByAttendantsDesc = sortByAttendantsDesc;
  }

  /**
   * @return the sortByShowsDesc
   */
  public boolean isSortByShowsDesc()
  {
    return sortByShowsDesc;
  }

  /**
   * @param sortByShowsDesc the sortByShowsDesc to set
   */
  public void setSortByShowsDesc( boolean sortByShowsDesc )
  {
    this.sortByShowsDesc = sortByShowsDesc;
  }

  /**
   * @return the sortByIncomesDesc
   */
  public boolean isSortByIncomesDesc()
  {
    return sortByIncomesDesc;
  }

  /**
   * @param sortByIncomesDesc the sortByIncomesDesc to set
   */
  public void setSortByIncomesDesc( boolean sortByIncomesDesc )
  {
    this.sortByIncomesDesc = sortByIncomesDesc;
  }

  /**
   * @return the sortByAttPerShowDesc
   */
  public boolean isSortByAttPerShowDesc()
  {
    return sortByAttPerShowDesc;
  }

  /**
   * @param sortByAttPerShowDesc the sortByAttPerShowDesc to set
   */
  public void setSortByAttPerShowDesc( boolean sortByAttPerShowDesc )
  {
    this.sortByAttPerShowDesc = sortByAttPerShowDesc;
  }

  /**
   * @return the sortByOccupancyDesc
   */
  public boolean isSortByOccupancyDesc()
  {
    return sortByOccupancyDesc;
  }

  /**
   * @param sortByOccupancyDesc the sortByOccupancyDesc to set
   */
  public void setSortByOccupancyDesc( boolean sortByOccupancyDesc )
  {
    this.sortByOccupancyDesc = sortByOccupancyDesc;
  }

  /**
   * @return the sortByVariationDesc
   */
  public boolean isSortByVariationDesc()
  {
    return sortByVariationDesc;
  }

  /**
   * @param sortByVariationDesc the sortByVariationDesc to set
   */
  public void setSortByVariationDesc( boolean sortByVariationDesc )
  {
    this.sortByVariationDesc = sortByVariationDesc;
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

  @Override
  public int compare( IncomeTreeTO income1, IncomeTreeTO income2 )
  {
    CompareToBuilder c = new CompareToBuilder();
    compareByMovie( income1, income2, c );
    compareByExhibitionWeek( income1, income2, c );
    compareByAttendants( income1, income2, c );
    compareByShows( income1, income2, c );
    compareByIncomes( income1, income2, c );
    compareByAttendantsPerShow( income1, income2, c );
    compareByOccupancy( income1, income2, c );
    compareByWeekleyVariation( income1, income2, c );
    return c.toComparison();
  }

  private void compareByExhibitionWeek( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByWeekDesc )
    {
      c.append( income2.getBooking().getExhibitionWeek(), income1.getBooking().getExhibitionWeek() );
    }
    else if( this.sortByWeekAsc )
    {
      c.append( income1.getBooking().getExhibitionWeek(), income2.getBooking().getExhibitionWeek() );
    }
  }

  private void compareByWeekleyVariation( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByVariationDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendVariation(), income1.getWeekendVariation() );
      }
      else
      {
        c.append( income2.getWeekVariation(), income1.getWeekVariation() );
      }
    }
    else if( this.sortByVariationAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendVariation(), income2.getWeekendVariation() );
      }
      else
      {
        c.append( income1.getWeekVariation(), income2.getWeekVariation() );
      }
    }
  }

  private void compareByOccupancy( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByOccupancyDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendOccupancy(), income1.getWeekendOccupancy() );
      }
      else
      {
        c.append( income2.getWeekOccupancy(), income1.getWeekOccupancy() );
      }
    }
    else if( this.sortByOccupancyAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendOccupancy(), income2.getWeekendOccupancy() );
      }
      else
      {
        c.append( income1.getWeekOccupancy(), income2.getWeekOccupancy() );
      }
    }
  }

  private void compareByAttendantsPerShow( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByAttPerShowDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendAttendace(), income1.getWeekendAttendace() );
      }
      else
      {
        c.append( income2.getWeekAttendace(), income1.getWeekAttendace() );
      }
    }
    else if( this.sortByAttPerShowAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendAttendace(), income2.getWeekendAttendace() );
      }
      else
      {
        c.append( income1.getWeekAttendace(), income2.getWeekAttendace() );
      }
    }
  }

  private void compareByIncomes( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByIncomesDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendIncome(), income1.getWeekendIncome() );
      }
      else
      {
        c.append( income2.getWeekIncome(), income1.getWeekIncome() );
      }
    }
    else if( this.sortByIncomesAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendIncome(), income2.getWeekendIncome() );
      }
      else
      {
        c.append( income1.getWeekIncome(), income2.getWeekIncome() );
      }
    }
  }

  private void compareByShows( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByShowsDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendQuantityShows(), income1.getWeekendQuantityShows() );
      }
      else
      {
        c.append( income2.getWeekQuantityShows(), income1.getWeekQuantityShows() );
      }
    }
    else if( this.sortByShowsAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendQuantityShows(), income2.getWeekendQuantityShows() );
      }
      else
      {
        c.append( income1.getWeekQuantityShows(), income2.getWeekQuantityShows() );
      }
    }
  }

  private void compareByAttendants( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByAttendantsDesc )
    {
      if( this.weekend )
      {
        c.append( income2.getWeekendTickets(), income1.getWeekendTickets() );
      }
      else
      {
        c.append( income2.getWeekTickets(), income1.getWeekTickets() );
      }
    }
    else if( this.sortByAttendantsAsc )
    {
      if( this.weekend )
      {
        c.append( income1.getWeekendTickets(), income2.getWeekendTickets() );
      }
      else
      {
        c.append( income1.getWeekTickets(), income2.getWeekTickets() );
      }
    }
  }

  private void compareByMovie( IncomeTreeTO income1, IncomeTreeTO income2, CompareToBuilder c )
  {
    if( this.sortByMovieDesc )
    {
      if( income1.getType().equals( IncomeTreeTO.Type.MOVIE ) )
      {
        c.append( income2.getBooking().getEvent().getDsEventName(), income1.getBooking().getEvent().getDsEventName() );
      }
      else if( income1.getType().equals( IncomeTreeTO.Type.SCREEN ) )
      {
        c.append( income2.getBooking().getScreen().getNuScreen(), income1.getBooking().getScreen().getNuScreen() );
      }
      else if( income1.getType().equals( IncomeTreeTO.Type.SHOW ) )
      {
        Calendar c1 = DateUtils.toCalendar( income1.getBooking().getShow() );
        Calendar c2 = DateUtils.toCalendar( income2.getBooking().getShow() );
        c.append( c2.get( Calendar.HOUR_OF_DAY ), c1.get( Calendar.HOUR_OF_DAY ) );
        c.append( c2.get( Calendar.MINUTE ), c1.get( Calendar.MINUTE ) );
      }
    }
    else if( this.sortByMovieAsc )
    {
      if( income1.getType().equals( IncomeTreeTO.Type.MOVIE ) )
      {
        c.append( income1.getBooking().getEvent().getDsEventName(), income2.getBooking().getEvent().getDsEventName() );
      }
      else if( income1.getType().equals( IncomeTreeTO.Type.SCREEN ) )
      {
        c.append( income1.getBooking().getScreen().getNuScreen(), income2.getBooking().getScreen().getNuScreen() );
      }
      else if( income1.getType().equals( IncomeTreeTO.Type.SHOW ) )
      {
        Calendar c1 = DateUtils.toCalendar( income1.getBooking().getShow() );
        Calendar c2 = DateUtils.toCalendar( income2.getBooking().getShow() );
        c.append( c1.get( Calendar.HOUR_OF_DAY ), c2.get( Calendar.HOUR_OF_DAY ) );
        c.append( c1.get( Calendar.MINUTE ), c2.get( Calendar.MINUTE ) );
      }
    }
  }

}
