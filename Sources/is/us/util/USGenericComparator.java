package is.us.util;

import is.us.wo.util.USC;

import java.text.Collator;
import java.util.Locale;

import com.webobjects.foundation.*;

/**
 * A comparator for comparing/sorting objects that implement KVC
 * based on a keypath.
 * 
 * Example use:
 * GenericComparator icelandicComparator = new GenericComparator( java.text.Collator.getInstance( new java.util.Locale( "is", "IS" ) ), "my.keypath", true, true);
 * myVeryOwnNSArray.sortUsingComparator( icelandicComparator  );
 *
 * @author Hugi Þórðarson
 */

public class USGenericComparator extends NSComparator {

	private java.text.Collator _collator;
	private String _keyPath;
	private boolean _ascending;
	private boolean _caseInsensitive;
	/**
	 * A Comparator for sorting Icelandic text alphabetically in a descending order.
	 */
	public static final NSComparator IcelandicDescendingComparator = new USGenericComparator._IcelandicComparator( false, true );
	/**
	 * A Comparator for sorting Icelandic text alphabetically in an ascending order.
	 */
	public static final NSComparator IcelandicAscendingComparator = new USGenericComparator._IcelandicComparator( true, true );

	public USGenericComparator( java.text.Collator collator, String keyPath, boolean ascending, boolean caseInsensitive ) {
		_collator = collator;
		_keyPath = keyPath;
		_ascending = ascending;
		_caseInsensitive = caseInsensitive;
	}

	public int compare( Object kvc1, Object kvc2 ) throws ComparisonException {

		String string1 = (String)NSKeyValueCodingAdditions.Utility.valueForKeyPath( kvc1, _keyPath );
		String string2 = (String)NSKeyValueCodingAdditions.Utility.valueForKeyPath( kvc2, _keyPath );

		if( string1 == null )
			string1 = USC.EMPTY_STRING;

		if( string2 == null )
			string2 = USC.EMPTY_STRING;

		if( string1 == string2 )
			return NSComparator.OrderedSame;

		int i = _collator.compare( string1, string2 );

		if( _caseInsensitive )
			i = _collator.compare( string1.toUpperCase(), string2.toUpperCase() );
		else
			i = _collator.compare( string1, string2 );

		if( i < 0 )
			return _ascending ? NSComparator.OrderedAscending : NSComparator.OrderedDescending;

		if( i > 0 )
			return _ascending ? NSComparator.OrderedDescending : NSComparator.OrderedAscending;
		else
			return NSComparator.OrderedSame;
	}

	/**
	 * A class for sorting string objects in correct Icelandic order.
	 *
	 * @author Hugi Þórðarson
	 */
	private static class _IcelandicComparator extends NSComparator {

		private boolean _ascending;
		private boolean _caseInsensitive;
		private static Collator collator = Collator.getInstance( new Locale( "is", "IS" ) );

		public int compare( Object obj, Object obj1 ) throws ComparisonException {

			if( obj == null || obj1 == null || !(obj instanceof String) || !(obj1 instanceof String) )
				throw new ComparisonException( "Unable to compare objects. Objects should be instance of class String. Comparison was made with " + obj + " and " + obj1 + "." );

			if( obj == obj1 )
				return 0;

			int i = collator.compare( (String)obj, (String)obj1 );

			if( _caseInsensitive )
				i = collator.compare( ((String)obj).toUpperCase(), ((String)obj1).toUpperCase() );
			else
				i = collator.compare( (String)obj, (String)obj1 );

			if( i < 0 )
				return _ascending ? -1 : 1;

			if( i > 0 )
				return _ascending ? 1 : -1;
			else
				return 0;
		}

		public _IcelandicComparator() {
			this( true, true );
		}

		public _IcelandicComparator( boolean flag, boolean flag1 ) {
			_ascending = flag;
			_caseInsensitive = flag1;
		}
	}
}