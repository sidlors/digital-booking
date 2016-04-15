package mx.com.cinepolis.digital.booking.service.util.docx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyBookingReportTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyDistributorReportTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.AbstractXlsxColumn;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.XlsxBooleanColumnFreeStyle;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.XlsxNumberColumn;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.XlsxStringColumn;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.SpreadsheetML.Styles;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTBorders;
import org.xlsx4j.sml.CTCellXfs;
import org.xlsx4j.sml.CTFills;
import org.xlsx4j.sml.CTFonts;
import org.xlsx4j.sml.CTMergeCell;
import org.xlsx4j.sml.CTNumFmts;
import org.xlsx4j.sml.CTStylesheet;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Col;
import org.xlsx4j.sml.Cols;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;

/**
 * Clase de utilería para los Excel (XLSX)<br>
 * <br>
 * Usar la siguiente página para obtener los estilos y formatos de las celdas.<br>
 * <a href="http://webapp.docx4java.org/OnlineDemo/demo_landing.html">Página</a> Revisar las partes:
 * <ul>
 * <li>/xl/styles.xml</li>
 * <li>/xl/worksheets/sheet1.xml</li>
 * </ul>
 * 
 * @author jchavez
 * @since 0.5.0
 */
public final class XlsxUtils
{
  private final int MAX_ROWS = 100;

  public static final long HEADER_STYLE = 1;
  public static final long STRING_EVENT = 2;
  public static final long STRING_ODD = 3;
  public static final long NUMBER_EVENT = 4;
  public static final long NUMBER_ODD = 5;
  public static final long PERCENTAGE_EVENT = 6;
  public static final long PERCENTAGE_ODD = 7;
  public static final long CURRENCY_EVENT = 8;
  public static final long CURRENCY_ODD = 9;
  public static final long DATE_EVENT = 10;
  public static final long DATE_ODD = 11;
  public static final long WINGDINGS_2_EVEN = 12;
  public static final long WINGDINGS_2_ODD = 13;
  public static final long HEADER_90_STYLE = 14;

  public static final long CURRENCY_FORMAT = 164;
  public static final long PERCENTAGE_FORMAT = 10;
  public static final long NUMBER_FORMAT = 3;
  public static final long DATE_FORMAT = 14;
  public static final long WINGDINGS_2_FORMAT = 15;
  public static final long VERDANA_9_FORMAT = 16;
  public static final long HEADER_VERDANA_STYLE = 15;

