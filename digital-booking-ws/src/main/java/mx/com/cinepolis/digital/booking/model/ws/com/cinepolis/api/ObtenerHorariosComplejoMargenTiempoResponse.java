
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfHorariosPeliculaHorariosComplejoNokia;


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
 *         &lt;element name="ObtenerHorariosComplejoMargenTiempoResult" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfHorariosPelicula.HorariosComplejoNokia" minOccurs="0"/>
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
    "obtenerHorariosComplejoMargenTiempoResult"
})
@XmlRootElement(name = "ObtenerHorariosComplejoMargenTiempoResponse")
public class ObtenerHorariosComplejoMargenTiempoResponse {

    @XmlElementRef(name = "ObtenerHorariosComplejoMargenTiempoResult", namespace = "http://api.cinepolis.com", type = JAXBElement.class)
    protected JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> obtenerHorariosComplejoMargenTiempoResult;

    /**
     * Gets the value of the obtenerHorariosComplejoMargenTiempoResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfHorariosPeliculaHorariosComplejoNokia }{@code >}
     *     
     */
    public JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> getObtenerHorariosComplejoMargenTiempoResult() {
        return obtenerHorariosComplejoMargenTiempoResult;
    }

    /**
     * Sets the value of the obtenerHorariosComplejoMargenTiempoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfHorariosPeliculaHorariosComplejoNokia }{@code >}
     *     
     */
    public void setObtenerHorariosComplejoMargenTiempoResult(JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> value) {
        this.obtenerHorariosComplejoMargenTiempoResult = ((JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> ) value);
    }

}
