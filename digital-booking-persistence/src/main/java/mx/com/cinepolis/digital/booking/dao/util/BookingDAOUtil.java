package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryTypeDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Class with tools for movie and theater booking.
 * 
 * @author afuentes
 */
public final class BookingDAOUtil
{

  private BookingDAOUtil()
  {
    // Se ofusca el constructor
  }

  /**
   * Method that adds a screen "zero" to a list of {@link ScreenTO}.
   * 
   * @param screenTOs List of {@link ScreenTO} to add the screen zero.
   * @param numSelectedScreens Amount of screens selected.
   */
  public static void addScreenZero( List<ScreenTO> screenTOs, int numSelectedScreens )
  {
    ScreenTO screenTO = new ScreenTO();
    screenTO.setId( 0L );
    screenTO.setNuScreen( 0 );
    screenTO.setSelected( numSelectedScreens == 0 );
    screenTOs.add( 0, screenTO );
  }

  /**
   * Method that marks every {@link ScreenTO} in the list provided if it was previously selected or if it should be
   * disabled.
   * 
   * @param screenTOs List of {@link ScreenTO} that must be marked.
   * @param selectedScreenDOs List of previously selected {@link ScreenDO} in the corresponding booking.
   * @param eventDO {@link EventDO} with the event information.
   */
  public static void markSelectedAndDisabledScreens( List<ScreenTO> screenTOs, List<ScreenDO> selectedScreenDOs,
      EventDO eventDO )
  {
    List<CatalogTO> movieFormatCategoryTOs = getMovieFormatCategoryTOs( eventDO );
    List<CatalogTO> selectedScreenTOs = getSelectedScreenTOs( selectedScreenDOs );
    for( ScreenTO screenTO : screenTOs )
    {
      screenTO.setSelected( selectedScreenTOs.contains( screenTO ) );
      if( movieFormatCategoryTOs != null && screenTO.getMovieFormats() != null )
      {
        screenTO.setDisabled( !CollectionUtils.containsAny( movieFormatCategoryTOs, screenTO.getMovieFormats() ) );
      }
    }
  }

  private static List<CatalogTO> getSelectedScreenTOs( List<ScreenDO> selectedScreenDOs )
  {
    List<CatalogTO> selectedScreenTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( selectedScreenDOs, new ScreenDOToScreenTOTransformer(), selectedScreenTOs );
    return selectedScreenTOs;
  }

  private static List<CatalogTO> getMovieFormatCategoryTOs( EventDO eventDO )
  {
    List<CatalogTO> movieFormatCategoryTOs = new ArrayList<CatalogTO>();
    if( eventDO != null )
    {
      List<CategoryDO> movieFormatCategories = new ArrayList<CategoryDO>();
      CollectionUtils.select(
        eventDO.getCategoryDOList(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdCategoryType" ),
          PredicateUtils.equalPredicate( new CategoryTypeDO( CategoryType.MOVIE_FORMAT.getId() ) ) ),
        movieFormatCategories );
      CollectionUtils.collect( movieFormatCategories, new CategoryDOToCatalogTOTransformer(), movieFormatCategoryTOs );
    }
    return movieFormatCategoryTOs;
  }

}
