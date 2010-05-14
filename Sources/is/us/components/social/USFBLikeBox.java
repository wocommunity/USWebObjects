package is.us.components.social;

import java.util.*;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXStatelessComponent;

/**
 * A component for Facebook's new "Like Box"-thingamabob.
 * 
 * The only required binding is "id", the id of your FaceBook page.
 * Other bindings will use FB's provided default values if not set.
 * 
 * Further documentation can be found on FB's developer site:
 * http://developers.facebook.com/docs/reference/plugins/like-box
 * 
 * @author Hugi Thordarson
 */

public class USFBLikeBox extends ERXStatelessComponent {

	public USFBLikeBox( WOContext context ) {
		super( context );
	}

	/**
	 * @return ID of the Facebook page this box is linked to. Required!
	 */
	public String id() {
		return stringValueForBinding( "id", "185550966885" );
	}

	/**
	 * @return Width of the like box. Defaults to 292 if no width is specified.
	 */
	public String width() {
		return stringValueForBinding( "width", "292" );
	}

	/**
	 * The box's height can be specified by a binding,
	 * but defaults to a value deduced from its content (As facebook does).
	 * 
	 * TODO: Pending implementation of the height calculation;
	 * currently using the maximum height of 587 as default.
	 * 
	 * @return Height of the like box.
	 */
	public String height() {
		return stringValueForBinding( "height", "587" );
	}

	/**
	 * @return Number of faces to show. Defaults to 10.
	 */
	public String connections() {
		return stringValueForBinding( "connections", "10" );
	}

	/**
	 * @return Indicates if we want to show a stream. Defaults to true.
	 */
	public String stream() {
		return booleanValueForBinding( "stream", true ) ? "true" : "false";
	}

	/**
	 * @return Indicates if we want to show a header. Defaults to true.
	 */
	public String header() {
		return booleanValueForBinding( "header", true ) ? "true" : "false";
	}

	/**
	 * @return Constructs the URL sent to Facebook.
	 */
	public String iframeSRC() {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put( "id", id() );
		parameters.put( "width", width() );
		parameters.put( "connections", connections() );
		parameters.put( "stream", stream() );
		parameters.put( "header", header() );
		parameters.put( "height", height() );

		return USFBUtil.constructURLStringWithParameters( "http://www.facebook.com/plugins/likebox.php", parameters );
	}

	/**
	 * @return CSS style string for the iframe.
	 */
	public String iframeStyle() {
		return "border:none; overflow:hidden; width:" + width() + "px; height:" + height() + "px;";
	}
}