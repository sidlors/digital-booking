package mx.com.cinepolis.digital.booking.web.beans.home;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.PersonUtils;
import mx.com.cinepolis.digital.booking.integration.home.ServiceHomeIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.event.CloseEvent;

/**
 * Backing bean for the home interface.
 * 
 * @author kperez
 */
@ManagedBean(name = "homeBean")
@ViewScoped
public class HomeBean extends BaseManagedBean
{

  private static final long serialVersionUID = -4120415690599512783L;

  @EJB
  private ServiceHomeIntegratorEJB serviceHomeIntegratorEJB;

  private List<EventMovieTO> eventMovieTOs;
  private List<IncomeTO> topWeekTOs;
  private EventMovieTO topWeekTO;
  private List<TheaterTO> theaterTOs;
  private List<NewsFeedObservationTO> newsFeedTOs;
  private String newComment;
  private NewsFeedObservationTO newsFeedTO;
  private String fullNameUser;
  private Date newStartDate;
  private Date newEndDate;
  private Date current;
  private boolean messageOwner;
  private WeekTO currentWeek;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void loadData()
  {
    eventMovieTOs = serviceHomeIntegratorEJB.findAllPremieres();
    AbstractTO abstractTO = createAbstractTO();
    theaterTOs = serviceHomeIntegratorEJB.getMyTheaters( abstractTO );
    topWeekTOs = serviceHomeIntegratorEJB.getTopWeek( abstractTO );
    this.currentWeek = this.serviceHomeIntegratorEJB.getTopWeekTO( abstractTO );
    this.getNeewsFeedList();
    UserTO user = (UserTO) getAttribute( USER_ATTRIBUTE );
    fullNameUser = PersonUtils.buildFullName( user.getPersonTO() );
  }

  private AbstractTO createAbstractTO()
  {
    AbstractTO abstractTO = new AbstractTO();
    fillSessionData( abstractTO );
    return abstractTO;
  }

  private void getNeewsFeedList()
  {
    newsFeedTOs = serviceHomeIntegratorEJB.getNewsFeeds( createAbstractTO() );
  }

  public void newsFeedHandleClose( CloseEvent event )
  {
    newComment = null;
    newStartDate = null;
    newEndDate = null;
  }

  public void saveNewsFeed()
  {
    newsFeedTO = null;
    serviceHomeIntegratorEJB.createNewsFeed( createNewsFeedObservationTO() );
    getNeewsFeedList();
  }

  public void updateNewsFeed()
  {
    NewsFeedObservationTO newsFeedObservationTO = getSelectionNewsFeedFromView();
    if( !serviceHomeIntegratorEJB.validateNewsFeed( newsFeedObservationTO ) )
    {
      createMessageError( "business.error.code.103" );
      getNeewsFeedList();
    }
    else
    {
      serviceHomeIntegratorEJB.editNewsFeed( newsFeedObservationTO );
      getNeewsFeedList();
    }
  }

  private NewsFeedObservationTO getSelectionNewsFeedFromView()
  {
    NewsFeedObservationTO newsFeed = new NewsFeedObservationTO();
    newsFeed.setId( newsFeedTO.getId() );
    newsFeed.setIdNewsFeed( newsFeedTO.getIdNewsFeed() );
    newsFeed.setEnd( newsFeedTO.getEnd() );
    newsFeed.setStart( newsFeedTO.getStart() );
    newsFeed.setObservation( newsFeedTO.getObservation() );
    super.fillSessionData( newsFeed );
    return newsFeed;
  }

  public void deleteNewsFeed()
  {
    NewsFeedObservationTO newsFeedObservationTO = getSelectionNewsFeedFromView();
    if( !serviceHomeIntegratorEJB.validateNewsFeed( newsFeedObservationTO ) )
    {
      createMessageError( "business.error.code.103" );
    }
    else
    {
      serviceHomeIntegratorEJB.deleteNewsFeed( newsFeedObservationTO );
      getNeewsFeedList();
    }
  }

  private NewsFeedObservationTO createNewsFeedObservationTO()
  {
    NewsFeedObservationTO newsFeedObservationTO = null;

    if( newsFeedTO != null )
    {
      newsFeedObservationTO = newsFeedTO;
    }
    else
    {
      newsFeedObservationTO = new NewsFeedObservationTO();
      newsFeedObservationTO.setEnd( newEndDate );
      newsFeedObservationTO.setStart( newStartDate );
      newsFeedObservationTO.setObservation( newComment );
    }
    fillSessionData( newsFeedObservationTO );

    return newsFeedObservationTO;
  }

