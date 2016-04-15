
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPromocionMoviles;


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
 *         &lt;element name="ObtenerPromocionesMovilesResult" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfPromocionMoviles" minOccurs="0"/>
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
    "obtenerPromocionesMovilesResult"
})
@XmlRootElement(name = "ObtenerPromocionesMovilesResponse")
public class ObtenerPromocionesMovilesResponse {

    @XmlElementRef(name = "ObtenerPromocionesMovilesResult", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfPromocionMoviles> obtenerPromocionesMovilesResult;

    /**
     * Gets the value of the obtenerPromocionesMovilesResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPromocionMoviles }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPromocionMoviles> getObtenerPromocionesMovilesResult() {
        return obtenerPromocionesMovilesResult;
    }

    /**
     * Sets the value of the obtenerPromocionesMovilesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPromocionMoviles }{@code >}
     *     
     */
    public void setObtenerPromocionesMovilesResult(JAXBElement<ArrayOfPromocionMoviles> value) {
        this.obtenerPromocionesMovilesResult = ((JAXBElement<ArrayOfPromocionMoviles> ) value);
    }

}
