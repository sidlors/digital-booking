package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.PresaleDO}
 * 
 * @author jcarbajal
 */
@Local
public interface PresaleDAO extends GenericDAO<PresaleDO>
{

  /**
   * Creates and persists a {link mx.com.cinepolis.digital.booking.model.PresaleDO} object in database.
   * 
   * @param presaleTO, the presale to create.
   * @return PresaleDO, the presale persisted in database.
   */
  PresaleDO save( PresaleTO presaleTO );
  
  /**
   * Updates and persists a {link mx.com.cinepolis.digital.booking.model.PresaleTO} object in database.
   * 
   * @param presaleTO, the presale to update.
   * @return PresaleDO, the presale updated in database.
   */
  PresaleDO update( PresaleTO presaleTO );

  /**
   * Finds all available presales to be deactivated.
   * 
   * @return presaleDOList, a {@link mx.com.cinepolis.digital.booking.model.PresaleDO} list.
   */
  List<PresaleDO> findActivePresaleForDeactivate();

  /**
   * Counts all active presales in database.
   * 
   * @return the number of active records in database.
   */
  int countAllActivePresales();

}
