package mx.com.cinepolis.digital.booking.commons.to.reports;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;

/**
 * Objeto de transferencia de las areas de promexico
 * 
 * @author gsegura
 * @since 0.1.0
 */
public class WorkSheetReportTO extends AbstractTO
{
  private static final long serialVersionUID = 6469490714212502801L;
  private Long id;
  private String name;
  private String fullAddress;
  private Boolean isActive;
 
  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName( String name )
  {
    this.name = name;
  }


  /**
   * @return the isActive
   */
  public Boolean getIsActive()
  {
    return isActive;
  }

  /**
   * @param isActive the isActive to set
   */
  public void setIsActive( Boolean isActive )
  {
    this.isActive = isActive;
  }


  /**
   * @return the fullAddress
   */
  public String getFullAddress()
  {
    return fullAddress;
  }

  /**
   * @param fullAddress the fullAddress to set
   */
  public void setFullAddress( String fullAddress )
  {
    this.fullAddress = fullAddress;
  }

}
