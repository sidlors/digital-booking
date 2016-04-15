package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.EmailTemplateDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailTemplateDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.EmailTemplateDAO}
 * 
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class EmailTemplateDAOImpl extends AbstractBaseDAO<EmailTemplateDO> implements EmailTemplateDAO
{
  private static final String EMAIL_TYPE_ID = "emailTypeId";
  private static final String REGION_ID = "regionId";
  private static final String FIND_BY_REGION_AND_EMAIL_TYPE_NAMED_QUERY = "EmailTemplateDO.namedQuery.findByRegionAndEmailType";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /**
   * Constructor default
   */
  public EmailTemplateDAOImpl()
  {
    super( EmailTemplateDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailTemplateDO findByRegionAndEmailType( Long regionId, int emailTypeId )
  {
    TypedQuery<EmailTemplateDO> q = getEntityManager().createNamedQuery( FIND_BY_REGION_AND_EMAIL_TYPE_NAMED_QUERY,
      EmailTemplateDO.class );
    q.setParameter( REGION_ID, regionId );
    q.setParameter( EMAIL_TYPE_ID, emailTypeId );
    return CinepolisUtils.findFirstElement( q.getResultList() );
  }

}
