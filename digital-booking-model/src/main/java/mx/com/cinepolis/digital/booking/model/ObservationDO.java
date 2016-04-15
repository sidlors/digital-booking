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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for K_OBSERVATION
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_OBSERVATION")
@NamedQueries({
    @NamedQuery(name = "ObservationDO.findAll", query = "SELECT o FROM ObservationDO o"),
    @NamedQuery(name = "ObservationDO.findByIdObservation", query = "SELECT o FROM ObservationDO o WHERE o.idObservation = :idObservation"),
    @NamedQuery(name = "ObservationDO.findByDsObservation", query = "SELECT o FROM ObservationDO o WHERE o.dsObservation = :dsObservation"),
    @NamedQuery(name = "ObservationDO.findByFgActive", query = "SELECT o FROM ObservationDO o WHERE o.fgActive = :fgActive"),
    @NamedQuery(name = "ObservationDO.findByDtLastModification", query = "SELECT o FROM ObservationDO o WHERE o.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "ObservationDO.findByIdLastUserModifier", query = "SELECT o FROM ObservationDO o WHERE o.idLastUserModifier = :idLastUserModifier") })
public class ObservationDO extends AbstractSignedEntity<ObservationDO>
{
  private static final long serialVersionUID = -8976969860240142919L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_OBSERVATION", nullable = false)
  private Long idObservation;

  @Column(name = "DS_OBSERVATION", nullable = false, length = 2147483647)
  private String dsObservation;

  @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserDO idUser;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idObservation", fetch = FetchType.LAZY)
  private List<NewsFeedDO> newsFeedDOList;

  @OneToMany(mappedBy = "idObservation", fetch = FetchType.LAZY)
  private List<BookingWeekScreenDO> bookingWeekScreenDOList;
  
  @OneToMany(mappedBy = "idObservation", fetch = FetchType.LAZY)
  private List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList;

  /**
   * Constructor default
   */
  public ObservationDO()
  {
  }

  /**
   * Constructor by idObservation
   * 
   * @param idObservation
   */
  public ObservationDO( Long idObservation )
  {
    this.idObservation = idObservation;
  }

  /**
   * @return the idObservation
   */
  public Long getIdObservation()
  {
    return idObservation;
  }

  /**
   * @param idObservation the idObservation to set
   */
  public void setIdObservation( Long idObservation )
  {
    this.idObservation = idObservation;
  }

  /**
   * @return the dsObservation
   */
  public String getDsObservation()
  {
    return dsObservation;
  }

  /**
   * @param dsObservation the dsObservation to set
   */
  public void setDsObservation( String dsObservation )
  {
    this.dsObservation = dsObservation;
  }

  /**
   * @return the idUser
   */
  public UserDO getIdUser()
  {
    return idUser;
  }

  /**
   * @param idUser the idUser to set
   */
  public void setIdUser( UserDO idUser )
  {
    this.idUser = idUser;
  }

  /**
   * @return the newsFeedDOList
   */
  public List<NewsFeedDO> getNewsFeedDOList()
  {
    return newsFeedDOList;
  }

  /**
   * @param newsFeedDOList the newsFeedDOList to set
   */
  public void setNewsFeedDOList( List<NewsFeedDO> newsFeedDOList )
  {
    this.newsFeedDOList = newsFeedDOList;
  }

  /**
   * @return the bookingWeekScreenDOList
   */
  public List<BookingWeekScreenDO> getBookingWeekScreenDOList()
  {
    return bookingWeekScreenDOList;
  }

  /**
   * @param bookingWeekScreenDOList the bookingWeekScreenDOList to set
   */
  public void setBookingWeekScreenDOList( List<BookingWeekScreenDO> bookingWeekScreenDOList )
  {
    this.bookingWeekScreenDOList = bookingWeekScreenDOList;
  }

  /**
   * @return the bookingSpecialEventScreenDOList
   */
  public List<BookingSpecialEventScreenDO> getBookingSpecialEventScreenDOList()
  {
    return bookingSpecialEventScreenDOList;
  }

  /**
   * @param bookingSpecialEventScreenDOList the bookingSpecialEventScreenDOList to set
   */
  public void setBookingSpecialEventScreenDOList( List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList )
  {
    this.bookingSpecialEventScreenDOList = bookingSpecialEventScreenDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idObservation != null ? idObservation.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof ObservationDO) )
    {
      return false;
    }
    ObservationDO other = (ObservationDO) object;
    if( (this.idObservation == null && other.idObservation != null)
        || (this.idObservation != null && !this.idObservation.equals( other.idObservation )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.ObservationDO[ idObservation=" + idObservation + " ]";
  }

  @Override
  public int compareTo( ObservationDO other )
  {
    return this.idObservation.compareTo( other.idObservation );
  }

}
