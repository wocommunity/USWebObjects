package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXStatelessComponent;

/**
 * Displays a string (such as an email address) in scrambled form so robots can't pick it up.
 * 
 * @author Hugi Þórðarson
 */

public class USObfuscatedString extends ERXStatelessComponent {

	public USObfuscatedString( WOContext context ) {
		super( context );
	}

	/**
	 * @return The bound value.
	 */
	private String value() {
		return stringValueForBinding( "value" );
	}

	/**
	 * @return Te obfuscated value
	 */
	public String obfuscatedValue() {

		String value = value();

		if( value == null ) {
			return null;
		}

		StringBuilder b = new StringBuilder();

		for( int j = 0; j < value.length(); j++ ) {
			b.append( String.valueOf( (int)value.charAt( j ) ) );

			if( !(j == value.length() - 1) ) {
				b.append( "," );
			}
		}

		return b.toString();
	}
}