package mx.com.cinepolis.digital.booking.commons.to;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author gsegura
 * @since 0.2.0
 */
public abstract class AbstractObservationTO extends AbstractTO implements Comparable<AbstractObservationTO>
{

  private static final long serialVersionUID = 5501726942173917527L;

  private Long id;

  private String observation;

  private PersonTO personTO;

  private UserTO user;

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
   * @return the observation
   */
  public String getObservation()
  {
    return observation;
  }

  /**
   * @param observation the observation to set
   */
  public void setObservation( String observation )
  {
    this.observation = observation;
  }

  /**
   * @return the personTO
   */
  public PersonTO getPersonTO()
  {
    return personTO;
  }

  /**
   * @param personTO the personTO to set
   */
  public void setPersonTO( PersonTO personTO )
  {
    this.personTO = personTO;
  }

  /**
   * @return the user
   */
  public UserTO getUser()
  {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser( UserTO user )
  {
    this.user = user;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof AbstractObservationTO )
    {
      flag = this.id.equals( ((AbstractObservationTO) object).id );
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return this.id.hashCode();
  }

  @Override
  public String toString()
  {
    ToStringBuilder ts = new ToStringBuilder( this );
    ts.append( "id", id );
    ts.append( "observation", observation );
    return ts.toString();
  }

}
