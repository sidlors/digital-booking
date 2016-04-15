package mx.com.cinepolis.digital.booking.integration.distributor;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface que dfine los servicios de integracion asociados a un distribuidor
 * 
 * @author agustin.ramirez
 */
public interface ServiceAdminDistributorIntegratorEJB
{
  /**
   * Metodo que se encarga de salvar al distribuidor
   * 
   * @param distributor
   */
  void saveDistributor( DistributorTO distributor );

  /**
   * Metodo que se encarga de eliminar al distribuidor
   * 
   * @param distributor
   */
  void deleteDistributor( DistributorTO distributor );

  /**
   * Metodo que se encarga de actualizar al distribuidor
   * 
   * @param distributor
   */
  void updateDistributor( DistributorTO distributor );

  /**
   * Metodo que se encarga de obtener a todos los distribuidores
   */
  List<DistributorTO> getAll();

  /**
   * Metodo que se encarga de obtener el catalogo de distribuidores paginado
   * 
   * @param pagingRequestTO
   * @return PagingResponseTO<CatalogTO>
   */
  PagingResponseTO<DistributorTO> getCatalogDistributorSummary( PagingRequestTO pagingRequestTO );

  /**
   * Method that finds a distributor with the given identifier.
   * 
   * @param idDistributor Distributor identifier.
   * @return {@link CatalogTO} with the distributor information or <code>null</code> if it cannot find the distributor.
   * @author afuentes
   */
  DistributorTO getDistributor( Integer idDistributor );

}
