package mx.com.cinepolis.digital.booking.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Abstract class for all entity classes of JPA which can be modified by the user.
 * 
 * @author gsegura
 * @since 0.0.1
 */
@MappedSuperclass
public abstract class AbstractSignedEntity<T> extends AbstractEntity<T>
{
  private static final long serialVersionUID = -9018264883336087117L;

  @Column(name = "FG_ACTIVE", nullable = false)
  private boolean fgActive = true;

  @Column(name = "ID_LAST_USER_MODIFIER", nullable = false)
  private int idLastUserModifier;

  @Column(name = "DT_LAST_MODIFICATION", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtLastModification;

  /**
   * @return the fgActive
   */
  public boolean isFgActive()
  {
    return fgActive;
  }

  /**
   * @param fgActive the fgActive to set
   */
  public void setFgActive( boolean fgActive )
  {
    this.fgActive = fgActive;
  }

  /**
   * @return the idLastUserModifier
   */
  public int getIdLastUserModifier()
  {
    return idLastUserModifier;
  }

  /**
   * @param idLastUserModifier the idLastUserModifier to set
   */
  public void setIdLastUserModifier( int idLastUserModifier )
  {
    this.idLastUserModifier = idLastUserModifier;
  }

  /**
   * @return the dtLastModification
   */
  public Date getDtLastModification()
  {
    return CinepolisUtils.enhancedClone( dtLastModification );
  }

  /**
   * @param dtLastModification the dtLastModification to set
   */
  public void setDtLastModification( Date dtLastModification )
  {
    this.dtLastModification = CinepolisUtils.enhancedClone( dtLastModification );
  }

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
