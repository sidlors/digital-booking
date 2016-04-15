package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA entity for C_EVENT_TYPE_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_EVENT_TYPE_LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "EventTypeLanguageDO.findAll", query = "SELECT e FROM EventTypeLanguageDO e"),
    @NamedQuery(name = "EventTypeLanguageDO.findByIdEventTypeLanguage", query = "SELECT e FROM EventTypeLanguageDO e WHERE e.idEventTypeLanguage = :idEventTypeLanguage"),
    @NamedQuery(name = "EventTypeLanguageDO.findByDsName", query = "SELECT e FROM EventTypeLanguageDO e WHERE e.dsName = :dsName") })
public class EventTypeLanguageDO extends AbstractEntity<EventTypeLanguageDO>
{
  private static final long serialVersionUID = 7874253955136819845L;

  @Id
  @Column(name = "ID_EVENT_TYPE_LANGUAGE", nullable = false)
  private Integer idEventTypeLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_EVENT_TYPE", referencedColumnName = "ID_EVENT_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventTypeDO idEventType;

  /**
   * Constructor default
   */
  public EventTypeLanguageDO()
  {
  }

  /**
   * Constructor by idEventTypeLanguage
   * 
   * @param idEventTypeLanguage
   */
  public EventTypeLanguageDO( Integer idEventTypeLanguage )
  {
    this.idEventTypeLanguage = idEventTypeLanguage;
  }

  /**
   * @return the idEventTypeLanguage
   */
  public Integer getIdEventTypeLanguage()
  {
    return idEventTypeLanguage;
  }

  /**
   * @param idEventTypeLanguage the idEventTypeLanguage to set
   */
  public void setIdEventTypeLanguage( Integer idEventTypeLanguage )
  {
    this.idEventTypeLanguage = idEventTypeLanguage;
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
   * @return the idLanguage
   */
  public LanguageDO getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( LanguageDO idLanguage )
  {
    this.idLanguage = idLanguage;
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

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEventTypeLanguage != null ? idEventTypeLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof EventTypeLanguageDO) )
    {
      return false;
    }
    EventTypeLanguageDO other = (EventTypeLanguageDO) object;
    if( (this.idEventTypeLanguage == null && other.idEventTypeLanguage != null)
        || (this.idEventTypeLanguage != null && !this.idEventTypeLanguage.equals( other.idEventTypeLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EventTypeLanguageDO[ idEventTypeLanguage=" + idEventTypeLanguage
        + " ]";
  }

  @Override
  public int compareTo( EventTypeLanguageDO other )
  {
    return this.idEventTypeLanguage.compareTo( other.idEventTypeLanguage );
  }

}
