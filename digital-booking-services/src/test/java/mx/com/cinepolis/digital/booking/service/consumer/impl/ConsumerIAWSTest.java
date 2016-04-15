package mx.com.cinepolis.digital.booking.service.consumer.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfCiudad;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfDistribuidora;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.ArrayOfPelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Ciudad;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Distribuidora;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Pelicula;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.Consumo;
import mx.com.cinepolis.digital.booking.model.ws.org.tempuri.IConsumo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;

/**
 * Prueba de Conexion con el web service API de IA ya mapeado en el OSB local de Desarrollo
 * 
 * @author agustin.ramirez
 */
public class ConsumerIAWSTest
{

  /**
   * Prueba para obtener las ciudades
   */
  @Test
  public void testWSObtenerCiudades()
  {
    Consumo consumerService = new Consumo();
    IConsumo port = consumerService.getBasic();
    ArrayOfCiudad response = new ArrayOfCiudad();
    response = port.obtenerCiudades( null );
    System.out.println( "Ciudades Obtenidas =>" + ReflectionToStringBuilder.toString( response ) );

    List<Ciudad> ccities = response.getCiudad();
    for( Ciudad city : ccities )
    {
      System.out.println( "Ciudad =>" + ReflectionToStringBuilder.toString( city ) );
      System.out.println( "Nombre =>" + city.getNombre().getValue() );
    }

  }

  @Test
  public void testWSObtenerEstrenos()
  {
    Consumo consumerService = new Consumo();

    IConsumo port = consumerService.getBasic();
    ArrayOfPelicula a = port.obtenerPeliculasEstrenos( "1", null );
    for( Pelicula p : a.getPelicula() )
    {
      System.out.println( p.getTitulo().getValue() );
    }
  }

  @Test
  public void testWS()
  {
    Consumo consumerService = new Consumo();

    IConsumo port = consumerService.getBasic();
    ArrayOfDistribuidora a = port.obtenerDistribuidoras( null );
    for( Distribuidora d : a.getDistribuidora() )
    {
      System.out.println( d.getNombre().getValue() + "-" + d.getId() );
    }
  }

}
