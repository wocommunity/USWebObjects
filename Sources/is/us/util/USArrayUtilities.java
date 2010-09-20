package is.us.util;

import is.us.wo.util.USC;

import java.util.*;

import org.slf4j.*;

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import com.webobjects.foundation.NSComparator.ComparisonException;

import er.extensions.foundation.ERXArrayUtilities;

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
	 * Note: Strings are trimmed, meaning whitespace before strings is ignored.
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

		return sortedArrayUsingIcelandicComparator( set.allObjects() );
	}

	/**
	 * Returns a new array sorted according to the Icelandic alphabet.
	 * 
	 * @param array the array to sort
	 * @param keyPath the keypath to sort by
	 */
	public static NSArray<String> sortedArrayUsingIcelandicComparator( NSArray<String> array ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		try {
			return array.sortedArrayUsingComparator( USGenericComparator.IcelandicAscendingComparator );
		}
		catch( ComparisonException e ) {
			logger.error( "Could not sort array", e );
			return array;
		}
	}

	/**
	 * Returns a new array sorted according to the icelandic alphabet on the given keypath of the array objects
	 * 
	 * @param array the array to sort
	 * @param keyPath the keyPath to sort by
	 */
	public static <E> NSArray<E> sortedArrayUsingIcelandicComparator( NSArray<E> array, String keyPath ) {

		if( !arrayHasObjects( array ) ) {
			return NSArray.emptyArray();
		}

		try {
			USGenericComparator icelandicComparator = new USGenericComparator( java.text.Collator.getInstance( new java.util.Locale( "is", "IS" ) ), keyPath, true, true );
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
		E object = array.objectAtIndex( number );
		return object;
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

		NSMutableArray<E> originalArrayMutable = array.mutableClone();
		NSMutableArray<E> result = new NSMutableArray<E>();

		for( int i = count; i > 0; i-- ) {
			E randomObject = randomObjectFromArray( originalArrayMutable );
			result.addObject( randomObject );
			originalArrayMutable.removeObject( randomObject );
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

		while( originalArray.count() > 0 ) {
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
	 * Example: To filter an array of objects that have a key called "timestamp" to get only the newest record for each "id".
	 * filterArrayForUniqueRecords( objects, SomeEntity.ID, SomeEntity.TIMESTAMP.descs() )
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

		for( E o : a ) {
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

		if( maxCount < 0 ) {
			throw new IllegalArgumentException( "The parameter 'maxCount' must be a positive integer" );
		}

		if( array.count() <= maxCount ) {
			return array;
		}

		return array.subarrayWithRange( new NSRange( 0, maxCount ) );
	}

	/**
	 * Groups an array by multiple keypaths
	 * 
	 * @param objects An array of objects to group.
	 * @param keyPaths The keypaths to group the objects by.
	 * @return A dictionary with the "distinct" values as a key, and the objects conforming to that distinct value set as object. 
	 */
	public static <E extends NSKeyValueCodingAdditions> NSDictionary<NSDictionary<String, Object>, NSArray<E>> arrayGroupedByKeyPaths( NSArray<E> objects, NSArray<String> keyPaths ) {

		if( objects == null ) {
			return NSDictionary.emptyDictionary();
		}

		if( keyPaths == null ) {
			throw new IllegalArgumentException( "The 'keyPaths' parameter must not be null" );
		}

		NSArray<NSDictionary<String, Object>> distinctObjects = distinctCombinationsWithKeyPaths( objects, keyPaths );

		NSMutableDictionary<NSDictionary<String, Object>, NSArray<E>> result = new NSMutableDictionary<NSDictionary<String, Object>, NSArray<E>>();

		for( NSDictionary<String, Object> distinctObject : distinctObjects ) {
			EOQualifier distinctQualifier = EOQualifier.qualifierToMatchAllValues( distinctObject );
			NSArray<E> filteredArray = EOQualifier.filteredArrayWithQualifier( objects, distinctQualifier );
			result.setObjectForKey( filteredArray, distinctObject );
		}

		return result;
	}

	/**
	 * Construct an array of dictionaries with all value combinations in the given keyPaths.
	 * 
	 * @param objects The object array to go through
	 * @param keyPaths The keyPaths to look at 
	 * @return An array of distinct objects
	 */
	public static <E extends NSKeyValueCodingAdditions> NSArray<NSDictionary<String, Object>> distinctCombinationsWithKeyPaths( NSArray<E> objects, NSArray<String> keyPaths ) {

		if( objects == null ) {
			return NSArray.emptyArray();
		}

		if( keyPaths == null ) {
			throw new IllegalArgumentException( "The 'keyPaths' parameter must not be null" );
		}

		NSMutableSet<NSDictionary<String, Object>> a = new NSMutableSet<NSDictionary<String, Object>>();

		for( E object : objects ) {
			NSMutableDictionary<String, Object> d = new NSMutableDictionary<String, Object>();
			for( String keyPath : keyPaths ) {
				Object value = object.valueForKeyPath( keyPath );

				if( value != null ) {
					d.setObjectForKey( value, keyPath );
				}
				else {
					d.setObjectForKey( NSKeyValueCoding.NullValue, keyPath );
				}
			}
			a.addObject( d );
		}

		return a.allObjects();
	}

	/**
	 * Fetch the distinct values of a keyPath.
	 */
	public static NSArray distinctValuesForKeyPath( NSArray objects, String keyPath ) {

		if( objects == null ) {
			return NSArray.emptyArray();
		}

		if( keyPath == null ) {
			throw new IllegalArgumentException( "The 'keyPath' parameter must not be null" );
		}

		NSArray result = (NSArray)objects.valueForKeyPath( keyPath );
		result = ERXArrayUtilities.arrayWithoutDuplicates( result );
		return result;
	}
}