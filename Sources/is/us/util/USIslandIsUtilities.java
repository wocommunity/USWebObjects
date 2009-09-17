package is.us.util;

import is.us.wo.util.USHTTPUtilities;

import org.slf4j.*;

import com.webobjects.appserver.*;

/**
 * A WO utility methods for island.is authentication service
 * 
 * @author Bjarni Sævarsson <bjarnis@us.is>
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 * @reviewedby Logi Helgu at Sep 16, 2009( see JIRA issue INN-728 )
 */

public class USIslandIsUtilities {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger( USIslandIsUtilities.class );

	/**
	 * IP-address of the localhost.
	 */
	private static final String LOCALHOST_IP = "127.0.0.1";

	private static final String TOKEN_KEY = "token";
	private static final String BASE_LINK = "https://www.island.is/audkenning/audkenning";

	/**
	 * @return The token.
	 */
	public static String token( WORequest request ) {
		return request.stringFormValueForKey( TOKEN_KEY );
	}

	/**
	 * @return The remote user's IP address, or the localhost IP if no IP is found.
	 */
	public static String userIp( WORequest request ) {
		String userIp = USHTTPUtilities.ipAddressFromRequest( request );

		if( userIp == null ) {
			userIp = LOCALHOST_IP;
		}

		return userIp;
	}

	/**
	 * determines if a token parameter is present
	 */
	public static boolean hasToken( WORequest request ) {
		return request.formValueForKey( TOKEN_KEY ) != null;
	}

	/**
	 * @return The island.is authentication url.
	 */
	public static String authenticationHref( String siteID ) {
		return BASE_LINK + "?id=" + siteID;
	}

	/**
	 * Redirects the client browser to the login page at island.is
	 */
	public static WOResponse authenticationResponse( String siteID ) {
		return USHTTPUtilities.redirectTemporary( authenticationHref( siteID ) );
	}
}