package is.us.components;

import is.us.util.USStringUtilities;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * The login page for USApplicationInfo.
 *  
 * @author Hugi Thordarson
 */

public class USApplicationInfoLogin extends ERXComponent {

	private static final String PASSWORD_PROPERTY_NAME = "USApplicationInfo.password";
	public String password;

	public USApplicationInfoLogin( WOContext context ) {
		super( context );
	}

	@Override
	protected com.webobjects.foundation.NSArray<String> additionalCSSFiles() {
		return new NSArray<String>( "USApplicationInfo.css" );
	}

	public WOActionResults login() {

		if( passwordProperty() == null ) {
			throw new RuntimeException( USStringUtilities.stringWithFormat( "The property {} must be set", PASSWORD_PROPERTY_NAME ) );
		}

		if( passwordProperty().equals( password ) ) {
			return pageWithName( USApplicationInfo.class );
		}

		return context().page();
	}

	private static final String passwordProperty() {
		return System.getProperty( PASSWORD_PROPERTY_NAME );
	}
}