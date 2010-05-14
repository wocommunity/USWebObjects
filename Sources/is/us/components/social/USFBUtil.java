package is.us.components.social;

import java.util.Map;

import er.extensions.appserver.ERXRequest;

/**
 * (Note: This class exists to temporarily remove dependencies on other
 * US libraries, so social components currently only require Wonder.
 * 
 * @author Hugi Thordarson
 */

public class USFBUtil {

	/**
	 * (ripped from USWebObjects/USHTTPUtilities, included temporarily for reduced dependencies)
	 * 
	 * @return The absolute URL used to invoke the given request.
	 */
	public static String absoluteURL( ERXRequest request ) {
		StringBuilder b = new StringBuilder();

		if( request.isSecure() ) {
			b.append( "https://" );
		}
		else {
			b.append( "http://" );
		}

		b.append( request.remoteHostName() );
		b.append( request.uri() );
		return b.toString();
	}

	/**
	 * (ripped from USJava/USStringUtilities, included temporarily for reduced dependencies)
	 * 
	 * Creates a complete url, starting with the base url, appending each parameter in query string format.
	 * 
	 * @param baseURL The baseURL to use.
	 * @param parameters A dictionary of URL parameters to append to the base url.
	 * @return A complete URL
	 */
	public static String constructURLStringWithParameters( String baseURL, Map<String, Object> parameters ) {

		StringBuilder b = new StringBuilder();

		if( baseURL != null ) {
			b.append( baseURL );
		}

		if( parameters != null && parameters.size() > 0 ) {
			b.append( "?" );

			for( String nextKey : parameters.keySet() ) {
				Object nextValue = parameters.get( nextKey );

				b.append( nextKey );
				b.append( "=" );

				if( nextValue != null ) {
					b.append( nextValue.toString() );
				}

				b.append( "&amp;" );
			}
		}

		return b.toString();
	}
}
