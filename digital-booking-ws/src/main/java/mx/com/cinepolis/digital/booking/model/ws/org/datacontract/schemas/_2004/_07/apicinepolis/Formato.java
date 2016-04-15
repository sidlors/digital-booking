
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Formato complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Formato">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HOCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdPeliculaVista" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Formato", propOrder = {
    "hoCode",
    "idPeliculaVista",
    "nombre"
})
@XmlSeeAlso({
    FormatoHorario.class
})
public class Formato {

    @XmlElementRef(name = "HOCode", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> hoCode;
    @XmlElementRef(name = "IdPeliculaVista", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> idPeliculaVista;
    @XmlElementRef(name = "Nombre", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> nombre;

    /**
     * Gets the value of the hoCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getHOCode() {
        return hoCode;
    }

    /**
     * Sets the value of the hoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setHOCode(JAXBElement<String> value) {
        this.hoCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idPeliculaVista property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdPeliculaVista() {
        return idPeliculaVista;
    }

    /**
     * Sets the value of the idPeliculaVista property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdPeliculaVista(JAXBElement<String> value) {
        this.idPeliculaVista = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setNombre(JAXBElement<String> value) {
        this.nombre = ((JAXBElement<String> ) value);
    }

}
