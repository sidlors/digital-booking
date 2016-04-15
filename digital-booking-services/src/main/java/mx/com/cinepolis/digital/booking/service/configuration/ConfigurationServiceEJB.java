package mx.com.cinepolis.digital.booking.service.configuration;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.model.ConfigurationDO;

/**
 * Interface que define los metodos relacionados ala configuracion
 * 
 * @author agustin.ramirez
 */
@Local
public interface ConfigurationServiceEJB {
	
	/**
	 * Obtiene el parametro por su ID
	 * @param id
	 * @return
	 */
	ConfigurationDO findParameterById(Long id);
	
	/**
	 * Obtiene el parametro por su nombre
	 * @param parameterName
	 * @return
	 */
	ConfigurationDO findParameterByName(String parameterName);

}
