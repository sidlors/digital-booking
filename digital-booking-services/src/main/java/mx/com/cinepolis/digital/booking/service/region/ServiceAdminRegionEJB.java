package mx.com.cinepolis.digital.booking.service.region;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;

/**
 * Interface que define los metodos asociados a la administración de zonas de los cines
 * 
 * @author rgarcia
 */
@Local
public interface ServiceAdminRegionEJB
{
  /**
   * Método que se encarga de registrar la información de la zona de un cine
   * 
   * @param theaterRegionTO
   */
  void saveRegion( RegionTO<CatalogTO, CatalogTO> theaterRegionTO );

  /**
   * Método que se encarga de eliminar la información de la zona de un cine. Solo se puede eliminar si no tiene cines
   * asignados
   * 
   * @param theaterRegionTO
   */
  void deleteRegion( RegionTO<CatalogTO, CatalogTO> theatherRegionTO );

  /**
   * Método que se encarga de actualizar la información de la zona de un cine
   * 
   * @param theatherRegionTO
   */
  void updateRegion( RegionTO<CatalogTO, CatalogTO> theatherRegionTO );

  /**
   * Método que se encarga de obtener el catálogo completo de las zonas de los cines
   * 
   * @return
   */
  List<RegionTO<CatalogTO, CatalogTO>> getAllRegions();

  /**
   * Método que se encarga de obtener secciones por páginas del catálogo de las zonas de los cines
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> getCatalogRegionSummary( PagingRequestTO pagingRequestTO );

  /**
   * Método que se encarga de obtener los territorios existentes
   * 
   * @return Lista de CatalogTO
   */
  List<CatalogTO> getAllTerritories();

  /**
   * Metodo que se encarga de obtener los paises
   * 
   * @return
   */
  List<CatalogTO> getAllCountries();

  /**
   * Metodo que se encarga de obtener los estados existentes
   * 
   * @return List<StateTO <CatalogTO, Number>>
   */
  List<StateTO<CatalogTO, Number>> getAllStates();

  /**
   * Metodo que se encarga de obtener las regiones de acuerdo a un territorio
   * 
   * @return
   */
  List<RegionTO<CatalogTO, CatalogTO>> getAllRegionsByTerritory( CatalogTO territory );

  /**
   * Metodo que se encarga de obtener las ciudades existentes
   * 
   * @return
   */
  List<CatalogTO> getAllCities();

  /**
   * Metodo que se encarga de obtener los estados existentes por pais
   * 
   * @return List<StateTO <CatalogTO, Number>>
   */
  List<StateTO<CatalogTO, Number>> getAllStatesByCountry( CatalogTO country );

  /**
   * Method that finds all active regions.
   * 
   * @param abstractTO {@link AbstractTO} with the current session information.
   * @return List of {@link CatalogTO} with the active regions information.
   * @author afuentes
   */
  List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO );

  /**
   * Method that cosults the available cities for a state id selected.
   * 
   * @param idState
   * @return a list with available cities.
   * @author jreyesv
   */
  List<CatalogTO> getAllCitiesByState( Long idState );

}
