package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;

import org.apache.commons.collections.Transformer;
/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.PresaleDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.PresaleTO}
 * 
 * @author jcarbajal
 */

public class PresaleDOToPresaleTOTransformer  implements Transformer
{
  private Language language;
  private Integer idWeek;
  /**
  * Constructor default
  */
 public PresaleDOToPresaleTOTransformer()
 {
   this.language = Language.ENGLISH;
 }

 /**
  * Constructor by language
  * 
  * @param language
  */
 public PresaleDOToPresaleTOTransformer( Language language )
 {
   this.language = language;
 }
 
  @Override
  public Object transform( Object object )
  {
    PresaleTO presaleTO=null;
    if(object instanceof PresaleDO)
    {
      PresaleDO presaleDO=(PresaleDO)object;
      presaleTO = new PresaleTO();
      presaleTO.setDtFinalDayPresale( presaleDO.getDtFinalDayPresale() );
      presaleTO.setDtReleaseDay( presaleDO.getDtReleaseDay() );
      presaleTO.setDtStartDayPresale( presaleDO.getDtStartDayPresale() );
      presaleTO.setFgActive( presaleDO.isFgActive()  );
      presaleTO.setIdLanguage( Long.valueOf( this.language.getId()));
      presaleTO.setIdPresale( presaleDO.getIdPresale() );
      presaleTO.setUserId( Long.valueOf(  presaleDO.getIdPresale()));
      if(presaleDO.getIdBookingSpecialEventScreen()!=null)
      {
      presaleTO.setIdBookingSpecialEventScreen( presaleDO.getIdBookingSpecialEventScreen().getIdBookingSpecialEventScreen() );
      }
      else if (presaleDO.getIdBookingWeekScreen()!=null)
      {
        presaleTO.setIdBookingWeekScreen( presaleDO.getIdBookingWeekScreen().getIdBookingWeekScreen() );
      }
    }
    return presaleTO;
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
