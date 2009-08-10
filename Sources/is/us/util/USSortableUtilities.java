package is.us.util;

import java.util.Collection;

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
	public static boolean isLast( USSortable object, Collection siblings ) {
		return object.sortNumber().intValue() == (siblings.size() - 1);
	}
}
