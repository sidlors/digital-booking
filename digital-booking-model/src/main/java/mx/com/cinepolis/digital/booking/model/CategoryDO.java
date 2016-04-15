package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_CATEGORY
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_CATEGORY")
@NamedQueries({
    @NamedQuery(name = "CategoryDO.findAll", query = "SELECT c FROM CategoryDO c"),
    @NamedQuery(name = "CategoryDO.findByIdCategory", query = "SELECT c FROM CategoryDO c WHERE c.idCategory = :idCategory"),
    @NamedQuery(name = "CategoryDO.findByIdCategoryTypeActive", query = "SELECT c FROM CategoryDO c INNER JOIN c.idCategoryType AS ct WHERE ct.idCategoryType = :idCategoryType and c.fgActive = true"),
    @NamedQuery(name = "CategoryDO.findByDsNameActive", query = "SELECT c FROM CategoryDO c INNER JOIN c.categoryLanguageDOList as cl INNER JOIN cl.idLanguage as l WHERE cl.dsName = :dsName and c.idCategoryType.idCategoryType = :idCategoryType and l.idLanguage = :idLanguage and c.fgActive = true"),
    @NamedQuery(name = "CategoryDO.findByFgActive", query = "SELECT c FROM CategoryDO c WHERE c.fgActive = :fgActive"),
    @NamedQuery(name = "CategoryDO.findByDtLastModification", query = "SELECT c FROM CategoryDO c WHERE c.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "CategoryDO.findByIdLastUserModifier", query = "SELECT c FROM CategoryDO c WHERE c.idLastUserModifier = :idLastUserModifier") })
public class CategoryDO extends AbstractSignedEntity<CategoryDO>
{
  private static final long serialVersionUID = 139013581371739063L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_CATEGORY", nullable = false)
  private Integer idCategory;

  @JoinTable(name = "K_EVENT_X_CATEGORY", joinColumns = { @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY") }, inverseJoinColumns = { @JoinColumn(name = "ID_EVENT", referencedColumnName = "ID_EVENT") })
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  private List<EventDO> eventDOList;

  @JoinTable(name = "C_SCREEN_X_CATEGORY", joinColumns = { @JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_SCREEN", referencedColumnName = "ID_SCREEN", nullable = false) })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<ScreenDO> screenDOList;
  
  @JoinTable(name = "K_TRAILER_X_CATEGORY", joinColumns = {@JoinColumn(name = "ID_CATEGORY", referencedColumnName = "ID_CATEGORY", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_TRAILER",referencedColumnName = "ID_TRAILER", nullable = false)})
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private List<TrailerDO> trailerDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCategory", fetch = FetchType.LAZY)
  private List<CategoryLanguageDO> categoryLanguageDOList;

  @JoinColumn(name = "ID_CATEGORY_TYPE", referencedColumnName = "ID_CATEGORY_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CategoryTypeDO idCategoryType;

  /**
   * Constructor default
   */
  public CategoryDO()
  {
  }

  /**
   * Constructor by idCategory
   * 
   * @param idCategory
   */
  public CategoryDO( Integer idCategory )
  {
    this.idCategory = idCategory;
  }

  /**
   * @return the idCategory
   */
  public Integer getIdCategory()
  {
    return idCategory;
  }

  /**
   * @param idCategory the idCategory to set
   */
  public void setIdCategory( Integer idCategory )
  {
    this.idCategory = idCategory;
  }

  /**
   * @return the eventDOList
   */
  public List<EventDO> getEventDOList()
  {
    return eventDOList;
  }

  /**
   * @param eventDOList the eventDOList to set
   */
  public void setEventDOList( List<EventDO> eventDOList )
  {
    this.eventDOList = eventDOList;
  }

  /**
   * @return the screenDOList
   */
  public List<ScreenDO> getScreenDOList()
  {
    return screenDOList;
  }

  /**
   * @param screenDOList the screenDOList to set
   */
  public void setScreenDOList( List<ScreenDO> screenDOList )
  {
    this.screenDOList = screenDOList;
  }

  /**
   * @return the categoryLanguageDOList
   */
  public List<CategoryLanguageDO> getCategoryLanguageDOList()
  {
    return categoryLanguageDOList;
  }

  /**
   * @param categoryLanguageDOList the categoryLanguageDOList to set
   */
  public void setCategoryLanguageDOList( List<CategoryLanguageDO> categoryLanguageDOList )
  {
    this.categoryLanguageDOList = categoryLanguageDOList;
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

  /**
   * @return the trailerDOList
   */
  public List<TrailerDO> getTrailerDOList()
  {
    return trailerDOList;
  }

  /**
   * @param trailerDOList the trailerDOList to set
   */
  public void setTrailerDOList( List<TrailerDO> trailerDOList )
  {
    this.trailerDOList = trailerDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCategory != null ? idCategory.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CategoryDO) )
    {
      return false;
    }
    CategoryDO other = (CategoryDO) object;
    if( (this.idCategory == null && other.idCategory != null)
        || (this.idCategory != null && !this.idCategory.equals( other.idCategory )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CategoryDO[ idCategory=" + idCategory + " ]";
  }

  @Override
  public int compareTo( CategoryDO other )
  {
    return this.idCategory.compareTo( other.idCategory );
  }

}
