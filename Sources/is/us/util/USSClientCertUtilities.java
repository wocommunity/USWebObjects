package is.us.util;

import org.slf4j.*;

import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * TODO: Missing comments // Hugi
 * 
 * @author Atli Páll Hafsteinsson
 */

public class USSClientCertUtilities {

	static final Logger logger = LoggerFactory.getLogger( USSClientCertUtilities.class );

	private static final String HEADER = "ssl_client_cert_cn";

	/**
	 * No instances created.
	 */
	private USSClientCertUtilities() {}

	/**
	 * TODO: Missing comments // Hugi
	 */
	public static String persidno( WORequest request ) {
		NSMutableDictionary<String, String> clientCertInfo = parseSslClientCertCn( request );
		String ssn = null;
		if( clientCertInfo != null ) {
			if( clientCertInfo.containsKey( "serialnumber" ) )
				ssn = clientCertInfo.objectForKey( "serialnumber" );
			else if( clientCertInfo.containsKey( "oid.2.5.4.5" ) )
				ssn = clientCertInfo.objectForKey( "oid.2.5.4.5" );
			logger.debug( "Returning ssn: " + ssn );
			return ssn;
		}

		return ssn;
	}

	/**
	 * TODO: Missing comments // Hugi
	 */
	public static boolean isClientCertPresent( WORequest request ) {
		//		return isClientCertPrecent( request.headers() );
		String header = request.headerForKey( HEADER );
		if( header != null && header.length() > 0 )
			return true;

		return false;
	}

	/**
	 * TODO: Missing comments // Hugi
	 *
	public static boolean isClientCertPresent( Map<String, List<String>> headers ) {
		String header = headers.get( HEADER );
		return (header != null && header.length() > 0);
	}
	*/

	/**
	 * TODO: Missing comments // Hugi
	 */
	private static NSMutableDictionary<String, String> parseSslClientCertCn( WORequest request ) {
		NSMutableDictionary<String, String> clientCertInfo = new NSMutableDictionary<String, String>();
		String header = request.headerForKey( HEADER );
		if( header != null ) {
			String[] parts = header.split( "/" );
			for( String part : parts ) {
				logger.debug( part );
				if( part != null && part.length() > 0 ) {
					String[] pair = part.split( "=" );
					if( pair.length == 2 )
						clientCertInfo.setObjectForKey( pair[1], pair[0].toLowerCase() );
				}
			}
		}
		return clientCertInfo;
	}
}