
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfCiudad;


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
 *         &lt;element name="ObtenerCiudadesPaisesResult" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfCiudad" minOccurs="0"/>
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
    "obtenerCiudadesPaisesResult"
})
@XmlRootElement(name = "ObtenerCiudadesPaisesResponse")
public class ObtenerCiudadesPaisesResponse {

    @XmlElementRef(name = "ObtenerCiudadesPaisesResult", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfCiudad> obtenerCiudadesPaisesResult;

    /**
     * Gets the value of the obtenerCiudadesPaisesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCiudad }{@code >}
     *     
     */
    public JAXBElement<ArrayOfCiudad> getObtenerCiudadesPaisesResult() {
        return obtenerCiudadesPaisesResult;
    }

    /**
     * Sets the value of the obtenerCiudadesPaisesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfCiudad }{@code >}
     *     
     */
    public void setObtenerCiudadesPaisesResult(JAXBElement<ArrayOfCiudad> value) {
        this.obtenerCiudadesPaisesResult = ((JAXBElement<ArrayOfCiudad> ) value);
    }

}
