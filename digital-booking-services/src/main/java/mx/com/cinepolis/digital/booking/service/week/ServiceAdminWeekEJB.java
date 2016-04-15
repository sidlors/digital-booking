package mx.com.cinepolis.digital.booking.service.week;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface que define los métodos asociados a la administración de Semanas.
 * 
 * @author shernandezl
 */
@Local
public interface ServiceAdminWeekEJB
{
  /**
   * Método que se encarga de obtener el catálogo de senmanas paginado.
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
   * Método que guarda el registro de una semana
   * 
   * @param weekTO
   */
  void saveWeek( WeekTO weekTO );

  /**
   * Método que actuliza un registro de una semana.
   * 
   * @param weekTO
   */
  void updateWeek( WeekTO weekTO );

  /**
   * Método que elimina un registro Semana.
   * 
   * @param weekTO
   */
  void deleteWeek( WeekTO weekTO );

}
