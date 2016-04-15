package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Category type constants
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum CategoryType
{
  SOUND_FORMAT(1), MOVIE_FORMAT(2), SCREEN_FORMAT(3);
  private int id;

  private CategoryType( int id )
  {
    this.id = id;
  }

  /**
   * Obtains a {@link mx.com.cinepolis.digital.booking.commons.constants.CategoryType} from its id
   * 
   * @param id
   * @return
   */
  public static CategoryType fromId( int id )
  {
    CategoryType ct = null;
    for( CategoryType categoryType : CategoryType.values() )
    {
      if( categoryType.id == id )
      {
        ct = categoryType;
        break;
      }
    }
    return ct;
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }
}
