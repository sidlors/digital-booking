package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Clase que modela la información de una película
 * 
 * @author rgarcia
 */
public class EventMovieTO extends EventTO
{

  /**
   * Serialización
   */
  private static final long serialVersionUID = -6735455369224807218L;

  /**
   * Id de la película
   */
  private Long idEventMovie;

  /**
   * Idioma
   */
  private CatalogTO movieLanguage = new CatalogTO();
  /**
   * Distribuidor
   */
  private DistributorTO distributor = new DistributorTO();
  /**
   * Fecha de lanzamiento
   */
  private Date dtRelease;

  /**
   * Sinopsis de la película
   */
  private String dsSynopsis;
  /**
   * Clasificación
   */
  private String dsRating;
  /**
   * Script
   */
  private String dsScript;
  /**
   * Genero
   */
  private String dsGenre;
  /**
   * URL de la película
   */
  private String dsUrl;
  /**
   * Nombre de director
   */
  private String dsDirector;
  /**
   * Actores
   **/
  private String dsActor;
  /**
   * País de procedencia
   */
  private String dsCountry;

  /**
   * Original movie name
   */
  private String dsOriginalName;

  /**
   * Id de la imagen de la película
   */
  private Long idMovieImage;

  /**
   * Max exhibition weeks
   */
  private Integer exhibitionWeeks;

  /**
   * @return the idEventMovie
   */
  public Long getIdEventMovie()
  {
    return idEventMovie;
  }

  /**
   * @param idEventMovie the idEventMovie to set
   */
  public void setIdEventMovie( Long idEventMovie )
  {
    this.idEventMovie = idEventMovie;
  }

  /**
   * @return the movieLanguage
   */
  public CatalogTO getMovieLanguage()
  {
    return movieLanguage;
  }

  /**
   * @param movieLanguage the movieLanguage to set
   */
  public void setMovieLanguage( CatalogTO movieLanguage )
  {
    this.movieLanguage = movieLanguage;
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
   * @return the dtRelease
   */
  public Date getDtRelease()
  {
    return CinepolisUtils.enhancedClone( dtRelease );
  }

  /**
   * @param dtRelease the dtRelease to set
   */
  public void setDtRelease( Date dtRelease )
  {
    this.dtRelease = CinepolisUtils.enhancedClone( dtRelease );
  }

  /**
   * @return the dsSynopsis
   */
  public String getDsSynopsis()
  {
    return dsSynopsis;
  }

  /**
   * @param dsSynopsis the dsSynopsis to set
   */
  public void setDsSynopsis( String dsSynopsis )
  {
    this.dsSynopsis = dsSynopsis;
  }

  /**
   * @return the dsRating
   */
  public String getDsRating()
  {
    return dsRating;
  }

  /**
   * @param dsRating the dsRating to set
   */
  public void setDsRating( String dsRating )
  {
    this.dsRating = dsRating;
  }
  
  /**
   * @return the dsScript
   */
  public String getDsScript()
  {
    return dsScript;
  }

  /**
   * @param dsScript the dsScript to set
   */
  public void setDsScript( String dsScript )
  {
    this.dsScript = dsScript;
  }

  /**
   * @return the dsGenre
   */
  public String getDsGenre()
  {
    return dsGenre;
  }

  /**
   * @param dsGenre the dsGenre to set
   */
  public void setDsGenre( String dsGenre )
  {
    this.dsGenre = dsGenre;
  }

  /**
   * @return the dsUrl
   */
  public String getDsUrl()
  {
    return dsUrl;
  }

  /**
   * @param dsUrl the dsUrl to set
   */
  public void setDsUrl( String dsUrl )
  {
    this.dsUrl = dsUrl;
  }

  /**
   * @return the dsDirector
   */
  public String getDsDirector()
  {
    return dsDirector;
  }

  /**
   * @param dsDirector the dsDirector to set
   */
  public void setDsDirector( String dsDirector )
  {
    this.dsDirector = dsDirector;
  }

  /**
   * @return the dsActor
   */
  public String getDsActor()
  {
    return dsActor;
  }

  /**
   * @param dsActor the dsActor to set
   */
  public void setDsActor( String dsActor )
  {
    this.dsActor = dsActor;
  }

  /**
   * @return the dsCountry
   */
  public String getDsCountry()
  {
    return dsCountry;
  }

  /**
   * @param dsCountry the dsCountry to set
   */
  public void setDsCountry( String dsCountry )
  {
    this.dsCountry = dsCountry;
  }

  /**
   * @return the dsOriginalName
   */
  public String getDsOriginalName()
  {
    return dsOriginalName;
  }

  /**
   * @param dsOriginalName the dsOriginalName to set
   */
  public void setDsOriginalName( String dsOriginalName )
  {
    this.dsOriginalName = dsOriginalName;
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
   * @return the exhibitionWeeks
   */
  public Integer getExhibitionWeeks()
  {
    return exhibitionWeeks;
  }

  /**
   * @param exhibitionWeeks the exhibitionWeeks to set
   */
  public void setExhibitionWeeks( Integer exhibitionWeeks )
  {
    this.exhibitionWeeks = exhibitionWeeks;
  }

}
