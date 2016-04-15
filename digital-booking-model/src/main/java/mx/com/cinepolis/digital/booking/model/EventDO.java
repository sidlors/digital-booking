/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for K_EVENT
 * 
 * @author kperez
 */
@Entity
@Table(name = "K_EVENT")
@NamedQueries({
    @NamedQuery(name = "EventDO.findAll", query = "SELECT e FROM EventDO e"),
    @NamedQuery(name = "EventDO.findByIdEvent", query = "SELECT e FROM EventDO e WHERE e.idEvent = :idEvent"),
    @NamedQuery(name = "EventDO.findByIdVista", query = "SELECT e FROM EventDO e WHERE e.idVista = :idVista"),
    @NamedQuery(name = "EventDO.findByIdVistaAndActive", query = "SELECT e FROM EventDO e WHERE e.idVista = :idVista and e.fgActive = true"),
    @NamedQuery(name = "EventDO.findByDsName", query = "SELECT e FROM EventDO e WHERE e.dsName = :dsName"),
    @NamedQuery(name = "EventDO.findByDsCodeDbs", query = "SELECT e FROM EventDO e WHERE e.dsCodeDbs = :dsCodeDbs and e.fgActive = true"),
    @NamedQuery(name = "EventDO.findByQtDuration", query = "SELECT e FROM EventDO e WHERE e.qtDuration = :qtDuration"),
    @NamedQuery(name = "EventDO.findByQtCopy", query = "SELECT e FROM EventDO e WHERE e.qtCopy = :qtCopy"),
    @NamedQuery(name = "EventDO.findByFgActive", query = "SELECT e FROM EventDO e WHERE e.fgActive = :fgActive"),
    @NamedQuery(name = "EventDO.findByDtLastModification", query = "SELECT e FROM EventDO e WHERE e.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "EventDO.findByIdLastUserModifier", query = "SELECT e FROM EventDO e WHERE e.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "EventDO.findActiveMovies", query = "SELECT e.idEvent, e.dsName, e.fgPremiere, d.idDistributor, d.dsName FROM EventDO e LEFT JOIN e.eventMovieDOList AS m LEFT JOIN m.idDistributor as d WHERE e.currentMovie = true AND e.fgActive = true AND e.idEventType.idEventType = 1 AND e.fgFestival = false ORDER BY e.fgPremiere DESC, e.dsName"),
    @NamedQuery(name = "EventDO.findActiveMoviesPremiere", query = "SELECT e.idEvent, e.dsName, e.fgPremiere, d.idDistributor, d.dsName FROM EventDO e LEFT JOIN e.eventMovieDOList AS m LEFT JOIN m.idDistributor as d WHERE e.fgPremiere = true AND e.fgActive = true AND e.currentMovie = true ORDER BY e.dsName"),
    @NamedQuery(name = "EventDO.findActiveMoviesPrerelease", query = "SELECT e.idEvent, e.dsName, e.fgPremiere, d.idDistributor, d.dsName FROM EventDO e LEFT JOIN e.eventMovieDOList AS m LEFT JOIN m.idDistributor as d WHERE e.fgPrerelease = true AND e.fgActive = true AND e.currentMovie = true ORDER BY e.dsName"),
    @NamedQuery(name = "EventDO.findActiveFestival", query = "SELECT e.idEvent, e.dsName, e.fgPremiere, d.idDistributor, d.dsName FROM EventDO e LEFT JOIN e.eventMovieDOList AS m LEFT JOIN m.idDistributor as d WHERE e.currentMovie = true AND e.fgActive = true AND e.fgFestival = true OR e.idEventType.idEventType=2 ORDER BY e.fgPremiere DESC, e.dsName"),
    @NamedQuery(name = "EventDO.turnOffPremiere", query = "UPDATE EventDO e SET e.fgPremiere = false, e.fgActiveIa = false")
    })
public class EventDO extends AbstractSignedEntity<EventDO>
{

  private static final long serialVersionUID = -5557873846327111515L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_EVENT", nullable = false)
  private Long idEvent;

  @Column(name = "ID_VISTA")
  private String idVista;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @Column(name = "DS_CODE_DBS")
  private String dsCodeDbs;

  @Column(name = "QT_DURATION")
  private Integer qtDuration;

  @Column(name = "QT_COPY")
  private Integer qtCopy;

  @Column(name = "FG_PREMIERE")
  private Boolean fgPremiere;

