package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.ProcessDO;
import mx.com.cinepolis.digital.booking.model.ProcessLanguageDO;

import org.apache.commons.collections.Transformer;
/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.ProcessDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author jcarbajal
 */
public class ProcessDOToCatalogTOTransformer implements Transformer
{

  private Language language;
  /**
   * Constructor by default
   */
  public ProcessDOToCatalogTOTransformer( )
  {
    this.language = Language.ENGLISH;
  }
  /**
   * Constructor by identifier
   * @param language
   */
  public ProcessDOToCatalogTOTransformer( Language language )
  {
    this.language = language;
  }
  /**
   * Method for transform
   */
  @Override
  public Object transform( Object input )
  {
    CatalogTO processTO=null;
    if(input instanceof ProcessDO)
    {
      ProcessDO processDO=(ProcessDO)input;
      processTO= new CatalogTO();
      processTO.setId( processDO.getIdProcess().longValue() );
      for(ProcessLanguageDO processLanguageDO:processDO.getProcessLanguegeDOList() )
      {
        if(processLanguageDO.getIdLanguage().getIdLanguage().intValue() == this.language.getId() )
        {
          processTO.setName( processLanguageDO.getDsName() );
          break;
        }
      }
    }
    return processTO;
  }
  public void setLanguage( Language language )
  {
    this.language = language;
  }

}
