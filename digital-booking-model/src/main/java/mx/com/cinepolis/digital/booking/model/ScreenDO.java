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
 * JPA entity for C_SCREEN
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_SCREEN")
@NamedQueries({
    @NamedQuery(name = "ScreenDO.findAll", query = "SELECT s FROM ScreenDO s"),
    @NamedQuery(name = "ScreenDO.findAllActiveByIdCinema", query = "SELECT s FROM ScreenDO s INNER JOIN s.idTheater AS t WHERE t.idTheater = :idTheater AND s.fgActive = true AND t.fgActive = true"),
    @NamedQuery(name = "ScreenDO.findByIdScreen", query = "SELECT s FROM ScreenDO s WHERE s.idScreen = :idScreen"),
    @NamedQuery(name = "ScreenDO.findByIdVista", query = "SELECT s FROM ScreenDO s WHERE s.idVista = :idVista"),
    @NamedQuery(name = "ScreenDO.findByIdVistaAndActive", query = "SELECT s FROM ScreenDO s WHERE s.idVista = :idVista and s.fgActive = true"),
    @NamedQuery(name = "ScreenDO.findByNuScreen", query = "SELECT s FROM ScreenDO s WHERE s.nuScreen = :nuScreen"),
    @NamedQuery(name = "ScreenDO.findByNuCapacity", query = "SELECT s FROM ScreenDO s WHERE s.nuCapacity = :nuCapacity"),
    @NamedQuery(name = "ScreenDO.findByFgActive", query = "SELECT s FROM ScreenDO s WHERE s.fgActive = :fgActive"),
    @NamedQuery(name = "ScreenDO.findByDtLastModification", query = "SELECT s FROM ScreenDO s WHERE s.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "ScreenDO.findByIdLastUserModifier", query = "SELECT s FROM ScreenDO s WHERE s.idLastUserModifier = :idLastUserModifier") })
public class ScreenDO extends AbstractSignedEntity<ScreenDO>
{
  private static final long serialVersionUID = 642383438583746945L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_SCREEN", nullable = false)
  private Integer idScreen;

  @Column(name = "ID_VISTA")
  private String idVista;

  @Column(name = "NU_SCREEN", nullable = false)
  private int nuScreen;

  @Column(name = "NU_CAPACITY", nullable = false)
  private int nuCapacity;

  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idScreen", fetch = FetchType.LAZY)
  private List<BookingWeekScreenDO> bookingWeekScreenDOList;

  @ManyToMany(mappedBy = "screenDOList", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
  private List<CategoryDO> categoryDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idScreen", fetch = FetchType.LAZY)
  private List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList;


  @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TheaterDO idTheater;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idScreen", fetch = FetchType.LAZY)
  private List<BookingIncomeDO> bookingIncomeDOList;

  /**
   * Constructor default
   */
  public ScreenDO()
  {
  }

  /**
   * Constructor by idScreen
   * 
   * @param idScreen
   */
  public ScreenDO( Integer idScreen )
  {
    this.idScreen = idScreen;
  }

  /**
   * @return the idScreen
   */
  public Integer getIdScreen()
  {
    return idScreen;
  }

  /**
   * @param idScreen the idScreen to set
   */
  public void setIdScreen( Integer idScreen )
  {
    this.idScreen = idScreen;
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
   * @return the nuScreen
   */
  public int getNuScreen()
  {
    return nuScreen;
  }

  /**
   * @param nuScreen the nuScreen to set
   */
  public void setNuScreen( int nuScreen )
  {
    this.nuScreen = nuScreen;
  }

  /**
   * @return the nuCapacity
   */
  public int getNuCapacity()
  {
    return nuCapacity;
  }

  /**
   * @param nuCapacity the nuCapacity to set
   */
  public void setNuCapacity( int nuCapacity )
  {
    this.nuCapacity = nuCapacity;
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
   * @return the idTheater
   */
  public TheaterDO getIdTheater()
  {
    return idTheater;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( TheaterDO idTheater )
  {
    this.idTheater = idTheater;
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
    hash += (idScreen != null ? idScreen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof ScreenDO) )
    {
      return false;
    }
    ScreenDO other = (ScreenDO) object;
    if( (this.idScreen == null && other.idScreen != null)
        || (this.idScreen != null && !this.idScreen.equals( other.idScreen )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idScreen", this.idScreen ).append( "nuScreen", this.nuScreen )
        .toString();
  }

  @Override
  public int compareTo( ScreenDO other )
  {
    return this.idScreen.compareTo( other.idScreen );
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
