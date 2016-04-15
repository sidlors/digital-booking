package mx.com.cinepolis.digital.booking.commons.to;

/**
 * Clase que modela la informacion relacioanda a un evento especial
 * @author agustin.ramirez
 *
 */
public class EventSpecialTO  extends EventTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 875158559220064730L;
	
	/**
	 * Id Type Event
	 */
	private Integer idTypeEvent;


	/**
	 * @return the idTypeEvent
	 */
	public Integer getIdTypeEvent() {
		return idTypeEvent;
	}


	/**
	 * @param idTypeEvent the idTypeEvent to set
	 */
	public void setIdTypeEvent(Integer idTypeEvent) {
		this.idTypeEvent = idTypeEvent;
	}

}
