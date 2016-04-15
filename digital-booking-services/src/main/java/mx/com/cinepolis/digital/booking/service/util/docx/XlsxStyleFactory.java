package mx.com.cinepolis.digital.booking.service.util.docx;

import javax.xml.bind.JAXBElement;

import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTBooleanProperty;
import org.xlsx4j.sml.CTBorder;
import org.xlsx4j.sml.CTBorderPr;
import org.xlsx4j.sml.CTCellAlignment;
import org.xlsx4j.sml.CTColor;
import org.xlsx4j.sml.CTFill;
import org.xlsx4j.sml.CTFont;
import org.xlsx4j.sml.CTFontFamily;
import org.xlsx4j.sml.CTFontName;
import org.xlsx4j.sml.CTFontScheme;
import org.xlsx4j.sml.CTFontSize;
import org.xlsx4j.sml.CTNumFmt;
import org.xlsx4j.sml.CTPatternFill;
import org.xlsx4j.sml.CTXf;
import org.xlsx4j.sml.STBorderStyle;
import org.xlsx4j.sml.STFontScheme;
import org.xlsx4j.sml.STHorizontalAlignment;
import org.xlsx4j.sml.STPatternType;
import org.xlsx4j.sml.STVerticalAlignment;

/**
 * Clase factory para los estilos iniciales de un excel
 * 
 * @author jchavez
 * @since 0.5.0
 */
public final class XlsxStyleFactory
{
  /**
   * Constructor privado
   */
  private XlsxStyleFactory()
  {
  }

  /** ================== INICIAN FORMATOS DE NÚMERO PARA LA CELDA ================== **/

  /**
   * Método que crea el formato monetario. {@code $#,##0.00}
   * 
   * @param numFmt Identificador del formato de número
   * @return Objeto para formato de número
   * @author jchavez
   * @since 0.5.0
   */
  public static CTNumFmt createFormatCurrency( Long numFmt )
  {
    CTNumFmt ctNumFmt = new CTNumFmt();
    ctNumFmt.setFormatCode( "\"$\"#,##0.00" );
    ctNumFmt.setNumFmtId( numFmt );
    return ctNumFmt;
  }

  /** ================== TERMINAN FORMATOS DE NÚMERO PARA LA CELDA ================== **/

  /** ================== INICIAN ESTILOS DE FUENTES ================== **/

