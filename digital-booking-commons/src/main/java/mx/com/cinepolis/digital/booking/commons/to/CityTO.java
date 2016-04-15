package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Class that modeling the information about cities
 * 
 * @author jreyesv
 */
public class CityTO extends CatalogTO implements Serializable
{

  /**
   * Serial version variable
   */
  private static final long serialVersionUID = 6005534446136306771L;

  /**
   * Country information
   */
  private CatalogTO country;

  /**
   * State information
   */
  private StateTO<CatalogTO, Number> state;

  /**
   * @return the country
   */
  public CatalogTO getCountry()
  {
    return country;
  }

  /**
   * @param country the country to set
   */
  public void setCountry( CatalogTO country )
  {
    this.country = country;
  }

  /**
   * @return the state
   */
  public StateTO<CatalogTO, Number> getState()
  {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState( StateTO<CatalogTO, Number> state )
  {
    this.state = state;
  }

  @Override
  public boolean equals( Object other )
  {
    boolean result = false;
    if( this == other )
    {
      result = true;
    }
    else if( other instanceof CityTO )
    {
      CityTO o = (CityTO) other;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( this.getId(), o.getId() );
      result = builder.isEquals();
    }
    return result;
  }

  @Override
  public int hashCode()
  {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append( this.getId() );
    return builder.toHashCode();
  }

  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.getId() );
    builder.append( "name", this.getName() );
    return builder.toString();
  }
}
