package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * Transfer object for Person
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class PersonTO extends CatalogTO
{

  private static final String COMMA_SEPARATOR = ", ";

  private static final long serialVersionUID = 6724801306918101194L;

  /**
   * Person lastname
   */
  private String dsLastname;

  /**
   * Person mother's lastname
   */
  private String dsMotherLastname;

  /**
   * Email
   */
  private List<CatalogTO> emails;
  
  /**
   * Full name
   */
  private String fullName;

  /**
   * @return the dsLastname
   */
  public String getDsLastname()
  {
    return dsLastname;
  }

  /**
   * @param dsLastname the dsLastname to set
   */
  public void setDsLastname( String dsLastname )
  {
    this.dsLastname = dsLastname;
  }

  /**
   * @return the dsMotherLastname
   */
  public String getDsMotherLastname()
  {
    return dsMotherLastname;
  }

  /**
   * @param dsMotherLastname the dsMotherLastname to set
   */
  public void setDsMotherLastname( String dsMotherLastname )
  {
    this.dsMotherLastname = dsMotherLastname;
  }

  /**
   * @return the emails
   */
  public List<CatalogTO> getEmails()
  {
    return emails;
  }

  /**
   * @param emails the emails to set
   */
  public void setEmails( List<CatalogTO> emails )
  {
    this.emails = emails;
  }

  /**
   * @return the fullName
   */
  public String getFullName()
  {
    return fullName;
  }

  /**
   * @param fullName the fullName to set
   */
  public void setFullName( String fullName )
  {
    this.fullName = fullName;
  }

  public String getEmail()
  {
    StringBuilder email = new StringBuilder();

    int i = 0;
    if( CollectionUtils.isNotEmpty( this.emails ) )
    {
      for( CatalogTO to : this.emails )
      {
        email.append( to.getName() );
        if( i + 1 < this.emails.size() )
        {
          email.append( COMMA_SEPARATOR );
        }
        i++;
      }
    }

    return email.toString();

  }

}
