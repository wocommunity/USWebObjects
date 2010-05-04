package is.us.components;

import java.text.SimpleDateFormat;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

import er.extensions.components.ERXComponent;

/**
 * 
 * 
 * @author Hugi Þórðarson
 */

public class USDateAndTimeField extends ERXComponent {

	private static SimpleDateFormat DATE_FORMAT_WITH_TIME = new SimpleDateFormat( "dd.MM.yyyy HH:mm" );

	public NSKeyValueCoding record;
	public String key;

	public USDateAndTimeField( WOContext context ) {
		super( context );
	}

	/**
	 *  
	 */
	public WOActionResults selectDate() {
		USDateAndTimePicker nextPage = pageWithName( USDateAndTimePicker.class );

		nextPage.record = record;
		nextPage.key = key;
		nextPage.componentToReturn = context().page();
		nextPage.setTimestamp( timestamp() );

		return nextPage;
	}

	/**
	 * The timestamp value selected. 
	 */
	public NSTimestamp timestamp() {
		if( record == null )
			return null;

		return (NSTimestamp)record.valueForKey( key );
	}

	/**
	 * FIXME: Localize 
	 */
	public String dateAsString() {

		if( timestamp() != null )
			return DATE_FORMAT_WITH_TIME.format( timestamp() );

		return "Engin dagsetning";
	}
}