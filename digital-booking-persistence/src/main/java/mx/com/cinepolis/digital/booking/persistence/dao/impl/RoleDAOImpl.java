package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.RoleDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * The RoleDAO implementation
 * 
 * @author agustin.ramirez
 * 
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class RoleDAOImpl extends AbstractBaseDAO<RoleDO> implements RoleDAO {

	@PersistenceContext(unitName = "DigitalBookingPU")
	private EntityManager em;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Constructor default
	 */
	public RoleDAOImpl() {
		super(RoleDO.class);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO#getAllRoleActive()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CatalogTO> getAllRoleActive() {
		Query q = em.createNamedQuery( "RoleDO.findByFgActive" );
	    q.setParameter( "fgActive", Boolean.TRUE );
	    List<CatalogTO> roles = null;
	    roles =  (List<CatalogTO>) CollectionUtils.collect(q.getResultList(), new  RoleDOToCatalogTOTransformer());
		return roles;
	}

}
