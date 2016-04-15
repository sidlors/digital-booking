package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.lang.BooleanUtils;

/**
 * Class that handles variables of type {@code boolean}. Provides basic functionality as transform the value of the  
 * Cell, format it and get the type of cell
 * 
 * @author gsegura
 */
public class XlsxBooleanColumnFreeStyle extends XlsxBooleanColumn
{

  private long oddStyle;
  private long evenStyle;

  /**
   * Constructor default
   */
  public XlsxBooleanColumnFreeStyle()
  {
    super();
  }

  /**
   * Constructor by fieldName, header, String from false value, String for true value
   * 
   * @param fieldName
   * @param header
   * @param falseText
   * @param trueText
   */
  public XlsxBooleanColumnFreeStyle( String fieldName, String header, String falseText, String trueText )
  {
    super( fieldName, header, falseText, trueText );
    this.oddStyle = XlsxUtils.WINGDINGS_2_ODD;
    this.evenStyle = XlsxUtils.WINGDINGS_2_EVEN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCellStyle( Boolean isOdd )
  {
    return BooleanUtils.isTrue( isOdd ) ? this.oddStyle : this.evenStyle;
  }

  /**
   * @return the oddStyle
   */
  public long getOddStyle()
  {
    return oddStyle;
  }

  /**
   * @param oddStyle the oddStyle to set
   */
  public void setOddStyle( long oddStyle )
  {
    this.oddStyle = oddStyle;
  }

  /**
   * @return the evenStyle
   */
  public long getEvenStyle()
  {
    return evenStyle;
  }

  /**
   * @param evenStyle the evenStyle to set
   */
  public void setEvenStyle( long evenStyle )
  {
    this.evenStyle = evenStyle;
  }

}
