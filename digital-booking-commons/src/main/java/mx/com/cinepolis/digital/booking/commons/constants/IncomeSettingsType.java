package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Enumeration for C_INCOME_SETTINGS_TYPE
 * 
 * @author gsegura
 */
public enum IncomeSettingsType
{
  SCREEN_OCCUPANCY(1), CHANGE_PREVIOUS_WEEK(2);

  private int id;

  private IncomeSettingsType( int id )
  {
    this.id = id;
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }

  /**
   * @return the id
   */
  public Long getIdLong()
  {
    return Long.valueOf( id );
  }

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.constants.IncomeSettingsType} from its id
   * 
   * @param id
   * @return
   */
  public IncomeSettingsType fromId( int id )
  {
    IncomeSettingsType type = null;
    for( IncomeSettingsType incomeSettingsType : IncomeSettingsType.values() )
    {
      if( incomeSettingsType.id == id )
      {
        type = incomeSettingsType;
        break;
      }
    }
    return type;
  }

}
