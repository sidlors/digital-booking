package mx.com.cinepolis.digital.booking.service.security.impl;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.naming.Context;

import mx.com.cinepolis.digital.booking.commons.constants.ActiveDirectory;
import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.Operation;
import mx.com.cinepolis.digital.booking.commons.constants.Process;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.model.SystemMenuDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB;
import mx.com.cinepolis.digital.booking.service.log.ServiceLog;
import mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.LoggingServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.activedirectory.ActiveDirectoryUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security services implementation.
 * 
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class, LoggingServiceInterceptor.class })
public class ServiceSecurityEJBImpl implements ServiceSecurityEJB
{

  private static final Logger LOG = LoggerFactory.getLogger( ServiceSecurityEJB.class );
  
  private static final String CONFIG_URL = "/config.do";

  private ActiveDirectoryUtil activeDirectoryUtil;

  @EJB
  private UserDAO userDAO;

  @EJB
  private SystemMenuDAO systemMenuDAO;

  @EJB
  private ConfigurationServiceEJB configurationServiceEJB;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void init()
  {
    Hashtable<String, Object> env = new Hashtable<String, Object>();

    String ldapUrl = configurationServiceEJB.findParameterByName(
      Configuration.ACTIVE_DIRECTORY_LDAP_URL.getParameter() ).getDsValue();
    String ldapAuthMethod = configurationServiceEJB.findParameterByName(
      Configuration.ACTIVE_DIRECTORY_LDAP_AUTH_METHOD.getParameter() ).getDsValue();
    String ldapVersion = configurationServiceEJB.findParameterByName(
      Configuration.ACTIVE_DIRECTORY_LDAP_VERSION.getParameter() ).getDsValue();
    String ldapAt = configurationServiceEJB.findParameterByName( Configuration.ACTIVE_DIRECTORY_LDAP_AT.getParameter() )
        .getDsValue();
    String trustStore = configurationServiceEJB.findParameterByName(
      Configuration.ACTIVE_DIRECTORY_LDAP_TRUST_STORE.getParameter() ).getDsValue();
    Boolean useSSL = Boolean.valueOf( configurationServiceEJB.findParameterByName(
      Configuration.ACTIVE_DIRECTORY_USE_SSL.getParameter() ).getDsValue() );
    env.put( Context.INITIAL_CONTEXT_FACTORY, ActiveDirectory.CONTEXT_FACTORY.getParameter() );
    env.put( Context.PROVIDER_URL, ldapUrl );
    env.put( Context.SECURITY_AUTHENTICATION, ldapAuthMethod );
    env.put( ActiveDirectory.LDAP_VERSION.getParameter(), ldapVersion );
    env.put( ActiveDirectory.LDAP_AT.getParameter(), ldapAt );
    env.put( ActiveDirectory.LDAP_USE_SSL.getParameter(), useSSL );
    env.put( ActiveDirectory.LDAP_TRUST_STORE.getParameter(), trustStore );

    activeDirectoryUtil = new ActiveDirectoryUtil( env );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB#authenticate(mx.com.cinepolis.digital.booking
   * .model.to.UserTO)
   */
  @Override
  @ServiceLog(operation = Operation.LOGIN, process = Process.SYSTEM )
  public void authenticate( UserTO userTO )
  {
    // Valida que exista el usuario en la bd
    if( isInDatabase( userTO ) )
    {
      // Valida el usuario en LDAP/AD
     /* if( !activeDirectoryUtil.authenticate( userTO.getName(), userTO.getPassword() ) )
      {
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SECURITY_ERROR_PASSWORD_INVALID );
      }*/
    }
    else
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SECURITY_ERROR_USER_DOES_NOT_EXISTS );
    }
  }

  private boolean isInDatabase( UserTO userTO )
  {
    UserTO user = findUserDetail( userTO.getName() );
    boolean isUserValid = user != null;
    if( isUserValid ) {
      userTO.setId( user.getId() );
      ((AbstractTO) userTO).setUserId( user.getId() );
      userTO.setTimestamp( Calendar.getInstance().getTime() );
    }
    return isUserValid;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB#findUserDetail(java.lang.String)
   */
  @Override
  public UserTO findUserDetail( String username )
  {
    return userDAO.getByUsername( username );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB#findMenuByUser(java.lang.Long)
   */
  @Override
  public SystemMenuTO findMenuByUser( Long idUser )
  {
    return systemMenuDAO.findMenuByUser( idUser );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB#isSystemMenuAllowed(java.lang.String,
   * java.util.List)
   */
  @Override
  public boolean isSystemMenuAllowed( String url, List<CatalogTO> userRoles )
  {
    return systemMenuDAO.isSystemMenuAllowed( url, userRoles );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB#clearJPACache()
   */
  @Override
  public void clearJPACache()
  {
    systemMenuDAO.clearJPACache();
  }

  /**
   * @param configurationServiceEJB the configurationServiceEJB to set
   */
  public void setConfigurationServiceEJB( ConfigurationServiceEJB configurationServiceEJB )
  {
    this.configurationServiceEJB = configurationServiceEJB;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasConfigPage( Long idUser )
  {
    boolean hasConfig = false;
    UserDO userDO = userDAO.find( idUser.intValue() );
    for( RoleDO roleDO : userDO.getRoleDOList() )
    {
      for( SystemMenuDO systemMenuDO : roleDO.getSystemMenuDOList() )
      {
        hasConfig |= StringUtils.contains( systemMenuDO.getDsSystemMenuUrl(), CONFIG_URL );
      }
    }
    return hasConfig;
  }
  
  
  @Override
  @ServiceLog(operation = Operation.LOGOUT, process = Process.SYSTEM )
  public void logOut(UserTO userTO)
  {
    if( isInDatabase( userTO ) )
    {
      LOG.debug("LogOut user: " + userTO.getName());
    }
    else
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SECURITY_ERROR_USER_DOES_NOT_EXISTS );
    }
  }


}
