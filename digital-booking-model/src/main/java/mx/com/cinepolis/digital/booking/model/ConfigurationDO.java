/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA entity for W_CONFIGURATION
 * 
 * @author kperez
 */
@Entity
@Table(name = "W_CONFIGURATION")
@NamedQueries({
    @NamedQuery(name = "ConfigurationDO.findAll", query = "SELECT c FROM ConfigurationDO c"),
    @NamedQuery(name = "ConfigurationDO.findByIdConfiguration", query = "SELECT c FROM ConfigurationDO c WHERE c.idConfiguration = :idConfiguration"),
    @NamedQuery(name = "ConfigurationDO.findByDsParameter", query = "SELECT c FROM ConfigurationDO c WHERE c.dsParameter = :dsParameter"),
    @NamedQuery(name = "ConfigurationDO.findByDsValue", query = "SELECT c FROM ConfigurationDO c WHERE c.dsValue = :dsValue") })
public class ConfigurationDO extends AbstractEntity<ConfigurationDO>
{

  private static final long serialVersionUID = -1087987152673507741L;

  @Id
  @Column(name = "ID_CONFIGURATION", nullable = false)
  private Integer idConfiguration;

  @Column(name = "DS_PARAMETER", nullable = false, length = 160)
  private String dsParameter;

  @Column(name = "DS_VALUE", nullable = false, length = 160)
  private String dsValue;

  /**
   * Constructor default
   */
  public ConfigurationDO()
  {
  }

  /**
   * Constructor by idConfiguration
   * @param idConfiguration
   */
  public ConfigurationDO( Integer idConfiguration )
  {
    this.idConfiguration = idConfiguration;
  }

  /**
   * @return the idConfiguration
   */
  public Integer getIdConfiguration()
  {
    return idConfiguration;
  }

  /**
   * @param idConfiguration the idConfiguration to set
   */
  public void setIdConfiguration( Integer idConfiguration )
  {
    this.idConfiguration = idConfiguration;
  }

  /**
   * @return the dsParameter
   */
  public String getDsParameter()
  {
    return dsParameter;
  }

  /**
   * @param dsParameter the dsParameter to set
   */
  public void setDsParameter( String dsParameter )
  {
    this.dsParameter = dsParameter;
  }

  /**
   * @return the dsValue
   */
  public String getDsValue()
  {
    return dsValue;
  }

  /**
   * @param dsValue the dsValue to set
   */
  public void setDsValue( String dsValue )
  {
    this.dsValue = dsValue;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idConfiguration != null ? idConfiguration.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof ConfigurationDO) )
    {
      return false;
    }
    ConfigurationDO other = (ConfigurationDO) object;
    if( (this.idConfiguration == null && other.idConfiguration != null)
        || (this.idConfiguration != null && !this.idConfiguration.equals( other.idConfiguration )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.ConfigurationDO[ idConfiguration=" + idConfiguration + " ]";
  }

  @Override
  public int compareTo( ConfigurationDO o )
  {
    return this.idConfiguration.compareTo( o.idConfiguration );
  }

}
