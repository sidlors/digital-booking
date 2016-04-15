package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Enumeration for ServiceLog
 * 
 * @author gsegura
 */
public enum Operation
{
  LOGIN(1), LOGOUT(2);

  private int operation;

  /**
   * @return the operation
   */
  public int getOperation()
  {
    return operation;
  }

  private Operation( int operation )
  {
    this.operation = operation;
  }

  public Operation fromOperation( int id )
  {
    Operation operation = null;
    for( Operation o : Operation.values() )
    {
      if( o.operation == id )
      {
        operation = o;
        break;
      }
    }
    return operation;
  }

}
