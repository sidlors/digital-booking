package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;

/**
 * Bean encargado de la logica de actualizacion de peliculas
 * 
 * @author agustin.ramirez
 */
@ManagedBean(name = "editMovieBean")
@ViewScoped
public class EditMovieBean extends BaseManagedBean implements Serializable
{

  private static final String SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE = "selectedFilterDistributor";
  private static final String FILTER_CODE_DBS_ATTRIBUTE = "filterCodeDBS";
  private static final String FILTER_MOVIE_NAME_ATTRIBUTE = "filterMovieName";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String UPDATE_MOVIE_BOOKED_MESSAGE_ERROR = "movies.mesgerror.updateMovieBooked";

  /**
	 * 
	 */
  private static final long serialVersionUID = -4323507090551688067L;

  /**
   * SelectedMovie
   */
  private EventMovieTO selectedMovie;

  /**
   * Service Movie integrator
   */
  @EJB
  private ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  private double random;

  private List<Object> movieFormatSelected;
  private List<Object> soundFormatSelected;

  private Long movieFormatIdSelected;
  private Long soundFormatIdSelected;
  private Long movieFormatIdOriginal;

  private String filterMovieName;
  private String filterCodeDBS;
  private Long selectedFilterDistributor;
  private String idDistributorAlternateContent;

  /**
   * loadData
   * 
   * @return
   * @throws FileNotFoundException
   */
  @PostConstruct
  public void loadData()
  {

    idDistributorAlternateContent = serviceAdminMovieIntegratorEJB.getIdDistributorParameter();
    filterMovieName = (String) getSession().getAttribute( FILTER_MOVIE_NAME_ATTRIBUTE );
    filterCodeDBS = (String) getSession().getAttribute( FILTER_CODE_DBS_ATTRIBUTE );
    selectedFilterDistributor = (Long) getSession().getAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE );

