package mx.com.cinepolis.digital.booking.model;

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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for C_PROCESS_LANGUAGE
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_PROCESS_LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "ProcessLanguageDO.findAll", query = "SELECT pl FROM ProcessLanguageDO pl"),
    @NamedQuery(name = "ProcessLanguageDO.findById", query = "SELECT pl FROM ProcessLanguageDO pl where pl.idProcessLanguage = :idProcessLanguage") })
public class ProcessLanguageDO extends AbstractEntity<ProcessLanguageDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = 8603679462699148200L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_PROCESS_LANGUAGE", nullable = false)
  private Integer idProcessLanguage;

  @JoinColumn(name = "ID_PROCESS", referencedColumnName = "ID_PROCESS", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ProcessDO idProcess;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @Column(name = "DS_NAME", nullable = false)
  private String dsName;

  /**
   * Constructor by default
   */
  public ProcessLanguageDO()
  {
  }

  /**
   * Constructor by identifier
   * 
   * @param idProcessLanguage
   */
  public ProcessLanguageDO( Integer idProcessLanguage )
  {
    this.idProcessLanguage = idProcessLanguage;
  }

  /**
   * @return the idProcessLanguage
   */
  public Integer getIdProcessLanguage()
  {
    return idProcessLanguage;
  }

  /**
   * @param idProcessLanguage the idProcessLanguage to set
   */
  public void setIdProcessLanguage( Integer idProcessLanguage )
  {
    this.idProcessLanguage = idProcessLanguage;
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
   * @return the idLanguage
   */
  public LanguageDO getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( LanguageDO idLanguage )
  {
    this.idLanguage = idLanguage;
  }

  /**
   * @return the dsName
   */
  public String getDsName()
  {
    return dsName;
  }

  /**
   * @param dsName the dsName to set
   */
  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo( ProcessLanguageDO o )
  {
    return this.idProcessLanguage.compareTo( o.idProcessLanguage );
  }

  /*
   * (non-Javadoc)s
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof ProcessLanguageDO) )
    {
      return false;
    }
    ProcessLanguageDO other = (ProcessLanguageDO) obj;
    if( (this.idProcessLanguage == null && other.idProcessLanguage != null)
        || (this.idProcessLanguage != null && !this.idProcessLanguage.equals( other.idProcessLanguage )) )
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
    hash += (idProcessLanguage != null ? idProcessLanguage.hashCode() : 0);
    return hash;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "ProcessLanguageDO [idProcessLanguage=", this.idProcessLanguage )
        .append( ", dsName=", this.dsName ).append( "]" ).toString();
  }

}
