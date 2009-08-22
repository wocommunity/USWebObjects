package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * 
 * @author Hugi Thordarson
 */

public class USApplicationInfoLogin extends ERXComponent {

	private static final String PASSWORD = "horsenose";
	public String password;

	public USApplicationInfoLogin( WOContext context ) {
		super( context );
	}

	@Override
	protected com.webobjects.foundation.NSArray<String> additionalCSSFiles() {
		return new NSArray<String>( "USApplicationInfo.css" );
	}

	public WOActionResults login() {
		if( PASSWORD.equals( password ) ) {
			return pageWithName( USApplicationInfo.class );
		}

		return context().page();
	}
}