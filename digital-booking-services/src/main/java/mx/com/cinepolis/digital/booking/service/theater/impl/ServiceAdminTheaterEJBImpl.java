package mx.com.cinepolis.digital.booking.service.theater.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.TheaterDOToTheaterTOSimpleTransformer;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa los metodos asociados a la administración de los cines
 * 
 * @author rgarcia
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminTheaterEJBImpl implements ServiceAdminTheaterEJB
{

  private static final String ID_VISTA_UNDEFINED = "0";
  private static final Logger LOG = LoggerFactory.getLogger( ServiceAdminTheaterEJBImpl.class );

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private ScreenDAO screenDAO;

  @EJB
  private CategoryDAO categoryDAO;

  @EJB
  private UserDAO userDAO;

  @EJB
  private ConfigurationDAO configurationDAO;

  @EJB
  private IncomeSettingsTypeDAO incomeSettingsTypeDAO;

  @EJB
  private IncomeSettingsDAO incomeSettingsDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveTheater( TheaterTO theaterTO )
  {
    ValidatorUtil.validateTheater( theaterTO );
    validateNameAndIdVistaForSaving( theaterTO );
    if( CollectionUtils.isEmpty( theaterDAO.findByNuTheater( theaterTO.getNuTheater() ) ) )
    {
      theaterDAO.save( theaterTO );
      if( theaterTO.getId() != null )
      {
        LOG.debug( "Id Theater: " + theaterTO.getId() );
        for( ScreenTO screenTO : theaterTO.getScreens() )
        {
          AbstractTOUtils.copyElectronicSign( theaterTO, screenTO );
          screenTO.setIdTheater( theaterTO.getId().intValue() );
          ValidatorUtil.validateScreen( screenTO );
          // TODO gsegura, se debe agregar validacion del id de vista para las pantallas ?
          LOG.debug( "Screen: " + screenTO );
          screenDAO.save( screenTO );
        }
        // Saves the income settings data.
        this.saveOrUpdateIncomeSettings( theaterTO );
      }
      Integer idTheater = theaterTO.getId().intValue();
      TheaterDO theaterDO = this.theaterDAO.find( idTheater );
      theaterDO.setScreenDOList( this.screenDAO.findAllActiveByIdCinema( idTheater ) );

      if( theaterDO.getTheaterLanguageDOList().size() == 1 )
      {
        TheaterLanguageDO spanishName = new TheaterLanguageDO();
        spanishName.setIdTheaterLanguage( null );
        spanishName.setIdTheater( theaterDO );
        spanishName.setIdLanguage( new LanguageDO( Language.SPANISH.getId() ) );
        spanishName.setDsName( theaterTO.getName() );
        theaterDO.getTheaterLanguageDOList().add( spanishName );
      }

      theaterDAO.edit( theaterDO );
      theaterDAO.flush();
    }
    else
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_NUM_THEATER_ALREADY_EXISTS );
    }

  }

  /**
   * Method to save or update the income settings for a region.
   * 
   * @param theaterTO, with the theater information to save.
   */
  private void saveOrUpdateIncomeSettings( TheaterTO theaterTO )
  {
    if( CollectionUtils.isNotEmpty( theaterTO.getIncomeSettingsList() ) )
    {
      for( IncomeSettingsTO incomeSettingTO : theaterTO.getIncomeSettingsList() )
      {
        incomeSettingTO.setIdTheater( theaterTO.getId() );
        incomeSettingTO.setUserId( theaterTO.getUserId() );
        this.incomeSettingsDAO.save( incomeSettingTO );
      }
    }
  }

  private void validateNameAndIdVistaForSaving( TheaterTO theaterTO )
  {
    if( CollectionUtils.isNotEmpty( theaterDAO.findByTheaterName( theaterTO.getName() ) ) )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
    }

    if( !theaterTO.getIdVista().equals( ID_VISTA_UNDEFINED ) )
    {
      List<TheaterDO> theaterDOs = theaterDAO.findByIdVistaAndActive( theaterTO.getIdVista() );
      if( !CollectionUtils.isEmpty( theaterDOs ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteTheater( TheaterTO theaterTO )
  {
    ValidatorUtil.validateTheater( theaterTO );
    TheaterDO theatherDO = theaterDAO.find( theaterTO.getId().intValue() );
    if( theatherDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_THEATER.getId() );
    }
    // Cambia el status activo de las configuraciones de ingresos
    if( CollectionUtils.isNotEmpty( theaterTO.getIncomeSettingsList() ) )
    {
      for( IncomeSettingsTO incomeSettingsTO : theaterTO.getIncomeSettingsList() )
      {
        AbstractEntityUtils.copyElectronicSignature( theaterTO, incomeSettingsTO );
        this.incomeSettingsDAO.delete( incomeSettingsTO );
      }
    }
    theaterDAO.delete( theaterTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateTheater( TheaterTO theaterTO )
  {
    ValidatorUtil.validateTheater( theaterTO );

    validateNameAndIdVistaForUpdating( theaterTO );

    TheaterDO theatherDO = theaterDAO.find( theaterTO.getId().intValue() );
    validateTheaterExistence( theaterTO, theatherDO );
    validateTheaterNumber( theaterTO );
    theaterDAO.update( theaterTO );

    List<ScreenDO> screensForRemove = new ArrayList<ScreenDO>();
    List<ScreenTO> screensForUpdate = new ArrayList<ScreenTO>();
    List<ScreenTO> screensForCreate = new ArrayList<ScreenTO>();

    for( ScreenTO screenTO : theaterTO.getScreens() )
    {
      if( screenTO.getId() == null )
      {
        screensForCreate.add( screenTO );
      }
      else
      {
        screensForUpdate.add( screenTO );
      }
    }

    for( ScreenDO screenDO : theatherDO.getScreenDOList() )
    {
      if( !CollectionUtils.exists(
        theaterTO.getScreens(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getId" ),
          PredicateUtils.equalPredicate( screenDO.getIdScreen().longValue() ) ) ) )
      {
        screensForRemove.add( screenDO );
      }
    }

    for( ScreenDO screenDO : screensForRemove )
    {
      this.screenDAO.remove( screenDO );
    }
    for( ScreenTO screenTO : screensForUpdate )
    {
      AbstractTOUtils.copyElectronicSign( theaterTO, screenTO );
      this.screenDAO.update( screenTO );
    }
    for( ScreenTO screenTO : screensForCreate )
    {
      AbstractTOUtils.copyElectronicSign( theaterTO, screenTO );
      screenTO.setIdTheater( theaterTO.getId().intValue() );
      ValidatorUtil.validateScreen( screenTO );
      screenDAO.save( screenTO );
    }

    Integer idTheater = theaterTO.getId().intValue();
    TheaterDO theaterDO = this.theaterDAO.find( idTheater );
    theaterDO.setScreenDOList( this.screenDAO.findAllActiveByIdCinema( idTheater ) );

    updateTheaterNameSpanish( theaterTO, theaterDO );

    theaterDAO.edit( theaterDO );
    theaterDAO.flush();
    // Saves the income settings data.
    this.saveOrUpdateIncomeSettings( theaterTO );
  }

  private void validateTheaterExistence( TheaterTO theaterTO, TheaterDO theatherDO )
  {
    if( theatherDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_THEATER.getId() );
    }
    LOG.debug( "Id Theater: " + theaterTO.getId() );
  }

  private void validateTheaterNumber( TheaterTO theaterTO )
  {
    List<TheaterDO> theaterDOs = theaterDAO.findByNuTheater( theaterTO.getNuTheater() );

    if( !CollectionUtils.isEmpty( theaterDOs ) && !isAllowedRepeated( theaterTO ) )
    {
      for( TheaterDO theaterDO : theaterDOs )
      {
        if( !theaterDO.getIdTheater().equals( theaterTO.getId().intValue() ) )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_NUM_THEATER_ALREADY_EXISTS );
        }
      }
    }
  }

  private void updateTheaterNameSpanish( TheaterTO theaterTO, TheaterDO theaterDO )
  {
    boolean existLanguageSpanish = false;
    for( TheaterLanguageDO theaterLanguageDO : theaterDO.getTheaterLanguageDOList() )
    {
      if( theaterLanguageDO.getIdLanguage().getIdLanguage().equals( Language.SPANISH.getId() ) )
      {
        theaterLanguageDO.setDsName( theaterTO.getName() );
        existLanguageSpanish = true;
      }
    }
    if( !existLanguageSpanish )
    {
      TheaterLanguageDO language = new TheaterLanguageDO();
      language.setDsName( theaterTO.getName() );
      language.setIdLanguage( new LanguageDO( Language.SPANISH.getId() ) );
      language.setIdTheater( theaterDO );
      theaterDO.getTheaterLanguageDOList().add( language );
    }
  }

  private void validateNameAndIdVistaForUpdating( TheaterTO theaterTO )
  {
    List<TheaterDO> theaters = theaterDAO.findByTheaterName( theaterTO.getName() );
    for( TheaterDO theater : theaters )
    {
      if( !theater.getIdTheater().equals( theaterTO.getId().intValue() ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
      }
    }

    if( !theaterTO.getIdVista().equals( ID_VISTA_UNDEFINED ) && !isAllowedRepeated( theaterTO ) )
    {
      List<TheaterDO> theaterDOs = theaterDAO.findByIdVistaAndActive( theaterTO.getIdVista() );
      for( TheaterDO theaterDO : theaterDOs )
      {
        if( !theaterDO.getIdTheater().equals( theaterTO.getId().intValue() ) )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
        }
      }
    }
  }

  private boolean isAllowedRepeated( TheaterTO theaterTO )
  {
    boolean flag = false;

    ConfigurationDO configurationDO = this.configurationDAO
        .findByParameterName( Configuration.THEATERS_ALLOWED_DUPLICATE_ID_VISTA.getParameter() );
    StringTokenizer st = new StringTokenizer( configurationDO.getDsValue(), "," );
    while( st.hasMoreTokens() )
    {
      String theaterId = st.nextToken();
      if( NumberUtils.isNumber( theaterId ) && theaterTO.getId().equals( Long.valueOf( theaterId ) ) )
      {
        flag = true;
        break;
      }
    }

    return flag;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TheaterTO> getAllTheaters()
  {
    List<TheaterDO> theatersDO = theaterDAO.findAll();
    return modifyDataTheaters( theatersDO );

  }

  /**
   * Se actualiza la información de los cines antes de enviarlos
   * 
   * @param theatersDO
   * @return
   */
  private List<TheaterTO> modifyDataTheaters( List<TheaterDO> theatersDO )
  {
    List<TheaterTO> theaters = new ArrayList<TheaterTO>();
    TheaterDOToTheaterTOTransformer transformer = new TheaterDOToTheaterTOTransformer();
    TheaterTO theaterTO = null;
    if( theatersDO != null )
    {
      for( TheaterDO theater : theatersDO )
      {
        theaterTO = (TheaterTO) transformer.transform( theater );
        theaters.add( theaterTO );
      }
    }
    return theaters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<TheaterTO> getCatalogTheaterSummary( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    PagingResponseTO<TheaterTO> pagingResponseTO = theaterDAO.findAllByPaging( pagingRequestTO );
    List<TheaterTO> theatersTO = pagingResponseTO.getElements();
    pagingResponseTO.setElements( theatersTO );
    return pagingResponseTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getMovieFormats()
  {
    return this.categoryDAO.getAllByCategoryType( CategoryType.MOVIE_FORMAT );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getSoundFormats()
  {
    return this.categoryDAO.getAllByCategoryType( CategoryType.SOUND_FORMAT );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getScreenFormats()
  {
    return this.categoryDAO.getAllByCategoryType( CategoryType.SCREEN_FORMAT );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TheaterTO> getTheatersByRegionId( CatalogTO region )
  {
    return this.theaterDAO.findByRegionId( region );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TheaterTO> getMyTheaters( AbstractTO abstractTO )
  {
    return getMyTheaters( abstractTO, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterTO> getMyTheaters( AbstractTO abstractTO, Language language )
  {
    UserDO user = this.userDAO.find( abstractTO.getUserId().intValue() );
    validateUser( user );
    List<TheaterDO> theaters = extractTheaters( user );

    if( CollectionUtils.isNotEmpty( user.getTheaterDOList() ) )
    {
      theaters = (List<TheaterDO>) CollectionUtils.intersection( theaters, user.getTheaterDOList() );
    }

    List<TheaterTO> theatersAssigned = new ArrayList<TheaterTO>();
    for( TheaterDO theaterDO : theaters )
    {
      if( theaterDO.isFgActive() )
      {
        TheaterTO theaterTO = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer( language ).transform( theaterDO );
        theatersAssigned.add( theaterTO );
      }
    }
    Collections.sort( theatersAssigned, new CatalogTOComparator( false ) );

    return theatersAssigned;
  }

  private List<TheaterDO> extractTheaters( UserDO user )
  {
    List<TheaterDO> theaters = new ArrayList<TheaterDO>();
    if( CollectionUtils.isNotEmpty( user.getRegionDOList() ) )
    {
      for( RegionDO region : user.getRegionDOList() )
      {
        if( region.isFgActive() && CollectionUtils.isNotEmpty( region.getTheaterDOList() ) )
        {
          for( TheaterDO theater : region.getTheaterDOList() )
          {
            theaters.add( theater );
          }
        }
      }
    }
    return theaters;
  }

  private void validateUser( UserDO user )
  {
    if( user == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SECURITY_ERROR_INVALID_USER );
    }
  }

  @Override
  public TheaterTO getTheater( Long theaterId )
  {
    TheaterDO theaterDO = this.theaterDAO.find( theaterId.intValue() );
    return (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB#getIndicatorTypeById(int, language)
   */
  @Override
  public IncomeSettingsTypeTO getIndicatorTypeById( int idIndicatorType, Language language )
  {
    return this.incomeSettingsTypeDAO.get( idIndicatorType, language );
  }

}
