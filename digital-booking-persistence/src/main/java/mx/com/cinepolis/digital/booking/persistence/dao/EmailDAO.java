package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.EmailDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface EmailDAO extends GenericDAO<EmailDO>
{

  /**
   * Finds the {@link mx.com.cinepolis.digital.booking.model.EmailDO} associated to the user id
   * 
   * @param userId
   * @return
   */
  List<EmailDO> findByUserId( Long userId );

  /**
   * Finds the {@link mx.com.cinepolis.digital.booking.model.EmailDO} associated to the person id
   * 
   * @param idPerson
   * @return
   */
  List<EmailDO> findByPersonId( Long idPerson );

}
