package mx.com.cinepolis.digital.booking.model.utils;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.model.AbstractSignedEntity;

/**
 * Utility class for enitites manipulation
 * 
 * @author gsegura
 * @since 0.0.1
 */
public final class AbstractEntityUtils
{

  private AbstractEntityUtils()
  {
  }

  /**
   * Applies the electronic sign from a {@link mx.com.cinepolis.digital.booking.commons.to.AbstractTO} to a
   * {@link mx.com.cinepolis.digital.booking.model.AbstractSignedEntity<?>}
   * 
   * @param abstractEntity
   * @param abstractTO
   */
  public static void applyElectronicSign( AbstractSignedEntity<?> abstractEntity, AbstractTO abstractTO )
  {
    abstractEntity.setDtLastModification( abstractTO.getTimestamp() );
    abstractEntity.setIdLastUserModifier( abstractTO.getUserId().intValue() );
  }

  /**
   * Copies the electronic sign from a {@link mx.com.cinepolis.digital.booking.model.to.AbstractSignedEntity<?>} to
   * another {@link mx.com.cinepolis.digital.booking.model.AbstractSignedEntity<?>}
   * 
   * @param abstractEntity
   * @param abstractTO
   */
  public static void copyElectronicSign( AbstractSignedEntity<?> source, AbstractSignedEntity<?> destination )
  {
    destination.setDtLastModification( source.getDtLastModification() );
    destination.setIdLastUserModifier( source.getIdLastUserModifier() );
  }

  /**
   * Copies the electronic signature from a {@link mx.com.cinepolis.digital.booking.commons.to.AbstractTO} to another
   * {@link mx.com.cinepolis.digital.booking.commons.to.AbstractTO}
   * 
   * @param source {@link AbstractTO} with the electronic signature.
   * @param destination {@link AbstractTO} where the signature will be copied.
   * @author afuentes
   */
  public static void copyElectronicSignature( AbstractTO source, AbstractTO destination )
  {
    destination.setTimestamp( source.getTimestamp() );
    destination.setUserId( source.getUserId() );
  }

}
