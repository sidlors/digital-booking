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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for C_THEATER
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_THEATER")
@NamedQueries({
    @NamedQuery(name = "TheaterDO.findAll", query = "SELECT t FROM TheaterDO t"),
    @NamedQuery(name = "TheaterDO.findByIdTheater", query = "SELECT t FROM TheaterDO t WHERE t.idTheater = :idTheater"),
    @NamedQuery(name = "TheaterDO.findByIdVista", query = "SELECT t FROM TheaterDO t WHERE t.idVista = :idVista"),
    @NamedQuery(name = "TheaterDO.findByIdVistaAndActive", query = "SELECT t FROM TheaterDO t WHERE t.idVista = :idVista and t.fgActive = true"),
    @NamedQuery(name = "TheaterDO.findByRegion", query = "SELECT t.idTheater, tl.dsName, t.nuTheater FROM TheaterDO t INNER JOIN t.idRegion AS r INNER JOIN t.theaterLanguageDOList AS tl INNER JOIN tl.idLanguage AS l WHERE r.idRegion = :idRegion AND l.idLanguage = :idLanguage AND t.fgActive = true ORDER BY tl.dsName, t.idTheater"),
    @NamedQuery(name = "TheaterDO.findByTheaterNameAndLanguage", query = "SELECT t FROM TheaterDO t INNER JOIN t.theaterLanguageDOList AS tl INNER JOIN tl.idLanguage AS l WHERE tl.dsName = :dsName AND l.idLanguage = :idLanguage AND t.fgActive = true"),
    @NamedQuery(name = "TheaterDO.findByDsTelephone", query = "SELECT t FROM TheaterDO t WHERE t.dsTelephone = :dsTelephone"),
    @NamedQuery(name = "TheaterDO.findByFgActive", query = "SELECT t FROM TheaterDO t WHERE t.fgActive = :fgActive"),
    @NamedQuery(name = "TheaterDO.findByDtLastModification", query = "SELECT t FROM TheaterDO t WHERE t.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "TheaterDO.findByIdLastUserModifier", query = "SELECT t FROM TheaterDO t WHERE t.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "TheaterDO.getNumberOfTheatersByRegion", query = "SELECT t FROM TheaterDO t WHERE t.idRegion.idRegion = :idRegion"),
    @NamedQuery(name = "TheaterDO.fidTheatersByRegion", query = "SELECT t FROM TheaterDO t WHERE t.idRegion.idRegion = :idRegion"),
    @NamedQuery(name = "TheaterDO.findByNuTheater", query = "SELECT t FROM TheaterDO t WHERE t.nuTheater = :nuTheater and t.fgActive = true") })
public class TheaterDO extends AbstractSignedEntity<TheaterDO>
{
  private static final long serialVersionUID = 7511426228518149918L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_THEATER")
  private Integer idTheater;

  @Column(name = "ID_VISTA")
  private String idVista;

  @Column(name = "NU_THEATER")
  private int nuTheater;

  @Column(name = "DS_TELEPHONE")
  private String dsTelephone;

  @Column(name = "DS_CC_EMAIL")
  private String dsCCEmail;

  @JoinColumn(name = "ID_STATE", referencedColumnName = "ID_STATE")
  @ManyToOne(fetch = FetchType.LAZY)
  private StateDO idState;

  @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION")
  @ManyToOne(fetch = FetchType.LAZY)
  private RegionDO idRegion;

  @JoinColumn(name = "ID_CITY", referencedColumnName = "ID_CITY")
  @ManyToOne(fetch = FetchType.LAZY)
  private CityDO idCity;

  @JoinColumn(name = "ID_EMAIL", referencedColumnName = "ID_EMAIL")
  @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  private EmailDO idEmail;

