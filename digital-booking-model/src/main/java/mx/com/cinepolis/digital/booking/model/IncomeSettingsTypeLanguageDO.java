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
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_INCOME_SETTINGS_TYPE_LANGUAGE
 * 
 * @author jreyesv
 * @since 0.0.1
 */
@Entity
@Table(name = "C_INCOME_SETTINGS_TYPE_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_INCOME_SETTINGS_TYPE",
"ID_LANGUAGE" }) })
@NamedQueries({
  @NamedQuery(name = "IncomeSettingsTypeLanguageDO.findAll", query = "SELECT i FROM IncomeSettingsTypeLanguageDO i"),
  @NamedQuery(name = "IncomeSettingsTypeLanguageDO.findByIdIncomeSettingsTypeLanguage", query = "SELECT i FROM IncomeSettingsTypeLanguageDO i WHERE i.idIncomeSettingsTypeLanguage = :idIncomeSettingsTypeLanguage"),
  @NamedQuery(name = "IncomeSettingsTypeLanguageDO.findByDsName", query = "SELECT i FROM IncomeSettingsTypeLanguageDO i WHERE i.dsName = :dsName") })
public class IncomeSettingsTypeLanguageDO extends AbstractEntity<IncomeSettingsTypeLanguageDO>
{

  static final long serialVersionUID = -4211771932597980744L;

  @Id
  @Column(name = "ID_INCOME_SETTINGS_TYPE_LANGUAGE", nullable = false)
  private Integer idIncomeSettingsTypeLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_INCOME_SETTINGS_TYPE", referencedColumnName = "ID_INCOME_SETTINGS_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private IncomeSettingsTypeDO idIncomeSettingsType;

  /**
   * Constructor por default
   */
  public IncomeSettingsTypeLanguageDO()
  {
  }

  /**
   * @return the idIncomeSettingsTypeLanguage
   */
  public Integer getIdIncomeSettingsTypeLanguage()
  {
    return idIncomeSettingsTypeLanguage;
  }

  /**
   * @param idIncomeSettingsTypeLanguage the idIncomeSettingsTypeLanguage to set
   */
  public void setIdIncomeSettingsTypeLanguage( Integer idIncomeSettingsTypeLanguage )
  {
    this.idIncomeSettingsTypeLanguage = idIncomeSettingsTypeLanguage;
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
   * @return the idIncomeSettingsType
   */
  public IncomeSettingsTypeDO getIdIncomeSettingsType()
  {
    return idIncomeSettingsType;
  }

  /**
   * @param idIncomeSettingsType the idIncomeSettingsType to set
   */
  public void setIdIncomeSettingsType( IncomeSettingsTypeDO idIncomeSettingsType )
  {
    this.idIncomeSettingsType = idIncomeSettingsType;
  }

  @Override
  public int compareTo( IncomeSettingsTypeLanguageDO other )
  {
    return this.idIncomeSettingsTypeLanguage.compareTo( other.idIncomeSettingsTypeLanguage );
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof IncomeSettingsTypeLanguageDO) )
    {
      return false;
    }
    IncomeSettingsTypeLanguageDO other = (IncomeSettingsTypeLanguageDO) object;
    if( (this.idIncomeSettingsTypeLanguage == null && other.idIncomeSettingsTypeLanguage != null)
        || (this.idIncomeSettingsTypeLanguage != null && !this.idIncomeSettingsTypeLanguage
            .equals( other.idIncomeSettingsTypeLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idIncomeSettingsTypeLanguage != null ? idIncomeSettingsTypeLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeLanguageDO[ idIncomeSettingsTypeLanguage="
        + idIncomeSettingsTypeLanguage + " ]";
  }

}
