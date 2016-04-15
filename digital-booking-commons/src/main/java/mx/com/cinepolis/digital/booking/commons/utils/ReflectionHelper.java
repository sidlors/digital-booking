package mx.com.cinepolis.digital.booking.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilería para llamar los métodos de reflexión
 * 
 * @author gsegura
 */
public final class ReflectionHelper
{
  private static final Logger LOG = LoggerFactory.getLogger( ReflectionHelper.class );

  /** Se ofusca el contructor */
  private ReflectionHelper()
  {
    //
  }

  /**
   * Establece el valor de una propiedad por medio de reflexi&oacute;n
   * 
   * @param value Valor a settear
   * @param fieldName El nombre del atributo (
   * @param myInstance
   */
  public static void set( Object value, String fieldName, Object myInstance )
  {

    try
    {

      Class<?> clazz = getClassFor( myInstance, fieldName );
      Field field = clazz.getDeclaredField( fieldName );
      makeAccessible( field );

      field.set( myInstance, value );
    }
    catch( SecurityException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( NoSuchFieldException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalArgumentException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalAccessException e )
    {
      LOG.error( e.getMessage(), e );
    }

  }

  /**
   * Obtiene el valor del atributo dado el nombre y la instancia de la clase
   * 
   * @param fieldName
   * @param myInstance
   * @return
   */
  public static Object get( String fieldName, Object myInstance )
  {
    Object value = null;
    try
    {

      Class<?> clazz = getClassFor( myInstance, fieldName );
      Field field = clazz.getDeclaredField( fieldName );
      makeAccessible( field );

      value = field.get( myInstance );
    }
    catch( SecurityException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( NoSuchFieldException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalArgumentException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalAccessException e )
    {
      LOG.error( e.getMessage(), e );
    }
    return value;
  }

  @SuppressWarnings("rawtypes")
  private static Class getClassFor( Object myInstance, String fieldName )
  {
    Class clazz = myInstance.getClass();

    boolean fieldNameFound = false;
    while( true )
    {

      for( Field field : clazz.getDeclaredFields() )
      {
        if( field.getName().equals( fieldName ) )
        {
          fieldNameFound = true;
          break;
        }
      }

      if( fieldNameFound )
      {
        break;
      }
      if( clazz.getSuperclass() != null )
      {
        clazz = clazz.getSuperclass();
      }
      else
      {
        break;
      }

    }

    return clazz;
  }

  /**
   * Makes a {@link java.lang.reflect.Field} accessible
   * 
   * @param field
   */
  public static void makeAccessible( Field field )
  {
    if( ((Modifier.isPublic( field.getModifiers() )) && (Modifier.isPublic( field.getDeclaringClass().getModifiers() )) && (!(Modifier
        .isFinal( field.getModifiers() )))) || (field.isAccessible()) )
    {
      return;
    }
    field.setAccessible( true );
  }

  /**
   * Makes a {@link java.lang.reflect.Method} accessible
   * 
   * @param method
   */
  public static void makeAccessible( Method method )
  {
    if( ((Modifier.isPublic( method.getModifiers() )) && (Modifier.isPublic( method.getDeclaringClass().getModifiers() )))
        || (method.isAccessible()) )
    {
      return;
    }
    method.setAccessible( true );
  }

  /**
   * Makes a {@link java.lang.reflect.Constructor<?>} accessible
   * 
   * @param ctor
   */
  public static void makeAccessible( Constructor<?> ctor )
  {
    if( ((Modifier.isPublic( ctor.getModifiers() )) && (Modifier.isPublic( ctor.getDeclaringClass().getModifiers() )))
        || (ctor.isAccessible()) )
    {
      return;
    }
    ctor.setAccessible( true );
  }

  /**
   * Search a {@link java.lang.reflect.Field} given its class and name
   * 
   * @param clazz
   * @param name
   * @return
   */
  public static Field findField( Class<?> clazz, String name )
  {
    return findField( clazz, name, null );
  }

  /**
   * Search a {@link java.lang.reflect.Field} given its class and name
   * 
   * @param clazz
   * @param name
   * @param type
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static Field findField( Class<?> clazz, String name, Class<?> type )
  {
    validateField( clazz, name, type );
    Class searchType = clazz;
    while( (!(Object.class.equals( searchType ))) && (searchType != null) )
    {
      Field[] fields = searchType.getDeclaredFields();
      for( Field field : fields )
      {
        if( (((name == null) || (name.equals( field.getName() ))))
            && (((type == null) || (type.equals( field.getType() )))) )
        {
          return field;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  private static void validateField( Class<?> clazz, String name, Class<?> type )
  {
    if( clazz == null )
    {
      throw new IllegalArgumentException( "Class must not be null" );
    }
    if( !((name != null) || (type != null)) )
    {
      throw new IllegalArgumentException( "Either name or type of the field must be specified" );
    }
  }

  /**
   * Gets the value of the field from a given object
   * 
   * @param field
   * @param target
   * @return
   */
  public static Object getField( Field field, Object target )
  {
    try
    {
      return field.get( target );
    }
    catch( IllegalAccessException ex )
    {
      handleReflectionException( ex );
      throw new IllegalStateException( "Unexpected reflection exception - " + ex.getClass().getName() + ": "
          + ex.getMessage() );
    }
  }

  /**
   * Handles the reflection exceptions
   * 
   * @param ex
   */
  public static void handleReflectionException( Exception ex )
  {
    if( ex instanceof NoSuchMethodException )
    {
      throw new IllegalStateException( "Method not found: " + ex.getMessage() );
    }
    if( ex instanceof IllegalAccessException )
    {
      throw new IllegalStateException( "Could not access method: " + ex.getMessage() );
    }
    if( ex instanceof InvocationTargetException )
    {
      handleInvocationTargetException( (InvocationTargetException) ex );
    }
    if( ex instanceof RuntimeException )
    {
      throw ((RuntimeException) ex);
    }
    throw new UndeclaredThrowableException( ex );
  }

  /**
   * Handles the reflection exceptions
   * 
   * @param ex
   */
  private static void handleInvocationTargetException( InvocationTargetException ex )
  {
    rethrowRuntimeException( ex.getTargetException() );
  }

  /**
   * Handles the reflection exceptions
   * 
   * @param ex
   */
  private static void rethrowRuntimeException( Throwable ex )
  {
    if( ex instanceof RuntimeException )
    {
      throw ((RuntimeException) ex);
    }
    if( ex instanceof Error )
    {
      throw ((Error) ex);
    }
    throw new UndeclaredThrowableException( ex );
  }

}
