package is.us.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * Implementation of TinyMCE
 * 
 * @author Hugi Þórðarson
 */

public class USHTMLEditorTinyMCE extends ERXComponent {

	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	public USHTMLEditorTinyMCE( WOContext context ) {
		super( context );
	}

	@Override
	protected NSArray<String> additionalJavascriptFiles() {
		return new NSArray<String>( "tiny_mce/tiny_mce.js" );
	}
}
