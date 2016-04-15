package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.lang.BooleanUtils;

/**
 * Clase que maneja las variables de tipo {@code Number}. Proporciona funcionalidades básicas como transformar el valor
 * de la celda, darle formato y obtener el tipo de celda
 * 
 * @author jchavez
 * @since 0.5.0
 */
public class XlsxNumberColumn extends AbstractXlsxColumn
{
  /**
   * Constructor por defecto
   */
  public XlsxNumberColumn()
  {
    super();
  }

  /**
   * Constructor que inicializa el objeto con el nombre de la propiedad y el encabezado
   * 
   * @param fieldName Nombre de la propiedad. Se usa para en método {@link #transformValue}
   * @param header Nombre del encabezado de la columna
   */
  public XlsxNumberColumn( String fieldName, String header )
  {
    super( fieldName, header );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String transformValue( Object object )
  {
    Object fieldValue = extractFieldValue( object );
    String result = "";
    if( fieldValue != null )
    {
      result = fieldValue.toString();
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCellStyle( Boolean isOdd )
  {
    return BooleanUtils.isTrue( isOdd ) ? XlsxUtils.NUMBER_ODD : XlsxUtils.NUMBER_EVENT;
  }
}