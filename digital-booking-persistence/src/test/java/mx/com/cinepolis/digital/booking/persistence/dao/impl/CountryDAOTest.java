package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that implements the {@link CountryDAO} unit tests.
 * 
 * @author afuentes
 */
public class CountryDAOTest extends AbstractDBEJBTestUnit
{
  private static final Logger LOG = LoggerFactory.getLogger( CountryDAOTest.class );

  private CountryDAO countryDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    countryDAO = new CountryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( countryDAO );

  }

  @Test
  public void testGetAll()
  {
    List<CatalogTO> tos = countryDAO.getAll();
    Assert.assertNotNull( tos );
    Assert.assertFalse( tos.isEmpty() );
    for( CatalogTO to : tos )
    {
      LOG.debug( StringUtils.EMPTY, to );
    }
  }

  @Test
  public void testGetAll_Language()
  {
    List<CatalogTO> tos = countryDAO.getAll( Language.SPANISH );
    Assert.assertNotNull( tos );
    Assert.assertFalse( tos.isEmpty() );
    for( CatalogTO to : tos )
    {
      LOG.debug( StringUtils.EMPTY, to );
    }
  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<CountryDO> countries = this.countryDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( countries );
    Assert.assertEquals( 1, countries.size() );
  }
}