  @JoinTable(name = "K_USER_X_THEATER", joinColumns = { @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER", nullable = true) }, inverseJoinColumns = { @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER", nullable = true) })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<UserDO> userDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTheater", fetch = FetchType.LAZY)
  private List<BookingDO> bookingDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTheater", fetch = FetchType.LAZY)
  private List<TheaterLanguageDO> theaterLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTheater", fetch = FetchType.LAZY)
  private List<ScreenDO> screenDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTheater", fetch = FetchType.LAZY)
  private List<BookingIncomeDO> bookingIncomeDOList;


  @JoinTable(name = "K_PERSON_X_THEATER", joinColumns = { @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER") }, inverseJoinColumns = { @JoinColumn(name = "ID_PERSON", referencedColumnName = "ID_PERSON") })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<PersonDO> personDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTheater", fetch = FetchType.LAZY)
  private List<IncomeSettingsDO> incomeSettingsDOList;

  /**
   * Constructor default
   */
  public TheaterDO()
  {
  }

  /**
   * Constructor by idTheater
   * 
   * @param idTheater
   */
  public TheaterDO( Integer idTheater )
  {
    this.idTheater = idTheater;
  }

  /**
   * @return the idTheater
   */
  public Integer getIdTheater()
  {
    return idTheater;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( Integer idTheater )
  {
    this.idTheater = idTheater;
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
   * @return the nuTheater
   */
  public int getNuTheater()
  {
    return nuTheater;
  }

  /**
   * @param nuTheater the nuTheater to set
   */
  public void setNuTheater( int nuTheater )
  {
    this.nuTheater = nuTheater;
  }

  /**
   * @return the dsTelephone
   */
  public String getDsTelephone()
  {
    return dsTelephone;
  }

  /**
   * @param dsTelephone the dsTelephone to set
   */
  public void setDsTelephone( String dsTelephone )
  {
    this.dsTelephone = dsTelephone;
  }

  /**
   * @return the userDOList
   */
  public List<UserDO> getUserDOList()
  {
    return userDOList;
  }

  /**
   * @param userDOList the userDOList to set
   */
  public void setUserDOList( List<UserDO> userDOList )
  {
    this.userDOList = userDOList;
  }

  /**
   * @return the idState
   */
  public StateDO getIdState()
  {
    return idState;
  }

  /**
   * @param idState the idState to set
   */
  public void setIdState( StateDO idState )
  {
    this.idState = idState;
  }

  /**
   * @return the idRegion
   */
  public RegionDO getIdRegion()
  {
    return idRegion;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( RegionDO idRegion )
  {
    this.idRegion = idRegion;
  }

  /**
   * @return the idCity
   */
  public CityDO getIdCity()
  {
    return idCity;
  }

  /**
   * @param idCity the idCity to set
   */
  public void setIdCity( CityDO idCity )
  {
    this.idCity = idCity;
  }

  /**
   * @return the idEmail
   */
  public EmailDO getIdEmail()
  {
    return idEmail;
  }

  /**
   * @param idEmail the idEmail to set
   */
  public void setIdEmail( EmailDO idEmail )
  {
    this.idEmail = idEmail;
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
   * @return the theaterLanguageDOList
   */
  public List<TheaterLanguageDO> getTheaterLanguageDOList()
  {
    return theaterLanguageDOList;
  }

  /**
   * @param theaterLanguageDOList the theaterLanguageDOList to set
   */
  public void setTheaterLanguageDOList( List<TheaterLanguageDO> theaterLanguageDOList )
  {
    this.theaterLanguageDOList = theaterLanguageDOList;
  }

  /**
   * @return the screenDOList
   */
  public List<ScreenDO> getScreenDOList()
  {
    return screenDOList;
  }

  /**
   * @param screenDOList the screenDOList to set
   */
  public void setScreenDOList( List<ScreenDO> screenDOList )
  {
    this.screenDOList = screenDOList;
  }

  /**
   * @return the personDOList
   */
  public List<PersonDO> getPersonDOList()
  {
    return personDOList;
  }

  /**
   * @param personDOList the personDOList to set
   */
  public void setPersonDOList( List<PersonDO> personDOList )
  {
    this.personDOList = personDOList;
  }

  /**
   * @return the dsCCEmail
   */
  public String getDsCCEmail()
  {
    return dsCCEmail;
  }

  /**
   * @param dsCCEmail the dsCCEmail to set
   */
  public void setDsCCEmail( String dsCCEmail )
  {
    this.dsCCEmail = dsCCEmail;
  }

  /**
   * @return the incomeSettingsDOList
   */
  public List<IncomeSettingsDO> getIncomeSettingsDOList()
  {
    return incomeSettingsDOList;
  }

  /**
   * @param incomeSettingsDOList the incomeSettingsDOList to set
   */
  public void setIncomeSettingsDOList( List<IncomeSettingsDO> incomeSettingsDOList )
  {
    this.incomeSettingsDOList = incomeSettingsDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTheater != null ? idTheater.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof TheaterDO) )
    {
      return false;
    }
    TheaterDO other = (TheaterDO) object;
    if( (this.idTheater == null && other.idTheater != null)
        || (this.idTheater != null && !this.idTheater.equals( other.idTheater )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idTheater", this.idTheater ).toString();
  }

  @Override
  public int compareTo( TheaterDO other )
  {
    return this.idTheater.compareTo( other.idTheater );
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
