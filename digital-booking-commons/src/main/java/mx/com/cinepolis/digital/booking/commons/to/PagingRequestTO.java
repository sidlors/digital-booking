package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;
import java.util.Map;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;

/**
 * Transfer object for paging queries
 * 
 * @author jchavez
 * @since 0.0.1
 */
public class PagingRequestTO extends AbstractTO
{
  private static final long serialVersionUID = -3781963431110076134L;
  private Integer pageSize;
  private Integer page;
  private List<ModelQuery> sort;
  private SortOrder sortOrder;
  private Map<ModelQuery, Object> filters;
  private Language language = Language.ENGLISH;
  private Boolean needsPaging = true;

  /**
   * @return the pageSize
   */
  public Integer getPageSize()
  {
    return pageSize;
  }

  /**
   * @param pageSize the pageSize to set
   */
  public void setPageSize( Integer pageSize )
  {
    this.pageSize = pageSize;
  }

  /**
   * @return the page
   */
  public Integer getPage()
  {
    return page;
  }

  /**
   * @param page the page to set
   */
  public void setPage( Integer page )
  {
    this.page = page;
  }

  /**
   * @return the sort
   */
  public List<ModelQuery> getSort()
  {
    return sort;
  }

  /**
   * @param sort the sort to set
   */
  public void setSort( List<ModelQuery> sort )
  {
    this.sort = sort;
  }

  /**
   * @return the sortOrder
   */
  public SortOrder getSortOrder()
  {
    return sortOrder;
  }

  /**
   * @param sortOrder the sortOrder to set
   */
  public void setSortOrder( SortOrder sortOrder )
  {
    this.sortOrder = sortOrder;
  }

  /**
   * @return the filters
   */
  public Map<ModelQuery, Object> getFilters()
  {
    return filters;
  }

  /**
   * @param filters the filters to set
   */
  public void setFilters( Map<ModelQuery, Object> filters )
  {
    this.filters = filters;
  }

  /**
   * @return the needsPaging
   */
  public Boolean getNeedsPaging()
  {
    return needsPaging;
  }

  /**
   * @param needsPaging the needsPaging to set
   */
  public void setNeedsPaging( Boolean needsPaging )
  {
    this.needsPaging = needsPaging;
  }

  /**
   * @return the language
   */
  public Language getLanguage()
  {
    return language;
  }

  /**
   * @param language the language to set
   */
  public void setLanguage( Language language )
  {
    this.language = language;
  }

}
