package is.us.components;

import is.us.util.*;
import is.us.wo.util.USHTTPUtilities;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

/**
 * @author Hugi Þórðarson
 * 
 * TODO: Use ERXBrowser to check for compatibility.
 */

public class USHTMLEditor extends ERXComponent {

	public String value;

	public USHTMLEditor( WOContext context ) {
		super( context );
	}

	public boolean isExplorerCompatible() {

		String userAgentString = USHTTPUtilities.userAgent( context().request() );

		if( !USStringUtilities.stringHasValue( userAgentString ) )
			return false;

		userAgentString = userAgentString.toLowerCase();

		if( userAgentString.indexOf( "msie" ) != -1 && userAgentString.indexOf( "windows" ) != -1 )
			return true;

		if( userAgentString.indexOf( "mozilla" ) != -1 && userAgentString.indexOf( "safari" ) == -1 )
			return true;

		return false;
	}
}
