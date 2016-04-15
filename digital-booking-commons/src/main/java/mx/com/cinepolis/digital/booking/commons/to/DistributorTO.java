package mx.com.cinepolis.digital.booking.commons.to;

import org.apache.commons.lang.builder.CompareToBuilder;

public class DistributorTO extends CatalogTO implements Comparable<DistributorTO>
{

  private static final long serialVersionUID = -6321448912936706865L;
  private String shortName;
  
  /**
   * @return the shortName
   */
  public String getShortName()
  {
    return shortName;
  }
  
  /**
   * @param shortName the shortName to set
   */
  public void setShortName( String shortName )
  {
    this.shortName = shortName;
  }

  @Override
  public int compareTo( DistributorTO o )
  {
    return new CompareToBuilder().append( this.shortName, o.shortName ).toComparison();
  }
}
