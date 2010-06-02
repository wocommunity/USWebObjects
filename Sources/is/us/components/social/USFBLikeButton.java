package is.us.components.social;

import is.us.util.USStringUtilities;
import is.us.wo.util.USHTTPUtilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import com.webobjects.appserver.WOContext;

import er.extensions.appserver.ERXRequest;
import er.extensions.components.ERXStatelessComponent;

/**
 * A component for Facebook's new "Like"-thingamabob.
 * 
 * The simplest way to use this component is to just include it on your
 * page, it will then use the default values as provided by Facebook.
 * 
 * Further documentation can be found on FB's developer site:
 * http://developers.facebook.com/docs/reference/plugins/like
 * 
 * If you want to use multiple like-buttons on your site,
 * you'll have to specify values for the "title" and "site"
 * bindings (thanks to @agorais for the technique).
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
		return stringValueForBinding( "colorscheme", "light" );
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
	 * @return An alternate title to show on FaceBook. Perfect if you want to include multiple Like-buttons on one page.
	 */
	public String title() {
		return stringValueForBinding( "title" );
	}

	/**
	 * @return An alternate sitename to show on Facebook. Perfect if you want to include multiple Like-buttons on one page.
	 */
	public String site() {
		return stringValueForBinding( "site" );
	}

	/**
	 * @return The url to like. Defaults to the current, absolute URL.
	 */
	public String url() throws UnsupportedEncodingException {
		String url = stringValueForBinding( "url" );

		if( url == null ) {
			url = USHTTPUtilities.absoluteURL( (ERXRequest)context().request() );
		}

		return URLEncoder.encode( url, "UTF-8" );
	}

	/**
	 * @return Constructs the URL sent to Facebook.
	 */
	public String iframeSRC() throws UnsupportedEncodingException {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put( "href", url() );
		parameters.put( "layout", layout() );
		parameters.put( "show_faces", faces() );
		parameters.put( "width", width() );
		parameters.put( "action", verb() );
		parameters.put( "colorscheme", colorscheme() );

		return USStringUtilities.constructURLStringWithParameters( "http://www.facebook.com/plugins/like.php", parameters );
	}

	/**
	 * @return CSS style string for the iframe.
	 */
	public String iframeStyle() {
		return "border:none; overflow:hidden; width:" + width() + "px; height:px";
	}
}