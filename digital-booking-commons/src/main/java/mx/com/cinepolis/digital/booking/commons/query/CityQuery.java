package mx.com.cinepolis.digital.booking.commons.query;

/**
 * Enumeration for criteria query for {@link mx.com.cinepolis.digital.booking.model.CityDO} entity.
 * 
 * @author jreyesv
 */
public enum CityQuery implements ModelQuery
{
  CITY_ID("idCity"), CITY_NAME("dsName"), COUNTRY_ID("idCountry"), STATE_ID("idState"), CITY_ACTIVE("fgActive"), CITY_LANGUAGE_ID(
      "idLanguage"), CITY_ID_VISTA("idVista"), LANGUAGE_ID("idLanguage");

  private String query;

  private CityQuery( String query )
  {
    this.query = query;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
