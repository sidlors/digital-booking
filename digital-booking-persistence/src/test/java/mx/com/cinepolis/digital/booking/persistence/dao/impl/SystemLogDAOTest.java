package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SystemLogQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.model.OperationDO;
import mx.com.cinepolis.digital.booking.model.ProcessDO;
import mx.com.cinepolis.digital.booking.model.SystemLogDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.OperationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ProcessDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase para realizar pruebas del SystemLogDAO
 * @author jcarbajal
 *
 */
public class SystemLogDAOTest extends AbstractDBEJBTestUnit
{
  private SystemLogDAO systemLogDAO;
  
  private ProcessDAO processDAO;
  
  private OperationDAO operationDAO;
  
  private UserDAO userDAO;
  
  @Before
  public void setUp()
  {
    systemLogDAO = new SystemLogDAOImpl();
    userDAO = new UserDAOImpl();
    processDAO=new ProcessDAOImpl();
    operationDAO= new OperationDAOImpl();
    super.setUp();
    connect(systemLogDAO);
    connect(userDAO);
    connect(processDAO);
    connect( operationDAO );
    this.initializeData( "dataset/business/systemLog.sql" );
  }
  
  /**
   * 
   */
  @Test
  public void finadAllOperation_Test()
  {
    List<CatalogTO> operations=this.operationDAO.findAllperations();
    Assert.assertNotNull( operations );
    for(CatalogTO operation:operations)
    {
      Assert.assertNotNull( operation );
      System.out.println( ToStringBuilder.reflectionToString( operation, ToStringStyle.MULTI_LINE_STYLE ) ); 
    }
  }
  @Test
  public void finadAllProcess_Test()
  {
    List<CatalogTO> process=this.processDAO.findAllProcces();
    Assert.assertNotNull( process );
    for(CatalogTO proces:process)
    {
      Assert.assertNotNull( proces );
      System.out.println( ToStringBuilder.reflectionToString( proces, ToStringStyle.MULTI_LINE_STYLE ) ); 
    }
  }
  /**
   * realize the test for the method that find all the records
   */
  @Test
  public void fidAll_Test()
  {
    List<SystemLogDO> systemLogDOList=this.systemLogDAO.findAll();
    Assert.assertNotNull( systemLogDOList );
    for(SystemLogDO systemLog:systemLogDOList)
    {
      Assert.assertNotNull( systemLog );
    }
  }
  
  /**
   * realize the tes for the method that find by id
   */
  @Test
  public void findById_Test()
  {
    SystemLogDO systemLog=this.systemLogDAO.find( 1L );
    Assert.assertNotNull( systemLog );
  }
  /**
   * realize the test for the method that count the records
   */
  @Test
  public void count_Test()
  {
    int size = this.systemLogDAO.count();
    Assert.assertEquals( size, size );
  }
  /**
   * realize test for the create method
   */
  
  @Test
  public void create_Test()
  {
   int sizeBeforeCreate=this.systemLogDAO.count();
   SystemLogDO systemLog=new SystemLogDO();
   systemLog.setDtOperation( new Date() );
   OperationDO idOperation=new OperationDO();
   idOperation.setIdOperation( 1 );
   systemLog.setIdOperation( idOperation );
   ProcessDO idProcess=new ProcessDO( 1 );
   systemLog.setIdProcess( idProcess );
   systemLog.setIdSystemLog( 100L );
   UserDO idUser=this.userDAO.find( 1 );
   Assert.assertNotNull( idUser );
   systemLog.setIdUser( idUser );
   this.systemLogDAO.create( systemLog );
   int sizeAfter=this.systemLogDAO.count();
   Assert.assertEquals( sizeBeforeCreate+1, sizeAfter );
  }
  
  /**
   * realize test for the paging query
   * with the user name parameter 
   */
  @Test
  public void findAlByPaging_Test()
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
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * realize test for the paging query
   * with the person name parameter 
   */
  @Test
  public void findAlByPaging_TestWithPersonName()
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
    pagingRequestTO.getFilters().put( SystemLogQuery.PERSON_NAME, "PERSONA" );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * realize test for the paging query
   * with the person last name parameter 
   */
  @Test
  public void findAlByPaging_TestWithPersonLastName()
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
    pagingRequestTO.getFilters().put( SystemLogQuery.PERSON_LAST_NAME, "PERSONA 2" );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * realize test for the paging query
   * with operation id  parameter 
   */
  @Test
  public void findAlByPaging_TestWithOperationId()
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
    pagingRequestTO.getFilters().put( SystemLogQuery.ID_OPERATION, 1 );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * realize test for the paging query
   * with process id  parameter 
   */
  @Test
  public void findAlByPaging_TestWithProcessId()
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
    pagingRequestTO.getFilters().put( SystemLogQuery.ID_PROCESS, 1 );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   * realize test for the paging query
   * with dates to search 
   */
  @Test
  public void findAlByPaging_TestWithDates()
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
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JUNE, 18 );
    Date date = cal.getTime();
    Calendar cal2 = Calendar.getInstance();
    cal2.set( 2014, Calendar.JUNE, 22 );
    Date date2 = cal2.getTime();
    
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( SystemLogQuery.START_DATE, date );
    pagingRequestTO.getFilters().put( SystemLogQuery.FINAL_DATE, date2 );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  
  /**
   * realize test for the paging query
   * with dates to search and sorting by DT_OPERATION
   */
  @Test
  public void findAlByPaging_TestWithSorting()
  {
    int pageSize = 10;
    int page = 0;
    
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( SystemLogQuery.DT_OPERATION );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JUNE, 18 );
    Date date = cal.getTime();
    Calendar cal2 = Calendar.getInstance();
    cal2.set( 2014, Calendar.JUNE, 22 );
    Date date2 = cal2.getTime();
    
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( SystemLogQuery.START_DATE, date );
    pagingRequestTO.getFilters().put( SystemLogQuery.FINAL_DATE, date2 );
    PagingResponseTO<SystemLogTO> response=this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for(SystemLogTO systemLog:response.getElements())
    {
      Assert.assertNotNull( systemLog );
      System.out.println( ToStringBuilder.reflectionToString( systemLog, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
}
