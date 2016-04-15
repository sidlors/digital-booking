package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO;
/**
 * Class that implements the methods of the Data Access Object related to Email. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO}
 * 
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class EmailDAOImpl extends AbstractBaseDAO<EmailDO> implements EmailDAO {
	
	/**
	 * Entity Manager
	 */
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
	 * Constructor
	 */
	public EmailDAOImpl() {
		super(EmailDO.class);
	}
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO#findByUserId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailDO> findByUserId(Long userId) {
		Query q = em.createNamedQuery( "EmailDO.findByUserId" );
	    q.setParameter( "userId", userId.intValue() );
	    return  q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO#findByPersonId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmailDO> findByPersonId(Long idPerson) {
		Query q = em.createNamedQuery( "EmailDO.findByPersonId" );
	    q.setParameter( "idPerson", idPerson.intValue() );
	    return q.getResultList();
	}

}
