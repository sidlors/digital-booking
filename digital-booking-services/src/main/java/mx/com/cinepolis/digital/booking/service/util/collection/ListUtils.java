package mx.com.cinepolis.digital.booking.service.util.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilería para la manipulación de listas {@link java.util.List<T>}
 * 
 * @author jchavez
 */
public final class ListUtils
{
  /**
   * Contructor privado
   */
  private ListUtils()
  {
    // Clase de utilería
  }

  /**
   * Método que crea una lista <T> {@link java.util.List<T>} a partir del arreglo de objetos
   * 
   * @param object
   * @return
   */
  public static <T> List<T> asList( T... object )
  {
    List<T> list = new ArrayList<T>();
    for( T t : object )
    {
      list.add( t );
    }
    return list;
  }

  /**
   * Método que devuelve una lista vacía en caso de ser nula
   * 
   * @param list
   * @return
   */
  public static <T> List<T> ensureNotNull( List<T> list )
  {
    List<T> result = list;
    if( result == null )
    {
      result = new ArrayList<T>();
    }
    return result;
  }

  /**
   * Método que une listas
   * 
   * @param list
   * @return
   */
  public static <T> List<T> joinLists( List<T>... list )
  {
    List<T> result = new ArrayList<T>();
    for( List<T> list2 : list )
    {
      result.addAll( list2 );
    }
    return result;
  }
}
