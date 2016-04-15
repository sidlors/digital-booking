package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for C_OPERATION
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_OPERATION")
@NamedQueries({
    @NamedQuery(name = "OperationDO.findAll", query = "SELECT o FROM OperationDO o"),
    @NamedQuery(name = "OperationDO.findById", query = "SELECT o FROM OperationDO o where o.idOperation = :idOperation") })
public class OperationDO extends AbstractEntity<OperationDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = -8053572518757029816L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_OPERATION", nullable = false)
  private Integer idOperation;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idOperation", fetch = FetchType.LAZY)
  private List<OperationLanguageDO> OperationLanguageDOList;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idOperation", fetch = FetchType.LAZY)
  private List<SystemLogDO> systemLogDOList;

  /**
   * Constructor by default
   */
  public OperationDO()
  {
  }

  /**
   * Constructor by identifier
   * 
   * @param idOperation
   */
  public OperationDO( Integer idOperation )
  {
    this.idOperation = idOperation;
  }

  /**
   * @return the idOperation
   */
  public Integer getIdOperation()
  {
    return idOperation;
  }

  /**
   * @param idOperation the idOperation to set
   */
  public void setIdOperation( Integer idOperation )
  {
    this.idOperation = idOperation;
  }

  /**
   * @return the operationLanguageDOList
   */
  public List<OperationLanguageDO> getOperationLanguageDOList()
  {
    return OperationLanguageDOList;
  }

  /**
   * @param operationLanguageDOList the operationLanguageDOList to set
   */
  public void setOperationLanguageDOList( List<OperationLanguageDO> operationLanguageDOList )
  {
    OperationLanguageDOList = operationLanguageDOList;
  }

  /**
   * @return the systemLogDOList
   */
  public List<SystemLogDO> getSystemLogDOList()
  {
    return systemLogDOList;
  }

  /**
   * @param systemLogDOList the systemLogDOList to set
   */
  public void setSystemLogDOList( List<SystemLogDO> systemLogDOList )
  {
    this.systemLogDOList = systemLogDOList;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo( OperationDO arg0 )
  {
    return this.idOperation.compareTo( arg0.idOperation );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof OperationDO) )
    {
      return false;
    }
    OperationDO other = (OperationDO) obj;
    if( (this.idOperation == null && other.idOperation != null)
        || (this.idOperation != null && !this.idOperation.equals( other.idOperation )) )
    {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#hashCode()
   */
  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idOperation != null ? idOperation.hashCode() : 0);
    return hash;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "OperationDO [idOperation=", this.idOperation ).toString();
  }

}
