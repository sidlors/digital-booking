
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfFestival;


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
 *         &lt;element name="ObtenerFestivalesResult" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfFestival" minOccurs="0"/>
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
    "obtenerFestivalesResult"
})
@XmlRootElement(name = "ObtenerFestivalesResponse")
public class ObtenerFestivalesResponse {

    @XmlElementRef(name = "ObtenerFestivalesResult", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfFestival> obtenerFestivalesResult;

    /**
     * Gets the value of the obtenerFestivalesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFestival }{@code >}
     *     
     */
    public JAXBElement<ArrayOfFestival> getObtenerFestivalesResult() {
        return obtenerFestivalesResult;
    }

    /**
     * Sets the value of the obtenerFestivalesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFestival }{@code >}
     *     
     */
    public void setObtenerFestivalesResult(JAXBElement<ArrayOfFestival> value) {
        this.obtenerFestivalesResult = ((JAXBElement<ArrayOfFestival> ) value);
    }

}
