package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase que modela la información general de los catálogos
 * 
 * @author rgarcia
 */
public class CatalogTO extends AbstractTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 639991633213132954L;

  /**
   * Id
   */
  private Long id;

  /**
   * Vist Id
   */
  private String idVista;
  /**
   * Descripcion
   */
  private String name;

  /** Constructor default */
  public CatalogTO()
  {
  }

  /**
   * Contructor con id
   * 
   * @param id Id del catálogo
   */
  public CatalogTO( Long id )
  {
    this.id = id;
  }

  /**
   * Contructor con id y nombre
   * 
   * @param id Id del catálogo
   * @param name Nombre del catálogo
   */
  public CatalogTO( Long id, String name )
  {
    this.id = id;
    this.name = name;
  }

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
   * @return the idVista
   */
  public String getIdVista()
  {
    return idVista;
  }

  /**
   * @param idVista the idVista to set
   */
  public void setIdVista( String idVista )
  {
    this.idVista = idVista;
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

  @Override
  public boolean equals( Object other )
  {
    boolean result = false;
    if( this == other )
    {
      result = true;
    }
    else if( other instanceof CatalogTO )
    {
      CatalogTO o = (CatalogTO) other;
      EqualsBuilder builder = new EqualsBuilder();
      builder.append( id, o.id );
      result = builder.isEquals();
    }
    return result;
  }

  @Override
  public int hashCode()
  {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append( this.id );
    return builder.toHashCode();
  }

  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.getId() );
    builder.append( "name", this.getName() );
    return builder.toString();
  }
}
