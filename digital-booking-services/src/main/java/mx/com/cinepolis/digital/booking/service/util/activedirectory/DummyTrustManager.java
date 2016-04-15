package mx.com.cinepolis.digital.booking.service.util.activedirectory;


import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
/**
 * Trust Manager Dummies
 * @author aramirezg
 *
 */
public class DummyTrustManager implements X509TrustManager {
	public void checkClientTrusted(X509Certificate[] cert, String authType) {
		return;
	}

	public void checkServerTrusted(X509Certificate[] cert, String authType) {
		return;
	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}
}