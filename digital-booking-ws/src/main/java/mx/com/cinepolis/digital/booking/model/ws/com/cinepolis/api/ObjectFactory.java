
package mx.com.cinepolis.digital.booking.model.ws.com.cinepolis.api;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfCiudad;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfComplejo;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfDistribuidora;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfEstado;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfFestival;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfFormato;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfHorariosPelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfHorariosPeliculaHorariosComplejoNokia;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfImagenGenerica;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfMultimedia;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPais;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPromocion;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPromocionMoviles;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfRelacionComplejoPelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfRuta;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfTarifa;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfTipoHorarioFiltro;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Version;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.cinepolis.api package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ObtenerRelacionesPeliculasComplejosResponseObtenerRelacionesPeliculasComplejosResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerRelacionesPeliculasComplejosResult");
    private final static QName _ObtenerMultimediaFichaTecnicaFormato_QNAME = new QName("http://api.cinepolis.com", "formato");
    private final static QName _ObtenerHistorialPeliculasFacebookIdsVista_QNAME = new QName("http://api.cinepolis.com", "idsVista");
    private final static QName _ObtenerHistorialPeliculasFacebookIdPais_QNAME = new QName("http://api.cinepolis.com", "idPais");
    private final static QName _ObtenerPromocionesFrontResponseObtenerPromocionesFrontResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPromocionesFrontResult");
    private final static QName _ObtenerInformacionVersionResponseObtenerInformacionVersionResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerInformacionVersionResult");
    private final static QName _ObtenerHorariosFiltroMargenTiempoResponseObtenerHorariosFiltroMargenTiempoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosFiltroMargenTiempoResult");
    private final static QName _ObtenerFormatosResponseObtenerFormatosResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerFormatosResult");
    private final static QName _ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME = new QName("http://api.cinepolis.com", "idsPaises");
    private final static QName _ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME = new QName("http://api.cinepolis.com", "idsComplejos");
    private final static QName _ObtenerPromocionesResponseObtenerPromocionesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPromocionesResult");
    private final static QName _ObtenerPaisesResponseObtenerPaisesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPaisesResult");
    private final static QName _ObtenerPeliculasFestivalesResponseObtenerPeliculasFestivalesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasFestivalesResult");
    private final static QName _ObtenerPeliculasEstrenoResponseObtenerPeliculasEstrenoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasEstrenoResult");
    private final static QName _ObtenerCiudadesPaisesResponseObtenerCiudadesPaisesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerCiudadesPaisesResult");
    private final static QName _ObtenerTarifasResponseObtenerTarifasResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerTarifasResult");
    private final static QName _ObtenerHorariosComplejoResponseObtenerHorariosComplejoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosComplejoResult");
    private final static QName _ObtenerComplejosIdsCiudades_QNAME = new QName("http://api.cinepolis.com", "idsCiudades");
    private final static QName _ObtenerHorariosFiltroResponseObtenerHorariosFiltroResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosFiltroResult");
    private final static QName _ObtenerMultimediaResponseObtenerMultimediaResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerMultimediaResult");
    private final static QName _ObtenerComplejosResponseObtenerComplejosResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerComplejosResult");
    private final static QName _ObtenerEstadosResponseObtenerEstadosResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerEstadosResult");
    private final static QName _ObtenerFestivalesResponseObtenerFestivalesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerFestivalesResult");
    private final static QName _RedencionPromocionLanzamientoResponseRedencionPromocionLanzamientoResult_QNAME = new QName("http://api.cinepolis.com", "RedencionPromocionLanzamientoResult");
    private final static QName _ObtenerPeliculasCarteleraDispositivoResponseObtenerPeliculasCarteleraDispositivoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasCarteleraDispositivoResult");
    private final static QName _ObtenerHistorialPeliculasFacebookResponseObtenerHistorialPeliculasFacebookResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHistorialPeliculasFacebookResult");
    private final static QName _ObtenerCiudadesPaisResponseObtenerCiudadesPaisResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerCiudadesPaisResult");
    private final static QName _ObtenerPeliculasEstenoDispositivoResponseObtenerPeliculasEstenoDispositivoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasEstenoDispositivoResult");
    private final static QName _ObtenerCiudadesResponseObtenerCiudadesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerCiudadesResult");
    private final static QName _RedencionPromocionLanzamientoStrIMEI_QNAME = new QName("http://api.cinepolis.com", "strIMEI");
    private final static QName _ObtenerMultimediaFichaTecnicaResponseObtenerMultimediaFichaTecnicaResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerMultimediaFichaTecnicaResult");
    private final static QName _ObtenerMultimediaDispositivoResponseObtenerMultimediaDispositivoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerMultimediaDispositivoResult");
    private final static QName _ObtenerDistribuidorasResponseObtenerDistribuidorasResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerDistribuidorasResult");
    private final static QName _ObtenerPeliculasEstrenosResponseObtenerPeliculasEstrenosResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasEstrenosResult");
    private final static QName _ObtenerHorariosPeliculaMargenTiempoResponseObtenerHorariosPeliculaMargenTiempoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosPeliculaMargenTiempoResult");
    private final static QName _ObtenerPeliculasCarteleraResponseObtenerPeliculasCarteleraResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasCarteleraResult");
    private final static QName _ObtenerHorariosPeliculaResponseObtenerHorariosPeliculaResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosPeliculaResult");
    private final static QName _ObtenerPromocionesMovilesResponseObtenerPromocionesMovilesResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPromocionesMovilesResult");
    private final static QName _ObtenerHorariosComplejoMargenTiempoResponseObtenerHorariosComplejoMargenTiempoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerHorariosComplejoMargenTiempoResult");
    private final static QName _ObtenerMultimediaFichaTecnicaDispositivoResponseObtenerMultimediaFichaTecnicaDispositivoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerMultimediaFichaTecnicaDispositivoResult");
    private final static QName _ObtenerPeliculasCarteleraPlasmaResponseObtenerPeliculasCarteleraPlasmaResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasCarteleraPlasmaResult");
    private final static QName _ObtenerPeliculasFestivalesDispositivoResponseObtenerPeliculasFestivalesDispositivoResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerPeliculasFestivalesDispositivoResult");
    private final static QName _ObtenerRutasResponseObtenerRutasResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerRutasResult");
    private final static QName _ObtenerImagenesGenericasResponseObtenerImagenesGenericasResult_QNAME = new QName("http://api.cinepolis.com", "ObtenerImagenesGenericasResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.cinepolis.api
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ObtenerMultimediaFichaTecnica }
     * 
     */
    public ObtenerMultimediaFichaTecnica createObtenerMultimediaFichaTecnica() {
        return new ObtenerMultimediaFichaTecnica();
    }

    /**
     * Create an instance of {@link ObtenerHistorialPeliculasFacebook }
     * 
     */
    public ObtenerHistorialPeliculasFacebook createObtenerHistorialPeliculasFacebook() {
        return new ObtenerHistorialPeliculasFacebook();
    }

    /**
     * Create an instance of {@link ObtenerInformacionVersionResponse }
     * 
     */
    public ObtenerInformacionVersionResponse createObtenerInformacionVersionResponse() {
        return new ObtenerInformacionVersionResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCarteleraPlasma }
     * 
     */
    public ObtenerPeliculasCarteleraPlasma createObtenerPeliculasCarteleraPlasma() {
        return new ObtenerPeliculasCarteleraPlasma();
    }

    /**
     * Create an instance of {@link ObtenerTarifas }
     * 
     */
    public ObtenerTarifas createObtenerTarifas() {
        return new ObtenerTarifas();
    }

    /**
     * Create an instance of {@link ObtenerPromocionesFront }
     * 
     */
    public ObtenerPromocionesFront createObtenerPromocionesFront() {
        return new ObtenerPromocionesFront();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasFestivalesResponse }
     * 
     */
    public ObtenerPeliculasFestivalesResponse createObtenerPeliculasFestivalesResponse() {
        return new ObtenerPeliculasFestivalesResponse();
    }

    /**
     * Create an instance of {@link ObtenerRutas }
     * 
     */
    public ObtenerRutas createObtenerRutas() {
        return new ObtenerRutas();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstreno }
     * 
     */
    public ObtenerPeliculasEstreno createObtenerPeliculasEstreno() {
        return new ObtenerPeliculasEstreno();
    }

    /**
     * Create an instance of {@link ObtenerHorariosComplejoResponse }
     * 
     */
    public ObtenerHorariosComplejoResponse createObtenerHorariosComplejoResponse() {
        return new ObtenerHorariosComplejoResponse();
    }

    /**
     * Create an instance of {@link ObtenerPaises }
     * 
     */
    public ObtenerPaises createObtenerPaises() {
        return new ObtenerPaises();
    }

    /**
     * Create an instance of {@link ObtenerHorariosFiltroMargenTiempo }
     * 
     */
    public ObtenerHorariosFiltroMargenTiempo createObtenerHorariosFiltroMargenTiempo() {
        return new ObtenerHorariosFiltroMargenTiempo();
    }

    /**
     * Create an instance of {@link ObtenerComplejos }
     * 
     */
    public ObtenerComplejos createObtenerComplejos() {
        return new ObtenerComplejos();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaResponse }
     * 
     */
    public ObtenerMultimediaResponse createObtenerMultimediaResponse() {
        return new ObtenerMultimediaResponse();
    }

    /**
     * Create an instance of {@link VerificarPromocionLanzamientoResponse }
     * 
     */
    public VerificarPromocionLanzamientoResponse createVerificarPromocionLanzamientoResponse() {
        return new VerificarPromocionLanzamientoResponse();
    }

    /**
     * Create an instance of {@link RedencionPromocionLanzamientoResponse }
     * 
     */
    public RedencionPromocionLanzamientoResponse createRedencionPromocionLanzamientoResponse() {
        return new RedencionPromocionLanzamientoResponse();
    }

    /**
     * Create an instance of {@link ObtenerEstados }
     * 
     */
    public ObtenerEstados createObtenerEstados() {
        return new ObtenerEstados();
    }

    /**
     * Create an instance of {@link ObtenerHistorialPeliculasFacebookResponse }
     * 
     */
    public ObtenerHistorialPeliculasFacebookResponse createObtenerHistorialPeliculasFacebookResponse() {
        return new ObtenerHistorialPeliculasFacebookResponse();
    }

    /**
     * Create an instance of {@link VerificarPromocionLanzamiento }
     * 
     */
    public VerificarPromocionLanzamiento createVerificarPromocionLanzamiento() {
        return new VerificarPromocionLanzamiento();
    }

    /**
     * Create an instance of {@link ObtenerCiudadesPaisResponse }
     * 
     */
    public ObtenerCiudadesPaisResponse createObtenerCiudadesPaisResponse() {
        return new ObtenerCiudadesPaisResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstenoDispositivoResponse }
     * 
     */
    public ObtenerPeliculasEstenoDispositivoResponse createObtenerPeliculasEstenoDispositivoResponse() {
        return new ObtenerPeliculasEstenoDispositivoResponse();
    }

    /**
     * Create an instance of {@link ObtenerCiudadesResponse }
     * 
     */
    public ObtenerCiudadesResponse createObtenerCiudadesResponse() {
        return new ObtenerCiudadesResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstrenosResponse }
     * 
     */
    public ObtenerPeliculasEstrenosResponse createObtenerPeliculasEstrenosResponse() {
        return new ObtenerPeliculasEstrenosResponse();
    }

    /**
     * Create an instance of {@link ObtenerDistribuidorasResponse }
     * 
     */
    public ObtenerDistribuidorasResponse createObtenerDistribuidorasResponse() {
        return new ObtenerDistribuidorasResponse();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaDispositivo }
     * 
     */
    public ObtenerMultimediaDispositivo createObtenerMultimediaDispositivo() {
        return new ObtenerMultimediaDispositivo();
    }

    /**
     * Create an instance of {@link ObtenerHorariosPeliculaMargenTiempoResponse }
     * 
     */
    public ObtenerHorariosPeliculaMargenTiempoResponse createObtenerHorariosPeliculaMargenTiempoResponse() {
        return new ObtenerHorariosPeliculaMargenTiempoResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCarteleraResponse }
     * 
     */
    public ObtenerPeliculasCarteleraResponse createObtenerPeliculasCarteleraResponse() {
        return new ObtenerPeliculasCarteleraResponse();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaFichaTecnicaDispositivo }
     * 
     */
    public ObtenerMultimediaFichaTecnicaDispositivo createObtenerMultimediaFichaTecnicaDispositivo() {
        return new ObtenerMultimediaFichaTecnicaDispositivo();
    }

    /**
     * Create an instance of {@link ObtenerHorariosPeliculaResponse }
     * 
     */
    public ObtenerHorariosPeliculaResponse createObtenerHorariosPeliculaResponse() {
        return new ObtenerHorariosPeliculaResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasFestivales }
     * 
     */
    public ObtenerPeliculasFestivales createObtenerPeliculasFestivales() {
        return new ObtenerPeliculasFestivales();
    }

    /**
     * Create an instance of {@link ObtenerDistribuidoras }
     * 
     */
    public ObtenerDistribuidoras createObtenerDistribuidoras() {
        return new ObtenerDistribuidoras();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstenoDispositivo }
     * 
     */
    public ObtenerPeliculasEstenoDispositivo createObtenerPeliculasEstenoDispositivo() {
        return new ObtenerPeliculasEstenoDispositivo();
    }

    /**
     * Create an instance of {@link ObtenerPromocionesMovilesResponse }
     * 
     */
    public ObtenerPromocionesMovilesResponse createObtenerPromocionesMovilesResponse() {
        return new ObtenerPromocionesMovilesResponse();
    }

    /**
     * Create an instance of {@link ObtenerHorariosFiltro }
     * 
     */
    public ObtenerHorariosFiltro createObtenerHorariosFiltro() {
        return new ObtenerHorariosFiltro();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaFichaTecnicaDispositivoResponse }
     * 
     */
    public ObtenerMultimediaFichaTecnicaDispositivoResponse createObtenerMultimediaFichaTecnicaDispositivoResponse() {
        return new ObtenerMultimediaFichaTecnicaDispositivoResponse();
    }

    /**
     * Create an instance of {@link ObtenerHorariosPeliculaMargenTiempo }
     * 
     */
    public ObtenerHorariosPeliculaMargenTiempo createObtenerHorariosPeliculaMargenTiempo() {
        return new ObtenerHorariosPeliculaMargenTiempo();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCarteleraPlasmaResponse }
     * 
     */
    public ObtenerPeliculasCarteleraPlasmaResponse createObtenerPeliculasCarteleraPlasmaResponse() {
        return new ObtenerPeliculasCarteleraPlasmaResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasFestivalesDispositivoResponse }
     * 
     */
    public ObtenerPeliculasFestivalesDispositivoResponse createObtenerPeliculasFestivalesDispositivoResponse() {
        return new ObtenerPeliculasFestivalesDispositivoResponse();
    }

    /**
     * Create an instance of {@link ObtenerCiudades }
     * 
     */
    public ObtenerCiudades createObtenerCiudades() {
        return new ObtenerCiudades();
    }

    /**
     * Create an instance of {@link ObtenerCiudadesPais }
     * 
     */
    public ObtenerCiudadesPais createObtenerCiudadesPais() {
        return new ObtenerCiudadesPais();
    }

    /**
     * Create an instance of {@link ObtenerRelacionesPeliculasComplejosResponse }
     * 
     */
    public ObtenerRelacionesPeliculasComplejosResponse createObtenerRelacionesPeliculasComplejosResponse() {
        return new ObtenerRelacionesPeliculasComplejosResponse();
    }

    /**
     * Create an instance of {@link ObtenerPromocionesFrontResponse }
     * 
     */
    public ObtenerPromocionesFrontResponse createObtenerPromocionesFrontResponse() {
        return new ObtenerPromocionesFrontResponse();
    }

    /**
     * Create an instance of {@link ObtenerHorariosFiltroMargenTiempoResponse }
     * 
     */
    public ObtenerHorariosFiltroMargenTiempoResponse createObtenerHorariosFiltroMargenTiempoResponse() {
        return new ObtenerHorariosFiltroMargenTiempoResponse();
    }

    /**
     * Create an instance of {@link ObtenerFormatosResponse }
     * 
     */
    public ObtenerFormatosResponse createObtenerFormatosResponse() {
        return new ObtenerFormatosResponse();
    }

    /**
     * Create an instance of {@link ObtenerRelacionesPeliculasComplejos }
     * 
     */
    public ObtenerRelacionesPeliculasComplejos createObtenerRelacionesPeliculasComplejos() {
        return new ObtenerRelacionesPeliculasComplejos();
    }

    /**
     * Create an instance of {@link ObtenerPromocionesResponse }
     * 
     */
    public ObtenerPromocionesResponse createObtenerPromocionesResponse() {
        return new ObtenerPromocionesResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstrenos }
     * 
     */
    public ObtenerPeliculasEstrenos createObtenerPeliculasEstrenos() {
        return new ObtenerPeliculasEstrenos();
    }

    /**
     * Create an instance of {@link ObtenerFormatos }
     * 
     */
    public ObtenerFormatos createObtenerFormatos() {
        return new ObtenerFormatos();
    }

    /**
     * Create an instance of {@link ObtenerPaisesResponse }
     * 
     */
    public ObtenerPaisesResponse createObtenerPaisesResponse() {
        return new ObtenerPaisesResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasEstrenoResponse }
     * 
     */
    public ObtenerPeliculasEstrenoResponse createObtenerPeliculasEstrenoResponse() {
        return new ObtenerPeliculasEstrenoResponse();
    }

    /**
     * Create an instance of {@link ObtenerPromocionesMoviles }
     * 
     */
    public ObtenerPromocionesMoviles createObtenerPromocionesMoviles() {
        return new ObtenerPromocionesMoviles();
    }

    /**
     * Create an instance of {@link ObtenerCiudadesPaisesResponse }
     * 
     */
    public ObtenerCiudadesPaisesResponse createObtenerCiudadesPaisesResponse() {
        return new ObtenerCiudadesPaisesResponse();
    }

    /**
     * Create an instance of {@link ObtenerTarifasResponse }
     * 
     */
    public ObtenerTarifasResponse createObtenerTarifasResponse() {
        return new ObtenerTarifasResponse();
    }

    /**
     * Create an instance of {@link ObtenerCiudadesPaises }
     * 
     */
    public ObtenerCiudadesPaises createObtenerCiudadesPaises() {
        return new ObtenerCiudadesPaises();
    }

    /**
     * Create an instance of {@link ObtenerHorariosFiltroResponse }
     * 
     */
    public ObtenerHorariosFiltroResponse createObtenerHorariosFiltroResponse() {
        return new ObtenerHorariosFiltroResponse();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCartelera }
     * 
     */
    public ObtenerPeliculasCartelera createObtenerPeliculasCartelera() {
        return new ObtenerPeliculasCartelera();
    }

    /**
     * Create an instance of {@link ObtenerComplejosResponse }
     * 
     */
    public ObtenerComplejosResponse createObtenerComplejosResponse() {
        return new ObtenerComplejosResponse();
    }

    /**
     * Create an instance of {@link ObtenerEstadosResponse }
     * 
     */
    public ObtenerEstadosResponse createObtenerEstadosResponse() {
        return new ObtenerEstadosResponse();
    }

    /**
     * Create an instance of {@link ObtenerFestivalesResponse }
     * 
     */
    public ObtenerFestivalesResponse createObtenerFestivalesResponse() {
        return new ObtenerFestivalesResponse();
    }

    /**
     * Create an instance of {@link ObtenerHorariosComplejo }
     * 
     */
    public ObtenerHorariosComplejo createObtenerHorariosComplejo() {
        return new ObtenerHorariosComplejo();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCarteleraDispositivoResponse }
     * 
     */
    public ObtenerPeliculasCarteleraDispositivoResponse createObtenerPeliculasCarteleraDispositivoResponse() {
        return new ObtenerPeliculasCarteleraDispositivoResponse();
    }

    /**
     * Create an instance of {@link ObtenerFestivales }
     * 
     */
    public ObtenerFestivales createObtenerFestivales() {
        return new ObtenerFestivales();
    }

    /**
     * Create an instance of {@link ObtenerInformacionVersion }
     * 
     */
    public ObtenerInformacionVersion createObtenerInformacionVersion() {
        return new ObtenerInformacionVersion();
    }

    /**
     * Create an instance of {@link RedencionPromocionLanzamiento }
     * 
     */
    public RedencionPromocionLanzamiento createRedencionPromocionLanzamiento() {
        return new RedencionPromocionLanzamiento();
    }

    /**
     * Create an instance of {@link ObtenerHorariosComplejoMargenTiempo }
     * 
     */
    public ObtenerHorariosComplejoMargenTiempo createObtenerHorariosComplejoMargenTiempo() {
        return new ObtenerHorariosComplejoMargenTiempo();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaFichaTecnicaResponse }
     * 
     */
    public ObtenerMultimediaFichaTecnicaResponse createObtenerMultimediaFichaTecnicaResponse() {
        return new ObtenerMultimediaFichaTecnicaResponse();
    }

    /**
     * Create an instance of {@link ObtenerMultimediaDispositivoResponse }
     * 
     */
    public ObtenerMultimediaDispositivoResponse createObtenerMultimediaDispositivoResponse() {
        return new ObtenerMultimediaDispositivoResponse();
    }

    /**
     * Create an instance of {@link ObtenerHorariosPelicula }
     * 
     */
    public ObtenerHorariosPelicula createObtenerHorariosPelicula() {
        return new ObtenerHorariosPelicula();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasCarteleraDispositivo }
     * 
     */
    public ObtenerPeliculasCarteleraDispositivo createObtenerPeliculasCarteleraDispositivo() {
        return new ObtenerPeliculasCarteleraDispositivo();
    }

    /**
     * Create an instance of {@link ObtenerHorariosComplejoMargenTiempoResponse }
     * 
     */
    public ObtenerHorariosComplejoMargenTiempoResponse createObtenerHorariosComplejoMargenTiempoResponse() {
        return new ObtenerHorariosComplejoMargenTiempoResponse();
    }

    /**
     * Create an instance of {@link ObtenerPromociones }
     * 
     */
    public ObtenerPromociones createObtenerPromociones() {
        return new ObtenerPromociones();
    }

    /**
     * Create an instance of {@link ObtenerMultimedia }
     * 
     */
    public ObtenerMultimedia createObtenerMultimedia() {
        return new ObtenerMultimedia();
    }

    /**
     * Create an instance of {@link ObtenerPeliculasFestivalesDispositivo }
     * 
     */
    public ObtenerPeliculasFestivalesDispositivo createObtenerPeliculasFestivalesDispositivo() {
        return new ObtenerPeliculasFestivalesDispositivo();
    }

    /**
     * Create an instance of {@link ObtenerImagenesGenericas }
     * 
     */
    public ObtenerImagenesGenericas createObtenerImagenesGenericas() {
        return new ObtenerImagenesGenericas();
    }

    /**
     * Create an instance of {@link ObtenerRutasResponse }
     * 
     */
    public ObtenerRutasResponse createObtenerRutasResponse() {
        return new ObtenerRutasResponse();
    }

    /**
     * Create an instance of {@link ObtenerImagenesGenericasResponse }
     * 
     */
    public ObtenerImagenesGenericasResponse createObtenerImagenesGenericasResponse() {
        return new ObtenerImagenesGenericasResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelacionComplejoPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerRelacionesPeliculasComplejosResult", scope = ObtenerRelacionesPeliculasComplejosResponse.class)
    public JAXBElement<ArrayOfRelacionComplejoPelicula> createObtenerRelacionesPeliculasComplejosResponseObtenerRelacionesPeliculasComplejosResult(ArrayOfRelacionComplejoPelicula value) {
        return new JAXBElement<ArrayOfRelacionComplejoPelicula>(_ObtenerRelacionesPeliculasComplejosResponseObtenerRelacionesPeliculasComplejosResult_QNAME, ArrayOfRelacionComplejoPelicula.class, ObtenerRelacionesPeliculasComplejosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerMultimediaFichaTecnica.class)
    public JAXBElement<String> createObtenerMultimediaFichaTecnicaFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerMultimediaFichaTecnica.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHistorialPeliculasFacebook.class)
    public JAXBElement<String> createObtenerHistorialPeliculasFacebookFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHistorialPeliculasFacebook.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsVista", scope = ObtenerHistorialPeliculasFacebook.class)
    public JAXBElement<String> createObtenerHistorialPeliculasFacebookIdsVista(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdsVista_QNAME, String.class, ObtenerHistorialPeliculasFacebook.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerHistorialPeliculasFacebook.class)
    public JAXBElement<String> createObtenerHistorialPeliculasFacebookIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerHistorialPeliculasFacebook.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPromocion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPromocionesFrontResult", scope = ObtenerPromocionesFrontResponse.class)
    public JAXBElement<ArrayOfPromocion> createObtenerPromocionesFrontResponseObtenerPromocionesFrontResult(ArrayOfPromocion value) {
        return new JAXBElement<ArrayOfPromocion>(_ObtenerPromocionesFrontResponseObtenerPromocionesFrontResult_QNAME, ArrayOfPromocion.class, ObtenerPromocionesFrontResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Version }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerInformacionVersionResult", scope = ObtenerInformacionVersionResponse.class)
    public JAXBElement<Version> createObtenerInformacionVersionResponseObtenerInformacionVersionResult(Version value) {
        return new JAXBElement<Version>(_ObtenerInformacionVersionResponseObtenerInformacionVersionResult_QNAME, Version.class, ObtenerInformacionVersionResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTipoHorarioFiltro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosFiltroMargenTiempoResult", scope = ObtenerHorariosFiltroMargenTiempoResponse.class)
    public JAXBElement<ArrayOfTipoHorarioFiltro> createObtenerHorariosFiltroMargenTiempoResponseObtenerHorariosFiltroMargenTiempoResult(ArrayOfTipoHorarioFiltro value) {
        return new JAXBElement<ArrayOfTipoHorarioFiltro>(_ObtenerHorariosFiltroMargenTiempoResponseObtenerHorariosFiltroMargenTiempoResult_QNAME, ArrayOfTipoHorarioFiltro.class, ObtenerHorariosFiltroMargenTiempoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfFormato }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerFormatosResult", scope = ObtenerFormatosResponse.class)
    public JAXBElement<ArrayOfFormato> createObtenerFormatosResponseObtenerFormatosResult(ArrayOfFormato value) {
        return new JAXBElement<ArrayOfFormato>(_ObtenerFormatosResponseObtenerFormatosResult_QNAME, ArrayOfFormato.class, ObtenerFormatosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerRelacionesPeliculasComplejos.class)
    public JAXBElement<String> createObtenerRelacionesPeliculasComplejosIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerRelacionesPeliculasComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerRelacionesPeliculasComplejos.class)
    public JAXBElement<String> createObtenerRelacionesPeliculasComplejosFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerRelacionesPeliculasComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerRelacionesPeliculasComplejos.class)
    public JAXBElement<String> createObtenerRelacionesPeliculasComplejosIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerRelacionesPeliculasComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPromocion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPromocionesResult", scope = ObtenerPromocionesResponse.class)
    public JAXBElement<ArrayOfPromocion> createObtenerPromocionesResponseObtenerPromocionesResult(ArrayOfPromocion value) {
        return new JAXBElement<ArrayOfPromocion>(_ObtenerPromocionesResponseObtenerPromocionesResult_QNAME, ArrayOfPromocion.class, ObtenerPromocionesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasEstrenos.class)
    public JAXBElement<String> createObtenerPeliculasEstrenosFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasEstrenos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasEstrenos.class)
    public JAXBElement<String> createObtenerPeliculasEstrenosIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasEstrenos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerTarifas.class)
    public JAXBElement<String> createObtenerTarifasFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerTarifas.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasCarteleraPlasma.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraPlasmaFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasCarteleraPlasma.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsVista", scope = ObtenerPeliculasCarteleraPlasma.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraPlasmaIdsVista(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdsVista_QNAME, String.class, ObtenerPeliculasCarteleraPlasma.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasCarteleraPlasma.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraPlasmaIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasCarteleraPlasma.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerFormatos.class)
    public JAXBElement<String> createObtenerFormatosFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerFormatos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPais }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPaisesResult", scope = ObtenerPaisesResponse.class)
    public JAXBElement<ArrayOfPais> createObtenerPaisesResponseObtenerPaisesResult(ArrayOfPais value) {
        return new JAXBElement<ArrayOfPais>(_ObtenerPaisesResponseObtenerPaisesResult_QNAME, ArrayOfPais.class, ObtenerPaisesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPromocionesFront.class)
    public JAXBElement<String> createObtenerPromocionesFrontFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPromocionesFront.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasFestivalesResult", scope = ObtenerPeliculasFestivalesResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasFestivalesResponseObtenerPeliculasFestivalesResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasFestivalesResponseObtenerPeliculasFestivalesResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasFestivalesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasEstrenoResult", scope = ObtenerPeliculasEstrenoResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasEstrenoResponseObtenerPeliculasEstrenoResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasEstrenoResponseObtenerPeliculasEstrenoResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasEstrenoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPromocionesMoviles.class)
    public JAXBElement<String> createObtenerPromocionesMovilesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPromocionesMoviles.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCiudad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerCiudadesPaisesResult", scope = ObtenerCiudadesPaisesResponse.class)
    public JAXBElement<ArrayOfCiudad> createObtenerCiudadesPaisesResponseObtenerCiudadesPaisesResult(ArrayOfCiudad value) {
        return new JAXBElement<ArrayOfCiudad>(_ObtenerCiudadesPaisesResponseObtenerCiudadesPaisesResult_QNAME, ArrayOfCiudad.class, ObtenerCiudadesPaisesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTarifa }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerTarifasResult", scope = ObtenerTarifasResponse.class)
    public JAXBElement<ArrayOfTarifa> createObtenerTarifasResponseObtenerTarifasResult(ArrayOfTarifa value) {
        return new JAXBElement<ArrayOfTarifa>(_ObtenerTarifasResponseObtenerTarifasResult_QNAME, ArrayOfTarifa.class, ObtenerTarifasResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerRutas.class)
    public JAXBElement<String> createObtenerRutasFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerRutas.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasEstreno.class)
    public JAXBElement<String> createObtenerPeliculasEstrenoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasEstreno.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasEstreno.class)
    public JAXBElement<String> createObtenerPeliculasEstrenoIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasEstreno.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfHorariosPeliculaHorariosComplejoNokia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosComplejoResult", scope = ObtenerHorariosComplejoResponse.class)
    public JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> createObtenerHorariosComplejoResponseObtenerHorariosComplejoResult(ArrayOfHorariosPeliculaHorariosComplejoNokia value) {
        return new JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia>(_ObtenerHorariosComplejoResponseObtenerHorariosComplejoResult_QNAME, ArrayOfHorariosPeliculaHorariosComplejoNokia.class, ObtenerHorariosComplejoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPaises.class)
    public JAXBElement<String> createObtenerPaisesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPaises.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerCiudadesPaises.class)
    public JAXBElement<String> createObtenerCiudadesPaisesIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerCiudadesPaises.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerCiudadesPaises.class)
    public JAXBElement<String> createObtenerCiudadesPaisesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerCiudadesPaises.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosFiltroMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosFiltroMargenTiempoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosFiltroMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosFiltroMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosFiltroMargenTiempoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosFiltroMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerComplejos.class)
    public JAXBElement<String> createObtenerComplejosFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerComplejos.class)
    public JAXBElement<String> createObtenerComplejosIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsCiudades", scope = ObtenerComplejos.class)
    public JAXBElement<String> createObtenerComplejosIdsCiudades(String value) {
        return new JAXBElement<String>(_ObtenerComplejosIdsCiudades_QNAME, String.class, ObtenerComplejos.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTipoHorarioFiltro }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosFiltroResult", scope = ObtenerHorariosFiltroResponse.class)
    public JAXBElement<ArrayOfTipoHorarioFiltro> createObtenerHorariosFiltroResponseObtenerHorariosFiltroResult(ArrayOfTipoHorarioFiltro value) {
        return new JAXBElement<ArrayOfTipoHorarioFiltro>(_ObtenerHorariosFiltroResponseObtenerHorariosFiltroResult_QNAME, ArrayOfTipoHorarioFiltro.class, ObtenerHorariosFiltroResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerPeliculasCartelera.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerPeliculasCartelera.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasCartelera.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasCartelera.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerPeliculasCartelera.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerPeliculasCartelera.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultimedia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerMultimediaResult", scope = ObtenerMultimediaResponse.class)
    public JAXBElement<ArrayOfMultimedia> createObtenerMultimediaResponseObtenerMultimediaResult(ArrayOfMultimedia value) {
        return new JAXBElement<ArrayOfMultimedia>(_ObtenerMultimediaResponseObtenerMultimediaResult_QNAME, ArrayOfMultimedia.class, ObtenerMultimediaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfComplejo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerComplejosResult", scope = ObtenerComplejosResponse.class)
    public JAXBElement<ArrayOfComplejo> createObtenerComplejosResponseObtenerComplejosResult(ArrayOfComplejo value) {
        return new JAXBElement<ArrayOfComplejo>(_ObtenerComplejosResponseObtenerComplejosResult_QNAME, ArrayOfComplejo.class, ObtenerComplejosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfEstado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerEstadosResult", scope = ObtenerEstadosResponse.class)
    public JAXBElement<ArrayOfEstado> createObtenerEstadosResponseObtenerEstadosResult(ArrayOfEstado value) {
        return new JAXBElement<ArrayOfEstado>(_ObtenerEstadosResponseObtenerEstadosResult_QNAME, ArrayOfEstado.class, ObtenerEstadosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfFestival }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerFestivalesResult", scope = ObtenerFestivalesResponse.class)
    public JAXBElement<ArrayOfFestival> createObtenerFestivalesResponseObtenerFestivalesResult(ArrayOfFestival value) {
        return new JAXBElement<ArrayOfFestival>(_ObtenerFestivalesResponseObtenerFestivalesResult_QNAME, ArrayOfFestival.class, ObtenerFestivalesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "RedencionPromocionLanzamientoResult", scope = RedencionPromocionLanzamientoResponse.class)
    public JAXBElement<String> createRedencionPromocionLanzamientoResponseRedencionPromocionLanzamientoResult(String value) {
        return new JAXBElement<String>(_RedencionPromocionLanzamientoResponseRedencionPromocionLanzamientoResult_QNAME, String.class, RedencionPromocionLanzamientoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerHorariosComplejo.class)
    public JAXBElement<String> createObtenerHorariosComplejoIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerHorariosComplejo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosComplejo.class)
    public JAXBElement<String> createObtenerHorariosComplejoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosComplejo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosComplejo.class)
    public JAXBElement<String> createObtenerHorariosComplejoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosComplejo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerFestivales.class)
    public JAXBElement<String> createObtenerFestivalesIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerFestivales.class)
    public JAXBElement<String> createObtenerFestivalesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerFestivales.class)
    public JAXBElement<String> createObtenerFestivalesIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasCarteleraDispositivoResult", scope = ObtenerPeliculasCarteleraDispositivoResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasCarteleraDispositivoResponseObtenerPeliculasCarteleraDispositivoResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasCarteleraDispositivoResponseObtenerPeliculasCarteleraDispositivoResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasCarteleraDispositivoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHistorialPeliculasFacebookResult", scope = ObtenerHistorialPeliculasFacebookResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerHistorialPeliculasFacebookResponseObtenerHistorialPeliculasFacebookResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerHistorialPeliculasFacebookResponseObtenerHistorialPeliculasFacebookResult_QNAME, ArrayOfPelicula.class, ObtenerHistorialPeliculasFacebookResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerEstados.class)
    public JAXBElement<String> createObtenerEstadosFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerEstados.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerInformacionVersion.class)
    public JAXBElement<String> createObtenerInformacionVersionFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerInformacionVersion.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = VerificarPromocionLanzamiento.class)
    public JAXBElement<String> createVerificarPromocionLanzamientoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, VerificarPromocionLanzamiento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCiudad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerCiudadesPaisResult", scope = ObtenerCiudadesPaisResponse.class)
    public JAXBElement<ArrayOfCiudad> createObtenerCiudadesPaisResponseObtenerCiudadesPaisResult(ArrayOfCiudad value) {
        return new JAXBElement<ArrayOfCiudad>(_ObtenerCiudadesPaisResponseObtenerCiudadesPaisResult_QNAME, ArrayOfCiudad.class, ObtenerCiudadesPaisResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasEstenoDispositivoResult", scope = ObtenerPeliculasEstenoDispositivoResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasEstenoDispositivoResponseObtenerPeliculasEstenoDispositivoResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasEstenoDispositivoResponseObtenerPeliculasEstenoDispositivoResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasEstenoDispositivoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCiudad }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerCiudadesResult", scope = ObtenerCiudadesResponse.class)
    public JAXBElement<ArrayOfCiudad> createObtenerCiudadesResponseObtenerCiudadesResult(ArrayOfCiudad value) {
        return new JAXBElement<ArrayOfCiudad>(_ObtenerCiudadesResponseObtenerCiudadesResult_QNAME, ArrayOfCiudad.class, ObtenerCiudadesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerHorariosComplejoMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosComplejoMargenTiempoIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerHorariosComplejoMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosComplejoMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosComplejoMargenTiempoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosComplejoMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosComplejoMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosComplejoMargenTiempoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosComplejoMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = RedencionPromocionLanzamiento.class)
    public JAXBElement<String> createRedencionPromocionLanzamientoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, RedencionPromocionLanzamiento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "strIMEI", scope = RedencionPromocionLanzamiento.class)
    public JAXBElement<String> createRedencionPromocionLanzamientoStrIMEI(String value) {
        return new JAXBElement<String>(_RedencionPromocionLanzamientoStrIMEI_QNAME, String.class, RedencionPromocionLanzamiento.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultimedia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerMultimediaFichaTecnicaResult", scope = ObtenerMultimediaFichaTecnicaResponse.class)
    public JAXBElement<ArrayOfMultimedia> createObtenerMultimediaFichaTecnicaResponseObtenerMultimediaFichaTecnicaResult(ArrayOfMultimedia value) {
        return new JAXBElement<ArrayOfMultimedia>(_ObtenerMultimediaFichaTecnicaResponseObtenerMultimediaFichaTecnicaResult_QNAME, ArrayOfMultimedia.class, ObtenerMultimediaFichaTecnicaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultimedia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerMultimediaDispositivoResult", scope = ObtenerMultimediaDispositivoResponse.class)
    public JAXBElement<ArrayOfMultimedia> createObtenerMultimediaDispositivoResponseObtenerMultimediaDispositivoResult(ArrayOfMultimedia value) {
        return new JAXBElement<ArrayOfMultimedia>(_ObtenerMultimediaDispositivoResponseObtenerMultimediaDispositivoResult_QNAME, ArrayOfMultimedia.class, ObtenerMultimediaDispositivoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerMultimediaDispositivo.class)
    public JAXBElement<String> createObtenerMultimediaDispositivoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerMultimediaDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDistribuidora }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerDistribuidorasResult", scope = ObtenerDistribuidorasResponse.class)
    public JAXBElement<ArrayOfDistribuidora> createObtenerDistribuidorasResponseObtenerDistribuidorasResult(ArrayOfDistribuidora value) {
        return new JAXBElement<ArrayOfDistribuidora>(_ObtenerDistribuidorasResponseObtenerDistribuidorasResult_QNAME, ArrayOfDistribuidora.class, ObtenerDistribuidorasResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasEstrenosResult", scope = ObtenerPeliculasEstrenosResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasEstrenosResponseObtenerPeliculasEstrenosResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasEstrenosResponseObtenerPeliculasEstrenosResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasEstrenosResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfHorariosPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosPeliculaMargenTiempoResult", scope = ObtenerHorariosPeliculaMargenTiempoResponse.class)
    public JAXBElement<ArrayOfHorariosPelicula> createObtenerHorariosPeliculaMargenTiempoResponseObtenerHorariosPeliculaMargenTiempoResult(ArrayOfHorariosPelicula value) {
        return new JAXBElement<ArrayOfHorariosPelicula>(_ObtenerHorariosPeliculaMargenTiempoResponseObtenerHorariosPeliculaMargenTiempoResult_QNAME, ArrayOfHorariosPelicula.class, ObtenerHorariosPeliculaMargenTiempoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosPelicula.class)
    public JAXBElement<String> createObtenerHorariosPeliculaFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosPelicula.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosPelicula.class)
    public JAXBElement<String> createObtenerHorariosPeliculaIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosPelicula.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasCarteleraResult", scope = ObtenerPeliculasCarteleraResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasCarteleraResponseObtenerPeliculasCarteleraResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasCarteleraResponseObtenerPeliculasCarteleraResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasCarteleraResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerMultimediaFichaTecnicaDispositivo.class)
    public JAXBElement<String> createObtenerMultimediaFichaTecnicaDispositivoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerMultimediaFichaTecnicaDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfHorariosPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosPeliculaResult", scope = ObtenerHorariosPeliculaResponse.class)
    public JAXBElement<ArrayOfHorariosPelicula> createObtenerHorariosPeliculaResponseObtenerHorariosPeliculaResult(ArrayOfHorariosPelicula value) {
        return new JAXBElement<ArrayOfHorariosPelicula>(_ObtenerHorariosPeliculaResponseObtenerHorariosPeliculaResult_QNAME, ArrayOfHorariosPelicula.class, ObtenerHorariosPeliculaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasFestivales.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerPeliculasFestivales.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerPeliculasFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasFestivales.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasFestivales.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasEstenoDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasEstenoDispositivoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasEstenoDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasEstenoDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasEstenoDispositivoIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasEstenoDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerDistribuidoras.class)
    public JAXBElement<String> createObtenerDistribuidorasFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerDistribuidoras.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsPaises", scope = ObtenerPeliculasCarteleraDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraDispositivoIdsPaises(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsPaises_QNAME, String.class, ObtenerPeliculasCarteleraDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasCarteleraDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraDispositivoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasCarteleraDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerPeliculasCarteleraDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasCarteleraDispositivoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerPeliculasCarteleraDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPromocionMoviles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPromocionesMovilesResult", scope = ObtenerPromocionesMovilesResponse.class)
    public JAXBElement<ArrayOfPromocionMoviles> createObtenerPromocionesMovilesResponseObtenerPromocionesMovilesResult(ArrayOfPromocionMoviles value) {
        return new JAXBElement<ArrayOfPromocionMoviles>(_ObtenerPromocionesMovilesResponseObtenerPromocionesMovilesResult_QNAME, ArrayOfPromocionMoviles.class, ObtenerPromocionesMovilesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfHorariosPeliculaHorariosComplejoNokia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerHorariosComplejoMargenTiempoResult", scope = ObtenerHorariosComplejoMargenTiempoResponse.class)
    public JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia> createObtenerHorariosComplejoMargenTiempoResponseObtenerHorariosComplejoMargenTiempoResult(ArrayOfHorariosPeliculaHorariosComplejoNokia value) {
        return new JAXBElement<ArrayOfHorariosPeliculaHorariosComplejoNokia>(_ObtenerHorariosComplejoMargenTiempoResponseObtenerHorariosComplejoMargenTiempoResult_QNAME, ArrayOfHorariosPeliculaHorariosComplejoNokia.class, ObtenerHorariosComplejoMargenTiempoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPromociones.class)
    public JAXBElement<String> createObtenerPromocionesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPromociones.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosFiltro.class)
    public JAXBElement<String> createObtenerHorariosFiltroFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosFiltro.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosFiltro.class)
    public JAXBElement<String> createObtenerHorariosFiltroIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosFiltro.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultimedia }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerMultimediaFichaTecnicaDispositivoResult", scope = ObtenerMultimediaFichaTecnicaDispositivoResponse.class)
    public JAXBElement<ArrayOfMultimedia> createObtenerMultimediaFichaTecnicaDispositivoResponseObtenerMultimediaFichaTecnicaDispositivoResult(ArrayOfMultimedia value) {
        return new JAXBElement<ArrayOfMultimedia>(_ObtenerMultimediaFichaTecnicaDispositivoResponseObtenerMultimediaFichaTecnicaDispositivoResult_QNAME, ArrayOfMultimedia.class, ObtenerMultimediaFichaTecnicaDispositivoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerMultimedia.class)
    public JAXBElement<String> createObtenerMultimediaFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerMultimedia.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerHorariosPeliculaMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosPeliculaMargenTiempoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerHorariosPeliculaMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerHorariosPeliculaMargenTiempo.class)
    public JAXBElement<String> createObtenerHorariosPeliculaMargenTiempoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerHorariosPeliculaMargenTiempo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerPeliculasFestivalesDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesDispositivoFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerPeliculasFestivalesDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idsComplejos", scope = ObtenerPeliculasFestivalesDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesDispositivoIdsComplejos(String value) {
        return new JAXBElement<String>(_ObtenerRelacionesPeliculasComplejosIdsComplejos_QNAME, String.class, ObtenerPeliculasFestivalesDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "idPais", scope = ObtenerPeliculasFestivalesDispositivo.class)
    public JAXBElement<String> createObtenerPeliculasFestivalesDispositivoIdPais(String value) {
        return new JAXBElement<String>(_ObtenerHistorialPeliculasFacebookIdPais_QNAME, String.class, ObtenerPeliculasFestivalesDispositivo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasCarteleraPlasmaResult", scope = ObtenerPeliculasCarteleraPlasmaResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasCarteleraPlasmaResponseObtenerPeliculasCarteleraPlasmaResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasCarteleraPlasmaResponseObtenerPeliculasCarteleraPlasmaResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasCarteleraPlasmaResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfPelicula }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerPeliculasFestivalesDispositivoResult", scope = ObtenerPeliculasFestivalesDispositivoResponse.class)
    public JAXBElement<ArrayOfPelicula> createObtenerPeliculasFestivalesDispositivoResponseObtenerPeliculasFestivalesDispositivoResult(ArrayOfPelicula value) {
        return new JAXBElement<ArrayOfPelicula>(_ObtenerPeliculasFestivalesDispositivoResponseObtenerPeliculasFestivalesDispositivoResult_QNAME, ArrayOfPelicula.class, ObtenerPeliculasFestivalesDispositivoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerImagenesGenericas.class)
    public JAXBElement<String> createObtenerImagenesGenericasFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerImagenesGenericas.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRuta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerRutasResult", scope = ObtenerRutasResponse.class)
    public JAXBElement<ArrayOfRuta> createObtenerRutasResponseObtenerRutasResult(ArrayOfRuta value) {
        return new JAXBElement<ArrayOfRuta>(_ObtenerRutasResponseObtenerRutasResult_QNAME, ArrayOfRuta.class, ObtenerRutasResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerCiudades.class)
    public JAXBElement<String> createObtenerCiudadesFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerCiudades.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfImagenGenerica }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "ObtenerImagenesGenericasResult", scope = ObtenerImagenesGenericasResponse.class)
    public JAXBElement<ArrayOfImagenGenerica> createObtenerImagenesGenericasResponseObtenerImagenesGenericasResult(ArrayOfImagenGenerica value) {
        return new JAXBElement<ArrayOfImagenGenerica>(_ObtenerImagenesGenericasResponseObtenerImagenesGenericasResult_QNAME, ArrayOfImagenGenerica.class, ObtenerImagenesGenericasResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.cinepolis.com", name = "formato", scope = ObtenerCiudadesPais.class)
    public JAXBElement<String> createObtenerCiudadesPaisFormato(String value) {
        return new JAXBElement<String>(_ObtenerMultimediaFichaTecnicaFormato_QNAME, String.class, ObtenerCiudadesPais.class, value);
    }

}
