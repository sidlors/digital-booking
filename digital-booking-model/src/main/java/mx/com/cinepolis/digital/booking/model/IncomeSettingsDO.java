package mx.com.cinepolis.digital.booking.model;

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
import javax.persistence.Table;

/**
 * JPA entity for C_INCOME_SETTINGS
 * 
 * @author jreyesv
 * @since 0.0.1
 */
@Entity
@Table(name = "C_INCOME_SETTINGS")
@NamedQueries({
    @NamedQuery(name = "IncomeSettingsDO.findAll", query = "SELECT i FROM IncomeSettingsDO i"),
    @NamedQuery(name = "IncomeSettingsDO.findByIdIncomeSettings", query = "SELECT i FROM IncomeSettingsDO i WHERE i.idIncomeSettings = :idIncomeSettings"),
    @NamedQuery(name = "IncomeSettingsDO.findIncomeSettingsByTheater", query = "SELECT i FROM IncomeSettingsDO i WHERE i.idTheater.idTheater = :idTheater AND i.fgActive = true") })
public class IncomeSettingsDO extends AbstractSignedEntity<IncomeSettingsDO>
{

  private static final long serialVersionUID = -6441034159872913634L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_INCOME_SETTINGS", nullable = false)
  private Integer idIncomeSettings;

  @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TheaterDO idTheater;

  @JoinColumn(name = "ID_INCOME_SETTINGS_TYPE", referencedColumnName = "ID_INCOME_SETTINGS_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private IncomeSettingsTypeDO idIncomeSettingsType;

  @Column(name = "QT_GREEN_SEMAPHORE", precision = 5, scale = 2, nullable = true)
  private Double qtGreenSemaphore;

  @Column(name = "QT_YELLOW_SEMAPHORE", precision = 5, scale = 2, nullable = true)
  private Double qtYellowSemaphore;

  @Column(name = "QT_RED_SEMAPHORE", precision = 5, scale = 2, nullable = true)
  private Double qtRedSemaphore;

  /**
   * Constructor default
   */
  public IncomeSettingsDO()
  {
  }

  /**
   * Constructor by idBookingStatus
   * 
   * @param idBookingStatus
   */
  public IncomeSettingsDO( Integer idIncomeSettings )
  {
    this.idIncomeSettings = idIncomeSettings;
  }

  /**
   * @return the idIncomeSettings
   */
  public Integer getIdIncomeSettings()
  {
    return idIncomeSettings;
  }

  /**
   * @param idIncomeSettings the idIncomeSettings to set
   */
  public void setIdIncomeSettings( Integer idIncomeSettings )
  {
    this.idIncomeSettings = idIncomeSettings;
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

  /**
   * @return the qtGreenSemaphore
   */
  public Double getQtGreenSemaphore()
  {
    return qtGreenSemaphore;
  }

  /**
   * @param qtGreenSemaphore the qtGreenSemaphore to set
   */
  public void setQtGreenSemaphore( Double qtGreenSemaphore )
  {
    this.qtGreenSemaphore = qtGreenSemaphore;
  }

  /**
   * @return the qtYellowSemaphore
   */
  public Double getQtYellowSemaphore()
  {
    return qtYellowSemaphore;
  }

  /**
   * @param qtYellowSemaphore the qtYellowSemaphore to set
   */
  public void setQtYellowSemaphore( Double qtYellowSemaphore )
  {
    this.qtYellowSemaphore = qtYellowSemaphore;
  }

  /**
   * @return the qtRedSemaphore
   */
  public Double getQtRedSemaphore()
  {
    return qtRedSemaphore;
  }

  /**
   * @param qtRedSemaphore the qtRedSemaphore to set
   */
  public void setQtRedSemaphore( Double qtRedSemaphore )
  {
    this.qtRedSemaphore = qtRedSemaphore;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idIncomeSettings != null ? idIncomeSettings.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof IncomeSettingsDO) )
    {
      return false;
    }
    IncomeSettingsDO other = (IncomeSettingsDO) object;
    if( (this.idIncomeSettings == null && other.idIncomeSettings != null)
        || (this.idIncomeSettings != null && !this.idIncomeSettings.equals( other.idIncomeSettings )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.IncomeSettingsDO[ idIncomeSettings=" + idIncomeSettings + " ]";
  }

  @Override
  public int compareTo( IncomeSettingsDO other )
  {
    return this.idIncomeSettings.compareTo( other.idIncomeSettings );
  }

}
