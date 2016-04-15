package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.model.SystemLogDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.SystemLogDO}
 * 
 * @author jcarbajal
 */
public interface SystemLogDAO extends GenericDAO<SystemLogDO>
{

  PagingResponseTO<SystemLogTO> findAllSystemLogByPaging(PagingRequestTO pagingRequestTO);
}