  /**
   * Método que crea el Excel apartir de una lista de objetos y una la lista de meta información con las propiedas que
   * se van a mostrar en el excel con el formato indicado.<br>
   * <br>
   * Los estilos de celda (conjunto de fuentes, fill, borders y formatos) que se definen son:
   * <ol>
   * <li value="1">Encabezado</li>
   * <li>Renglón (event) para cadenas</li>
   * <li>Renglón (odd) para cadenas</li>
   * <li>Renglón (event) para números (Formato: #,##0)</li>
   * <li>Renglón (odd) para números (Formato: #,##0)</li>
   * <li>Renglón (event) para porcentaje (Formato: 0.00%)</li>
   * <li>Renglón (odd) para porcentaje (Formato: 0.00%)</li>
   * <li>Renglón (event) para moneda (Formato: $#,##0.00)</li>
   * <li>Renglón (odd) para moneda (Formato: $#,##0.00)</li>
   * <li>Renglón (event) para fechas (Formato: dd/MM/yyyy)</li>
   * <li>Renglón (odd) para fechas (Formato: dd/MM/yyyy)</li>
   * </ol>
   * <b>Nota:</b> Todos los estilos definidos tienen borde<br>
   * <br>
   * Las fuentes definidas:
   * <ol>
   * <li value="1">Negritas (el tamaño y tipo de letra es el definido a nivel de workbook)</li>
   * </ol>
   * Los fondos definidos son:
   * <ol>
   * <li value="2">Fondo azul (tema 4)</li>
   * <li>Fondo gris</li>
   * </ol>
   * <b>Nota:</b> El fondo azul es para el encabezado y el fondo gris es para los renglones odd, no se define uno para
   * los renglones event porque se usa el fondo 0 (por defecto)<br>
   * <br>
   * Los bordes definidos son:
   * <ol>
   * <li value="1">Borde completo</li>
   * </ol>
   * 
   * @param objects Lista de objetos que contienen la información de cada renglón
   * @param metadata Lista de {@code XlsxColumn} que sirven para obtener la información, estilo y tipo de la celda.
   *          <b>Nota:</b> Es obligatorio que tenga la propiedad {@code fieldName} exista en los objetos de
   *          {@code objects}
   * @param worksheetName Nombre de la hoja
   * @return Arreglo de bytes del archivo Excel formado con la información proporcionada
   * @throws ServiceException
   * @author jchavez
   * @since 0.5.0
   */
  public byte[] createXlsxFromList( List<? extends Serializable> objects, List<AbstractXlsxColumn> metadata,
      String worksheetName )
  {
    byte[] bytes = null;
    try
    {
      SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
      WorksheetPart sheet = pkg.createWorksheetPart( new PartName( "/xl/worksheets/sheet1.xml" ), worksheetName, 1 );
      SheetData sheetData = sheet.getJaxbElement().getSheetData();
      Styles styles = new Styles( new PartName( "/xl/styles.xml" ) );
      CTStylesheet ctStylesheet = createStyles();
      styles.setJaxbElement( ctStylesheet );
      pkg.getWorkbookPart().addTargetPart( styles );

      createColumnsWidth( metadata, sheet );

      Row headerRow = createHeaderRow( metadata, ctStylesheet );
      sheetData.getRow().add( headerRow );
      List<Row> rows = createDinamicRows( objects, metadata );

      adjustCellWidth( sheet, rows );

      sheetData.getRow().addAll( rows );
      Save saver = new Save( pkg );
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      saver.save( outputStream );
      bytes = outputStream.toByteArray();
      outputStream.close();
    }
    catch( JAXBException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( Docx4JException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( IOException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    return bytes;
  }

  /**
   * Creates an excel report for an instance of
   * {@link mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyBookingReportTO}
   * 
   * @param report
   * @param worksheetName
   * @return
   */
  public byte[] createFileWeeklyBookingFromList( WorkSheetWeeklyBookingReportTO report, String worksheetName )
  {
    byte[] bytes = null;
    try
    {

      List<AbstractXlsxColumn> metadata = new ArrayList<AbstractXlsxColumn>();
      metadata.add( new XlsxStringColumn( "dsMovie", "Película" ) );
      metadata.add( new XlsxStringColumn( "dsDistributor", "Dist." ) );
      metadata.add( new XlsxNumberColumn( "nuWeek", "Sem." ) );
      metadata.add( new XlsxNumberColumn( "nuScreen", "Sala" ) );
      metadata.add( new XlsxNumberColumn( "nuCapacity", "Cap" ) );
      metadata.add( new XlsxStringColumn( "dsNote", "Nota" ) );

      List<String> header = Arrays.asList( report.getHeader().getTitle(), report.getHeader().getStrExhibitionWeek(),
        report.getHeader().getStrCurrentDate() );

      SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
      WorksheetPart sheet = pkg.createWorksheetPart( new PartName( "/xl/worksheets/DBS.xml" ), worksheetName, 1 );
      SheetData sheetData = sheet.getJaxbElement().getSheetData();
      Styles styles = new Styles( new PartName( "/xl/styles.xml" ) );
      CTStylesheet ctStylesheet = createStyles();
      styles.setJaxbElement( ctStylesheet );
      pkg.getWorkbookPart().addTargetPart( styles );

      createColumnsWidth( metadata, sheet );

      List<Row> headerRows = createHeaderRowFileWeeklyBooking( header );
      sheet.getJaxbElement().setMergeCells( Context.getsmlObjectFactory().createCTMergeCells() );
      sheet.getJaxbElement().getMergeCells().setCount( Long.valueOf( header.size() ) );
      for( int i = 1; i <= header.size(); i++ )
      {
        CTMergeCell mergecell = Context.getsmlObjectFactory().createCTMergeCell();
        mergecell.setRef( "A" + i + ":F" + i );
        sheet.getJaxbElement().getMergeCells().getMergeCell().add( mergecell );
      }
      sheetData.getRow().addAll( headerRows );

      int i = 0;
      for( WeeklyBookingReportTheaterTO reportTheater : report.getTheaters() )
      {
        boolean isLast = i + 1 == report.getTheaters().size();
        String theater = reportTheater.getDsTheater();
        List<? extends Serializable> objects = reportTheater.getMovies();

        sheetData.getRow().add( createTheaterHeaderRow( theater ) );

        sheetData.getRow().add( createHeaderRow( metadata, ctStylesheet ) );
        List<Row> rows = createDinamicRows( objects, metadata );
        adjustCellWidth( sheet, rows );
        sheetData.getRow().addAll( rows );

        sheetData.getRow().addAll( createTheaterFooterRows( isLast ) );
        i++;
      }

      Save saver = new Save( pkg );
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      saver.save( outputStream );
      bytes = outputStream.toByteArray();
      outputStream.close();
    }
    catch( JAXBException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( Docx4JException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( IOException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    return bytes;
  }

  /**
   * Creates an excel report for an instance of
   * {@link mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyBookingReportTO}
   * 
   * @param report
   * @param worksheetName
   * @return
   */
  public byte[] createFileWeeklyBookingPresaleFromList( WorkSheetWeeklyBookingReportTO report, String worksheetName )
  {
    byte[] bytes = null;
    try
    {
      List<AbstractXlsxColumn> metadata = new ArrayList<AbstractXlsxColumn>();
      metadata.add( new XlsxStringColumn( "dsCine", "Cine" ) );
      metadata.add( new XlsxStringColumn( "dsMovie", "Película" ) );
      metadata.add( new XlsxStringColumn( "dsDistributor", "Dist." ) );
      // metadata.add( new XlsxNumberColumn( "nuWeek", "Sem." ) );
      metadata.add( new XlsxNumberColumn( "nuScreen", "Sala" ) );
      metadata.add( new XlsxNumberColumn( "nuCapacity", "Cap" ) );
      metadata.add( new XlsxStringColumn( "dsNote", "Nota" ) );

      List<String> header = Arrays.asList( report.getHeader().getTitle(), report.getHeader().getStrExhibitionWeek(),
        report.getHeader().getStrCurrentDate() );
      SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
      WorksheetPart sheet = pkg.createWorksheetPart( new PartName( "/xl/worksheets/DBS.xml" ), worksheetName, 1 );
      SheetData sheetData = sheet.getJaxbElement().getSheetData();
      Styles styles = new Styles( new PartName( "/xl/styles.xml" ) );
      CTStylesheet ctStylesheet = createStyles();
      styles.setJaxbElement( ctStylesheet );
      pkg.getWorkbookPart().addTargetPart( styles );

      createColumnsWidth( metadata, sheet );

      List<Row> headerRows = createHeaderRowFileWeeklyBooking( header );
      sheet.getJaxbElement().setMergeCells( Context.getsmlObjectFactory().createCTMergeCells() );
      sheet.getJaxbElement().getMergeCells().setCount( Long.valueOf( header.size() ) );
      for( int i = 1; i <= header.size(); i++ )
      {
        CTMergeCell mergecell = Context.getsmlObjectFactory().createCTMergeCell();
        mergecell.setRef( "A" + i + ":F" + i );
        sheet.getJaxbElement().getMergeCells().getMergeCell().add( mergecell );
      }
      sheetData.getRow().addAll( headerRows );

      int i = 0;
      for( WeeklyBookingReportTheaterTO reportTheater : report.getTheaters() )
      {
        boolean isLast = i + 1 == report.getTheaters().size();
        List<? extends Serializable> objects = reportTheater.getMovies();

        sheetData.getRow().add( createHeaderRow( metadata, ctStylesheet ) );
        List<Row> rows = createDinamicRows( objects, metadata );
        adjustCellWidth( sheet, rows );
        sheetData.getRow().addAll( rows );

        sheetData.getRow().addAll( createTheaterFooterRows( isLast ) );
        i++;
      }

      Save saver = new Save( pkg );
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      saver.save( outputStream );
      bytes = outputStream.toByteArray();
      outputStream.close();

    }
    catch( JAXBException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( Docx4JException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( IOException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    return bytes;
  }

  private void createColumnsWidth( List<AbstractXlsxColumn> metadata, WorksheetPart sheet )
  {
    Cols cols = new Cols();
    int i = 1;
    for( AbstractXlsxColumn xlsxColumn : metadata )
    {

      double width = xlsxColumn.getHeader().length() + 1.0;

      if( xlsxColumn instanceof XlsxBooleanColumnFreeStyle )
      {
        width = 4.0;
      }

      Col col = new Col();
      col.setCustomWidth( true );
      col.setMin( i );
      col.setMax( i );
      col.setWidth( width );
      cols.getCol().add( col );
      i++;
    }
    sheet.getJaxbElement().getCols().add( cols );
  }

  private List<Row> createHeaderRowFileWeeklyBooking( List<String> header )
  {
    long[] styles = { 15L, 15L, 21L };
    int i = 0;
    List<Row> headerRows = new ArrayList<Row>();
    for( String head : header )
    {
      Row row = Context.getsmlObjectFactory().createRow();
      row.getSpans().add( "1:6" );
      Cell cell = Context.getsmlObjectFactory().createCell();
      // B, N, E, S, STR, INLINE_STR;
      cell.setT( STCellType.STR );
      cell.setV( head );
      cell.setS( styles[i] );
      row.getC().add( cell );

      headerRows.add( row );
      i++;
    }

    return headerRows;
  }

  private Row createTheaterHeaderRow( String theater )
  {
    Row row = Context.getsmlObjectFactory().createRow();
    Cell cell = Context.getsmlObjectFactory().createCell();
    // B, N, E, S, STR, INLINE_STR;
    cell.setT( STCellType.STR );
    cell.setV( theater );
    cell.setS( 16L );
    row.getC().add( cell );

    for( int i = 0; i < 4; i++ )
    {
      cell = Context.getsmlObjectFactory().createCell();
      cell.setT( STCellType.STR );
      cell.setS( 17L );
      row.getC().add( cell );
    }
    cell = Context.getsmlObjectFactory().createCell();
    cell.setT( STCellType.STR );
    cell.setS( 18L );
    row.getC().add( cell );
    return row;
  }

  /**
   * Método que crea el renglon con el encabezado de las columnas. Se usa el estilo 1 para las celdas. También se agrega
   * la lista de estilos a la lista de metadata.
   * 
   * @param metadata Lista de meta información necesaria para obtener el nombre de las columnas
   * @param ctStylesheet Estilos del workbook
   * @return Renglón del encabezado
   * @author jchavez
   * @since 0.5.0
   */
  private Row createHeaderRow( List<AbstractXlsxColumn> metadata, CTStylesheet ctStylesheet )
  {
    Row row = Context.getsmlObjectFactory().createRow();
    Cell cell = null;

    for( AbstractXlsxColumn xlsxColumn : metadata )
    {

      cell = Context.getsmlObjectFactory().createCell();

      cell.setT( STCellType.STR );
      cell.setV( xlsxColumn.getHeader() );

      cell.setS( 19L );

      row.getC().add( cell );
      xlsxColumn.setCtStylesheet( ctStylesheet );
    }
    return row;
  }

  /**
   * Método que crea la lista de renglones dinámicos
   * 
   * @param objects Lista con la información de un renglón.
   * @param metadata Lista con los convertidores por celda.
   * @return Lista de renglones
   * @author jchavez
   * @since 0.5.0
   */
  private List<Row> createDinamicRows( List<?> objects, List<AbstractXlsxColumn> metadata )
  {
    List<Row> rows = new ArrayList<Row>();
    if( CollectionUtils.isNotEmpty( objects ) )
    {
      Row row = null;
      Cell cell = null;
      for( Object object : objects )
      {
        row = Context.getsmlObjectFactory().createRow();
        for( AbstractXlsxColumn xlsxColumn : metadata )
        {
          cell = Context.getsmlObjectFactory().createCell();
          cell.setT( xlsxColumn.getCellType() );
          cell.setV( xlsxColumn.transformValue( object ) );
          cell.setS( 20L );
          row.getC().add( cell );
        }
        rows.add( row );
      }
    }
    return rows;
  }

  private List<Row> createTheaterFooterRows( boolean isLast )
  {
    List<Row> footer = new ArrayList<Row>();
    Row row = Context.getsmlObjectFactory().createRow();
    Cell cell = Context.getsmlObjectFactory().createCell();

    if( !isLast )
    {
      row = Context.getsmlObjectFactory().createRow();
      for( int i = 0; i < 6; i++ )
      {
        cell = Context.getsmlObjectFactory().createCell();
        cell.setT( STCellType.STR );
        cell.setS( 17L );
        row.getC().add( cell );

      }
      footer.add( row );
    }
    return footer;
  }

  private void adjustCellWidth( WorksheetPart sheet, List<Row> rows )
  {
    List<Col> cols = sheet.getJaxbElement().getCols().get( 0 ).getCol();
    int n = 0;
    for( Row row : rows )
    {
      if( n >= MAX_ROWS )
      {
        break;
      }
      int c = 0;
      for( Cell cell : row.getC() )
      {
        String s = cell.getV();
        if( StringUtils.isNotEmpty( s ) && cols.get( c ).getWidth() < s.length() )
        {
          cols.get( c ).setWidth( s.length() + 1.0 );
        }
        c++;
      }
      n++;
    }
  }

  /**
   * Método que crea los estilos iniciales del excel. Para los fuentes, bordes y conjunto de estilo para las celdas es
   * obligatorio tener un estilo por defecto y después agregar los demás. Para el estilo de fondo se deben de agregar
   * dos y luego definir fondos propios. Los indices de las listas de estilos (fuentes, fondos, formatos, bordes y
   * conjunto de estilos) empiezan en 0, al momento hacer referencia a los estilos es necesario considerar el indice
   * como posición.
   * 
   * @return Objeto con los estilos para el workbook.
   * @author jchavez
   * @since 0.5.0
   */
  private CTStylesheet createStyles()
  {
    CTStylesheet ss = Context.getsmlObjectFactory().createCTStylesheet();

    ss.setNumFmts( createNumericFonts() );
    ss.setFonts( createFonts() );
    ss.setFills( createFills() );
    ss.setBorders( createBorders() );
    ss.setCellXfs( createCellStyles() );

    return ss;
  }

  private CTCellXfs createCellStyles()
  {
    // Lista de estilos para las celdas
    CTCellXfs ctCellXfs = new CTCellXfs();
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyleDefault() );
    // Estilo de las celdas del encabezado
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 1L, 2L, 1L ) );
    // Estilo de las celdas String Even
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 0L, 0L, 1L ) );
    // Estilo de las celdas String Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 0L, 3L, 1L ) );
    // Estilo de las celdas Number Event
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( NUMBER_FORMAT, 0L, 0L, 1L ) );
    // Estilo de las celdas Number Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( NUMBER_FORMAT, 0L, 3L, 1L ) );
    // Estilo de las celdas Percentage Event
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( PERCENTAGE_FORMAT, 0L, 0L, 1L ) );
    // Estilo de las celdas Percentage Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( PERCENTAGE_FORMAT, 0L, 3L, 1L ) );
    // Estilo de las celdas Currency Event
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( CURRENCY_FORMAT, 0L, 0L, 1L ) );
    // Estilo de las celdas Currency Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( CURRENCY_FORMAT, 0L, 3L, 1L ) );
    // Estilo de las celdas Date Event
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( DATE_FORMAT, 0L, 0L, 1L ) );
    // Estilo de las celdas Date Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( DATE_FORMAT, 0L, 3L, 1L ) );

    // Estilo de las celdas Wingdings 2 Event
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( WINGDINGS_2_FORMAT, 2L, 0L, 1L ) );
    // Estilo de las celdas Wingdings 2 Odd
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( WINGDINGS_2_FORMAT, 2L, 3L, 1L ) );

    // Estilo de las celdas del encabezado rotado 90grados
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 1L, 2L, 1L, 90L ) );
    // Estilo de celdas Verdana 9
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyleAndCenter( 0L, 3L, 6L, 0L ) );

    // Estilo de celdas Verdana 9 Bold, Border LeftTopBottom -> 16
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 4L, 6L, 2L ) );
    // Estilo de celdas Verdana 9 Bold, Border TopBottom -> 17
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 4L, 6L, 3L ) );
    // Estilo de celdas Verdana 9 Bold, Border TopBottom -> 18
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 4L, 6L, 4L ) );

    // Estilo de las celdas del encabezado -> 19
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0l, 5L, 4L, 1L ) );
    // Estilo de las celdas Arial 8 / light gray -> 20
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 6L, 5L, 1L ) );
    // Estilo de celdas Verdana 8 -> 21
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyleAndCenter( 0L, 7L, 6L, 0L ) );

    // Estilo de celdas Arial 8 Bold-> 22
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 9L, 5L, 1L ) );
    // Estilo de celdas Arial 14 Bold-> 23
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyleAndCenter( 0L, 8L, 6L, 0L ) );
    // Estilo de celdas Verdana 9 alineadoa la izq -> 24
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 3L, 6L, 0L ) );

    // Estilo de las celdas Arial 8 / gray -> 25
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 6L, 7L, 1L ) );
    // Estilo de celdas Arial 8 Bold / gray-> 26
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyle( 0L, 9L, 7L, 1L ) );
    return ctCellXfs;
  }

  private CTNumFmts createNumericFonts()
  {
    CTNumFmts ctNumFmts = new CTNumFmts();
    ctNumFmts.getNumFmt().add( XlsxStyleFactory.createFormatCurrency( CURRENCY_FORMAT ) );
    return ctNumFmts;
  }

  private CTBorders createBorders()
  {
    // Valores obligatorios de border
    CTBorders borders = new CTBorders();
    borders.getBorder().add( XlsxStyleFactory.createBorderDefault() );
    borders.getBorder().add( XlsxStyleFactory.createBorderAll() );
    borders.getBorder().add( XlsxStyleFactory.createBorderLeftTopBottom() );
    borders.getBorder().add( XlsxStyleFactory.createBorderTopBottom() );
    borders.getBorder().add( XlsxStyleFactory.createBorderRightTopBottom() );
    return borders;
  }

  private CTFonts createFonts()
  {
    // Se crean las fuentes para el Excel
    CTFonts fonts = new CTFonts();
    // Calibri 11 default --> 0
    fonts.getFont().add( XlsxStyleFactory.createFontDefault() );
    // Fuente con negritas, las demás propiedades se dejan por DEFAULT (color, tipo y tamaño) --> 1
    fonts.getFont().add( XlsxStyleFactory.createFontBold() );
    // Fuente Wingding 2, para --> 2
    fonts.getFont().add( XlsxStyleFactory.createFontWingdings2() );
    // Fuente Verdana 9 para --> 3
    fonts.getFont().add( XlsxStyleFactory.createFontVerdanaSize9() );
    // Fuente Verdana 9 bold --> 4
    fonts.getFont().add( XlsxStyleFactory.createFontVerdanaSize9Bold() );
    // Fuente Verdana 9 bold --> 5
    fonts.getFont().add( XlsxStyleFactory.createFontVerdanaSize8BoldWhite() );
    // Fuente Arial 8 --> 6
    fonts.getFont().add( XlsxStyleFactory.createFontArialSize8() );
    // Fuente Verdana 8 --> 7
    fonts.getFont().add( XlsxStyleFactory.createFontVerdanaSize8() );
    // Fuente Arial 14 Bold --> 8
    fonts.getFont().add( XlsxStyleFactory.createFontArialSize14Bold() );
    // Fuente Arial 8 Bold --> 9
    fonts.getFont().add( XlsxStyleFactory.createFontArialSize8Bold() );
    return fonts;
  }

  private CTFills createFills()
  {
    // Creación de los Fill de celdas
    CTFills fills = new CTFills();
    // Fill ->0
    fills.getFill().add( XlsxStyleFactory.createFillDefault() );
    // Fill ->1
    fills.getFill().add( XlsxStyleFactory.createFillGrayDefault() );
    // Fill ->2
    fills.getFill().add( XlsxStyleFactory.createFillHeader() );
    // Fill ->3
    fills.getFill().add( XlsxStyleFactory.createFillOdd() );
    // Fill dark gray->4
    fills.getFill().add( XlsxStyleFactory.createFillDarkGray() );
    // Fill light gray ->5
    fills.getFill().add( XlsxStyleFactory.createFillLightGray() );
    // Fill white ->6
    fills.getFill().add( XlsxStyleFactory.createFillWhite() );
    // Fill gray ->7
    fills.getFill().add( XlsxStyleFactory.createFillGray() );
    return fills;
  }

  /**
   * Creates an excel report for an instance of
   * {@link mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyDistributorReportTO}
   * 
   * @param report
   * @param worksheetName
   * @return
   */
  public byte[] createFileWeeklyDistributorFromList( WorkSheetWeeklyDistributorReportTO report, String worksheetName )
  {
    byte[] bytes = null;
    try
    {

      List<AbstractXlsxColumn> metadata = new ArrayList<AbstractXlsxColumn>();
      metadata.add( new XlsxStringColumn( "dsRegion", "Zona" ) );
      metadata.add( new XlsxStringColumn( "dsTheater", "Cine" ) );
      metadata.add( new XlsxStringColumn( "dsCity", "Ciudad" ) );
      metadata.add( new XlsxStringColumn( "dsMovie", "Película" ) );
      metadata.add( new XlsxStringColumn( "dsDistributor", "Distribuidor" ) );
      metadata.add( new XlsxNumberColumn( "nuWeek", "Semana" ) );
      metadata.add( new XlsxNumberColumn( "nuScreen", "Sala" ) );
      metadata.add( new XlsxStringColumn( "dsStatus", "Status" ) );

      List<String> header = Arrays.asList( report.getHeader().getTitle(), report.getHeader().getStrDistributor(),
        report.getHeader().getStrExhibitionWeek() );

      SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
      WorksheetPart sheet = pkg.createWorksheetPart( new PartName( "/xl/worksheets/sheet1.xml" ), worksheetName, 1 );
      SheetData sheetData = sheet.getJaxbElement().getSheetData();
      Styles styles = new Styles( new PartName( "/xl/styles.xml" ) );
      CTStylesheet ctStylesheet = createStyles();
      styles.setJaxbElement( ctStylesheet );
      pkg.getWorkbookPart().addTargetPart( styles );

      createColumnsWidth( metadata, sheet );

      List<Row> headerRows = createHeaderRowFileWeeklyDistributor( header );
      sheet.getJaxbElement().setMergeCells( Context.getsmlObjectFactory().createCTMergeCells() );
      sheet.getJaxbElement().getMergeCells().setCount( Long.valueOf( header.size() ) );
      for( int i = 1; i <= header.size(); i++ )
      {
        CTMergeCell mergecell = Context.getsmlObjectFactory().createCTMergeCell();
        mergecell.setRef( "A" + i + ":H" + i );
        sheet.getJaxbElement().getMergeCells().getMergeCell().add( mergecell );
      }
      sheetData.getRow().addAll( headerRows );
      sheetData.getRow().add( createHeaderRow( metadata, ctStylesheet ) );
      int i = 0;
      long styleBooked;
      long styleContinues;
      for( WeeklyBookingReportTheaterTO reportTheater : report.getTheaters() )
      {
        if( i % 2 == 0 )
        {
          styleContinues = 20L;
          styleBooked = 22L;
        }
        else
        {
          styleContinues = 25L;
          styleBooked = 26L;
        }

        List<WeeklyBookingReportMovieTO> movies = reportTheater.getMovies();
        if( CollectionUtils.isNotEmpty( movies ) )
        {
          for( WeeklyBookingReportMovieTO to : movies )
          {
            to.setDsTheater( reportTheater.getDsTheater() );
            to.setDsCity( reportTheater.getDsCity() );
            to.setDsRegion( reportTheater.getDsRegion() );
          }

          List<Row> rows = createDinamicRowsWithStyle( movies, metadata, styleBooked, styleContinues );
          adjustCellWidth( sheet, rows );
          sheetData.getRow().addAll( rows );

          i++;
        }
      }

      Save saver = new Save( pkg );
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      saver.save( outputStream );
      bytes = outputStream.toByteArray();
      outputStream.close();
    }
    catch( JAXBException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( Docx4JException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    catch( IOException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CREATE_XLSX_ERROR.getId(), e );
    }
    return bytes;
  }

  private List<Row> createHeaderRowFileWeeklyDistributor( List<String> header )
  {
    long[] styles = { 23L, 24L, 24L };
    int i = 0;
    List<Row> headerRows = new ArrayList<Row>();
    for( String head : header )
    {
      Row row = Context.getsmlObjectFactory().createRow();
      row.getSpans().add( "1:8" );
      Cell cell = Context.getsmlObjectFactory().createCell();
      // B, N, E, S, STR, INLINE_STR;
      cell.setT( STCellType.STR );
      cell.setV( head );
      cell.setS( styles[i] );
      row.getC().add( cell );

      headerRows.add( row );
      i++;
    }

    return headerRows;
  }

  private List<Row> createDinamicRowsWithStyle( List<WeeklyBookingReportMovieTO> objects,
      List<AbstractXlsxColumn> metadata, long styleBooked, long styleContinues )
  {
    List<Row> rows = new ArrayList<Row>();
    if( CollectionUtils.isNotEmpty( objects ) )
    {
      Row row = null;
      Cell cell = null;
      for( WeeklyBookingReportMovieTO object : objects )
      {
        row = Context.getsmlObjectFactory().createRow();
        for( AbstractXlsxColumn xlsxColumn : metadata )
        {
          cell = Context.getsmlObjectFactory().createCell();
          cell.setT( xlsxColumn.getCellType() );
          cell.setV( xlsxColumn.transformValue( object ) );
          if( object.getBookingStatus().equals( BookingStatus.BOOKED ) && !object.isToBeContinue()
              && xlsxColumn.getFieldName().equals( "dsStatus" ) )
          {
            cell.setS( styleBooked );
          }
          else
          {
            cell.setS( styleContinues );
          }
          row.getC().add( cell );
        }
        rows.add( row );
      }
    }
    return rows;
  }
}