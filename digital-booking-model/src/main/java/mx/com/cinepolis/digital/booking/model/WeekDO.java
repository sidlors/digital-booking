package mx.com.cinepolis.digital.booking.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for C_WEEK
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_WEEK")
@NamedQueries({
    @NamedQuery(name = "WeekDO.findAll", query = "SELECT w FROM WeekDO w"),
    @NamedQuery(name = "WeekDO.findByIdWeek", query = "SELECT w FROM WeekDO w WHERE w.idWeek = :idWeek"),
    @NamedQuery(name = "WeekDO.findByCurrentWeek", query = "SELECT w FROM WeekDO w WHERE :date BETWEEN w.dtStartingDayWeek AND w.dtFinalDayWeek AND w.fgActive = true AND w.fgSpecialWeek = false ORDER BY w.dtStartingDayWeek, w.dtFinalDayWeek"),
    @NamedQuery(name = "WeekDO.findBySpecialWeeks", query = "SELECT w FROM WeekDO w WHERE (w.dtStartingDayWeek BETWEEN :dateStart AND :dateEnd OR w.dtFinalDayWeek BETWEEN :dateStart AND :dateEnd) AND w.fgActive = true AND w.fgSpecialWeek = true ORDER BY w.dtStartingDayWeek, w.dtFinalDayWeek"),
    @NamedQuery(name = "WeekDO.findByDtStartingDayWeek", query = "SELECT w FROM WeekDO w WHERE w.dtStartingDayWeek = :dtStartingDayWeek"),
    @NamedQuery(name = "WeekDO.findByDtFinalDayWeek", query = "SELECT w FROM WeekDO w WHERE w.dtFinalDayWeek = :dtFinalDayWeek"),
    @NamedQuery(name = "WeekDO.findByFgActive", query = "SELECT w FROM WeekDO w WHERE w.fgActive = :fgActive"),
    @NamedQuery(name = "WeekDO.findByDtLastModification", query = "SELECT w FROM WeekDO w WHERE w.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "WeekDO.findByIdLastUserModifier", query = "SELECT w FROM WeekDO w WHERE w.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "WeekDO.countWeeks", query = "SELECT COUNT(w) FROM WeekDO w WHERE w.dtStartingDayWeek BETWEEN :firstDate1 AND :firstDate2 AND w.fgActive = true AND w.fgSpecialWeek = false"),
    @NamedQuery(name = "WeekDO.findMaxNumWeek", query = "SELECT w FROM WeekDO w WHERE w.nuWeek = (SELECT DISTINCT MAX(w2.nuWeek) FROM WeekDO w2 WHERE w2.fgSpecialWeek = false AND w2.fgActive = true AND w2.nuYear = :currentYear) AND w.nuYear = :currentYear"),
    @NamedQuery(name = "WeekDO.findByDates", query = "SELECT w FROM WeekDO w WHERE w.dtStartingDayWeek = :startDate AND w.dtFinalDayWeek = :finalDate AND w.fgActive = true AND w.fgSpecialWeek = :specialWeek"),
    @NamedQuery(name = "WeekDO.findByStartDayAndFinalDay",query = "SELECT w FROM WeekDO w WHERE :dateStart BETWEEN w.dtStartingDayWeek AND  w.dtFinalDayWeek OR :dateEnd BETWEEN w.dtStartingDayWeek AND w.dtFinalDayWeek OR w.dtStartingDayWeek BETWEEN :dateStart AND :dateEnd OR w.dtFinalDayWeek BETWEEN :dateStart AND :dateEnd ORDER BY w.dtStartingDayWeek, w.dtFinalDayWeek"),
    })
public class WeekDO extends AbstractSignedEntity<WeekDO>
{
  private static final long serialVersionUID = 2950999459069892098L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_WEEK", nullable = false)
  private Integer idWeek;

  @Column(name = "NU_WEEK")
  private int nuWeek;

  @Column(name = "NU_YEAR")
  private int nuYear;

