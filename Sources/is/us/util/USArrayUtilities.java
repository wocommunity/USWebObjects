package is.us.util;

import is.us.wo.util.USC;

import java.util.*;

import org.slf4j.*;

import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.*;

/**
 * Various utility methods applying to NSArrays.
 */

public class USArrayUtilities {

	private static final Logger logger = LoggerFactory.getLogger( USArrayUtilities.class );

	/**
	 * No instances created, ever.
	 */
	private USArrayUtilities() {}

	/**
	 * Returns an array of the first letters of the keyPath of anArray
	 * Note: whitespace as a first letter is ignored.
	 */
	public static NSArray<String> firstLettersForKeyPathInArray( String keyPath, NSArray<? extends NSKeyValueCoding> array ) {

		if( !arrayHasObjects( array ) ) {
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
	 * Returns a new array sorted according to the icelandic alphabet on the given keypath of the array objects
	 * 
	 * @param array the array to sort
	 * @param keypath the keypath to sort by
	 */
	public static <E> NSArray<E> sortedArrayUsingIcelandicComparator( NSArray<E> array, String keypath ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

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
	public static <E> E randomObjectFromArray( NSArray<E> array ) {

		if( !arrayHasObjects( array ) ) {
			return null;
		}

		int count = array.count();
		int number = USC.RANDOM.nextInt( count );
		return array.objectAtIndex( number );
	}

	/**
	 * Constructs a new array containing the specified number of random objects form the source array.
	 * If the array is null, an empty array is returned.
	 * If the source array is shorter or equal in length to the count, the original array is returned randomized.
	 * 
	 * @param array The array to read from.
	 * @param count The max size of the return array.
	 */
	public static <E> NSArray<E> randomObjectsFromArray( NSArray<E> array, int count ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		if( count >= array.count() ) {
			return arrayByRandomizingArray( array );
		}

		NSMutableArray<E> result = new NSMutableArray<E>();

		for( int i = count; i > 0; i-- ) {
			result.addObject( randomObjectFromArray( array ) );
		}

		return result.immutableClone();
	}

	/**
	 * Randomizes the objects in an array
	 */
	public static <E> NSArray<E> arrayByRandomizingArray( NSArray<E> array ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		NSMutableArray<E> originalArray = array.mutableClone();
		NSMutableArray<E> resultArray = new NSMutableArray<E>();

		Enumeration<E> e = originalArray.objectEnumerator();

		while( e.hasMoreElements() ) {
			E o = randomObjectFromArray( originalArray );
			resultArray.addObject( o );
			originalArray.removeObject( o );
		}

		return resultArray.immutableClone();
	}

	/**
	 * Reverses the order of the objects in the given array.
	 */
	public static <E> NSArray<E> arrayByReversingArray( NSArray<E> array ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		Enumeration<E> e = array.reverseObjectEnumerator();
		NSMutableArray<E> b = new NSMutableArray<E>();

		while( e.hasMoreElements() ) {
			b.addObject( e.nextElement() );
		}

		return b;
	}

	/**
	 * Filters an array, so that only the first entry with a distinct value is used.
	 * 
	 * Example: To filter an array of VEHI objects to get only the newest record for each vehicledate.
	 * filterArrayForUniqueRecords( vehicles, Vehi.PERMNO, Vehi.VEHICLEDATE.desc() )
	 * 
	 * @param array And array containing objects implementing KVC.
	 * @param keypath The keypath for the unique value in the returned array. For example, a vehicle's permno.
	 * @param sortOrderings The sortorderings to use.
	 */
	public static NSArray<? extends NSKeyValueCoding> filterArrayForUniqueRecords( NSArray<? extends NSKeyValueCoding> array, String keypath, NSArray<EOSortOrdering> sortOrderings ) {

		NSArray<? extends NSKeyValueCoding> sortedArray = array;

		if( sortOrderings != null ) {
			sortedArray = EOSortOrdering.sortedArrayUsingKeyOrderArray( array, sortOrderings );
		}

		NSMutableArray<NSKeyValueCoding> resultArray = new NSMutableArray<NSKeyValueCoding>();

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
	 */
	public static <E> NSArray<E> arrayWithoutDuplicates( NSArray<E> a ) {

		if( !USArrayUtilities.arrayHasObjects( a ) ) {
			return NSArray.emptyArray();
		}

		NSMutableArray<E> alreadyCheckedObjects = new NSMutableArray<E>();
		NSMutableArray<E> duplicates = new NSMutableArray<E>();

		for( Object o : a ) {
			if( alreadyCheckedObjects.containsObject( o ) && !duplicates.containsObject( o ) ) {
				duplicates.addObject( o );
			}

			alreadyCheckedObjects.addObject( o );
		}

		return duplicates;
	}

	/**
	 * This method filters an NSArray with a given substring string.
	 * It combines one or more elements to a string and compares that value to the substring.
	 * Note, If == -1 then substr can occure anywhere in array element( e.g. instr )
	 */
	public static NSArray filterArrayWithSubstr( NSArray arr, String substr, NSArray keypath, int offset ) {
		return filterArrayWithSubstr( arr, substr, keypath, offset, false );
	}

	/**
	 * This method filters an NSArray with a given substring string.
	 * It combines one or more elements to a string and compares that value to the substring.
	 * Note, If == -1 then substr can occure anywhere in array element( e.g. instr )
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

		if( !arrayHasObjects( array ) ) {
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
}