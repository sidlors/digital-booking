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
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminScreenFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB;

/**
 * Clase que implementa los metodos para la administracion de screen formats
 * 
 * @author kperez
 */
@Stateless
@Local(value = ServiceAdminScreenFormatIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminScreenFormatIntegratorEJBImpl implements ServiceAdminScreenFormatIntegratorEJB
{

  /**
   * Service CategoryEJB
   */
  @EJB
  private ServiceAdminCategoryEJB serviceAdminCategoryEJB;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.impl.ServiceAdminScreenFormatIntegratorEJBImpl#
   * saveScreenFormat (mx.com .cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void saveScreenFormat( CatalogTO screenFormat )
  {
    serviceAdminCategoryEJB.saveCategory( screenFormat, CategoryType.SCREEN_FORMAT );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.impl.ServiceAdminScreenFormatIntegratorEJBImpl#
   * deleteScreenFormat (mx .com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void deleteScreenFormat( CatalogTO screenFormat )
  {
    serviceAdminCategoryEJB.deleteCategory( screenFormat );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.impl.ServiceAdminScreenFormatIntegratorEJBImpl#
   * updateScreenFormat (mx .com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void updateScreenFormat( CatalogTO screenFormat )
  {
    serviceAdminCategoryEJB.updateCategory( screenFormat, CategoryType.SCREEN_FORMAT );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.impl.ServiceAdminScreenFormatIntegratorEJBImpl#
   * getCatalogScreenFormatSumary (mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<CatalogTO> getCatalogScreenFormatSumary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging( pagingRequestTO, CategoryType.SCREEN_FORMAT, Boolean.FALSE );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.impl.ServiceAdminScreenFormatIntegratorEJBImpl#
   * getScreenFormatAll ()
   */
  @Override
  public PagingResponseTO<CatalogTO> getScreenFormatAll()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ACTIVE, true );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging( pagingRequestTO, CategoryType.SCREEN_FORMAT,
      Boolean.TRUE );
  }

}
