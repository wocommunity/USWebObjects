package is.us.util;

import is.us.wo.util.USC;

import java.lang.reflect.Array;
import java.util.*;

import org.slf4j.*;

import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.*;

/**
 * Various utility methods for handling of arrays.
 * 
 * @author Hugi Þórðarson
 */

public class USArrayUtilities {

	private static final Logger logger = LoggerFactory.getLogger( USArrayUtilities.class );

	/**
	 * No instances created, ever.
	 */
	private USArrayUtilities() {}

	/**
	 * Returns an String array of the first letters of the keyPath of anArray
	 * Note: whitespace as a first letter is ignored.
	 */
	public static NSArray<String> firstLettersForKeyPathInArray( String keyPath, NSArray<? extends NSKeyValueCoding> array ) {

		if( array == null ) {
			return NSArray.emptyArray();
		}

		NSMutableSet<String> set = new NSMutableSet<String>();

		for( NSKeyValueCoding kvc : array ) {
			String stringValue = ((String)kvc.valueForKey( keyPath )).trim();

			if( stringValue != null && stringValue.length() > 0 ) {
				set.addObject( stringValue.substring( 0, 1 ).toUpperCase() );
			}
		}
		try {
			return set.allObjects().sortedArrayUsingComparator( USGenericComparator.IcelandicAscendingComparator );
		}
		catch( Exception e ) {
			logger.warn( "Could not sort objects in set", e );
			return set.allObjects();
		}
	}

