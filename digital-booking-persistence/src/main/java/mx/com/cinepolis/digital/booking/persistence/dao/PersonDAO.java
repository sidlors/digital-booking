package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.PersonDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface PersonDAO extends GenericDAO<PersonDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<PersonTO>} with the results
   */
  PagingResponseTO<PersonTO> findAllByPaging( PagingRequestTO pagingRequestTO );

}
