package mx.com.cinepolis.digital.booking.service.reports;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;

/**
 * Interface que define los metodos asociados a la administración de zonas de los cines
 * 
 * @author rgarcia
 */
@Local
public interface ServiceReportsEJB
{

  /**
   * Se obtiene el reporte de hoja de programación por el cine
   * 
   * @param idWeek
   * @param idTheater
   * @return
   */
  FileTO getWeeklyBookingReportByTheater( Long idWeek, Long idTheater );

  /**
   * Se obtiene el reporte de hora de programacion con todos los cines por region
   * 
   * @param idWeek
   * @param regionId
   * @return
   */
  FileTO getWeeklyBookingReportByRegion( Long weekId, Long regionId );

  /**
   * Se obtiene el reporte de programacion de distribuidores
   * 
   * @param idWeek
   * @param idDistributor
   * @return
   */
  FileTO getWeeklyDistributorReportByDistributor( Long idWeek, Long idRegion, Long idDistributor );

  /**
   * Se obtiene el reporte de programacion de todos los distribuidores
   * 
   * @param idWeek
   * @return
   */
  FileTO getWeeklyDistributorReportByAllDistributors( Long idWeek, Long idRegion );

  /**
   * Se obtiene el reporte de programacion de preventas por zona
   * 
   * @param List<BookingTO>
   * @return FileTO
   */
  FileTO getWeeklyBookingReportPresaleByRegion( List<EventTO> movies, Long idWeek, Long idRegion );
  
  /**
   * @author jcarbajal
   * 
   * Se obtiene el reporte de programacion de preventas por cine
   * 
   * @param List<EventTO> 
   * @param idWeek
   * @param idTheater
   * @return FileTO
   */
  FileTO getWeeklyBookingReportPresaleByTheater( List<EventTO> movies, Long idWeek, Long idTheater );

  /**
   * Method that returns the bookings in presale by idWeek, idRegion and a eventTO list.
   * 
   * @param movies
   * @param idWeek
   * @param idRegion
   * @return bookingTO list
   */
  List<BookingTO> findBookingsInPresaleByEevntZoneAndWeek( List<EventTO> movies, Long idWeek, Long idRegion );
}
