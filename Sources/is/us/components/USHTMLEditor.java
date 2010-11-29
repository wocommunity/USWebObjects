package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.appserver.*;
import er.extensions.components.ERXComponent;

/**
 * @author Hugi Þórðarson
 * 
 */

public class USHTMLEditor extends ERXComponent {

	public String value;

	public USHTMLEditor( WOContext context ) {
		super( context );
	}

	/**
	 * @return true if the browser is supported by the HTML editor.
	 */
	public boolean isExplorerCompatible() {

		ERXBrowser browser = ((ERXRequest)context().request()).browser();

		if( browser.isIE() || browser.isSafari() ) {
			return true;
		}

		return false;
	}
}
