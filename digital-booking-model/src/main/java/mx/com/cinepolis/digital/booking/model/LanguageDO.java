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
 * JPA entity for W_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "W_LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "LanguageDO.findAll", query = "SELECT l FROM LanguageDO l"),
    @NamedQuery(name = "LanguageDO.findByIdLanguage", query = "SELECT l FROM LanguageDO l WHERE l.idLanguage = :idLanguage"),
    @NamedQuery(name = "LanguageDO.findByDsCode", query = "SELECT l FROM LanguageDO l WHERE l.dsCode = :dsCode"),
    @NamedQuery(name = "LanguageDO.findByDsCountryCode", query = "SELECT l FROM LanguageDO l WHERE l.dsCountryCode = :dsCountryCode"),
    @NamedQuery(name = "LanguageDO.findByDsLanguageCode", query = "SELECT l FROM LanguageDO l WHERE l.dsLanguageCode = :dsLanguageCode") })
public class LanguageDO extends AbstractEntity<LanguageDO>
{
  private static final long serialVersionUID = -5578562514371067898L;

  @Id
  @Column(name = "ID_LANGUAGE", nullable = false)
  private Integer idLanguage;

  @Column(name = "DS_CODE", length = 10)
  private String dsCode;

  @Column(name = "DS_COUNTRY_CODE", length = 4)
  private String dsCountryCode;

  @Column(name = "DS_LANGUAGE_CODE", nullable = false, length = 4)
  private String dsLanguageCode;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<CityLanguageDO> cityLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<TerritoryLanguageDO> territoryLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<TheaterLanguageDO> theaterLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<EventTypeLanguageDO> eventTypeLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<RegionLanguageDO> regionLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<BookingStatusLanguageDO> bookingStatusLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<CountryLanguageDO> countryLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<StateLanguageDO> stateLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<CategoryTypeLanguageDO> categoryTypeLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<CategoryLanguageDO> categoryLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<IncomeSettingsTypeLanguageDO> incomeSettingsTypeLanguageDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<TrailerStatusLanguageDO> trailerStatusLanguageDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<OperationLanguageDO> operationLanguageDO;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLanguage", fetch = FetchType.LAZY)
  private List<ProcessLanguageDO> processLanguageDOList; 
  /**
   * Constructor default
   */
  public LanguageDO()
  {
  }

  /**
   * Constructor by idLanguage
   * 
   * @param idLanguage
   */
  public LanguageDO( Integer idLanguage )
  {
    this.idLanguage = idLanguage;
  }

  /**
   * @return the idLanguage
   */
  public Integer getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( Integer idLanguage )
  {
    this.idLanguage = idLanguage;
  }

  /**
   * @return the dsCode
   */
  public String getDsCode()
  {
    return dsCode;
  }

  /**
   * @param dsCode the dsCode to set
   */
  public void setDsCode( String dsCode )
  {
    this.dsCode = dsCode;
  }

  /**
   * @return the dsCountryCode
   */
  public String getDsCountryCode()
  {
    return dsCountryCode;
  }

  /**
   * @param dsCountryCode the dsCountryCode to set
   */
  public void setDsCountryCode( String dsCountryCode )
  {
    this.dsCountryCode = dsCountryCode;
  }

  /**
   * @return the dsLanguageCode
   */
  public String getDsLanguageCode()
  {
    return dsLanguageCode;
  }

  /**
   * @param dsLanguageCode the dsLanguageCode to set
   */
  public void setDsLanguageCode( String dsLanguageCode )
  {
    this.dsLanguageCode = dsLanguageCode;
  }

  /**
   * @return the cityLanguageDOList
   */
  public List<CityLanguageDO> getCityLanguageDOList()
  {
    return cityLanguageDOList;
  }

  /**
   * @param cityLanguageDOList the cityLanguageDOList to set
   */
  public void setCityLanguageDOList( List<CityLanguageDO> cityLanguageDOList )
  {
    this.cityLanguageDOList = cityLanguageDOList;
  }

  /**
   * @return the territoryLanguageDOList
   */
  public List<TerritoryLanguageDO> getTerritoryLanguageDOList()
  {
    return territoryLanguageDOList;
  }

