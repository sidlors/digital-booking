
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfHorario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfHorario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Horario" type="{http://schemas.datacontract.org/2004/07/APICinepolis}Horario" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfHorario", propOrder = {
    "horario"
})
public class ArrayOfHorario {

    @XmlElement(name = "Horario", nillable = true)
    protected List<Horario> horario;

    /**
     * Gets the value of the horario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the horario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHorario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Horario }
     * 
     * 
     */
    public List<Horario> getHorario() {
        if (horario == null) {
            horario = new ArrayList<Horario>();
        }
        return this.horario;
    }

}
