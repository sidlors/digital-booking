package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.SystemLogDO;

import org.apache.commons.collections.Transformer;

public class SystemLogDOToSystemLogTOTransformer implements Transformer
{
  private Language language;

  /**
   * Constructor by default
   */
  public SystemLogDOToSystemLogTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor by language
   */
  public SystemLogDOToSystemLogTOTransformer( Language language )
  {
    this.language = language;
  }
  /* (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @Override
  public Object transform( Object input )
  {
    SystemLogTO systemLogTO=null;
    if(input instanceof SystemLogDO)
    {
      SystemLogDO systemLogDO=(SystemLogDO) input;
      systemLogTO=new SystemLogTO();
      systemLogTO.setDtOperation( systemLogDO.getDtOperation() );
      systemLogTO.setOperationTO( (CatalogTO) new OperationDOToCatalogTOTransformer( language ).transform( systemLogDO.getIdOperation() ) );
      systemLogTO.setIdSystemLog( systemLogDO.getIdSystemLog() );
      systemLogTO.setProcessTO( (CatalogTO) new ProcessDOToCatalogTOTransformer( language ).transform( systemLogDO.getIdProcess() ) );
      systemLogTO.setUserTO( (UserTO) new UserDOToUserTOTransformer( language ).transform( systemLogDO.getIdUser() ) );
      
    }
    return systemLogTO;
  }
/**
 * 
 * @param language 
 */
  public void setLanguage( Language language )
  {
    this.language = language;
  }

}
