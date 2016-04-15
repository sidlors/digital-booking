package mx.com.cinepolis.digital.booking.service.movie.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.service.movie.MovieServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
/**
 * Clase que implementa los servicios relacionados alas peliculas
 * @author agustin.ramirez
 *
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class MovieServiceEJBImpl implements MovieServiceEJB{
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.movie.MovieServiceEJB#findUpcomingMovies()
	 */
	@Override
	public List<CatalogTO> findUpcomingMovies() {
		// TODO Auto-generated method stub
		return null;
	}

}
