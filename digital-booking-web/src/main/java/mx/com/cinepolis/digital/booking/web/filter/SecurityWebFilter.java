package mx.com.cinepolis.digital.booking.web.filter;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB;

import org.apache.commons.lang.StringUtils;

/**
 * Servlet Filter implementation class WebFilter
 */
@WebFilter(filterName = "SecurityWebFilter", urlPatterns = { "/views/analysis/*", "/views/booking/*", "/views/data/*",
    "/views/home/*", "/views/reports/*", "/views/systemconfiguration/*" })
public class SecurityWebFilter implements Filter
{

  private static final String LOGIN_URL = "/views/login/login.do";
  private static final String REQUSTED_URL_ATTRIBUTE = "requstedUrl";
  private static final String USER_ATTRIBUTE = "user";
  private static final String HOME_URL = "/views/home/home.do";

  @EJB
  private ServiceSecurityIntegratorEJB serviceSecurityIntegratorEJB;

  /**
   * Default constructor.
   */
  public SecurityWebFilter()
  {
  }

  /**
   * @see Filter#destroy()
   */
  public void destroy()
  {
  }

  /**
   * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   */
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
      ServletException
  {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpSession session = req.getSession();

    UserTO userTO = (UserTO) session.getAttribute( USER_ATTRIBUTE );
    if( userTO != null )
    {
      if( isSystemMenuAllowed( userTO, getMenuUrl( req ) ) )
      {
        chain.doFilter( request, response );
      }
      else
      {
        redirectToUrl( response, req, HOME_URL );
      }
    }
    else
    {
      session.setAttribute( REQUSTED_URL_ATTRIBUTE, req.getRequestURI() );
      redirectToUrl( response, req, LOGIN_URL );
      return;
    }
  }

  private boolean isSystemMenuAllowed( UserTO userTO, String menuUrl )
  {
    return menuUrl.contains( HOME_URL )
        || serviceSecurityIntegratorEJB.isSystemMenuAllowed( menuUrl, userTO.getRoles() );
  }

  private String getMenuUrl( HttpServletRequest req )
  {
    return req.getRequestURI().replaceAll( req.getContextPath(), StringUtils.EMPTY );
  }

  private void redirectToUrl( ServletResponse response, HttpServletRequest req, String url ) throws IOException
  {
    HttpServletResponse res = (HttpServletResponse) response;
    res.sendRedirect( CinepolisUtils.buildStringUsingMutable( req.getContextPath(), url ) );
  }

  /**
   * @see Filter#init(FilterConfig)
   */
  public void init( FilterConfig fConfig ) throws ServletException
  {
  }

}
