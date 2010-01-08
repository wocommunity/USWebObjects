package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * @author Hugi Þórðarson
 */

public class USHTMLEditorSimple extends ERXComponent {

	private String _uniqueID;

	public String value;

	public USHTMLEditorSimple( WOContext context ) {
		super( context );
	}

	/**
	 * Return a uniqueID for html elements
	 */
	public String uniqueID() {

		if( _uniqueID == null ) {
			_uniqueID = ERXStringUtilities.replaceStringByStringInString( ".", "_", context().elementID() );
		}

		return _uniqueID;
	}
}
