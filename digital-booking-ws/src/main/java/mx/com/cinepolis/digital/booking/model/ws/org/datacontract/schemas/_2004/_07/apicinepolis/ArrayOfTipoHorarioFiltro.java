
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTipoHorarioFiltro complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTipoHorarioFiltro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoHorarioFiltro" type="{http://schemas.datacontract.org/2004/07/APICinepolis}TipoHorarioFiltro" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTipoHorarioFiltro", propOrder = {
    "tipoHorarioFiltro"
})
public class ArrayOfTipoHorarioFiltro {

    @XmlElement(name = "TipoHorarioFiltro", nillable = true)
    protected List<TipoHorarioFiltro> tipoHorarioFiltro;

    /**
     * Gets the value of the tipoHorarioFiltro property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipoHorarioFiltro property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipoHorarioFiltro().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoHorarioFiltro }
     * 
     * 
     */
    public List<TipoHorarioFiltro> getTipoHorarioFiltro() {
        if (tipoHorarioFiltro == null) {
            tipoHorarioFiltro = new ArrayList<TipoHorarioFiltro>();
        }
        return this.tipoHorarioFiltro;
    }

}
