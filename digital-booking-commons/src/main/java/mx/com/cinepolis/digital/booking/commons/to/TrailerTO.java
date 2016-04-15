package mx.com.cinepolis.digital.booking.commons.to;

import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

public class TrailerTO extends AbstractTO implements Comparable<TrailerTO>
{
  /**
   * 
   */
  private static final long serialVersionUID = -4576617511214217761L;
  /**
   * trailer id
   */
  private Long idTrailer;
  /**
   * trailer status
   */
  private TrailerStatusLanguageTO idTrailerStatus;
  /**
   * Trailer Distributor
   */
  private DistributorTO idDistributor;
  /**
   * trailer duration
   */
  private Integer qtDuration;
  /**
   * trailer genre
   */
  private String dsGenre;
  /**
   * trailer release
   */
  private Date dtRelease;
  /**
   * indicate if the trailer is current
   */
  private Boolean fgCurrent;

  /**
   * @return the idTrailer
   */
  public Long getIdTrailer()
  {
    return idTrailer;
  }

  /**
   * @param idTrailer the idTrailer to set
   */
  public void setIdTrailer( Long idTrailer )
  {
    this.idTrailer = idTrailer;
  }

  /**
   * @return the idTrailerStatus
   */
  public TrailerStatusLanguageTO getIdTrailerStatus()
  {
    return idTrailerStatus;
  }

  /**
   * @param idTrailerStatus the idTrailerStatus to set
   */
  public void setIdTrailerStatus( TrailerStatusLanguageTO idTrailerStatus )
  {
    this.idTrailerStatus = idTrailerStatus;
  }

  /**
   * @return the idDistributor
   */
  public DistributorTO getIdDistributor()
  {
    return idDistributor;
  }

  /**
   * @param idDistributor the idDistributor to set
   */
  public void setIdDistributor( DistributorTO idDistributor )
  {
    this.idDistributor = idDistributor;
  }

  /**
   * @return the qtDuration
   */
  public Integer getQtDuration()
  {
    return qtDuration;
  }

  /**
   * @param qtDuration the qtDuration to set
   */
  public void setQtDuration( Integer qtDuration )
  {
    this.qtDuration = qtDuration;
  }

  /**
   * @return the dsGenre
   */
  public String getDsGenre()
  {
    return dsGenre;
  }

  /**
   * @param dsGenre the dsGenre to set
   */
  public void setDsGenre( String dsGenre )
  {
    this.dsGenre = dsGenre;
  }

  /**
   * @return the dtRelease
   */
  public Date getDtRelease()
  {
    return CinepolisUtils.enhancedClone(  dtRelease);
  }

  /**
   * @param dtRelease the dtRelease to set
   */
  public void setDtRelease( Date dtRelease )
  {
    this.dtRelease = dtRelease;
  }

  /**
   * @return the fgCurrent
   */
  public Boolean getFgCurrent()
  {
    return fgCurrent;
  }

  /**
   * @param fgCurrent the fgCurrent to set
   */
  public void setFgCurrent( Boolean fgCurrent )
  {
    this.fgCurrent = fgCurrent;
  }

  @Override
  public int compareTo( TrailerTO o )
  {
    return this.idTrailer.compareTo( o.idTrailer );
  }

}
