package mx.com.cinepolis.digital.booking.service.category;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface que define los metodos asociados ala administracion de formatos
 * 
 * @author rgarcia
 */
@Local
public interface ServiceAdminCategoryEJB
{
  /**
   * Salva una Categoria
   * 
   * @param category
   */
  void saveCategory( CatalogTO category, CategoryType categoryType );

  /**
   * Elimina una categoria
   * 
   * @param category
   */
  void deleteCategory( CatalogTO category );

  /**
   * Actualiza una categoria
   * 
   * @param category
   */
  void updateCategory( CatalogTO category, CategoryType categoryType );

  /**
   * Realiza la busqueda
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<CatalogTO> getCatalogForCategoryAndPaging( PagingRequestTO pagingRequestTO,
      CategoryType categoryType, Boolean isAll );

}
