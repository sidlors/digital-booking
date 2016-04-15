package mx.com.cinepolis.digital.booking.service.util.docx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;

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
import org.xlsx4j.sml.CTNumFmts;
import org.xlsx4j.sml.CTStylesheet;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.STCellType;
import org.xlsx4j.sml.SheetData;

/**
 * Clase dummy de ejemplo
 * 
 * @author gsegura
 */
public class XlsUtilsExample
{

  private static final Long NUMERIC_FONT_DEFAULT = 0L;
  private static final Long FONT_DEFAULT = 0L;
  private static final Long FILL_DEFAULT = 0L;
  private static final Long FILL_ORANGE_ACCENT2 = 2L;
  private static final Long BORDER_DEFAULT = 0L;
  private static final Long BORDER_ALL = 1L;
  private static final Long BORDER_ALL_MEDIUM_TOP_LEFT = 2L;
  private static final Long BORDER_ALL_MEDIUM_TOP = 3L;
  private static final Long BORDER_ALL_MEDIUM_TOP_RIGHT = 4L;
  private static final Long BORDER_ALL_MEDIUM_LEFT = 5L;
  private static final Long BORDER_ALL_MEDIUM_RIGHT = 6L;
  private static final Long BORDER_ALL_MEDIUM_BOTTOM_LEFT = 7L;
  private static final Long BORDER_ALL_MEDIUM_BOTTOM = 8L;
  private static final Long BORDER_ALL_MEDIUM_BOTTOM_RIGHT = 9L;

  private static final Long STYLE_DEFAULT = 0L;
  private static final Long STYLE_BORDER_ALL = 9L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP_LEFT = 1L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP = 2L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP_RIGHT = 3L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_LEFT = 4L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_RIGHT = 5L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_BOTTOM_LEFT = 6L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_BOTTOM = 7L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_BOTTOM_RIGHT = 8L;

  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP_LEFT_FILL_ORANGE = 10L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP_FILL_ORANGE = 11L;
  private static final Long STYLE_BORDER_ALL_MEDIUM_TOP_RIGHT_FILL_ORANGE = 12L;

  public byte[] generateReport( List<BookingTO> bookings )
  {

    byte[] bytes = null;
    try
    {
      SpreadsheetMLPackage pkg = SpreadsheetMLPackage.createPackage();
      String worksheetName = "sheet1";
      WorksheetPart sheet = pkg.createWorksheetPart( new PartName( "/xl/worksheets/sheet1.xml" ), worksheetName, 1 );
      SheetData sheetData = sheet.getJaxbElement().getSheetData();

      // Agregar estilos
      Styles styles = new Styles( new PartName( "/xl/styles.xml" ) );
      CTStylesheet ctStylesheet = createStyles();
      styles.setJaxbElement( ctStylesheet );
      pkg.getWorkbookPart().addTargetPart( styles );

      Row row = extractHeader();
      sheetData.getRow().add( row );

      List<Row> rows = extractDyamicRows( bookings );
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

  private Row extractHeader()
  {
    Row row = null;
    Cell cell = null;
    row = Context.getsmlObjectFactory().createRow();

    // Cine
    cell = Context.getsmlObjectFactory().createCell();
    cell.setT( STCellType.STR );
    cell.setV( "Cine" );
    cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_LEFT_FILL_ORANGE );
    row.getC().add( cell );

    // Pelicula
    cell = Context.getsmlObjectFactory().createCell();
    cell.setT( STCellType.STR );
    cell.setV( "Película" );
    cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_FILL_ORANGE );
    row.getC().add( cell );

    // Clasificacion
    cell = Context.getsmlObjectFactory().createCell();
    cell.setT( STCellType.STR );
    cell.setV( "Clasificación" );
    cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_FILL_ORANGE );
    row.getC().add( cell );