	/**
	 * Slices [array] into [columns] number of arrays, adding [paddingObject] to fill up the last column
	 */
	public static <E> NSArray<NSArray<E>> arrayByTransposingArray( NSArray<E> array, int columns, E paddingObject ) {

		if( !USArrayUtilities.arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		NSMutableArray<E> arrayClone = array.mutableClone();

		while( arrayClone.count() % columns != 0 ) {
			arrayClone.addObject( paddingObject );
		}

		int countInEachColumn = arrayClone.count() / columns;

		NSMutableArray columnArray = new NSMutableArray( columns );

		for( int i = 0; columns > i; i++ ) {
			columnArray.addObject( arrayClone.subarrayWithRange( new NSRange( i * countInEachColumn, countInEachColumn ) ) );
		}

		NSMutableArray rowArray = new NSMutableArray();

		for( int i = 0; countInEachColumn > i; i++ ) {
			NSMutableArray row = new NSMutableArray();

			for( int j = 0; columns > j; j++ ) {
				row.addObject( ((NSArray)columnArray.objectAtIndex( j )).objectAtIndex( i ) );
			}

			rowArray.addObject( row );
		}

		return rowArray;
	}

	/**
	 * Returns a new array sorted according to the icelandic alphabet on the given keypath of the array objects
	 * 
	 * @param array the array to sort
	 * @param keypath the keypath to sort by
	 */
	public static <E> NSArray<E> sortedArrayUsingIcelandicComparator( NSArray<E> array, String keypath ) {

		if( !arrayHasObjects( array ) )
			return NSArray.emptyArray();

		try {
			USGenericComparator icelandicComparator = new USGenericComparator( java.text.Collator.getInstance( new java.util.Locale( "is", "IS" ) ), keypath, true, true );
			return array.sortedArrayUsingComparator( icelandicComparator );
		}
		catch( Exception e ) {
			logger.warn( "Unable to sort array, returning original array", e );
			return array;
		}
	}

	/**
	 * Returns a random object from the specified array. If the array contains no objects or is null, null is returned.
	 */
	public static <E> E randomObjectFromArray( NSArray<E> anArray ) {

		if( anArray == null )
			return null;

		int count = anArray.count();

		if( count == 0 )
			return null;

		int number = USC.RANDOM.nextInt( count );
		return anArray.objectAtIndex( number );
	}

	/**
	 * Reverses the order of the objects in the given array.
	 */
	public static <E> NSArray<E> arrayByReversingArray( NSArray<E> a ) {

		if( !arrayHasObjects( a ) )
			return NSArray.emptyArray();

		Enumeration<E> e = a.reverseObjectEnumerator();
		NSMutableArray<E> b = new NSMutableArray<E>();

		while( e.hasMoreElements() ) {
			b.addObject( e.nextElement() );
		}

		return b;
	}

	/**
	 * Randomizes the objects in an array
	 */
	public static <E> NSArray<E> arrayByRandomizingArray( NSArray<E> a ) {

		if( !arrayHasObjects( a ) ) {
			return NSArray.emptyArray();
		}

		NSMutableArray<E> originalArray = a.mutableClone();
		NSMutableArray<E> resultArray = new NSMutableArray<E>();

		Enumeration<E> e = originalArray.objectEnumerator();

		while( e.hasMoreElements() ) {
			E o = randomObjectFromArray( originalArray );
			resultArray.addObject( o );
			originalArray.removeObject( o );
		}

		return resultArray;
	}

	/**
	 * Filters an array, so that only the first entry with a distinct value is used.
	 * 
	 * @param array And array containing objects implementing KVC.
	 * @param keypath The keypath for the unique value in the returned array. For example, a vehicle's permno.
	 * @param sortOrderings The sortorderings to use.
	 * 
	 * Example:
	 * To filter an array of VEHI objects to get only the newest record for each vehicledate.
	 * 
	 * filterArrayForUniqueRecords( vehicles, Vehi.PERMNO, Vehi.VEHICLEDATE.descs() )
	 *
	 * @author Hugi Þórðarson
	 */
	public static NSArray<? extends NSKeyValueCoding> filterArrayForUniqueRecords( NSArray<? extends NSKeyValueCoding> array, String keypath, NSArray<EOSortOrdering> sortOrderings ) {

		NSArray<? extends NSKeyValueCoding> sortedArray = EOSortOrdering.sortedArrayUsingKeyOrderArray( array, sortOrderings );
		NSMutableArray<NSKeyValueCoding> resultArray = new NSMutableArray();

		String lastAddedValue = null;

		for( NSKeyValueCoding v : sortedArray ) {
			String nextValue = (String)v.valueForKey( keypath );

			if( !nextValue.equals( lastAddedValue ) ) {
				resultArray.addObject( v );
				lastAddedValue = nextValue;
			}
		}

		return resultArray;
	}

	/**
	 * This method will check an NSArray for duplicate objects.
	 * If it finds any duplicates, it will return an NSArray containing one instance of each duplicate object.
	 * 
	 * @author Hugi Þórðarson
	 */
	public static NSArray findDuplicatesInArray( NSArray a ) {

		if( !USArrayUtilities.arrayHasObjects( a ) )
			return NSArray.EmptyArray;

		NSMutableArray alreadyCheckedObjects = new NSMutableArray();
		NSMutableArray duplicates = new NSMutableArray();

		for( Object o : a ) {
			if( alreadyCheckedObjects.containsObject( o ) && !duplicates.containsObject( o ) )
				duplicates.addObject( o );

			alreadyCheckedObjects.addObject( o );
		}

		return duplicates;
	}

	/**
	 * This method filters an NSArray with a given substring string.
	 * It combines one or more elements to a string and compares that value to the substring.
	 * Note, If == -1 then substr can occure anywhere in array element( e.g. instr )
	 * 
	 * @author Bjarni Sævarsson
	 */
	public static NSArray filterArrayWithSubstr( NSArray arr, String substr, NSArray keypath, int offset ) {
		return filterArrayWithSubstr( arr, substr, keypath, offset, false );
	}

	/**
	 * This method filters an NSArray with a given substring string.
	 * It combines one or more elements to a string and compares that value to the substring.
	 * Note, If == -1 then substr can occure anywhere in array element( e.g. instr )
	 * 
	 * @author Bjarni Sævarsson
	 */
	public static NSArray filterArrayWithSubstr( NSArray arr, String substr, NSArray keypath, int offset, boolean caseInsensitive ) {

		if( !USStringUtilities.stringHasValue( substr ) || !arrayHasObjects( arr ) )
			return arr;

		String pattern = substr;
		int patternLen = pattern.length();
		int minElementLen = patternLen + ((offset >= 0) ? offset : 0);
		if( caseInsensitive )
			pattern = pattern.toUpperCase();

		NSMutableArray filteredArray = new NSMutableArray();
		Enumeration e = arr.objectEnumerator();

		while( e.hasMoreElements() ) {
			NSKeyValueCoding objEl = (NSKeyValueCoding)e.nextElement();
			String objStr = "";
			for( int i = 0; i < keypath.count(); i++ )
				objStr += (String)objEl.valueForKey( (String)keypath.get( i ) );

			if( (objStr == null) || (objStr.length() < minElementLen) )
				continue;

			if( caseInsensitive )
				objStr = objStr.toUpperCase();

			if( offset >= 0 ) {
				objStr = objStr.substring( offset, patternLen );
				if( pattern.equals( objStr ) )
					filteredArray.addObject( objEl );
			}
			else if( objStr.indexOf( pattern ) >= 0 )
				filteredArray.addObject( objEl );
		}

		return filteredArray;
	}

	/**
	 * Wrapper function to join collections.
	 * 
	 * @param collection
	 * @param separator
	 * @return
	 * @author Bjarni Sævarsson
	 */
	public static String join( Collection<? extends Object> collection, String separator ) {

		if( collection == null ) {
			return null;
		}

		return USArrayUtilities.join( collection.toArray(), separator );
	}

	/**
	 * Joins an array, using the delimiter, into a string.
	 * 
	 * @param arr
	 * @param delimiter
	 * @return String
	 * @author Bjarni Sævarsson
	 */
	public static String join( Object[] collection, String separator ) {

		if( collection == null ) {
			return null;
		}

		StringBuffer retString = new StringBuffer();
		for( int i = 0; i < collection.length; i++ ) {
			if( (i > 0) && (separator != null) ) {
				retString.append( separator );
			}
			retString.append( String.valueOf( collection[i] ) );
		}
		return retString.toString();
	}

	/**
	 * Create an NSArray from arbitrary parameters
	 *
	 * @param objs An Object..., stored in returned NSArray
	 * @return An NSArray containing objs
	 */
	public static <E> NSArray<E> nsArray( E... objs ) {
		return new NSArray<E>( objs );
	}

	/**
	 * Returns true if the given array object is not null and contains object
	 */
	public static boolean arrayHasObjects( List<?> array ) {
		return array != null && array.size() > 0;
	}

	/**
	 * Returns the array unmodified if it contains [maxCount] or fewer items, otherwise
	 * it returns the first [maxCount] items.
	 * 
	 * @param array The array to work with
	 * @param maxCount The maximum number of items to return
	 */
	public static <E> NSArray<E> maxObjectsFromArray( NSArray<E> array, Integer maxCount ) {

		if( array == null || array.count() == 0 ) {
			return NSArray.emptyArray();
		}

		if( maxCount == null ) {
			return array;
		}

		if( array.count() > maxCount ) {
			return array.subarrayWithRange( new NSRange( 0, maxCount ) );
		}

		return array;
	}

	// TODO A handful of unit tests would look good for these utility functions ;) -> there are unit tests in Tests.is.us.util.TestUSArrayUtilities :)
	/**
	 * Searches for an element in an unsorted array
	 * 
	 * @param arr array to search through
	 * @param elementToFind element to find
	 * @return index of element in the array, if element is not found then -1
	 * 
	 * @reviewedby Logi Helgu at Oct 13, 2009( see JIRA issue INN-739 )
	 */
	public static <T> int searchUnsorted( T[] arr, T elementToFind ) {
		if( (arr == null) || (elementToFind == null) || (arr.length < 1) ) {
			return -1;
		}
		for( int i = 0; i < arr.length; i++ ) {
			if( arr[i].equals( elementToFind ) ) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Resizes a array.
	 * 
	 * @param arr array to resize
	 * @param newSize new array size
	 * @return resized array
	 * @exception  IndexOutOfBoundsException  if copying would cause
	 *               access of data outside array bounds.
	 * @exception  ArrayStoreException  if an element in the <code>src</code>
	 *               array could not be stored into the <code>dest</code> array
	 *               because of a type mismatch.
	 * @exception  NullPointerException if either <code>src</code> or
	 *               <code>dest</code> is <code>null</code>.
	 * 
	 * @reviewedby Logi Helgu at Oct 13, 2009( see JIRA issue INN-739 )
	 */
	@SuppressWarnings( "unchecked" )
	public static <T> T[] resize( T[] arr, int newSize ) {
		if( newSize < 1 ) {
			return (T[])Array.newInstance( arr.getClass().getComponentType(), 0 );
		}
		T[] newArr = (T[])Array.newInstance( arr.getClass().getComponentType(), newSize );
		System.arraycopy( arr, 0, newArr, 0, arr.length );
		return newArr;
	}
}
