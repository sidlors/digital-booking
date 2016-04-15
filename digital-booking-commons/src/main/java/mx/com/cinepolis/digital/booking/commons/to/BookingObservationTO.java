package mx.com.cinepolis.digital.booking.commons.to;

public class BookingObservationTO extends AbstractObservationTO
{

  private static final long serialVersionUID = -6781192576425437363L;

  private Long idBooking;
  private Long idBookingWeek;
  private Long idBookingWeekScreen;

  /**
   * @return the idBooking
   */
  public Long getIdBooking()
  {
    return idBooking;
  }

  /**
   * @param idBooking the idBooking to set
   */
  public void setIdBooking( Long idBooking )
  {
    this.idBooking = idBooking;
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
   * @return the idBookingWeekScreen
   */
  public Long getIdBookingWeekScreen()
  {
    return idBookingWeekScreen;
  }

  /**
   * @param idBookingWeekScreen the idBookingWeekScreen to set
   */
  public void setIdBookingWeekScreen( Long idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object )
  {
    return super.equals( object ) && object instanceof BookingObservationTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (this.getId() != null ? this.getId().hashCode() : 0);
    return hash;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( AbstractObservationTO o )
  {
    return super.getId().compareTo( o.getId() );
  }
}
