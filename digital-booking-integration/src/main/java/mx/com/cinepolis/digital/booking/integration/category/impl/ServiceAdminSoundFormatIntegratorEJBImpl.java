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
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB;

/**
 * Clase que implementa los metodos para la administracion de sound formats
 * 
 * @author agustin.ramirez
 */
@Stateless
@Local(value = ServiceAdminSoundFormatIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminSoundFormatIntegratorEJBImpl implements ServiceAdminSoundFormatIntegratorEJB
{

  /**
   * Service CategoryEJB
   */
  @EJB
  private ServiceAdminCategoryEJB serviceAdminCategoryEJB;

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB#saveSoundFormat(mx.com
   * .cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void saveSoundFormat( CatalogTO soundFormat )
  {
    serviceAdminCategoryEJB.saveCategory( soundFormat, CategoryType.SOUND_FORMAT );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB#deleteSoundFormat(mx
   * .com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void deleteSoundFormat( CatalogTO soundFormat )
  {
    serviceAdminCategoryEJB.deleteCategory( soundFormat );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB#updateSoundFormat(mx
   * .com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void updateSoundFormat( CatalogTO soundFormat )
  {
    serviceAdminCategoryEJB.updateCategory( soundFormat, CategoryType.SOUND_FORMAT );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB#getCatalogSoundFormatSumary
   * (mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<CatalogTO> getCatalogSoundFormatSumary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging( pagingRequestTO, CategoryType.SOUND_FORMAT,
      Boolean.FALSE );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB#getSoundFormatAll()
   */
  @Override
  public PagingResponseTO<CatalogTO> getSoundFormatAll()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ACTIVE, true );
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    return serviceAdminCategoryEJB.getCatalogForCategoryAndPaging( pagingRequestTO, CategoryType.SOUND_FORMAT, Boolean.TRUE );
  }

}
