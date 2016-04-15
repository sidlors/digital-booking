package mx.com.cinepolis.digital.booking.service.util;

import java.lang.reflect.Method;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import mx.com.cinepolis.digital.booking.commons.constants.Operation;
import mx.com.cinepolis.digital.booking.commons.constants.Process;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.OperationDO;
import mx.com.cinepolis.digital.booking.model.ProcessDO;
import mx.com.cinepolis.digital.booking.model.SystemLogDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDAO;
import mx.com.cinepolis.digital.booking.service.log.ServiceLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gsegura
 */
public class LoggingServiceInterceptor
{

  private static final Logger LOG = LoggerFactory.getLogger( LoggingServiceInterceptor.class );
  
  @EJB
  private SystemLogDAO systemLogDAO;
  
  /**
   * Intercepts the exceptions
   * 
   * @param invocationContext
   * @return
   * @throws Exception
   */
  @AroundInvoke
  public Object handleExceptions( final InvocationContext invocationContext ) throws Exception
  {

    Object result = null;
    try
    {
      result = invocationContext.proceed();

      Method m = invocationContext.getMethod();
      Object[] parameters = invocationContext.getParameters();
      ServiceLog sl = m.getAnnotation( ServiceLog.class );
      if( sl != null )
      {
        Process process = sl.process();
        Operation operation = sl.operation();
        LOG.debug( process.toString() );
        LOG.debug( operation.toString() );
        
        for( Object o : parameters )
        {
          UserTO userTO = (UserTO) o;
          SystemLogDO systemLogDO = new SystemLogDO();
          fillCommonParams(process, operation, userTO, systemLogDO);
          systemLogDO.setIdUser( new UserDO(userTO.getId().intValue()) );
          systemLogDAO.create(systemLogDO);
          systemLogDAO.flush();
          LOG.debug("id: " + systemLogDO.getIdSystemLog() + ", user:" + systemLogDO.getIdUser());
        }
        
      }
    }
    catch( Exception e )
    {
      throw e;
    }
    return result;

  }
  
  /**
   * Fill params
   * @param process
   * @param operation
   * @param abstractTO
   * @param systemLogDO
   */
  private void fillCommonParams(Process process, Operation operation, AbstractTO abstractTO, SystemLogDO systemLogDO )
  {
    systemLogDO.setIdOperation( new OperationDO(operation.getOperation()));
    systemLogDO.setIdProcess( new ProcessDO(process.getProcess()) );
    systemLogDO.setIdUser( new UserDO(abstractTO.getUserId().intValue()) );
    systemLogDO.setDtOperation( abstractTO.getTimestamp() );
    
  }

}
