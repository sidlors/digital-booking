package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Process enum
 * 
 * @author gsegura
 */
public enum Process
{
  SYSTEM(1);

  private int process;

  /**
   * @return the process
   */
  public int getProcess()
  {
    return process;
  }

  private Process( int process )
  {
    this.process = process;
  }

  public Process fromProcess( int id )
  {
    Process process = null;
    for( Process e : Process.values() )
    {
      if( e.process == id )
      {
        process = e;
        break;
      }
    }
    return process;
  }

}
