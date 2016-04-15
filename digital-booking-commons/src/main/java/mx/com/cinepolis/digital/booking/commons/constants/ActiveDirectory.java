package mx.com.cinepolis.digital.booking.commons.constants;

public enum ActiveDirectory
{
  CONTEXT_FACTORY("com.sun.jndi.ldap.LdapCtxFactory"), LDAP_VERSION("java.naming.ldap.version"), LDAP_AT(
      "active.directory.ldap.at"), LDAP_SSL_PROTOCOL("ssl"), LDAP_USE_SSL("active.directory.use.SSL"), LDAP_FACTORY_SOCKET(
      "java.naming.ldap.factory.socket"), LDAP_TRUST_STORE("javax.net.ssl.trustStore");
  /**
   * Parameter
   */
  private String parameter;

  private ActiveDirectory( String parameter )
  {
    this.parameter = parameter;
  }

  /**
   * Gets the parameter associated with the enumeration
   * 
   * @return
   */
  public String getParameter()
  {
    return parameter;
  }
}
