package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

/**
 * Displays a string (such as an email address) in scrambled form so robots can't pick it up.
 * 
 * @author Hugi Þórðarson
 */

public class USObfuscatedString extends ERXComponent {

	public String value;
	private String _mungedValue;

	public USObfuscatedString( WOContext context ) {
		super( context );
	}

	public String mungedValue() {

		if( value == null ) {
			return null;
		}

		if( _mungedValue == null ) {
			StringBuffer b = new StringBuffer();

			for( int j = 0; j < value.length(); j++ ) {
				b.append( String.valueOf( (int)value.charAt( j ) ) );

				if( !(j == value.length() - 1) ) {
					b.append( "," );
				}
			}

			_mungedValue = b.toString();
		}

		return _mungedValue;
	}
}