  @Column(name = "DT_STARTING_DAY_WEEK", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtStartingDayWeek;

  @Column(name = "DT_FINAL_DAY_WEEK", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtFinalDayWeek;

  @Column(name = "FG_SPECIAL_WEEK")
  private boolean fgSpecialWeek;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idWeek", fetch = FetchType.LAZY)
  private List<BookingWeekDO> bookingWeekDOList;
  
  @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE},mappedBy="idWeek",fetch=FetchType.LAZY)
  private List<SpecialEventWeekDO> specialEventWeekDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idWeek", fetch = FetchType.LAZY)
  private List<BookingIncomeDO> bookingIncomeDOList;

  /**
   * Constructor default
   */
  public WeekDO()
  {
  }

  /**
   * Constructor by idBookingStatus
   * 
   * @param idBookingStatus
   */
  public WeekDO( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the idWeek
   */
  public Integer getIdWeek()
  {
    return idWeek;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the nuWeek
   */
  public int getNuWeek()
  {
    return nuWeek;
  }

  /**
   * @param nuWeek the nuWeek to set
   */
  public void setNuWeek( int nuWeek )
  {
    this.nuWeek = nuWeek;
  }

  /**
   * @return the nuYear
   */
  public int getNuYear()
  {
    return nuYear;
  }

  /**
   * @param nuYear the nuYear to set
   */
  public void setNuYear( int nuYear )
  {
    this.nuYear = nuYear;
  }

  /**
   * @return the dtStartingDayWeek
   */
  public Date getDtStartingDayWeek()
  {
    return CinepolisUtils.enhancedClone( dtStartingDayWeek );
  }

  /**
   * @param dtStartingDayWeek the dtStartingDayWeek to set
   */
  public void setDtStartingDayWeek( Date dtStartingDayWeek )
  {
    this.dtStartingDayWeek = CinepolisUtils.enhancedClone( dtStartingDayWeek );
  }

  /**
   * @return the dtFinalDayWek
   */
  public Date getDtFinalDayWeek()
  {
    return CinepolisUtils.enhancedClone( dtFinalDayWeek );
  }

  /**
   * @param dtFinalDayWek the dtFinalDayWek to set
   */
  public void setDtFinalDayWeek( Date dtFinalDayWeek )
  {
    this.dtFinalDayWeek = CinepolisUtils.enhancedClone( dtFinalDayWeek );
  }

  /**
   * @return the bookingWeekDOList
   */
  public List<BookingWeekDO> getBookingWeekDOList()
  {
    return bookingWeekDOList;
  }

  /**
   * @param bookingWeekDOList the bookingWeekDOList to set
   */
  public void setBookingWeekDOList( List<BookingWeekDO> bookingWeekDOList )
  {
    this.bookingWeekDOList = bookingWeekDOList;
  }

  /**
   * @return the fgSpecialWeek
   */
  public boolean isFgSpecialWeek()
  {
    return fgSpecialWeek;
  }

  /**
   * @param fgSpecialWeek the fgSpecialWeek to set
   */
  public void setFgSpecialWeek( boolean fgSpecialWeek )
  {
    this.fgSpecialWeek = fgSpecialWeek;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idWeek != null ? idWeek.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof WeekDO) )
    {
      return false;
    }
    WeekDO other = (WeekDO) object;
    if( (this.idWeek == null && other.idWeek != null) || (this.idWeek != null && !this.idWeek.equals( other.idWeek )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idWeek", this.idWeek )
        .append( "dtStartingDayWeek", this.dtStartingDayWeek ).toString();
  }

  @Override
  public int compareTo( WeekDO other )
  {
    return this.idWeek.compareTo( other.idWeek );
  }

  /**
   * @return the specialEventWeekDOList
   */
  public List<SpecialEventWeekDO> getSpecialEventWeekDOList()
  {
    return specialEventWeekDOList;
  }

  /**
   * @param specialEventWeekDOList the specialEventWeekDOList to set
   */
  public void setSpecialEventWeekDOList( List<SpecialEventWeekDO> specialEventWeekDOList )
  {
    this.specialEventWeekDOList = specialEventWeekDOList;
  }

  public List<BookingIncomeDO> getBookingIncomeDOList()
  {
    return bookingIncomeDOList;
  }

  public void setBookingIncomeDOList( List<BookingIncomeDO> bookingIncomeDOList )
  {
    this.bookingIncomeDOList = bookingIncomeDOList;
  }

}
