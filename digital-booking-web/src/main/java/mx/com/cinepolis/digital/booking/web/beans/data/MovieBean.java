package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.query.DistributorQuery;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador de Movies , para consulta , borrado e insercion
 * 
 * @author agustin.ramirez
 */
@ManagedBean(name = "movieBean")
@ViewScoped
public class MovieBean extends BaseManagedBean implements Serializable
{

  private static final String SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE = "selectedFilterDistributor";
  private static final String FILTER_CODE_DBS_ATTRIBUTE = "filterCodeDBS";
  private static final String FILTER_MOVIE_NAME_ATTRIBUTE = "filterMovieName";
  private static final String FILTER_PRERELEASE_ATTRIBUTE = "filterPreRelease";
  private static final String FILTER_PREMIERE_ATTRIBUTE = "filterPremiere";
  private static final String FILTER_CURRENT_MOVIE_ATTRIBUTE = "filterCurrentMovie";
  private static final String FILTER_FESTIVAL_ATTRIBUTE = "filterFestival";
  private static final String FILTER_ALTERNATE_CONTENT = "filterAlternateContent";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String SAVED_IMAGE_SUCCES = "movies.savedImage.succes";
  private static final String ACTORS_ERROR_SIZE = "movies.actors.errorSize";
  private static final String UPLOAD_IMAGE_ERROR = "movies.uploadImage";
  private static final String SPECIAL_EVENT_NOT_VALID = "movies.specialevent.notvalid";
  private static final String FESTIVAL_NOT_VALID = "movies.festival.notvalid";

  private static final Logger LOG = LoggerFactory.getLogger( MovieBean.class );
  /**
   * Variable de Serializacion
   */
  private static final long serialVersionUID = 8401073496958873266L;
  /**
   * Servicio de Distribuidores
   */
  @EJB
  private ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB;

  /**
   * Servicio de Movie Formats
   */
  @EJB
  private ServiceAdminMovieFormatIntegratorEJB serviceAdminMovieFormatIntegratorEJB;

  /**
   * Servicios de sound Formats
   */
  @EJB
  private ServiceAdminSoundFormatIntegratorEJB serviceAdminSoundFormatIntegratorEJB;

  /**
   * Servicio de Movies
   */
  @EJB
  private ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB;

  /**
   * Servicio de sincronización
   */
  @EJB
  private ServiceDataSynchronizerIntegratorEJB dataSynchronizerIntegratorEJB;
  /***
   * Variable para datos dela tabla
   */
  private MovieLazyDatamodel movies;

  /**
   * Nueva pelicula
   */
  private EventMovieTO newMovie = new EventMovieTO();

  /**
   * Distribuidores(Combos)
   */
  private List<DistributorTO> distributors;

  /**
   * sound format
   */
  private List<CatalogTO> soundFormats;
  /**
   * Movie Formats
   */
  private List<CatalogTO> movieFormats;

  private String newCountry;

  /**
   * Movie Format
   */

  private EventMovieTO selectedMovie;

  private List<Object> movieFormatSelected;
  private List<Object> soundFormatSelected;

  private Long movieFormatIdSelected;
  private Long soundFormatIdSelected;

  /**
   * Filters
   */
  private String filterCodeDBS;
  private String filterMovieName;
  private List<DistributorTO> filterDistributors;
  private Long selectedFilterDistributor;
  private boolean filterPreRelease;
  private boolean filterPremiere;
  private boolean filterCurrentMovie;
  private boolean filterFestival;
  private boolean filetrAlternateContent;
  private String idDistributorAlternateContent;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    idDistributorAlternateContent = serviceAdminMovieIntegratorEJB.getIdDistributorParameter();
    this.movies = new MovieLazyDatamodel( serviceAdminMovieIntegratorEJB, super.getUserId() );
    this.filterDistributors = getDistributors();

