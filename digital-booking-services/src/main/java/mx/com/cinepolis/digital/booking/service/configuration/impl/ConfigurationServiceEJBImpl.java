package mx.com.cinepolis.digital.booking.service.configuration.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Ejb que implementa los meodo definidos en {@link ConfigurationServiceEJB}
 * 
 * @author agustin.ramirez
 * 
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ConfigurationServiceEJBImpl implements ConfigurationServiceEJB {

	/**
	 * DAO
	 */
	@EJB
	private ConfigurationDAO configurationDAO;
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB#findParameterById(java.lang.Long)
	 */
	@Override
	public ConfigurationDO findParameterById(Long id) {
		if(id == null ){
			 throw DigitalBookingExceptionBuilder
	          .build( DigitalBookingExceptionCode.CONFIGURATION_ID_IS_NULL );
		}
		
		return configurationDAO.find(id.intValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB#findParameterByName(java.lang.String)
	 */
	@Override
	public ConfigurationDO findParameterByName(String parameterName) {
		if(StringUtils.isEmpty(parameterName)){
			 throw DigitalBookingExceptionBuilder
	          .build( DigitalBookingExceptionCode.CONFIGURATION_NAME_IS_NULL );
		}
		return configurationDAO.findByParameterName(parameterName);
	}

}
