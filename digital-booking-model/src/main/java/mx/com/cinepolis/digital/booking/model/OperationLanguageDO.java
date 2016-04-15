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
 * JPA entity for C_OPERATION_LANGUAGE
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_OPERATION_LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "OperationLanguageDO.findAll", query = "SELECT ol FROM OperationLanguageDO ol"),
    @NamedQuery(name = "OperationLanguageDO.findById", query = "SELECT ol FROM OperationLanguageDO ol where ol.idOperationLanguage = :idOperationLanguage") })
public class OperationLanguageDO extends AbstractEntity<OperationLanguageDO>
{

  /**
   * 
   */
  private static final long serialVersionUID = 3699647738773704051L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_OPERATION_LANGUAGE", nullable = false)
  private Integer idOperationLanguage;

  @JoinColumn(name = "ID_OPERATION", referencedColumnName = "ID_OPERATION", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private OperationDO idOperation;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @Column(name = "DS_NAME", nullable = false)
  private String dsName;

  /**
   * Constructor by default
   */
  public OperationLanguageDO()
  {
  }

  /**
   * Constructor by id
   */
  public OperationLanguageDO( Integer idOperationLanguage )
  {
    this.idOperationLanguage = idOperationLanguage;
  }

  /**
   * @return the idOperationLanguage
   */
  public Integer getIdOperationLanguage()
  {
    return idOperationLanguage;
  }

  /**
   * @param idOperationLanguage the idOperationLanguage to set
   */
  public void setIdOperationLanguage( Integer idOperationLanguage )
  {
    this.idOperationLanguage = idOperationLanguage;
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
  public int compareTo( OperationLanguageDO o )
  {
    return this.idOperationLanguage.compareTo( o.idOperationLanguage );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.AbstractEntity#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof OperationLanguageDO) )
    {
      return false;
    }
    OperationLanguageDO other = (OperationLanguageDO) obj;
    if( (this.idOperationLanguage == null && other.idOperationLanguage != null)
        || (this.idOperationLanguage != null && !this.idOperationLanguage.equals( other.idOperationLanguage )) )
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
    hash += (idOperationLanguage != null ? idOperationLanguage.hashCode() : 0);
    return hash;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "OperationLanguageDO [idOperationLanguage=", this.idOperationLanguage )
        .append( ", dsName=", this.dsName ).append( "]" ).toString();
  }

}
