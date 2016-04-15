package mx.com.cinepolis.digital.booking.commons.to;

/**
 * Transfer object for income settings data.
 * 
 * @author jreyesv
 */
public class IncomeSettingsTO extends CatalogTO
{

  /**
   * Serialization.
   */
  private static final long serialVersionUID = -3279690493779902950L;

  /**
   * The theater id.
   */
  private Long idTheater;

  /**
   * Income setting type.
   */
  private IncomeSettingsTypeTO incomeSettingsType;

  /**
   * Green semaphore setting.
   */
  private Double greenSemaphore;

  /**
   * Yellow semaphore setting.
   */
  private Double yellowSemaphore;

  /**
   * Red semaphore setting.
   */
  private Double redSemaphore;

  /**
   * @return the idTheater
   */
  public Long getIdTheater()
  {
    return idTheater;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( Long idTheater )
  {
    this.idTheater = idTheater;
  }

  /**
   * @return the incomeSettingsType
   */
  public IncomeSettingsTypeTO getIncomeSettingsType()
  {
    return incomeSettingsType;
  }

  /**
   * @param incomeSettingsType the incomeSettingsType to set
   */
  public void setIncomeSettingsType( IncomeSettingsTypeTO incomeSettingsType )
  {
    this.incomeSettingsType = incomeSettingsType;
  }

  /**
   * @return the greenSemaphore
   */
  public Double getGreenSemaphore()
  {
    return greenSemaphore;
  }

  /**
   * @param greenSemaphore the greenSemaphore to set
   */
  public void setGreenSemaphore( Double greenSemaphore )
  {
    this.greenSemaphore = greenSemaphore;
  }

  /**
   * @return the yellowSemaphore
   */
  public Double getYellowSemaphore()
  {
    return yellowSemaphore;
  }

  /**
   * @param yellowSemaphore the yellowSemaphore to set
   */
  public void setYellowSemaphore( Double yellowSemaphore )
  {
    this.yellowSemaphore = yellowSemaphore;
  }

  /**
   * @return the redSemaphore
   */
  public Double getRedSemaphore()
  {
    return redSemaphore;
  }

  /**
   * @param redSemaphore the redSemaphore to set
   */
  public void setRedSemaphore( Double redSemaphore )
  {
    this.redSemaphore = redSemaphore;
  }

}
