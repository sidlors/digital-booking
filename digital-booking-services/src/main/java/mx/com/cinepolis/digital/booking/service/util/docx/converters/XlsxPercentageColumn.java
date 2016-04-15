package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.lang.BooleanUtils;

/**
 * Clase que maneja las variables de tipo {@code Number} con formato de porcentaje. Proporciona funcionalidades básicas
 * como transformar el valor de la celda, darle formato y obtener el tipo de celda
 * 
 * @author jchavez
 * @since 0.5.0
 */
public class XlsxPercentageColumn extends XlsxNumberColumn
{
  /**
   * Constructor por defecto
   */
  public XlsxPercentageColumn()
  {
    super();
  }

  /**
   * Constructor que inicializa el objeto con el nombre de la propiedad y el encabezado
   * 
   * @param fieldName Nombre de la propiedad. Se usa para en método {@link #transformValue}
   * @param header Nombre del encabezado de la columna
   */
  public XlsxPercentageColumn( String fieldName, String header )
  {
    super( fieldName, header );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCellStyle( Boolean isOdd )
  {
    return BooleanUtils.isTrue( isOdd ) ? XlsxUtils.PERCENTAGE_ODD : XlsxUtils.PERCENTAGE_EVENT;
  }
}