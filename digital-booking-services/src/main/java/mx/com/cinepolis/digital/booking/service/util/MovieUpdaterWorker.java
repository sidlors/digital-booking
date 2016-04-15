package mx.com.cinepolis.digital.booking.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.dao.util.DistributorDOToDistributorTOTransformer;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfFormato;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Formato;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Pelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.Consumo;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.IConsumo;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.certum.commons.lang.time.DateUtils;

/**
 * Class for updating movies asynchronously
 * 
 * @author gsegura
 * @since 0.3.0
 */
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MovieUpdaterWorker implements Runnable
{

  private static final Integer DISTRIBUTOR_UNDEFINED = 2;

  private static final Logger LOG = LoggerFactory.getLogger( MovieUpdaterWorker.class );

  private static final String REG_EX_DURATION = "[a-zA-Z ]";
  private static final String REG_EX_FORMAT = "(?i)\\s(dob|esp|sub)";
  private static final String PATTERN_COUNTRY = ".*\\(.*";
  private static final String SPLIT_ONE = "\\(";
  private static final String SPLIT_TWO = ",";

  private static final String LOG_EVENT_MOVIE_FORMAT = ": ERROR - The premiere does haven't associated format.";
  private static final String LOG_EVENT_MOVIE_NAME_FORMAT = ": Info, The premiere doesn't have a name associated format";
  private static final String LOG_EVENT_MOVIE_IMAGE = "ERROR to connect ws to synchronize movie images";
  private static final String LOG_ID_DISTRIBUTOR_UNDEFINED = "The record is discarded because it has an indefinite distributor";
  private static final String LOG_EVENT_MOVIE_ERROR = "ERROR - The premiere: "; 
  private static final String LOG_EVENT_MOVIE_IDVISTA = ", has a idVista: ";
  private static final String LOG_EVENT_MOVIE_IDVISTA_REPEATED = " associated with another premiere.";
  private static final String LOG_MOVIE_FORMAT = "The format of the premiere: ";
  private static final String LOG_MOVIE_IDVISTA = ", with idVista: ";
  private static final String LOG_FORMAT_NOT_UPDATE = " is not updated because it is programmed.";
  
  private Pelicula pelicula;
  private EventDAO eventDAO;
  private MovieImageDAO movieImageDAO;
  private IConsumo iConsumo;
  private Map<String, Integer> mapFormats;
  private CatalogTO defaultMovieFormat;
  private CatalogTO defaultSoundFormat;
  private DistributorDAO distributorDAO;
  private DistributorTO defaultDistributorTO;
  private String urlCartel;
  private Set<String> vistaIds;
  private List<String> listVistaIdsBooked;
  private Long idDistAltContent;

 /**
  * Constructor
  */
  public MovieUpdaterWorker()
  {
    Consumo consumo = new Consumo();
    iConsumo = consumo.getBasic();
  }

  /**
   * Synchronize the movies from the webservice into the database
   */
  @Override
  public void run()
  {
    EventMovieTO eventMovieTO = null;
    StringBuilder name;
    StringBuilder error;
    
    // Validar que la distribuidora sea diferente de 2
    if( pelicula.getIdDistribuidora() != null && !pelicula.getIdDistribuidora().equals( DISTRIBUTOR_UNDEFINED ) )
    {
      eventMovieTO = new EventMovieTO();
      // Obtener datos de película
      getMovieDetails( pelicula, eventMovieTO );
      error = new StringBuilder( pelicula.getTitulo()
          .getValue() );
      // Obtener formatos
      ArrayOfFormato arrayOfFormato = iConsumo.obtenerFormatos( pelicula.getId(), null );
      if( arrayOfFormato != null )
      {
        //Por cada formato generar una pelicula
        for( Formato formato : arrayOfFormato.getFormato() )
        {
          //Validar que no se sincronicen peliculas con id de vista repetido.
          if( !existsIdVista( formato.getIdPeliculaVista().getValue() ) )
          {
            //Se agrega idVista a la película
            eventMovieTO.setIdVista( formato.getIdPeliculaVista().getValue() );
            //Se obtiene nombre de la película
            name = new StringBuilder( pelicula.getTitulo().getValue().trim() );
            error = new StringBuilder( name );
            String movieFormat = "";
            
            //Validar que el nombre del formato de pelicula no este vacio
            if( !formato.getNombre().getValue().isEmpty() )
            {
              //Se concatena formato al nombre de la película
              name.append( " " ).append( formato.getNombre().getValue().trim() );
              //Se obtiene el formato de película de IA.
              movieFormat = formato.getNombre().getValue().replaceAll( REG_EX_FORMAT, "" );
            }
            else
            {
              LOG.info( error.append( LOG_EVENT_MOVIE_NAME_FORMAT ).toString() );
            }
            
            //Se agrega nombre de película
            eventMovieTO.setDsEventName( name.toString() );
            //Se valida por medio del idVista, que la pelicula no esté programada
            if( CollectionUtils.isEmpty( listVistaIdsBooked )
                || !listVistaIdsBooked.contains( formato.getIdPeliculaVista().getValue() ) )
            {
              //Se obtiene de la BD el formato de película correspondiente al obtenido de IA.
              eventMovieTO.setMovieFormats( getMovieFormats( mapFormats.get(  movieFormat.toUpperCase().trim() ) ) );
            }
            else 
            {
              error = new StringBuilder( LOG_MOVIE_FORMAT );
              error.append( pelicula.getTitulo().getValue() )
              .append( LOG_MOVIE_IDVISTA ).append( formato.getIdPeliculaVista().getValue() )
                  .append( LOG_FORMAT_NOT_UPDATE );
              LOG.info( error.toString() );
            }
            
            //Se obtiene el sonido default de la película
            eventMovieTO.setSoundFormats( getMovieSoundFormats() );
            //Se setea en CodeDBS el idVista de la película.
            eventMovieTO.setCodeDBS( formato.getIdPeliculaVista()
                .getValue() );
            // Guardar o actualizar el estreno
            saveOrUpdateEventMovie( eventMovieTO );

            //Se setean los datos de IA para el Log.
            String messageReceived = CinepolisUtils.buildStringUsingMutable( "{ id:", pelicula.getId(), ", idVista:",
                formato.getIdPeliculaVista()
                    .getValue() + ", idDistribuidora:", pelicula.getIdDistribuidora(), ", titulo:'",
                pelicula.getTitulo()
                    .getValue(), "', formato:'", formato.getNombre()
                    .getValue(), "' }\n" );
            
            //Se setean los datos guardados para el Log. 
            String dataSaved = CinepolisUtils.buildStringUsingMutable(
                "{ idEvent:", eventMovieTO.getIdEvent(),
                ", idVista:", eventMovieTO.getIdVista(),
                ", distributorId:", eventMovieTO.getDistributor().getId(),
                ", dsEventName:'", eventMovieTO.getDsEventName(),  
                "', movieFormatId:",
                CollectionUtils.isNotEmpty( eventMovieTO.getMovieFormats() ) ? CinepolisUtils.findFirstElement(
                    eventMovieTO.getMovieFormats() )
                    .getId() : "NA",
                ", movieFormat: '", movieFormat, "' }" );

            LOG.info( CinepolisUtils.buildStringUsingMutable( "\nReceived: ", messageReceived, "Saved: ", dataSaved ) );
          }
          else
          {
            error = new StringBuilder( LOG_EVENT_MOVIE_ERROR );
            error.append( pelicula.getTitulo().getValue().trim() ).append( LOG_EVENT_MOVIE_IDVISTA )
                .append( formato.getIdPeliculaVista().getValue() ).append( LOG_EVENT_MOVIE_IDVISTA_REPEATED );
            LOG.error( error.toString() );
          }
        }
      }
      else
      {
        LOG.error( error.append( LOG_EVENT_MOVIE_FORMAT )
            .toString() );
      }
    }
    else
    {
      LOG.info( LOG_ID_DISTRIBUTOR_UNDEFINED );
    }

  }

  /**
   * @param pelicula the pelicula to set
   */
  public void setPelicula( Pelicula pelicula )
  {
    this.pelicula = pelicula;
  }

  /**
   * @param eventDAO the eventDAO to set
   */
  public void setEventDAO( EventDAO eventDAO )
  {
    this.eventDAO = eventDAO;
  }

  /**
   * @param movieImageDAO the movieImageDAO to set
   */
  public void setMovieImageDAO( MovieImageDAO movieImageDAO )
  {
    this.movieImageDAO = movieImageDAO;
  }

  /**
   * @param distributorDAO the distributorDAO to set
   */
  public void setDistributorDAO( DistributorDAO distributorDAO )
  {
    this.distributorDAO = distributorDAO;
  }

  /**
   * @param mapFormats the mapFormats to set
   */
  public void setMapFormats( Map<String, Integer> mapFormats )
  {
    this.mapFormats = mapFormats;
  }

  /**
   * @param defaultMovieFormat the defaultMovieFormat to set
   */
  public void setDefaultMovieFormat( CatalogTO defaultMovieFormat )
  {
    this.defaultMovieFormat = defaultMovieFormat;
  }

  /**
   * @param defaultSoundFormat the defaultSoundFormat to set
   */
  public void setDefaultSoundFormat( CatalogTO defaultSoundFormat )
  {
    this.defaultSoundFormat = defaultSoundFormat;
  }

  /**
   * @param defaultDistributorTO the defaultDistributorTO to set
   */
  public void setDefaultDistributorTO( DistributorTO defaultDistributorTO )
  {
    this.defaultDistributorTO = defaultDistributorTO;
  }

  /**
   * @param urlCartel the urlCartel to set
   */
  public void setUrlCartel( String urlCartel )
  {
    this.urlCartel = urlCartel;
  }

  /**
   * @param idDistAltContent the idDistAltContent to set
   */
  public void setIdDistAltContent( Long idDistAltContent )
  {
    this.idDistAltContent = idDistAltContent;
  }

  /**
   * @param vistaIds the vistaIds to set
   */
  public void setVistaIds( Set<String> vistaIds )
  {
    this.vistaIds = vistaIds;
  }

  /**
   * @return the listVistaIdsBooked
   */
  public List<String> getListVistaIdsBooked()
  {
    return listVistaIdsBooked;
  }

  /**
   * @param listVistaIdsBooked the listVistaIdsBooked to set
   */
  public void setListVistaIdsBooked( List<String> listVistaIdsBooked )
  {
    this.listVistaIdsBooked = listVistaIdsBooked;
  }

  /**
   * Método que setea los datos de la pelicula a EventMovieTO.
   * 
   * @param pelicula
   * @return
   */
  private void getMovieDetails( Pelicula pelicula, EventMovieTO eventMovieTO )
  {
    ValidatorUtil.validateMovie( pelicula );
    eventMovieTO.setDtRelease( DateUtils.parseDate( pelicula.getFechaEstreno()
        .getValue() ) );
    eventMovieTO.setDsDirector( pelicula.getDirector()
        .getValue() );
    eventMovieTO.setDsSynopsis( pelicula.getSinopsis()
        .getValue() );
    eventMovieTO.setDsRating( pelicula.getClasificacion()
        .getValue() );
    eventMovieTO.setDsGenre( pelicula.getGenero()
        .getValue() );
    eventMovieTO.setDsOriginalName( pelicula.getTituloOriginal()
        .getValue() );
    eventMovieTO.setDsActor( pelicula.getActores()
        .getValue() );
    eventMovieTO.setDistributor( getMovieDistributor( pelicula.getIdDistribuidora() ) );
    eventMovieTO.setDuration( getLengthMovie( pelicula.getDuracion().getValue() ) );
    
    eventMovieTO.setCurrentMovie( true );
    eventMovieTO.setFgActiveIa( true );
    eventMovieTO.setDsCountry( getMovieCountry( pelicula.getTituloOriginal().getValue() ) );
    eventMovieTO.setUserId( 1L );
  }

  /**
   * Método que obtiene la distrubuidora asociada a una película.
   * 
   * @param idDistributor
   * @return
   */
  private DistributorTO getMovieDistributor( Integer idDistributor )
  {
    DistributorTO distributorTO = null;
    List<DistributorDO> distributors = distributorDAO.findByIdVistaAndActive( idDistributor.toString() );
    if( CollectionUtils.isNotEmpty( distributors ) )
    {
      distributorTO = (DistributorTO) new DistributorDOToDistributorTOTransformer().transform( distributors.get( 0 ) );
    }
    if( distributorTO == null )
    {
      distributorTO = this.defaultDistributorTO;
    }
    return distributorTO;
  }
  
  /**
   * Método que retorna falso si
   * la distribuidora es de contenido alternativo.
   * 
   * @param idDistributor
   * @return
   */
  private boolean getValueOfPremiere( Long idDistributor, EventMovieTO eventMovieTO )
  {
    boolean distAltContent = false;
    if( idDistributor.equals( idDistAltContent ) )
    {
      eventMovieTO.setFgAlternateContent( true );
      distAltContent = true;
    }
    return distAltContent;
  }

  /**
   * Método que obtiene la duración de una película.
   * 
   * @param duration
   * @return
   */
  private Integer getLengthMovie( String duration )
  {
    Integer record = 0;
    for( String time : duration.split( REG_EX_DURATION ) )
    {
      if( !time.isEmpty() )
      {
        record = (Integer.valueOf( time ));
        break;
      }
    }
    return record;
  }

  /**
   * Método que obtiene el país de origen de una película.
   * 
   * @param country
   * @return
   */
  private String getMovieCountry( String country )
  {
    String dsCountry = "";
    Pattern pat = Pattern.compile( PATTERN_COUNTRY );
    Matcher mat = pat.matcher( country );
    if( mat.matches() )
    {
      dsCountry = country.split( SPLIT_ONE )[1].split( SPLIT_TWO )[0];
    }
    return dsCountry;
  }

  /**
   * Método que obtiene las imagenes de las peliculas y las guarda en base de
   * datos.
   * 
   * @param cartel
   * @return
   */
  private Long getIdMovieImage( String cartel, Long idImage )
  {
    Long idMovieImage = idImage;
    File file;
    if( !cartel.isEmpty() && !urlCartel.isEmpty() )
    {
      try
      {
        StringBuilder url = null;
        url = new StringBuilder( urlCartel );
        url.append( cartel );
        URL imageUrl = new URL( url.toString() );
        InputStream input = imageUrl.openStream();
        file = File.createTempFile( cartel, ".jpg" );
        FileOutputStream fileOutputStream = new FileOutputStream( file );
        fileOutputStream.write( IOUtils.toByteArray( input ) );
        fileOutputStream.flush();
        fileOutputStream.close();
        byte[] image = IOUtils.toByteArray( new FileInputStream( file ) );
        FileTO fileTO = new FileTO();
        fileTO.setName( cartel );
        fileTO.setFile( image );
        fileTO.setId( idImage );
        ValidatorUtil.validateFileTO( fileTO );
        movieImageDAO.saveUploadedMovieImage( fileTO );
        idMovieImage = fileTO.getId();
      }
      catch( Exception e )
      {
        LOG.error( LOG_EVENT_MOVIE_IMAGE, e );
        if( idImage == null )
        {
          idMovieImage = createDummyImage();
        }
      }
    }
    else if( idImage == null )
    {
      idMovieImage = createDummyImage();
    }
    return idMovieImage;
  }

  /**
   * Método que genera la imagen Dummy por default
   * 
   * @param idImage
   * @return
   */
  private Long createDummyImage()
  {
    Long idMovieImage = null;
    File file;

    InputStream input = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream( "noimage.png" );
    try
    {
      file = File.createTempFile( "noimage", "png" );
      FileOutputStream fileOutputStream = new FileOutputStream( file );
      fileOutputStream.write( IOUtils.toByteArray( input ) );
      fileOutputStream.flush();
      fileOutputStream.close();
      byte[] image = IOUtils.toByteArray( new FileInputStream( file ) );
      FileTO fileTO = new FileTO();
      fileTO.setName( "noimage.png" );
      fileTO.setFile( image );
      ValidatorUtil.validateFileTO( fileTO );
      movieImageDAO.saveUploadedMovieImage( fileTO );
      idMovieImage = fileTO.getId();
    }
    catch( IOException e )
    {
      LOG.error( e.getMessage(), e );
    }
    return idMovieImage;
  }

  /**
   * Método que guarda o actuliza los estrenos.
   * 
   * @param eventMovieTO
   */
  private void saveOrUpdateEventMovie( EventMovieTO eventMovieTO )
  {
    try
    {
      if( ValidatorUtil.validatEventMove( eventMovieTO ) )
      {
        List<EventDO> eventDOs = eventDAO.findByIdVistaAndActive( eventMovieTO.getIdVista() );
        if( CollectionUtils.isEmpty( eventDOs ) )
        {
          // Obtener imagen
          eventMovieTO.setIdMovieImage( getIdMovieImage( pelicula.getCartel().getValue(), null ) );
          eventMovieTO.setPremiere( getValueOfPremiere( eventMovieTO.getDistributor().getId(), eventMovieTO ) );
          eventDAO.save( eventMovieTO, idDistAltContent );
        }
        else
        {
          EventDO eventDO = eventDOs.get( 0 );
          eventMovieTO.setIdEvent( eventDO.getIdEvent() );
          // Obtener imagen
          eventMovieTO.setIdMovieImage( getIdMovieImage( pelicula.getCartel()
              .getValue(), eventDO.getEventMovieDOList()
              .get( 0 )
              .getIdMovieImage() ) );
          eventMovieTO.setPrerelease( eventDO.isFgPrerelease() );
          eventMovieTO.setFestival( eventDO.isFgFestival() );
          eventMovieTO.setFgAlternateContent( eventDO.isFgAlternateContent() );
          if( !eventMovieTO.isFgAlternateContent() && !eventMovieTO.isFestival() )
          {
            eventMovieTO.setPremiere( true );
          }
          eventDAO.update( eventMovieTO, idDistAltContent );
        }
      }
    }
    catch( DigitalBookingException e )
    {
      LOG.warn( e.getMessage(), e );
    }
  }

  /**
   * Método que obtiene los formatos de sonido.
   * 
   * @return
   */
  private List<CatalogTO> getMovieSoundFormats()
  {
    List<CatalogTO> categorySound = new ArrayList<CatalogTO>();
    categorySound.add( defaultSoundFormat );
    return categorySound;
  }

  /**
   * Método que setea el formato de la película
   * 
   * @param movieFormat
   * @return
   */
  private List<CatalogTO> getMovieFormats( Integer idMovieFormat )
  {
    List<CatalogTO> categoryMovieFormat = new ArrayList<CatalogTO>();
    if( idMovieFormat == null )
    {
      categoryMovieFormat.add( defaultMovieFormat );
    }
    else
    {
      CatalogTO catalogTO = new CatalogTO();
      catalogTO.setId( idMovieFormat.longValue() );
      categoryMovieFormat.add( catalogTO );
    }
    return categoryMovieFormat;
  }

  /**
   * Método que valida que no se sincronicen peliculas con id de vista repetido.
   * 
   * @param idVista
   * @return
   */
  private synchronized boolean existsIdVista( String idVista )
  {
    boolean exists = this.vistaIds.contains( idVista );
    if( !exists )
    {
      this.vistaIds.add( idVista );
    }
    return exists;
  }
}
