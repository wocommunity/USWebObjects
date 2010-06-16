package is.us.util;

import java.util.*;

import com.webobjects.foundation.NSMutableArray;

/**
 * Mixin for use in objects that implement the SWSortable interface.
 * 
 * @author Hugi Þórðarson
 */

public abstract class USSortableUtilities {

	/**
	 * Indicates that this element is the first in it's list. 
	 */
	public static boolean isFirst( USSortable object ) {
		return object.sortNumber().intValue() == 0;
	}

	/**
	 * Indicates that this element is the last in it's list. 
	 */
	public static boolean isLast( USSortable object, Collection<? extends USSortable> siblings ) {
		return object.sortNumber().intValue() == (siblings.size() - 1);
	}

	/**
	 * Indicates that this element is the last in it's list. 
	 */
	public static boolean moveUp( USSortable object, Collection<? extends USSortable> siblings ) {
		return object.sortNumber().intValue() == (siblings.size() - 1);
	}

	public static void changeSortOrder( USSortable object, NSMutableArray<USSortable> siblings, int offset ) {
		int i = siblings.indexOf( object );
		siblings.remove( i );
		siblings.insertObjectAtIndex( object, i + offset );

		// Go through all components and resort
		Enumeration<USSortable> e = siblings.objectEnumerator();

		while( e.hasMoreElements() ) {
			USSortable sibling = e.nextElement();
			sibling.setSortNumber( siblings.indexOf( sibling ) );
		}
	}
}
