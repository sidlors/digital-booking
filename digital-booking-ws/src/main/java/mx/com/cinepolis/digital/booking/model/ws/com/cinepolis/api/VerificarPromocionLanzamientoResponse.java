
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="VerificarPromocionLanzamientoResult" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "verificarPromocionLanzamientoResult"
})
@XmlRootElement(name = "VerificarPromocionLanzamientoResponse")
public class VerificarPromocionLanzamientoResponse {

    @XmlElement(name = "VerificarPromocionLanzamientoResult")
    protected Boolean verificarPromocionLanzamientoResult;

    /**
     * Gets the value of the verificarPromocionLanzamientoResult property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerificarPromocionLanzamientoResult() {
        return verificarPromocionLanzamientoResult;
    }

    /**
     * Sets the value of the verificarPromocionLanzamientoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerificarPromocionLanzamientoResult(Boolean value) {
        this.verificarPromocionLanzamientoResult = value;
    }

}
