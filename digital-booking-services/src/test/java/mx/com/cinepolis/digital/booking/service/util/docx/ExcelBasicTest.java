package mx.com.cinepolis.digital.booking.service.util.docx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.junit.Test;
import org.xlsx4j.exceptions.Xlsx4jException;
import org.xlsx4j.sml.CTCellFormula;
import org.xlsx4j.sml.Cell;
import org.xlsx4j.sml.Row;
import org.xlsx4j.sml.SheetData;

public class ExcelBasicTest
{

  @Test
  public void test() throws Docx4JException, Xlsx4jException, IOException
  {

    File file = new File( "c:/temp/ejemplo.xlsx" );
    SpreadsheetMLPackage pkg = SpreadsheetMLPackage.load( file );
    WorksheetPart sheet = pkg.getWorkbookPart().getWorksheet( 0 );
    SheetData sheetData = sheet.getJaxbElement().getSheetData();

    Cell cell;
    char[] characters = new char[26];
    characters[0] = 'A';
    for( int i = 1; i < characters.length; i++ )
    {
      char c = characters[i - 1];
      byte b = (byte) (((byte) c) + (byte) 1);
      c = (char) b;
      characters[i] = c;
    }

    for( int rowId = 4; rowId <= 10; rowId++ )
    {
      // String vacio
      Row row = new Row();
      row.setR( Long.valueOf( rowId ) );
      row.getSpans().add( "2:5" );

      // Valores
      int sumValue = 0;
      StringBuilder formulaS = new StringBuilder();
      for( int colId = 2; colId <= 4; colId++ )
      {
        cell = new Cell();
        String cellId = getCellId( rowId, colId );
        formulaS.append( cellId );
        if( colId < 4 )
        {
          formulaS.append( "+" );
        }
        cell.setR( cellId );
        // Style 2
        cell.setS( 2L );
        int cellValue = getData();
        sumValue += cellValue;
        cell.setV( String.valueOf( cellValue ) );
        row.getC().add( cell );
      }

      cell = new Cell();
      cell.setR( getCellId( rowId, 5 ) );
      // Style 3
      cell.setS( 3L );
      CTCellFormula formula = new CTCellFormula();
      formula.setValue( formulaS.toString() );
      cell.setF( formula );
      cell.setV( String.valueOf( sumValue ) );
      row.getC().add( cell );
      sheetData.getRow().add( row );

    }

    Save saver = new Save( pkg );
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    saver.save( outputStream );
    byte[] bytes = outputStream.toByteArray();
    outputStream.close();
    
    FileOutputStream fileOutputStream = new FileOutputStream( "c:/temp/editado.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();

  }

  private int getData()
  {
    double n = Math.random() * 100 + 1;
    int c = (int) n;
    return c;
  }

  private String getCellId( int row, int column )
  {
    String s = "";

    char[] characters = new char[26];
    characters[0] = 'A';
    for( int i = 1; i < characters.length; i++ )
    {
      char c = characters[i - 1];
      byte b = (byte) (((byte) c) + (byte) 1);
      c = (char) b;
      characters[i] = c;
    }

    int residue = (column-1) % 26;
    int div = (column-1) / 26;
    if( div > 0 )
    {
      s += characters[div] + characters[residue];
    }
    else
    {
      s += characters[residue];
    }
    s += row;

    return s;
  }

}
