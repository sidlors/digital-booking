package mx.com.cinepolis.digital.booking.persistence.base.dao;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Clase abstracta que define los metodos de un dao
 * 
 * @author agustin.ramirez
 */
public abstract class AbstractBaseDAO<T>
{
  /**
   * Entidad
   */
  private Class<T> entityClass;

  /**
   * Entity Manager
   * 
   * @return
   */
  protected abstract EntityManager getEntityManager();

  /**
   * Constructor de la clase
   * 
   * @param entityClass
   */
  public AbstractBaseDAO( Class<T> entityClass )
  {
    this.entityClass = entityClass;
  }

  /**
   * Metodo insertar
   * 
   * @param entity
   */
  public void create( T entity )
  {
    getEntityManager().persist( entity );
  }

  /**
   * Metodo editar o actualizar
   * 
   * @param entity
   */
  public void edit( T entity )
  {
    getEntityManager().merge( entity );
  }

  /**
   * Metodo Eliminar
   * 
   * @param entity
   */
  public void remove( T entity )
  {
    getEntityManager().remove( getEntityManager().merge( entity ) );
  }

  /**
   * Metodo de busqueda por Id
   * 
   * @param id
   * @return
   */
  public T find( Object id )
  {
    return getEntityManager().find( entityClass, id );
  }

  /**
   * Metodo de busqueda por todos
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<T> findAll()
  {
    return getEntityManager().createQuery( "select object(o) from " + entityClass.getSimpleName() + " as o" )
        .getResultList();
  }

  /**
   * Metodo de busqueda por rango
   * 
   * @param range
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<T> findRange( int[] range )
  {
    javax.persistence.Query q = getEntityManager().createQuery(
      "select object(o) from " + entityClass.getSimpleName() + " as o" );
    q.setMaxResults( range[1] - range[0] );
    q.setFirstResult( range[0] );
    return q.getResultList();
  }

  /**
   * Metodo contador
   * 
   * @return
   */
  public int count()
  {
    return ((Long) getEntityManager().createQuery( "select count(o) from " + entityClass.getSimpleName() + " as o" )
        .getSingleResult()).intValue();
  }

  /**
   * Fuerza el flush al entitymanager
   */
  public void flush()
  {
    getEntityManager().flush();
  }

}
