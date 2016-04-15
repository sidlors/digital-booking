package mx.com.cinepolis.digital.booking.persistence;

import java.util.HashMap;
import java.util.Map;

public final class MapDAO
{

  public static Map<Class<?>, Class<?>> init()
  {
    Map<Class<?>, Class<?>> mapDAO = new HashMap<Class<?>, Class<?>>();
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.DistributorDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.CategoryDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.MovieImageDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.RegionDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.IncomeSettingsDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.IncomeSettingsTypeDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.ScreenDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.CityDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.CityDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.StateDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.StateDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.TerritoryDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.CountryDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingStatusDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingTypeDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingTypeDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.PresaleDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekScreenDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenShowDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekScreenShowDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingSpecialEventDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingSpecialEventScreenDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingSpecialEventScreenShowDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.SpecialEventWeekDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.WeekDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.EventDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.EventMovieDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.EventMovieDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.EmailDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.PersonDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.UserDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.UserDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.ObservationDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.NewsFeedDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.NewsFeedDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.SystemMenuDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.ConfigurationDAOImpl.class );

    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.ReportDAOImpl.class );
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.RoleDAOImpl.class );
    
    mapDAO.put( mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO.class,
      mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingIncomeDAOImpl.class );
    return mapDAO;
  }
}
