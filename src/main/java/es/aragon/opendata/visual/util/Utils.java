package es.aragon.opendata.visual.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class Utils {

	private final static Logger log = Logger.getLogger(Utils.class);
	private static final int defaultReadTimeOut = 10000;
	private static final int defaultTimeOut = 5000;
	
	public static String processURLGet(String url, String urlParameters, Map<String, String> headers, Cookies cookies, String encoding) throws SocketTimeoutException {

		//Security SSL
		TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
			    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			        return null;
			    }
			    public void checkClientTrusted(
			        java.security.cert.X509Certificate[] certs, String authType) {
			    }
			    public void checkServerTrusted(
			        java.security.cert.X509Certificate[] certs, String authType) {
			    }
			}
		};

		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			   
		}
	   
		String output = null;
		HttpsURLConnection httpsConnection = null;
		try {
			URL targetUrl = null;
			if ((urlParameters == null) || (urlParameters.equals(""))) {
				targetUrl = new URL(url);
			} else {
				targetUrl = new URL(url + "?" + urlParameters);
			}

			httpsConnection = (HttpsURLConnection) targetUrl.openConnection();
			httpsConnection.setDoOutput(true);
			httpsConnection.setRequestMethod("GET");
			if (headers != null) {
				Iterator<Entry<String, String>> it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
					httpsConnection.setRequestProperty(pairs.getKey(), pairs.getValue());
				}
			}
			cookies.setCookies(httpsConnection);
			httpsConnection.setConnectTimeout(defaultTimeOut);
			httpsConnection.setReadTimeout(defaultReadTimeOut);

			InputStream is = httpsConnection.getInputStream();

			output = IOUtils.toString(is, encoding);

			cookies.storeCookies(httpsConnection);

			if (httpsConnection.getResponseCode() != 200) {
				log.error("The URI does not return a 200 code");
				log.error(output);
				return "";
			}

		} catch (MalformedURLException e) {
			log.error("Error with the URI: " + url + "?" + urlParameters, e);

		} catch (SocketTimeoutException e) {
			throw e;

		} catch (IOException e) {
			log.error("IOError: " + url + "?" + urlParameters, e);
			
		} finally {
			httpsConnection.disconnect();
		}

		return output;

	}
	
	public static boolean v(Object c) {

		if (c == null)
			return false;

		if (c instanceof StringBuffer) {
			if (((StringBuffer) c).length() <= 0)
				return false;
			return true;
		}

		if (c instanceof List) {
			if (((List<?>) c).size() == 0)
				return false;
			return true;
		}

		if (c instanceof String) {
			if (c.equals(""))
				return false;
			return true;
		}

		if (Float.class.isInstance(c)) {
			if ((float) c == -1)
				return false;
			return true;
		}

		if (Integer.class.isInstance(c)) {
			if ((int) c == -1)
				return false;
			return true;
		}

		return true;
	}


}
