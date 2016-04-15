package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.CategoryDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum CategoryQuery implements ModelQuery
{
  CATEGORY_ID("idCategory"), CATEGORY_TYPE_ID("idCategoryType"), CATEGORY_LANGUAGE_ID("idLanguage"), CATEGORY_NAME(
      "dsName"), CATEGORY_ACTIVE("fgActive");
  private String query;

  private CategoryQuery( String query )
  {
    this.query = query;
  }

  /**
   * @return the query
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
