
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Complejo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Complejo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Direccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EsCompraAnticipada" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdCiudad" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdEstado" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdVista" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Latitud" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Longitud" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Complejo", propOrder = {
    "direccion",
    "esCompraAnticipada",
    "id",
    "idCiudad",
    "idEstado",
    "idVista",
    "latitud",
    "longitud",
    "nombre",
    "telefono"
})
public class Complejo {

    @XmlElementRef(name = "Direccion", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> direccion;
    @XmlElement(name = "EsCompraAnticipada")
    protected Boolean esCompraAnticipada;
    @XmlElement(name = "Id")
    protected Integer id;
    @XmlElement(name = "IdCiudad")
    protected Integer idCiudad;
    @XmlElement(name = "IdEstado")
    protected Integer idEstado;
    @XmlElementRef(name = "IdVista", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> idVista;
    @XmlElement(name = "Latitud")
    protected Double latitud;
    @XmlElement(name = "Longitud")
    protected Double longitud;
    @XmlElementRef(name = "Nombre", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> nombre;
    @XmlElementRef(name = "Telefono", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> telefono;

    /**
     * Gets the value of the direccion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDireccion() {
        return direccion;
    }

    /**
     * Sets the value of the direccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDireccion(JAXBElement<String> value) {
        this.direccion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the esCompraAnticipada property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsCompraAnticipada() {
        return esCompraAnticipada;
    }

    /**
     * Sets the value of the esCompraAnticipada property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsCompraAnticipada(Boolean value) {
        this.esCompraAnticipada = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the idCiudad property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdCiudad() {
        return idCiudad;
    }

    /**
     * Sets the value of the idCiudad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdCiudad(Integer value) {
        this.idCiudad = value;
    }

    /**
     * Gets the value of the idEstado property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdEstado() {
        return idEstado;
    }

    /**
     * Sets the value of the idEstado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdEstado(Integer value) {
        this.idEstado = value;
    }

    /**
     * Gets the value of the idVista property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdVista() {
        return idVista;
    }

    /**
     * Sets the value of the idVista property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdVista(JAXBElement<String> value) {
        this.idVista = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the latitud property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLatitud() {
        return latitud;
    }

    /**
     * Sets the value of the latitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLatitud(Double value) {
        this.latitud = value;
    }

    /**
     * Gets the value of the longitud property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getLongitud() {
        return longitud;
    }

    /**
     * Sets the value of the longitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setLongitud(Double value) {
        this.longitud = value;
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

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTelefono(JAXBElement<String> value) {
        this.telefono = ((JAXBElement<String> ) value);
    }

}
