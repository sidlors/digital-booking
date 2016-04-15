
package mx.com.cinepolis.digital.booking.model.ws.org.tempuri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "Consumo", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://api.cinepolis.com.mx/Consumo.svc?wsdl")
public class Consumo
    extends Service
{

    private final static URL CONSUMO_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(mx.com.cinepolis.digital.booking.model.ws.org.tempuri.Consumo.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = mx.com.cinepolis.digital.booking.model.ws.org.tempuri.Consumo.class.getResource(".");
            url = new URL(baseUrl, "http://api.cinepolis.com.mx/Consumo.svc?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://api.cinepolis.com.mx/Consumo.svc?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        CONSUMO_WSDL_LOCATION = url;
    }

    public Consumo(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Consumo() {
        super(CONSUMO_WSDL_LOCATION, new QName("http://tempuri.org/", "Consumo"));
    }

    /**
     * 
     * @return
     *     returns IConsumo
     */
    @WebEndpoint(name = "Basic")
    public IConsumo getBasic() {
        return super.getPort(new QName("http://tempuri.org/", "Basic"), IConsumo.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IConsumo
     */
    @WebEndpoint(name = "Basic")
    public IConsumo getBasic(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "Basic"), IConsumo.class, features);
    }

}
