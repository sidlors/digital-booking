package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for the entity
 * {@link mx.com.cinepolis.digital.booking.model.PersonDO}
 * 
 * @author agustin.ramirez
 */
public enum PersonQuery implements ModelQuery {
	PERSON_ID("idPerson"), PERSON_NAME("dsName"), PERSON_LAST_NAME("dsLastname"), PERSON_MOTHER_LAST_NAME(
			"dsMotherLastname"), PERSON_ID_THEATER(
			"idTheater"), PERSON_ID_USER("idUser"), PERSON_ACTIVE("fgActive");
	/**
	 * Query
	 */
	private String query;
	
	/**
	 * Constructor
	 * @param query
	 */
	private PersonQuery(String query) {
		this.query = query;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQuery() {
		return query;
	}

}
