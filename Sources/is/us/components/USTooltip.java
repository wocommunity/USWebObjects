package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.ajax.AjaxUtils;
import er.extensions.components.ERXNonSynchronizingComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * For adding tooltips to HTML elements.
 * 
 * Bindings:
 *  value   : The text to display in the tooltip.
 *  noStyle : Disables styling of the tooltip (you'll usually want to set this to "true" if adding tooltips to anything other than a string).
 */

public class USTooltip extends ERXNonSynchronizingComponent {

	private String _uniqueID;

	public USTooltip( WOContext context ) {
		super( context );
	}

	@Override
	protected NSArray<String> additionalCSSFiles() {
		return new NSArray<String>( "css/USTooltip.css" );
	}

	@Override
	protected com.webobjects.foundation.NSArray<String> additionalJavascriptFiles() {
		return new NSArray<String>( "js/BubbleTooltips.js" );
	}

	@Override
	public void appendToResponse( WOResponse response, WOContext context ) {
		AjaxUtils.addScriptResourceInHead( context, response, "Ajax", "prototype.js" );
		super.appendToResponse( response, context );
	}

	/**
	 * @return A unique identifier for the component, used by the javascript.
	 */
	public String uniqueID() {
		if( _uniqueID == null ) {
			_uniqueID = componentName() + ERXStringUtilities.replaceStringByStringInString( ".", "_", context().elementID().toString() );
		}

		return _uniqueID;
	}

	/**
	 * @return The CSS class of the tooltip element.
	 */
	public String cssClass() {
		return noStyle() ? null : "tooltipLink";
	}

	/**
	 * @return The string value of the tooltip.
	 */
	public String value() {
		return stringValueForBinding( "value" );
	}

	/**
	 * @return Binding, you,ll probably want to set this to true if adding tips to elements other than strings.
	 */
	public boolean noStyle() {
		return booleanValueForBinding( "noStyle", false );
	}
}