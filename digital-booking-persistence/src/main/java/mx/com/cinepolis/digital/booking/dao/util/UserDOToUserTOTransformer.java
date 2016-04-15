package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.UserDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.UserTO}
 * 
 * @author agustin.ramirez
 * @author afuentes
 * @since 0.0.1
 */
public class UserDOToUserTOTransformer implements Transformer
{
  private Language language;

  /**
   * Default constructor.
   */
  public UserDOToUserTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language The {@link Language}.
   */
  public UserDOToUserTOTransformer( Language laguage )
  {
    this.language = laguage;
  }

  /*
   * (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @Override
  public Object transform( Object object )
  {
    UserTO userTO = null;
    if( object instanceof UserDO )
    {
      UserDO user = (UserDO) object;
      userTO = new UserTO();
      userTO.setFgActive( user.isFgActive() );
      userTO.setId( user.getIdUser().longValue() );
      userTO.setName( user.getDsUsername() );
      userTO.setTimestamp( user.getDtLastModification() );
      userTO.setUserId( Long.valueOf( user.getIdLastUserModifier() ) );
      userTO.setUsername( user.getDsUsername() );
      PersonDOToPersonTOTransformer transformer = new PersonDOToPersonTOTransformer();
      userTO.setPersonTO( (PersonTO) transformer.transform( user.getIdPerson() ) );
      userTO.setRoles( extractRoles( user.getRoleDOList() ) );
      userTO.setRegions( extractRegions( user.getRegionDOList() ) );
      userTO.setTheaters( extractTheaters( user.getTheaterDOList() ) );
    }
    return userTO;
  }

  private List<CatalogTO> extractTheaters( List<TheaterDO> theaterDOList )
  {
    List<CatalogTO> theaterCatalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( theaterDOList, new TheaterDOToCatalogTOTransformer( language ), theaterCatalogTOs );
    return theaterCatalogTOs;
  }

  private List<CatalogTO> extractRegions( List<RegionDO> regionDOList )
  {
    List<CatalogTO> regionCatalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( regionDOList, new RegionDOToCatalogTOTransformer( language ), regionCatalogTOs );
    return regionCatalogTOs;
  }

  private List<CatalogTO> extractRoles( List<RoleDO> roleDOList )
  {
    List<CatalogTO> roleCatalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( roleDOList, new RoleDOToCatalogTOTransformer(), roleCatalogTOs );
    return roleCatalogTOs;
  }

}
