package mx.com.cinepolis.digital.booking.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Embedded PK for {@link mx.com.cinepolis.digital.booking.model.IncomeViewDO}
 * @author gsegura
 *
 */
@Embeddable
public class IncomeViewPK implements Serializable, Comparable<IncomeViewPK>
{
  private static final long serialVersionUID = 8879980714981474592L;

  @Column(name = "idVistaComplejo")
  private int theaterId;

  @Column(name = "numeroSalaComplejo")
  private int screen;

  @Column(name = "fechaFuncion")
  private String date;

  @Column(name = "horaFuncion")
  private String show;

  @Column(name = "idVistaPelicula")
  private int movieId;

  /**
   * @return the theaterId
   */
  public int getTheaterId()
  {
    return theaterId;
  }

  /**
   * @param theaterId the theaterId to set
   */
  public void setTheaterId( int theaterId )
  {
    this.theaterId = theaterId;
  }

  /**
   * @return the screen
   */
  public int getScreen()
  {
    return screen;
  }

  /**
   * @param screen the screen to set
   */
  public void setScreen( int screen )
  {
    this.screen = screen;
  }

  /**
   * @return the date
   */
  public String getDate()
  {
    return date;
  }

  /**
   * @param date the date to set
   */
  public void setDate( String date )
  {
    this.date = date ;
  }

  /**
   * @return the show
   */
  public String getShow()
  {
    return show;
  }

  /**
   * @param show the show to set
   */
  public void setShow( String show )
  {
    this.show = show;
  }

  /**
   * @return the movieId
   */
  public int getMovieId()
  {
    return movieId;
  }

  /**
   * @param movieId the movieId to set
   */
  public void setMovieId( int movieId )
  {
    this.movieId = movieId;
  }

  @Override
  public int compareTo( IncomeViewPK that )
  {
    return new CompareToBuilder().append( this.theaterId, that.theaterId ).append( this.screen, that.screen )
        .append( this.movieId, that.movieId ).append( this.date, that.date ).append( this.show, that.show )
        .toComparison();
  }

  @Override
  public boolean equals( Object o )
  {
    boolean flag = false;
    if( this == o )
    {
      flag = true;
    }
    else if( o instanceof IncomeViewPK )
    {
      IncomeViewPK that = (IncomeViewPK) o;
      flag = new EqualsBuilder().append( this.theaterId, that.theaterId ).append( this.screen, that.screen )
          .append( this.movieId, that.movieId ).append( this.date, that.date ).append( this.show, that.show )
          .isEquals();
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.theaterId ).append( this.screen ).append( this.movieId )
        .append( this.date ).append( this.show ).toHashCode();
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString( this );
  }
}
