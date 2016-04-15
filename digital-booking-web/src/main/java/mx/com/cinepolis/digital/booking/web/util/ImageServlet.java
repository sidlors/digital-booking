package mx.com.cinepolis.digital.booking.web.util;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB;

/**
 * Clase que maneja las imagenes de película
 * @author kperez
 *
 */
public class ImageServlet extends HttpServlet
{
  private static final long serialVersionUID = 3951560548941902765L;

  @EJB
  private ServiceAdminMovieIntegratorEJB serviceAdminMovieIntegratorEJB;
  
  /** Content-type */
  public static final String CONTENT_TYPE = "Content-type";
  /** application/octet-stream */
  public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

  /**
   * Método que obtiene la imagen de una película
   * 
   * @param request Petición
   * @param response Respuesta
   * @throws IOException
   * @throws ServletException
   */
  public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
      IOException
  {
    String id = request.getParameter( "id" );
    if (!id.isEmpty()){
      FileTO file = serviceAdminMovieIntegratorEJB.findMovieImage( Long.valueOf( id ) );
      response.getOutputStream().write( file.getFile() );
      response.setHeader( CONTENT_TYPE, APPLICATION_OCTET_STREAM );
    }
  }
}
