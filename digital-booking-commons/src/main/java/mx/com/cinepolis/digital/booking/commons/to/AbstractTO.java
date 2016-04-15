package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;
import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Clase base con la información general de los TO's
 * 
 * @author rgarcia
 */
public class AbstractTO implements Serializable
{

  private static final long serialVersionUID = 2943980536005153152L;
  /**
   * Clave del usuario
   */
  private Long userId;
  /**
   * Nombre del usuario
   */
  private String username;
  /**
   * Fecha actual
   */
  // TODO AFU Quitar inicialización cuando se implemente el interceptor con info de
  // sesión
  private Date timestamp = new Date();
  /**
   * Indicador de si la información está activa
   */
  private boolean fgActive;

  private Long idLanguage = 1L;// TODO AFU Quitar inicialización cuando se implemente el interceptor con info de sesión

  /**
   * @return the id
   */
  public Long getUserId()
  {
    return this.userId;
  }

  /**
   * @param id the id to set
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * @return the username
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername( String username )
  {
    this.username = username;
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp()
  {
    return CinepolisUtils.enhancedClone( timestamp );
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp( Date timestamp )
  {
    this.timestamp = CinepolisUtils.enhancedClone( timestamp );
  }

  /**
   * @return the fgActive
   */
  public boolean isFgActive()
  {
    return fgActive;
  }

  /**
   * @param fgActive the fgActive to set
   */
  public void setFgActive( boolean fgActive )
  {
    this.fgActive = fgActive;
  }

  /**
   * @return the idLanguage
   */
  public Long getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( Long idLanguage )
  {
    this.idLanguage = idLanguage;
  }
}
