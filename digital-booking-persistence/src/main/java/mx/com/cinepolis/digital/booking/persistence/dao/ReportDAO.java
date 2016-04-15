package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;


/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.BookingDO}
 * 
 * @author rgarcia
 * @since 0.2.0
 */
@Local
public interface ReportDAO
{
  /**
   * Se obtiene la información del booking para el cine
   * @param idWeek
   * @param idTheater
   * @return
   */
  WeeklyBookingReportTheaterTO findWeeklyBookingReportTheaterTO(Long idWeek, Long idTheater, Language language );
  /**
   * Se obtiene la informacion del booking para todos los cines
   * @param idWeek
   * @param idRegion 
   * @param idTheater
   * @return
   */
  List<WeeklyBookingReportTheaterTO> findAllWeeklyBookingReportTheaterTO(Long idWeek, Long idRegion, Language language );
  
  /**
   * 
   * @param idWeek
   * @param idDistributor
   * @param language
   * @return
   */
  List<WeeklyBookingReportTheaterTO> findWeeklyBookingReportTheaterTOByDistributor(Long idWeek, Long idRegion, Long idDistributor, Language language );

  /**
   * 
   * @param idWeek
   * @param language
   * @return
   */
  List<WeeklyBookingReportTheaterTO> findWeeklyBookingReportTheaterTOByAllDistributors(Long idWeek, Long idRegion, Language language );
  
}
