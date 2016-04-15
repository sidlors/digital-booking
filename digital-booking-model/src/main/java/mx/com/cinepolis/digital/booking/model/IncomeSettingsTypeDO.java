package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_INCOME_SETTINGS_TYPE
 * 
 * @author jreyesv
 * @since 0.0.1
 */
@Entity
@Table(name = "C_INCOME_SETTINGS_TYPE")
@NamedQueries({
    @NamedQuery(name = "IncomeSettingsTypeDO.findAll", query = "SELECT i FROM IncomeSettingsTypeDO i"),
    @NamedQuery(name = "IncomeSettingsTypeDO.findByIdIncomeSettingsType", query = "SELECT i FROM IncomeSettingsTypeDO i WHERE i.idIncomeSettingsType = :idIncomeSettingsType") })
public class IncomeSettingsTypeDO extends AbstractEntity<IncomeSettingsTypeDO>
{

  private static final long serialVersionUID = 8480741796644053706L;

  @Id
  @Column(name = "ID_INCOME_SETTINGS_TYPE", nullable = false)
  private Integer idIncomeSettingsType;

  @Column(name = "DS_INDICATOR_TYPE", nullable = false, length = 160)
  private String dsIndicatorType;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idIncomeSettingsType", fetch = FetchType.LAZY)
  private List<IncomeSettingsTypeLanguageDO> incomeSettingsTypeLanguageDOList;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idIncomeSettingsType", fetch = FetchType.LAZY)
  private List<IncomeSettingsDO> incomeSettingsDOList;

  /**
   * Constructor default
   */
  public IncomeSettingsTypeDO()
  {
  }

  /**
   * Constructor by idBookingStatus
   * 
   * @param idBookingStatus
   */
  public IncomeSettingsTypeDO( Integer idIncomeSettingsType )
  {
    this.idIncomeSettingsType = idIncomeSettingsType;
  }

  /**
   * @return the idIncomeSettingsType
   */
  public Integer getIdIncomeSettingsType()
  {
    return idIncomeSettingsType;
  }

  /**
   * @param idIncomeSettingsType the idIncomeSettingsType to set
   */
  public void setIdIncomeSettingsType( Integer idIncomeSettingsType )
  {
    this.idIncomeSettingsType = idIncomeSettingsType;
  }

  /**
   * @return the dsIndicatorType
   */
  public String getDsIndicatorType()
  {
    return dsIndicatorType;
  }

  /**
   * @param dsIndicatorType the dsIndicatorType to set
   */
  public void setDsIndicatorType( String dsIndicatorType )
  {
    this.dsIndicatorType = dsIndicatorType;
  }

  /**
   * @return the incomeSettingsTypeLanguageDOList
   */
  public List<IncomeSettingsTypeLanguageDO> getIncomeSettingsTypeLanguageDOList()
  {
    return incomeSettingsTypeLanguageDOList;
  }

  /**
   * @param incomeSettingsTypeLanguageDOList the incomeSettingsTypeLanguageDOList to set
   */
  public void setIncomeSettingsTypeLanguageDOList( List<IncomeSettingsTypeLanguageDO> incomeSettingsTypeLanguageDOList )
  {
    this.incomeSettingsTypeLanguageDOList = incomeSettingsTypeLanguageDOList;
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
    hash += (idIncomeSettingsType != null ? idIncomeSettingsType.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof IncomeSettingsTypeDO) )
    {
      return false;
    }
    IncomeSettingsTypeDO other = (IncomeSettingsTypeDO) object;
    if( (this.idIncomeSettingsType == null && other.idIncomeSettingsType != null)
        || (this.idIncomeSettingsType != null && !this.idIncomeSettingsType.equals( other.idIncomeSettingsType )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingStatusDO[ idIncomeSettingsType=" + idIncomeSettingsType + " ]";
  }

  @Override
  public int compareTo( IncomeSettingsTypeDO other )
  {
    return this.idIncomeSettingsType.compareTo( other.idIncomeSettingsType );
  }

}