    getSession().removeAttribute( FILTER_MOVIE_NAME_ATTRIBUTE );
    getSession().removeAttribute( FILTER_CODE_DBS_ATTRIBUTE );
    getSession().removeAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE );

    EventMovieTO movieTO = (EventMovieTO) getSession().getAttribute( "selectedMovie" );
    selectedMovie = movieTO;
    selectedMovie.setSoundsSelected( new ArrayList<Long>() );
    selectedMovie.setMoviesSelected( new ArrayList<Long>() );
    this.movieFormatSelected = new ArrayList<Object>();
    this.soundFormatSelected = new ArrayList<Object>();
    if( CollectionUtils.isNotEmpty( selectedMovie.getSoundFormats() ) )
    {
      soundFormatIdSelected = selectedMovie.getSoundFormats().get( 0 ).getId();
    }
    if( CollectionUtils.isNotEmpty( selectedMovie.getMovieFormats() ) )
    {
      movieFormatIdSelected = selectedMovie.getMovieFormats().get( 0 ).getId();
    }
    setRandom( Math.random() );
  }

  /**
   * Metodo para regresar a la pantalla anterior
   * 
   * @throws IOException
   */
  public void back() throws IOException
  {
    selectedMovie = null;
    getSession().setAttribute( FILTER_MOVIE_NAME_ATTRIBUTE, filterMovieName );
    getSession().setAttribute( FILTER_CODE_DBS_ATTRIBUTE, filterCodeDBS );
    getSession().setAttribute( SELECTED_FILTER_DISTRIBUTOR_ATTRIBUTE, selectedFilterDistributor );

    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( "movies.do" );
  }

  /**
   * Edit Movie
   * 
   * @throws IOException
   */
  public void editMovie()
  {
    if( !this.validateMovieFormatChange() )
    {
      super.fillSessionData( selectedMovie );
      selectedMovie.setMovieFormats( new ArrayList<CatalogTO>() );
      selectedMovie.setSoundFormats( new ArrayList<CatalogTO>() );

      selectedMovie.getMovieFormats().add( new CatalogTO( movieFormatIdSelected ) );
      selectedMovie.getSoundFormats().add( new CatalogTO( soundFormatIdSelected ) );
      if( this.validateMovieFlags( selectedMovie ) )
      {
        Long preReleaseBooked = bookingServiceIntegratorEJB.countPrereleaseBookingBooked( selectedMovie );
        if( preReleaseBooked <= 0 )
        {
          serviceAdminMovieIntegratorEJB.updateMovie( selectedMovie );
        }
        else
        {
          createMessageError( "movies.preRelease.dontUpdate" );
        }
      }
    }
  }

  /**
   * Método que valida la configuración de los parámetros .
   * 
   * @param selectedMovie
   * @return valid Resultado de la validación
   */
  public boolean validateMovieFlags( EventMovieTO selectedMovie )
  {
    boolean isValid = true;
    boolean isPreReleaseOrPremier = selectedMovie.isPrerelease() || selectedMovie.isPremiere();
    if( selectedMovie.getDistributor().getId().toString().equals( idDistributorAlternateContent )
        && (isPreReleaseOrPremier || selectedMovie.isFestival()) )
    {
      isValid = false;
      validationFail();
      createMessageError( "movies.specialevent.notvalid" );
    }
    if( selectedMovie.isFestival()
        && (isPreReleaseOrPremier || selectedMovie.getDistributor().getId().toString()
            .equals( this.idDistributorAlternateContent )) )
    {
      isValid = false;
      validationFail();
      createMessageError( "movies.festival.notvalid" );
    }
    if( selectedMovie.isFgAlternateContent()
        && (isPreReleaseOrPremier || selectedMovie.isFestival()) )
    {
      isValid = false;
      validationFail();
      createMessageError( "movies.specialevent.notvalid" );
    }
    return isValid;
  }

  /**
   * Method that validates whether movie format changes and whether the movie is in booking.
   * 
   * @return isValid with validate information.
   */
  private boolean validateMovieFormatChange()
  {
    boolean isValid = false;
    if( !this.movieFormatIdSelected.equals( this.movieFormatIdOriginal ) )
    {
      if( isValid = this.serviceAdminMovieIntegratorEJB.isMovieInBooking( selectedMovie.getIdEvent() ) )
      {
        createMessageError( UPDATE_MOVIE_BOOKED_MESSAGE_ERROR );
        validationFail();
      }
    }
    return isValid;
  }

  /**
   * Si en UI el campo "pre release" o "premiere" fueron seleccionados, establece el valor del campo "festival" y
   * "alternate content" como falso
   * 
   * @param event
   */
  public void validatePreRelease( AjaxBehaviorEvent event )
  {
    Boolean isPreRelease = (Boolean) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    if( isPreRelease )
    {
      this.selectedMovie.setFestival( false );
      this.selectedMovie.setFgAlternateContent( false );
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
      this.selectedMovie.setPrerelease( false );
      this.selectedMovie.setPremiere( false );
      this.selectedMovie.setFgAlternateContent( false );
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
      this.selectedMovie.setPrerelease( false );
      this.selectedMovie.setPremiere( false );
      this.selectedMovie.setFestival( false );
    }
  }

  /**
   * Metodo para actualizar una imagen
   * 
   * @return
   * @throws IOException
   */
  public void handleFileUpload( FileUploadEvent event )
  {
    FileTO newImage = new FileTO();
    super.fillSessionData( newImage );
    newImage.setId( selectedMovie.getIdMovieImage() );
    newImage.setName( event.getFile().getFileName() );
    newImage.setFile( event.getFile().getContents() );
    serviceAdminMovieIntegratorEJB.saveMovieImage( newImage );
    selectedMovie.setIdMovieImage( newImage.getId() );
    setRandom( Math.random() );
    createMessageSuccessWithMsg( "movies.savedImage.succes" );
  }

  /**
   * @return the selectedMovie
   */
  public EventMovieTO getSelectedMovie()
  {
    return selectedMovie;
  }

  /**
   * @param selectedMovie the selectedMovie to set
   */
  public void setSelectedMovie( EventMovieTO selectedMovie )
  {

    this.selectedMovie = selectedMovie;

  }

  /**
   * @return the random
   */
  public double getRandom()
  {
    return random;
  }

  /**
   * @param random the random to set
   */
  public void setRandom( double random )
  {
    this.random = random;
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
