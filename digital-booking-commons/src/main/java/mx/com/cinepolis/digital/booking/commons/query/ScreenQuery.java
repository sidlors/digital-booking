package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity
 * {@link mx.com.cinepolis.digital.booking.model.ScreenDO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public enum ScreenQuery implements ModelQuery {
	SCREEN_ID("idScreen"),  SCREEN_CAPACITY(
			"nuCapacity"), SCREEN_NUMBER("nuScreen"),SCREEN_THEATER_ID("idTheater");
	/**
	 * Query Attribute
	 */
	private String query;
	
	/**
	 * Contructor
	 * @param query
	 */
	private ScreenQuery(String query) {
		this.query = query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mx.com.cinepolis.digital.booking.model.query.ModelQuery#getQuery()
	 */
	@Override
	public String getQuery() {

		return query;
	}

}
