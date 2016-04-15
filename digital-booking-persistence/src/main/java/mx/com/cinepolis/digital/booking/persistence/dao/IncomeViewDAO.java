package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import javax.ejb.Local;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.IncomeViewDO;

/**
 * DAO Interface for entity view IncomeViewDO
 * 
 * @author gsegura
 */
@Local
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public interface IncomeViewDAO
{

  /**
   * Return a List with all the records from a view
   * 
   * @return
   */
  List<IncomeViewDO> findAll();

  /**
   * Gets the number of records of the view
   * 
   * @return
   */
  int count();
}
