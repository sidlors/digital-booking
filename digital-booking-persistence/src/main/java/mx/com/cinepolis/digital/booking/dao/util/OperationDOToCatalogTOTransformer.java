package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.OperationDO;
import mx.com.cinepolis.digital.booking.model.OperationLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.OperationDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author jcarbajal
 */
public class OperationDOToCatalogTOTransformer implements Transformer 
{
  private Language language;
  
  /**
   * @param language
   */
  public OperationDOToCatalogTOTransformer(  )
  {
    this.language = Language.ENGLISH;
  }
  /**
   * @param language
   */
  public OperationDOToCatalogTOTransformer( Language language )
  {
    this.language = language;
  }

  /* (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @Override
  public Object transform( Object input )
  {
    CatalogTO operationTO=null;
    if(input instanceof OperationDO)
    {
      OperationDO operationDO=(OperationDO)input;
      operationTO=new CatalogTO();
      operationTO.setId( operationDO.getIdOperation().longValue() );
      for(OperationLanguageDO operationLanguageDO:operationDO.getOperationLanguageDOList())
      {
        if(operationLanguageDO.getIdLanguage().getIdLanguage().intValue() == language.getId() )
        {
          operationTO.setName( operationLanguageDO.getDsName() );
          break;
        }
      }
    }
    return operationTO;
  }
  /**
   * @param language the language to set
   */
  public void setLanguage( Language language )
  {
    this.language = language;
  }


}
