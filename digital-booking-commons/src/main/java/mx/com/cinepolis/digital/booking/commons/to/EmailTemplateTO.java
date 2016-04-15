package mx.com.cinepolis.digital.booking.commons.to;

/**
 * Data Transfer Object class to transport information relating to the e-mail template.
 * 
 * @author afuentes
 */
public class EmailTemplateTO extends AbstractTO
{

  private static final long serialVersionUID = -7562886884237638214L;

  private Integer id;
  private String subject;
  private String body;

  /**
   * @return the id
   */
  public Integer getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( Integer id )
  {
    this.id = id;
  }

  /**
   * @return the subject
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  /**
   * @return the body
   */
  public String getBody()
  {
    return body;
  }

  /**
   * @param body the body to set
   */
  public void setBody( String body )
  {
    this.body = body;
  }

}
