package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.TheaterDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class TheaterDOToTheaterTOTransformer implements Transformer
{
  /**
   * Languaje
   */
  private Language language;

  /**
   * Constructor default
   */
  public TheaterDOToTheaterTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor con Languaje
   */
  public TheaterDOToTheaterTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object transform( Object object )
  {
    TheaterTO theaterTO = null;
    if( object instanceof TheaterDO )
    {
      theaterTO = new TheaterTO();
      TheaterDO entity = (TheaterDO) object;

      theaterTO.setDsTelephone( entity.getDsTelephone() );
      theaterTO.setDsCCEmail( entity.getDsCCEmail() );
      theaterTO.setFgActive( entity.isFgActive() );
      theaterTO.setId( Long.valueOf( entity.getIdTheater() ) );
      theaterTO.setTimestamp( entity.getDtLastModification() );
      theaterTO.setUserId( Long.valueOf( entity.getIdLastUserModifier() ) );
      theaterTO.setIdVista( entity.getIdVista() );
      theaterTO.setNuTheater( entity.getNuTheater() );

      if( entity.getIdEmail() != null )
      {
        theaterTO.setEmail( new CatalogTO( entity.getIdEmail().getIdEmail().longValue(), entity.getIdEmail()
            .getDsEmail() ) );
      }

      for( TheaterLanguageDO languajeDO : entity.getTheaterLanguageDOList() )
      {
        if( languajeDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          theaterTO.setName( languajeDO.getDsName() );
          break;
        }
      }
      RegionDOToRegionTOTransformer transformerRegion = new RegionDOToRegionTOTransformer( this.language );
      RegionTO<CatalogTO, CatalogTO> region = (RegionTO<CatalogTO, CatalogTO>) transformerRegion.transform( entity
          .getIdRegion() );
      theaterTO.setRegion( region );

      CityDOToCatalogTOTransformer transformerCity = new CityDOToCatalogTOTransformer( this.language );
      CatalogTO cityCatalog = (CatalogTO) transformerCity.transform( entity.getIdCity() );
      theaterTO.setCity( cityCatalog );

      StateDOToStateTOTransformer stateTransformer = new StateDOToStateTOTransformer( this.language );
      StateTO<CatalogTO, Integer> state = (StateTO<CatalogTO, Integer>) stateTransformer
          .transform( entity.getIdState() );
      theaterTO.setState( state );
      List<ScreenTO> screenTOs = new ArrayList<ScreenTO>();
      ScreenDOToScreenTOTransformer transformerScreen = new ScreenDOToScreenTOTransformer( this.language );
      for( ScreenDO screenDO : entity.getScreenDOList() )
      {
        if( screenDO.isFgActive() )
        {
          screenTOs.add( (ScreenTO) transformerScreen.transform( screenDO ) );
        }

      }
      theaterTO.setScreens( screenTOs );
      this.extractIncomeSettgins( theaterTO, entity );
    }

    return theaterTO;
  }

  /**
   * Method that extracts the income settings information for a
   * {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} object.
   * 
   * @param theaterTO
   * @param entity
   */
  private void extractIncomeSettgins( TheaterTO theaterTO, TheaterDO entity )
  {
    theaterTO.setIncomeSettingsList( new ArrayList<IncomeSettingsTO>() );
    if( CollectionUtils.isNotEmpty( entity.getIncomeSettingsDOList() ) )
    {
      for( IncomeSettingsDO incomeSettingsDO : entity.getIncomeSettingsDOList() )
      {
        IncomeSettingsTO incomeSettingsTO = (IncomeSettingsTO) new IncomeSettingsDOToIncomeSettingsTOTransformer(
            language ).transform( incomeSettingsDO );
        theaterTO.getIncomeSettingsList().add( incomeSettingsTO );
      }
    }
  }

}
