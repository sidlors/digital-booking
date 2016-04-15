package mx.com.cinepolis.digital.booking.service.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mx.com.cinepolis.digital.booking.commons.constants.Process;
import mx.com.cinepolis.digital.booking.commons.constants.Operation;

/**
 * Annotation for interceptor
 * 
 * @author gsegura
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceLog
{

  /**
   * @return
   */
  Operation operation();

  /**
   * @return
   */
  Process process();
}
