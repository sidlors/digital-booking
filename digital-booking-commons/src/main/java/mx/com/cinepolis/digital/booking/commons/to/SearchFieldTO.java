package mx.com.cinepolis.digital.booking.commons.to;




/**
 * Clase del Data Transfer Object para transportar la información de un parámetro de búsqueda.
 * 
 * @author jnargota
 */

public class SearchFieldTO extends AbstractTO
{
  private static final long serialVersionUID = -8680681009775846177L;
  private String searchField;
  private String searchValue1;
  private String searchValue2;
  
  /**
   * @return the searchFiel
   */
  public String getSearchField()
  {
    return searchField;
  }
  /**
   * @param searchField the searchFiel to set
   */
  public void setSearchField( String searchField )
  {
    this.searchField = searchField;
  }
  /**
   * @return the searchValue1
   */
  public String getSearchValue1()
  {
    return searchValue1;
  }
  /**
   * @param searchValue1 the searchValue1 to set
   */
  public void setSearchValue1( String searchValue1 )
  {
    this.searchValue1 = searchValue1;
  }
  /**
   * @return the searchValue2
   */
  public String getSearchValue2()
  {
    return searchValue2;
  }
  /**
   * @param searchValue2 the searchValue2 to set
   */
  public void setSearchValue2( String searchValue2 )
  {
    this.searchValue2 = searchValue2;
  }

}
