package mx.com.cinepolis.digital.booking.integration.city;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface that defines the integration services relating to cities administration.
 * 
 * @author jreyesv
 */
public interface ServiceCityIntegratorEJB
{

  /**
   * Method that saves a city.
   * 
   * @param cityTO, a {@linmx.com.cinepolis.digital.booking.commons.to.CityTO} object with the city information.
   */
  void saveCity( CityTO cityTO );

  /**
   * Method that logically deletes a city.
   * 
   * @param cityTO, a {@linmx.com.cinepolis.digital.booking.commons.to.CityTO} object with the city information.
   */
  void deleteCity( CityTO cityTO );

  /**
   * Method that updates a city.
   * 
   * @param cityTO, a {@linmx.com.cinepolis.digital.booking.commons.to.CityTO} object with the city information.
   */
  void updateCity( CityTO cityTO );

  /**
   * Method that finds, by paging, all active cities according to criteria selected.
   * 
   * @param pagingRequestTO, a {@link mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO} object with the
   *          paging request information.
   * @return {@link mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO<CityTO>}, with the paging response
   *         information.
   */
  PagingResponseTO<CityTO> findAllCitiesByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Method that consults all active countries in data base.
   * @return a {@link java.util.List<CatalogTO>} with the countries found.
   */
  List<CatalogTO> findAllActiveCountries();

}
