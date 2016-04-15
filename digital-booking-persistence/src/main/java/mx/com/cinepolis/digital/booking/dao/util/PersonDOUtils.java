package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.utils.PersonUtils;
import mx.com.cinepolis.digital.booking.model.PersonDO;

/**
 * @author gsegura
 */
public final class PersonDOUtils
{

  private PersonDOUtils()
  {
  }

  /**
   * Method that builds a person's full name.
   * 
   * @param personDO Entity {@link PersonDO} with the person information.
   * @return {@link String} with the full name.
   * @author afuentes
   */
  public static String buildFullName( PersonDO personDO )
  {
    return PersonUtils.buildFullName( personDO.getDsName(), personDO.getDsLastname(), personDO.getDsMotherLastname() );
  }
}
