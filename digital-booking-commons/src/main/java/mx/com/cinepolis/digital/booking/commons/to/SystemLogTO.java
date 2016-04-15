package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SystemLogTO extends AbstractTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 5310604429391536231L;
  /**
   * Log system identifier
   */
  private Long idSystemLog;
  /**
   * Transfer object of user
   */
  private UserTO userTO;
  /**
   * transfer object of process
   */
  private CatalogTO processTO;
  
  /**
   * transfer object of  operation 
   */
  private CatalogTO operationTO;
  /**
   * operation date 
   */
  private Date dtOperation;
 /**
  * Constructor by default
  */
  public SystemLogTO()
  {
    
  }
  /**
   * Constructor by identifier
   */
   public SystemLogTO(Long idSystemLog)
   {
     this.idSystemLog= idSystemLog;
   }
  /**
   * @return the idSystemLog
   */
  public Long getIdSystemLog()
  {
    return idSystemLog;
  }
  /**
   * @param idSystemLog the idSystemLog to set
   */
  public void setIdSystemLog( Long idSystemLog )
  {
    this.idSystemLog = idSystemLog;
  }
  /**
   * @return the userTO
   */
  public UserTO getUserTO()
  {
    return userTO;
  }
  /**
   * @param userTO the userTO to set
   */
  public void setUserTO( UserTO userTO )
  {
    this.userTO = userTO;
  }
  /**
   * @return the processTO
   */
  public CatalogTO getProcessTO()
  {
    return processTO;
  }
  /**
   * @param processTO the processTO to set
   */
  public void setProcessTO( CatalogTO processTO )
  {
    this.processTO = processTO;
  }
  /**
   * @return the operationTO
   */
  public CatalogTO getOperationTO()
  {
    return operationTO;
  }
  /**
   * @param operationTO the operationTO to set
   */
  public void setOperationTO( CatalogTO operationTO )
  {
    this.operationTO = operationTO;
  }
  /**
   * @return the dtOperation
   */
  public Date getDtOperation()
  {
    return dtOperation;
  }
  /**
   * @param dtOperation the dtOperation to set
   */
  public void setDtOperation( Date dtOperation )
  {
    this.dtOperation = dtOperation;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object other )
  {
    boolean result = false;
    if( this == other )
    {
      result = true;
    }
    else if( other instanceof SystemLogTO )
    {
      SystemLogTO o = (SystemLogTO) other;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( idSystemLog, o.idSystemLog );
      result = builder.isEquals();
    }
    return result;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append( this.idSystemLog );
    return builder.toHashCode();
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.getIdSystemLog() );
    builder.append( "name", this.getOperationTO() );
    return builder.toString();
  }
   
}
