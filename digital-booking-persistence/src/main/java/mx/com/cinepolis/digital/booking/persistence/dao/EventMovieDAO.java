package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.EventMovieDO}
 * 
 * @author afuentes
 */
public interface EventMovieDAO extends GenericDAO<EventMovieDO>
{
  
  /**
   * Find EventMovie by distributor
   * @param idEvent
   * @return
   */
  List<EventMovieDO> findByIdDistributor( DistributorDO idDistributor );
  
  /**
   * Find EventMovie by idVista and active
   * @param idVista
   * @return
   */
  EventMovieTO findByIdVistaAndActive( String idVista );

}
