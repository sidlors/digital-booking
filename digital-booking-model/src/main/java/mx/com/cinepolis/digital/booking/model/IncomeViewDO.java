package mx.com.cinepolis.digital.booking.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Entity for view RPT.V_INGRESOS_TAQUILLA_DIGITAL_BOOKING
 * @author gsegura
 *
 */
@Entity
@Table( name="V_INGRESOS_TAQUILLA_DIGITAL_BOOKING", schema="RPT" )
public class IncomeViewDO implements Serializable, Comparable<IncomeViewDO>
{
  private static final long serialVersionUID = 1744761365287347584L;

  @EmbeddedId
  private IncomeViewPK id;

  @Column(name = "nombreComplejo")
  private String theaterName;

  @Column(name = "idCiudadComplejo")
  private int cityId;

  @Column(name = "idEstadoComplejo")
  private int stateId;

  @Column(name = "nombrePelicula")
  private String movieName;

  @Column(name = "ingresos")
  private Double income;

  @Column(name = "numeroBoletos")
  private int tickets;

  /**
   * @return the id
   */
  public IncomeViewPK getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( IncomeViewPK id )
  {
    this.id = id;
  }

  /**
   * @return the theaterName
   */
  public String getTheaterName()
  {
    return theaterName;
  }

  /**
   * @param theaterName the theaterName to set
   */
  public void setTheaterName( String theaterName )
  {
    this.theaterName = theaterName;
  }

  /**
   * @return the cityId
   */
  public int getCityId()
  {
    return cityId;
  }

  /**
   * @param cityId the cityId to set
   */
  public void setCityId( int cityId )
  {
    this.cityId = cityId;
  }

  /**
   * @return the stateId
   */
  public int getStateId()
  {
    return stateId;
  }

  /**
   * @param stateId the stateId to set
   */
  public void setStateId( int stateId )
  {
    this.stateId = stateId;
  }

  /**
   * @return the movieName
   */
  public String getMovieName()
  {
    return movieName;
  }

  /**
   * @param movieName the movieName to set
   */
  public void setMovieName( String movieName )
  {
    this.movieName = movieName;
  }

  /**
   * @return the income
   */
  public Double getIncome()
  {
    return income;
  }

  /**
   * @param income the income to set
   */
  public void setIncome( Double income )
  {
    this.income = income;
  }

  /**
   * @return the tickets
   */
  public int getTickets()
  {
    return tickets;
  }

  /**
   * @param tickets the tickets to set
   */
  public void setTickets( int tickets )
  {
    this.tickets = tickets;
  }

  @Override
  public int compareTo( IncomeViewDO that )
  {
    return new CompareToBuilder().append( this.id, that.id ).toComparison();
  }

  @Override
  public boolean equals( Object o )
  {
    boolean flag = false;
    if( this == o )
    {
      flag = true;
    }
    else if( o instanceof IncomeViewDO )
    {
      flag = new EqualsBuilder().append( this.id, ((IncomeViewDO) o).id ).isEquals();
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.id ).toHashCode();
  }
  
  @Override
  public String toString(){
    return ToStringBuilder.reflectionToString( this );
  }

}
