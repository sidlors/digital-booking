package mx.com.cinepolis.digital.booking.service.util.activedirectory;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import mx.com.cinepolis.digital.booking.commons.constants.ActiveDirectory;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class in charge of communication with the active directory.
 * 
 * @author gsegura
 * @author afuentes
 */
public class ActiveDirectoryUtil
{
  private static final String AUTHENTICATION_ERROR = "Error al autenticar el usuario: ";
  private static final Logger LOG = LoggerFactory.getLogger( ActiveDirectoryUtil.class );

  private final String ldapUrl;
  private final String ldapAuthMethod;
  private final String ldapAt;
  private final String ldapVersion;
  private final boolean useSSL;
  private final String trustStore;

  /**
   * Constructor.
   * 
   * @param env Hashtable with the active directory configuration information.
   * @param ldapAt Server domain name prefixed with @domain.name.server
   * @param ldapVersion Ldap version.
   */
  public ActiveDirectoryUtil( Hashtable<String, Object> env )
  {
    this.ldapUrl = (String) env.get( Context.PROVIDER_URL );
    this.ldapAuthMethod = (String) env.get( Context.SECURITY_AUTHENTICATION );
    this.ldapAt = (String) env.get( ActiveDirectory.LDAP_AT.getParameter() );
    this.ldapVersion = (String) env.get( ActiveDirectory.LDAP_VERSION.getParameter() );
    this.useSSL = (Boolean) env.get( ActiveDirectory.LDAP_USE_SSL.getParameter() );
    this.trustStore = (String) env.get( ActiveDirectory.LDAP_TRUST_STORE.getParameter() );
  }

  /**
   * Method that authenticates a user into the active directory.
   * 
   * @param user The user name.
   * @param password The user password.
   * @return <code>true</code> if authentication succeeds, <code>false</code> otherwise.
   */
  public boolean authenticate( String user, String password )
  {
    boolean authentication = false;

    if( StringUtils.isNotEmpty( user ) && StringUtils.isNotEmpty( password ) )
    {

      Hashtable<String, Object> envLocal = new Hashtable<String, Object>();

      envLocal.put( Context.INITIAL_CONTEXT_FACTORY, ActiveDirectory.CONTEXT_FACTORY.getParameter() );
      envLocal.put( Context.PROVIDER_URL, ldapUrl );
      envLocal.put( Context.SECURITY_AUTHENTICATION, ldapAuthMethod );
      envLocal.put( Context.SECURITY_PRINCIPAL, new StringBuilder( user ).append( ldapAt ).toString() );
      envLocal.put( Context.SECURITY_CREDENTIALS, password );

      envLocal.put( ActiveDirectory.LDAP_VERSION.getParameter(), ldapVersion );

      if( this.useSSL )
      {
        // specify use of ssl
        System.setProperty( ActiveDirectory.LDAP_TRUST_STORE.getParameter(), trustStore );
        envLocal.put( Context.SECURITY_PROTOCOL, ActiveDirectory.LDAP_SSL_PROTOCOL.getParameter() );
      }
      try
      {
        new InitialLdapContext( envLocal, null );
        authentication = true;
      }
      catch( NamingException e )
      {
        String error = CinepolisUtils.buildStringUsingMutable( AUTHENTICATION_ERROR, e.getMessage() );
        LOG.warn( error );
        LOG.warn( e.getMessage(), e );
        authentication = false;
      }
    }

    return authentication;
  }

}
