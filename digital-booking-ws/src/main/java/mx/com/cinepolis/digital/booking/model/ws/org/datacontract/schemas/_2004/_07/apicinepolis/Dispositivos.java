
package mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Dispositivos.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Dispositivos">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Windows8"/>
 *     &lt;enumeration value="Windows81"/>
 *     &lt;enumeration value="WindowsPhone"/>
 *     &lt;enumeration value="WindowsPhone8"/>
 *     &lt;enumeration value="Android"/>
 *     &lt;enumeration value="AndroidUSA"/>
 *     &lt;enumeration value="iPhone"/>
 *     &lt;enumeration value="iosUSA"/>
 *     &lt;enumeration value="iPhone3"/>
 *     &lt;enumeration value="iPad3"/>
 *     &lt;enumeration value="iPad"/>
 *     &lt;enumeration value="NokiaBelle"/>
 *     &lt;enumeration value="NokiaS60"/>
 *     &lt;enumeration value="NokiaS40"/>
 *     &lt;enumeration value="SmartTV"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Dispositivos")
@XmlEnum
public enum Dispositivos {

    @XmlEnumValue("Windows8")
    WINDOWS_8("Windows8"),
    @XmlEnumValue("Windows81")
    WINDOWS_81("Windows81"),
    @XmlEnumValue("WindowsPhone")
    WINDOWS_PHONE("WindowsPhone"),
    @XmlEnumValue("WindowsPhone8")
    WINDOWS_PHONE_8("WindowsPhone8"),
    @XmlEnumValue("Android")
    ANDROID("Android"),
    @XmlEnumValue("AndroidUSA")
    ANDROID_USA("AndroidUSA"),
    @XmlEnumValue("iPhone")
    I_PHONE("iPhone"),
    @XmlEnumValue("iosUSA")
    IOS_USA("iosUSA"),
    @XmlEnumValue("iPhone3")
    I_PHONE_3("iPhone3"),
    @XmlEnumValue("iPad3")
    I_PAD_3("iPad3"),
    @XmlEnumValue("iPad")
    I_PAD("iPad"),
    @XmlEnumValue("NokiaBelle")
    NOKIA_BELLE("NokiaBelle"),
    @XmlEnumValue("NokiaS60")
    NOKIA_S_60("NokiaS60"),
    @XmlEnumValue("NokiaS40")
    NOKIA_S_40("NokiaS40"),
    @XmlEnumValue("SmartTV")
    SMART_TV("SmartTV");
    private final String value;

    Dispositivos(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Dispositivos fromValue(String v) {
        for (Dispositivos c: Dispositivos.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
