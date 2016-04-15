package mx.com.cinepolis.digital.booking.integration.category.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.query.CategoryQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB;

/**
 * Clase que implementa los metodos de ServiceAdminMovieFormat
 * @author agustin.ramirez
 *
 */
@Stateless
@Local(value = ServiceAdminMovieFormatIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminMovieFormatIntegratorEJBImpl implements ServiceAdminMovieFormatIntegratorEJB{
	/**
	 * Service Admin Category EJB
	 */
	@EJB
	private ServiceAdminCategoryEJB serviceAdminCategoryEJB;
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB#saveMovieFormat(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
	 */
	@Override
	public void saveMovieFormat(CatalogTO movieFormat) {
		serviceAdminCategoryEJB.saveCategory(movieFormat, CategoryType.MOVIE_FORMAT);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB#deleteMovieFormat(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
	 */
	@Override
	public void deleteMovieFormat(CatalogTO movieFormat) {
		serviceAdminCategoryEJB.deleteCategory(movieFormat);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB#updateMovieFormat(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
	 */
	@Override
	public void updateMovieFormat(CatalogTO movieFormat) {
		serviceAdminCategoryEJB.updateCategory(movieFormat, CategoryType.MOVIE_FORMAT);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB#getCatalogMovieFormatSumary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
	 */
	@Override
	public PagingResponseTO<CatalogTO> getCatalogMovieFormatSumary(
			PagingRequestTO pagingRequestTO) {

		return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging(pagingRequestTO, CategoryType.MOVIE_FORMAT, Boolean.FALSE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB#getMovieFormatAll()
	 */
	@Override
	public PagingResponseTO<CatalogTO> getMovieFormatAll() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>() );
		pagingRequestTO.getFilters().put(CategoryQuery.CATEGORY_ACTIVE, true);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
		pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
		
		return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging(pagingRequestTO, CategoryType.MOVIE_FORMAT, Boolean.TRUE);
	}

}
