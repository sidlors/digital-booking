package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for the entity
 * {@link mx.com.cinepolis.digital.booking.model.UserDO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public enum UserQuery implements ModelQuery {
	USER_ID("idUser"),USER_NAME("dsUsername"),USER_ACTIVE("fgActive"),USER_ID_PERSON("idPerson")
	,USER_ID_REGION("idRegion"),USER_ID_THEATER("idTheater"),USER_ROLE_ID("idRole");

	/**
	 * Query Attribute
	 */
	private String query;

	/**
	 * Constructor
	 * 
	 * @param query
	 */
	private UserQuery(String query) {
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
