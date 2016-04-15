package mx.com.cinepolis.digital.booking.service.specialevent.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.to.EventSpecialTO;
import mx.com.cinepolis.digital.booking.service.specialevent.SpecialEventServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

/**
 * Clase que implementa los sevricios relacionados a un evento especial
 * @author agustin.ramirez
 *
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class SpecialEventServiceEJBImpl implements SpecialEventServiceEJB {
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.specialevent.SpecialEventService#saveSpecialEvent(mx.com.cinepolis.digital.booking.model.to.EventSpecialTO)
	 */
	@Override
	public void saveSpecialEvent(EventSpecialTO eventSpecialTO) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.specialevent.SpecialEventService#editSpecialEvent(mx.com.cinepolis.digital.booking.model.to.EventSpecialTO)
	 */
	@Override
	public void editSpecialEvent(EventSpecialTO eventSpecialTO) {
		// TODO Auto-generated method stub
		
	}
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.specialevent.SpecialEventServiceEJB#findAllSpecialEvent()
	 */
	@Override
	public List<EventSpecialTO> findAllSpecialEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
