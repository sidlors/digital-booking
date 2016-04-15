package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * JPA entity for C_MOVIE_IMAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_MOVIE_IMAGE")
@NamedQueries({
    @NamedQuery(name = "MovieImageDO.findAll", query = "SELECT m FROM MovieImageDO m"),
    @NamedQuery(name = "MovieImageDO.findByIdMovieImage", query = "SELECT m FROM MovieImageDO m WHERE m.idMovieImage = :idMovieImage"),
    @NamedQuery(name = "MovieImageDO.findByDsImage", query = "SELECT m FROM MovieImageDO m WHERE m.dsImage = :dsImage") })
public class MovieImageDO extends AbstractEntity<MovieImageDO>
{
  private static final long serialVersionUID = -3400957686877246784L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_MOVIE_IMAGE")
  private Long idMovieImage;

  @Column(name = "DS_IMAGE")
  private String dsImage;

  @Lob
  @Basic(fetch=FetchType.LAZY)
  @Column(name = "DS_FILE")
  private byte[] dsFile;


  /**
   * Constructor default
   */
  public MovieImageDO()
  {
  }

  /**
   * Constructor by idMovieImage
   * 
   * @param idMovieImage
   */
  public MovieImageDO( Long idMovieImage )
  {
    this.idMovieImage = idMovieImage;
  }

  /**
   * @return the idMovieImage
   */
  public Long getIdMovieImage()
  {
    return idMovieImage;
  }

  /**
   * @param idMovieImage the idMovieImage to set
   */
  public void setIdMovieImage( Long idMovieImage )
  {
    this.idMovieImage = idMovieImage;
  }

  /**
   * @return the dsImage
   */
  public String getDsImage()
  {
    return dsImage;
  }

  /**
   * @param dsImage the dsImage to set
   */
  public void setDsImage( String dsImage )
  {
    this.dsImage = dsImage;
  }

  /**
   * @return the dsFile
   */
  public byte[] getDsFile()
  {
    return CinepolisUtils.enhacedByteArrayClone( dsFile );
  }

  /**
   * @param dsFile the dsFile to set
   */
  public void setDsFile( byte[] dsFile )
  {
    this.dsFile = CinepolisUtils.enhacedByteArrayClone( dsFile );
  }


  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idMovieImage != null ? idMovieImage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof MovieImageDO) )
    {
      return false;
    }
    MovieImageDO other = (MovieImageDO) object;
    if( (this.idMovieImage == null && other.idMovieImage != null)
        || (this.idMovieImage != null && !this.idMovieImage.equals( other.idMovieImage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.MovieImageDO[ idMovieImage=" + idMovieImage + " ]";
  }

  @Override
  public int compareTo( MovieImageDO other )
  {
    return this.idMovieImage.compareTo( other.idMovieImage );
  }

}
