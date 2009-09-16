package is.us.util;

import is.us.wo.util.USHTTPUtilities;

import org.slf4j.*;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSBundle;

/**
 * A WO wrapper for {@link is.us.util.USIslandIsAuthenticationClient}
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
	 * @param request the {@link WORequest} for this session
	 * @param keystorePath the path to the key store file
	 * @param keystorePassword the password for the key store
	 * @param username the island.is authentication service username
	 * @param password the island.is authentication service password
	 */
	@SuppressWarnings( "deprecation" )
	public static USIslandIsAuthenticationClient client( WORequest request, String keystorePath, String keystorePassword, String username, String password ) {

		String keystoreFile = NSBundle.bundleForClass( USIslandIsUtilities.class ).pathForResource( keystorePath, null );
		logger.debug( "Using keystore file at: " + keystoreFile );
		System.setProperty( "javax.net.ssl.trustStore", keystoreFile );
		System.setProperty( "javax.net.ssl.trustStorePassword", keystorePassword );

		return new USIslandIsAuthenticationClient( token( request ), userIp( request ), username, password );
	}

	/**
	 * @return The token.
	 */
	private static String token( WORequest request ) {
		return request.stringFormValueForKey( TOKEN_KEY );
	}

	/**
	 * @return The remote user's IP address, or the localhost IP if no IP is found.
	 */
	private static String userIp( WORequest request ) {
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