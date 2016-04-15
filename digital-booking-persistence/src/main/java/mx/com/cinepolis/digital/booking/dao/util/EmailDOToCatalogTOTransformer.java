package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.EmailDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.EmailDO} to a {@link
 * mx.com.cinepolis.digital.booking.to.CatalogTO.CatalogTO()}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class EmailDOToCatalogTOTransformer implements Transformer {

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
	 */
	@Override
	public Object transform(Object object) {
		  CatalogTO to = null;
		    if( object instanceof EmailDO )
		    {
		      to = new CatalogTO();
		      to.setId( ((EmailDO) object).getIdEmail().longValue() );
		      to.setName( ((EmailDO) object).getDsEmail() );
	
		    }
		    return to;
	}

}
