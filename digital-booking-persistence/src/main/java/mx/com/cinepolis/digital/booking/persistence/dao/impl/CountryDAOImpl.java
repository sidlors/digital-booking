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
import mx.com.cinepolis.digital.booking.dao.util.CountryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.CountryLanguageDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Class that implements the methods of the Data Access Object related to Country. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO}
 * 
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class CountryDAOImpl extends AbstractBaseDAO<CountryDO> implements CountryDAO
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
  public CountryDAOImpl()
  {
    super( CountryDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getAll()
  {
    return getAll( Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getAll( Language language )
  {
    List<CountryDO> countries = new ArrayList<CountryDO>();
    countries.addAll( CollectionUtils.select(
      super.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( true ) ) ) );

    List<CatalogTO> tos = new ArrayList<CatalogTO>();
    tos.addAll( CollectionUtils.collect( countries, new CountryDOToCatalogTOTransformer( language ) ) );

    return tos;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CountryDO> findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "CountryDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CountryDO> findByIdVista( String idVista )
  {
    Query q = em.createNamedQuery( "CountryDO.findByIdVista" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @Override
  public void update( CatalogTO catalogTO )
  {
    update( catalogTO, Language.ENGLISH );
  }

  @Override
  public void update( CatalogTO catalogTO, Language language )
  {
    CountryDO countryDO = this.find( catalogTO.getId().intValue() );
    if( countryDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( countryDO, catalogTO );
      for( CountryLanguageDO countryLanguageDO : countryDO.getCountryLanguageDOList() )
      {
        if( countryLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          countryLanguageDO.setDsName( catalogTO.getName() );
          break;
        }
      }
      this.edit( countryDO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO )
  {
    save( catalogTO, Language.ENGLISH );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO, Language language )
  {
    CountryDO countryDO = new CountryDO();
    if( catalogTO.getId() != null )
    {
      countryDO = this.find( catalogTO.getId().intValue() );
      if( CollectionUtils.isEmpty( countryDO.getCountryLanguageDOList() ) )
      {
        countryDO.setCountryLanguageDOList( new ArrayList<CountryLanguageDO>() );
      }
    }
    else
    {
      countryDO.setCountryLanguageDOList( new ArrayList<CountryLanguageDO>() );
    }
    AbstractEntityUtils.applyElectronicSign( countryDO, catalogTO );

    CountryLanguageDO countryLanguageDO = new CountryLanguageDO();
    countryLanguageDO.setDsName( catalogTO.getName() );
    countryLanguageDO.setIdCountry( countryDO );
    countryLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    countryDO.getCountryLanguageDOList().add( countryLanguageDO );
    countryDO.setIdVista( catalogTO.getIdVista() );

    this.create( countryDO );
    this.flush();
    catalogTO.setId( countryDO.getIdCountry().longValue() );
  }

}
