package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Mock class for trailersTO object.
 * 
 * @author jreyesv
 */
public class TrailerTOMock extends AbstractTO implements Serializable
{
  /**
   * Serial version
   */
  private static final long serialVersionUID = -6720399717962724844L;

  private Long idTrailer;
  private String name;
  private CatalogTO format;
  private CatalogTO version;
  private CatalogTO rating;
  private String genre;
  private String duration;
  private DistributorTO distributor;
  private Date releaseDate;
  private CatalogTO statusTrailer;
  private boolean current;

  /**
   * Constructor.
   * 
   * @param idTrailer
   * @param name
   * @param format
   * @param version
   * @param rating
   * @param genre
   * @param duration
   * @param distributor
   * @param releaseDate
   * @param statusTrailer
   * @param current
   */
  public TrailerTOMock( Long idTrailer, String name, CatalogTO format, CatalogTO version, CatalogTO rating, String genre, String duration, DistributorTO distributor, Date releaseDate, CatalogTO statusTrailer, boolean current )
  {
    this.idTrailer = idTrailer;
    this.name = name;
    this.format = format;
    this.version = version;
    this.rating = rating;
    this.genre = genre;
    this.duration = duration;
    this.distributor = distributor;
    this.releaseDate = releaseDate;
    this.statusTrailer = statusTrailer;
    this.current = current;
  }

  /**
   * @return the idTrailer
   */
  public Long getIdTrailer()
  {
    return idTrailer;
  }

  /**
   * @param idTrailer the idTrailer to set
   */
  public void setIdTrailer( Long idTrailer )
  {
    this.idTrailer = idTrailer;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return the format
   */
  public CatalogTO getFormat()
  {
    return format;
  }

  /**
   * @param format the format to set
   */
  public void setFormat( CatalogTO format )
  {
    this.format = format;
  }

  /**
   * @return the version
   */
  public CatalogTO getVersion()
  {
    return version;
  }

  /**
   * @param version the version to set
   */
  public void setVersion( CatalogTO version )
  {
    this.version = version;
  }

  /**
   * @return the rating
   */
  public CatalogTO getRating()
  {
    return rating;
  }

  /**
   * @param rating the rating to set
   */
  public void setRating( CatalogTO rating )
  {
    this.rating = rating;
  }

  /**
   * @param statusTrailer the statusTrailer to set
   */
  public void setStatusTrailer( CatalogTO statusTrailer )
  {
    this.statusTrailer = statusTrailer;
  }

  /**
   * @return the genre
   */
  public String getGenre()
  {
    return genre;
  }

  /**
   * @param genre the genre to set
   */
  public void setGenre( String genre )
  {
    this.genre = genre;
  }

  /**
   * @return the duration
   */
  public String getDuration()
  {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration( String duration )
  {
    this.duration = duration;
  }

  /**
   * @return the distributor
   */
  public DistributorTO getDistributor()
  {
    return distributor;
  }

  /**
   * @param distributor the distributor to set
   */
  public void setDistributor( DistributorTO distributor )
  {
    this.distributor = distributor;
  }

  /**
   * @return the releaseDate
   */
  public Date getReleaseDate()
  {
    return releaseDate;
  }

  /**
   * @param releaseDate the releaseDate to set
   */
  public void setReleaseDate( Date releaseDate )
  {
    this.releaseDate = releaseDate;
  }

  /**
   * @return the statusTrailer
   */
  public CatalogTO getStatusTrailer()
  {
    return statusTrailer;
  }

  /**
   * @return the current
   */
  public boolean isCurrent()
  {
    return current;
  }

  /**
   * @param current the current to set
   */
  public void setCurrent( boolean current )
  {
    this.current = current;
  }

  @Override
  public boolean equals( Object other )
  {
    boolean result = false;
    if( this == other )
    {
      result = true;
    }
    else if( other instanceof TrailerTOMock )
    {
      TrailerTOMock o = (TrailerTOMock) other;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( idTrailer, o.idTrailer );
      result = builder.isEquals();
    }
    return result;
  }

  @Override
  public int hashCode()
  {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append( this.idTrailer );
    return builder.toHashCode();
  }

  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.getIdTrailer() );
    builder.append( "name", this.getName() );
    return builder.toString();
  }
}
