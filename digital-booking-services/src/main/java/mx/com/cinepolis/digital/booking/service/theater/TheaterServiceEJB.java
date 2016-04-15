package mx.com.cinepolis.digital.booking.service.theater;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
/**
 * Servicio que define los  metodos relacionados a un cine
 * @author agustin.ramirez
 *
 */
@Local
public interface TheaterServiceEJB {
	
	/**
	 * Obtiene la lista de Cines activos asociados a una region
	 * @param region
	 * @return
	 */
	List<TheaterTO> getTheatersByRegion(RegionTO<CatalogTO, CatalogTO> region);

}
