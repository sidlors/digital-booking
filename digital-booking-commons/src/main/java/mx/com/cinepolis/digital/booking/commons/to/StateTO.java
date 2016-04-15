package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Clase que modela un Estado
 * @author rgarcia
 *
 * @param <T1> Catálogo de Estado
 * @param <T2> Id del país al que pertenece el estado
 */
public class StateTO<T1 extends CatalogTO, T2 extends Number> implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 7483155831748744798L;

  private T1 catalogState;
  private T2 idCountry;

  /**
   * Constructor del estado
   * @param catalogState  Información del catálogo del estado
   * @param idCountry Id del país
   */
  public StateTO( T1 catalogState, T2  idCountry )
  {
    this.catalogState = catalogState;
    this.idCountry = idCountry;
  }
  
  /**
   *  Se obtiene el catálogo del estado
   * @return the catalogState
   */
  public T1 getCatalogState()
  {
    return catalogState;
  }

  /**
   * Se obtiene el id del país a donde pertenece el estado
   * @return the catalogCountry
   */
  public T2 getCatalogCountry()
  {
    return idCountry;
  }
  
  @Override
  public String toString()
  {
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "idCountry", this.getCatalogCountry().toString() );
    builder.append( "IdState", this.getCatalogState().toString() );
    return builder.toString();
  }

}
