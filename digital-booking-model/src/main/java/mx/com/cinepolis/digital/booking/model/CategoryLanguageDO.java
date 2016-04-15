package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_CATEGORY_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_CATEGORY_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_CATEGORY",
    "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "CategoryLanguageDO.findAll", query = "SELECT c FROM CategoryLanguageDO c"),
    @NamedQuery(name = "CategoryLanguageDO.findByIdCategoryLanguage", query = "SELECT c FROM CategoryLanguageDO c WHERE c.idCategoryLanguage = :idCategoryLanguage"),
    @NamedQuery(name = "CategoryLanguageDO.findByIdLanguage", query = "SELECT c FROM CategoryLanguageDO c WHERE c.idLanguage = :idLanguage"),
    @NamedQuery(name = "CategoryLanguageDO.findByDsName", query = "SELECT c FROM CategoryLanguageDO c WHERE c.dsName = :dsName") })
public class CategoryLanguageDO extends AbstractEntity<CategoryLanguageDO>
{
  private static final long serialVersionUID = 8481791252361174827L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_CATEGORY_LANGUAGE", nullable = false)
  private Integer idCategoryLanguage;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CategoryDO idCategory;

  /**
   * Constructor default
   */
  public CategoryLanguageDO()
  {
  }

  /**
   * Constructor by idCategoryLanguage
   * 
   * @param idCategoryLanguage
   */
  public CategoryLanguageDO( Integer idCategoryLanguage )
  {
    this.idCategoryLanguage = idCategoryLanguage;
  }

  /**
   * @return the idCategoryLanguage
   */
  public Integer getIdCategoryLanguage()
  {
    return idCategoryLanguage;
  }

  /**
   * @param idCategoryLanguage the idCategoryLanguage to set
   */
  public void setIdCategoryLanguage( Integer idCategoryLanguage )
  {
    this.idCategoryLanguage = idCategoryLanguage;
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
   * @return the idCategory
   */
  public CategoryDO getIdCategory()
  {
    return idCategory;
  }

  /**
   * @param idCategory the idCategory to set
   */
  public void setIdCategory( CategoryDO idCategory )
  {
    this.idCategory = idCategory;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCategoryLanguage != null ? idCategoryLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CategoryLanguageDO) )
    {
      return false;
    }
    CategoryLanguageDO other = (CategoryLanguageDO) object;
    if( (this.idCategoryLanguage == null && other.idCategoryLanguage != null)
        || (this.idCategoryLanguage != null && !this.idCategoryLanguage.equals( other.idCategoryLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CategoryLanguageDO[ idCategoryLanguage=" + idCategoryLanguage + " ]";
  }

  @Override
  public int compareTo( CategoryLanguageDO other )
  {
    return this.idCategoryLanguage.compareTo( other.idCategoryLanguage );
  }

}
