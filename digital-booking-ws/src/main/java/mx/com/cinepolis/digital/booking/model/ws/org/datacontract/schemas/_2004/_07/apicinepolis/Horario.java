
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Horario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Horario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EsReservaPermitida" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Fecha" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Hora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdShowtime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sala" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sello" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Horario", propOrder = {
    "esReservaPermitida",
    "fecha",
    "fechaHasta",
    "hora",
    "idShowtime",
    "sala",
    "sello"
})
public class Horario {

    @XmlElement(name = "EsReservaPermitida")
    protected Boolean esReservaPermitida;
    @XmlElementRef(name = "Fecha", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> fecha;
    @XmlElement(name = "FechaHasta")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHasta;
    @XmlElementRef(name = "Hora", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> hora;
    @XmlElementRef(name = "IdShowtime", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> idShowtime;
    @XmlElementRef(name = "Sala", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> sala;
    @XmlElementRef(name = "Sello", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> sello;

    /**
     * Gets the value of the esReservaPermitida property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsReservaPermitida() {
        return esReservaPermitida;
    }

    /**
     * Sets the value of the esReservaPermitida property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsReservaPermitida(Boolean value) {
        this.esReservaPermitida = value;
    }

    /**
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFecha(JAXBElement<String> value) {
        this.fecha = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the fechaHasta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Sets the value of the fechaHasta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHasta(XMLGregorianCalendar value) {
        this.fechaHasta = value;
    }

    /**
     * Gets the value of the hora property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getHora() {
        return hora;
    }

    /**
     * Sets the value of the hora property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setHora(JAXBElement<String> value) {
        this.hora = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idShowtime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdShowtime() {
        return idShowtime;
    }

    /**
     * Sets the value of the idShowtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdShowtime(JAXBElement<String> value) {
        this.idShowtime = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sala property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSala() {
        return sala;
    }

    /**
     * Sets the value of the sala property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSala(JAXBElement<String> value) {
        this.sala = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sello property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSello() {
        return sello;
    }

    /**
     * Sets the value of the sello property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSello(JAXBElement<String> value) {
        this.sello = ((JAXBElement<String> ) value);
    }

}
