package mx.com.cinepolis.digital.booking.web.filter;

import java.io.IOException;

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

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * Servlet Filter implementation class WebFilter
 */
@WebFilter(filterName = "LoginWebFilter", urlPatterns = { "/views/login/*" })
public class LoginWebFilter implements Filter
{

  private static final String HOME_URL = "/views/home/home.do";
  private static final String USER_ATTRIBUTE = "user";

  /**
   * Default constructor.
   */
  public LoginWebFilter()
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

    if( session.getAttribute( USER_ATTRIBUTE ) == null )
    {
      chain.doFilter( request, response );
    }
    else
    {
      HttpServletResponse res = (HttpServletResponse) response;
      res.sendRedirect( CinepolisUtils.buildStringUsingMutable( req.getContextPath(), HOME_URL ) );
      return;
    }
  }

  /**
   * @see Filter#init(FilterConfig)
   */
  public void init( FilterConfig fConfig ) throws ServletException
  {
  }

}
