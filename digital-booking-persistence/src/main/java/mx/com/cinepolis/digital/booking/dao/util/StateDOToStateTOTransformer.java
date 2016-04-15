package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.StateLanguageDO;

import org.apache.commons.collections.Transformer;
/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.StateDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.StateTO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class StateDOToStateTOTransformer implements Transformer{
	 private Language language;

	  /**
	   * Constructor default
	   */
	  public StateDOToStateTOTransformer()
	  {
	    this.language = Language.ENGLISH;
	  }

	  /**
	   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
	   * 
	   * @param language
	   */
	  public StateDOToStateTOTransformer( Language laguage )
	  {
	    this.language = laguage;
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Object transform( Object object )
	  {
	    StateTO<CatalogTO, Integer> to = null;
	    if( object instanceof StateDO )
	    {
	      to = new StateTO<CatalogTO, Integer>( new CatalogTO(), ((StateDO) object).getIdCountry().getIdCountry() );
	      to.getCatalogState().setId( Long.valueOf( ((StateDO) object).getIdState().longValue() ));
	      to.getCatalogState().setTimestamp( ((StateDO) object).getDtLastModification() );
	      to.getCatalogState().setUserId( Long.valueOf( ((StateDO) object).getIdLastUserModifier() ) );
	      to.getCatalogState().setIdVista( ((StateDO) object).getIdVista() );

	      for( StateLanguageDO stateLanguageDO : ((StateDO) object).getStateLanguageDOList() )
	      {
	        if( stateLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
	        {
	          to.getCatalogState().setName( stateLanguageDO.getDsName() );
	        }
	      }
	    }
	    return to;
	  }


}
