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

/**
 * JPA entity for C_TRAILER
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_TRAILER")
@NamedQueries({ @NamedQuery(name = "TrailerDO.findAll", query = "SELECT t FROM TrailerDO t"),
    @NamedQuery(name = "TrailerDO.findById", query = "SELECT t FROM TrailerDO t where t.idTrailer = :idTrailer") })
public class TrailerDO extends AbstractSignedEntity<TrailerDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = -2088029832921568545L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_TRAILER")
  private Long idTrailer;

  @JoinColumn(name = "ID_TRAILER_STATUS", referencedColumnName = "ID_TRAILER_STATUS", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TrailerStatusDO idTrailerStatus;

  @JoinColumn(name = "ID_DISTRIBUTOR", referencedColumnName = "ID_DISTRIBUTOR", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private DistributorDO idDistributor;

  @Column(name = "DS_NAME")
  private String dsName;

  @Column(name = "QT_DURATION")
  private Integer qtDuration;

  @Column(name = "DS_GENRE")
  private String dsGenre;

  @Column(name = "DT_RELEASE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtRelease;

  @Column(name = "FG_CURRENT")
  private Boolean fgCurrent;
/**
 * Constructor by default
 */
  public TrailerDO(  )
  {
  }
  /**
   * Constructor by identifier
   * @param idTrailer
   */
  public TrailerDO( Long idTrailer )
  {
    this.idTrailer = idTrailer;
  }

  /**
   * @return the idTrailer
   */
  public Long getIdTrailer()
  {
    return idTrailer;
  }

  /**
   * @param idTrailer the idTrailer to set
   */
  public void setIdTrailer( Long idTrailer )
  {
    this.idTrailer = idTrailer;
  }

  /**
   * @return the idTrailerStatus
   */
  public TrailerStatusDO getIdTrailerStatus()
  {
    return idTrailerStatus;
  }

  /**
   * @param idTrailerStatus the idTrailerStatus to set
   */
  public void setIdTrailerStatus( TrailerStatusDO idTrailerStatus )
  {
    this.idTrailerStatus = idTrailerStatus;
  }

  /**
   * @return the idDistributor
   */
  public DistributorDO getIdDistributor()
  {
    return idDistributor;
  }

  /**
   * @param idDistributor the idDistributor to set
   */
  public void setIdDistributor( DistributorDO idDistributor )
  {
    this.idDistributor = idDistributor;
  }

  /**
   * @return the dsName
   */
  public String getDsName()
  {
    return dsName;
  }

  /**
   * @param dsName the dsName to set
   */
  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  /**
   * @return the qtDuration
   */
  public Integer getQtDuration()
  {
    return qtDuration;
  }

  /**
   * @param qtDuration the qtDuration to set
   */
  public void setQtDuration( Integer qtDuration )
  {
    this.qtDuration = qtDuration;
  }

  /**
   * @return the dsGenre
   */
  public String getDsGenre()
  {
    return dsGenre;
  }

  /**
   * @param dsGenre the dsGenre to set
   */
  public void setDsGenre( String dsGenre )
  {
    this.dsGenre = dsGenre;
  }

  /**
   * @return the dtRelease
   */
  public Date getDtRelease()
  {
    return CinepolisUtils.enhancedClone( dtRelease);
  }

  /**
   * @param dtRelease the dtRelease to set
   */
  public void setDtRelease( Date dtRelease )
  {
    this.dtRelease = dtRelease;
  }

  /**
   * @return the fgCurrent
   */
  public Boolean getFgCurrent()
  {
    return fgCurrent;
  }

  /**
   * @param fgCurrent the fgCurrent to set
   */
  public void setFgCurrent( Boolean fgCurrent )
  {
    this.fgCurrent = fgCurrent;
  }

  @Override
  public int compareTo( TrailerDO o )
  {
    return this.idTrailer.compareTo( o.idTrailer );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof TrailerDO) )
    {
      return false;
    }
    TrailerDO other = (TrailerDO) obj;
    if( (this.idTrailer == null && other.idTrailer != null)
        || (this.idTrailer != null && !this.idTrailer.equals( other.idTrailer )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTrailer != null ? idTrailer.hashCode() : 0);
    return hash;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "TrailerDO [idTrailer=" + idTrailer + ", idTrailerStatus=" + idTrailerStatus + ", idDistributor="
        + idDistributor + ", dsName=" + dsName + ", qtDuration=" + qtDuration + ", dsGenre=" + dsGenre + ", dtRelease="
        + dtRelease + ", fgCurrent=" + fgCurrent + "]";
  }

}
