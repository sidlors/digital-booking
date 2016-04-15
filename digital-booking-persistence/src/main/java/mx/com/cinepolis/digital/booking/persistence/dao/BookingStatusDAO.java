package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity
 * {@link mx.com.cinepolis.digital.booking.model.BookingStatusDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface BookingStatusDAO extends GenericDAO<BookingStatusDO>
{

  /**
   * Edits a register
   * 
   * @param bookingStatusDO
   */
  void edit( BookingStatusDO bookingStatusDO );

  /**
   * Removes a register
   * 
   * @param bookingStatusDO
   */
  void remove( BookingStatusDO bookingStatusDO );

  /**
   * Finds a register by its id
   * 
   * @param id
   * @return An instance of
   *         {@link mx.com.cinepolis.digital.booking.model.BookingStatusDO}
   */
  BookingStatusDO find( Object id );

  /**
   * Find all registers
   * 
   * @return List of
   *         {@link mx.com.cinepolis.digital.booking.model.BookingStatusDO}
   */
  List<BookingStatusDO> findAll();

  /**
   * Finds a ranged registers
   * 
   * @param range A range represented by a initial record(int[0]) and a final
   *          record (int[1])
   * @return List of {link
   *         mx.com.cinepolis.digital.booking.model.BookingStatusDO}
   */
  List<BookingStatusDO> findRange( int[] range );

  /**
   * Counts the records
   * 
   * @return the number of records
   */
  int count();

  /**
   * Forces the CRUD
   */
  void flush();

  /**
   * Finds the booking status
   * 
   * @param id of the booking status
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
   */
  CatalogTO get( int id );

  /**
   * Finds the booking status
   * 
   * @param id of the booking status
   * @param language the language
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
   */
  CatalogTO get( int id, Language language );

  /**
   * Finds all the booking status
   * 
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
   */
  List<CatalogTO> getAll();

  /**
   * Finds all the booking status
   * 
   * @param id of the booking status
   * @param language the language
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
   */
  List<CatalogTO> getAll( Language language );

}
