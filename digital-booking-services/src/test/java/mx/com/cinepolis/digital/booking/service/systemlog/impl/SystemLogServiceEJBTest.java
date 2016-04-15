package mx.com.cinepolis.digital.booking.service.systemlog.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SystemLogQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.OperationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ProcessDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.OperationDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.ProcessDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.SystemLogDAOImpl;
import mx.com.cinepolis.digital.booking.service.systemlog.ServiceSystemLogEJB;

public class SystemLogServiceEJBTest extends AbstractDBEJBTestUnit
{
  private ServiceSystemLogEJB serviceSystemLogEJB;
  

  @Before
  public void setUp()
  {
    serviceSystemLogEJB=new ServiceSystemLogEJBImpl();
    ProcessDAO processDAO=new ProcessDAOImpl();
    OperationDAO operationDAO=new OperationDAOImpl();
    SystemLogDAO systemLogDAO= new SystemLogDAOImpl();
    super.setUp();
    connect(serviceSystemLogEJB);
    connect(processDAO);
    connect( operationDAO );
    connect( systemLogDAO );
    initializeData( "dataset/business/systemLog.sql" );
    ReflectionHelper.set( processDAO, "processDAO", serviceSystemLogEJB );
    ReflectionHelper.set( operationDAO, "operationDAO", serviceSystemLogEJB );
    ReflectionHelper.set( systemLogDAO, "systemLogDAO", serviceSystemLogEJB );
  }
  /**
   * test for the method that get the sumary log 
   */
  @Test
  public void getSystemLogByPaging_Test()
  {
    int pageSize = 10;
    int page = 0;
    
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( SystemLogQuery.USER_NAME, "USER" );
    PagingResponseTO<SystemLogTO> response=this.serviceSystemLogEJB.getSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    
  }
  /**
   * test for get All Operations
   */
  @Test
  public void findOperations_Test()
  {
    List<CatalogTO> operationList=this.serviceSystemLogEJB.findOperations();
    Assert.assertNotNull( operationList );
    for(CatalogTO operation:operationList)
    {
      Assert.assertNotNull( operation );
      System.out.println( ToStringBuilder.reflectionToString( operation, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * test for get all process
   */
  @Test
  public void findProcess_Process()
  {
    List<CatalogTO> processList=this.serviceSystemLogEJB.findProcess();
    Assert.assertNotNull( processList );
    for(CatalogTO proces:processList)
    {
      Assert.assertNotNull( proces );
      System.out.println( ToStringBuilder.reflectionToString( proces, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
}
