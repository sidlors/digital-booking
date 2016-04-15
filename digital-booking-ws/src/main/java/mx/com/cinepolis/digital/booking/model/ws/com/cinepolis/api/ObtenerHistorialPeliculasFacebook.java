
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idsVista" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idPais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="formato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "idsVista",
    "idPais",
    "formato"
})
@XmlRootElement(name = "ObtenerHistorialPeliculasFacebook")
public class ObtenerHistorialPeliculasFacebook {

    @XmlElementRef(name = "idsVista", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<String> idsVista;
    @XmlElementRef(name = "idPais", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<String> idPais;
    @XmlElementRef(name = "formato", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<String> formato;

    /**
     * Gets the value of the idsVista property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdsVista() {
        return idsVista;
    }

    /**
     * Sets the value of the idsVista property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdsVista(JAXBElement<String> value) {
        this.idsVista = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idPais property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdPais() {
        return idPais;
    }

    /**
     * Sets the value of the idPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdPais(JAXBElement<String> value) {
        this.idPais = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the formato property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFormato() {
        return formato;
    }

    /**
     * Sets the value of the formato property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFormato(JAXBElement<String> value) {
        this.formato = ((JAXBElement<String> ) value);
    }

}
