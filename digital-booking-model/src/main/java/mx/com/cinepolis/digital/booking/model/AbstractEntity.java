package mx.com.cinepolis.digital.booking.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * Abstract class for all entity classes of JPA.
 * 
 * @author gsegura
 * @since 0.0.1
 */
@MappedSuperclass
public abstract class AbstractEntity<T> implements Comparable<T>, Serializable
{
  private static final long serialVersionUID = -8132633738683047017L;

  /**
   * The entities must implement the method equals(Object):boolean
   * 
   * @param obj
   */
  public abstract boolean equals( Object obj );

  /**
   * The entities must implement the method hashCode():int
   */
  public abstract int hashCode();
}
