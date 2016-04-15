package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import java.lang.reflect.Field;

import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;

import org.xlsx4j.sml.CTStylesheet;
import org.xlsx4j.sml.STCellType;

/**
 * Clase abstracta usada para manejar los tipos de variables del objeto. Proporciona funcionalidades básicas como
 * transformar el valor de la celda, darle formato y obtener el tipo de celda
 * 
 * @author jchavez
 * @since 0.5.0
 */
public abstract class AbstractXlsxColumn
{
  private String fieldName;
  private String header;
  private CTStylesheet ctStylesheet;

  /**
   * Constructor por defecto
   */
  public AbstractXlsxColumn()
  {
  }

  /**
   * Constructor que inicializa el objeto con el nombre de la propiedad y el encabezado
   * 
   * @param fieldName Nombre de la propiedad. Se usa para en método {@link #transformValue}
   * @param header Nombre del encabezado de la columna
   */
  public AbstractXlsxColumn( String fieldName, String header )
  {
    this.fieldName = fieldName;
    this.header = header;
  }

  /**
   * Método abstracto que transforma el valor de la propiedad {@link #fieldName} del argumento {@code object} a string
   * 
   * @param object Objeto con toda la información del renglón
   * @return Cadena con el valor de la celda.
   * @author jchavez
   * @since 0.5.0
   */
  public abstract String transformValue( Object object );

  /**
   * Método abstracto que obtiene el conjunto de estilos de la celda. Este estilo va en función de si el renglón actual
   * es un event o un odd.
   * 
   * @param isOdd {@code true} si la celda se encuentra en un renglón {@code odd}, de lo contrario {@code false}
   * @return Indice del conjunto de estilos
   * @author jchavez
   * @since 0.5.0
   */
  public abstract Long getCellStyle( Boolean isOdd );

  /**
   * Método que obtiene el valor en la propiedad {@link #fieldName} del parámetro {@code object}
   * 
   * @param object Objeto con la información del renglón.
   * @return Valor en la propiedad.
   * @author jchavez
   * @since 0.5.0
   */
  public Object extractFieldValue( Object object )
  {
    Class<?> clazz = object.getClass();
    Field field = ReflectionHelper.findField( clazz, this.fieldName );
    ReflectionHelper.makeAccessible( field );
    return ReflectionHelper.getField( field, object );
  }

  /**
   * Método que obtiene el tipo de celda
   * 
   * @return Enum con el tipo de celda, por defecto regresa null.
   * @author jchavez
   * @since 0.5.0
   */
  public STCellType getCellType()
  {
    return null;
  }

  /**
   * @return the fieldName
   */
  public String getFieldName()
  {
    return fieldName;
  }

  /**
   * @param fieldName the fieldName to set
   */
  public void setFieldName( String fieldName )
  {
    this.fieldName = fieldName;
  }

  /**
   * @return the header
   */
  public String getHeader()
  {
    return header;
  }

  /**
   * @param header the header to set
   */
  public void setHeader( String header )
  {
    this.header = header;
  }

  /**
   * @return the ctStylesheet
   */
  public CTStylesheet getCtStylesheet()
  {
    return ctStylesheet;
  }

  /**
   * @param ctStylesheet the ctStylesheet to set
   */
  public void setCtStylesheet( CTStylesheet ctStylesheet )
  {
    this.ctStylesheet = ctStylesheet;
  }
}