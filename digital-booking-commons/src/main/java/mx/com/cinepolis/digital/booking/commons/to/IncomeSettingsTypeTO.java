package mx.com.cinepolis.digital.booking.commons.to;


/**
 * Transfer object for income settings type data.
 * 
 * @author jreyesv
 */
public class IncomeSettingsTypeTO extends CatalogTO
{

  /**
   * Serialization.
   */
  private static final long serialVersionUID = -8915675250201821059L;

  /**
   * Type of indicator.
   */
  private String indicatorType;

  /**
   * Default constructor.
   */
  public IncomeSettingsTypeTO()
  {
  }

  /**
   * Constructor with id parameter.
   * 
   * @param id with the id of the transfer object.
   */
  public IncomeSettingsTypeTO( Long id )
  {
    this.setId( id );
  }

  /**
   * @return the indicatorType
   */
  public String getIndicatorType()
  {
    return indicatorType;
  }

  /**
   * @param indicatorType the indicatorType to set
   */
  public void setIndicatorType( String indicatorType )
  {
    this.indicatorType = indicatorType;
  }

}