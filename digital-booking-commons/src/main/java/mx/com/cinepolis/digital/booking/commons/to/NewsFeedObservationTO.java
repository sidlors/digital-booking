package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Transfer object for news feeds
 * 
 * @author gsegura
 */
public class NewsFeedObservationTO extends AbstractObservationTO
{
  private static final long serialVersionUID = 1L;

  /**
   * The id news feed
   */
  private Long idNewsFeed;

  /**
   * Date starting of the publication
   */
  private Date start;
  /**
   * Date ending of the publication
   */
  private Date end;
  /**
   * Regions which will see the publication, if null/empty its for all the users
   */
  private List<CatalogTO> regions;

  /**
   * @return the idNewsFeed
   */
  public Long getIdNewsFeed()
  {
    return idNewsFeed;
  }

  /**
   * @param idNewsFeed the idNewsFeed to set
   */
  public void setIdNewsFeed( Long idNewsFeed )
  {
    this.idNewsFeed = idNewsFeed;
  }

  /**
   * @return the start
   */
  public Date getStart()
  {
    return CinepolisUtils.enhancedClone( start );
  }

  /**
   * @param start the start to set
   */
  public void setStart( Date start )
  {
    this.start = CinepolisUtils.enhancedClone( start );
  }

  /**
   * @return the end
   */
  public Date getEnd()
  {
    return CinepolisUtils.enhancedClone( end );
  }

  /**
   * @param end the end to set
   */
  public void setEnd( Date end )
  {
    this.end = CinepolisUtils.enhancedClone( end );
  }

  /**
   * @return the regions
   */
  public List<CatalogTO> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<CatalogTO> regions )
  {
    this.regions = regions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object object )
  {
    return super.equals( object ) && object instanceof NewsFeedObservationTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return super.getId().hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( AbstractObservationTO o )
  {
    return super.getId().compareTo( o.getId() );
  }

  @Override
  public String toString()
  {

    ToStringBuilder ts = new ToStringBuilder( this );
    ts.append( "idNewsFeed", idNewsFeed );
    ts.appendSuper( super.toString() );
    return ts.toString();

  }

}
