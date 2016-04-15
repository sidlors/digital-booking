package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Enumeration for semaphores
 * 
 * @author gsegura
 */
public enum Semaphore
{
  GREEN("sem_vd"), YELLOW("sem_am"), RED("sem_rj");
  private String semaphore;

  private Semaphore( String semaphore )
  {
    this.semaphore = semaphore;
  }

  /**
   * @return the semaphore
   */
  public String getSemaphore()
  {
    return semaphore;
  }

  /**
   * Obtains a {@link mx.com.cinepolis.digital.booking.commons.constants.Semaphore} from a given String
   * 
   * @param s
   * @return
   */
  public Semaphore fromString( String s )
  {
    Semaphore semaphore = null;
    for( Semaphore sem : Semaphore.values() )
    {
      if( sem.semaphore.equals( s ) )
      {
        semaphore = sem;
        break;
      }
    }
    return semaphore;
  }

}
