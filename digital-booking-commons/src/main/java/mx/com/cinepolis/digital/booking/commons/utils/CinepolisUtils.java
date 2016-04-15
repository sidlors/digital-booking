package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase con diversas utilerias para el proyecto.
 */
public final class CinepolisUtils implements Serializable
{
  private static final Logger LOG = LoggerFactory.getLogger( CinepolisUtils.class );

  private static final long serialVersionUID = 7623091118983819364L;
  private static final String EXTENSION_SEPARATOR = ".";

  private CinepolisUtils()
  {

  }

  /**
   * Clona un {@code java.util.Date} realizando antes las validaciones mas comunes.
   * 
   * @param original {@code java.util.Date} que se desea clonar.
   * @return Un {@code java.util.Date} clon de {@code original} o {@code null} si {@code original} es {@code null}.
   * @author iarces
   */
  public static Date enhancedClone( Date original )
  {
    Date clone = null;

    if( original != null )
    {
      clone = (Date) original.clone();
    }

    return clone;
  }

  /**
   * Clona un arreglo de <T> a otro
   * 
   * @param object Arreglo origen
   * @return Arreglo clonado
   */
  public static <T> T[] enhacedArrayClone( T[] object )
  {
    T[] clone = null;
    if( object != null )
    {
      clone = object.clone();
    }
    return clone;
  }

  /**
   * Clona un arreglo de bytes a otro
   * 
   * @param object Arreglo origen
   * @return Arreglo clonado
   */
  public static byte[] enhacedByteArrayClone( byte[] object )
  {
    byte[] clone = null;
    if( object != null )
    {
      clone = object.clone();
    }
    return clone;
  }

  /**
   * Cierra de manera segura un {@code java.io.Writer}, haciendo primero validacion de nulabilidad.
   * 
   * @param stream El flujo que se desea cerrar
   * @throws IOException Si ocurre algun error al cerrar el flujo.
   */
  public static void safeStreamClose( Writer stream ) throws IOException
  {
    if( stream != null )
    {
      stream.close();
    }
  }

  /**
   * Cierra de manera segura un {@code java.io.Reader}, haciendo primero validacion de nulabilidad.
   * 
   * @param stream El flujo que se desea cerrar
   * @throws IOException Si ocurre algun error al cerrar el flujo.
   */
  public static void safeStreamClose( Reader stream ) throws IOException
  {
    if( stream != null )
    {
      stream.close();
    }
  }

  /**
   * Cierra de manera segura un {@code java.io.OutputStream}, haciendo primero validacion de nulabilidad.
   * 
   * @param stream El flujo que se desea cerrar
   * @throws IOException Si ocurre algun error al cerrar el flujo.
   */
  public static void safeStreamClose( OutputStream stream ) throws IOException
  {
    if( stream != null )
    {
      stream.close();
    }
  }

  /**
   * Cierra de manera segura un {@code java.io.InputStream}, haciendo primero validacion de nulabilidad.
   * 
   * @param stream El flujo que se desea cerrar
   * @throws IOException Si ocurre algun error al cerrar el flujo.
   */
  public static void safeStreamClose( InputStream stream ) throws IOException
  {
    if( stream != null )
    {
      stream.close();
    }
  }

  /**
   * Cierra de manera segura un {@link CallableStatement}, haciendo primero validacion de nulabilidad.
   * 
   * @param call El statement que se desea cerrar
   * @throws SQLException Si ocurre algun error al cerrar el statement.
   */
  public static void safeCallableStatementClose( CallableStatement call ) throws SQLException
  {
    if( call != null )
    {
      call.close();
    }
  }

  /**
   * Cierra de manera segura un {@link ResultSet}, haciendo primero validacion de nulabilidad.
   * 
   * @param resultSet El resultSet que se desea cerrar
   * @throws SQLException Si ocurre algun error al cerrar el resultSet.
   */
  public static void safeResultSetClose( ResultSet resultSet ) throws SQLException
  {
    if( resultSet != null )
    {
      resultSet.close();
    }

  }

  /**
   * M&eacute;todo que construye una cadena mutable en base a los argumentos proporcionados.
   * 
   * @param args Argumentos para construir la cadena mutable.
   * @return {@link StringBuilder} con la cadena mutable en base a los argumentos proporcionados.
   * @author afuentes
   */
  public static StringBuilder buildMutableString( Object... args )
  {
    StringBuilder builder = new StringBuilder();
    for( Object object : args )
    {
      builder.append( object );
    }
    return builder;
  }

