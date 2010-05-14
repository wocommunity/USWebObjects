package is.us.components;

import java.text.SimpleDateFormat;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

import er.extensions.components.ERXNonSynchronizingComponent;

/**
 * A field for selecting date and time.
 * 
 * @author Hugi Þórðarson
 */

public class USDateAndTimeField extends ERXNonSynchronizingComponent {

	/**
	 * Formatter for the component.
	 * 
	 * Note the advanced localization features, that allows
	 * you to modify the format, just by modifying the code.
	 */
	private static SimpleDateFormat DATE_FORMAT_WITH_TIME = new SimpleDateFormat( "dd.MM.yyyy HH:mm" );

	public USDateAndTimeField( WOContext context ) {
		super( context );
	}

	/**
	 * @return the record we want to set the timestamp value in
	 */
	public NSKeyValueCodingAdditions record() {
		return (NSKeyValueCodingAdditions)valueForBinding( "record" );
	}

	/**
	 * @return The key of the timestamp we're going to set in record()
	 */
	public String key() {
		return stringValueForBinding( "key" );
	}

	/**
	 * The current value of the timestamp object. 
	 */
	public NSTimestamp timestamp() {
		return (NSTimestamp)record().valueForKeyPath( key() );
	}

	/**
	 * Invoked when the user presses the button to select a date. 
	 */
	public WOActionResults selectDate() {
		USDateAndTimePicker nextPage = pageWithName( USDateAndTimePicker.class );
		nextPage.record = record();
		nextPage.key = key();
		nextPage.componentToReturn = context().page();
		nextPage.setTimestamp( timestamp() );
		return nextPage;
	}

	/**
	 * The timestamp formatted for display on the component's button. 
	 */
	public String dateAsString() {

		if( timestamp() != null ) {
			return DATE_FORMAT_WITH_TIME.format( timestamp() );
		}

		return "...";
	}
}