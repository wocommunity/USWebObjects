package is.us.util;

import java.text.*;
import java.util.*;

import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXTimestampUtility;

/**
 * Timestamp related utility classes.
 * 
 * @author Hugi Þórðarson
 */

public class USTimestampUtilities {

	/**
	 * No instances created, ever.
	 */
	private USTimestampUtilities() {}

	/**
	 * Normalizes a timestamp to midnight on the given day.
	 */
	public static NSTimestamp normalizeTimestampToMidnight( NSTimestamp oldTimestamp ) {
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( oldTimestamp );

		int hourOfDay = calendar.get( GregorianCalendar.HOUR_OF_DAY );
		int minuteOfHour = calendar.get( GregorianCalendar.MINUTE );
		int secondOfMinute = calendar.get( GregorianCalendar.SECOND );

		return oldTimestamp.timestampByAddingGregorianUnits( 0, 0, 0, -hourOfDay, -minuteOfHour, -secondOfMinute );
	}

	/**
	 * This method will normalize an NSTimestamp to the beginning of the month,
	 * i.e., creates a timestamp set to midnight on the first day of the month.
	 * 
	 * @param timestamp The timestamp to normalize
	 */
	public static NSTimestamp normalizeTimestampForMonth( NSTimestamp timestamp ) {
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( timestamp );

		int dayOfMonth = calendar.get( GregorianCalendar.DAY_OF_MONTH );
		int hourOfDay = calendar.get( GregorianCalendar.HOUR_OF_DAY );
		int minuteOfHour = calendar.get( GregorianCalendar.MINUTE );
		int secondOfMinute = calendar.get( GregorianCalendar.SECOND );

		return timestamp.timestampByAddingGregorianUnits( 0, 0, -dayOfMonth + 1, -hourOfDay, -minuteOfHour, -secondOfMinute );
	}

	/**
	 * Normalizes a timestamp to 23:59:59 on the same day.
	 */
	public static NSTimestamp normalizeTimestampToOneSecondBeforeMidnight( NSTimestamp oldTimestamp ) {
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( oldTimestamp );

		int hourOfDay = calendar.get( GregorianCalendar.HOUR_OF_DAY );
		int minuteOfHour = calendar.get( GregorianCalendar.MINUTE );
		int secondOfMinute = calendar.get( GregorianCalendar.SECOND );

		return oldTimestamp.timestampByAddingGregorianUnits( 0, 0, 1, -hourOfDay, -minuteOfHour, -secondOfMinute - 1 );
	}

	/**
	 * Normalizes a timestamp to the minute, that is "removes" the seconds
	 */
	public static NSTimestamp normalizeTimestampToTheMinute( NSTimestamp oldTimestamp ) {
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( oldTimestamp );

		int secondOfMinute = calendar.get( GregorianCalendar.SECOND );

		return oldTimestamp.timestampByAddingGregorianUnits( 0, 0, 0, 0, 0, -secondOfMinute );
	}

	/**
	 * ???
	 * 
	 * @return
	 */
	public static NSTimestamp beginningOfLastMonth() {
		return normalizeTimestampForMonth( new NSTimestamp() ).timestampByAddingGregorianUnits( 0, -1, 0, 0, 0, 0 );
	}

	/**
	 * Indicates if the two given timestamps are in the same day (midnight to
	 * midnight). If either of the arguments are null, then false is returned.
	 */
	public static boolean inSameDay( NSTimestamp t1, NSTimestamp t2 ) {

		if( t1 == null || t2 == null )
			return false;

		t1 = normalizeTimestampToMidnight( t1 );
		t2 = normalizeTimestampToMidnight( t2 );

		return t1.equals( t2 );
	}

	/**
	 * Indicates if the given timestamp is within the given period, including
	 * the start of the period (not the end).
	 */
	public static boolean inPeriod( NSTimestamp timestamp, NSTimestamp startOfPeriod, NSTimestamp endOfPeriod ) {

		if( timestamp == null )
			return false;

		return ((startOfPeriod == null) || timestamp.equals( startOfPeriod ) || timestamp.after( startOfPeriod )) && ((endOfPeriod == null) || timestamp.before( endOfPeriod ));
	}

	/**
	 * Normalizes a timestamp to the year.
	 */
	public static NSTimestamp normalizeTimestampToBeginningOfYear( NSTimestamp oldTimestamp ) {
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( oldTimestamp );

		int month = calendar.get( GregorianCalendar.MONTH );
		int dayOfMonth = calendar.get( GregorianCalendar.DAY_OF_MONTH );
		int hourOfDay = calendar.get( GregorianCalendar.HOUR_OF_DAY );
		int minuteOfHour = calendar.get( GregorianCalendar.MINUTE );
		int secondOfMinute = calendar.get( GregorianCalendar.SECOND );

		return oldTimestamp.timestampByAddingGregorianUnits( 0, -month, -dayOfMonth + 1, -hourOfDay, -minuteOfHour, -secondOfMinute );
	}

