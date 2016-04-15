package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Language constants
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum Language
{
  ALL(0), ENGLISH(1), SPANISH(2);
  private int id;

  private Language( int id )
  {
    this.id = id;
  }

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.constants.Language} according its id
   * 
   * @param id
   * @return an {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   */
  public static Language fromId( int id )
  {
    Language l = null;
    for( Language languageConstant : Language.values() )
    {
      if( languageConstant.id == id )
      {
        l = languageConstant;
        break;
      }
    }
    return l;
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }

}
