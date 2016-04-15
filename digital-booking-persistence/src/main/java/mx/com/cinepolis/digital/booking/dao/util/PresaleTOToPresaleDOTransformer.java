package mx.com.cinepolis.digital.booking.dao.util;

import org.apache.commons.collections.Transformer;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;

public class PresaleTOToPresaleDOTransformer implements Transformer
{

  private Language language;
  private Integer idWeek;

  /**
   * Constructor default
   */
  public PresaleTOToPresaleDOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor by language
   * 
   * @param language
   */
  public PresaleTOToPresaleDOTransformer( Language language )
  {
    this.language = language;
  }

  @Override
  public Object transform( Object obj )
  {
    PresaleDO presaleDO = null;
    if( obj instanceof PresaleTO )
    {
      PresaleTO presaleTO = (PresaleTO) obj;
      presaleDO = new PresaleDO();
      presaleDO.setDtFinalDayPresale( presaleTO.getDtFinalDayPresale() );
      presaleDO.setDtStartDayPresale( presaleTO.getDtStartDayPresale() );
      presaleDO.setDtReleaseDay( presaleTO.getDtReleaseDay() );
      presaleDO.setDtLastModification( presaleTO.getTimestamp() );
      presaleDO.setFgActive( presaleTO.isFgActive() );
      presaleDO.setIdLastUserModifier( presaleTO.getUserId().intValue() );
      presaleDO.setIdPresale( presaleTO.getIdPresale() );
      if( presaleTO.getIdBookingSpecialEventScreen() != null )
      {
        presaleDO.setIdBookingSpecialEventScreen( new BookingSpecialEventScreenDO( presaleTO
            .getIdBookingSpecialEventScreen() ) );
      }
      else if( presaleTO.getIdBookingWeekScreen() != null )
      {
        presaleDO.setIdBookingWeekScreen( new BookingWeekScreenDO(presaleTO.getIdBookingWeekScreen()) );
      }
    }
    return presaleDO;
  }

  public Language getLanguage()
  {
    return language;
  }

  public void setLanguage( Language language )
  {
    this.language = language;
  }

  public Integer getIdWeek()
  {
    return idWeek;
  }

  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

}
