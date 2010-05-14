package is.us.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * Array and dictionary editors inherit from this guy.
 * 
 * @author Hugi Thordarson
 */

public class USPlistEditorAbstract extends ERXComponent {

	protected static final String TYPE_ARRAY = "Array";
	protected static final String TYPE_DICTIONARY = "Dictionary";
	protected static final String TYPE_STRING = "String";

	public USPlistEditorAbstract( WOContext c ) {
		super( c );
	}

	/**
	 * Types of objects that can be inserted into the dictionary.
	 */
	public NSArray<String> objectTypes() {
		return new NSArray<String>( new String[] { TYPE_STRING, TYPE_DICTIONARY, TYPE_ARRAY } );
	}
}