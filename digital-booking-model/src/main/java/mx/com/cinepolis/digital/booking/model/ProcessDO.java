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
 * JPA entity for C_PROCESS
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_PROCESS")
@NamedQueries({ @NamedQuery(name = "ProcessDO.findAll", query = "SELECT p FROM ProcessDO p"),
    @NamedQuery(name = "ProcessDO.findById", query = "SELECT p FROM ProcessDO p where p.idProcess = :idProcess") })
public class ProcessDO extends AbstractEntity<ProcessDO>
{

  /**
   * 
   */
  private static final long serialVersionUID = 9141299280491820564L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_PROCESS", nullable = false)
  private Integer idProcess;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idProcess", fetch = FetchType.LAZY)
  private List<ProcessLanguageDO> processLanguegeDOList;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idProcess", fetch = FetchType.LAZY)
  private List<SystemLogDO> systemLogDOList;

  /**
   * Constructor by default
   */
  public ProcessDO()
  {
  }

  /**
   * Constructor by identifier
   * 
   * @param idProcess
   */
  public ProcessDO( Integer idProcess )
  {
    this.idProcess = idProcess;
  }

  /**
   * @return the idProcess
   */
  public Integer getIdProcess()
  {
    return idProcess;
  }

  /**
   * @param idProcess the idProcess to set
   */
  public void setIdProcess( Integer idProcess )
  {
    this.idProcess = idProcess;
  }

  /**
   * @return the processLanguegeDOList
   */
  public List<ProcessLanguageDO> getProcessLanguegeDOList()
  {
    return processLanguegeDOList;
  }

  /**
   * @param processLanguegeDOList the processLanguegeDOList to set
   */
  public void setProcessLanguegeDOList( List<ProcessLanguageDO> processLanguegeDOList )
  {
    this.processLanguegeDOList = processLanguegeDOList;
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
  public int compareTo( ProcessDO arg0 )
  {

    return this.idProcess.compareTo( arg0.idProcess );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof ProcessDO) )
    {
      return false;
    }
    ProcessDO other = (ProcessDO) obj;
    if( (this.idProcess == null && other.idProcess != null)
        || (this.idProcess != null && !this.idProcess.equals( other.idProcess )) )
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
    hash += (idProcess != null ? idProcess.hashCode() : 0);
    return hash;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "ProcessDO [idProcess=", this.idProcess ).append( "]" ).toString();
  }

}
