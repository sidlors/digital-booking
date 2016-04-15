
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Pelicula complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Pelicula">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Actores" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Cartel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Clasificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Director" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Duracion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FechaEstreno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Festival" type="{http://schemas.datacontract.org/2004/07/APICinepolis}Festival" minOccurs="0"/>
 *         &lt;element name="Genero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdDistribuidora" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IdVista" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sinopsis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TituloOriginal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pelicula", propOrder = {
    "actores",
    "cartel",
    "clasificacion",
    "director",
    "duracion",
    "fechaEstreno",
    "festival",
    "genero",
    "id",
    "idDistribuidora",
    "idVista",
    "sinopsis",
    "tipo",
    "titulo",
    "tituloOriginal"
})
public class Pelicula {

    @XmlElementRef(name = "Actores", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> actores;
    @XmlElementRef(name = "Cartel", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> cartel;
    @XmlElementRef(name = "Clasificacion", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> clasificacion;
    @XmlElementRef(name = "Director", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> director;
    @XmlElementRef(name = "Duracion", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> duracion;
    @XmlElementRef(name = "FechaEstreno", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> fechaEstreno;
    @XmlElementRef(name = "Festival", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<Festival> festival;
    @XmlElementRef(name = "Genero", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> genero;
    @XmlElement(name = "Id")
    protected Integer id;
    @XmlElement(name = "IdDistribuidora")
    protected Integer idDistribuidora;
    @XmlElementRef(name = "IdVista", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> idVista;
    @XmlElementRef(name = "Sinopsis", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> sinopsis;
    @XmlElementRef(name = "Tipo", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> tipo;
    @XmlElementRef(name = "Titulo", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> titulo;
    @XmlElementRef(name = "TituloOriginal", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<String> tituloOriginal;

    /**
     * Gets the value of the actores property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getActores() {
        return actores;
    }

    /**
     * Sets the value of the actores property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setActores(JAXBElement<String> value) {
        this.actores = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the cartel property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCartel() {
        return cartel;
    }

    /**
     * Sets the value of the cartel property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCartel(JAXBElement<String> value) {
        this.cartel = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the clasificacion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getClasificacion() {
        return clasificacion;
    }

    /**
     * Sets the value of the clasificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setClasificacion(JAXBElement<String> value) {
        this.clasificacion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the director property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDirector() {
        return director;
    }

    /**
     * Sets the value of the director property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDirector(JAXBElement<String> value) {
        this.director = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the duracion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDuracion() {
        return duracion;
    }

    /**
     * Sets the value of the duracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDuracion(JAXBElement<String> value) {
        this.duracion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the fechaEstreno property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFechaEstreno() {
        return fechaEstreno;
    }

    /**
     * Sets the value of the fechaEstreno property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFechaEstreno(JAXBElement<String> value) {
        this.fechaEstreno = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the festival property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Festival }{@code >}
     *     
     */
    public JAXBElement<Festival> getFestival() {
        return festival;
    }

    /**
     * Sets the value of the festival property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Festival }{@code >}
     *     
     */
    public void setFestival(JAXBElement<Festival> value) {
        this.festival = ((JAXBElement<Festival> ) value);
    }

    /**
     * Gets the value of the genero property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGenero() {
        return genero;
    }

    /**
     * Sets the value of the genero property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGenero(JAXBElement<String> value) {
        this.genero = ((JAXBElement<String> ) value);
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
     * Gets the value of the idDistribuidora property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIdDistribuidora() {
        return idDistribuidora;
    }

    /**
     * Sets the value of the idDistribuidora property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIdDistribuidora(Integer value) {
        this.idDistribuidora = value;
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
     * Gets the value of the sinopsis property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSinopsis() {
        return sinopsis;
    }

    /**
     * Sets the value of the sinopsis property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSinopsis(JAXBElement<String> value) {
        this.sinopsis = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTipo(JAXBElement<String> value) {
        this.tipo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTitulo(JAXBElement<String> value) {
        this.titulo = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the tituloOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTituloOriginal() {
        return tituloOriginal;
    }

    /**
     * Sets the value of the tituloOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTituloOriginal(JAXBElement<String> value) {
        this.tituloOriginal = ((JAXBElement<String> ) value);
    }

}
