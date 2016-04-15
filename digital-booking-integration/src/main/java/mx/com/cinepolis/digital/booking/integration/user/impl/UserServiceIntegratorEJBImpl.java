package mx.com.cinepolis.digital.booking.integration.user.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.user.UserServiceEJB;

/**
 * Implementation of UserServiceIntegratorEJB
 * 
 * @author agustin.ramirez
 * 
 */
@Stateless
@Local(value = UserServiceIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserServiceIntegratorEJBImpl implements UserServiceIntegratorEJB {

	/**
	 * UserServiceEJB
	 */
	@EJB
	private UserServiceEJB userServiceEJB;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB
	 * #saveUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void saveUser(UserTO userTO) {
		userServiceEJB.saveUser(userTO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB
	 * #deleteUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void deleteUser(UserTO userTO) {
		userServiceEJB.deleteUser(userTO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB
	 * #updateUser(mx.com.cinepolis.digital.booking.model.to.UserTO)
	 */
	@Override
	public void updateUser(UserTO userTO) {
		userServiceEJB.updateUser(userTO);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB
	 * #findAllUsers(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
	 */
	@Override
	public PagingResponseTO<UserTO> findAllUsers(PagingRequestTO request) {

		return userServiceEJB.findAllUsers(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB
	 * #findAllRolesActive()
	 */
	@Override
	public List<CatalogTO> findAllRolesActive() {

		return userServiceEJB.findAllRolesActive();
	}

}
