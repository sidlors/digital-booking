/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * JPA entity for C_COUNTRY_LANGUAGE
 * 
 * @author kperez
 */
@Entity
@Table(name = "C_COUNTRY_LANGUAGE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID_COUNTRY", "ID_LANGUAGE"})})
@NamedQueries({
    @NamedQuery(name = "CountryLanguageDO.findAll", query = "SELECT c FROM CountryLanguageDO c"),
    @NamedQuery(name = "CountryLanguageDO.findByIdCountryLanguage", query = "SELECT c FROM CountryLanguageDO c WHERE c.idCountryLanguage = :idCountryLanguage"),
    @NamedQuery(name = "CountryLanguageDO.findByDsName", query = "SELECT c FROM CountryLanguageDO c WHERE c.dsName = :dsName")})
public class CountryLanguageDO extends AbstractEntity<CountryLanguageDO> {

  private static final long serialVersionUID = 616860248830342657L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COUNTRY_LANGUAGE", nullable = false)
    private Integer idCountryLanguage;
    
    @Column(name = "DS_NAME", length = 160)
    private String dsName;
    
    @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private LanguageDO idLanguage;
    
    @JoinColumn(name = "ID_COUNTRY", referencedColumnName = "ID_COUNTRY", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CountryDO idCountry;

    /**
     * Constructor default
     */
    public CountryLanguageDO() {
    }

    /**
     * Constructor by idCountryLanguage
     * @param idCountryLanguage
     */
    public CountryLanguageDO(Integer idCountryLanguage) {
        this.idCountryLanguage = idCountryLanguage;
    }

    /**
     * @return the idCountryLanguage
     */
    public Integer getIdCountryLanguage()
    {
      return idCountryLanguage;
    }

    /**
     * @param idCountryLanguage the idCountryLanguage to set
     */
    public void setIdCountryLanguage( Integer idCountryLanguage )
    {
      this.idCountryLanguage = idCountryLanguage;
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
     * @return the idCountry
     */
    public CountryDO getIdCountry()
    {
      return idCountry;
    }

    /**
     * @param idCountry the idCountry to set
     */
    public void setIdCountry( CountryDO idCountry )
    {
      this.idCountry = idCountry;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCountryLanguage != null ? idCountryLanguage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CountryLanguageDO)) {
            return false;
        }
        CountryLanguageDO other = (CountryLanguageDO) object;
        if ((this.idCountryLanguage == null && other.idCountryLanguage != null) || (this.idCountryLanguage != null && !this.idCountryLanguage.equals(other.idCountryLanguage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.cinepolis.digital.booking.model.CountryLanguageDO[ idCountryLanguage=" + idCountryLanguage + " ]";
    }

    @Override
    public int compareTo( CountryLanguageDO o )
    {
      return this.idCountryLanguage.compareTo( o.idCountryLanguage );
    }
    
}
