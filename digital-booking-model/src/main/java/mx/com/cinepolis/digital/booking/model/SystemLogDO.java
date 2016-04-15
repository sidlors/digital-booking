package mx.com.cinepolis.digital.booking.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * JPA entity for K_SYSTEM_LOG
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_SYSTEM_LOG")
@NamedQueries({
    @NamedQuery(name = "SystemLogDO.findAll", query = "SELECT sl FROM SystemLogDO sl"),
    @NamedQuery(name = "SystemLogDO.findById", query = "SELECT sl FROM SystemLogDO sl where sl.idSystemLog = :idSystemLog") })
public class SystemLogDO extends AbstractEntity<SystemLogDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = -5273962431999888454L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_SYSTEM_LOG", nullable = false)
  private Long idSystemLog;

  @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserDO idUser;

  @JoinColumn(name = "ID_OPERATION", referencedColumnName = "ID_OPERATION", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private OperationDO idOperation;

  @JoinColumn(name = "ID_PROCESS", referencedColumnName = "ID_PROCESS", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ProcessDO idProcess;

  @Column(name = "DT_OPERATION")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtOperation;

  
  
  /**
   * Constructor by default
   */
  public SystemLogDO()
  {
  }

  /**
   * Constructor by identifier
   * 
   * @param idSystemLog
   */
  public SystemLogDO( Long idSystemLog )
  {
    this.idSystemLog = idSystemLog;
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
   * @return the idUser
   */
  public UserDO getIdUser()
  {
    return idUser;
  }

  /**
   * @param idUser the idUser to set
   */
  public void setIdUser( UserDO idUser )
  {
    this.idUser = idUser;
  }

  /**
   * @return the idOperation
   */
  public OperationDO getIdOperation()
  {
    return idOperation;
  }

  /**
   * @param idOperation the idOperation to set
   */
  public void setIdOperation( OperationDO idOperation )
  {
    this.idOperation = idOperation;
  }

  /**
   * @return the idProcess
   */
  public ProcessDO getIdProcess()
  {
    return idProcess;
  }

  /**
   * @param idProcess the idProcess to set
   */
  public void setIdProcess( ProcessDO idProcess )
  {
    this.idProcess = idProcess;
  }

  /**
   * @return the dtOperation
   */
  public Date getDtOperation()
  {
    return CinepolisUtils.enhancedClone( dtOperation );
  }

  /**
   * @param dtOperation the dtOperation to set
   */
  public void setDtOperation( Date dtOperation )
  {
    this.dtOperation = dtOperation;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo( SystemLogDO o )
  {
    return this.idSystemLog.compareTo( o.idSystemLog );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof SystemLogDO) )
    {
      return false;
    }
    SystemLogDO other = (SystemLogDO) obj;
    if( (this.idSystemLog == null && other.idSystemLog != null)
        || (this.idSystemLog != null && !this.idSystemLog.equals( other.idSystemLog )) )
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
    hash += (idSystemLog != null ? idSystemLog.hashCode() : 0);
    return hash;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "SystemLogDO [idSystemLog=" + idSystemLog + ", idUser=" + idUser + ", idOperation=" + idOperation
        + ", idProcess=" + idProcess + ", dtOperation=" + dtOperation + "]";
  }

}
