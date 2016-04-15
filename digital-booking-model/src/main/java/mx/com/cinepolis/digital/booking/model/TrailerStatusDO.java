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
 * JPA entity for C_TRAILER_STATUS
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_TRAILER_STATUS")
@NamedQueries({
    @NamedQuery(name = "TrailerStatusDO.findAll", query = "SELECT ts FROM TrailerStatusDO ts"),
    @NamedQuery(name = "TrailerStatusDO.findById", query = "SELECT ts FROM TrailerStatusDO ts where ts.idTrailerStatus = :idTrailerStatus") })
public class TrailerStatusDO extends AbstractEntity<TrailerStatusDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = 5463615986002799665L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_TRAILER_STATUS", nullable = false)
  private Integer idTrailerStatus;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idTrailerStatus", fetch = FetchType.LAZY)
  private List<TrailerStatusLanguageDO> trailerStatusLanguageDOList;
  
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idTrailerStatus", fetch =FetchType.LAZY)
  private List<TrailerDO> trailerDOList;
/**
 * Constructor by default
 */
  public TrailerStatusDO(  )
  {
  }
  /**
   * Constructor by identifier
   * @param idTrailerStatus
   */
  public TrailerStatusDO( Integer idTrailerStatus )
  {
    this.idTrailerStatus = idTrailerStatus;
  }

  /**
   * @return the idTrailerStatus
   */
  public Integer getIdTrailerStatus()
  {
    return idTrailerStatus;
  }

  /**
   * @param idTrailerStatus the idTrailerStatus to set
   */
  public void setIdTrailerStatus( Integer idTrailerStatus )
  {
    this.idTrailerStatus = idTrailerStatus;
  }

  /**
   * @return the trailerStatusLanguageDOList
   */
  public List<TrailerStatusLanguageDO> getTrailerStatusLanguageDOList()
  {
    return trailerStatusLanguageDOList;
  }

  /**
   * @param trailerStatusLanguageDOList the trailerStatusLanguageDOList to set
   */
  public void setTrailerStatusLanguageDOList( List<TrailerStatusLanguageDO> trailerStatusLanguageDOList )
  {
    this.trailerStatusLanguageDOList = trailerStatusLanguageDOList;
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
  public int compareTo( TrailerStatusDO o )
  {
    return this.idTrailerStatus.compareTo( o.idTrailerStatus );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof TrailerStatusDO) )
    {
      return false;
    }
    TrailerStatusDO other = (TrailerStatusDO) obj;
    if( (this.idTrailerStatus == null && other.idTrailerStatus != null)
        || (this.idTrailerStatus != null && !this.idTrailerStatus.equals( other.idTrailerStatus )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTrailerStatus != null ? idTrailerStatus.hashCode() : 0);
    return hash;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "TrailerStatusDO [idTrailerStatus=" + idTrailerStatus + ", trailerStatusLanguageDOList="
        + trailerStatusLanguageDOList + ", trailerDOList=" + trailerDOList + "]";
  }
}
