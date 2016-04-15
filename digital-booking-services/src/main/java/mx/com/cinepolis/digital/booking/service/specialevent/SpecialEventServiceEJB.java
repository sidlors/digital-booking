package mx.com.cinepolis.digital.booking.service.specialevent;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.EventSpecialTO;

/**
 * Define los servicios relacionados a un evento especial
 * @author agustin.ramirez
 *
 */
@Local
public interface SpecialEventServiceEJB {
	
	/**
	 * Salva un evento especial 
	 * @param eventSpecialTO
	 */
	void saveSpecialEvent(EventSpecialTO eventSpecialTO);
	
	/**
	 * Actualiza un evento especial 
	 * @param eventSpecialTO
	 */
	void editSpecialEvent(EventSpecialTO eventSpecialTO);
	
	/**
	 * Obtiene todos los eventos especiales activos
	 * @return List<EventSpecialTO>
	 */
	List<EventSpecialTO> findAllSpecialEvent();


}
