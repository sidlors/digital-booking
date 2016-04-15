
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FormatoHorario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FormatoHorario">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/APICinepolis}Formato">
 *       &lt;sequence>
 *         &lt;element name="Horarios" type="{http://schemas.datacontract.org/2004/07/APICinepolis}ArrayOfHorario" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FormatoHorario", propOrder = {
    "horarios"
})
public class FormatoHorario
    extends Formato
{

    @XmlElementRef(name = "Horarios", namespace = "http://schemas.datacontract.org/2004/07/APICinepolis", type = JAXBElement.class)
    protected JAXBElement<ArrayOfHorario> horarios;

    /**
     * Gets the value of the horarios property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfHorario }{@code >}
     *     
     */
    public JAXBElement<ArrayOfHorario> getHorarios() {
        return horarios;
    }

    /**
     * Sets the value of the horarios property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfHorario }{@code >}
     *     
     */
    public void setHorarios(JAXBElement<ArrayOfHorario> value) {
        this.horarios = ((JAXBElement<ArrayOfHorario> ) value);
    }

}
