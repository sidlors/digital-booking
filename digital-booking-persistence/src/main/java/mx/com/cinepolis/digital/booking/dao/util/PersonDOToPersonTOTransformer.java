package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.utils.PersonUtils;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.PersonDO} to a
 * {@link mx.com.cinepolis.digital.booking.to.PersonTO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class PersonDOToPersonTOTransformer implements Transformer
{

  @Override
  public Object transform( Object object )
  {
    PersonTO personTO = null;
    if( object instanceof PersonDO )
    {
      personTO = new PersonTO();
      PersonDO person = (PersonDO) object;
      personTO.setDsLastname( person.getDsLastname() );
      personTO.setDsMotherLastname( person.getDsMotherLastname() );
      personTO.setFgActive( person.isFgActive() );
      personTO.setId( person.getIdPerson().longValue() );
      personTO.setName( person.getDsName() );
      personTO.setTimestamp( person.getDtLastModification() );
      personTO.setUserId( Long.valueOf( person.getIdLastUserModifier() ) );
      personTO.setEmails( new ArrayList<CatalogTO>() );
      personTO.setFullName( PersonDOUtils.buildFullName( person ) );

      EmailDOToCatalogTOTransformer transformer = new EmailDOToCatalogTOTransformer();
      for( EmailDO emailDO : person.getEmailDOList() )
      {

        personTO.getEmails().add( (CatalogTO) transformer.transform( emailDO ) );

      }
    }
    return personTO;
  }

}