  public void checkOwner()
  {
    messageOwner = false;
    if( this.newsFeedTO != null )
    {
      NewsFeedObservationTO newsFeedObservationTO = getSelectionNewsFeedFromView();
      messageOwner = serviceHomeIntegratorEJB.validateNewsFeed( newsFeedObservationTO );
    }
  }

  public void validateSelection()
  {
    if( newsFeedTO == null )
    {
      validationFail();
      createMessageValidationSelection();
    }
  }

  /**
   * @return the eventMovieTOs
   */
  public List<EventMovieTO> getEventMovieTOs()
  {
    return eventMovieTOs;
  }

  /**
   * @param eventMovieTOs the eventMovieTOs to set
   */
  public void setEventMovieTOs( List<EventMovieTO> eventMovieTOs )
  {
    this.eventMovieTOs = eventMovieTOs;
  }

  /**
   * @return the topWeekTOs
   */
  public List<IncomeTO> getTopWeekTOs()
  {
    return topWeekTOs;
  }

  /**
   * @param topWeekTOs the topWeekTOs to set
   */
  public void setTopWeekTOs( List<IncomeTO> topWeekTOs )
  {
    this.topWeekTOs = topWeekTOs;
  }

  /**
   * @return the topWeekTO
   */
  public EventMovieTO getTopWeekTO()
  {
    return topWeekTO;
  }

  /**
   * @param topWeekTO the topWeekTO to set
   */
  public void setTopWeekTO( EventMovieTO topWeekTO )
  {
    this.topWeekTO = topWeekTO;
  }

  /**
   * @return the theaterTOs
   */
  public List<TheaterTO> getTheaterTOs()
  {
    return theaterTOs;
  }

  /**
   * @param theaterTOs the theaterTOs to set
   */
  public void setTheaterTOs( List<TheaterTO> theaterTOs )
  {
    this.theaterTOs = theaterTOs;
  }

  /**
   * @return the newsFeedTOs
   */
  public List<NewsFeedObservationTO> getNewsFeedTOs()
  {
    return newsFeedTOs;
  }

  /**
   * @param newsFeedTOs the newsFeedTOs to set
   */
  public void setNewsFeedTOs( List<NewsFeedObservationTO> newsFeedTOs )
  {
    this.newsFeedTOs = newsFeedTOs;
  }

  /**
   * @return the newComment
   */
  public String getNewComment()
  {
    return newComment;
  }

  /**
   * @param newComment the newComment to set
   */
  public void setNewComment( String newComment )
  {
    this.newComment = newComment;
  }

  /**
   * @return the newsFeedTO
   */
  public NewsFeedObservationTO getNewsFeedTO()
  {
    return newsFeedTO;
  }

  /**
   * @param newsFeedTO the newsFeedTO to set
   */
  public void setNewsFeedTO( NewsFeedObservationTO newsFeedTO )
  {
    this.newsFeedTO = newsFeedTO;
  }

  /**
   * @return the fullNameUser
   */
  public String getFullNameUser()
  {
    return fullNameUser;
  }

  /**
   * @param fullNameUser the fullNameUser to set
   */
  public void setFullNameUser( String fullNameUser )
  {
    this.fullNameUser = fullNameUser;
  }

  /**
   * @return the newStartDate
   */
  public Date getNewStartDate()
  {
    return CinepolisUtils.enhancedClone( newStartDate );
  }

  /**
   * @param newStartDate the newStartDate to set
   */
  public void setNewStartDate( Date newStartDate )
  {
    this.newStartDate = CinepolisUtils.enhancedClone( newStartDate );
  }

  /**
   * @return the newEndDate
   */
  public Date getNewEndDate()
  {
    return CinepolisUtils.enhancedClone( newEndDate );
  }

  /**
   * @param newEndDate the newEndDate to set
   */
  public void setNewEndDate( Date newEndDate )
  {
    this.newEndDate = CinepolisUtils.enhancedClone( newEndDate );
  }

  /**
   * @return the current
   */
  public Date getCurrent()
  {
    return CinepolisUtils.enhancedClone( current );
  }

  /**
   * @param current the current to set
   */
  public void setCurrent( Date current )
  {
    this.current = CinepolisUtils.enhancedClone( current );
  }

  /**
   * @return the messageOwner
   */
  public boolean isMessageOwner()
  {
    return messageOwner;
  }

  /**
   * @param messageOwner the messageOwner to set
   */
  public void setMessageOwner( boolean messageOwner )
  {
    this.messageOwner = messageOwner;
  }

  /**
   * @return the currentWeek
   */
  public WeekTO getCurrentWeek()
  {
    return currentWeek;
  }

  /**
   * @return the currentWeek in {@link java.lang.String} format.
   */
  public String getCurrentWeekString()
  {
    return new StringBuilder().append( "(" ).append( this.currentWeek.getWeekDescription() ).append( ")" ).toString();
  }

}
