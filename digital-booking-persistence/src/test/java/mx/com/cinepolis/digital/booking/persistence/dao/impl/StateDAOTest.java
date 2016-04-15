package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StateDAOTest extends AbstractDBEJBTestUnit
{

  private StateDAO stateDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    stateDAO = new StateDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( stateDAO );

  }

  @Test
  public void testFindAllStatesLanguage()
  {
    List<StateTO<CatalogTO, Number>> states = this.stateDAO.findAllStates();

    Assert.assertNotNull( states );
    Assert.assertFalse( states.isEmpty() );

    for( StateTO<CatalogTO, Number> state : states )
    {
      System.out.println( state.getCatalogCountry() + ", stateId: " + state.getCatalogState().getId() + ", stateName: "
          + state.getCatalogState().getName() );
    }
  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<StateDO> states = this.stateDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( states );
    Assert.assertEquals( 1, states.size() );

  }

}
