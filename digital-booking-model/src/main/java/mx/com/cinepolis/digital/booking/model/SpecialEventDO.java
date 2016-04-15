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
 * JPA for entity K_SPECIAL_EVENT
 * 
 * @author gsegura
 */
@Entity
@Table(name = "K_SPECIAL_EVENT")
@NamedQueries({
    @NamedQuery(name = "SpecialEventDO.findAll", query = "SELECT s FROM SpecialEventDO s"),
    @NamedQuery(name = "SpecialEventDO.findByIdSpecialEvent", query = "SELECT s FROM SpecialEventDO s WHERE s.idSpecialEvent = :idSpecialEvent"),
    @NamedQuery(name = "SpecialEventDO.findByDtRelease", query = "SELECT s FROM SpecialEventDO s WHERE s.dtRelease = :dtRelease"),
    @NamedQuery(name = "SpecialEventDO.findByDsGenre", query = "SELECT s FROM SpecialEventDO s WHERE s.dsGenre = :dsGenre"),
    @NamedQuery(name = "SpecialEventDO.findByDsOriginalName", query = "SELECT s FROM SpecialEventDO s WHERE s.dsOriginalName = :dsOriginalName") })
public class SpecialEventDO extends AbstractEntity<SpecialEventDO>
{

  /**
   * 
   */
  private static final long serialVersionUID = -4584544585228421404L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_SPECIAL_EVENT")
  private Long idSpecialEvent;

  @Column(name = "DT_RELEASE")
  @Temporal(TemporalType.DATE)
  private Date dtRelease;

  @Column(name = "DS_GENRE")
  private String dsGenre;

  @Column(name = "DS_ORIGINAL_NAME")
  private String dsOriginalName;

  @JoinColumn(name = "ID_EVENT", referencedColumnName = "ID_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventDO idEvent;

  @JoinColumn(name = "ID_DISTRIBUTOR", referencedColumnName = "ID_DISTRIBUTOR")
  @ManyToOne(fetch = FetchType.LAZY)
  private DistributorDO idDistributor;

  /**
   * Constructor default
   */
  public SpecialEventDO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idSpecialEvent
   */
  public SpecialEventDO( Long idSpecialEvent )
  {
    this.idSpecialEvent = idSpecialEvent;
  }

  /**
   * @return the idSpecialEvent
   */
  public Long getIdSpecialEvent()
  {
    return idSpecialEvent;
  }

  /**
   * @param idSpecialEvent the idSpecialEvent to set
   */
  public void setIdSpecialEvent( Long idSpecialEvent )
  {
    this.idSpecialEvent = idSpecialEvent;
  }

  /**
   * @return the dtRelease
   */
  public Date getDtRelease()
  {
    return CinepolisUtils.enhancedClone( dtRelease );
  }

  /**
   * @param dtRelease the dtRelease to set
   */
  public void setDtRelease( Date dtRelease )
  {
    this.dtRelease = CinepolisUtils.enhancedClone( dtRelease );
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
   * @return the dsOriginalName
   */
  public String getDsOriginalName()
  {
    return dsOriginalName;
  }

  /**
   * @param dsOriginalName the dsOriginalName to set
   */
  public void setDsOriginalName( String dsOriginalName )
  {
    this.dsOriginalName = dsOriginalName;
  }

  /**
   * @return the idEvent
   */
  public EventDO getIdEvent()
  {
    return idEvent;
  }

  /**
   * @param idEvent the idEvent to set
   */
  public void setIdEvent( EventDO idEvent )
  {
    this.idEvent = idEvent;
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

  @Override
  public int compareTo( SpecialEventDO that )
  {
    return this.idSpecialEvent.compareTo( that.idSpecialEvent );
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof SpecialEventDO) )
    {
      return false;
    }
    SpecialEventDO other = (SpecialEventDO) object;
    if( (this.idSpecialEvent == null && other.idSpecialEvent != null)
        || (this.idSpecialEvent != null && !this.idSpecialEvent.equals( other.idSpecialEvent )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idSpecialEvent != null ? idSpecialEvent.hashCode() : 0);
    return hash;
  }

}