  @ManyToMany(mappedBy = "eventDOList", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  private List<CategoryDO> categoryDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvent", fetch = FetchType.LAZY)
  private List<BookingDO> bookingDOList;

  @JoinColumn(name = "ID_EVENT_TYPE", referencedColumnName = "ID_EVENT_TYPE")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventTypeDO idEventType;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvent", fetch = FetchType.LAZY)
  private List<EventMovieDO> eventMovieDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEvent", fetch = FetchType.LAZY)
  private List<BookingIncomeDO> bookingIncomeDOList;

  @Column(name = "FG_CURRENT")
  private boolean currentMovie;
  
  @Column(name = "FG_PRERELEASE")
  private boolean fgPrerelease;
  
  @Column(name="FG_FESTIVAL")
  private boolean fgFestival;
  
  @Column(name = "FG_ACTIVE_IA")
  private boolean fgActiveIa;
  
  @Column(name = "FG_ALTERNATE_CONTENT")
  private boolean fgAlternateContent;

  /**
   * Constructor default
   */
  public EventDO()
  {
  }

  /**
   * Constructor by idEvent
   * 
   * @param idEvent
   */
  public EventDO( Long idEvent )
  {
    this.idEvent = idEvent;
  }

  /**
   * @return the idEvent
   */
  public Long getIdEvent()
  {
    return idEvent;
  }

  /**
   * @param idEvent the idEvent to set
   */
  public void setIdEvent( Long idEvent )
  {
    this.idEvent = idEvent;
  }

  /**
   * @return the idVista
   */
  public String getIdVista()
  {
    return idVista;
  }

  /**
   * @param idVista the idVista to set
   */
  public void setIdVista( String idVista )
  {
    this.idVista = idVista;
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
   * @return the dsCodeDbs
   */
  public String getDsCodeDbs()
  {
    return dsCodeDbs;
  }

  /**
   * @param dsCodeDbs the dsCodeDbs to set
   */
  public void setDsCodeDbs( String dsCodeDbs )
  {
    this.dsCodeDbs = dsCodeDbs;
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
   * @return the qtCopy
   */
  public Integer getQtCopy()
  {
    return qtCopy;
  }

  /**
   * @param qtCopy the qtCopy to set
   */
  public void setQtCopy( Integer qtCopy )
  {
    this.qtCopy = qtCopy;
  }

  /**
   * @return the categoryDOList
   */
  public List<CategoryDO> getCategoryDOList()
  {
    return categoryDOList;
  }

  /**
   * @param categoryDOList the categoryDOList to set
   */
  public void setCategoryDOList( List<CategoryDO> categoryDOList )
  {
    this.categoryDOList = categoryDOList;
  }

  /**
   * @return the bookingDOList
   */
  public List<BookingDO> getBookingDOList()
  {
    return bookingDOList;
  }

  /**
   * @param bookingDOList the bookingDOList to set
   */
  public void setBookingDOList( List<BookingDO> bookingDOList )
  {
    this.bookingDOList = bookingDOList;
  }

  /**
   * @return the idEventType
   */
  public EventTypeDO getIdEventType()
  {
    return idEventType;
  }

  /**
   * @param idEventType the idEventType to set
   */
  public void setIdEventType( EventTypeDO idEventType )
  {
    this.idEventType = idEventType;
  }

  /**
   * @return the eventMovieDOList
   */
  public List<EventMovieDO> getEventMovieDOList()
  {
    return eventMovieDOList;
  }

  /**
   * @param eventMovieDOList the eventMovieDOList to set
   */
  public void setEventMovieDOList( List<EventMovieDO> eventMovieDOList )
  {
    this.eventMovieDOList = eventMovieDOList;
  }

  /**
   * @return the fgPremiere
   */
  public Boolean getFgPremiere()
  {
    return fgPremiere;
  }

  /**
   * @param fgPremiere the fgPremiere to set
   */
  public void setFgPremiere( Boolean fgPremiere )
  {
    this.fgPremiere = fgPremiere;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEvent != null ? idEvent.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof EventDO) )
    {
      return false;
    }
    EventDO other = (EventDO) object;
    if( (this.idEvent == null && other.idEvent != null)
        || (this.idEvent != null && !this.idEvent.equals( other.idEvent )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idEvent ", this.idEvent ).toString();
  }

  @Override
  public int compareTo( EventDO o )
  {
    return this.idEvent.compareTo( o.idEvent );
  }

  /**
   * @return the currentMovie
   */
  public boolean isCurrentMovie()
  {
    return currentMovie;
  }

  /**
   * @param currentMovie the currentMovie to set
   */
  public void setCurrentMovie( boolean currentMovie )
  {
    this.currentMovie = currentMovie;
  }


  /**
   * @return the fgPrerelease
   */
  public boolean isFgPrerelease()
  {
    return fgPrerelease;
  }

  /**
   * @param fgPrerelease the fgPrerelease to set
   */
  public void setFgPrerelease( boolean fgPrerelease )
  {
    this.fgPrerelease = fgPrerelease;
  }

  /**
   * @return the fgFestival
   */
  public boolean isFgFestival()
  {
    return fgFestival;
  }

  /**
   * @param fgFestival the fgFestival to set
   */
  public void setFgFestival( boolean fgFestival )
  {
    this.fgFestival = fgFestival;
  }

  /**
   * @return the fgActiveIa
   */
  public boolean isFgActiveIa()
  {
    return fgActiveIa;
  }

  /**
   * @param fgActiveIa the fgActiveIa to set
   */
  public void setFgActiveIa( boolean fgActiveIa )
  {
    this.fgActiveIa = fgActiveIa;
  }

  public List<BookingIncomeDO> getBookingIncomeDOList()
  {
    return bookingIncomeDOList;
  }

  public void setBookingIncomeDOList( List<BookingIncomeDO> bookingIncomeDOList )
  {
    this.bookingIncomeDOList = bookingIncomeDOList;
  }

  /**
   * @return the fgAlternateContent
   */
  public boolean isFgAlternateContent()
  {
    return fgAlternateContent;
  }

  /**
   * @param fgAlternateContent the fgAlternateContent to set
   */
  public void setFgAlternateContent( boolean fgAlternateContent )
  {
    this.fgAlternateContent = fgAlternateContent;
  }

}
