package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;

/**
 * TO que contiene la informaciona  enviar por correo
 * @author agustin.ramirez
 *
 */
public class EmailBookingTO extends AbstractTO{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6342736823013030105L;
	/**
	 * Booking
	 */
	private BookingTO bookingTO;
	
	/**
	 * Email de los Destinatarios
	 */
	private List<String> email;

	/**
	 * @return the bookingTO
	 */
	public BookingTO getBookingTO() {
		return bookingTO;
	}

	/**
	 * @param bookingTO the bookingTO to set
	 */
	public void setBookingTO(BookingTO bookingTO) {
		this.bookingTO = bookingTO;
	}

	/**
	 * @return the email
	 */
	public List<String> getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(List<String> email) {
		this.email = email;
	}

}
