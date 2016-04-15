package mx.com.cinepolis.digital.booking.commons.query;

/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.SystemLogDO}
 * 
 * @author jcarbajal
 */
public enum SystemLogQuery implements ModelQuery
{
  ID_SYSTEM_LOG("idSystemLog"), ID_OPERATION("idOperation"), ID_PROCESS("idProcess"), ID_PERSON("idPerson"), ID_USER(
      "idUser"), USER_NAME("dsUsername"), PERSON_NAME("dsName"), PERSON_LAST_NAME("dsLastname"), DT_OPERATION(
      "dtOperation"), START_DATE("startDate"), FINAL_DATE("finalDate");
  private String query;

  private SystemLogQuery( String query )
  {
    this.query = query;
  }

  /**
   * 
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
