
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHorariosPelicula complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHorariosPelicula">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HorariosPelicula" type="{http://schemas.datacontract.org/2004/07/APICinepolis}HorariosPelicula" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHorariosPelicula", propOrder = {
    "horariosPelicula"
})
public class ArrayOfHorariosPelicula {

    @XmlElement(name = "HorariosPelicula", nillable = true)
    protected List<HorariosPelicula> horariosPelicula;

    /**
     * Gets the value of the horariosPelicula property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the horariosPelicula property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHorariosPelicula().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HorariosPelicula }
     * 
     * 
     */
    public List<HorariosPelicula> getHorariosPelicula() {
        if (horariosPelicula == null) {
            horariosPelicula = new ArrayList<HorariosPelicula>();
        }
        return this.horariosPelicula;
    }

}
