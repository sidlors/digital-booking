
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfTipoHorarioFiltro;


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
 *         &lt;element name="ObtenerHorariosFiltroResult" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfTipoHorarioFiltro" minOccurs="0"/>
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
    "obtenerHorariosFiltroResult"
})
@XmlRootElement(name = "ObtenerHorariosFiltroResponse")
public class ObtenerHorariosFiltroResponse {

    @XmlElementRef(name = "ObtenerHorariosFiltroResult", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfTipoHorarioFiltro> obtenerHorariosFiltroResult;

    /**
     * Gets the value of the obtenerHorariosFiltroResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTipoHorarioFiltro }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTipoHorarioFiltro> getObtenerHorariosFiltroResult() {
        return obtenerHorariosFiltroResult;
    }

    /**
     * Sets the value of the obtenerHorariosFiltroResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTipoHorarioFiltro }{@code >}
     *     
     */
    public void setObtenerHorariosFiltroResult(JAXBElement<ArrayOfTipoHorarioFiltro> value) {
        this.obtenerHorariosFiltroResult = ((JAXBElement<ArrayOfTipoHorarioFiltro> ) value);
    }

}
