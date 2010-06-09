package is.us.components;

import org.apache.axis.encoding.Base64;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;

import er.extensions.components.ERXStatelessComponent;

/**
 * Displays an image using a data url.
 * 
 * @author Hugi Þórðarson
 */

public class USDataImage extends ERXStatelessComponent {

	public USDataImage( WOContext context ) {
		super( context );
	}

	/**
	 * @return The bound data.
	 */
	public NSData data() {
		return (NSData)valueForBinding( "data" );
	}

	/**
	 * @return The bound mimetype. Defaults to "image/png".
	 */
	public String mimeType() {
		return stringValueForBinding( "mimeType", "image/png" );
	}

	public String src() {

		if( data() == null ) {
			return null;
		}

		return "data:" + mimeType() + ";base64," + Base64.encode( data().bytes() );
	}
}