package mx.com.cinepolis.digital.booking.service.util.docx.converters;

import java.util.Calendar;
import java.util.Date;

import mx.com.cinepolis.digital.booking.service.util.docx.XlsxUtils;

import org.apache.commons.lang.BooleanUtils;

/**
 * Clase que maneja las variables de tipo {@code Date}. Proporciona funcionalidades básicas como transformar el valor de
 * la celda, darle formato y obtener el tipo de celda
 * 
 * @author jchavez
 * @since 0.5.0
 */
public class XlsxDateColumn extends AbstractXlsxColumn
{
  // leap days in previous 1900 years
  private static final int PREVIOUS_LEAP_YEARS = 460;
  // plus years divisible by 400
  private static final int DIVISIBLE_400_YEARS = 400;
  // minus prior century years
  private static final int PRIOR_CENTURY_YEARS = 100;
  // plus julian leap days in prior years
  private static final int JULIAN_LEAP_DAYS = 4;
  private static final int WINDOWING_1904 = 1904;
  private static final int WINDOWING_1900 = 1900;
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int MINUTES_PER_HOUR = 60;
  private static final int HOURS_PER_DAY = 24;
  private static final int MILLISECOND_PER_SECOND = 1000;
  // used to specify that date is invalid
  private static final int BAD_DATE = -1;
  private static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
  private static final long DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;
  private static final int DAYS_PER_YEARS = 365;

  /**
   * Constructor por defecto
   */
  public XlsxDateColumn()
  {
    super();
  }

  /**
   * Constructor que inicializa el objeto con el nombre de la propiedad y el encabezado
   * 
   * @param fieldName Nombre de la propiedad. Se usa para en método {@link #transformValue}
   * @param header Nombre del encabezado de la columna
   */
  public XlsxDateColumn( String fieldName, String header )
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
    if( fieldValue instanceof Calendar )
    {
      Calendar calendar = (Calendar) fieldValue;
      double time = internalGetExcelDate( calendar, false );
      result = String.valueOf( time );
    }
    else if( fieldValue instanceof Date )
    {
      Date date = (Date) fieldValue;
      Calendar calendar = Calendar.getInstance();
      calendar.setTime( date );
      double time = internalGetExcelDate( calendar, false );
      result = String.valueOf( time );
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long getCellStyle( Boolean isOdd )
  {
    return BooleanUtils.isTrue( isOdd ) ? XlsxUtils.DATE_ODD : XlsxUtils.DATE_EVENT;
  }

  /** Date Excel Format org.apache.poi.hssf.usermodel.HSSFCell */

  private double internalGetExcelDate( Calendar date, boolean use1904windowing )
  {
    if( (!use1904windowing && date.get( Calendar.YEAR ) < WINDOWING_1900)
        || (use1904windowing && date.get( Calendar.YEAR ) < WINDOWING_1904) )
    {
      return BAD_DATE;
    }

    // Because of daylight time saving we cannot use
    // date.getTime() - calStart.getTimeInMillis()
    // as the difference in milliseconds between 00:00 and 04:00
    // can be 3, 4 or 5 hours but Excel expects it to always
    // be 4 hours.
    // E.g. 2004-03-28 04:00 CEST - 2004-03-28 00:00 CET is 3 hours
    // and 2004-10-31 04:00 CET - 2004-10-31 00:00 CEST is 5 hours
    double fraction = (((date.get( Calendar.HOUR_OF_DAY ) * MINUTES_PER_HOUR + date.get( Calendar.MINUTE ))
        * SECONDS_PER_MINUTE + date.get( Calendar.SECOND ))
        * MILLISECOND_PER_SECOND + date.get( Calendar.MILLISECOND ))
        / (double) DAY_MILLISECONDS;

    Calendar calStart = dayStart( date );
    double value = fraction + absoluteDay( calStart, use1904windowing );
    if( !use1904windowing && value >= MINUTES_PER_HOUR )
    {
      value++;
    }
    else if( use1904windowing )
    {
      value--;
    }
    return value;
  }

  private Calendar dayStart( final Calendar cal )
  {
    // force recalculation of internal fields
    cal.get( Calendar.HOUR_OF_DAY );
    cal.set( Calendar.HOUR_OF_DAY, 0 );
    cal.set( Calendar.MINUTE, 0 );
    cal.set( Calendar.SECOND, 0 );
    cal.set( Calendar.MILLISECOND, 0 );
    // force recalculation of internal fields
    cal.get( Calendar.HOUR_OF_DAY );
    return cal;
  }

  private int absoluteDay( Calendar cal, boolean use1904windowing )
  {
    return cal.get( Calendar.DAY_OF_YEAR ) + daysInPriorYears( cal.get( Calendar.YEAR ), use1904windowing );
  }

  private int daysInPriorYears( int yr, boolean use1904windowing )
  {
    if( (!use1904windowing && yr < WINDOWING_1900) || (use1904windowing && yr < WINDOWING_1900) )
    {
      throw new IllegalArgumentException( "'year' must be 1900 or greater" );
    }

    int yr1 = yr - 1;
    int leapDays = yr1 / JULIAN_LEAP_DAYS - yr1 / PRIOR_CENTURY_YEARS + yr1 / DIVISIBLE_400_YEARS - PREVIOUS_LEAP_YEARS;

    return DAYS_PER_YEARS * (yr - (use1904windowing ? WINDOWING_1904 : WINDOWING_1900)) + leapDays;
  }
}
