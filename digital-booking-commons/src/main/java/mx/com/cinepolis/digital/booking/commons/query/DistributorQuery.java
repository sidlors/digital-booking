package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.DistributorDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum DistributorQuery implements ModelQuery
{
  DISTRIBUTOR_ID("idDistributor"), DISTRIBUTOR_NAME("dsName"), DISTRIBUTOR_ACTIVE("fgActive"), DISTRIBUTOR_SHORT_NAME("dsShortName");

  private String query;

  private DistributorQuery( String query )
  {
    this.query = query;
  }

  /**
   * @return the query
   */
  @Override
  public String getQuery()
  {
    return query;
  }
}
