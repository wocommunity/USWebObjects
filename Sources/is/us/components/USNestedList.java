package is.us.components;

import is.us.util.USArrayUtilities;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * Displays a hierarchical list.<br />
 * The bindings to use are:
 * <ul>
 * <li> <b>index</b><br />This variable is updates at each iteration in the current level in the hierarchy.
 * <li> <b>item</b><br />The current item being iterated over in the list
 * <li> <b>list</b><br />An NSArray of root elements to display
 * <li> <b>sublist</b><br />The list of subelements to display. An NSArray contained in the item - "item.someArray"
 *
 * @author Hugi Þórðarson
 * @version 2.9.2b6
 * @since 2.5
 */

public class USNestedList extends ERXComponent {

	public USNestedList( WOContext context ) {
		super( context );
	}

	/**
	 * Determines if the list should be displayed as an ordered list or an unnumbered list.
	 */
	public String listTagName() {
		return valueForBinding( "isOrdered" ) != null ? "ol" : "ul";
	}

	/**
	 * Determines if the current node has a sublist
	 */
	public boolean hasSublist() {
		return USArrayUtilities.arrayHasObjects( (NSArray)valueForBinding( "sublist" ) );
	}

	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	public boolean isStateless() {
		return true;
	}
}