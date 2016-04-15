package mx.com.cinepolis.digital.booking.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

@Entity
@Table(name = "K_PRESALE")
@NamedQueries({ @NamedQuery(name = "PresaleDO.findAll", query = "SELECT p FROM PresaleDO p"),
    @NamedQuery(name = "PresaleDO.findbyId", query = "SELECT p FROM PresaleDO p WHERE p.idPresale = :idPresale"),
    @NamedQuery(name = "PresaleDO.findActivePresaleForDeactivate", query = "SELECT p FROM PresaleDO p WHERE p.fgActive = true AND p.dtFinalDayPresale < :today"),
    @NamedQuery(name = "PresaleDO.countAllActive", query = "SELECT COUNT(p) FROM PresaleDO p WHERE p.fgActive = true")})
public class PresaleDO extends AbstractSignedEntity<PresaleDO>
{

  /**
   * 
   */
  private static final long serialVersionUID = -4471238790520866603L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_PRESALE", nullable = false)
  private Long idPresale;

  @Column(name = "DT_START_DAY_PRESALE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtStartDayPresale;

  @Column(name = "DT_FINAL_DAY_PRESALE", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtFinalDayPresale;

  @Column(name = "DT_RELEASE_DAY", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtReleaseDay;

  @JoinColumn(name = "ID_BOOKING_SPECIAL_EVENT_SCREEN", referencedColumnName = "ID_BOOKING_SPECIAL_EVENT_SCREEN", nullable = true)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private BookingSpecialEventScreenDO idBookingSpecialEventScreen;

  @JoinColumn(name = "ID_BOOKING_WEEK_SCREEN", referencedColumnName = "ID_BOOKING_WEEK_SCREEN", nullable = true)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private BookingWeekScreenDO idBookingWeekScreen;

  /**
   * Contructor default
   */
  public PresaleDO()
  {
  }

  /**
   * Constructor by id
   */
  public PresaleDO( Long idPresale )
  {
    this.idPresale = idPresale;
  }

  /**
   * @return idPresale
   */
  public Long getIdPresale()
  {
    return idPresale;
  }

  /**
   * @param idPresale the idPresale to set
   */
  public void setIdPresale( Long idPresale )
  {
    this.idPresale = idPresale;
  }

  /**
   * @return dtStartDayPresale
   */
  public Date getDtStartDayPresale()
  {
    return CinepolisUtils.enhancedClone( dtStartDayPresale );
  }

  /**
   * @param dtStartDayPresale the dtStartDayPresale to set
   */
  public void setDtStartDayPresale( Date dtStartDayPresale )
  {
    this.dtStartDayPresale = dtStartDayPresale;
  }

  /**
   * @return dtFinalDayPresale
   */
  public Date getDtFinalDayPresale()
  {
    return CinepolisUtils.enhancedClone( dtFinalDayPresale );
  }

  /**
   * @param dtFinalDayPresale the dtFinalDayPresale to set
   */
  public void setDtFinalDayPresale( Date dtFinalDayPresale )
  {
    this.dtFinalDayPresale = dtFinalDayPresale;
  }

  /**
   * @return dtReleaseDay
   */
  public Date getDtReleaseDay()
  {
    return CinepolisUtils.enhancedClone( dtReleaseDay );
  }

  /**
   * @param dtReleaseDay the dtReleaseDay to set
   */
  public void setDtReleaseDay( Date dtReleaseDay )
  {
    this.dtReleaseDay = dtReleaseDay;
  }

  /**
   * @return idBookingSpecialEventScreen
   */
  public BookingSpecialEventScreenDO getIdBookingSpecialEventScreen()
  {
    return idBookingSpecialEventScreen;
  }

  /**
   * @param idBookingSpecialEventScreen the idBookingSpecialEventScreen to set
   */
  public void setIdBookingSpecialEventScreen( BookingSpecialEventScreenDO idBookingSpecialEventScreen )
  {
    this.idBookingSpecialEventScreen = idBookingSpecialEventScreen;
  }

  /**
   * @return idBookingWeekScreenDO
   */
  public BookingWeekScreenDO getIdBookingWeekScreen()
  {
    return idBookingWeekScreen;
  }

  /**
   * @param idBookingWeekScreenDO the idBookingWeekScreenDO to set
   */
  public void setIdBookingWeekScreen( BookingWeekScreenDO idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
  }

  @Override
  public int compareTo( PresaleDO other )
  {
    return this.idPresale.compareTo( other.idPresale );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof PresaleDO) )
    {
      return false;
    }
    PresaleDO other = (PresaleDO) obj;
    if( (this.idPresale == null && other.idPresale != null)
        || (this.idPresale != null && !this.idPresale.equals( other.idPresale )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idPresale != null ? idPresale.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "PresaleDO [idPresale=" + idPresale + ", dtStartDayPresale=" + dtStartDayPresale + ", dtFinalDayPresale="
        + dtFinalDayPresale + ", dtReleaseDay=" + dtReleaseDay + "]";
  }

}
