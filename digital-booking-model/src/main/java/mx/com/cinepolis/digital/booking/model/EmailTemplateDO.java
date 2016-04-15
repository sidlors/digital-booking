package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPA entity for K_EMAIL_TEMPLATE
 * 
 * @author afuentes
 */
@Entity
@Table(name = "K_EMAIL_TEMPLATE")
public class EmailTemplateDO extends AbstractEntity<EmailTemplateDO>
{

  private static final long serialVersionUID = 4062716791292630978L;

  @Id
  @Column(name = "ID_EMAIL_TEMPLATE", nullable = false)
  private Integer idEmailTemplate;

  @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private RegionDO regionDO;

  @JoinColumn(name = "ID_EMAIL_TYPE", referencedColumnName = "ID_EMAIL_TYPE")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EmailTypeDO emailTypeDO;

  @Column(name = "DS_SUBJECT")
  private String dsSubject;

  @Column(name = "DS_BODY")
  private String dsBody;

  /**
   * Constructor default
   */
  public EmailTemplateDO()
  {
  }

  /**
   * Constructor by idEmailTemplate
   * 
   * @param idEmailTemplate
   */
  public EmailTemplateDO( Integer idEmailTemplate )
  {
    this.idEmailTemplate = idEmailTemplate;
  }

  /**
   * @return the idEmailTemplate
   */
  public Integer getIdEmailTemplate()
  {
    return idEmailTemplate;
  }

  /**
   * @param idEmailTemplate the idEmailTemplate to set
   */
  public void setIdEmailTemplate( Integer idEmailTemplate )
  {
    this.idEmailTemplate = idEmailTemplate;
  }

  /**
   * @return the regionDO
   */
  public RegionDO getRegionDO()
  {
    return regionDO;
  }

  /**
   * @param regionDO the regionDO to set
   */
  public void setRegionDO( RegionDO regionDO )
  {
    this.regionDO = regionDO;
  }

  /**
   * @return the emailTypeDO
   */
  public EmailTypeDO getEmailTypeDO()
  {
    return emailTypeDO;
  }

  /**
   * @param emailTypeDO the emailTypeDO to set
   */
  public void setEmailTypeDO( EmailTypeDO emailTypeDO )
  {
    this.emailTypeDO = emailTypeDO;
  }

  /**
   * @return the dsSubject
   */
  public String getDsSubject()
  {
    return dsSubject;
  }

  /**
   * @param dsSubject the dsSubject to set
   */
  public void setDsSubject( String dsSubject )
  {
    this.dsSubject = dsSubject;
  }

  /**
   * @return the dsBody
   */
  public String getDsBody()
  {
    return dsBody;
  }

  /**
   * @param dsBody the dsBody to set
   */
  public void setDsBody( String dsBody )
  {
    this.dsBody = dsBody;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idEmailTemplate != null ? idEmailTemplate.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof EmailTemplateDO) )
    {
      return false;
    }
    EmailTemplateDO other = (EmailTemplateDO) object;
    if( (this.idEmailTemplate == null && other.idEmailTemplate != null)
        || (this.idEmailTemplate != null && !this.idEmailTemplate.equals( other.idEmailTemplate )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.EmailTemplateDO[ idEmailTemplate=" + idEmailTemplate + " ]";
  }

  @Override
  public int compareTo( EmailTemplateDO other )
  {
    return this.idEmailTemplate.compareTo( other.idEmailTemplate );
  }

}
