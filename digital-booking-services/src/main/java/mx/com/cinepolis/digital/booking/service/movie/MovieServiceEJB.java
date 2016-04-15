package mx.com.cinepolis.digital.booking.service.movie;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;

/**
 * Interface que define que los emtodos relacionados a peliculas
 * @author agustin.ramirez
 *
 */
@Local
public interface MovieServiceEJB {

	/**
	 * Metodo que devuelve la lista de  los proximos estrenos
	 * (metodo ocupado por el carrusel)(de momento se obtendran unicamente
	 * los ultimos registros en la bae de datos)
	 * @return
	 */
	List<CatalogTO> findUpcomingMovies();
	
}
