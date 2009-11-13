package is.us.components;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.*;

import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * Display floating tooltip above an HTML element
 * 
 * @author Logi Helgu
 */

public class USTooltip extends ERXComponent {

	private String _fieldName;
	public String value;
	public String url;
	public String target;

	/**
	 * set true to hide the CSS style when tooltipping other elements than strings
	 */
	public boolean hideCSSStyle = false;

	public USTooltip( WOContext context ) {
		super( context );
	}

	@Override
	protected NSArray<String> additionalCSSFiles() {
		NSMutableArray<String> a = new NSMutableArray<String>();
		a.addObject( "css/tooltips.css" );
		return a;
	}

	@Override
	protected com.webobjects.foundation.NSArray<String> additionalJavascriptFiles() {
		NSMutableArray<String> a = new NSMutableArray<String>();
		a.addObject( "js/BubbleTooltips.js" );
		return a;
	}

	/**
	 * Get a unique name( id ) of for the HTML/JS
	 */
	public String fieldName() {
		if( _fieldName == null ) {
			_fieldName = "USTooltip" + ERXStringUtilities.replaceStringByStringInString( ".", "_", context().elementID().toString() );
		}

		return _fieldName;
	}

	/**
	 * Returns the CSS style to use if hideStyle is not true
	 */
	public String getStyle() {
		return hideCSSStyle ? "" : "tooltipLink";
	}
}