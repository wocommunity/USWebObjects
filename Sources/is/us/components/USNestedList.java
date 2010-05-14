package is.us.components;

import is.us.util.USArrayUtilities;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXStatelessComponent;

/**
 * A hierarchical repetition.
 * 
 * Bindings:
 * [item] 		: The item currently being iterated over
 * [list] 		: An NSArray of root elements to display
 * [sublist] 	: The list of subelements to display. An NSArray contained in the item - "item.someArray"
 * [index] 		: index of the repetition in each level of the hierarchy.
 * [isOrdered]	: indicates if we want to generate an ordered or unordered list (ol or ul)
 * 
 * @author Hugi Thordarson
 */

public class USNestedList extends ERXStatelessComponent {

	public USNestedList( WOContext context ) {
		super( context );
	}

	/**
	 * Binding indicating if the list should be ordered. 
	 */
	private boolean isOrdered() {
		return booleanValueForBinding( "isOrdered" );
	}

	/**
	 * Sublist binding 
	 */
	private NSArray<?> sublist() {
		return (NSArray<?>)valueForBinding( "sublist" );
	}

	/**
	 * Ordered or unordered.
	 */
	public String listTagName() {
		return isOrdered() ? "ol" : "ul";
	}

	/**
	 * Determines if the current node has a sublist
	 */
	public boolean hasSublist() {
		return USArrayUtilities.arrayHasObjects( sublist() );
	}
}