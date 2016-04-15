package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;
import java.util.List;

/**
 * Transfer object for paging queries
 * 
 * @author gsegura
 * @param <T>
 */
public class PagingResponseTO<T extends Serializable> implements Serializable
{

  private static final long serialVersionUID = -8867284539744137372L;
  private int totalCount;
  private List<T> elements;

  /**
   * @return the totalCount
   */
  public int getTotalCount()
  {
    return totalCount;
  }

  /**
   * @param totalCount the totalCount to set
   */
  public void setTotalCount( int totalCount )
  {
    this.totalCount = totalCount;
  }

  /**
   * @return the elements
   */
  public List<T> getElements()
  {
    return elements;
  }

  /**
   * @param elements the elements to set
   */
  public void setElements( List<T> elements )
  {
    this.elements = elements;
  }

}
