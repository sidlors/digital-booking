package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA entity for C_EMAIL_TYPE
 * 
 * @author afuentes
 */
@Entity
@Table(name = "C_EMAIL_TYPE")
public class EmailTypeDO extends AbstractEntity<EmailTypeDO>
{
  private static final long serialVersionUID = 4437305304453409889L;

  @Id
  @Column(name = "ID_EMAIL_TYPE", nullable = false)
  private Integer idEmailType;

  @Column(name = "DS_EMAIL_TYPE")
  private String dsEmailType;

  /**
   * Constructor default
   */
  public EmailTypeDO()
  {
  }

  /**
   * Constructor by idEmailType
   * 
   * @param idEmailType
   */
  public EmailTypeDO( Integer idEmailType )
  {
    this.idEmailType = idEmailType;
  }

  /**
   * @return the idEmailType
   */
  public Integer getIdEmailType()
  {
    return idEmailType;
  }

  /**
   * @param idEmailType the idEmailType to set
   */
  public void setIdEmailType( Integer idEmailType )
  {
    this.idEmailType = idEmailType;
  }

  /**
   * @return the dsEmailType
   */
  public String getDsEmailType()
  {
    return dsEmailType;
  }

  /**
   * @param dsEmailType the dsEmailType to set
   */
  public void setDsEmailType( String dsEmailType )
  {
    this.dsEmailType = dsEmailType;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEmailType != null ? idEmailType.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof EmailTypeDO) )
    {
      return false;
    }
    EmailTypeDO other = (EmailTypeDO) object;
    if( (this.idEmailType == null && other.idEmailType != null)
        || (this.idEmailType != null && !this.idEmailType.equals( other.idEmailType )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EmailTypeDO[ idEmailType=" + idEmailType + " ]";
  }

  @Override
  public int compareTo( EmailTypeDO other )
  {
    return this.idEmailType.compareTo( other.idEmailType );
  }

}
