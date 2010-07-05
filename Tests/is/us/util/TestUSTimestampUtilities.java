package is.us.util;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.*;

/**
 * Unit tests for USTimestampUtilities 
 */

public class TestUSTimestampUtilities {

	public NSTimestamp previousDate;
	public NSTimestamp currentDate;

	/**
	 * Setup test dates as the same date( today )
	 */
	@Before
	public void setUpDate() {
		previousDate = ERXTimestampUtilities.today();
		currentDate = previousDate;
	}

	/**
	 * Verify that there isn't more that one month between the same date 
	 */
	@Test
	public void sameDateDoesntHaveMoreThatOneMonthBetweenDates() {

		// Check both dates as today
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );

		// Check same date in mid month
		previousDate = new NSTimestamp( 2007, 2, 10, 0, 0, 0, null );
		currentDate = previousDate;
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
	}

	/**
	 * Today and tomorrow don't have more than one month between them
	 */
	@Test
	public void oneDayDifference() {
		// Set current to next day
		currentDate = previousDate.timestampByAddingGregorianUnits( 0, 0, 1, 0, 0, 0 );
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
	}

	/**
	 *  Boundary check from the beginning of one month to the next
	 */
	@Test
	public void checkEndOfMonth() {
		// Check the end of month( 1. February 2007 )
		previousDate = new NSTimestamp( 2007, 2, 1, 0, 0, 0, null );
		// currentDate is now 1. March 2007( and then there is only one month between the dates, not more than one month )
		currentDate = previousDate.timestampByAddingGregorianUnits( 0, 0, 28, 0, 0, 0 );
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
		// currentDate is now 2. March 2007 and we have 1 month and 1 day between the dates
		currentDate = previousDate.timestampByAddingGregorianUnits( 0, 0, 29, 0, 0, 0 );
		assertEquals( true, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );

		// REVIEW should dates like this be set in new variables like NSTimestamp firstOfFebruary2007 ?

	}

	/**
	 * Verify that dates with a year between them pass 
	 */
	@Test
	public void oneYearBetweenDates() {
		currentDate = previousDate.timestampByAddingGregorianUnits( 1, 0, 0, 0, 0, 0 );
		assertEquals( true, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
	}

	/**
	 * If the prior date is later in time then this returns false( untill the code is changed to return an Exception )
	 */
	@Test
	public void priorDateCannotBeLaterInTime() {
		// Set current before previous
		currentDate = previousDate.timestampByAddingGregorianUnits( 0, 0, -1, 0, 0, 0 );
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
	}

	/**
	 * Null values return false( until the code will be changed to return an Exception )
	 */
	@Test
	public void nullCheck() {
		// Test null
		currentDate = null;
		assertEquals( false, USTimestampUtilities.moreThanOneMonthFromPriorDateToDate( currentDate, previousDate ) );
	}

	/**
	 * Test that dateWithTime is setting the time of a timestamp
	 */
	@Test
	public void testDateWithTimeFunction() {
		NSTimestamp noTime = new NSTimestamp();
		noTime = noTime.timestampByAddingGregorianUnits( 0, 0, 0, -ERXTimestampUtility.hourOfDay( noTime ), -ERXTimestampUtility.minuteOfHour( noTime ), -ERXTimestampUtility.secondOfMinute( noTime ) );
		NSTimestamp tempTime = (NSTimestamp)noTime.clone();

		// verify that no time is set
		assertEquals( 0, ERXTimestampUtility.hourOfDay( noTime ) );
		assertEquals( 0, ERXTimestampUtility.minuteOfHour( noTime ) );
		assertEquals( 0, ERXTimestampUtility.secondOfMinute( noTime ) );
		assertEquals( noTime, tempTime );

		tempTime = USTimestampUtilities.dateWithTime( noTime );

		// by adding time to the tempTime it should after noTime
		assertEquals( true, tempTime.after( noTime ) );
	}

	/**
	 * Test that normalizeTimestampToNineNextWorkingDay is setting the right time and date 
	 */
	@Test
	public void testNormalizeTimestampToNineNextWorkingDay() {
		// Check for weekends
		assertEquals( new NSTimestamp( 2008, 12, 1, 9, 0, 0, null ), USTimestampUtilities.normalizeTimestampToNineNextWorkingDay( new NSTimestamp( 2008, 11, 28, 0, 0, 0, null ) ) ); // Friday passed
		assertEquals( new NSTimestamp( 2008, 12, 1, 9, 0, 0, null ), USTimestampUtilities.normalizeTimestampToNineNextWorkingDay( new NSTimestamp( 2008, 11, 29, 0, 0, 0, null ) ) ); // Saturday passed
		assertEquals( new NSTimestamp( 2008, 12, 1, 9, 0, 0, null ), USTimestampUtilities.normalizeTimestampToNineNextWorkingDay( new NSTimestamp( 2008, 11, 30, 0, 0, 0, null ) ) ); // Sunday passed

		// Check for leap years
		assertEquals( new NSTimestamp( 2008, 2, 29, 9, 0, 0, null ), USTimestampUtilities.normalizeTimestampToNineNextWorkingDay( new NSTimestamp( 2008, 2, 28, 0, 0, 0, null ) ) ); // Friday 29.02.2008 at nine confirmed
		assertEquals( new NSTimestamp( 2007, 3, 1, 9, 0, 0, null ), USTimestampUtilities.normalizeTimestampToNineNextWorkingDay( new NSTimestamp( 2007, 2, 28, 0, 0, 0, null ) ) ); // Thursday 1.03.2007 at nine confirmed
	}
}