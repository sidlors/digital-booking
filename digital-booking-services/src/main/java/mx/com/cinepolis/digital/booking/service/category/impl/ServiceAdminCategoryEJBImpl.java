package mx.com.cinepolis.digital.booking.service.category.impl;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.CategoryQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa los metodos asociados una Categoria
 * 
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminCategoryEJBImpl implements ServiceAdminCategoryEJB
{
  private static final Logger LOG = LoggerFactory.getLogger( ServiceAdminCategoryEJBImpl.class );

  /**
   * Category DAO
   */
  @EJB
  private CategoryDAO categoryDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB
   * #saveMovieFormat(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void saveCategory( CatalogTO category, CategoryType categoryType )
  {
    ValidatorUtil.validateCatalog( category );
    if( CollectionUtils.isEmpty( categoryDAO.findByDsNameActive( category.getName(), categoryType.getId() ) ) )
    {
      // Se genera el registro de la categoría con lenguaje Inglés
      categoryDAO.save( category, categoryType, Language.ENGLISH );
      
     // Se genera el registro de la categoría con lenguaje Español
     categoryDAO.save( category, categoryType, Language.SPANISH );
    }
    else
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB
   * #deleteCategory(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void deleteCategory( CatalogTO category )
  {
    ValidatorUtil.validateCatalog( category );
    CategoryDO categoryDO = categoryDAO.find( category.getId().intValue() );
    if( categoryDO != null )
    {
      validateExistingScreens( categoryDO );
      validateExistingMovies( categoryDO );
    }

    categoryDAO.delete( category );

  }

  private void validateExistingMovies( CategoryDO remove )
  {
    if( CollectionUtils.isNotEmpty( remove.getEventDOList() ) )
    {
      for( EventDO e : remove.getEventDOList() )
      {
        if( e.isFgActive() )
        {
          LOG.error( "Movie " + e.getDsName() );
          LOG.error( "Movie Id" + e.getIdEvent() );
          if( remove.getIdCategoryType().getIdCategoryType().equals( CategoryType.SOUND_FORMAT.getId() ) )
          {
            throw DigitalBookingExceptionBuilder.build(
              DigitalBookingExceptionCode.CATEGORY_SOUND_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE,
              new Object[] { e.getIdEvent(), e.getDsName() } );
          }
          else
          {
            throw DigitalBookingExceptionBuilder.build(
              DigitalBookingExceptionCode.CATEGORY_MOVIE_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE,
              new Object[] { e.getIdEvent(), e.getDsName() } );
          }

        }
      }
    }

  }

  private void validateExistingScreens( CategoryDO remove )
  {
    if( CollectionUtils.isNotEmpty( remove.getScreenDOList() ) )
    {
      for( ScreenDO s : remove.getScreenDOList() )
      {
        if( s.isFgActive() && s.getIdTheater().isFgActive() )
        {
          TheaterDOToTheaterTOTransformer t = new TheaterDOToTheaterTOTransformer();
          TheaterTO theaterTO = (TheaterTO) t.transform( s.getIdTheater() );
          LOG.error( "Theater " + theaterTO.getName() );
          LOG.error( "Theater Id " + theaterTO.getId() );
          LOG.error( "Screen " + s.getNuScreen() );
          if( remove.getIdCategoryType().getIdCategoryType().equals( CategoryType.SOUND_FORMAT.getId() ) )
          {
            throw DigitalBookingExceptionBuilder.build(
              DigitalBookingExceptionCode.CATEGORY_SOUND_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN, new Object[] {
                  theaterTO.getId(), theaterTO.getName(), s.getNuScreen() } );
          }
          else if( remove.getIdCategoryType().getIdCategoryType().equals( CategoryType.MOVIE_FORMAT.getId() ) )
          {
            throw DigitalBookingExceptionBuilder.build(
              DigitalBookingExceptionCode.CATEGORY_MOVIE_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN, new Object[] {
                  theaterTO.getId(), theaterTO.getName(), s.getNuScreen() } );
          }
          else
          {
            throw DigitalBookingExceptionBuilder.build(
              DigitalBookingExceptionCode.CATEGORY_SCREEN_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN, new Object[] {
                  theaterTO.getId(), theaterTO.getName(), s.getNuScreen() } );
          }

        }
      }
    }

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB
   * #updateCategory(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void updateCategory( CatalogTO category, CategoryType categoryType )
  {
    ValidatorUtil.validateCatalog( category );
    //Se valida que no se duplique la categoría en la base de datos
    validateUpdateCategory( category, categoryType.getId(), Language.ENGLISH );
    //Se obtiene categoria con leguaje inglés sin modificar
  	List<CatalogTO> catalogTO = this.categoryDAO.findByIdCategory(category.getId().intValue(), Language.ENGLISH);
    
  	//Se modifica registro en inglés
    categoryDAO.update( category, Language.ENGLISH );
    
    //Se consulta la categoría en lenguaje español para obtener su id
    List<CategoryDO> categoryDOs = categoryDAO.findByDsNameActive( catalogTO.get(0).getName(), categoryType.getId(), Language.SPANISH );
    category.setId( categoryDOs.get(0).getIdCategory().longValue());
   //Se modifica la categoria con lenguaje español
    categoryDAO.update( category, Language.SPANISH );
  }
  
  /**
   * Método que valida que la característica a actualizar no exista en base de datos
   */
  private void validateUpdateCategory( CatalogTO category, int idCategoryType, Language language )
  {
	List<CategoryDO> categoryDOs = categoryDAO.findByDsNameActive( category.getName(), idCategoryType, Language.ENGLISH );
	for( CategoryDO categoryDO : categoryDOs )
	{
	  if( !categoryDO.getIdCategory().equals( category.getId().intValue() ) )
	  {
	    throw DigitalBookingExceptionBuilder
	        .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
	  }
	}
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB #
   * getCatalogForCategoryAndPaging(mx.com.cinepolis.digital.booking.model.to. PagingRequestTO)
   */
  @Override
  public PagingResponseTO<CatalogTO> getCatalogForCategoryAndPaging( PagingRequestTO pagingRequestTO,
      CategoryType categoryType, Boolean isAll )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    pagingRequestTO.setNeedsPaging( (isAll ? Boolean.FALSE : Boolean.TRUE) );
    if( pagingRequestTO.getFilters() == null )
    {
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    }
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, categoryType.getId() );
    return categoryDAO.findAllByPaging( pagingRequestTO );
  }

}