  /**
   * Método que crea la fuente por defecto para el Excel. El tamaño de la fuenta es 11 y tipo Calibri
   * 
   * @return Objeto con el estilo de la fuente
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFont createFontDefault()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 11L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 1L );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Calibri" );
    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );
    CTFontFamily famname0 = new CTFontFamily();
    famname0.setVal( 2 );
    JAXBElement<CTFontFamily> element3 = Context.getsmlObjectFactory().createCTFontFamily( famname0 );
    font.getNameOrCharsetOrFamily().add( element3 );
    CTFontScheme fschema = new CTFontScheme();
    fschema.setVal( STFontScheme.MINOR );
    JAXBElement<CTFontScheme> element4 = Context.getsmlObjectFactory().createCTFontScheme( fschema );
    font.getNameOrCharsetOrFamily().add( element4 );
    return font;
  }

  /**
   * Método que crea la fuente con negritas. No se especifica tamaño ni tipo.
   * 
   * @return Objeto con el estilo de la fuente
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFont createFontBold()
  {
    CTFont font1 = new CTFont();
    JAXBElement<CTBooleanProperty> element16 = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font1.getNameOrCharsetOrFamily().add( element16 );
    return font1;
  }

  /**
   * Creates a font Verdana size 9
   * 
   * @return
   */
  public static CTFont createFontVerdanaSize9()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 9L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 1L );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Verdana" );
    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );
    CTFontFamily famname0 = new CTFontFamily();
    famname0.setVal( 2 );
    JAXBElement<CTFontFamily> element3 = Context.getsmlObjectFactory().createCTFontFamily( famname0 );
    font.getNameOrCharsetOrFamily().add( element3 );
    return font;
  }

  /**
   * Creates a font Verdana 8
   * 
   * @return
   */
  public static CTFont createFontVerdanaSize8()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 8L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 1L );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Verdana" );
    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );
    CTFontFamily famname0 = new CTFontFamily();
    famname0.setVal( 2 );
    JAXBElement<CTFontFamily> element3 = Context.getsmlObjectFactory().createCTFontFamily( famname0 );
    font.getNameOrCharsetOrFamily().add( element3 );
    return font;
  }

  /**
   * Creates a font Verdana Size 9 Bold
   * 
   * @return
   */
  public static CTFont createFontVerdanaSize9Bold()
  {
    CTFont font = createFontVerdanaSize9();
    JAXBElement<CTBooleanProperty> bold = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font.getNameOrCharsetOrFamily().add( bold );
    return font;
  }

  /**
   * Creates a font Verdana size 8 Bold white
   * 
   * @return
   */
  public static CTFont createFontVerdanaSize8BoldWhite()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 8L );
    JAXBElement<CTFontSize> fontSize = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( fontSize );
    CTColor color = new CTColor();
    color.setRgb( new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 } );
    color.setIndexed( 64L );
    JAXBElement<CTColor> fontColor = Context.getsmlObjectFactory().createCTFontColor( color );
    font.getNameOrCharsetOrFamily().add( fontColor );
    CTFontName ctFontName = new CTFontName();
    ctFontName.setVal( "Verdana" );
    JAXBElement<CTFontName> fontName = Context.getsmlObjectFactory().createCTFontName( ctFontName );
    font.getNameOrCharsetOrFamily().add( fontName );
    CTFontFamily ctFontFamily = new CTFontFamily();
    ctFontFamily.setVal( 2 );
    JAXBElement<CTFontFamily> fontFamily = Context.getsmlObjectFactory().createCTFontFamily( ctFontFamily );
    font.getNameOrCharsetOrFamily().add( fontFamily );

    JAXBElement<CTBooleanProperty> bold = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font.getNameOrCharsetOrFamily().add( bold );

    return font;
  }

  /**
   * Creates a font Arial size 8
   * 
   * @return
   */
  public static CTFont createFontArialSize8()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 8L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 1L );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Arial" );
    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );
    CTFontFamily famname0 = new CTFontFamily();
    famname0.setVal( 2 );
    JAXBElement<CTFontFamily> element3 = Context.getsmlObjectFactory().createCTFontFamily( famname0 );
    font.getNameOrCharsetOrFamily().add( element3 );
    return font;
  }

  /**
   * Creates a font Arial 8 Bold
   * 
   * @return
   */
  public static CTFont createFontArialSize8Bold()
  {
    CTFont font = createFontArialSize8();
    JAXBElement<CTBooleanProperty> bold = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font.getNameOrCharsetOrFamily().add( bold );
    return font;
  }

  /**
   * Creates a font Arial size 14 Bold
   * 
   * @return
   */
  public static CTFont createFontArialSize14Bold()
  {
    CTFont font = new CTFont();
    CTFontSize sz = new CTFontSize();
    sz.setVal( 14L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 1L );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Arial" );
    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );
    CTFontFamily famname0 = new CTFontFamily();
    famname0.setVal( 2 );
    JAXBElement<CTFontFamily> element3 = Context.getsmlObjectFactory().createCTFontFamily( famname0 );
    font.getNameOrCharsetOrFamily().add( element3 );
    JAXBElement<CTBooleanProperty> bold = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font.getNameOrCharsetOrFamily().add( bold );
    return font;
  }

  /**
   * Método que crea la fuente webdings 2 (para el ícono de la "palomita")
   * 
   * @return Objeto con el estilo de la fuente
   * @author gsegura
   * @since 0.6.0
   */
  public static CTFont createFontWingdings2()
  {
    CTFont font = new CTFont();

    CTFontSize sz = new CTFontSize();
    sz.setVal( 11L );
    JAXBElement<CTFontSize> element0 = Context.getsmlObjectFactory().createCTFontSz( sz );
    font.getNameOrCharsetOrFamily().add( element0 );
    CTColor col = new CTColor();
    col.setTheme( 6L );
    col.setTint( -0.499984740745262 );
    JAXBElement<CTColor> element1 = Context.getsmlObjectFactory().createCTFontColor( col );
    font.getNameOrCharsetOrFamily().add( element1 );
    CTFontName fname0 = new CTFontName();
    fname0.setVal( "Wingdings 2" );

    JAXBElement<CTFontName> element2 = Context.getsmlObjectFactory().createCTFontName( fname0 );
    font.getNameOrCharsetOrFamily().add( element2 );

    JAXBElement<CTBooleanProperty> bold = Context.getsmlObjectFactory().createCTFontB( new CTBooleanProperty() );
    font.getNameOrCharsetOrFamily().add( bold );

    return font;
  }

  /** ================== TERMINAN ESTILOS DE FUENTES ================== **/

  /** ================== INICIAN ESTILOS DE FONDOS DE CELDA ================== **/

  /**
   * Método que crea el estilo del fondo por defecto. Sin color
   * 
   * @return Objeto con el estilo del fondo
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFill createFillDefault()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.NONE );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Método que crea el estilo del fondo por defecto. Color gris
   * 
   * @return Objeto con el estilo del fondo
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFill createFillGrayDefault()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.GRAY_125 );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Creates a background of Dark Gray (RGB: 0x8A8A8A)
   * 
   * @return
   */
  public static CTFill createFillDarkGray()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setRgb( new byte[] { (byte) 255, (byte) 138, (byte) 138, (byte) 138 } );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Creates a background of Light Gray (RGB: 0xEEEEEE)
   * 
   * @return
   */
  public static CTFill createFillLightGray()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setRgb( new byte[] { (byte) 255, (byte) 238, (byte) 238, (byte) 238 } );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Creates a background of Gray (RGB: 0xE1E1E1)
   * 
   * @return
   */
  public static CTFill createFillGray()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setRgb( new byte[] { (byte) 255, (byte) 225, (byte) 225, (byte) 225 } );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Creates a background fill solid with RGB
   * 
   * @param red
   * @param green
   * @param blue
   * @return
   */
  public static CTFill createFillRGB( int red, int green, int blue )
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setRgb( new byte[] { (byte) 255, (byte) red, (byte) green, (byte) blue } );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Creates a background of White (RGB: 0xFFFFFF)
   * 
   * @return
   */
  public static CTFill createFillWhite()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setRgb( new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 } );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Método que crea el estilo del fondo color azul
   * 
   * @return Objeto con el estilo del fondo
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFill createFillHeader()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setTheme( 4L );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /**
   * Método que crea el estilo del fondo color gris tenue
   * 
   * @return Objeto con el estilo del fondo
   * @author jchavez
   * @since 0.5.0
   */
  public static CTFill createFillOdd()
  {
    CTFill ctFill = new CTFill();
    CTPatternFill ctPatternFill = new CTPatternFill();
    ctPatternFill.setPatternType( STPatternType.SOLID );
    CTColor fgColor = new CTColor();
    fgColor.setTheme( 0L );
    fgColor.setTint( -0.14996795556505021 );
    CTColor bgColor = new CTColor();
    bgColor.setIndexed( 64L );
    ctPatternFill.setFgColor( fgColor );
    ctPatternFill.setBgColor( bgColor );
    ctFill.setPatternFill( ctPatternFill );
    return ctFill;
  }

  /** ================== TERMINAN ESTILOS DE FONDOS DE CELDA ================== **/

  /** ================== INICIAN ESTILOS DE BORDES DE CELDA ================== **/

  /**
   * Método que crea el estilo del borde por defecto. Sin bordes
   * 
   * @return Objeto con el estilo del borde
   * @author jchavez
   * @since 0.5.0
   */
  public static CTBorder createBorderDefault()
  {
    CTBorder ctBorder = new CTBorder();
    ctBorder.setLeft( new CTBorderPr() );
    ctBorder.setRight( new CTBorderPr() );
    ctBorder.setTop( new CTBorderPr() );
    ctBorder.setBottom( new CTBorderPr() );
    ctBorder.setDiagonal( new CTBorderPr() );
    return ctBorder;
  }

  /**
   * Método que crea el estilo del borde cuadrado
   * 
   * @return Objeto con el estilo del borde
   * @author jchavez
   * @since 0.5.0
   */
  public static CTBorder createBorderAll()
  {
    CTBorder ctBorder = new CTBorder();
    ctBorder.setLeft( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setRight( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setTop( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setBottom( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setDiagonal( new CTBorderPr() );
    return ctBorder;
  }

  /**
   * Creates a border with style with default color
   * 
   * @param style
   * @return
   */
  public static CTBorderPr createBorderPr( STBorderStyle style )
  {
    return createBorderPr( style, null, null, null );
  }

  /**
   * Creates a border with style and color, if all colors are null it will be used the default color black
   * 
   * @param style
   * @param red
   * @param green
   * @param blue
   * @return
   */
  public static CTBorderPr createBorderPr( STBorderStyle style, Integer red, Integer green, Integer blue )
  {
    CTBorderPr ctBorderPr = new CTBorderPr();
    ctBorderPr.setStyle( style );
    CTColor ctColor = new CTColor();

    boolean applyColor = false;
    byte[] rgb = { (byte) 255, (byte) 0, (byte) 0, (byte) 0, };
    rgb[0] = (byte) 255;
    if( red != null )
    {
      rgb[1] = (byte) red.intValue();
      applyColor = true;
    }
    if( green != null )
    {
      rgb[2] = (byte) green.intValue();
      applyColor = true;
    }
    if( blue != null )
    {
      rgb[3] = (byte) blue.intValue();
      applyColor = true;
    }
    if( applyColor )
    {
      ctColor.setRgb( rgb );
    }

    ctColor.setIndexed( 64L );
    ctBorderPr.setColor( ctColor );
    return ctBorderPr;
  }

  /**
   * Creater a border style thin, borders: {Left, Top, Bottom}
   * 
   * @return
   */
  public static CTBorder createBorderLeftTopBottom()
  {
    CTBorder ctBorder = new CTBorder();
    ctBorder.setLeft( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setRight( new CTBorderPr() );
    ctBorder.setTop( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setBottom( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setDiagonal( new CTBorderPr() );
    return ctBorder;
  }

  /**
   * Creater a border style thin, borders: {Top, Bottom}
   * 
   * @return
   */
  public static CTBorder createBorderTopBottom()
  {
    CTBorder ctBorder = new CTBorder();
    ctBorder.setLeft( new CTBorderPr() );
    ctBorder.setRight( new CTBorderPr() );
    ctBorder.setTop( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setBottom( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setDiagonal( new CTBorderPr() );
    return ctBorder;
  }

  /**
   * Creater a border style thin, borders: {Right, Top, Bottom}
   * 
   * @return
   */
  public static CTBorder createBorderRightTopBottom()
  {
    CTBorder ctBorder = new CTBorder();
    ctBorder.setLeft( new CTBorderPr() );
    ctBorder.setRight( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setTop( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setBottom( createBorderPr( STBorderStyle.THIN ) );
    ctBorder.setDiagonal( new CTBorderPr() );
    return ctBorder;
  }

  /**
   * Creates a border medium in all sides
   * 
   * @return
   */
  public static CTBorder createBorderAllMedium()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getLeft().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getRight().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getTop().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getBottom().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in top side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumTop()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getTop().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in top and left side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumTopLeft()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getLeft().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getTop().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in top and right side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumTopRight()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getRight().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getTop().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in left side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumLeft()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getLeft().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in right side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumRight()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getRight().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in bottom and left side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumBottomLeft()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getLeft().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getBottom().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in bottom side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumBottom()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getBottom().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /**
   * Creates a border medium in bottom and right side, thin in all rest
   * 
   * @return
   */
  public static CTBorder createBorderAllMediumBottomRight()
  {
    CTBorder ctBorder = createBorderAll();
    ctBorder.getRight().setStyle( STBorderStyle.MEDIUM );
    ctBorder.getBottom().setStyle( STBorderStyle.MEDIUM );
    return ctBorder;
  }

  /** ================== TERMINAN ESTILOS DE BORDES DE CELDA ================== **/

  /** ================== INICIAN CONJUNTO DE ESTILOS DE CELDA ================== **/

  /**
   * Método que crea el conjunto de estilos para las celdas por defecto
   * 
   * @return Objeto con el conjunto de estilos
   * @author jchavez
   * @since 0.5.0
   */
  public static CTXf createFinalStyleDefault()
  {
    CTXf xf = new CTXf();
    xf.setNumFmtId( 0L );
    xf.setFontId( 0L );
    xf.setFillId( 0L );
    xf.setBorderId( 0L );
    xf.setXfId( 0L );
    return xf;
  }

  /**
   * Método que crea el conjunto de estilos por medio de los argumentos
   * 
   * @param numFmt Identificador del formato de la celda
   * @param font Índice de la fuente
   * @param fill Índice del fondo
   * @param border Índice del borde
   * @return Objeto con el conjunto de estilos
   * @author jchavez
   * @since 0.5.0
   */
  public static CTXf createFinalStyle( Long numFmt, Long font, Long fill, Long border )
  {
    CTXf xf = new CTXf();
    xf.setNumFmtId( numFmt );
    xf.setFontId( font );
    xf.setFillId( fill );
    xf.setBorderId( border );
    xf.setXfId( 0L );
    xf.setApplyNumberFormat( numFmt != null && numFmt > 0 );
    xf.setApplyFont( font != null && font > 0 );
    xf.setApplyFill( fill != null && fill > 0 );
    xf.setApplyBorder( border != null && border > 0 );
    return xf;
  }

  /**
   * Método que crea el conjunto de estilos por medio de los argumentos
   * 
   * @param numFmt Identificador del formato de la celda
   * @param font Índice de la fuente
   * @param fill Índice del fondo
   * @param border Índice del borde
   * @param rotation Valor de la rotación del objeto
   * @return Objeto con el conjunto de estilos
   * @author gsegura
   * @since 0.6.0
   */
  public static CTXf createFinalStyle( Long numFmt, Long font, Long fill, Long border, Long rotation )
  {
    CTXf xf = createFinalStyle( numFmt, font, fill, border );
    CTCellAlignment value = new CTCellAlignment();
    value.setTextRotation( rotation );
    xf.setAlignment( value );
    return xf;
  }

  /**
   * Método que crea el conjunto de estilos por medio de los argumentos
   * 
   * @param numFmt Identificador del formato de la celda
   * @param font Índice de la fuente
   * @param fill Índice del fondo
   * @param border Índice del borde
   * @param rotation Valor de la rotación del objeto
   * @return Objeto con el conjunto de estilos
   * @author rgarcia
   * @since 0.6.0
   */
  public static CTXf createFinalStyleAndCenter( Long numFmt, Long font, Long fill, Long border )
  {
    CTXf xf = createFinalStyle( numFmt, font, fill, border );
    CTCellAlignment value = new CTCellAlignment();
    value.setHorizontal( STHorizontalAlignment.CENTER );
    value.setVertical( STVerticalAlignment.BOTTOM );
    xf.setAlignment( value );
    return xf;
  }

  /** ================== TERMINAN CONJUNTO DE ESTILOS DE CELDA ================== **/
}