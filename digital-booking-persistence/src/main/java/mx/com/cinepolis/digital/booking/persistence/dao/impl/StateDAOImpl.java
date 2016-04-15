package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.StateDOToStateTOTransformer;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.StateLanguageDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.StateDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class StateDAOImpl extends AbstractBaseDAO<StateDO> implements StateDAO
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
  public StateDAOImpl()
  {
    super( StateDO.class );
  }

  /**
   * {@inheritDoc}
   */
  public List<StateTO<CatalogTO, Number>> findAllStates()
  {
    return findAllStates( Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StateTO<CatalogTO, Number>> findAllStates( Language language )
  {
    List<StateDO> states = new ArrayList<StateDO>();
    states.addAll( CollectionUtils.select(
      this.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( true ) ) ) );

    List<StateTO<CatalogTO, Number>> stateTOs = new ArrayList<StateTO<CatalogTO, Number>>();
    stateTOs.addAll( CollectionUtils.collect( states, new StateDOToStateTOTransformer( language ) ) );

    return stateTOs;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<StateDO> findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "StateDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<StateDO> findByIdVista( String idVista )
  {
    Query q = em.createNamedQuery( "StateDO.findByIdVista" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @Override
  public void update( StateTO<CatalogTO, Number> stateTO )
  {
    update( stateTO, Language.ENGLISH );
  }

  @Override
  public void update( StateTO<CatalogTO, Number> stateTO, Language language )
  {
    CatalogTO catalogTO = stateTO.getCatalogState();
    StateDO stateDO = this.find( catalogTO.getId().intValue() );
    if( stateDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( stateDO, catalogTO );
      for( StateLanguageDO stateLanguageDO : stateDO.getStateLanguageDOList() )
      {
        if( stateLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          stateLanguageDO.setDsName( catalogTO.getName() );
          break;
        }
      }
      this.edit( stateDO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( StateTO<CatalogTO, Number> stateTO )
  {
    save( stateTO, Language.ENGLISH );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( StateTO<CatalogTO, Number> stateTO, Language language )
  {
    CatalogTO catalogTO = stateTO.getCatalogState();
    StateDO stateDO = new StateDO();
    if( catalogTO.getId() != null )
    {
      stateDO = this.find( catalogTO.getId().intValue() );
      if( CollectionUtils.isEmpty( stateDO.getStateLanguageDOList() ) )
      {
        stateDO.setStateLanguageDOList( new ArrayList<StateLanguageDO>() );
      }
    }
    else
    {
      stateDO.setStateLanguageDOList( new ArrayList<StateLanguageDO>() );
    }
    AbstractEntityUtils.applyElectronicSign( stateDO, catalogTO );

    StateLanguageDO stateLanguageDO = new StateLanguageDO();
    stateLanguageDO.setDsName( catalogTO.getName() );
    stateLanguageDO.setIdState( stateDO );
    stateLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    stateDO.getStateLanguageDOList().add( stateLanguageDO );
    stateDO.setIdCountry( new CountryDO( stateTO.getCatalogCountry().intValue() ) );
    stateDO.setIdVista( catalogTO.getIdVista() );

    this.create( stateDO );
    this.flush();
    catalogTO.setId( stateDO.getIdState().longValue() );
  }

}
