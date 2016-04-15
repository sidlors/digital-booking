package mx.com.cinepolis.digital.booking.commons.to;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Transfer object for user
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class UserTO extends AbstractTO implements Comparable<UserTO>
{
  private static final long serialVersionUID = 9155904254743138804L;

  /**
   * The user id in the system
   */
  private Long id;
  /**
   * The user name in the system, do not confuse with the field
   * {@link mx.com.cinepolis.digital.booking.commons.to.AbstractTO.username}
   */
  private String name;

  /**
   * Password of the user, is only used when authenticating
   */
  private String password;

  /**
   * Person associated with user
   */
  private PersonTO personTO = new PersonTO();

  /**
   * Roles associated with user.
   */
  private List<CatalogTO> roles;

  /**
   * Regions associated with user.
   */
  private List<CatalogTO> regions;

  /**
   * Theaters associated with user.
   */
  private List<CatalogTO> theaters;

  /**
   * Roles Selected
   */
  private List<Long> rolesSelected;

  /**
   * Email To Updated
   */
  private String emailToUpdate;

  private Boolean isUpdated = true;

  private Long regionSelected;

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword( String password )
  {
    this.password = password;
  }

  /**
   * @return the personTO
   */
  public PersonTO getPersonTO()
  {
    return personTO;
  }

  /**
   * @param personTO the personTO to set
   */
  public void setPersonTO( PersonTO personTO )
  {
    this.personTO = personTO;
  }

  /**
   * @return the roles
   */
  public List<CatalogTO> getRoles()
  {
    return roles;
  }

  /**
   * @param roles the roles to set
   */
  public void setRoles( List<CatalogTO> roles )
  {
    this.roles = roles;
  }

  /**
   * @return the regions
   */
  public List<CatalogTO> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<CatalogTO> regions )
  {
    this.regions = regions;
  }

  /**
   * @return the theaters
   */
  public List<CatalogTO> getTheaters()
  {
    return theaters;
  }

  /**
   * @param theaters the theaters to set
   */
  public void setTheaters( List<CatalogTO> theaters )
  {
    this.theaters = theaters;
  }

  /**
   * @return the rolesSelected
   */
  public List<Long> getRolesSelected()
  {
    if( getIsUpdated() )
    {
      setRolesSelected( new ArrayList<Long>() );
      if( !CollectionUtils.isEmpty( roles ) )
      {
        for( CatalogTO r : roles )
        {
          rolesSelected.add( r.getId() );
        }
      }
    }
    return rolesSelected;
  }

  /**
   * @param rolesSelected the rolesSelected to set
   */
  public void setRolesSelected( List<Long> rolesSelected )
  {
    this.rolesSelected = rolesSelected;
  }

  /**
   * @return the emailToUpdate
   */
  public String getEmailToUpdate()
  {
    if( getIsUpdated() && CollectionUtils.isNotEmpty( personTO.getEmails() ) )
    {
      setEmailToUpdate( personTO.getEmails().get( 0 ).getName() );
    }
    return emailToUpdate;
  }

  /**
   * @param emailToUpdate the emailToUpdate to set
   */
  public void setEmailToUpdate( String emailToUpdate )
  {
    this.emailToUpdate = emailToUpdate;
  }

  /**
   * @return the isUpdated
   */
  public Boolean getIsUpdated()
  {
    return isUpdated;
  }

  /**
   * @param isUpdated the isUpdated to set
   */
  public void setIsUpdated( Boolean isUpdated )
  {
    this.isUpdated = isUpdated;
  }

  /**
   * @return the regionSelected
   */
  public Long getRegionSelected()
  {
    return regionSelected;
  }

  /**
   * @param regionSelected the regionSelected to set
   */
  public void setRegionSelected( Long regionSelected )
  {
    this.regionSelected = regionSelected;
  }

  public String getRegionAssigned()
  {
    String regionAssigend = null;
    if( CollectionUtils.isNotEmpty( this.regions ) )
    {
      StringBuilder sb = new StringBuilder();
      int i = 0;
      for( CatalogTO region : this.regions )
      {
        sb.append( region.getName() );

        if( ++i < this.regions.size() )
        {
          sb.append( ", " );
        }
      }
      regionAssigend = sb.toString();
    }
    return regionAssigend;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( UserTO that )
  {
    return new CompareToBuilder().append( this.name, that.name ).toComparison();
  }

  /**
   * {@inheritDoc}
   */
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof UserTO )
    {
      flag = this.id.equals( ((UserTO) object).id );
    }
    return flag;
  }

  /**
   * {@inheritDoc}
   */
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.id ).toHashCode();
  }

}
