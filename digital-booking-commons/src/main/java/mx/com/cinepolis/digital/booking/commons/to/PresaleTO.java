package mx.com.cinepolis.digital.booking.commons.to;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Tranfer Object for presales
 * 
 * @author jcarbajal
 */
public class PresaleTO extends AbstractTO
{

  private static final long serialVersionUID = -6261791192620645936L;
  private static final String A_TO_DATE = " to ";
  private static final String DE_TO_DATE = " of ";
  private Long idPresale;
  private Date dtStartDayPresale;
  private Date dtFinalDayPresale;
  private Date dtReleaseDay;
  private String strPresaleDates;
  private String strReleaseDate;
  private Long idBookingSpecialEventScreen;
  private Long idBookingWeekScreen;

  public PresaleTO()
  {
    super();
  }

  public PresaleTO( Long idPresale, boolean fgActive )
  {
    this.idPresale = idPresale;
    this.setFgActive( fgActive );
  }

  public PresaleTO( Long idPresale, boolean fgActive, Date dtStartDayPresale, Date dtFinalDayPresale, Date dtReleaseDay )
  {
    this.idPresale = idPresale;
    this.dtStartDayPresale = dtStartDayPresale;
    this.dtFinalDayPresale = dtFinalDayPresale;
    this.dtReleaseDay = dtReleaseDay;
    this.setFgActive( fgActive );
  }

  public Long getIdPresale()
  {
    return idPresale;
  }

  public void setIdPresale( Long idPresale )
  {
    this.idPresale = idPresale;
  }

  public Date getDtStartDayPresale()
  {
    return CinepolisUtils.enhancedClone( dtStartDayPresale );
  }

  public void setDtStartDayPresale( Date dtStartDayPresale )
  {
    this.dtStartDayPresale = dtStartDayPresale;
  }

  public Date getDtFinalDayPresale()
  {
    return CinepolisUtils.enhancedClone( dtFinalDayPresale );
  }

  public void setDtFinalDayPresale( Date dtFinalDayPresale )
  {
    this.dtFinalDayPresale = dtFinalDayPresale;
  }

  public Date getDtReleaseDay()
  {
    return CinepolisUtils.enhancedClone( dtReleaseDay );
  }

  public void setDtReleaseDay( Date dtReleaseDay )
  {
    this.dtReleaseDay = dtReleaseDay;
  }

  /**
   * @return the strPresaleDates
   */
  public String getStrPresaleDates()
  {
    return strPresaleDates;
  }

  /**
   * TODO por cambiar el locale al lenguaje apropiado
   * 
   * @param strPresaleDates the strPresaleDates to set
   */
  public void setStrPresaleDates( String strPresaleDates )
  {
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "en", "US" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "en", "US" ) );
    StringBuilder sb = new StringBuilder();
    if( this.validatePresaleDates() )
    {
      if( DateUtils.isSameDay( this.dtStartDayPresale, this.dtFinalDayPresale ) )
      {
        sb.append( format.format( this.dtStartDayPresale ) ).append( DE_TO_DATE )
            .append( format2.format( this.dtStartDayPresale ) ).append( ". " );
        this.strPresaleDates = sb.toString();
      }
      else
      {
        sb.append( format.format( this.dtStartDayPresale ) ).append( DE_TO_DATE )
            .append( format2.format( this.dtStartDayPresale ) ).append( A_TO_DATE )
            .append( format.format( this.dtFinalDayPresale ) ).append( DE_TO_DATE )
            .append( format2.format( this.dtFinalDayPresale ) ).append( ". " );
        this.strPresaleDates = sb.toString();
      }
    }
    else
    {
      this.strPresaleDates = strPresaleDates;
    }
  }

  /**
   * @return the strReleaseDate
   */
  public String getStrReleaseDate()
  {
    return strReleaseDate;
  }

  /**
   * TODO por cambiar el locale al lenguaje apropiado
   * 
   * @param strReleaseDate the strReleaseDate to set
   */
  public void setStrReleaseDate( String strReleaseDate )
  {
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "en", "US" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "en", "US" ) );
    StringBuilder sb = new StringBuilder();
    if( this.dtReleaseDay != null )
    {
      sb.append( format.format( this.dtReleaseDay ) ).append( DE_TO_DATE ).append( format2.format( this.dtReleaseDay ) )
          .append( ". " );
      this.strReleaseDate = sb.toString();
    }
    else
    {
      this.strReleaseDate = strReleaseDate;
    }
  }

  public Long getIdBookingSpecialEventScreen()
  {
    return idBookingSpecialEventScreen;
  }

  public void setIdBookingSpecialEventScreen( Long idBookingSpecialEventScreen )
  {
    this.idBookingSpecialEventScreen = idBookingSpecialEventScreen;
  }

  public Long getIdBookingWeekScreen()
  {
    return idBookingWeekScreen;
  }

  public void setIdBookingWeekScreen( Long idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
  }

  /**
   * Method that validates whether start and final dates are not null.
   * 
   * @return isValid
   */
  private boolean validatePresaleDates()
  {
    return (this.dtStartDayPresale != null && this.dtFinalDayPresale != null);
  }

}