	/**
	 * Returns the timestamp normalized to the beginning of Monday of last week.
	 * If invoked on a Monday.
	 */
	public static NSTimestamp mondayOfLastWeek() {
		NSTimestamp now = normalizeTimestampToMidnight( new NSTimestamp() );
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( now );
		int dayOfWeek = calendar.get( GregorianCalendar.DAY_OF_WEEK );
		return now.timestampByAddingGregorianUnits( 0, 0, -dayOfWeek + GregorianCalendar.MONDAY - 7, 0, 0, 0 );
	}

	/**
	 * @return a timestamp normalized to midnight last monday
	 */
	public static NSTimestamp lastMonday() {
		NSTimestamp now = normalizeTimestampToMidnight( new NSTimestamp() );
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( now );
		int dayOfWeek = calendar.get( GregorianCalendar.DAY_OF_WEEK );
		return now.timestampByAddingGregorianUnits( 0, 0, GregorianCalendar.MONDAY - dayOfWeek, 0, 0, 0 );
	}

	/**
	 * @return a timestamp normalized to one second before midnight next sunday
	 */
	public static NSTimestamp nextSunday() {
		NSTimestamp now = normalizeTimestampToOneSecondBeforeMidnight( new NSTimestamp() );
		GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
		calendar.setTime( now );
		int dayOfWeek = calendar.get( GregorianCalendar.DAY_OF_WEEK );
		int sun = Calendar.SUNDAY;
		return now.timestampByAddingGregorianUnits( 0, 0, Calendar.SUNDAY - dayOfWeek + 7, 0, 0, 0 );
	}

	/**
	 * Normalizes an NSTimestamp to nine o'clock on the next weekday.
	 */
	public static NSTimestamp normalizeTimestampToNineNextWorkingDay( NSTimestamp t ) {
		t = USTimestampUtilities.normalizeTimestampToMidnight( t );
		t = t.timestampByAddingGregorianUnits( 0, 0, 1, 9, 0, 0 );

		while( !USDateUtilities.isWorkday( t ) ) {
			t = t.timestampByAddingGregorianUnits( 0, 0, 1, 0, 0, 0 );
		}

		return t;
	}

	/**
	 * Checks if date is at least one month and one date after priorDate
	 * 
	 * @param date {@link NSTimestamp} must be after priorDate
	 * @param priorDate
	 * @return true if there date is at least one month after priorDate
	 */
	public static boolean moreThanOneMonthFromPriorDateToDate( NSTimestamp date, NSTimestamp priorDate ) {

		// TODO throw Exception
		if( date == null || priorDate == null ) {
			return false;
		}

		// TODO throw Exception
		if( priorDate.after( date ) ) {
			return false;
		}

		boolean currentDateAfterOrEqual = ERXTimestampUtility.dayOfMonth( date ) >= ERXTimestampUtility.dayOfMonth( priorDate );
		boolean sameMonth = ERXTimestampUtility.monthOfYear( date ) == ERXTimestampUtility.monthOfYear( priorDate );
		boolean sameYear = ERXTimestampUtility.yearOfCommonEra( date ) == ERXTimestampUtility.yearOfCommonEra( priorDate );
		boolean currentDateBeforeOrEqual = ERXTimestampUtility.dayOfMonth( date ) <= ERXTimestampUtility.dayOfMonth( priorDate );
		boolean higherMonth = (ERXTimestampUtility.monthOfYear( date )) > ERXTimestampUtility.monthOfYear( priorDate );

		if( currentDateAfterOrEqual && sameMonth && sameYear ) {
			return false;
		}
		else if( currentDateBeforeOrEqual && higherMonth && sameYear ) {
			return false;
		}

		return true;
	}

	/**
	 * Small utility, when using USDate field you do not get the time and when
	 * time is a part of composite key 00:00:00 is not a option, so this returns
	 * the date that is binded with USDatefield and returns it with system time.
	 * BUT there must be another/better way to do this.
	 * 
	 * @return {@link NSTimestamp} with the current time( hour, minute, second )
	 *         added
	 */
	public static NSTimestamp dateWithTime( NSTimestamp date ) {
		// Rename setTimeInTimestamp
		NSTimestamp t = new NSTimestamp();
		return date.timestampByAddingGregorianUnits( 0, 0, 0, ERXTimestampUtility.hourOfDay( t ), ERXTimestampUtility.minuteOfHour( t ), ERXTimestampUtility.secondOfMinute( t ) );
	}

	/**
	 * Creates an NSTimestamp given a string and a format string.
	 */
	public static NSTimestamp toNSTimestamp( String dateAndTimeString, String formatString ) {
		SimpleDateFormat dateFormat = new SimpleDateFormat( formatString );
		try {
			return new NSTimestamp( dateFormat.parse( dateAndTimeString ) );
		}
		catch( ParseException e ) {
			return null;
		}
	}

	/**
	 * Check if [original] is between [start] and [end], inclusive. 
	 */
	public static boolean between( NSTimestamp original, NSTimestamp start, NSTimestamp end ) {
		if( original.getTime() == start.getTime() ) {
			return true;
		}
		if( original.getTime() == end.getTime() ) {
			return true;
		}

		return (original.getTime() > start.getTime()) && (original.getTime() < end.getTime());
	}
}