package mx.com.cinepolis.digital.booking.web.beans.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;
import mx.com.cinepolis.digital.booking.web.controller.BookingTheaterDocumentController;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Backing bean for the distributors report user interface.
 * 
 * @author afuentes
 */
@ManagedBean(name = "distributorsReportBean")
@ViewScoped
public class DistibutorsReportBean extends BaseManagedBean
{
  private static final long serialVersionUID = 3890084581046265856L;
  private static final Logger LOG = LoggerFactory.getLogger( BookingTheaterDocumentController.class );

  private static final String EXCEL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  private static final String XLSX_EXTENSION = ".xlsx";
  private static final String WEEK = " Semana ";
  private static final String DISTRIBUTOR = "Distribuidor ";
  private static final String DISTRIBUTORS_WEEK = "Distribuidores Semana ";
  private static final String FAIL_PARAMETER = "fail";

  private Integer idWeek;
  private List<WeekTO> weeks;
  private Integer idDistributor;
  private List<DistributorTO> distributors;
  private Integer idRegion;
  private List<CatalogTO> regions;
  private StreamedContent file;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  @EJB
  private ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void init()
  {
    AbstractTO abstractTO = new AbstractTO();
    super.fillSessionData( abstractTO );
    weeks = bookingServiceIntegratorEJB.findWeeksActive( abstractTO );
    distributors = serviceAdminDistributorIntegratorEJB.getAll();
    regions = bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );
    
    for( WeekTO weekTO : weeks )
    {
      if( weekTO.isActiveWeek() )
      {
        this.idWeek = weekTO.getIdWeek();
      }
    }
  }

  /**
   * Method that builds the distributor report file.
   * 
   * @return Object {@link StreamedContent} with the distributor report file stream.
   */
  public StreamedContent getFile()
  {
    if( this.validateSelection() )
    {
      Long idWeekLong = idWeek.longValue();
      Long idDistributorLong = idDistributor.longValue();
      Long idRegionLong = idRegion.longValue() > 0 ? idRegion.longValue() : null;
      boolean isSingleDistributor = idDistributor > 0;
      // TODO Agregar el Id de la region
      FileTO fileTO = isSingleDistributor ? bookingServiceIntegratorEJB.getWeeklyDistributorReportByDistributor(
        idWeekLong, idRegionLong, idDistributorLong ) : bookingServiceIntegratorEJB
          .getWeeklyDistributorReportByAllDistributors( idWeekLong, idRegionLong );

      byte[] bytes = fileTO.getFile();

      File excelFile;
      try
      {
        String name = getName( idWeekLong, isSingleDistributor, idDistributor );
        excelFile = File.createTempFile( name, XLSX_EXTENSION );
        FileOutputStream fileOutputStream = new FileOutputStream( excelFile );
        fileOutputStream.write( bytes );
        fileOutputStream.flush();
        fileOutputStream.close();

        file = new DefaultStreamedContent( new FileInputStream( excelFile ), EXCEL_CONTENT_TYPE, name + ".xlsx" );
      }
      catch( Exception e )
      {
        LOG.error( e.getMessage(), e );
      }
    }
    return file;
  }

  private String getName( Long weekId, boolean isSingleDistributor, Integer idDistributor )
  {
    String name = null;
    WeekTO week = bookingServiceIntegratorEJB.findWeek( weekId.intValue() );
    if( isSingleDistributor )
    {
      CatalogTO distributorCatalogTO = serviceAdminDistributorIntegratorEJB.getDistributor( idDistributor );
      name = CinepolisUtils.buildStringUsingMutable( DISTRIBUTOR, distributorCatalogTO.getName(), WEEK,
        week.getNuWeek() );
    }
    else
    {
      name = CinepolisUtils.buildStringUsingMutable( DISTRIBUTORS_WEEK, week.getNuWeek() );
    }
    return name;
  }

  /**
   * Method that validates that the mandatory fields have valid values.
   * 
   * @return {@link Boolean} <code>true</code> if the mandatory fields have valid values, <code>false</code> otherwise.
   */
  public Boolean validateSelection()
  {
    Boolean isValid = true;
    isValid &= idWeek != null && idWeek.intValue() > 0;
    isValid &= idDistributor != null && idDistributor.intValue() >= 0;
    isValid &= idRegion != null && idRegion.intValue() >= 0;
    if( !isValid )
    {
      RequestContext.getCurrentInstance().addCallbackParam( FAIL_PARAMETER, true );
    }
    return isValid;
  }

  /* Getters and setters */

  /**
   * @return the idWeek
   */
  public Integer getIdWeek()
  {
    return idWeek;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the weeks
   */
  public List<WeekTO> getWeeks()
  {
    return weeks;
  }

  /**
   * @param weeks the weeks to set
   */
  public void setWeeks( List<WeekTO> weeks )
  {
    this.weeks = weeks;
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
   * @return the distributors
   */
  public List<DistributorTO> getDistributors()
  {
    return distributors;
  }

  /**
   * @param distributors the distributors to set
   */
  public void setDistributors( List<DistributorTO> distributors )
  {
    this.distributors = distributors;
  }

  /**
   * @return the regions
   */
  public List<CatalogTO> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<CatalogTO> regions )
  {
    this.regions = regions;
  }

  /**
   * @return the idRegion
   */
  public Integer getIdRegion()
  {
    return idRegion;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( Integer idRegion )
  {
    this.idRegion = idRegion;
  }

}
