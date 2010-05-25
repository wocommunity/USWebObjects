package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

import er.ajax.AjaxUtils;
import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * Display floating tooltip above an HTML element
 */

public class USTooltip extends ERXComponent {

	private String _fieldName;

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

	@Override
	public void appendToResponse( WOResponse response, WOContext context ) {
		AjaxUtils.addScriptResourceInHead( context, response, "prototype.js" );
		super.appendToResponse( response, context );
	}

	/**
	 * @return A unique identifier for the component, used in the javascript.
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

	/**
	 * @return The value of the tooltip.
	 */
	public String value() {
		return stringValueForBinding( "value" );
	}

	/**
	 * WTF?
	 */
	public String url() {
		return stringValueForBinding( "url" );
	}

	/**
	 * WTF?
	 */
	public String target() {
		return stringValueForBinding( "target" );
	}
}