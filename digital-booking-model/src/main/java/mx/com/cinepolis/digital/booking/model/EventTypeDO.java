package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_EVENT_TYPE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_EVENT_TYPE")
@NamedQueries({
    @NamedQuery(name = "EventTypeDO.findAll", query = "SELECT e FROM EventTypeDO e"),
    @NamedQuery(name = "EventTypeDO.findByIdEventType", query = "SELECT e FROM EventTypeDO e WHERE e.idEventType = :idEventType") })
public class EventTypeDO extends AbstractEntity<EventTypeDO>
{

  private static final long serialVersionUID = -9005400296633705991L;
  @Id
  @Column(name = "ID_EVENT_TYPE", nullable = false)
  private Integer idEventType;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEventType", fetch = FetchType.LAZY)
  private List<EventDO> eventDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEventType", fetch = FetchType.LAZY)
  private List<EventTypeLanguageDO> eventTypeLanguageDOList;

  /**
   * Constructor default
   */
  public EventTypeDO()
  {
  }

  /**
   * Constructor by idEventType
   * 
   * @param idEventType
   */
  public EventTypeDO( Integer idEventType )
  {
    this.idEventType = idEventType;
  }

  /**
   * @return the idEventType
   */
  public Integer getIdEventType()
  {
    return idEventType;
  }

  /**
   * @param idEventType the idEventType to set
   */
  public void setIdEventType( Integer idEventType )
  {
    this.idEventType = idEventType;
  }

  /**
   * @return the eventDOList
   */
  public List<EventDO> getEventDOList()
  {
    return eventDOList;
  }

  /**
   * @param eventDOList the eventDOList to set
   */
  public void setEventDOList( List<EventDO> eventDOList )
  {
    this.eventDOList = eventDOList;
  }

  /**
   * @return the eventTypeLanguageDOList
   */
  public List<EventTypeLanguageDO> getEventTypeLanguageDOList()
  {
    return eventTypeLanguageDOList;
  }

  /**
   * @param eventTypeLanguageDOList the eventTypeLanguageDOList to set
   */
  public void setEventTypeLanguageDOList( List<EventTypeLanguageDO> eventTypeLanguageDOList )
  {
    this.eventTypeLanguageDOList = eventTypeLanguageDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEventType != null ? idEventType.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof EventTypeDO) )
    {
      return false;
    }
    EventTypeDO other = (EventTypeDO) object;
    if( (this.idEventType == null && other.idEventType != null)
        || (this.idEventType != null && !this.idEventType.equals( other.idEventType )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EventTypeDO[ idEventType=" + idEventType + " ]";
  }

  @Override
  public int compareTo( EventTypeDO other )
  {
    return this.idEventType.compareTo( other.idEventType );
  }

}
