package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase que modela a un EventoS
 * 
 * @author rgarcia
 */
public class EventTO extends CatalogTO
{
  /**
   * Serialización 
   */
  private static final long serialVersionUID = 7951308022685411595L;
  /**
   * Id del Evento
   */
  private Long idEvent;
  /**
   * Nombre del evento
   */
  private String dsEventName;
  /**
   * Duración del evento
   */
  private Integer duration;
  /**
   * Código DBS
   */
  private String codeDBS;
  /**
   * Cantidad de copias
   */
  private Integer qtCopy;
  /**
   * Movie Format
   */
  private List<CatalogTO> movieFormats;
  /**
   * Sound Selected
   */
  private List<Long> soundsSelected;
  /**
   * Movies Selected
   */
  private List<Long> moviesSelected;
  /**
   * Sound Format
   */
  private List<CatalogTO> soundFormats;
  /**
   * Bandera para estrenos
   */
  private boolean premiere;
  /**
   * Bandera para deshabilitar las películas
   */
  private boolean currentMovie;
  /**
   * Bandera para identificar si es preestreno
   */
  private boolean prerelease;
  /**
   * Bandera para identificar festivales
   */
  private boolean festival;
  /**
   * Bandera para identificar un evento activo en IA
   */
  private boolean fgActiveIa;
  /**
   * Bandera para identificar eventos de yipo contenido alternativo
   */
  private boolean fgAlternateContent;

  /**
   * @return the idEvent
   */
  public Long getIdEvent()
  {
    return idEvent;
  }

  /**
   * @param idEvent the idEvent to set
   */
  public void setIdEvent( Long idEvent )
  {
    this.idEvent = idEvent;
  }

  /**
   * @return the dsEventName
   */
  public String getDsEventName()
  {
    return dsEventName;
  }

  /**
   * @param dsEventName the dsEventName to set
   */
  public void setDsEventName( String dsEventName )
  {
    this.dsEventName = dsEventName;
  }

  /**
   * @return the duration
   */
  public Integer getDuration()
  {
    return duration;
  }

  /**
   * @param duration the duration to set
   */
  public void setDuration( Integer duration )
  {
    this.duration = duration;
  }

  /**
   * @return the codeDBS
   */
  public String getCodeDBS()
  {
    return codeDBS;
  }

  /**
   * @param codeDBS the codeDBS to set
   */
  public void setCodeDBS( String codeDBS )
  {
    this.codeDBS = codeDBS;
  }

  /**
   * @return the qtCopy
   */
  public Integer getQtCopy()
  {
    return qtCopy;
  }

  /**
   * @param qtCopy the qtCopy to set
   */
  public void setQtCopy( Integer qtCopy )
  {
    this.qtCopy = qtCopy;
  }

  /**
   * @return the movieFormats
   */
  public List<CatalogTO> getMovieFormats()
  {
    return movieFormats;
  }

  /**
   * @param movieFormats the movieFormats to set
   */
  public void setMovieFormats( List<CatalogTO> movieFormats )
  {
    this.movieFormats = movieFormats;
  }

  /**
   * @return the soundsSelected
   */
  public List<Long> getSoundsSelected()
  {
    return soundsSelected;
  }

  /**
   * @param soundsSelected the soundsSelected to set
   */
  public void setSoundsSelected( List<Long> soundsSelected )
  {
    this.soundsSelected = soundsSelected;
  }

  /**
   * @return the moviesSelected
   */
  public List<Long> getMoviesSelected()
  {
    return moviesSelected;
  }

  /**
   * @param moviesSelected the moviesSelected to set
   */
  public void setMoviesSelected( List<Long> moviesSelected )
  {
    this.moviesSelected = moviesSelected;
  }

  /**
   * @return the soundFormats
   */
  public List<CatalogTO> getSoundFormats()
  {
    return soundFormats;
  }

  /**
   * @param soundFormats the soundFormats to set
   */
  public void setSoundFormats( List<CatalogTO> soundFormats )
  {
    this.soundFormats = soundFormats;
  }

  /**
   * @return the premiere
   */
  public boolean isPremiere()
  {
    return premiere;
  }

  /**
   * @param premiere the premiere to set
   */
  public void setPremiere( boolean premiere )
  {
    this.premiere = premiere;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.getIdEvent() );
    builder.append( "name", this.getDsEventName() );
    return builder.toString();
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof EventTO )
    {
      flag = this.idEvent.equals( ((EventTO) object).idEvent );
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.idEvent ).hashCode();
  }
  
  /**
   * @return the currentMovie
   */
  public boolean isCurrentMovie() {
	  return currentMovie;
  }

  /**
   * @param currentMovie the currentMovie to set
   */
  public void setCurrentMovie(boolean currentMovie) {
	  this.currentMovie = currentMovie;
  }

  /**
   * @return the prerelease
   */
  public boolean isPrerelease()
  {
    return prerelease;
  }

  /**
   * @param prerelease the prerelease to set
   */
  public void setPrerelease( boolean prerelease )
  {
    this.prerelease = prerelease;
  }

  /**
   * @return the festival
   */
  public boolean isFestival()
  {
    return festival;
  }

  /**
   * @param festival the festival to set
   */
  public void setFestival( boolean festival )
  {
    this.festival = festival;
  }

  /**
   * @return the fgActiveIa
   */
  public boolean isFgActiveIa()
  {
    return fgActiveIa;
  }

  /**
   * @param fgActiveIa the fgActiveIa to set
   */
  public void setFgActiveIa( boolean fgActiveIa )
  {
    this.fgActiveIa = fgActiveIa;
  }

  /**
   * @return the fgAlternateContent
   */
  public boolean isFgAlternateContent()
  {
    return fgAlternateContent;
  }

  /**
   * @param fgAlternateContent the fgAlternateContent to set
   */
  public void setFgAlternateContent( boolean fgAlternateContent )
  {
    this.fgAlternateContent = fgAlternateContent;
  }

}
