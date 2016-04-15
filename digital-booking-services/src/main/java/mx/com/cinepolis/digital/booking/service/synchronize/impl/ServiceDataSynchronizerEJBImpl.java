package mx.com.cinepolis.digital.booking.service.synchronize.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CityDOToCityTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CountryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.DistributorDOToDistributorTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.StateDOToStateTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.MovieFormatDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfCiudad;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfComplejo;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfDistribuidora;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfEstado;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPais;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfRuta;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Ciudad;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Complejo;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Distribuidora;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Estado;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Pais;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Pelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Ruta;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.Consumo;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.IConsumo;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieFormatDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.MovieUpdaterWorker;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implemeta los metodos relacionados a un cine
 * 
 * @author rgarcia
 */
@Stateless(name = "ServiceDataSynchronizerEJB", mappedName = "ejb/ServiceDataSynchronizerEJB")
@Remote(ServiceDataSynchronizerEJB.class)
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceDataSynchronizerEJBImpl implements ServiceDataSynchronizerEJB
{
  private static final Logger LOG = LoggerFactory.getLogger( ServiceDataSynchronizerEJBImpl.class );
  private static final Long ID_REGION_DEFAULT = -1L;
  private static final Long ID_DEFAULT_USER = 1L;
  private static final String QT_CHARACTERS_IN_SHORT_NAME = "10";
  private static final String WS_FORMAT = "";
  private static final String WS_ID_COMPLEJOS = "0";
  private static final String LOG_EVENT_MOVIE_SYNCHRONIZE = "Error to connect ws to synchronize EventMovies information";
  private static final String URL_TYPE = "Cartel";

  private IConsumo iConsumo;

  @EJB
  private DistributorDAO distributorDAO;

  @EJB
  private CountryDAO countryDAO;
  
  @EJB
  private CityDAO cityDAO;
  
  @EJB
  private StateDAO stateDAO;
  
  @EJB
  private TheaterDAO theaterDAO;
  
  @EJB
  private RegionDAO regionDAO;
  
  @EJB
  private ConfigurationDAO configurationDAO;
    
  @EJB
  private EventDAO eventDAO;
  
  @EJB
  private MovieImageDAO movieImageDAO;
  
  @EJB
  private MovieFormatDAO movieFormatDAO;
  
  @EJB
  private BookingDAO bookingDAO;

  /**
   * Método que inicializa la invocación al WS.
   */
  private void initializeService()
  {
    Consumo consumo = new Consumo();
    iConsumo = consumo.getBasic();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeDistributors()
  {
    LOG.debug( "INICIANDO: synchronizeDistributors ...  " );
    initializeService();
    ArrayOfDistribuidora arrayOfDistributors = null;
    try
    {
      arrayOfDistributors = iConsumo.obtenerDistribuidoras( WS_FORMAT );
    }
    catch( Exception ex )
    {
      LOG.error( "Error to connect ws to synchronize distributors information", ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex );
    }
    DistributorTO distributorTO = null;
    DistributorDO distributorDO = null;
    List<Distribuidora> newDistributors = arrayOfDistributors.getDistribuidora();
    // Se construye la informacion de los distribuidores
    for( Distribuidora distributor : newDistributors )
    {
      
      List<DistributorDO> currentDistributors = distributorDAO.findByIdVistaAndActive( String.valueOf( distributor.getId()  ));
      
      
      if( CollectionUtils.isNotEmpty( currentDistributors ) )
      {
        distributorDO = currentDistributors.get( 0 );
        distributorTO =  (DistributorTO) new DistributorDOToDistributorTOTransformer(  ).transform( distributorDO );
        distributorTO.setName( distributor.getNombre().getValue() );
        // Se deja el nombre corto actual
        distributorTO.setShortName( distributorDO.getDsShortName() );
        // Si aparece en el catalogo quiere decir que es activa
        distributorTO.setFgActive( true );
        distributorTO.setUserId( ID_DEFAULT_USER );
        // Se actualiza la información de la distribuidora
        distributorDAO.update( distributorTO );
      }
      else
      {
        //Se valida si ya existe por nombre de la distribuidora, si existe solo se actualiza el id de vista y el estado
        currentDistributors = distributorDAO.findByDsNameActive( distributor.getNombre().getValue() );
        if( CollectionUtils.isNotEmpty( currentDistributors ) )
        {
          distributorDO = currentDistributors.get( 0 );
          distributorTO =  (DistributorTO) new DistributorDOToDistributorTOTransformer(  ).transform( distributorDO );
          //Se actualiza el ID de vista
          distributorTO.setIdVista( String.valueOf( distributor.getId() ) );
          // Se deja el nombre corto actual
          distributorTO.setShortName( distributorDO.getDsShortName() );
          // Si aparece en el catalogo quiere decir que es activa
          distributorTO.setFgActive( true );
          distributorTO.setUserId( ID_DEFAULT_USER );
          // Se actualiza la información de la distribuidora
          distributorDAO.update( distributorTO );
        }
        else
        {
          distributorTO = new DistributorTO();
          distributorTO.setName( distributor.getNombre().getValue() );
          distributorTO.setIdVista( String.valueOf( distributor.getId() ) );
          // Si aparece en el catalogo quiere decir que es activa
          distributorTO.setFgActive( true );
          //Se toma el id del usuario default
          distributorTO.setUserId( ID_DEFAULT_USER );
          if( distributorTO.getName() != null )
          {
            // Si el nombre es mayor a 10 caracteres, se corta. Si no se pone
            // completo.
            distributorTO.setShortName( (distributorTO.getName().length() > 10) ? distributorTO.getName()
                .substring( 0, Integer.valueOf( QT_CHARACTERS_IN_SHORT_NAME ) ) : distributorTO.getName() );
          }
          // Se agrega una nueva distribuidora
          distributorDAO.save( distributorTO );
        }
      }
       
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeCountries( Language language )
  {
    LOG.debug( "INICIANDO : synchronizeCountries ..." );
    initializeService();
    ArrayOfPais arrayOfCountries = null;
    try
    {
      arrayOfCountries = iConsumo.obtenerPaises( WS_FORMAT );
    }
    catch( Exception ex )
    {
      LOG.error( "Error to connect ws to synchronize countries information", ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex );
    }
    CatalogTO countryTO = null;
    CountryDO countryDO = null;
    List<Pais> newCountries = arrayOfCountries.getPais();
    // Se construye la informacion de los paises
    for( Pais country : newCountries )
    {
      
      List<CountryDO> currentCountries = this.countryDAO.findByIdVista( String.valueOf( country.getId() ) );
      
      if( CollectionUtils.isNotEmpty( currentCountries ) )
      {
        countryDO = currentCountries.get( 0 );
        countryTO =  (CatalogTO) new CountryDOToCatalogTOTransformer( language ).transform( countryDO );
        countryTO.setName( country.getNombre().getValue() );
        countryTO.setId( Long.valueOf( countryDO.getIdCountry() ) );
        // Si aparece en el catalogo quiere decir que es activa
        countryTO.setFgActive( true );
        countryTO.setUserId( ID_DEFAULT_USER );
        // Se actualiza la información del pais
        countryDAO.update( countryTO, language );
      }
      else
      {
        countryTO = new CatalogTO();
        countryTO.setName( country.getNombre().getValue() );
        countryTO.setIdVista( String.valueOf( country.getId() ) );
        countryTO.setIdLanguage( Long.valueOf( language.getId() ) );
        //Se toma el id del usuario default
        countryTO.setUserId( ID_DEFAULT_USER );
        //Si aparece en el catalogo quiere decir que es activa
        countryTO.setFgActive( true );
        // Se agrega un pais
        countryDAO.save( countryTO );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeCities( Long countryId, Language language )
  {
    LOG.debug( "INICIANDO : synchronizeCities ..." );
    initializeService();
    ArrayOfCiudad arrayOfCities = null;
    try
    {
      arrayOfCities = iConsumo.obtenerCiudadesPais( countryId.intValue(), WS_FORMAT );
    }
    catch( Exception ex )
    {
      LOG.error( "Error to connect ws to synchronize cities information", ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex );
    }
    CityTO cityTO = null;
    CityDO cityDO = null;
    List<Ciudad> newCities = arrayOfCities.getCiudad();
    // Se construye la informacion de los paises
    for( Ciudad city : newCities )
    {
      
      List<CityDO> currentCities = cityDAO.findByIdVista( String.valueOf( city.getId() ) );
      if( CollectionUtils.isNotEmpty( currentCities ) )
      {
        cityDO = currentCities.get( 0 );
        // jreyesv -> Was: cityTO =  (CatalogTO) new CityDOToCatalogTOTransformer( language ).transform( cityDO );
        cityTO =  (CityTO) new CityDOToCityTOTransformer( language ).transform( cityDO );
        cityTO.setId( Long.valueOf( cityDO.getIdCity() ) );
        cityTO.setName( city.getNombre().getValue() );
        // Si aparece en el catalogo quiere decir que es activa
        cityTO.setFgActive( true );
        cityTO.setUserId( ID_DEFAULT_USER );
        // Se actualiza la información del pais
        cityDAO.update( cityTO, language );
      }
      else
      {
        // jreyesv -> was: new CatalogTO();
        cityTO = new CityTO();
        cityTO.setName( city.getNombre().getValue() );
        cityTO.setIdVista( String.valueOf( city.getId() ) );
        cityTO.setIdLanguage( Long.valueOf( language.getId() ) );
        // Si aparece en el catalogo quiere decir que es activa
        cityTO.setFgActive( true );
        //Se toma el id del usuario default
        cityTO.setUserId( ID_DEFAULT_USER );
        // jreyesv -> Se agrega el país a la ciudad
        cityTO.setCountry( new CatalogTO( countryId ) );
        // Se agrega una ciudad
        cityDAO.save( cityTO, language );
        // cityDAO.save( cityTO, null, null );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void synchronizeStates( Long countryId, Language language )
  {
    LOG.debug( "INICIANDO : synchronizeStates ..." );
    initializeService();
    ArrayOfEstado arrayOfStates = null;
    try
    {
      arrayOfStates = iConsumo.obtenerEstados( countryId.intValue(), WS_FORMAT );
    }
    catch( Exception ex )
    {
      LOG.error( "Error to connect ws to synchronize states information", ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex);
    }
    CatalogTO catalogTO = null;
    StateDO stateDO = null;
    StateTO<CatalogTO, Number> stateTO = null;
    List<Estado> newStates = arrayOfStates.getEstado();
    // Se construye la informacion de los paises
    for( Estado state : newStates )
    {
      List<StateDO> currentStates = stateDAO.findByIdVista( String.valueOf( state.getId() ) );
      if( CollectionUtils.isNotEmpty( currentStates ) )
      {
        stateDO = currentStates.get( 0 );
        stateTO =  (StateTO<CatalogTO, Number>) new StateDOToStateTOTransformer( language ).transform( stateDO );
        stateTO.getCatalogState().setName( state.getNombre().getValue() );
        // Si aparece en el catalogo quiere decir que es activa
        stateTO.getCatalogState().setFgActive( true );
        catalogTO.setUserId( ID_DEFAULT_USER );
        // Se actualiza la información del pais
        stateDAO.update( stateTO, language );
      }
      else
      {
        catalogTO = new CatalogTO();
        catalogTO.setName( state.getNombre().getValue() );
        catalogTO.setIdVista( String.valueOf( state.getId() ) );
        catalogTO.setIdLanguage( Long.valueOf( language.getId() ) );
        // Si aparece en el catalogo quiere decir que es activa
        catalogTO.setFgActive( true );
        //Se toma el id del usuario default
        catalogTO.setUserId( ID_DEFAULT_USER );
        stateTO = new StateTO<CatalogTO, Number>( catalogTO, countryId );
        // Se agrega una ciudad
        stateDAO.save( stateTO );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeEventMovies()
  {
    LOG.debug( "INICIANDO: synchronizeTheaters... " );
    initializeService();
    String idCountry = this.configurationDAO.findByParameterName( Configuration.ID_COUNTRY_MX.getParameter() )
        .getDsValue();
    try
    {
      ArrayOfPelicula arrayOfPelicula = iConsumo.obtenerPeliculasEstreno( idCountry, WS_FORMAT );
      if( arrayOfPelicula != null )
      {
        //Obtiene la lista de estrenos programados para evitar la modificación de sus formatos
        List<String> listVistaIdsBooked = this.bookingDAO.findPremiereBooking();
        
        //Actualizar fgPremiere a false
        this.eventDAO.updatePremiere();
        
        //Se obtiene el formato default de película
        String idDefaultMovieFormat = this.configurationDAO.findByParameterName(
          Configuration.ID_CATEGORY_MOVIE_FORMAT.getParameter() ).getDsValue();
        CatalogTO defaultMovieFormat = new CatalogTO();
        defaultMovieFormat.setId( Long.valueOf( idDefaultMovieFormat ) );
        
        //Se obtiene el sonido default de película
        String idDefaultSoundFormat = this.configurationDAO
            .findByParameterName( Configuration.ID_CATEGORY_MOVIE_SOUND.getParameter() ).getDsValue();
        CatalogTO defaultSoundFormat = new CatalogTO();
        defaultSoundFormat.setId( Long.valueOf( idDefaultSoundFormat ) );
        
        //Se obtiene distribuidor default de película
        String idCategory = this.configurationDAO.findByParameterName(
          Configuration.ID_DEFAULT_DISTRIBUTOR.getParameter() ).getDsValue();
        DistributorTO defaultDistributorTO = distributorDAO.get( Integer.valueOf( idCategory ) );
        
        //Se obtiene id de distribuidor de contenido alternativo
        String idAltContent = this.configurationDAO.findByParameterName(
          Configuration.ID_DISTRIBUTOR_ALTERNATIVE_CONTENT.getParameter() ).getDsValue();
        long idDistAltContent = Long.valueOf( idAltContent );
        
        //Se obtiene url de tipo Cartel
        ArrayOfRuta arrayOfRuta = iConsumo.obtenerRutas( null, null );
        String url = null;
        if( arrayOfRuta != null )
        {
          for( Ruta ruta : arrayOfRuta.getRuta() )
          {
            if( ruta.getTipo().getValue().equals( URL_TYPE ) )
            {
              url = ruta.getUrl().getValue();
              break;
            }
          }
        }
        
        //Se obtienen las variaciones de formatos de IA
        Map<String, Integer> mapFormats = new HashMap<String, Integer>();
        List<MovieFormatDO> formatDOs = this.movieFormatDAO.findAll();
        if(!formatDOs.isEmpty())
        {
          for( MovieFormatDO movieFormatDO : formatDOs )
          {
            mapFormats.put( movieFormatDO.getDsName(), movieFormatDO.getIdCategory());
          }
        }

        //Variable que contendrá los id´s de vista para validar que no se repitan
        Set<String> vistaIds = new ConcurrentSkipListSet <String>();
        List<MovieUpdaterWorker> workers = new ArrayList<MovieUpdaterWorker>();
        for( Pelicula pelicula : arrayOfPelicula.getPelicula() )
        {
          String legend = CinepolisUtils.buildStringUsingMutable( "Creando MovieUpdaterWorker de la pelicula ",
            pelicula.getTitulo().getValue() );
          LOG.info( legend );
          MovieUpdaterWorker movieUpdaterWorker = new MovieUpdaterWorker();
          movieUpdaterWorker.setDistributorDAO( distributorDAO );
          movieUpdaterWorker.setEventDAO( eventDAO );
          movieUpdaterWorker.setMovieImageDAO( movieImageDAO );
          movieUpdaterWorker.setMapFormats( mapFormats );
          movieUpdaterWorker.setPelicula( pelicula );
          movieUpdaterWorker.setDefaultMovieFormat( defaultMovieFormat );
          movieUpdaterWorker.setDefaultSoundFormat( defaultSoundFormat );
          movieUpdaterWorker.setDefaultDistributorTO( defaultDistributorTO );
          movieUpdaterWorker.setUrlCartel( url );
          movieUpdaterWorker.setIdDistAltContent( idDistAltContent );
          movieUpdaterWorker.setVistaIds( vistaIds );
          movieUpdaterWorker.setListVistaIdsBooked( listVistaIdsBooked );
          workers.add( movieUpdaterWorker );
        }
        executeWorkers( workers );
      }
    }
    catch( Exception ex )
    {
      LOG.error( LOG_EVENT_MOVIE_SYNCHRONIZE, ex.getMessage(), ex );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex );
    }
  }

  private void executeWorkers( List<MovieUpdaterWorker> workers )
  {
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor( 2, workers.size(), 30, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>( 2 ), threadFactory );
    for( MovieUpdaterWorker movieUpdaterWorker : workers )
    {
      executorPool.execute( movieUpdaterWorker );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeTheaters( Language language )
  {
    LOG.debug( "INICIANDO: synchronizeTheaters... " );
    initializeService();
    ArrayOfComplejo arrayOfComplejo = null;
    TheaterDO theaterDO = null;
    TheaterTO theaterTO = null;
    StateTO<CatalogTO, Integer> stateTO = null;
    List<CatalogTO> countries = countryDAO.getAll( language );
    for( CatalogTO country : countries )
    {
      List<CityDO> cities = cityDAO.findByIdCountry( country.getId().longValue() );
      for( CityDO city : cities )
      {
        try
        {
          // Se obtiene los complejos para la ciudad indicada
          arrayOfComplejo = iConsumo.obtenerComplejos( city.getIdVista(), WS_ID_COMPLEJOS, WS_FORMAT );
          for( Complejo complejo : arrayOfComplejo.getComplejo() )
          {
            List<TheaterDO> theaters = theaterDAO.findByIdVista( complejo.getIdVista().getValue() );

            if( CollectionUtils.isNotEmpty( theaters ) )
            {
              theaterDO = theaters.get( 0 );
              theaterTO = (TheaterTO) new TheaterDOToTheaterTOTransformer( language ).transform( theaterDO );
           
              theaterDO.setNuTheater( Integer.valueOf( complejo.getIdVista().getValue() ) );
              theaterTO.setName( complejo.getNombre().getValue() );
              // TODO RGV Descomentar hasta que el teléfono este actualizado correctamente en el WS
              // theaterTO.setDsTelephone( complejo.getTelefono().getValue() );
              theaterTO.setUserId( ID_DEFAULT_USER );
              theaterDAO.update( theaterTO, language );
            }
            else
            {
              RegionDO regionDO = null;
              theaterTO = new TheaterTO();
              theaterTO.setName( complejo.getNombre().getValue() );
              theaterTO.setIdVista( complejo.getIdVista().getValue() );
              theaterTO.setCity( new CatalogTO( city.getIdCity().longValue() ) );
              //Se toma el id del usuario default
              theaterTO.setUserId( ID_DEFAULT_USER );
              if( CollectionUtils.isNotEmpty( city.getTheaterDOList() ) )
              {
                regionDO = city.getTheaterDOList().get( 0 ).getIdRegion();
              }
              else
              {
                regionDO = regionDAO.find( ID_REGION_DEFAULT.intValue() );
              }
              // Se agrega region por default o se obtiene de los cines de la ciudad
              RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( regionDO
                  .getIdRegion().longValue() ), new CatalogTO( regionDO.getIdTerritory().getIdTerritory().longValue() ) );
              theaterTO.setRegion( region );

              List<StateDO> stateDO = stateDAO.findByIdVista( String.valueOf( complejo.getIdEstado() ) );
              CatalogTO catalogState = new CatalogTO( stateDO.get( 0 ).getIdState().longValue() );
              stateTO = new StateTO<CatalogTO, Integer>( catalogState, country.getId().intValue() );
              theaterTO.setState( stateTO );
              
              theaterDAO.save( theaterTO );
            }

          }
        }
        catch( Exception ex )
        {
          LOG.error( "Error to connect ws to synchronize theaters information", ex );
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_CONNECT_TO_SERVICE, ex );
        }
      }
    }
  }

}