  /**
   * M&eacute;todo que construye una cadena usando una cadena mutable en base a los argumentos proporcionados.
   * 
   * @param args Argumentos para construir la cadena mutable.
   * @return {@link String} con la cadena no mutable en base a los argumentos proporcionados.
   * @author afuentes
   */
  public static String buildStringUsingMutable( Object... args )
  {
    return buildMutableString( args ).toString();
  }

  /**
   * M&eacute;todo que revisa si todos los {@link String} proporcionados son diferente de <code>null</code>, no
   * vac&iacute;os y que no tengan s&oacute;lo espacios en blanco.
   * 
   * @param args Argumentos {@link String} para verificar.
   * @return <code>true</code> si todos los {@link String} proporcionados son diferente de <code>null</code>, no
   *         vac&iacute;os y que no tengan s&oacute;lo espacios en blanco, <code>false</code> en otro caso.
   * @author afuentes
   */
  public static boolean assertNotEmpty( String... args )
  {
    for( String string : args )
    {
      if( StringUtils.isBlank( string ) )
      {
        return false;
      }
    }
    return true;
  }

  /**
   * M&eacute;todo que obtiene la extensi&oacute;n del nombre del archivo proporcionado.
   * 
   * @param fileName Nombre del archivo con extensi&oacute;n.
   * @return {@link String} con la extensi&oacute;n del nombre del archivo proporcionado.
   * @author afuentes
   */
  public static String getFileExtension( String fileName )
  {
    String extension = StringUtils.EMPTY;
    if( StringUtils.isNotBlank( fileName ) )
    {
      extension = StringUtils.substringAfterLast( fileName, EXTENSION_SEPARATOR );
    }
    return extension;
  }

  /**
   * M&eacute;todo que obtiene el primer elemento de una lista.
   * 
   * @param list Lista de objetos.
   * @return El primer elemento de la lista o <code>null</code> si la lista es <code>null</code> o si est&aacute;
   *         vac&iacute;a.
   * @author afuentes
   */
  public static <T> T findFirstElement( List<T> list )
  {
    T element = null;
    if( CollectionUtils.isNotEmpty( list ) )
    {
      element = list.get( 0 );
    }
    return element;
  }

  /**
   * M&eacute;todo que obtiene el primer elemento activo de una lista. Se considera un elemento activo si su
   * m&eacute;todo getActive devuelve <code>true</code>.
   * 
   * @param list Lista de objetos.
   * @return El primer elemento activo de la lista o <code>null</code> si la lista es <code>null</code>, si est&aacute;
   *         vac&iacute;a o si no tiene elementos activos.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public static <T> T findFirstActiveElement( List<T> list )
  {
    return (T) CollectionUtils.find( list, PredicateUtils.invokerPredicate( "isFgActive" ) );
  }

  /**
   * M&eacute;todo que valida que la fecha {@code second} sea mayor o igual que la fecha {@code first}
   * 
   * @param first Primera fecha
   * @param second Segunda fecha
   * @return {@code true} si la segunda fecha es mayor o igual a la primera, de lo contrario {@code false}
   * @author jchavez
   * @since 0.2.0
   */
  public static boolean geDates( Date first, Date second )
  {
    return second.after( first ) || second.equals( first );
  }

  /**
   * M&eacute;todo que clona la fecha proporcionada (para no afectar la original) y trunca la hora.
   * 
   * @param date Fecha original.
   * @return Clon de la fecha proporcionada con la hora truncada.
   * @author afuentes
   */
  public static Date cloneAndTruncateTime( Date date )
  {
    return DateUtils.truncate( enhancedClone( date ), Calendar.DATE );
  }

  /**
   * M&eacute;todo que cierra un {@link java.sql.Connection} validando que no sea nulo
   * 
   * @param connection
   * @throws SQLException
   */
  public static void safeConnectionClose( Connection connection ) throws SQLException
  {
    if( connection != null )
    {
      connection.close();
    }
  }

  /**
   * M&eacute;todo que cierra un {@link java.sql.PreparedStatement} validando que no sea nulo
   * 
   * @param ps
   * @throws SQLException
   */
  public static void safePreparedStatementClose( PreparedStatement ps ) throws SQLException
  {
    if( ps != null )
    {
      ps.close();
    }

  }

}
