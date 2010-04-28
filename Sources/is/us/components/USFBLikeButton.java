package is.us.components;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.webobjects.appserver.WOContext;

import er.extensions.appserver.ERXRequest;
import er.extensions.components.ERXStatelessComponent;

/**
 * A component for Facebook's new "Like"-thingamabob.
 * 
 * The simplest way to use this component is to just include it in your page.
 * Default values are the same as provided by Facebook.
 * 
 * Further documentation can be found on FB's developer site:
 * http://developers.facebook.com/docs/reference/plugins/like
 * 
 * @author Hugi Thordarson
 */

public class USFBLikeButton extends ERXStatelessComponent {

	public USFBLikeButton( WOContext context ) {
		super( context );
	}

	/**
	 * @return Width of the button. Defaults to 450 if no width is specified.
	 */
	public String width() {
		return stringValueForBinding( "width", "450" );
	}

	/**
	 * @return The colorscheme to use, one of "light", "dark" or "evil". Defaults to "light"
	 */
	public String colorscheme() {
		return stringValueForBinding( "colorscheme", "450" );
	}

	/**
	 * @return The verb to display, "like" or "recommend". Defaults to "like".
	 */
	public String verb() {
		return stringValueForBinding( "verb", "like" );
	}

	/**
	 * @return Indicates if we want to show faces or not. Defaults to "true".
	 */
	public String faces() {
		return booleanValueForBinding( "faces", true ) ? "true" : "false";
	}

	/**
	 * @return The layout, "standard" or "button_count". Defaults to "standard".
	 */
	public String layout() {
		return stringValueForBinding( "layout", "standard" );
	}

	/**
	 * @return The url to like. Defaults to the current, absolute URL.
	 */
	public String url() throws UnsupportedEncodingException {
		String url = stringValueForBinding( "url" );

		if( url == null ) {
			//			url = absoluteURL( (ERXRequest)context().request() );
			url = "http://www.apple.com/";
		}

		return URLEncoder.encode( url, "UTF-8" );
	}

	/**
	 * @return Constructs the URL we send to Facebook
	 */
	public String iframeSRC() throws UnsupportedEncodingException {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put( "href", url() );
		parameters.put( "layout", layout() );
		parameters.put( "show_faces", faces() );
		parameters.put( "width", width() );
		parameters.put( "action", verb() );
		parameters.put( "colorscheme", colorscheme() );

		return constructURLStringWithParameters( "http://www.facebook.com/plugins/like.php?", parameters );
	}

	/**
	 * @return CSS style string for the iframe.
	 */
	public String iframeStyle() {
		return "border:none; overflow:hidden; width:" + width() + "px; height:px";
	}

	/**
	 * (ripped from USWebObjects/USHTTPUtilities, included temporarily for reduced dependencies)
	 * 
	 * @return The absolute URL used to invoke the given request.
	 */
	private static String absoluteURL( ERXRequest request ) {
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
	private static String constructURLStringWithParameters( String baseURL, Map<String, String> parameters ) {

		StringBuilder b = new StringBuilder();

		if( baseURL != null ) {
			b.append( baseURL );
		}

		if( parameters != null && parameters.size() > 0 ) {
			b.append( "?" );

			for( String nextKey : parameters.keySet() ) {
				String nextValue = parameters.get( nextKey );

				b.append( nextKey );
				b.append( "=" );

				if( nextValue != null ) {
					b.append( nextValue );
				}

				b.append( "&amp;" );
			}
		}

		return b.toString();
	}
}