    Object filterMovieNameObj = getSession().getAttribute( FILTER_MOVIE_NAME_ATTRIBUTE );
    Object filterCodeDBSObj = getSession().getAttribute( FILTER_CODE_DBS_ATTRIBUTE );
    Object selectedFilterDistributorObj = getSession().getAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE );
    Object filterPreReleaseObj = getSession().getAttribute( FILTER_PRERELEASE_ATTRIBUTE );
    Object filterPremiereObj = getSession().getAttribute( FILTER_PREMIERE_ATTRIBUTE );
    Object filterCurrentMovieObj = getSession().getAttribute( FILTER_CURRENT_MOVIE_ATTRIBUTE );
    Object filterFestivalObj = getSession().getAttribute( FILTER_FESTIVAL_ATTRIBUTE );
    Object filetrAlternateContentObj = getSession().getAttribute( FILTER_ALTERNATE_CONTENT );
    
    if( filterMovieNameObj != null )
    {
      filterMovieName = (String) filterMovieNameObj;
      removeAttribute( FILTER_MOVIE_NAME_ATTRIBUTE );
    }
    if( filterCodeDBSObj != null )
    {
      filterCodeDBS = (String) filterCodeDBSObj;
      removeAttribute( FILTER_CODE_DBS_ATTRIBUTE );
    }
    if( selectedFilterDistributorObj != null )
    {
      selectedFilterDistributor = (Long) selectedFilterDistributorObj;
      removeAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE );
    }
    if( filterPremiereObj != null )
    {
      filterPremiere = (Boolean) filterPremiereObj;
      removeAttribute( FILTER_PREMIERE_ATTRIBUTE );
    }
    if( filterPreReleaseObj != null )
    {
      filterPreRelease = (Boolean) filterPreReleaseObj;
      removeAttribute( FILTER_PRERELEASE_ATTRIBUTE );
    }
    if( filterCurrentMovieObj != null )
    {
      filterCurrentMovie = (Boolean) filterCurrentMovieObj;
      removeAttribute( FILTER_CURRENT_MOVIE_ATTRIBUTE );
    }
    if( filterFestivalObj != null )
    {
      filterFestival = (Boolean) filterFestivalObj;
      removeAttribute( FILTER_FESTIVAL_ATTRIBUTE );
    }
    if( filetrAlternateContentObj != null )
    {
      filetrAlternateContent = ( Boolean ) filetrAlternateContentObj;
      removeAttribute( FILTER_ALTERNATE_CONTENT );
    }
    searchFilters();
  }

  /**
   * Método que elimina distribuidor seleccionado
   */
  public void deleteMovie()
  {
    super.fillSessionData( selectedMovie );
    serviceAdminMovieIntegratorEJB.deleteMovie( selectedMovie );
    this.setSelectedMovie( new EventMovieTO() );
  }

  /**
   * Metodo que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedMovie == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  public String showAddPanel()
  {
    return "/views/data/movies/addMovie.do";
  }

  /**
   * Metodo que cambia ala pagina de edicion
   * 
   * @throws IOException
   */
  public void chargePage() throws IOException
  {
    if( getSelectedMovie() != null )
    {
      FacesContext ctx = FacesContext.getCurrentInstance();
      getSession().setAttribute( "selectedMovie", getSelectedMovie() );
      getSession().setAttribute( FILTER_MOVIE_NAME_ATTRIBUTE, filterMovieName );
      getSession().setAttribute( FILTER_CODE_DBS_ATTRIBUTE, filterCodeDBS );
      getSession().setAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE, selectedFilterDistributor );
      getSession().setAttribute( FILTER_PREMIERE_ATTRIBUTE, filterPremiere );
      getSession().setAttribute( FILTER_PRERELEASE_ATTRIBUTE, filterPreRelease );
      getSession().setAttribute( FILTER_CURRENT_MOVIE_ATTRIBUTE, filterCurrentMovie );
      getSession().setAttribute( FILTER_ALTERNATE_CONTENT, filetrAlternateContent );
      getSession().setAttribute( FILTER_FESTIVAL_ATTRIBUTE, filterFestival );
      ctx.getExternalContext().redirect( "editMovie.do" );
    }
    else
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * Metodo para salver una imagen
   * 
   * @return
   * @throws IOException
   */
  public void handleFileUpload( FileUploadEvent event ) throws IOException
  {
    FileTO newImage = new FileTO();
    super.fillSessionData( newImage );
    newImage.setName( event.getFile().getFileName() );
    newImage.setFile( event.getFile().getContents() );
    FileTO fileSaved = serviceAdminMovieIntegratorEJB.saveMovieImage( newImage );
    newMovie.setIdMovieImage( fileSaved.getId() );
    createMessageSuccessWithMsg( SAVED_IMAGE_SUCCES );
  }

  /**
   * Metodo que salva una imagen
   * 
   * @throws IOException
   */
  public void saveMovie() throws IOException
  {
    if( newMovie.getIdMovieImage() != null )
    {
      newMovie.setMovieFormats( new ArrayList<CatalogTO>() );
      newMovie.setSoundFormats( new ArrayList<CatalogTO>() );
      super.fillSessionData( newMovie );
      newMovie.getMovieFormats().add( new CatalogTO( movieFormatIdSelected ) );
      newMovie.getSoundFormats().add( new CatalogTO( soundFormatIdSelected ) );
      super.fillSessionData( newMovie );
      if( validateMovie( newMovie ) )
      {
        if( validateMovieFlags( newMovie ) )
        {
          serviceAdminMovieIntegratorEJB.saveMovie( newMovie );
        }
      }
      else
      {
        validationFail();
        createMessageError( ACTORS_ERROR_SIZE );
      }
    }
    else
    {
      validationFail();
      createMessageError( UPLOAD_IMAGE_ERROR );
    }
  }

  /**
   * Método que valida la configuración de los parámetros .
   * 
   * @param newMovie
   * @return valid Resultado de la validación
   */
  public boolean validateMovieFlags( EventMovieTO newMovie )
  {
    boolean isValid = true;
    boolean isAlternateContentDistributor = newMovie.getDistributor().getId().toString()
        .equals( this.idDistributorAlternateContent );
    boolean isRelease = (newMovie.isPrerelease() || newMovie.isPremiere());
    if( isAlternateContentDistributor && (isRelease || newMovie.isFestival()) )
    {
      isValid = false;
      validationFail();
      createMessageError( SPECIAL_EVENT_NOT_VALID );
    }
    else if( newMovie.isFestival() && (isRelease || isAlternateContentDistributor) )
    {
      isValid = false;
      validationFail();
      createMessageError( FESTIVAL_NOT_VALID );
    }
    if( newMovie.isFgAlternateContent()
        && (isRelease || newMovie.isFestival()) )
    {
      isValid = false;
      validationFail();
      createMessageError( "movies.specialevent.notvalid" );
    }
    return isValid;
  }

  /**
   * Método que valida la extensión del parámetro DsActor.
   * 
   * @param newMovie
   * @return
   */
  public boolean validateMovie( EventMovieTO newMovie )
  {
    boolean valid = true;
    int maxActorsLength = 320;
    if( newMovie.getDsActor().length() > maxActorsLength )
    {
      valid = false;
    }
    return valid;
  }

  /**
   * Si en UI el campo "pre release" o "premiere" fueron seleccionados, establece el valor del campo "festival" como
   * falso
   * 
   * @param event
   */
  public void validatePreRelease( AjaxBehaviorEvent event )
  {
    Boolean isPreRelease = (Boolean) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    if( isPreRelease )
    {
      this.getNewMovie().setFestival( false );
      this.getNewMovie().setFgAlternateContent( false );
    }
  }

  /**
   * Si en UI el campo "festival" fue seleccionado, establece el valor de los campos "pre release" y "premiere" como
   * falsos
   * 
   * @param event
   */
  public void validateFestival( AjaxBehaviorEvent event )
  {
    Boolean isFestival = (Boolean) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    if( isFestival )
    {
      this.getNewMovie().setPrerelease( false );
      this.getNewMovie().setPremiere( false );
      this.getNewMovie().setFgAlternateContent( false );
    }
  }

  /**
   * Si en UI el campo "AlternateContent" fue seleccionado, establece el valor de los campos "pre release" y "premiere" como
   * falsos
   * 
   * @param event
   */
  public void validateAlternateContent( AjaxBehaviorEvent event )
  {
    Boolean isAlternateContent = (Boolean) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    if( isAlternateContent )
    {
      this.getNewMovie().setPrerelease( false );
      this.getNewMovie().setPremiere( false );
      this.getNewMovie().setFestival( false );
    }
  }

  /**
   * Metodo para regresar a la pantalla anterior
   * 
   * @throws IOException
   */
  public void back() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( "movies.do" );
  }

  /**
   * Obtiene todos los ditributors
   * 
   * @return the distributors
   */
  public List<DistributorTO> getDistributors()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( super.getUserId() );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( DistributorQuery.DISTRIBUTOR_SHORT_NAME );
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_ACTIVE, true );
    PagingResponseTO<DistributorTO> response = serviceAdminDistributorIntegratorEJB
        .getCatalogDistributorSummary( pagingRequestTO );
    this.setDistributors( response.getElements() );
    return distributors;
  }

  /**
   * @param distributors the distributors to set
   */
  public void setDistributors( List<DistributorTO> distributors )
  {
    this.distributors = distributors;
  }

  /**
   * @return the movieFormats
   */
  public List<CatalogTO> getMovieFormats()
  {
    PagingResponseTO<CatalogTO> pagingResponseTO = serviceAdminMovieFormatIntegratorEJB.getMovieFormatAll();
    setMovieFormats( pagingResponseTO.getElements() );
    for( Object o : movieFormats )
    {
      LOG.info( o.toString() );
    }
    return movieFormats;
  }

  /**
   * @param movieFormats the movieFormats to set
   */
  public void setMovieFormats( List<CatalogTO> movieFormats )
  {
    this.movieFormats = movieFormats;
  }

  /**
   * @return the soundFormats
   */
  public List<CatalogTO> getSoundFormats()
  {
    PagingResponseTO<CatalogTO> response = serviceAdminSoundFormatIntegratorEJB.getSoundFormatAll();
    setSoundFormats( response.getElements() );
    return soundFormats;
  }

  /**
   * @param soundFormats the soundFormats to set
   */
  public void setSoundFormats( List<CatalogTO> soundFormats )
  {
    this.soundFormats = soundFormats;
  }

  /**
   * Selecciona movie
   * 
   * @return
   */
  public EventMovieTO getSelectedMovie()
  {
    return selectedMovie;
  }

  /**
   * set Selecciona movie
   * 
   * @param selectedMovie
   */
  public void setSelectedMovie( EventMovieTO selectedMovie )
  {
    this.selectedMovie = selectedMovie;
  }

  /**
   * @return the movies
   */
  public LazyDataModel<EventMovieTO> getMovies()
  {
    return movies;
  }

  /**
   * @param movies the movies to set
   */
  public void setMovies( MovieLazyDatamodel movies )
  {
    this.movies = movies;
  }

  /**
   * @return the newMovie
   */
  public EventMovieTO getNewMovie()
  {
    return newMovie;
  }

  /**
   * @param newMovie the newMovie to set
   */
  public void setNewMovie( EventMovieTO newMovie )
  {
    this.newMovie = newMovie;
  }

  /**
   * @return the newCountry
   */
  public String getNewCountry()
  {
    return newCountry;
  }

  /**
   * @param newCountry the newCountry to set
   */
  public void setNewCountry( String newCountry )
  {
    this.newCountry = newCountry;
  }

  /**
   * @return the movieFormatSelected
   */
  public List<Object> getMovieFormatSelected()
  {
    return movieFormatSelected;
  }

  /**
   * @param movieFormatSelected the movieFormatSelected to set
   */
  public void setMovieFormatSelected( List<Object> movieFormatSelected )
  {
    this.movieFormatSelected = movieFormatSelected;
  }

  /**
   * @return the soundFormatSelected
   */
  public List<Object> getSoundFormatSelected()
  {
    return soundFormatSelected;
  }

  /**
   * @param soundFormatSelected the soundFormatSelected to set
   */
  public void setSoundFormatSelected( List<Object> soundFormatSelected )
  {
    this.soundFormatSelected = soundFormatSelected;
  }

  /**
   * @return the filterCodeDBS
   */
  public String getFilterCodeDBS()
  {
    return filterCodeDBS;
  }

  /**
   * @param filterCodeDBS the filterCodeDBS to set
   */
  public void setFilterCodeDBS( String filterCodeDBS )
  {
    this.filterCodeDBS = filterCodeDBS;
  }

  /**
   * @return the filterMovieName
   */
  public String getFilterMovieName()
  {
    return filterMovieName;
  }

  /**
   * @param filterMovieName the filterMovieName to set
   */
  public void setFilterMovieName( String filterMovieName )
  {
    this.filterMovieName = filterMovieName;
  }

  /**
   * @return the filterDistributors
   */
  public List<DistributorTO> getFilterDistributors()
  {
    return filterDistributors;
  }

  /**
   * @return the filterPreRelease
   */
  public boolean isFilterPreRelease()
  {
    return filterPreRelease;
  }

  /**
   * @param filterPreRelease the filterPreRelease to set
   */
  public void setFilterPreRelease( boolean filterPreRelease )
  {
    this.filterPreRelease = filterPreRelease;
  }

  /**
   * @param filterDistributors the filterDistributors to set
   */
  public void setFilterDistributors( List<DistributorTO> filterDistributors )
  {
    this.filterDistributors = filterDistributors;
  }

  /**
   * @return the selectedFilterDistributor
   */
  public Long getSelectedFilterDistributor()
  {
    return selectedFilterDistributor;
  }

  /**
   * @param selectedFilterDistributor the selectedFilterDistributor to set
   */
  public void setSelectedFilterDistributor( Long selectedFilterDistributor )
  {
    this.selectedFilterDistributor = selectedFilterDistributor;
  }

  /**
   * @return the filterPremiere
   */
  public boolean isFilterPremiere()
  {
    return filterPremiere;
  }

  /**
   * @param filterPremiere the filterPremiere to set
   */
  public void setFilterPremiere( boolean filterPremiere )
  {
    this.filterPremiere = filterPremiere;
  }

  /**
   * @return the filterCurrentMovie
   */
  public boolean isFilterCurrentMovie()
  {
    return filterCurrentMovie;
  }

  /**
   * @param filterCurrentMovie the filterCurrentMovie to set
   */
  public void setFilterCurrentMovie( boolean filterCurrentMovie )
  {
    this.filterCurrentMovie = filterCurrentMovie;
  }

  /**
   * @return the filterFestival
   */
  public boolean isFilterFestival()
  {
    return filterFestival;
  }

  /**
   * @param filterFestival the filterFestival to set
   */
  public void setFilterFestival( boolean filterFestival )
  {
    this.filterFestival = filterFestival;
  }

  /**
   * @return the filetrAlternateContent
   */
  public boolean isFiletrAlternateContent()
  {
    return filetrAlternateContent;
  }

  /**
   * @param filetrAlternateContent the filetrAlternateContent to set
   */
  public void setFiletrAlternateContent( boolean filetrAlternateContent )
  {
    this.filetrAlternateContent = filetrAlternateContent;
  }

  public void resetFilters()
  {
    this.movies.setCodeDBS( null );
    this.movies.setMovieName( null );
    this.movies.setDistributorId( null );
    this.movies.setPremiere( null );
    this.movies.setPrerelease( null );
    this.movies.setCurrentMovie( null );
    this.filterCodeDBS = null;
    this.filterMovieName = null;
    this.selectedFilterDistributor = null;
    this.filterPremiere = false;
    this.filterPreRelease = false;
    this.filterCurrentMovie = false;
    this.filterFestival = false;
    this.filetrAlternateContent = false;
  }

  public void searchFilters()
  {
    this.movies.setCodeDBS( this.filterCodeDBS );
    this.movies.setMovieName( this.filterMovieName );
    this.movies.setDistributorId( this.selectedFilterDistributor );
    this.movies.setPremiere( this.filterPremiere );
    this.movies.setPrerelease( this.filterPreRelease );
    this.movies.setCurrentMovie( this.filterCurrentMovie );
    this.movies.setFestival( this.filterFestival );
    this.movies.setFgAlternateContent( this.filetrAlternateContent );
  }

  public void handleToggleFilters( ToggleEvent event )
  {
    resetFilters();
  }

  public void synchronizeWithView()
  {
    dataSynchronizerIntegratorEJB.synchronizeEventMovies();
  }

  static class MovieLazyDatamodel extends LazyDataModel<EventMovieTO>
  {

    private static final long serialVersionUID = 3769164417858303397L;
    private String movieName;
    private String codeDBS;
    private Long distributorId;
    private Boolean prerelease;
    private Boolean premiere;
    private Boolean currentMovie;
    private Boolean festival;
    private Boolean fgAlternateContent;
    /**
     * @return the fgAlternateContent
     */
    public Boolean getFgAlternateContent()
    {
      return fgAlternateContent;
    }

    /**
     * @param fgAlternateContent the fgAlternateContent to set
     */
    public void setFgAlternateContent( Boolean fgAlternateContent )
    {
      this.fgAlternateContent = fgAlternateContent;
    }

    private Long userId;
    private ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB;

    public MovieLazyDatamodel( ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB, Long userId )
    {
      this.serviceAdminMovieIntegratorEJB = serviceAdminMovieIntegratorEJB;
      this.userId = userId;
    }

    @Override
    public List<EventMovieTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( EventQuery.EVENT_ACTIVE, true );

      if( StringUtils.isNotBlank( this.movieName ) )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_NAME, this.movieName );
      }
      if( StringUtils.isNotBlank( this.codeDBS ) )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_CODE_DBS, this.codeDBS );
      }
      if( this.distributorId != null && distributorId.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, this.distributorId );
      }
      this.setFlagsToRequest( pagingRequestTO );
      PagingResponseTO<EventMovieTO> response = serviceAdminMovieIntegratorEJB.getCatalogMovieSummary( pagingRequestTO );
      this.setRowCount( response.getTotalCount() );
      return response.getElements();

    }

    private void setFlagsToRequest( PagingRequestTO pagingRequestTO )
    {
      if( this.premiere != null && this.premiere )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_PREMIERE, this.premiere );
      }
      if( this.prerelease != null && this.prerelease )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_PRERELEASE, this.prerelease );
      }
      if( this.currentMovie != null && this.currentMovie )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_CURRENT_MOVIE, this.currentMovie );
      }
      if( this.festival != null && this.festival )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_FESTIVAL, this.festival );
      }
      if (this.fgAlternateContent != null && this.fgAlternateContent)
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_ALTERNATE_CONTENT, this.fgAlternateContent );
      }
    }

    /**
     * @return the movieName
     */
    public String getMovieName()
    {
      return movieName;
    }

    /**
     * @param movieName the movieName to set
     */
    public void setMovieName( String movieName )
    {
      this.movieName = movieName;
    }

    /**
     * @return the codeDBS
     */
    public String getCodeDBS()
    {
      return codeDBS;
    }

    /**
     * @param codeDBS the codeDBS to set
     */
    public void setCodeDBS( String codeDBS )
    {
      this.codeDBS = codeDBS;
    }

    /**
     * @return the distributorId
     */
    public Long getDistributorId()
    {
      return distributorId;
    }

    /**
     * @param distributorId the distributorId to set
     */
    public void setDistributorId( Long distributorId )
    {
      this.distributorId = distributorId;
    }

    /**
     * @return the premiere
     */
    public Boolean getPremiere()
    {
      return premiere;
    }

    /**
     * @param premiere the premiere to set
     */
    public void setPremiere( Boolean premiere )
    {
      this.premiere = premiere;
    }

    /**
     * @return the currentMovie
     */
    public Boolean isCurrentMovie()
    {
      return currentMovie;
    }

    /**
     * @param currentMovie the currentMovie to set
     */
    public void setCurrentMovie( Boolean currentMovie )
    {
      this.currentMovie = currentMovie;
    }

    /**
     * @return the prerelease
     */
    public Boolean getPrerelease()
    {
      return prerelease;
    }

    /**
     * @param prerelease the prerelease to set
     */
    public void setPrerelease( Boolean prerelease )
    {
      this.prerelease = prerelease;
    }

    /**
     * @return the festival
     */
    public Boolean getFestival()
    {
      return festival;
    }

    /**
     * @param festival the festival to set
     */
    public void setFestival( Boolean festival )
    {
      this.festival = festival;
    }

  }

  /**
   * @return the movieFormatIdSelected
   */
  public Long getMovieFormatIdSelected()
  {
    return movieFormatIdSelected;
  }

  /**
   * @param movieFormatIdSelected the movieFormatIdSelected to set
   */
  public void setMovieFormatIdSelected( Long movieFormatIdSelected )
  {
    this.movieFormatIdSelected = movieFormatIdSelected;
  }

  /**
   * @return the soundFormatIdSelected
   */
  public Long getSoundFormatIdSelected()
  {
    return soundFormatIdSelected;
  }

  /**
   * @param soundFormatIdSelected the soundFormatIdSelected to set
   */
  public void setSoundFormatIdSelected( Long soundFormatIdSelected )
  {
    this.soundFormatIdSelected = soundFormatIdSelected;
  }

  /**
   * @return the idDistributorAlternateContent
   */
  public String getIdDistributorAlternateContent()
  {
    return idDistributorAlternateContent;
  }

  /**
   * @param idDistributorAlternateContent the idDistributorAlternateContent to set
   */
  public void setIdDistributorAlternateContent( String idDistributorAlternateContent )
  {
    this.idDistributorAlternateContent = idDistributorAlternateContent;
  }

}