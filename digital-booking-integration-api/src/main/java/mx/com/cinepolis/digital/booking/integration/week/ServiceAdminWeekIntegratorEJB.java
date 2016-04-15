package mx.com.cinepolis.digital.booking.integration.week;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface que define los servicios de Integracion asociado a la administración de semanas.
 * 
 * @author shernandezl
 */
public interface ServiceAdminWeekIntegratorEJB
{
  /**
   * Método que se encarga de obtener el catálogo de semanas paginado.
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<WeekTO> getCatalogWeekSummary( PagingRequestTO pagingRequestTO );

  /**
   * Método que obtiene el número de semana más grande registrado en base de datos.
   * 
   * @return
   */
  WeekTO getNextWeek();

  /**
   * Método que guarda una semana.
   * 
   * @param weekTO
   */
  void saveWeek( WeekTO weekTO );

  /**
   * Método que se una semana.
   * 
   * @param weekTO
   */
  void updateWeeK( WeekTO weekTO );

  /**
   * Método que se encarga de eliminar una semana.
   * 
   * @param weekTO
   */
  void deleteWeek( WeekTO weekTO );
}
