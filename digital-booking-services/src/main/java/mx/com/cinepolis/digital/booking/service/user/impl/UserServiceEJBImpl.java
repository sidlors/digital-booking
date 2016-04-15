package mx.com.cinepolis.digital.booking.service.user.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.service.user.UserServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

/**
 * The UserService EJB
 * @author agustin.ramirez
 *
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class UserServiceEJBImpl implements UserServiceEJB{
	
	/**
	 * UserDAO
	 */
	@EJB
	private UserDAO userDAO;
	/**
	 * Role DAO
	 */
	@EJB
	private RoleDAO roleDAO;
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.user.UserServiceEJB#saveUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void saveUser(UserTO userTO) {
		ValidatorUtil.validateUser(userTO);
		UserTO  user  = userDAO.getByUsername(userTO.getName());
		if(user != null){
			throw DigitalBookingExceptionBuilder.build(DigitalBookingExceptionCode.USER_THE_USERNAME_IS_DUPLICATE);
		}
		userDAO.save(userTO);
	}

	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.user.UserServiceEJB#deleteUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void deleteUser(UserTO userTO) {
		if(userTO == null || userTO.getId() == null){
			throw DigitalBookingExceptionBuilder.build(DigitalBookingExceptionCode.USER_IS_NULL);
		}
		
		userDAO.delete(userTO);
		
	}

	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.user.UserServiceEJB#updateUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void updateUser(UserTO userTO) {
		ValidatorUtil.validateUser(userTO);
		userDAO.update(userTO);
		
	}

	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.user.UserServiceEJB#findAllUsers(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
	 */
	@Override
	public PagingResponseTO<UserTO> findAllUsers(PagingRequestTO request) {
	    ValidatorUtil.validatePagingRequest( request );
	    PagingResponseTO<UserTO> response = null ;
	    response = userDAO.findAllByPaging(request);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.service.user.UserServiceEJB#findAllRolesActive()
	 */
	@Override
	public List<CatalogTO> findAllRolesActive() {
		
		return roleDAO.getAllRoleActive();
	}

}
