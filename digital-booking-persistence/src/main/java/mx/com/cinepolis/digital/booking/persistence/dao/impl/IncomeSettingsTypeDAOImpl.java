package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDO}
 * 
 * @author jreyesv
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class IncomeSettingsTypeDAOImpl extends AbstractBaseDAO<IncomeSettingsTypeDO> implements IncomeSettingsTypeDAO
{

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
  public IncomeSettingsTypeDAOImpl()
  {
    super( IncomeSettingsTypeDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IncomeSettingsTypeTO get( int id )
  {
    return get( id, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IncomeSettingsTypeTO get( int id, Language language )
  {
    IncomeSettingsTypeDO incomeSettingsTypeDO = this.find( id );
    return (IncomeSettingsTypeTO) new IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer( language )
        .transform( incomeSettingsTypeDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IncomeSettingsTypeTO> getAll()
  {
    return this.getAll( Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<IncomeSettingsTypeTO> getAll( Language language )
  {
    return (List<IncomeSettingsTypeTO>) CollectionUtils.collect( this.findAll(),
      new IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer( language ) );
  }

}
