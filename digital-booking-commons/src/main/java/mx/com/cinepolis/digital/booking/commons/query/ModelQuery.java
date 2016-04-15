package mx.com.cinepolis.digital.booking.commons.query;

/**
 * @author gsegura
 * @since 0.0.1
 */
public interface ModelQuery
{

  /**
   * Gets the query mapping for JPA of the model entity
   * 
   * @return the mapping
   */
  String getQuery();
}
