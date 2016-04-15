package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.MenuElementComparator;
import mx.com.cinepolis.digital.booking.dao.util.RoleDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.SystemMenuDOToSystemMenuTOTransformer;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.model.SystemMenuDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class that implements the methods of the Data Access Object related to a system menu element. Implementation of the
 * interface {@link mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO}
 * 
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class SystemMenuDAOImpl extends AbstractBaseDAO<SystemMenuDO> implements SystemMenuDAO
{
  private static final String PERCENTAGE = "%";
  private static final String MENU_URL_PARAMETER = "dsSystemMenuUrl";
  private static final String FIND_MENU_BY_URL_QUERY = "SystemMenuDO.findByUrl";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private UserDAO userDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /**
   * Constructor default
   */
  public SystemMenuDAOImpl()
  {
    super( SystemMenuDO.class );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO#findMenuByUser(java.lang.Long)
   */
  @Override
  public SystemMenuTO findMenuByUser( Long idUser )
  {
    SystemMenuTO fullSystemMenuTO = new SystemMenuTO();
    List<SystemMenuTO> menuElements = new ArrayList<SystemMenuTO>();

    UserDO userDO = userDAO.find( idUser.intValue() );
    Set<SystemMenuDO> allSystemMenuDOs = new HashSet<SystemMenuDO>();
    for( RoleDO roleDO : userDO.getRoleDOList() )
    {
      allSystemMenuDOs.addAll( roleDO.getSystemMenuDOList() );
    }

    Map<Integer, SystemMenuTO> menusMap = createMenusMap( allSystemMenuDOs );
    for( SystemMenuDO menuDO : allSystemMenuDOs )
    {
      SystemMenuDO parentMenu = menuDO.getIdParentSystemMenu();
      if( parentMenu != null )
      {
        SystemMenuTO parentTO = menusMap.get( parentMenu.getIdSystemMenu() );
        if( parentTO == null )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.MENU_EXCEPTION );
        }
        parentTO.addChild( menusMap.get( menuDO.getIdSystemMenu() ) );
      }
      else if( menuDO.getOrder().intValue() != 0 )
      {
        menuElements.add( menusMap.get( menuDO.getIdSystemMenu() ) );
      }
    }
    sortMenuElements( menuElements );
    fullSystemMenuTO.setChildren( menuElements );
    return fullSystemMenuTO;
  }

  private void sortMenuElements( List<SystemMenuTO> menuElements )
  {
    for( SystemMenuTO menuTO : menuElements )
    {
      List<SystemMenuTO> children = menuTO.getChildren();
      if( CollectionUtils.isNotEmpty( children ) )
      {
        Collections.sort( children, new MenuElementComparator<SystemMenuTO>() );
        sortMenuElements( children );
      }
    }
  }

  private Map<Integer, SystemMenuTO> createMenusMap( Set<SystemMenuDO> allSystemMenuDOs )
  {
    Map<Integer, SystemMenuTO> menusMap = new HashMap<Integer, SystemMenuTO>();
    List<SystemMenuTO> menuTOs = new ArrayList<SystemMenuTO>();
    CollectionUtils.collect( allSystemMenuDOs, new SystemMenuDOToSystemMenuTOTransformer(), menuTOs );
    for( SystemMenuTO menuTO : menuTOs )
    {
      menusMap.put( menuTO.getId().intValue(), menuTO );
    }
    return menusMap;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO#isSystemMenuAllowed(java.lang.String,
   * java.util.List)
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean isSystemMenuAllowed( String url, List<CatalogTO> userRoles )
  {
    boolean isAllowed = false;
    Query query = em.createNamedQuery( FIND_MENU_BY_URL_QUERY );
    query.setParameter( MENU_URL_PARAMETER, CinepolisUtils.buildStringUsingMutable( PERCENTAGE, url, PERCENTAGE ) );
    SystemMenuDO systemMenuDO = CinepolisUtils.findFirstElement( (List<SystemMenuDO>) query.getResultList() );
    if( systemMenuDO != null )
    {
      List<CatalogTO> menuRoles = new ArrayList<CatalogTO>();
      CollectionUtils.collect( systemMenuDO.getRoleDOList(), new RoleDOToCatalogTOTransformer(), menuRoles );
      isAllowed = CollectionUtils.containsAny( menuRoles, userRoles );
    }
    return isAllowed;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO#clearJPACache()
   */
  @Override
  public void clearJPACache()
  {
    em.getEntityManagerFactory().getCache().evictAll();
  }

}
