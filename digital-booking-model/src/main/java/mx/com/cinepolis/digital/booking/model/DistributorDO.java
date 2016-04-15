/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
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

/**
 * JPA entity for C_DISTRIBUTOR
 * 
 * @author kperez
 */
@Entity
@Table(name = "C_DISTRIBUTOR")
@NamedQueries({
    @NamedQuery(name = "DistributorDO.findAll", query = "SELECT d FROM DistributorDO d"),
    @NamedQuery(name = "DistributorDO.findByIdDistributor", query = "SELECT d FROM DistributorDO d WHERE d.idDistributor = :idDistributor"),
    @NamedQuery(name = "DistributorDO.findByIdVista", query = "SELECT d FROM DistributorDO d WHERE d.idVista = :idVista"),
    @NamedQuery(name = "DistributorDO.findByIdVistaAndActive", query = "SELECT d FROM DistributorDO d WHERE d.idVista = :idVista and d.fgActive = true"),
    @NamedQuery(name = "DistributorDO.findByDsName", query = "SELECT d FROM DistributorDO d WHERE d.dsName = :dsName"),
    @NamedQuery(name = "DistributorDO.findByDsShortName", query = "SELECT d FROM DistributorDO d WHERE d.dsShortName = :dsShortName and d.fgActive = true"),
    @NamedQuery(name = "DistributorDO.findByDsNameActive", query = "SELECT d FROM DistributorDO d WHERE d.dsName = :dsName and d.fgActive = true"),
    @NamedQuery(name = "DistributorDO.findByDsName", query = "SELECT d FROM DistributorDO d WHERE d.dsName = :dsName"),
    @NamedQuery(name = "DistributorDO.findByFgActive", query = "SELECT d FROM DistributorDO d WHERE d.fgActive = :fgActive"),
    @NamedQuery(name = "DistributorDO.findByDtLastModification", query = "SELECT d FROM DistributorDO d WHERE d.dtLastModification = :dtLastModification") })
public class DistributorDO extends AbstractSignedEntity<DistributorDO>
{

  private static final long serialVersionUID = -1515227974060957321L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_DISTRIBUTOR")
  private Integer idDistributor;

  @Column(name = "ID_VISTA")
  private String idVista;

  @Column(name = "DS_NAME", nullable = false, length = 50)
  private String dsName;

  @Column(name = "DS_SHORT_NAME")
  private String dsShortName;

  @OneToMany(mappedBy = "idDistributor", fetch = FetchType.LAZY)
  private List<EventMovieDO> eventMovieDOList;

  @OneToMany(mappedBy = "idDistributor", fetch = FetchType.LAZY)
  private List<SpecialEventDO> specialEventDOList;

  @OneToMany(cascade= {CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "idDistributor", fetch= FetchType.LAZY )
  private List<TrailerDO> trailerDOList;
  /**
   * Constructor default
   */
  public DistributorDO()
  {
  }

  /**
   * Constructor by idDistributor
   * 
   * @param idDistributor
   */
  public DistributorDO( Integer idDistributor )
  {
    this.idDistributor = idDistributor;
  }

  /**
   * @return the idDistributor
   */
  public Integer getIdDistributor()
  {
    return idDistributor;
  }

  /**
   * @param idDistributor the idDistributor to set
   */
  public void setIdDistributor( Integer idDistributor )
  {
    this.idDistributor = idDistributor;
  }

  /**
   * @return the idVista
   */
  public String getIdVista()
  {
    return idVista;
  }

  /**
   * @param idVista the idVista to set
   */
  public void setIdVista( String idVista )
  {
    this.idVista = idVista;
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
   * @return the dsShortName
   */
  public String getDsShortName()
  {
    return dsShortName;
  }

  /**
   * @param dsShortName the dsShortName to set
   */
  public void setDsShortName( String dsShortName )
  {
    this.dsShortName = dsShortName;
  }

  /**
   * @return the eventMovieDOList
   */
  public List<EventMovieDO> getEventMovieDOList()
  {
    return eventMovieDOList;
  }

  /**
   * @param eventMovieDOList the eventMovieDOList to set
   */
  public void setEventMovieDOList( List<EventMovieDO> eventMovieDOList )
  {
    this.eventMovieDOList = eventMovieDOList;
  }

  /**
   * @return the specialEventDOList
   */
  public List<SpecialEventDO> getSpecialEventDOList()
  {
    return specialEventDOList;
  }

  /**
   * @param specialEventDOList the specialEventDOList to set
   */
  public void setSpecialEventDOList( List<SpecialEventDO> specialEventDOList )
  {
    this.specialEventDOList = specialEventDOList;
  }

  /**
   * @return the trailerDOList
   */
  public List<TrailerDO> getTrailerDOList()
  {
    return trailerDOList;
  }

  /**
   * @param trailerDOList the trailerDOList to set
   */
  public void setTrailerDOList( List<TrailerDO> trailerDOList )
  {
    this.trailerDOList = trailerDOList;
  }
  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idDistributor != null ? idDistributor.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof DistributorDO) )
    {
      return false;
    }
    DistributorDO other = (DistributorDO) object;
    if( (this.idDistributor == null && other.idDistributor != null)
        || (this.idDistributor != null && !this.idDistributor.equals( other.idDistributor )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.DistributorDO[ idDistributor=" + idDistributor + " ]";
  }

  @Override
  public int compareTo( DistributorDO o )
  {
    return this.idDistributor.compareTo( o.idDistributor );
  }

}
