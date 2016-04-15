/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.model;

import java.util.List;

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
 * JPA entity for K_EMAIL
 * 
 * @author kperez
 */
@Entity
@Table(name = "K_EMAIL")
@NamedQueries({
    @NamedQuery(name = "EmailDO.findAll", query = "SELECT e FROM EmailDO e"),
    @NamedQuery(name = "EmailDO.findByIdEmail", query = "SELECT e FROM EmailDO e WHERE e.idEmail = :idEmail"),
    @NamedQuery(name = "EmailDO.findByDsEmail", query = "SELECT e FROM EmailDO e WHERE e.dsEmail = :dsEmail"),
    @NamedQuery(name = "EmailDO.findByUserId", query = "SELECT e FROM EmailDO e  WHERE e.idPerson.user.idUser = :userId"),
    @NamedQuery(name = "EmailDO.findByPersonId", query = "SELECT e FROM EmailDO e  WHERE e.idPerson.idPerson = :idPerson") })
public class EmailDO extends AbstractEntity<EmailDO>
{

  private static final long serialVersionUID = -4075408201900818617L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_EMAIL", nullable = false)
  private Integer idEmail;

  @Column(name = "DS_EMAIL", nullable = false, length = 160)
  private String dsEmail;

  @JoinColumn(name = "ID_PERSON", referencedColumnName = "ID_PERSON")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private PersonDO idPerson;

  @OneToMany(mappedBy = "idEmail", fetch = FetchType.LAZY)
  private List<TheaterDO> theaterDOList;

  /**
   * Constructor default
   */
  public EmailDO()
  {
  }

  /**
   * Constructor by idEmail
   * 
   * @param idEmail
   */
  public EmailDO( Integer idEmail )
  {
    this.idEmail = idEmail;
  }

  /**
   * @return the idEmail
   */
  public Integer getIdEmail()
  {
    return idEmail;
  }

  /**
   * @param idEmail the idEmail to set
   */
  public void setIdEmail( Integer idEmail )
  {
    this.idEmail = idEmail;
  }

  /**
   * @return the dsEmail
   */
  public String getDsEmail()
  {
    return dsEmail;
  }

  /**
   * @param dsEmail the dsEmail to set
   */
  public void setDsEmail( String dsEmail )
  {
    this.dsEmail = dsEmail;
  }

  /**
   * @return the idPerson
   */
  public PersonDO getIdPerson()
  {
    return idPerson;
  }

  /**
   * @param idPerson the idPerson to set
   */
  public void setIdPerson( PersonDO idPerson )
  {
    this.idPerson = idPerson;
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

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEmail != null ? idEmail.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof EmailDO) )
    {
      return false;
    }
    EmailDO other = (EmailDO) object;
    if( (this.idEmail == null && other.idEmail != null)
        || (this.idEmail != null && !this.idEmail.equals( other.idEmail )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EmailDO[ idEmail=" + idEmail + " ]";
  }

  @Override
  public int compareTo( EmailDO o )
  {
    return this.idEmail.compareTo( o.idEmail );
  }

}
