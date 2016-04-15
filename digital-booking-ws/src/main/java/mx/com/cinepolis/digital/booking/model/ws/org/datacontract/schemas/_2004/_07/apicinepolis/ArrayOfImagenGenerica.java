
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfImagenGenerica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfImagenGenerica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ImagenGenerica" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ImagenGenerica" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfImagenGenerica", propOrder = {
    "imagenGenerica"
})
public class ArrayOfImagenGenerica {

    @XmlElement(name = "ImagenGenerica", nillable = true)
    protected List<ImagenGenerica> imagenGenerica;

    /**
     * Gets the value of the imagenGenerica property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the imagenGenerica property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImagenGenerica().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImagenGenerica }
     * 
     * 
     */
    public List<ImagenGenerica> getImagenGenerica() {
        if (imagenGenerica == null) {
            imagenGenerica = new ArrayList<ImagenGenerica>();
        }
        return this.imagenGenerica;
    }

}
