package mx.com.cinepolis.digital.booking.web.components;

public interface Successable
{
  public abstract boolean requiresSuccessMsg();

  public abstract void setSuccessMsgScript(String paramString);

  public abstract String getSuccessMsgScript();
}
