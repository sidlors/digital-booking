package mx.com.cinepolis.digital.booking.commons.utils;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class with tools used to work with entity {@link PersonDO}.
 * 
 * @author afuentes
 */
public final class PersonUtils
{
  private static final String SLASH = " / ";
  private static final String BLANK = " ";

  private PersonUtils()
  {
  }

  /**
   * Method that builds a person's full name.
   * 
   * @param name {@link String} with the person's name.
   * @param lastName {@link String} with the person's lastName.
   * @param motherLastName {@link String} with the person's motherLastName.
   * @return {@link String} with the full name.
   * @author afuentes
   */
  public static String buildFullName( String name, String lastName, String motherLastName )
  {
    StringBuilder sb = CinepolisUtils.buildMutableString( name, BLANK, lastName );
    if( StringUtils.isNotBlank( motherLastName ) )
    {
      sb.append( BLANK );
      sb.append( motherLastName );
    }
    return sb.toString();
  }

  /**
   * Method that builds a person's full name.
   * 
   * @param personTO {@link PersonTO} with the person information.
   * @return {@link String} with the full name.
   * @author afuentes
   */
  public static String buildFullName( PersonTO personTO )
  {
    return buildFullName( personTO.getName(), personTO.getDsLastname(), personTO.getDsMotherLastname() );
  }

  /**
   * Method that builds a {@link String} with the roles provided.
   * 
   * @param roles List of {@link CatalogTO} with roles information
   * @return A {@link String} with the roles provided.
   * @author afuentes
   */
  public static String buildRolesList( List<CatalogTO> roles )
  {
    StringBuilder builder = new StringBuilder();
    for( CatalogTO catalogTO : roles )
    {
      builder.append( catalogTO.getName() );
      builder.append( SLASH );
    }
    String roleList = builder.toString();
    return StringUtils.isNotEmpty( roleList ) ? StringUtils.left( roleList, roleList.length() - 2 ) : roleList;
  }

}
