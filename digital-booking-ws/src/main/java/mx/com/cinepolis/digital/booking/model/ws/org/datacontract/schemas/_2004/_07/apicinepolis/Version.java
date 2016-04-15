
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Version complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Version">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MasReciente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Minima" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Version", propOrder = {
    "masReciente",
    "minima"
})
public class Version {

    @XmlElementRef(name = "MasReciente", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> masReciente;
    @XmlElementRef(name = "Minima", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> minima;

    /**
     * Gets the value of the masReciente property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMasReciente() {
        return masReciente;
    }

    /**
     * Sets the value of the masReciente property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMasReciente(JAXBElement<String> value) {
        this.masReciente = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the minima property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMinima() {
        return minima;
    }

    /**
     * Sets the value of the minima property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMinima(JAXBElement<String> value) {
        this.minima = ((JAXBElement<String> ) value);
    }

}
