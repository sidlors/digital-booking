package mx.com.cinepolis.digital.booking.service.theater.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.service.theater.TheaterServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
/**
 * Clase que implemeta los metodos relacionados a un cine
 * @author Agustin.ramirez
 *
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class TheaterServiceEJBImpl implements TheaterServiceEJB{
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.theater.TheaterService#getTheatersByRegion(mx.com.cinepolis.digital.booking.model.to.RegionTO)
	 */
	@Override
	public List<TheaterTO> getTheatersByRegion(
			RegionTO<CatalogTO, CatalogTO> region) {
		// TODO Auto-generated method stub
		return null;
	}

}
