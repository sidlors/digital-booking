package mx.com.cinepolis.digital.booking.commons.constants;

import org.apache.commons.collections.map.MultiKeyMap;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;

/**
 * @author gsegura
 */
public enum BookingShow
{

  FIRST(new CatalogTO( 1L, "1st" )), SECOND(new CatalogTO( 2L, "2nd" )), THIRD(new CatalogTO( 3L, "3rd" )), FOURTH(
      new CatalogTO( 4L, "4th" )), FIVETH(new CatalogTO( 5L, "5th" )), SIXTH(new CatalogTO( 6L, "6th" ));

  private CatalogTO show;
  private static MultiKeyMap languageMap;

  static
  {
    languageMap = new MultiKeyMap();
    languageMap.put( Language.ENGLISH, 1L, "1st" );
    languageMap.put( Language.ENGLISH, 2L, "2nd" );
    languageMap.put( Language.ENGLISH, 3L, "3rd" );
    languageMap.put( Language.ENGLISH, 4L, "4th" );
    languageMap.put( Language.ENGLISH, 5L, "5th" );
    languageMap.put( Language.ENGLISH, 6L, "6th" );
    languageMap.put( Language.SPANISH, 1L, "1a" );
    languageMap.put( Language.SPANISH, 2L, "2a" );
    languageMap.put( Language.SPANISH, 3L, "3a" );
    languageMap.put( Language.SPANISH, 4L, "4a" );
    languageMap.put( Language.SPANISH, 5L, "5a" );
    languageMap.put( Language.SPANISH, 6L, "6a" );

  }

  private BookingShow( CatalogTO show )
  {
    this.show = show;
  }

  /**
   * @return the show
   */
  public CatalogTO getShow()
  {
    return show;
  }

  public CatalogTO getShow( Language language )
  {
    CatalogTO c = new CatalogTO();
    c.setId( this.show.getId() );
    if( languageMap.containsKey( language, c.getId() ) )
    {
      c.setName( (String) languageMap.get( language, c.getId() ) );
    }
    else
    {
      c.setName( this.show.getName() );
    }
    return c;
  }

  public Long getShowNumber()
  {
    return this.show.getId();
  }

  public String getCardinalName()
  {
    return this.show.getName();
  }

  public static BookingShow fromId( Long id )
  {
    BookingShow bookingShow = null;
    for( BookingShow b : BookingShow.values() )
    {
      if( id.equals( b.show.getId() ) )
      {
        bookingShow = b;
        break;
      }
    }
    return bookingShow;
  }
}
