package mx.com.cinepolis.digital.booking.commons.to;

public class TrailerStatusLanguageTO extends CatalogTO implements Comparable<TrailerStatusLanguageTO>
{
  private Integer idTrailerStatusLanguage;
  
  
  
  /**
   * @return the idTrailerStatusLanguage
   */
  public Integer getIdTrailerStatusLanguage()
  {
    return idTrailerStatusLanguage;
  }

  /**
   * @param idTrailerStatusLanguage the idTrailerStatusLanguage to set
   */
  public void setIdTrailerStatusLanguage( Integer idTrailerStatusLanguage )
  {
    this.idTrailerStatusLanguage = idTrailerStatusLanguage;
  }


  @Override
  public int compareTo( TrailerStatusLanguageTO o )
  {
    return this.idTrailerStatusLanguage.compareTo( o.idTrailerStatusLanguage );
  }

}
