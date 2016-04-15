package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * JPA entity for C_BOOKING_STATUS
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_PERSON")
@NamedQueries({
    @NamedQuery(name = "PersonDO.findAll", query = "SELECT p FROM PersonDO p"),
    @NamedQuery(name = "PersonDO.findByIdPerson", query = "SELECT p FROM PersonDO p WHERE p.idPerson = :idPerson"),
    @NamedQuery(name = "PersonDO.findByDsName", query = "SELECT p FROM PersonDO p WHERE p.dsName = :dsName"),
    @NamedQuery(name = "PersonDO.findByDsLastname", query = "SELECT p FROM PersonDO p WHERE p.dsLastname = :dsLastname"),
    @NamedQuery(name = "PersonDO.findByDsMotherLastname", query = "SELECT p FROM PersonDO p WHERE p.dsMotherLastname = :dsMotherLastname"),
    @NamedQuery(name = "PersonDO.findByFgActive", query = "SELECT p FROM PersonDO p WHERE p.fgActive = :fgActive"),
    @NamedQuery(name = "PersonDO.findByDtLastModification", query = "SELECT p FROM PersonDO p WHERE p.dtLastModification = :dtLastModification") })
public class PersonDO extends AbstractSignedEntity<PersonDO>
{
  private static final long serialVersionUID = 5140595903539193018L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_PERSON", nullable = false)
  private Integer idPerson;

  @Column(name = "DS_NAME", nullable = false, length = 50)
  private String dsName;

  @Column(name = "DS_LASTNAME", nullable = false, length = 50)
  private String dsLastname;

  @Column(name = "DS_MOTHER_LASTNAME", length = 50)
  private String dsMotherLastname;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerson", fetch = FetchType.LAZY)
  private List<EmailDO> emailDOList;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "idPerson")
  private UserDO user;

  @ManyToMany(mappedBy = "personDOList", fetch = FetchType.LAZY)
  private List<TheaterDO> theaterDOList;

  @ManyToMany(mappedBy = "personDOList", fetch = FetchType.LAZY)
  private List<RegionDO> regionDOList;

  /**
   * Constructor default
   */
  public PersonDO()
  {
  }

  /**
   * Constructor by idBookingStatus
   * 
   * @param idBookingStatus
   */
  public PersonDO( Integer idPerson )
  {
    this.idPerson = idPerson;
  }

  /**
   * @return the idPerson
   */
  public Integer getIdPerson()
  {
    return idPerson;
  }

  /**
   * @param idPerson the idPerson to set
   */
  public void setIdPerson( Integer idPerson )
  {
    this.idPerson = idPerson;
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
   * @return the dsLastname
   */
  public String getDsLastname()
  {
    return dsLastname;
  }

  /**
   * @param dsLastname the dsLastname to set
   */
  public void setDsLastname( String dsLastname )
  {
    this.dsLastname = dsLastname;
  }

  /**
   * @return the dsMotherLastname
   */
  public String getDsMotherLastname()
  {
    return dsMotherLastname;
  }

  /**
   * @param dsMotherLastname the dsMotherLastname to set
   */
  public void setDsMotherLastname( String dsMotherLastname )
  {
    this.dsMotherLastname = dsMotherLastname;
  }

  /**
   * @return the emailDOList
   */
  public List<EmailDO> getEmailDOList()
  {
    return emailDOList;
  }

  /**
   * @param emailDOList the emailDOList to set
   */
  public void setEmailDOList( List<EmailDO> emailDOList )
  {
    this.emailDOList = emailDOList;
  }

  /**
   * @return the theaterDOList
   */
  public List<TheaterDO> getTheaterDOList()
  {
    return theaterDOList;
  }

  /**
   * @param theaterDOList the theaterDOList to set
   */
  public void setTheaterDOList( List<TheaterDO> theaterDOList )
  {
    this.theaterDOList = theaterDOList;
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
    hash += (idPerson != null ? idPerson.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof PersonDO) )
    {
      return false;
    }
    PersonDO other = (PersonDO) object;
    if( (this.idPerson == null && other.idPerson != null)
        || (this.idPerson != null && !this.idPerson.equals( other.idPerson )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.PersonDO[ idPerson=" + idPerson + " ]";
  }

  @Override
  public int compareTo( PersonDO other )
  {
    return this.idPerson.compareTo( other.idPerson );
  }

  /**
   * @return the user
   */
  public UserDO getUser()
  {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser( UserDO user )
  {
    this.user = user;
  }

}
