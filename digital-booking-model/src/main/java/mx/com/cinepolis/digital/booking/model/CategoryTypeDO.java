package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_CATEGORY_TYPE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_CATEGORY_TYPE")
@NamedQueries({
    @NamedQuery(name = "CategoryTypeDO.findAll", query = "SELECT c FROM CategoryTypeDO c"),
    @NamedQuery(name = "CategoryTypeDO.findByIdCategoryType", query = "SELECT c FROM CategoryTypeDO c WHERE c.idCategoryType = :idCategoryType") })
public class CategoryTypeDO extends AbstractEntity<CategoryTypeDO>
{
  private static final long serialVersionUID = -7187889855979472672L;

  @Id
  @Column(name = "ID_CATEGORY_TYPE", nullable = false)
  private Integer idCategoryType;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategoryType", fetch = FetchType.LAZY)
  private List<CategoryDO> categoryDOList;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategoryType", fetch = FetchType.LAZY)
  private List<CategoryTypeLanguageDO> categoryTypeLanguageDOList;

  /**
   * Constructor default
   */
  public CategoryTypeDO()
  {
  }

  /**
   * Constructor by idCategoryType
   * 
   * @param idCategoryType
   */
  public CategoryTypeDO( Integer idCategoryType )
  {
    this.idCategoryType = idCategoryType;
  }

  /**
   * @return the idCategoryType
   */
  public Integer getIdCategoryType()
  {
    return idCategoryType;
  }

  /**
   * @param idCategoryType the idCategoryType to set
   */
  public void setIdCategoryType( Integer idCategoryType )
  {
    this.idCategoryType = idCategoryType;
  }

  /**
   * @return the categoryDOList
   */
  public List<CategoryDO> getCategoryDOList()
  {
    return categoryDOList;
  }

  /**
   * @param categoryDOList the categoryDOList to set
   */
  public void setCategoryDOList( List<CategoryDO> categoryDOList )
  {
    this.categoryDOList = categoryDOList;
  }

  /**
   * @return the categoryTypeLanguageDOList
   */
  public List<CategoryTypeLanguageDO> getCategoryTypeLanguageDOList()
  {
    return categoryTypeLanguageDOList;
  }

  /**
   * @param categoryTypeLanguageDOList the categoryTypeLanguageDOList to set
   */
  public void setCategoryTypeLanguageDOList( List<CategoryTypeLanguageDO> categoryTypeLanguageDOList )
  {
    this.categoryTypeLanguageDOList = categoryTypeLanguageDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCategoryType != null ? idCategoryType.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CategoryTypeDO) )
    {
      return false;
    }
    CategoryTypeDO other = (CategoryTypeDO) object;
    if( (this.idCategoryType == null && other.idCategoryType != null)
        || (this.idCategoryType != null && !this.idCategoryType.equals( other.idCategoryType )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CategoryTypeDO[ idCategoryType=" + idCategoryType + " ]";
  }

  @Override
  public int compareTo( CategoryTypeDO other )
  {
    return this.idCategoryType.compareTo( other.idCategoryType );
  }

}
