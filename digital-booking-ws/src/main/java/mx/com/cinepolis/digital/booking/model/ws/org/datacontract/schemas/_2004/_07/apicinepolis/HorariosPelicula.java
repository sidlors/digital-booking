
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HorariosPelicula complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HorariosPelicula">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Complejo" type="{http://schemas.datacontract.org/2004/07/APICinepolis}Complejo" minOccurs="0"/>
 *         &lt;element name="Formatos" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfFormatoHorario" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HorariosPelicula", propOrder = {
    "complejo",
    "formatos"
})
public class HorariosPelicula {

    @XmlElementRef(name = "Complejo", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<Complejo> complejo;
    @XmlElementRef(name = "Formatos", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<ArrayOfFormatoHorario> formatos;

    /**
     * Gets the value of the complejo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Complejo }{@code >}
     *     
     */
    public JAXBElement<Complejo> getComplejo() {
        return complejo;
    }

    /**
     * Sets the value of the complejo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Complejo }{@code >}
     *     
     */
    public void setComplejo(JAXBElement<Complejo> value) {
        this.complejo = ((JAXBElement<Complejo> ) value);
    }

    /**
     * Gets the value of the formatos property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFormatoHorario }{@code >}
     *     
     */
    public JAXBElement<ArrayOfFormatoHorario> getFormatos() {
        return formatos;
    }

    /**
     * Sets the value of the formatos property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfFormatoHorario }{@code >}
     *     
     */
    public void setFormatos(JAXBElement<ArrayOfFormatoHorario> value) {
        this.formatos = ((JAXBElement<ArrayOfFormatoHorario> ) value);
    }

}
