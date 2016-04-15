package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.query.DistributorQuery;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TrailerTOMock;
import mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data trailers screen controller
 * 
 * @author jreyesv
 */
@ManagedBean(name = "trailersBean")
@ViewScoped
public class TrailersBean extends BaseManagedBean implements Serializable
{

  /**
   * Serial version
   */
  private static final long serialVersionUID = -7663737573561735252L;

  /**
   * Constants
   */
  private static final String STRING_TITLE_ADD_TRAILER = "digitalbooking.trailers.administration.addTrailerWindowTitle";
  private static final String STRING_TITLE_EDIT_TRAILER = "digitalbooking.trailers.administration.editTrailerWindowTitle";
  private static final Logger LOG = LoggerFactory.getLogger( TrailersBean.class );

  /**
   * Services
   */
  @EJB
  private ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB;

  /***
   * Variables
   */
  private TrailerLazyDatamodel trailerTOList;
  private TrailerTOMock trailerTOSelected;

  /**
   * For filters
   */
  private String trailerNameFilter;
  private Long idRatingFilter;
  private Long idDistributorFilter;
  private boolean trailerCurrentFilter = false;

  /**
   * For fill combos
   */
  private List<DistributorTO> distributorTOList;
  private List<CatalogTO> formatList;
  private List<CatalogTO> versionList;
  private List<CatalogTO> ratingList;
  private List<CatalogTO> trailerStatusList;

  /**
   * For add or edit trailers
   */
  private String trailerName;
  private Long trailerFormat;
  private Long trailerVersion;
  private Long trailerRating;
  private String trailerGenre;
  private String trailerDuration;
  private Long trailerDistributor;
  private Date trailerReleaseDate;
  private Long trailerStatus;
  private boolean trailerCurrent;

  /**
   * Constructor
   */
  @PostConstruct
  public void init()
  {
    this.formatList = new ArrayList<CatalogTO>();
    this.formatList.add( new CatalogTO( 1L, "2D" ) );
    this.versionList = new ArrayList<CatalogTO>();
    this.versionList.add( new CatalogTO( 1L, "Dob" ) );
    this.ratingList = new ArrayList<CatalogTO>();
    this.ratingList.add( new CatalogTO( 1L, "B" ) );
    this.trailerStatusList = new ArrayList<CatalogTO>();
    this.trailerStatusList.add( new CatalogTO( 1L, "Proximos estrenos" ) );
    this.distributorTOList = this.getDistributors();
    this.trailerTOList = new TrailerLazyDatamodel( null, getUserId() );
    LOG.info( "********************* Initializing for trailers screen *********************" );
  }

  /**
   * Method that sets the search criteria.
   */
  public void setFilters()
  {
    if( this.trailerTOList != null )
    {
      this.trailerTOList.setTrailerName( this.trailerNameFilter );
      this.trailerTOList.setRatingId( this.idRatingFilter );
      this.trailerTOList.setDistributorId( this.idDistributorFilter );
      this.trailerTOList.setCurrentTrailer( this.trailerCurrentFilter );
    }
  }

  /**
   * Method that resets the search criteria.
   */
  public void resetFilters()
  {
    this.trailerNameFilter = null;
    this.idRatingFilter = null;
    this.idDistributorFilter = null;
    this.trailerCurrentFilter = false;
    this.setFilters();
  }

