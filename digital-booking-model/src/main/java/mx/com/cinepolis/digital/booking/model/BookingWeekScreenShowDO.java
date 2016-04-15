package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA entity for K_BOOKING_WEEK_SCREEN_SHOW
 * 
 * @author gsegura
 * @since 0.2.7
 */
@Entity
@Table(name = "K_BOOKING_WEEK_SCREEN_SHOW")
@NamedQueries({
    @NamedQuery(name = "BookingWeekScreenShowDO.findAll", query = "SELECT b FROM BookingWeekScreenShowDO b"),
    @NamedQuery(name = "BookingWeekScreenShowDO.findByIdBookingWeekScreenShow", query = "SELECT b FROM BookingWeekScreenShowDO b WHERE b.idBookingWeekScreenShow = :idBookingWeekScreenShow"),
    @NamedQuery(name = "BookingWeekScreenShowDO.findByNuShow", query = "SELECT b FROM BookingWeekScreenShowDO b WHERE b.nuShow = :nuShow") })
public class BookingWeekScreenShowDO extends AbstractEntity<BookingWeekScreenShowDO>
{
  private static final long serialVersionUID = -2717097030671949285L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_WEEK_SCREEN_SHOW")
  private Long idBookingWeekScreenShow;

  @Column(name = "NU_SHOW")
  private int nuShow;

  @JoinColumn(name = "ID_BOOKING_WEEK_SCREEN", referencedColumnName = "ID_BOOKING_WEEK_SCREEN")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingWeekScreenDO idBookingWeekScreen;

  /**
   * Constructor default
   */
  public BookingWeekScreenShowDO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idBookingWeekScreenShow
   */
  public BookingWeekScreenShowDO( Long idBookingWeekScreenShow )
  {
    this.idBookingWeekScreenShow = idBookingWeekScreenShow;
  }

  /**
   * @return the idBookingWeekScreenShow
   */
  public Long getIdBookingWeekScreenShow()
  {
    return idBookingWeekScreenShow;
  }

  /**
   * @param idBookingWeekScreenShow the idBookingWeekScreenShow to set
   */
  public void setIdBookingWeekScreenShow( Long idBookingWeekScreenShow )
  {
    this.idBookingWeekScreenShow = idBookingWeekScreenShow;
  }

  /**
   * @return the nuShow
   */
  public int getNuShow()
  {
    return nuShow;
  }

  /**
   * @param nuShow the nuShow to set
   */
  public void setNuShow( int nuShow )
  {
    this.nuShow = nuShow;
  }

  /**
   * @return the idBookingWeekScreen
   */
  public BookingWeekScreenDO getIdBookingWeekScreen()
  {
    return idBookingWeekScreen;
  }

  /**
   * @param idBookingWeekScreen the idBookingWeekScreen to set
   */
  public void setIdBookingWeekScreen( BookingWeekScreenDO idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
  }

  @Override
  public int compareTo( BookingWeekScreenShowDO that )
  {
    return this.idBookingWeekScreenShow.compareTo( that.idBookingWeekScreenShow );
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof BookingWeekScreenShowDO) )
    {
      return false;
    }
    BookingWeekScreenShowDO other = (BookingWeekScreenShowDO) object;
    if( (this.idBookingWeekScreenShow == null && other.idBookingWeekScreenShow != null)
        || (this.idBookingWeekScreenShow != null && !this.idBookingWeekScreenShow
            .equals( other.idBookingWeekScreenShow )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingWeekScreenShow != null ? idBookingWeekScreenShow.hashCode() : 0);
    return hash;
  }

}
