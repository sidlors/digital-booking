package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Collections;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.utils.ScreenTOComparator;

import org.apache.commons.collections.CollectionUtils;

/**
 * Clase que modela la información de un cineS
 * 
 * @author rgarcia
 */
public class TheaterTO extends CatalogTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 8331743053593973782L;

  /**
   * Telefono del Cine
   */
  private String dsTelephone;
  /**
   * CC para email
   */
  private String dsCCEmail;
  /**
   * Región ala que pertenece el cineS
   */
  private RegionTO<CatalogTO, CatalogTO> region;
  /**
   * Ciudad a la que pertenece el cineS
   */
  private CatalogTO city;
  /**
   * Estado al que pertenece el cineS
   */
  private StateTO<CatalogTO, Integer> state;
  /**
   * Salas disponibles en el cine
   */
  private List<ScreenTO> screens;

  /**
   * Dirección de correo electrónico del cine
   */
  private CatalogTO email;

  /**
   * Número del cine
   */
  private int nuTheater;

  /**
   * Imagen del semáforo para el cine
   */
  private String imageSemaphore;

  /**
   * Lista de configuraciones de ingresos
   */
  private List<IncomeSettingsTO> incomeSettingsList;

  public TheaterTO()
  {
    super();
  }

  public TheaterTO( Long id )
  {
    super( id );
  }

  public TheaterTO( Long id, String name )
  {
    super( id, name );
  }

  /**
   * @return the dsTelephone
   */
  public String getDsTelephone()
  {
    return dsTelephone;
  }

  /**
   * @param dsTelephone the dsTelephone to set
   */
  public void setDsTelephone( String dsTelephone )
  {
    this.dsTelephone = dsTelephone;
  }

  /**
   * @return the region
   */
  public RegionTO<CatalogTO, CatalogTO> getRegion()
  {
    return region;
  }

  /**
   * @param region the region to set
   */
  public void setRegion( RegionTO<CatalogTO, CatalogTO> region )
  {
    this.region = region;
  }

  /**
   * @return the city
   */
  public CatalogTO getCity()
  {
    return city;
  }

  /**
   * @param city the city to set
   */
  public void setCity( CatalogTO city )
  {
    this.city = city;
  }

  /**
   * @return the state
   */
  public StateTO<CatalogTO, Integer> getState()
  {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState( StateTO<CatalogTO, Integer> state )
  {
    this.state = state;
  }

  /**
   * @return the screens
   */
  public List<ScreenTO> getScreens()
  {
    return screens;
  }

  /**
   * @param screens the screens to set
   */
  public void setScreens( List<ScreenTO> screens )
  {
    this.screens = screens;
  }

  /**
   * @return the screensNamesTheater
   */
  public String getScreensNamesTheater()
  {
    Collections.sort( screens, new ScreenTOComparator() );
    StringBuilder sb = new StringBuilder();
    if( CollectionUtils.isNotEmpty( screens ) )
    {
      int n = 0;
      for( ScreenTO to : screens )
      {
        sb.append( to.getNuScreen() );
        if( n < screens.size() - 1 )
        {
          sb.append( ", " );
        }
        n++;
      }

    }
    return sb.toString();
  }

  /**
   * @return the email
   */
  public CatalogTO getEmail()
  {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail( CatalogTO email )
  {
    this.email = email;
  }

  /**
   * @return the nuTheater
   */
  public int getNuTheater()
  {
    return nuTheater;
  }

  /**
   * @param nuTheater the nuTheater to set
   */
  public void setNuTheater( int nuTheater )
  {
    this.nuTheater = nuTheater;
  }

  /**
   * @return the imageSemaphore
   */
  public String getImageSemaphore()
  {
    return imageSemaphore;
  }

  /**
   * @param imageSemaphore the imageSemaphore to set
   */
  public void setImageSemaphore( String imageSemaphore )
  {
    this.imageSemaphore = imageSemaphore;
  }

  /**
   * @return the dsCCEmail
   */
  public String getDsCCEmail()
  {
    return dsCCEmail;
  }

  /**
   * @param dsCCEmail the dsCCEmail to set
   */
  public void setDsCCEmail( String dsCCEmail )
  {
    this.dsCCEmail = dsCCEmail;
  }

  /**
   * @return the incomeSettingsList
   */
  public List<IncomeSettingsTO> getIncomeSettingsList()
  {
    return incomeSettingsList;
  }

  /**
   * @param incomeSettingsList the incomeSettingsList to set
   */
  public void setIncomeSettingsList( List<IncomeSettingsTO> incomeSettingsList )
  {
    this.incomeSettingsList = incomeSettingsList;
  }

}