  /**
   * Method that gets a distributor list.
   * 
   * @return the distributorTOList
   */
  private List<DistributorTO> getDistributors()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( super.getUserId() );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( DistributorQuery.DISTRIBUTOR_SHORT_NAME );
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_ACTIVE, true );
    PagingResponseTO<DistributorTO> response = serviceAdminDistributorIntegratorEJB
        .getCatalogDistributorSummary( pagingRequestTO );
    this.distributorTOList = response.getElements();
    return distributorTOList;
  }

  /**
   * Method that gets the corresponding title for add/edit modal.
   * 
   * @return the trailerHeader
   */
  public String getTrailerHeader()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    ResourceBundle msg = fc.getApplication().evaluateExpressionGet( fc, "#{msg}", ResourceBundle.class );

    String trailerHeader = "";
    if( this.trailerTOSelected == null )
    {
      trailerHeader = msg.getString( STRING_TITLE_ADD_TRAILER );
    }
    else
    {
      trailerHeader = msg.getString( STRING_TITLE_EDIT_TRAILER );
    }

    return trailerHeader;
  }

  /**
   * Method that resets the data to capture a new trailer.
   */
  public void initializationForNewTrailer()
  {
    this.trailerName = null;
    this.trailerFormat = null;
    this.trailerVersion = null;
    this.trailerRating = null;
    this.trailerGenre = null;
    this.trailerDuration = null;
    this.trailerDistributor = null;
    this.trailerReleaseDate = null;
    this.trailerStatus = null;
    this.trailerCurrent = true;
  }

  /**
   * Method that sets the data to edit a trailer.
   */
  public void initializationForModifyTrailer()
  {
    if( this.trailerTOSelected != null )
    {
      this.trailerName = this.trailerTOSelected.getName();
      this.trailerFormat = this.trailerTOSelected.getFormat().getId();
      this.trailerVersion = this.trailerTOSelected.getVersion().getId();
      this.trailerRating = this.trailerTOSelected.getRating().getId();
      this.trailerGenre = this.trailerTOSelected.getGenre();
      this.trailerDuration = this.trailerTOSelected.getDuration();
      this.trailerDistributor = this.trailerTOSelected.getDistributor().getId();
      this.trailerReleaseDate = this.trailerTOSelected.getReleaseDate();
      this.trailerStatus = this.trailerTOSelected.getStatusTrailer().getId();
      this.trailerCurrent = this.trailerTOSelected.isCurrent();
    }
    else
    {
      this.initializationForNewTrailer();
    }
  }

  public void validateTrailerTOSelected()
  {

  }

  /**
   * @return the trailerTOList
   */
  public TrailerLazyDatamodel getTrailerTOList()
  {
    return trailerTOList;
  }

  /**
   * @return the trailerTOSelected
   */
  public TrailerTOMock getTrailerTOSelected()
  {
    return trailerTOSelected;
  }

  /**
   * @param trailerTOSelected the trailerTOSelected to set
   */
  public void setTrailerTOSelected( TrailerTOMock trailerTOSelected )
  {
    this.trailerTOSelected = trailerTOSelected;
  }

  /**
   * @return the distributorTOList
   */
  public List<DistributorTO> getDistributorTOList()
  {
    return distributorTOList;
  }

  /**
   * @param distributorTOList the distributorTOList to set
   */
  public void setDistributorTOList( List<DistributorTO> distributorTOList )
  {
    this.distributorTOList = distributorTOList;
  }

  /**
   * @return the formatList
   */
  public List<CatalogTO> getFormatList()
  {
    return formatList;
  }

  /**
   * @param formatList the formatList to set
   */
  public void setFormatList( List<CatalogTO> formatList )
  {
    this.formatList = formatList;
  }

  /**
   * @return the versionList
   */
  public List<CatalogTO> getVersionList()
  {
    return versionList;
  }

  /**
   * @param versionList the versionList to set
   */
  public void setVersionList( List<CatalogTO> versionList )
  {
    this.versionList = versionList;
  }

  /**
   * @return the ratingList
   */
  public List<CatalogTO> getRatingList()
  {
    return ratingList;
  }

  /**
   * @param ratingList the ratingList to set
   */
  public void setRatingList( List<CatalogTO> ratingList )
  {
    this.ratingList = ratingList;
  }

  /**
   * @return the trailerStatusList
   */
  public List<CatalogTO> getTrailerStatusList()
  {
    return trailerStatusList;
  }

  /**
   * @param trailerStatusList the trailerStatusList to set
   */
  public void setTrailerStatusList( List<CatalogTO> trailerStatusList )
  {
    this.trailerStatusList = trailerStatusList;
  }

  /**
   * @return the trailerName
   */
  public String getTrailerName()
  {
    return trailerName;
  }

  /**
   * @param trailerName the trailerName to set
   */
  public void setTrailerName( String trailerName )
  {
    this.trailerName = trailerName;
  }

  /**
   * @return the trailerFormat
   */
  public Long getTrailerFormat()
  {
    return trailerFormat;
  }

  /**
   * @param trailerFormat the trailerFormat to set
   */
  public void setTrailerFormat( Long trailerFormat )
  {
    this.trailerFormat = trailerFormat;
  }

  /**
   * @return the trailerVersion
   */
  public Long getTrailerVersion()
  {
    return trailerVersion;
  }

  /**
   * @param trailerVersion the trailerVersion to set
   */
  public void setTrailerVersion( Long trailerVersion )
  {
    this.trailerVersion = trailerVersion;
  }

  /**
   * @return the trailerRating
   */
  public Long getTrailerRating()
  {
    return trailerRating;
  }

  /**
   * @param trailerRating the trailerRating to set
   */
  public void setTrailerRating( Long trailerRating )
  {
    this.trailerRating = trailerRating;
  }

  /**
   * @return the trailerGenre
   */
  public String getTrailerGenre()
  {
    return trailerGenre;
  }

  /**
   * @param trailerGenre the trailerGenre to set
   */
  public void setTrailerGenre( String trailerGenre )
  {
    this.trailerGenre = trailerGenre;
  }

  /**
   * @return the trailerDuration
   */
  public String getTrailerDuration()
  {
    return trailerDuration;
  }

  /**
   * @param trailerDuration the trailerDuration to set
   */
  public void setTrailerDuration( String trailerDuration )
  {
    this.trailerDuration = trailerDuration;
  }

  /**
   * @return the trailerDistributor
   */
  public Long getTrailerDistributor()
  {
    return trailerDistributor;
  }

  /**
   * @param trailerDistributor the trailerDistributor to set
   */
  public void setTrailerDistributor( Long trailerDistributor )
  {
    this.trailerDistributor = trailerDistributor;
  }

  /**
   * @return the trailerReleaseDate
   */
  public Date getTrailerReleaseDate()
  {
    return trailerReleaseDate;
  }

  /**
   * @param trailerReleaseDate the trailerReleaseDate to set
   */
  public void setTrailerReleaseDate( Date trailerReleaseDate )
  {
    this.trailerReleaseDate = trailerReleaseDate;
  }

  /**
   * @return the trailerStatus
   */
  public Long getTrailerStatus()
  {
    return trailerStatus;
  }

  /**
   * @param trailerStatus the trailerStatus to set
   */
  public void setTrailerStatus( Long trailerStatus )
  {
    this.trailerStatus = trailerStatus;
  }

  /**
   * @return the trailerCurrent
   */
  public boolean isTrailerCurrent()
  {
    return trailerCurrent;
  }

  /**
   * @param trailerCurrent the trailerCurrent to set
   */
  public void setTrailerCurrent( boolean trailerCurrent )
  {
    this.trailerCurrent = trailerCurrent;
  }

  /**
   * @return the trailerNameFilter
   */
  public String getTrailerNameFilter()
  {
    return trailerNameFilter;
  }

  /**
   * @param trailerNameFilter the trailerNameFilter to set
   */
  public void setTrailerNameFilter( String trailerNameFilter )
  {
    this.trailerNameFilter = trailerNameFilter;
  }

  /**
   * @return the idRatingFilter
   */
  public Long getIdRatingFilter()
  {
    return idRatingFilter;
  }

  /**
   * @param idRatingFilter the idRatingFilter to set
   */
  public void setIdRatingFilter( Long idRatingFilter )
  {
    this.idRatingFilter = idRatingFilter;
  }

  /**
   * @return the idDistributorFilter
   */
  public Long getIdDistributorFilter()
  {
    return idDistributorFilter;
  }

  /**
   * @param idDistributorFilter the idDistributorFilter to set
   */
  public void setIdDistributorFilter( Long idDistributorFilter )
  {
    this.idDistributorFilter = idDistributorFilter;
  }

  /**
   * @return the trailerCurrentFilter
   */
  public boolean isTrailerCurrentFilter()
  {
    return trailerCurrentFilter;
  }

  /**
   * @param trailerCurrentFilter the trailerCurrentFilter to set
   */
  public void setTrailerCurrentFilter( boolean trailerCurrentFilter )
  {
    this.trailerCurrentFilter = trailerCurrentFilter;
  }

  /**
   * Lazy data model class for Trailers list.
   * 
   * @author jreyesv
   */
  static class TrailerLazyDatamodel extends LazyDataModel<TrailerTOMock>
  {
    private static final long serialVersionUID = 3769164417858303397L;
    private String trailerName;
    private Long ratingId;
    private Long distributorId;
    private Boolean currentTrailer;
    private Long userId;

    // private ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB;

    public TrailerLazyDatamodel( ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB, Long userId )
    {
      // this.serviceAdminMovieIntegratorEJB = serviceAdminMovieIntegratorEJB;
      this.userId = userId;
    }

    @Override
    public List<TrailerTOMock> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( EventQuery.EVENT_ACTIVE, true );

      this.setFiltersToRequest( pagingRequestTO );
      // PagingResponseTO<EventMovieTO> response = serviceAdminMovieIntegratorEJB.getCatalogMovieSummary(
      // pagingRequestTO );
      // this.setRowCount( response.getTotalCount() );
      // return response.getElements();
      DistributorTO distributor = new DistributorTO();
      distributor.setId( 3L );
      distributor.setName( "Generico" );
      distributor.setFgActive( true );
      List<TrailerTOMock> responseMock = new ArrayList<TrailerTOMock>();
      responseMock.add( new TrailerTOMock( 1L, "La guerra y la paz", new CatalogTO( 1L, "2D" ), new CatalogTO( 1L,
          "Dob" ), new CatalogTO( 1L, "B" ), "Action", "1:15", distributor, new Date(), new CatalogTO( 1L,
          "Proximos estrenos" ), true ) );
      responseMock.add( new TrailerTOMock( 2L, "Focus", new CatalogTO( 1L, "2D" ), new CatalogTO( 1L,
          "Dob" ), new CatalogTO( 2L, "A" ), "Infantil", "1:15", distributor, new Date(), new CatalogTO( 1L,
          "Proximos estrenos" ), true ) );
      responseMock.add( new TrailerTOMock( 3L, "Cenicienta", new CatalogTO( 1L, "2D" ), new CatalogTO( 1L,
          "Dob" ), new CatalogTO( 1L, "B" ), "Action", "1:15", distributor, new Date(), new CatalogTO( 1L,
          "Proximos estrenos" ), true ) );
      this.setRowCount( responseMock.size() );
      return responseMock;
    }

    private void setFiltersToRequest( PagingRequestTO pagingRequestTO )
    {
      if( StringUtils.isNotBlank( this.trailerName ) )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_NAME, this.trailerName );
      }
      if( this.ratingId != null && this.ratingId.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, this.ratingId );
      }
      if( this.distributorId != null && this.distributorId.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, this.distributorId );
      }
      if( this.currentTrailer != null && this.currentTrailer )
      {
        pagingRequestTO.getFilters().put( EventQuery.EVENT_CURRENT_MOVIE, this.currentTrailer );
      }
    }

    /**
     * @return the trailerName
     */
    public String getTrailerName()
    {
      return trailerName;
    }

    /**
     * @param trailerName the trailerName to set
     */
    public void setTrailerName( String trailerName )
    {
      this.trailerName = trailerName;
    }

    /**
     * @return the ratingId
     */
    public Long getRatingId()
    {
      return ratingId;
    }

    /**
     * @param ratingId the ratingId to set
     */
    public void setRatingId( Long ratingId )
    {
      this.ratingId = ratingId;
    }

    /**
     * @return the distributorId
     */
    public Long getDistributorId()
    {
      return distributorId;
    }

    /**
     * @param distributorId the distributorId to set
     */
    public void setDistributorId( Long distributorId )
    {
      this.distributorId = distributorId;
    }

    /**
     * @return the currentTrailer
     */
    public Boolean getCurrentTrailer()
    {
      return currentTrailer;
    }

    /**
     * @param currentTrailer the currentTrailer to set
     */
    public void setCurrentTrailer( Boolean currentTrailer )
    {
      this.currentTrailer = currentTrailer;
    }

  }

}