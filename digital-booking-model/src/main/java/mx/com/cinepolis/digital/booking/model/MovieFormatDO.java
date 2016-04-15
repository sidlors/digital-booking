package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for W_MOVIE_FORMAT
 * 
 * @author shernandezl
 *
 */
@Entity
@Table(name = "W_MOVIE_FORMAT")
@NamedQueries({
  @NamedQuery(name = "MovieFormatDO.findAll", query = "SELECT mf FROM MovieFormatDO mf")
})
public class MovieFormatDO extends AbstractEntity<MovieFormatDO>
{
  private static final long serialVersionUID = 1794285946337517185L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_MOVIE_FORMAT")
  private Integer idMovieFormat;
  
  @Column(name = "DS_NAME")
  private String dsName;
  
  @Column(name = "ID_CATEGORY")
  private Integer idCategory;
  
  /**
   * Constructor default
   */
  public MovieFormatDO ()
  {
  }
  
  /**
   * @return the idMovieFormat
   */
  public Integer getIdMovieFormat()
  {
    return idMovieFormat;
  }

  /**
   * @param idMovieFormat the idMovieFormat to set
   */
  public void setIdMovieFormat( Integer idMovieFormat )
  {
    this.idMovieFormat = idMovieFormat;
  }

  /**
   * @return the dsName
   */
  public String getDsName()
  {
    return dsName;
  }

  /**
   * @param dsName the dsName to set
   */
  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  /**
   * @return the idCategory
   */
  public Integer getIdCategory()
  {
    return idCategory;
  }

  /**
   * @param idCategory the idCategory to set
   */
  public void setIdCategory( Integer idCategory )
  {
    this.idCategory = idCategory;
  }

  @Override
  public int compareTo( MovieFormatDO other )
  {
    return this.idMovieFormat.compareTo( other.idMovieFormat );
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof MovieFormatDO) )
    {
      return false;
    }
    MovieFormatDO other = (MovieFormatDO) object;
    if( (this.idMovieFormat == null && other.idMovieFormat != null)
        || (this.idMovieFormat != null && !this.idMovieFormat.equals( other.idMovieFormat )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idMovieFormat != null ? idMovieFormat.hashCode() : 0);
    return hash;
  }
  
  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idMovieFormat", this.idMovieFormat ).toString();
  }

}
