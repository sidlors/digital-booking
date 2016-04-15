package mx.com.cinepolis.digital.booking.commons.to;

/**
 * Transfer Object class for income detail
 * 
 * @author gsegura
 */
public class IncomeDetailTO extends AbstractTO
{
  private static final long serialVersionUID = 8648043331791785075L;

  private Long theaterId;
  private Long eventId;
  private Integer weekId;
  private Long bookingId;
  private Integer screenId;

  private double fridayIncome;
  private double saturdayIncome;
  private double sundayIncome;
  private double totalIncome;
  private long fridayTickets;
  private long saturdayTickets;
  private long sundayTickets;
  private long totalTickets;

  /**
   * @return the theaterId
   */
  public Long getTheaterId()
  {
    return theaterId;
  }

  /**
   * @param theaterId the theaterId to set
   */
  public void setTheaterId( Long theaterId )
  {
    this.theaterId = theaterId;
  }

  /**
   * @return the eventId
   */
  public Long getEventId()
  {
    return eventId;
  }

  /**
   * @param eventId the eventId to set
   */
  public void setEventId( Long eventId )
  {
    this.eventId = eventId;
  }

  /**
   * @return the weekId
   */
  public Integer getWeekId()
  {
    return weekId;
  }

  /**
   * @param weekId the weekId to set
   */
  public void setWeekId( Integer weekId )
  {
    this.weekId = weekId;
  }

  /**
   * @return the bookingId
   */
  public Long getBookingId()
  {
    return bookingId;
  }

  /**
   * @param bookingId the bookingId to set
   */
  public void setBookingId( Long bookingId )
  {
    this.bookingId = bookingId;
  }

  /**
   * @return the screenId
   */
  public Integer getScreenId()
  {
    return screenId;
  }

  /**
   * @param screenId the screenId to set
   */
  public void setScreenId( Integer screenId )
  {
    this.screenId = screenId;
  }

  /**
   * @return the fridayIncome
   */
  public double getFridayIncome()
  {
    return fridayIncome;
  }

  /**
   * @param fridayIncome the fridayIncome to set
   */
  public void setFridayIncome( double fridayIncome )
  {
    this.fridayIncome = fridayIncome;
  }

  /**
   * @return the saturdayIncome
   */
  public double getSaturdayIncome()
  {
    return saturdayIncome;
  }

  /**
   * @param saturdayIncome the saturdayIncome to set
   */
  public void setSaturdayIncome( double saturdayIncome )
  {
    this.saturdayIncome = saturdayIncome;
  }

  /**
   * @return the sundayIncome
   */
  public double getSundayIncome()
  {
    return sundayIncome;
  }

  /**
   * @param sundayIncome the sundayIncome to set
   */
  public void setSundayIncome( double sundayIncome )
  {
    this.sundayIncome = sundayIncome;
  }

  /**
   * @return the totalIncome
   */
  public double getTotalIncome()
  {
    return totalIncome;
  }

  /**
   * @param totalIncome the totalIncome to set
   */
  public void setTotalIncome( double totalIncome )
  {
    this.totalIncome = totalIncome;
  }

  /**
   * @return the fridayTickets
   */
  public long getFridayTickets()
  {
    return fridayTickets;
  }

  /**
   * @param fridayTickets the fridayTickets to set
   */
  public void setFridayTickets( long fridayTickets )
  {
    this.fridayTickets = fridayTickets;
  }

  /**
   * @return the saturdayTickets
   */
  public long getSaturdayTickets()
  {
    return saturdayTickets;
  }

  /**
   * @param saturdayTickets the saturdayTickets to set
   */
  public void setSaturdayTickets( long saturdayTickets )
  {
    this.saturdayTickets = saturdayTickets;
  }

  /**
   * @return the sundayTickets
   */
  public long getSundayTickets()
  {
    return sundayTickets;
  }

  /**
   * @param sundayTickets the sundayTickets to set
   */
  public void setSundayTickets( long sundayTickets )
  {
    this.sundayTickets = sundayTickets;
  }

  /**
   * @return the totalTickets
   */
  public long getTotalTickets()
  {
    return totalTickets;
  }

  /**
   * @param totalTickets the totalTickets to set
   */
  public void setTotalTickets( long totalTickets )
  {
    this.totalTickets = totalTickets;
  }

}