    // Clasificacion
    cell = Context.getsmlObjectFactory().createCell();
    cell.setT( STCellType.STR );
    cell.setV( "Fecha" );
    cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_RIGHT_FILL_ORANGE );
    row.getC().add( cell );
    return row;
  }

  private List<Row> extractDyamicRows( List<BookingTO> bookings )
  {
    List<Row> rows = new ArrayList<Row>();
    Row row = null;
    Cell cell = null;
    int i = 0;
    for( BookingTO booking : bookings )
    {
      row = Context.getsmlObjectFactory().createRow();

      // Cine
      cell = Context.getsmlObjectFactory().createCell();
      cell.setT( STCellType.STR );
      cell.setV( booking.getTheater().getName() );
      if( i == 0 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_LEFT );
      }
      else if( i == bookings.size() - 1 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_BOTTOM_LEFT );
      }
      else
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_LEFT );
      }

      row.getC().add( cell );

      // Pelicula
      cell = Context.getsmlObjectFactory().createCell();
      cell.setT( STCellType.STR );
      cell.setV( booking.getEvent().getDsEventName() );
      if( i == 0 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP );
      }
      else if( i == bookings.size() - 1 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_BOTTOM );
      }
      else
      {
        cell.setS( STYLE_BORDER_ALL );
      }

      row.getC().add( cell );

      // Clasificacion
      cell = Context.getsmlObjectFactory().createCell();
      cell.setT( STCellType.STR );
      cell.setV( ((EventMovieTO) booking.getEvent()).getDsRating() );
      if( i == 0 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP );
      }
      else if( i == bookings.size() - 1 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_BOTTOM );
      }
      else
      {
        cell.setS( STYLE_BORDER_ALL );
      }

      row.getC().add( cell );

      // Fecha
      DateFormat df = new SimpleDateFormat( "dd-MM-yyyy" );
      cell = Context.getsmlObjectFactory().createCell();
      cell.setT( STCellType.STR );
      cell.setV( df.format( new Date() ) );
      if( i == 0 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_TOP_RIGHT );
      }
      else if( i == bookings.size() - 1 )
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_BOTTOM_RIGHT );
      }
      else
      {
        cell.setS( STYLE_BORDER_ALL_MEDIUM_RIGHT );
      }

      row.getC().add( cell );

      rows.add( row );
      i++;
    }
    return rows;
  }

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
    // 0
    ctCellXfs.getXf().add( XlsxStyleFactory.createFinalStyleDefault() );

    // Estilo 1
    ctCellXfs.getXf()
        .add(
          (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT,
            BORDER_ALL_MEDIUM_TOP_LEFT )) );
    // Estilo 2
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT, BORDER_ALL_MEDIUM_TOP )) );
    // Estilo 3
    ctCellXfs.getXf().add(
      (XlsxStyleFactory
          .createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT, BORDER_ALL_MEDIUM_TOP_RIGHT )) );
    // Estilo 4
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT, BORDER_ALL_MEDIUM_LEFT )) );
    // Estilo 5
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT, BORDER_ALL_MEDIUM_RIGHT )) );
    // Estilo 6
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT,
        BORDER_ALL_MEDIUM_BOTTOM_LEFT )) );
    // Estilo 7
    ctCellXfs.getXf()
        .add(
          (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT,
            BORDER_ALL_MEDIUM_BOTTOM )) );
    // Estilo 8
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT,
        BORDER_ALL_MEDIUM_BOTTOM_RIGHT )) );
    // Estilo 9
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_DEFAULT, BORDER_ALL )) );

    // Estilo 10
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_ORANGE_ACCENT2,
        BORDER_ALL_MEDIUM_TOP )) );
    // Estilo 11
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_ORANGE_ACCENT2,
        BORDER_ALL_MEDIUM_TOP_RIGHT )) );
    // Estilo 12
    ctCellXfs.getXf().add(
      (XlsxStyleFactory.createFinalStyle( NUMERIC_FONT_DEFAULT, FONT_DEFAULT, FILL_ORANGE_ACCENT2,
        BORDER_ALL_MEDIUM_LEFT )) );

    return ctCellXfs;
  }

  private CTBorders createBorders()
  {
    // Valores obligatorios de border
    CTBorders borders = new CTBorders();
    // 0
    borders.getBorder().add( XlsxStyleFactory.createBorderDefault() );
    // 1
    borders.getBorder().add( XlsxStyleFactory.createBorderAll() );
    // 2
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumTopLeft() );
    // 3
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumTop() );
    // 4
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumTopRight() );
    // 5
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumLeft() );
    // 6
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumRight() );
    // 7
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumBottomLeft() );
    // 8
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumBottom() );
    // 9
    borders.getBorder().add( XlsxStyleFactory.createBorderAllMediumBottomRight() );

    return borders;
  }

  private CTFills createFills()
  {
    // Creación de los Fill de celdas
    CTFills fills = new CTFills();
    // Fill ->0
    fills.getFill().add( XlsxStyleFactory.createFillDefault() );
    // Fill ->1
    fills.getFill().add( XlsxStyleFactory.createFillGrayDefault() );
    // Fill -> 2
    fills.getFill().add( XlsxStyleFactory.createFillRGB( 244, 176, 132 ) );
    return fills;
  }

  private CTFonts createFonts()
  {
    CTFonts fonts = new CTFonts();
    // Calibri 11 default --> 0
    fonts.getFont().add( XlsxStyleFactory.createFontDefault() );
    return fonts;
  }

  private CTNumFmts createNumericFonts()
  {
    CTNumFmts ctNumFmts = new CTNumFmts();
    ctNumFmts.getNumFmt().add( XlsxStyleFactory.createFormatCurrency( 164L ) );
    return ctNumFmts;
  }

}
