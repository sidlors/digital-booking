package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.IncomeSettingsDOToIncomeSettingsTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.IncomeSettingsTOToIncomeSettingsDOTransformer;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDO}
 * 
 * @author jreyesv
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class IncomeSettingsDAOImpl extends AbstractBaseDAO<IncomeSettingsDO> implements IncomeSettingsDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private IncomeSettingsTypeDAO incomeSettingsTypeDAO;

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
  public IncomeSettingsDAOImpl()
  {
    super( IncomeSettingsDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( IncomeSettingsTO incomeSettingTO )
  {
    if( this.validateIcomeSetting( incomeSettingTO ) )
    {
      if( !this.isIcomeSettingToEdit( incomeSettingTO ) )
      {
        IncomeSettingsDO incomeSettingsDO = (IncomeSettingsDO) new IncomeSettingsTOToIncomeSettingsDOTransformer()
            .transform( incomeSettingTO );
        incomeSettingsDO.setIdTheater( this.theaterDAO.find( incomeSettingTO.getIdTheater().intValue() ) );
        incomeSettingsDO.setIdIncomeSettingsType( incomeSettingsTypeDAO.find( incomeSettingTO.getIncomeSettingsType()
            .getId().intValue() ) );
        AbstractEntityUtils.applyElectronicSign( incomeSettingsDO, incomeSettingTO );
        this.create( incomeSettingsDO );
        this.flush();
        incomeSettingTO.setId( incomeSettingsDO.getIdIncomeSettings().longValue() );
      }
      else
      {
        this.update( incomeSettingTO );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( IncomeSettingsTO incomeSettingTO )
  {
    if( this.validateIcomeSetting( incomeSettingTO ) )
    {
      if( this.isIcomeSettingToEdit( incomeSettingTO ) )
      {
        IncomeSettingsDO incomeSettingsDO = this.find( incomeSettingTO.getId().intValue() );
        incomeSettingsDO.setQtGreenSemaphore( incomeSettingTO.getGreenSemaphore() );
        incomeSettingsDO.setQtYellowSemaphore( incomeSettingTO.getYellowSemaphore() );
        incomeSettingsDO.setQtRedSemaphore( incomeSettingTO.getRedSemaphore() );
        AbstractEntityUtils.applyElectronicSign( incomeSettingsDO, incomeSettingTO );
        this.edit( incomeSettingsDO );
        this.flush();
      }
    }
  }

  /**
   * Validates whether incomeSettingTO object is valid.
   * 
   * @param incomeSettingTO
   * @return isValid
   */
  private boolean validateIcomeSetting( IncomeSettingsTO incomeSettingTO )
  {
    return (incomeSettingTO != null);
  }

  /**
   * Validates whether incomeSettingTO object is to edit.
   * 
   * @param incomeSettingTO
   * @return isValid
   */
  private boolean isIcomeSettingToEdit( IncomeSettingsTO incomeSettingTO )
  {
    return (incomeSettingTO.getId() != null && incomeSettingTO.getId().intValue() > 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( IncomeSettingsTO incomeSettingTO )
  {
    IncomeSettingsDO incomeSettingToRemoveDO = this.find( incomeSettingTO.getId().intValue() );
    if( incomeSettingToRemoveDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( incomeSettingToRemoveDO, incomeSettingTO );
      incomeSettingToRemoveDO.setFgActive( Boolean.FALSE );
      this.edit( incomeSettingToRemoveDO );
      this.flush();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<IncomeSettingsTO> findIncomeSettingsByTheater( Long idTheater )
  {
    Query q = this.em.createNamedQuery( "IncomeSettingsDO.findIncomeSettingsByTheater" );
    q.setParameter( "idTheater", idTheater );
    List<IncomeSettingsDO> settings = q.getResultList();
    List<IncomeSettingsTO> list = new ArrayList<IncomeSettingsTO>();
    
    list.addAll( CollectionUtils.collect( settings, new IncomeSettingsDOToIncomeSettingsTOTransformer() ) );
    return list;
  }

}
