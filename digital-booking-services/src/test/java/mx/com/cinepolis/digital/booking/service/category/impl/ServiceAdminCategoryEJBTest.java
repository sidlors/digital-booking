package mx.com.cinepolis.digital.booking.service.category.impl;

import java.util.Calendar;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.dao.util.CategoryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CategoryDAOImpl;
import mx.com.cinepolis.digital.booking.service.category.ServiceAdminCategoryEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas de ServiceAdminCategoryEJB
 * @author agustin.ramirez
 *
 */
public class ServiceAdminCategoryEJBTest extends AbstractDBEJBTestUnit{
	
	/**
	 * Service
	 */
	private ServiceAdminCategoryEJB serviceAdminCategoryEJB;
	
	/**
	 * CategoryDao
	 */
	private CategoryDAO categoryDAO;
	/**
	 * Transformer
	 */
	private CategoryDOToCatalogTOTransformer transformer;
	
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
	 */
	  @Before
	  public void setUp()
	  {	
	    // instanciar el servicio
		  serviceAdminCategoryEJB = new ServiceAdminCategoryEJBImpl();
		  categoryDAO = new CategoryDAOImpl();
		  transformer = new CategoryDOToCatalogTOTransformer();

	    // Llamar la prueba padre para obtener el EntityManager
	    super.setUp();	
	    connect( serviceAdminCategoryEJB );
	    connect(categoryDAO);

	  }
	  /**
	   * Prueba para el guardado de Categoria SOUND FORMAT
	   */
	  @Test
	  public void testSaveCategorySound(){
		  CatalogTO  newSound = new CatalogTO();
		  newSound.setFgActive(Boolean.TRUE);
		  newSound.setName("SOUND_TEST");
		  newSound.setTimestamp(Calendar.getInstance().getTime());
		  newSound.setUserId(1L);
		  newSound.setUsername("admin");
		  serviceAdminCategoryEJB.saveCategory(newSound, CategoryType.SOUND_FORMAT);
		  Assert.assertNotNull(newSound.getId());
		  
	  }
	  
	  /**
	   * Prueba para el guardado de Categoria Movie format
	   */
	  @Test
	  public void testSaveCategoryMovieFormat(){
		  CatalogTO  newMovieFormat = new CatalogTO();
		  newMovieFormat.setFgActive(Boolean.TRUE);
		  newMovieFormat.setName("MOVIE_TEST");
		  newMovieFormat.setTimestamp(Calendar.getInstance().getTime());
		  newMovieFormat.setUserId(1L);
		  newMovieFormat.setUsername("admin");
		  serviceAdminCategoryEJB.saveCategory(newMovieFormat, CategoryType.MOVIE_FORMAT);
		  Assert.assertNotNull(newMovieFormat.getId());
		  
	  }
	  
	  /**
	   * Prueba para el borrado logico de una categoria
	   */
	  @Test
	  public void testDeleteCategory(){
		  CategoryDO categoryDO = categoryDAO.find(10);
		  CatalogTO catalogToDelete = (CatalogTO) transformer.transform(categoryDO);
		  catalogToDelete.setUserId( 1L );
		  serviceAdminCategoryEJB.deleteCategory(catalogToDelete);
		  categoryDAO.flush();
		  CategoryDO categoryDeleted = categoryDAO.find(10);
		  Assert.assertFalse(categoryDeleted.isFgActive());
	  }
	  
	  /**
	   * Prueba para  la actualizacion de una categoria
	   */
	  @Test(expected = ArrayIndexOutOfBoundsException.class)
	  public void testUpdateCategory(){
		  CategoryDO categoryDO = categoryDAO.find(1);
		  CatalogTO catalogToUpdate = (CatalogTO) transformer.transform(categoryDO);
		  catalogToUpdate.setFgActive( false );
		  serviceAdminCategoryEJB.updateCategory(catalogToUpdate, CategoryType.SOUND_FORMAT);
		  //CategoryDO categoryUpdate = categoryDAO.find(1);
		  //CatalogTO catalogUpdated = (CatalogTO) transformer.transform(categoryUpdate);
		  //Assert.assertTrue(catalogUpdated.getName().equals("DIGITAL"));
	  }
	  
	  /**
	   * Prueba para obtener los  movie formats con paginacion
	   */
	  @Test
	  public void testGetCatalogForCategoryAndPaging_MOVIE_FORMAT(){
		  
		  PagingRequestTO pagingRequestTO = new PagingRequestTO();
		    pagingRequestTO.setPage( 0 );
		    pagingRequestTO.setPageSize( 3 );

		    PagingResponseTO<CatalogTO> response = serviceAdminCategoryEJB.getCatalogForCategoryAndPaging(pagingRequestTO, CategoryType.MOVIE_FORMAT, Boolean.FALSE);

		    Assert.assertNotNull( response );

		    for( CatalogTO catalogTO : response.getElements() )
		    {
		      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
		    }
		    System.out.println( response.getTotalCount() );

		  }
	  /**
	   * PRueba para obtener los sound formats con paginacion
	   */
	  @Test
	  public void testGetCatalogForCategoryAndPaging_SOUND_FORMAT(){
		  
		  PagingRequestTO pagingRequestTO = new PagingRequestTO();
		    pagingRequestTO.setPage( 0 );
		    pagingRequestTO.setPageSize( 3 );

		    PagingResponseTO<CatalogTO> response = serviceAdminCategoryEJB.getCatalogForCategoryAndPaging(pagingRequestTO, CategoryType.SOUND_FORMAT, Boolean.FALSE);

		    Assert.assertNotNull( response );

		    for( CatalogTO catalogTO : response.getElements() )
		    {
		      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
		    }
		    System.out.println( response.getTotalCount() );

		  }
	  
	  //TODO faltarian pruebas sin paginacion , estas se deberan implementar cuando
	  // se implemente lo del parametro NeedsPaging
	  
}
