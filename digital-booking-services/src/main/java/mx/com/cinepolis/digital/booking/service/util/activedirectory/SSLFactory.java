package mx.com.cinepolis.digital.booking.service.util.activedirectory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aramirezg
 */
public class SSLFactory extends SSLSocketFactory
{

  private static final Logger LOG = LoggerFactory.getLogger( SSLFactory.class );
  private SSLSocketFactory factory;

  public SSLFactory()
  {
    try
    {

      SSLContext sslcontext = null;
      if( sslcontext == null )
      {
        sslcontext = SSLContext.getInstance( "TLS" );
        sslcontext.init( null, new TrustManager[] { new DummyTrustManager() }, new java.security.SecureRandom() );
      }

      factory = (SSLSocketFactory) sslcontext.getSocketFactory();

    }
    catch( Exception ex )
    {
      LOG.error( ex.getMessage(), ex );
    }
  }

  public static SocketFactory getDefault()
  {
    return new SSLFactory();
  }

  public Socket createSocket( Socket socket, String s, int i, boolean flag ) throws IOException
  {
    return factory.createSocket( socket, s, i, flag );
  }

  public Socket createSocket( InetAddress inaddr, int i, InetAddress inaddr1, int j ) throws IOException
  {
    return factory.createSocket( inaddr, i, inaddr1, j );
  }

  public Socket createSocket( InetAddress inaddr, int i ) throws IOException
  {
    return factory.createSocket( inaddr, i );
  }

  public Socket createSocket( String s, int i, InetAddress inaddr, int j ) throws IOException
  {
    return factory.createSocket( s, i, inaddr, j );
  }

  public Socket createSocket( String s, int i ) throws IOException
  {
    return factory.createSocket( s, i );
  }

  public String[] getDefaultCipherSuites()
  {
    return factory.getSupportedCipherSuites();
  }

  public String[] getSupportedCipherSuites()
  {
    return factory.getSupportedCipherSuites();
  }

}