  /**
   * @param territoryLanguageDOList the territoryLanguageDOList to set
   */
  public void setTerritoryLanguageDOList( List<TerritoryLanguageDO> territoryLanguageDOList )
  {
    this.territoryLanguageDOList = territoryLanguageDOList;
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
   * @return the eventTypeLanguageDOList
   */
  public List<EventTypeLanguageDO> getEventTypeLanguageDOList()
  {
    return eventTypeLanguageDOList;
  }

  /**
   * @param eventTypeLanguageDOList the eventTypeLanguageDOList to set
   */
  public void setEventTypeLanguageDOList( List<EventTypeLanguageDO> eventTypeLanguageDOList )
  {
    this.eventTypeLanguageDOList = eventTypeLanguageDOList;
  }

  /**
   * @return the regionLanguageDOList
   */
  public List<RegionLanguageDO> getRegionLanguageDOList()
  {
    return regionLanguageDOList;
  }

  /**
   * @param regionLanguageDOList the regionLanguageDOList to set
   */
  public void setRegionLanguageDOList( List<RegionLanguageDO> regionLanguageDOList )
  {
    this.regionLanguageDOList = regionLanguageDOList;
  }

  /**
   * @return the bookingStatusLanguageDOList
   */
  public List<BookingStatusLanguageDO> getBookingStatusLanguageDOList()
  {
    return bookingStatusLanguageDOList;
  }

  /**
   * @param bookingStatusLanguageDOList the bookingStatusLanguageDOList to set
   */
  public void setBookingStatusLanguageDOList( List<BookingStatusLanguageDO> bookingStatusLanguageDOList )
  {
    this.bookingStatusLanguageDOList = bookingStatusLanguageDOList;
  }

  /**
   * @return the countryLanguageDOList
   */
  public List<CountryLanguageDO> getCountryLanguageDOList()
  {
    return countryLanguageDOList;
  }

  /**
   * @param countryLanguageDOList the countryLanguageDOList to set
   */
  public void setCountryLanguageDOList( List<CountryLanguageDO> countryLanguageDOList )
  {
    this.countryLanguageDOList = countryLanguageDOList;
  }

  /**
   * @return the stateLanguageDOList
   */
  public List<StateLanguageDO> getStateLanguageDOList()
  {
    return stateLanguageDOList;
  }

  /**
   * @param stateLanguageDOList the stateLanguageDOList to set
   */
  public void setStateLanguageDOList( List<StateLanguageDO> stateLanguageDOList )
  {
    this.stateLanguageDOList = stateLanguageDOList;
  }

  /**
   * @return the categoryTypeLanguageDOList
   */
  public List<CategoryTypeLanguageDO> getCategoryTypeLanguageDOList()
  {
    return categoryTypeLanguageDOList;
  }

  /**
   * @param categoryTypeLanguageDOList the categoryTypeLanguageDOList to set
   */
  public void setCategoryTypeLanguageDOList( List<CategoryTypeLanguageDO> categoryTypeLanguageDOList )
  {
    this.categoryTypeLanguageDOList = categoryTypeLanguageDOList;
  }

  /**
   * @return the categoryLanguageDOList
   */
  public List<CategoryLanguageDO> getCategoryLanguageDOList()
  {
    return categoryLanguageDOList;
  }

  /**
   * @param categoryLanguageDOList the categoryLanguageDOList to set
   */
  public void setCategoryLanguageDOList( List<CategoryLanguageDO> categoryLanguageDOList )
  {
    this.categoryLanguageDOList = categoryLanguageDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idLanguage != null ? idLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof LanguageDO) )
    {
      return false;
    }
    LanguageDO other = (LanguageDO) object;
    if( (this.idLanguage == null && other.idLanguage != null)
        || (this.idLanguage != null && !this.idLanguage.equals( other.idLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.LanguageDO[ idLanguage=" + idLanguage + " ]";
  }

  @Override
  public int compareTo( LanguageDO other )
  {
    return this.idLanguage.compareTo( other.idLanguage );
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
   * @return the trailerStatusLanguageDOList
   */
  public List<TrailerStatusLanguageDO> getTrailerStatusLanguageDOList()
  {
    return trailerStatusLanguageDOList;
  }

  /**
   * @param trailerStatusLanguageDOList the trailerStatusLanguageDOList to set
   */
  public void setTrailerStatusLanguageDOList( List<TrailerStatusLanguageDO> trailerStatusLanguageDOList )
  {
    this.trailerStatusLanguageDOList = trailerStatusLanguageDOList;
  }

  /**
   * @return the operationLanguageDO
   */
  public List<OperationLanguageDO> getOperationLanguageDO()
  {
    return operationLanguageDO;
  }

  /**
   * @param operationLanguageDO the operationLanguageDO to set
   */
  public void setOperationLanguageDO( List<OperationLanguageDO> operationLanguageDO )
  {
    this.operationLanguageDO = operationLanguageDO;
  }

  /**
   * @return the processLanguageDOList
   */
  public List<ProcessLanguageDO> getProcessLanguageDOList()
  {
    return processLanguageDOList;
  }

  /**
   * @param processLanguageDOList the processLanguageDOList to set
   */
  public void setProcessLanguageDOList( List<ProcessLanguageDO> processLanguageDOList )
  {
    this.processLanguageDOList = processLanguageDOList;
  }

}
