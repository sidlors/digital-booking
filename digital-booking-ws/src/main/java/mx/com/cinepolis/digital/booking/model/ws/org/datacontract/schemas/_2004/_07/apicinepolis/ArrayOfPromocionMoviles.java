
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfPromocionMoviles complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfPromocionMoviles">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PromocionMoviles" type="{http://schemas.datacontract.org/2004/07/APICinepolis}PromocionMoviles" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPromocionMoviles", propOrder = {
    "promocionMoviles"
})
public class ArrayOfPromocionMoviles {

    @XmlElement(name = "PromocionMoviles", nillable = true)
    protected List<PromocionMoviles> promocionMoviles;

    /**
     * Gets the value of the promocionMoviles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the promocionMoviles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPromocionMoviles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PromocionMoviles }
     * 
     * 
     */
    public List<PromocionMoviles> getPromocionMoviles() {
        if (promocionMoviles == null) {
            promocionMoviles = new ArrayList<PromocionMoviles>();
        }
        return this.promocionMoviles;
    }

}
