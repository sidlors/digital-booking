/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
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
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_STATE_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_STATE_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_STATE", "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "StateLanguageDO.findAll", query = "SELECT s FROM StateLanguageDO s"),
    @NamedQuery(name = "StateLanguageDO.findByIdStateLaguange", query = "SELECT s FROM StateLanguageDO s WHERE s.idStateLaguange = :idStateLaguange"),
    @NamedQuery(name = "StateLanguageDO.findByDsName", query = "SELECT s FROM StateLanguageDO s WHERE s.dsName = :dsName") })
public class StateLanguageDO extends AbstractEntity<StateLanguageDO>
{
  private static final long serialVersionUID = 3807922854774388335L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_STATE_LAGUANGE", nullable = false)
  private Integer idStateLaguange;

  @Column(name = "DS_NAME", length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_STATE", referencedColumnName = "ID_STATE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private StateDO idState;

  /**
   * Constructor default
   */
  public StateLanguageDO()
  {
  }

  /**
   * Constructor by idStateLaguange
   * 
   * @param idStateLaguange
   */
  public StateLanguageDO( Integer idStateLaguange )
  {
    this.idStateLaguange = idStateLaguange;
  }

  /**
   * @return the idStateLaguange
   */
  public Integer getIdStateLaguange()
  {
    return idStateLaguange;
  }

  /**
   * @param idStateLaguange the idStateLaguange to set
   */
  public void setIdStateLaguange( Integer idStateLaguange )
  {
    this.idStateLaguange = idStateLaguange;
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
   * @return the idState
   */
  public StateDO getIdState()
  {
    return idState;
  }

  /**
   * @param idState the idState to set
   */
  public void setIdState( StateDO idState )
  {
    this.idState = idState;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idStateLaguange != null ? idStateLaguange.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof StateLanguageDO) )
    {
      return false;
    }
    StateLanguageDO other = (StateLanguageDO) object;
    if( (this.idStateLaguange == null && other.idStateLaguange != null)
        || (this.idStateLaguange != null && !this.idStateLaguange.equals( other.idStateLaguange )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.StateLanguageDO[ idStateLaguange=" + idStateLaguange + " ]";
  }

  @Override
  public int compareTo( StateLanguageDO other )
  {
    return this.idStateLaguange.compareTo( other.idStateLaguange );
  }

}
