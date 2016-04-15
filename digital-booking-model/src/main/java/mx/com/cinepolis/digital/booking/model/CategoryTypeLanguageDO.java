package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_CATEGORY_TYPE_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_CATEGORY_TYPE_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_CATEGORY_TYPE",
    "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "CategoryTypeLanguageDO.findAll", query = "SELECT c FROM CategoryTypeLanguageDO c"),
    @NamedQuery(name = "CategoryTypeLanguageDO.findByIdCategoryTypeLanguage", query = "SELECT c FROM CategoryTypeLanguageDO c WHERE c.idCategoryTypeLanguage = :idCategoryTypeLanguage"),
    @NamedQuery(name = "CategoryTypeLanguageDO.findByDsName", query = "SELECT c FROM CategoryTypeLanguageDO c WHERE c.dsName = :dsName") })
public class CategoryTypeLanguageDO extends AbstractEntity<CategoryTypeLanguageDO>
{
  private static final long serialVersionUID = -2300791164367140943L;

  @Id
  @Column(name = "ID_CATEGORY_TYPE_LANGUAGE", nullable = false)
  private Integer idCategoryTypeLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_CATEGORY_TYPE", referencedColumnName = "ID_CATEGORY_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CategoryTypeDO idCategoryType;

  /**
   * Constructor default
   */
  public CategoryTypeLanguageDO()
  {
  }

  /**
   * Constructor by idCategoryTypeLanguage
   * 
   * @param idCategoryTypeLanguage
   */
  public CategoryTypeLanguageDO( Integer idCategoryTypeLanguage )
  {
    this.idCategoryTypeLanguage = idCategoryTypeLanguage;
  }

  /**
   * @return the idCategoryTypeLanguage
   */
  public Integer getIdCategoryTypeLanguage()
  {
    return idCategoryTypeLanguage;
  }

  /**
   * @param idCategoryTypeLanguage the idCategoryTypeLanguage to set
   */
  public void setIdCategoryTypeLanguage( Integer idCategoryTypeLanguage )
  {
    this.idCategoryTypeLanguage = idCategoryTypeLanguage;
  }

  /**
   * @return the dsName
   */
  public String getDsName()
  {
    return dsName;
  }

  /**
   * @param dsName the dsName to set
   */
  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  /**
   * @return the idLanguage
   */
  public LanguageDO getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( LanguageDO idLanguage )
  {
    this.idLanguage = idLanguage;
  }

  /**
   * @return the idCategoryType
   */
  public CategoryTypeDO getIdCategoryType()
  {
    return idCategoryType;
  }

  /**
   * @param idCategoryType the idCategoryType to set
   */
  public void setIdCategoryType( CategoryTypeDO idCategoryType )
  {
    this.idCategoryType = idCategoryType;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCategoryTypeLanguage != null ? idCategoryTypeLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CategoryTypeLanguageDO) )
    {
      return false;
    }
    CategoryTypeLanguageDO other = (CategoryTypeLanguageDO) object;
    if( (this.idCategoryTypeLanguage == null && other.idCategoryTypeLanguage != null)
        || (this.idCategoryTypeLanguage != null && !this.idCategoryTypeLanguage.equals( other.idCategoryTypeLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CategoryTypeLanguageDO[ idCategoryTypeLanguage="
        + idCategoryTypeLanguage + " ]";
  }

  @Override
  public int compareTo( CategoryTypeLanguageDO other )
  {
    return this.idCategoryTypeLanguage.compareTo( other.idCategoryTypeLanguage );
  }

}
