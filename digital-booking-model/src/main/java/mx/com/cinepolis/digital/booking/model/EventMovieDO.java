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
 * JPA entity for K_EVENT_MOVIE
 * 
 * @author kperez
 */
@Entity
@Table(name = "K_EVENT_MOVIE")
@NamedQueries({
    @NamedQuery(name = "EventMovieDO.findAll", query = "SELECT e FROM EventMovieDO e"),
    @NamedQuery(name = "EventMovieDO.findByIdEventMovie", query = "SELECT e FROM EventMovieDO e WHERE e.idEventMovie = :idEventMovie"),
    @NamedQuery(name = "EventMovieDO.findByDtRelease", query = "SELECT e FROM EventMovieDO e WHERE e.dtRelease = :dtRelease"),
    @NamedQuery(name = "EventMovieDO.findByDsDirector", query = "SELECT e FROM EventMovieDO e WHERE e.dsDirector = :dsDirector"),
    @NamedQuery(name = "EventMovieDO.findByDsSynopsis", query = "SELECT e FROM EventMovieDO e WHERE e.dsSynopsis = :dsSynopsis"),
    @NamedQuery(name = "EventMovieDO.findByDsRating", query = "SELECT e FROM EventMovieDO e WHERE e.dsRating = :dsRating"),
    @NamedQuery(name = "EventMovieDO.findByDsScript", query = "SELECT e FROM EventMovieDO e WHERE e.dsScript = :dsScript"),
    @NamedQuery(name = "EventMovieDO.findByDsActor", query = "SELECT e FROM EventMovieDO e WHERE e.dsActor = :dsActor"),
    @NamedQuery(name = "EventMovieDO.findByDsGenre", query = "SELECT e FROM EventMovieDO e WHERE e.dsGenre = :dsGenre"),
    @NamedQuery(name = "EventMovieDO.findByDsUrl", query = "SELECT e FROM EventMovieDO e WHERE e.dsUrl = :dsUrl"),
    @NamedQuery(name = "EventMovieDO.findByDsCountry", query = "SELECT e FROM EventMovieDO e WHERE e.dsCountry = :dsCountry"),
    @NamedQuery(name = "EventMovieDO.findByIdEvent", query = "SELECT e FROM EventMovieDO e WHERE e.idEvent = :idEvent"),
    @NamedQuery(name = "EventMovieDO.findByidDistributor", query = "SELECT e FROM EventMovieDO e WHERE e.idDistributor = :idDistributor AND e.idEvent.fgActive = true"),
    @NamedQuery(name = "EventMovieDO.findByIdVistaAndActive", query = "SELECT e FROM EventMovieDO e WHERE e.idEvent.idVista = :idVista AND e.idEvent.fgActive = true") })
public class EventMovieDO extends AbstractEntity<EventMovieDO>
{

  private static final long serialVersionUID = 4435023014928393936L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_EVENT_MOVIE")
  private Long idEventMovie;

  @Column(name = "DT_RELEASE")
  @Temporal(TemporalType.DATE)
  private Date dtRelease;

  @Column(name = "DS_DIRECTOR")
  private String dsDirector;

  @Column(name = "DS_SYNOPSIS")
  private String dsSynopsis;

  @Column(name = "DS_RATING")
  private String dsRating;

  @Column(name = "DS_SCRIPT")
  private String dsScript;

  @Column(name = "DS_ACTOR")
  private String dsActor;

  @Column(name = "DS_GENRE")
  private String dsGenre;

  @Column(name = "DS_URL")
  private String dsUrl;

  @Column(name = "DS_COUNTRY")
  private String dsCountry;

  @Column(name = "DS_ORIGINAL_NAME")
  private String dsOriginalName;

  @JoinColumn(name = "ID_EVENT", referencedColumnName = "ID_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventDO idEvent;

  @Column(name = "ID_MOVIE_IMAGE")
  private Long idMovieImage;

  @JoinColumn(name = "ID_DISTRIBUTOR", referencedColumnName = "ID_DISTRIBUTOR")
  @ManyToOne(fetch = FetchType.LAZY)
  private DistributorDO idDistributor;

  /**
   * Constructor default
   */
  public EventMovieDO()
  {
  }

  /**
   * Constructor by idEventMovie
   * 
   * @param idEventMovie
   */
  public EventMovieDO( Long idEventMovie )
  {
    this.idEventMovie = idEventMovie;
  }

  /**
   * @return the idEventMovie
   */
  public Long getIdEventMovie()
  {
    return idEventMovie;
  }

  /**
   * @param idEventMovie the idEventMovie to set
   */
  public void setIdEventMovie( Long idEventMovie )
  {
    this.idEventMovie = idEventMovie;
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
   * @return the dsDirector
   */
  public String getDsDirector()
  {
    return dsDirector;
  }

  /**
   * @param dsDirector the dsDirector to set
   */
  public void setDsDirector( String dsDirector )
  {
    this.dsDirector = dsDirector;
  }

  /**
   * @return the dsSynopsis
   */
  public String getDsSynopsis()
  {
    return dsSynopsis;
  }

  /**
   * @param dsSynopsis the dsSynopsis to set
   */
  public void setDsSynopsis( String dsSynopsis )
  {
    this.dsSynopsis = dsSynopsis;
  }

  /**
   * @return the dsRating
   */
  public String getDsRating()
  {
    return dsRating;
  }

  /**
   * @param dsRating the dsRating to set
   */
  public void setDsRating( String dsRating )
  {
    this.dsRating = dsRating;
  }

  /**
   * @return the dsScript
   */
  public String getDsScript()
  {
    return dsScript;
  }

  /**
   * @param dsScript the dsScript to set
   */
  public void setDsScript( String dsScript )
  {
    this.dsScript = dsScript;
  }

  /**
   * @return the dsActor
   */
  public String getDsActor()
  {
    return dsActor;
  }

  /**
   * @param dsActor the dsActor to set
   */
  public void setDsActor( String dsActor )
  {
    this.dsActor = dsActor;
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
   * @return the dsUrl
   */
  public String getDsUrl()
  {
    return dsUrl;
  }

  /**
   * @param dsUrl the dsUrl to set
   */
  public void setDsUrl( String dsUrl )
  {
    this.dsUrl = dsUrl;
  }

  /**
   * @return the dsCountry
   */
  public String getDsCountry()
  {
    return dsCountry;
  }

  /**
   * @param dsCountry the dsCountry to set
   */
  public void setDsCountry( String dsCountry )
  {
    this.dsCountry = dsCountry;
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
   * @return the idMovieImage
   */
  public Long getIdMovieImage()
  {
    return idMovieImage;
  }

  /**
   * @param idMovieImage the idMovieImage to set
   */
  public void setIdMovieImage( Long idMovieImage )
  {
    this.idMovieImage = idMovieImage;
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
  public int hashCode()
  {
    int hash = 0;
    hash += (idEventMovie != null ? idEventMovie.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof EventMovieDO) )
    {
      return false;
    }
    EventMovieDO other = (EventMovieDO) object;
    if( (this.idEventMovie == null && other.idEventMovie != null)
        || (this.idEventMovie != null && !this.idEventMovie.equals( other.idEventMovie )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EventMovieDO[ idEventMovie=" + idEventMovie + " ]";
  }

  @Override
  public int compareTo( EventMovieDO o )
  {
    return this.idEventMovie.compareTo( o.idEventMovie );
  }

}
