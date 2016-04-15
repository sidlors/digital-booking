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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * JPA entity for K_NEWS_FEED
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_NEWS_FEED")
@NamedQueries({
    @NamedQuery(name = "NewsFeedDO.findAll", query = "SELECT n FROM NewsFeedDO n"),
    @NamedQuery(name = "NewsFeedDO.findByDate", query = "SELECT n FROM NewsFeedDO n WHERE :date BETWEEN n.dtStart AND n.dtEnd AND n.fgActive = true"),
    @NamedQuery(name = "NewsFeedDO.findByIdNewsFeed", query = "SELECT n FROM NewsFeedDO n WHERE n.idNewsFeed = :idNewsFeed"),
    @NamedQuery(name = "NewsFeedDO.findByDtStart", query = "SELECT n FROM NewsFeedDO n WHERE n.dtStart = :dtStart"),
    @NamedQuery(name = "NewsFeedDO.findByDtEnd", query = "SELECT n FROM NewsFeedDO n WHERE n.dtEnd = :dtEnd"),
    @NamedQuery(name = "NewsFeedDO.findByFgActive", query = "SELECT n FROM NewsFeedDO n WHERE n.fgActive = :fgActive"),
    @NamedQuery(name = "NewsFeedDO.findByDtLastModification", query = "SELECT n FROM NewsFeedDO n WHERE n.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "NewsFeedDO.findByIdLastUserModifier", query = "SELECT n FROM NewsFeedDO n WHERE n.idLastUserModifier = :idLastUserModifier") })
public class NewsFeedDO extends AbstractSignedEntity<NewsFeedDO>
{
  private static final long serialVersionUID = 1457330028114768756L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_NEWS_FEED", nullable = false)
  private Long idNewsFeed;

  @Column(name = "DT_START", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtStart;

  @Column(name = "DT_END", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date dtEnd;

  @JoinColumn(name = "ID_OBSERVATION", referencedColumnName = "ID_OBSERVATION", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ObservationDO idObservation;



  @ManyToMany(mappedBy = "newsFeedDOList", fetch = FetchType.LAZY, cascade={CascadeType.ALL})
  private List<RegionDO> regionDOList;

  /**
   * Constructor default
   */
  public NewsFeedDO()
  {
  }

  /**
   * Constructor by idNewsFeed
   * 
   * @param idNewsFeed
   */
  public NewsFeedDO( Long idNewsFeed )
  {
    this.idNewsFeed = idNewsFeed;
  }

  /**
   * @return the idNewsFeed
   */
  public Long getIdNewsFeed()
  {
    return idNewsFeed;
  }

  /**
   * @param idNewsFeed the idNewsFeed to set
   */
  public void setIdNewsFeed( Long idNewsFeed )
  {
    this.idNewsFeed = idNewsFeed;
  }

  /**
   * @return the dtStart
   */
  public Date getDtStart()
  {
    return CinepolisUtils.enhancedClone( dtStart );
  }

  /**
   * @param dtStart the dtStart to set
   */
  public void setDtStart( Date dtStart )
  {
    this.dtStart = CinepolisUtils.enhancedClone( dtStart );
  }

  /**
   * @return the dtEnd
   */
  public Date getDtEnd()
  {
    return CinepolisUtils.enhancedClone( dtEnd );
  }

  /**
   * @param dtEnd the dtEnd to set
   */
  public void setDtEnd( Date dtEnd )
  {
    this.dtEnd = CinepolisUtils.enhancedClone( dtEnd );
  }

  /**
   * @return the idObservation
   */
  public ObservationDO getIdObservation()
  {
    return idObservation;
  }

  /**
   * @param idObservation the idObservation to set
   */
  public void setIdObservation( ObservationDO idObservation )
  {
    this.idObservation = idObservation;
  }


  /**
   * @return the regionDOList
   */
  public List<RegionDO> getRegionDOList()
  {
    return regionDOList;
  }

  /**
   * @param regionDOList the regionDOList to set
   */
  public void setRegionDOList( List<RegionDO> regionDOList )
  {
    this.regionDOList = regionDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idNewsFeed != null ? idNewsFeed.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof NewsFeedDO) )
    {
      return false;
    }
    NewsFeedDO other = (NewsFeedDO) object;
    if( (this.idNewsFeed == null && other.idNewsFeed != null)
        || (this.idNewsFeed != null && !this.idNewsFeed.equals( other.idNewsFeed )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.NewsFeedDO[ idNewsFeed=" + idNewsFeed + " ]";
  }

  @Override
  public int compareTo( NewsFeedDO other )
  {
    return this.idNewsFeed.compareTo( other.idNewsFeed );
  }

}
