package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;



/**
 * Clase TO para el envío de correo
 * 
 * @author agustin.ramirez
 */
public class EmailTO extends AbstractTO
{

  /**
	 * 
	 */
	private static final long serialVersionUID = -2582783224039503710L;

/** Listado de recipientes 
   * 
   * */
  private List<String> recipients;
  /** 
   * Listado de recipientes CC 
   * */
  private List<String> recipientsCC;
  
  /** 
   * Listado de recipientes BCC 
   * */
  private List<String> recipientsBCC;
  
  /** 
   * Asunto del correo 
   * */
  private String subject;
  /**
   * Mensaje del correo
   */
  private String message;

  /**
   * Files
   */
  private List<FileTO> files ;
  


  /**
   * @return the recipients
   */
  public List<String> getRecipients()
  {
    return recipients;
  }

  /**
   * @param recipients the recipients to set
   */
  public void setRecipients( List<String> recipients )
  {
    this.recipients = recipients;
  }

  /**
   * @return the recipientsCC
   */
  public List<String> getRecipientsCC()
  {
    return recipientsCC;
  }

  /**
   * @param recipientsCC the recipientsCC to set
   */
  public void setRecipientsCC( List<String> recipientsCC )
  {
    this.recipientsCC = recipientsCC;
  }

  /**
   * @return the recipientsBCC
   */
  public List<String> getRecipientsBCC()
  {
    return recipientsBCC;
  }

  /**
   * @param recipientsBCC the recipientsBCC to set
   */
  public void setRecipientsBCC( List<String> recipientsBCC )
  {
    this.recipientsBCC = recipientsBCC;
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
 * @return the message
 */
public String getMessage() {
	return message;
}

/**
 * @param message the message to set
 */
public void setMessage(String message) {
	this.message = message;
}

/**
 * @return the files
 */
public List<FileTO> getFiles() {
	return files;
}

/**
 * @param files the files to set
 */
public void setFiles(List<FileTO> files) {
	this.files = files;
}

 
}
