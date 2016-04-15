
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelacionComplejoPelicula complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelacionComplejoPelicula">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdComplejo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdPelicula" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelacionComplejoPelicula", propOrder = {
    "idComplejo",
    "idPelicula"
})
public class RelacionComplejoPelicula {

    @XmlElement(name = "IdComplejo")
    protected Integer idComplejo;
    @XmlElement(name = "IdPelicula")
    protected Integer idPelicula;

    /**
     * Gets the value of the idComplejo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdComplejo() {
        return idComplejo;
    }

    /**
     * Sets the value of the idComplejo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdComplejo(Integer value) {
        this.idComplejo = value;
    }

    /**
     * Gets the value of the idPelicula property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdPelicula() {
        return idPelicula;
    }

    /**
     * Sets the value of the idPelicula property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdPelicula(Integer value) {
        this.idPelicula = value;
    }

}
