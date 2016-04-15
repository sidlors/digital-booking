package mx.com.cinepolis.digital.booking.persistence.base.dao;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * Clase abstracta que define los metodos de un dao
 * 
 * @author gsegura
 */
public abstract class AbstractViewBaseDAO<T>
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
  public AbstractViewBaseDAO( Class<T> entityClass )
  {
    this.entityClass = entityClass;
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
   * Metodo contador
   * 
   * @return
   */
  public int count()
  {
    return ((Long) getEntityManager().createQuery( "select count(o) from " + entityClass.getSimpleName() + " as o" )
        .getSingleResult()).intValue();
  }

}
