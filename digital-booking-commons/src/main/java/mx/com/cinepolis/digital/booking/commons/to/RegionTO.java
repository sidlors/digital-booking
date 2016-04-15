package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase que modela una región
 * 
 * @author rgarcia
 * @param <T1> Catálogo de Región
 * @param <T2>Id del Territorio padre de la región
 */
public class RegionTO<T1 extends CatalogTO, T2 extends CatalogTO> implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 7483155831748744798L;

  private T1 catalogRegion;
  private T2 idTerritory;

  private List<PersonTO> persons;

  /**
   * Constructor de la Región
   * 
   * @param catalogRegion Información del catálogo de la región
   * @param idTerritory
   */
  public RegionTO( T1 catalogRegion, T2 idTerritory )
  {
    this.catalogRegion = catalogRegion;
    this.idTerritory = idTerritory;
  }

  /**
   * @return the catalogRegion
   */
  public T1 getCatalogRegion()
  {
    return catalogRegion;
  }

  /**
   * @param catalogRegion the catalogRegion to set
   */
  public void setCatalogRegion( T1 catalogRegion )
  {
    this.catalogRegion = catalogRegion;
  }

  /**
   * @return the idTerritory
   */
  public T2 getIdTerritory()
  {
    return idTerritory;
  }

  /**
   * @param idTerritory the idTerritory to set
   */
  public void setIdTerritory( T2 idTerritory )
  {
    this.idTerritory = idTerritory;
  }

  /**
   * @return the persons
   */
  public List<PersonTO> getPersons()
  {
    return persons;
  }

  /**
   * @param persons the persons to set
   */
  public void setPersons( List<PersonTO> persons )
  {
    this.persons = persons;
  }

  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "idRegion", this.getCatalogRegion().toString() );
    builder.append( "IdTerritory", this.getIdTerritory().toString() );
    return builder.toString();
  }

}
