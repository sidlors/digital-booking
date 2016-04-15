
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Dispositivos;


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
 *         &lt;element name="dispositivo" type="{http://schemas.datacontract.org/2004/07/APICinepolis}Dispositivos" minOccurs="0"/>
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
    "dispositivo",
    "formato"
})
@XmlRootElement(name = "ObtenerInformacionVersion")
public class ObtenerInformacionVersion {

    protected Dispositivos dispositivo;
    @XmlElementRef(name = "formato", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<String> formato;

    /**
     * Gets the value of the dispositivo property.
     * 
     * @return
     *     possible object is
     *     {@link Dispositivos }
     *     
     */
    public Dispositivos getDispositivo() {
        return dispositivo;
    }

    /**
     * Sets the value of the dispositivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Dispositivos }
     *     
     */
    public void setDispositivo(Dispositivos value) {
        this.dispositivo = value;
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
