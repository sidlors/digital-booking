package mx.com.cinepolis.digital.booking.persistence.base.dao;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.model.AbstractEntity;

/**
 * Generic interface for DAOs
 * 
 * @author gsegura
 * @param <T>
 */
@Local
public interface GenericDAO<T extends AbstractEntity<T>>
{

  /**
   * Creates an entity
   * 
   * @param entity
   */
  void create( T entity );

  /**
   * Edits an entity
   * 
   * @param entity
   */
  void edit( T entity );

  /**
   * Removes the entity
   * 
   * @param entity
   */
  void remove( T entity );

  /**
   * Finds an entity given its id
   * 
   * @param id
   * @return
   */
  T find( Object id );

  /**
   * Finds all entities
   * 
   * @return
   */
  List<T> findAll();

  /**
   * Find a rage of entities
   * 
   * @param range
   * @return
   */
  List<T> findRange( int[] range );

  /**
   * Counts the number of entities registered
   * 
   * @return
   */
  int count();

  /**
   * Forces the flush to the entity manager
   */
  void flush();

}
