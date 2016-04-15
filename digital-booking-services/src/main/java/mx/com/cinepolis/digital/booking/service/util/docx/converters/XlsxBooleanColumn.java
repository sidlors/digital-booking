package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.lang.BooleanUtils;
import org.xlsx4j.sml.STCellType;

/**
 * Clase que maneja las variables de tipo {@code boolean}. Proporciona funcionalidades básicas como transformar el valor
 * de la celda, darle formato y obtener el tipo de celda
 * 
 * @author jchavez
 * @since 0.5.0
 */
public class XlsxBooleanColumn extends AbstractXlsxColumn
{
  private String falseText;
  private String trueText;

  /**
   * Constructor por defecto
   */
  public XlsxBooleanColumn()
  {
    super();
  }

  /**
   * Constructor que inicializa el objeto con el nombre de la propiedad, el encabezado, el texto cuando el valor es
   * {@code true} y cuando es {@code false}
   * 
   * @param fieldName Nombre de la propiedad. Se usa para en método {@link #transformValue}
   * @param header Nombre del encabezado de la columna
   * @param falseText Texto cuando la variable es {@code false}
   * @param trueText Texto cuando la variable es {@code true}
   */
  public XlsxBooleanColumn( String fieldName, String header, String falseText, String trueText )
  {
    super( fieldName, header );
    this.falseText = falseText;
    this.trueText = trueText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String transformValue( Object object )
  {
    Object fieldValue = extractFieldValue( object );
    String result = "";
    if( fieldValue instanceof Boolean )
    {
      Boolean value = (Boolean) fieldValue;
      result = BooleanUtils.isTrue( value ) ? this.trueText : this.falseText;
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCellStyle( Boolean isOdd )
  {
    return BooleanUtils.isTrue( isOdd ) ? XlsxUtils.STRING_ODD : XlsxUtils.STRING_EVENT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public STCellType getCellType()
  {
    return STCellType.STR;
  }

  /**
   * @return the falseText
   */
  public String getFalseText()
  {
    return falseText;
  }

  /**
   * @param falseText the falseText to set
   */
  public void setFalseText( String falseText )
  {
    this.falseText = falseText;
  }

  /**
   * @return the trueText
   */
  public String getTrueText()
  {
    return trueText;
  }

  /**
   * @param trueText the trueText to set
   */
  public void setTrueText( String trueText )
  {
    this.trueText = trueText;
  }

}