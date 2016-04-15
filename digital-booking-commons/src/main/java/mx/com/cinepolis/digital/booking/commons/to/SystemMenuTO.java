package mx.com.cinepolis.digital.booking.commons.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Transfer object for a system menu element.
 * 
 * @author afuentes
 */
public class SystemMenuTO extends CatalogTO
{
  private static final long serialVersionUID = -5684750507663353431L;

  private String url;
  private String icon;
  private Boolean isFunction;
  private Integer order;
  private List<SystemMenuTO> children = new ArrayList<SystemMenuTO>();

  /**
   * Method that tells whether the current {@link SystemMenuTO} has children or not.
   * 
   * @return <code>true</code> if the current {@link SystemMenuTO} has children, <code>false</code> otherwise.
   */
  public boolean hasChildren()
  {
    return CollectionUtils.isNotEmpty( children );
  }

  /**
   * Method that adds a child to the current {@link SystemMenuTO}.
   * 
   * @param systemMenuTO {@link SystemMenuTO} to add as a child.
   */
  public void addChild( SystemMenuTO systemMenuTO )
  {
    if( systemMenuTO != null && !children.contains( systemMenuTO ) )
    {
      children.add( systemMenuTO );
    }
  }

  /**
   * @return the children
   */
  public List<SystemMenuTO> getChildren()
  {
    return children;
  }

  /**
   * @param children the children to set
   */
  public void setChildren( List<SystemMenuTO> children )
  {
    this.children = children;
  }

  /**
   * @return the url
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl( String url )
  {
    this.url = url;
  }

  /**
   * @return the icon
   */
  public String getIcon()
  {
    return icon;
  }

  /**
   * @param icon the icon to set
   */
  public void setIcon( String icon )
  {
    this.icon = icon;
  }

  /**
   * @return the isFunction
   */
  public Boolean getIsFunction()
  {
    return isFunction;
  }

  /**
   * @param isFunction the isFunction to set
   */
  public void setIsFunction( Boolean isFunction )
  {
    this.isFunction = isFunction;
  }

  /**
   * @return the order
   */
  public Integer getOrder()
  {
    return order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder( Integer order )
  {
    this.order = order;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof SystemMenuTO )
    {
      flag = new EqualsBuilder().append( super.getId(), ((SystemMenuTO) object).getId() ).isEquals();
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( getId() ).hashCode();
  }

  @Override
  public String toString()
  {
    ToStringBuilder ts = new ToStringBuilder( this );
    ts.append( "id", getId() );
    return ts.toString();
  }

}
