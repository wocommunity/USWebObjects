package is.us.components;

import is.us.util.USStringUtilities;

import java.util.*;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.*;

import er.extensions.components.ERXComponent;

/**
 * For picking date and time from a menu.
 * 
 * @author Hugi Þórðarson
 */

public class USDateAndTimePicker extends ERXComponent {

	private GregorianCalendar calendar;
	public NSKeyValueCodingAdditions record;
	public WOComponent componentToReturn;
	public String key;

	public USDateAndTimePicker( WOContext context ) {
		super( context );
	}

	public NSTimestamp timestamp() {
		return (calendar != null) ? new NSTimestamp( calendar.getTime() ) : null;
	}

	public void setTimestamp( NSTimestamp newStamp ) {
		if( newStamp != null ) {
			initializeCalendar();
			calendar.setTime( newStamp );
		}
	}

	public GregorianCalendar calendar() {
		return calendar;
	}

	private void initializeCalendar() {
		setCalendar( (GregorianCalendar)GregorianCalendar.getInstance( Locale.getDefault() ) );
		setHour( "0" );
		setMinute( "0" );
		setSecond( "0" );
	}

	public void setCalendar( GregorianCalendar g ) {
		calendar = g;
	}

	public String second() {
		return calendarValueForKey( Calendar.SECOND );
	}

	public String minute() {
		return calendarValueForKey( Calendar.MINUTE );
	}

	public String hour() {
		return calendarValueForKey( Calendar.HOUR_OF_DAY );
	}

	public String day() {
		return calendarValueForKey( Calendar.DAY_OF_MONTH );
	}

	public String month() {
		String initialValue = calendarValueForKey( Calendar.MONTH );

		if( initialValue != null )
			return String.valueOf( Integer.parseInt( initialValue ) + 1 );
		else
			return null;
	}

	public void setMonth( String value ) {
		if( value != null ) {
			String finalValue = String.valueOf( Integer.parseInt( value ) - 1 );
			calendarSetValueForKey( finalValue, Calendar.MONTH );
		}
	}

	public String year() {
		return calendarValueForKey( Calendar.YEAR );
	}

	public void setSecond( String value ) {
		calendarSetValueForKey( value, Calendar.SECOND );
	}

	public void setMinute( String value ) {
		calendarSetValueForKey( value, Calendar.MINUTE );
	}

	public void setHour( String value ) {
		calendarSetValueForKey( value, Calendar.HOUR_OF_DAY );
	}

	public void setDay( String value ) {
		calendarSetValueForKey( value, Calendar.DAY_OF_MONTH );
	}

	public void setYear( String value ) {
		calendarSetValueForKey( value, Calendar.YEAR );
	}

	public String calendarValueForKey( int key ) {

		if( calendar() != null )
			return String.valueOf( calendar().get( key ) );

		return null;
	}

	public void calendarSetValueForKey( String value, int key ) {

		if( USStringUtilities.stringHasValue( value ) ) {

			if( calendar() == null )
				initializeCalendar();

			calendar().set( key, new Integer( value ).intValue() );
		}
	}

	public NSArray<String> days() {
		return arrayWithNumberStringsInRange( 1, 31 );
	}

	public NSArray<String> months() {
		return arrayWithNumberStringsInRange( 1, 12 );
	}

	public NSArray<String> years() {
		return arrayWithNumberStringsInRange( 2000, 2020 );
	}

	public NSArray<String> hours() {
		return arrayWithNumberStringsInRange( 0, 23 );
	}

	public NSArray<String> minutes() {
		return arrayWithNumberStringsInRange( 0, 59 );
	}

	/**
	 * Constructs an array of strings with all numbers the given numeric range.
	 * 
	 * @param start The first number (included)
	 * @param end The last number (included)
	 * @return An array of Strings with the given numbers
	 */
	private static NSArray<String> arrayWithNumberStringsInRange( int start, int end ) {
		int i = start;
		NSMutableArray<String> resultArray = new NSMutableArray<String>();

		while( i < (end + 1) ) {
			resultArray.addObject( String.valueOf( i++ ) );
		}

		return resultArray.immutableClone();
	}

	public WOComponent nullifyCalendar() {
		setCalendar( null );
		record.takeValueForKey( null, key );

		componentToReturn.ensureAwakeInContext( context() );
		return componentToReturn;
	}

	/**
	 * Note that we invoke saveChanges on the record's editing context if it's an EOEnterpriseObject.
	 * 
	 * 
	 * @return The previous component.
	 */
	public WOActionResults submit() {
		record.takeValueForKeyPath( timestamp(), key );

		if( record instanceof EOEnterpriseObject ) {
			((EOEnterpriseObject)record).editingContext().saveChanges();
		}

		componentToReturn.ensureAwakeInContext( context() );
		return componentToReturn;
	}
}