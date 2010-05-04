package is.us.util;

import java.util.Enumeration;

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

import er.extensions.foundation.ERXArrayUtilities;

/**
 * Allows the programmer to do various things with objects that implement the USHierarchy interface
 *
 * @author Hugi Þórðarson
 */

public class USHierarchyUtilities {

	/**
	 * Indicates if the node has subnodes.
	 */
	public static boolean hasChildren( USHierarchy object ) {
		return USArrayUtilities.arrayHasObjects( object.children() );
	}

	/**
	 * Indicates if the node is at the top of the hiearchy (has no parent).
	 */
	public static boolean isRoot( USHierarchy object ) {
		boolean isRoot = object.parent() == null;
		return isRoot;
	}

	/**
	 * Returns nodes at the same level as this one in the hierarchy.
	 */
	public static NSArray siblings( USHierarchy object ) {

		if( isRoot( object ) )
			return new NSArray( object );

		return (object.parent()).children();
	}

	/**
	 * Returns an array with all nodes below this one in the hierarchy.
	 *
	 * @param anObject the SWHierarchy object
	 * @param includingSelf indicates if the topLevePage should be included in the array.
	 */
	public static NSArray everyChild( USHierarchy anObject, boolean includingSelf ) {

		NSMutableArray tempArray = new NSMutableArray();

		if( includingSelf )
			tempArray.addObject( anObject );

		if( hasChildren( anObject ) ) {
			Enumeration e = anObject.children().objectEnumerator();

			while( e.hasMoreElements() ) {
				USHierarchy a = (USHierarchy)e.nextElement();
				tempArray.addObject( a );
				if( hasChildren( a ) )
					tempArray.addObjectsFromArray( everyChild( a, true ) );
			}
		}

		return tempArray;
	}

	/**
	 * Indicates if child is a subnode of parent.
	 * 
	 * @param includingSelf indicates if child should be checked against itself as well.
	 */
	public static boolean isParentNodeOfNode( USHierarchy parent, USHierarchy child, boolean includingSelf ) {
		return everyParentNode( child, includingSelf ).containsObject( parent );
	}

	/**
	 * Tells us if the specified page owes inheritance to the specified page
	 *
	 * @param child the page to check against
	 * @param parent
	 * @param includingTopLevel Indicates if the object should be checked against itself as well as it's children.
	 */
	public static boolean isChildOfNode( USHierarchy child, USHierarchy parent, boolean includingTopLevel ) {
		return isParentNodeOfNode( parent, child, includingTopLevel );
	}

	/**
	 * Returns the root node of the hierarchy.
	 * 
	 * @param object The object to find the root for.
	 */
	public static USHierarchy root( USHierarchy object ) {

		USHierarchy h = object;

		while( !isRoot( h ) ) {
			h = h.parent();
		}

		return h;
	}

	/**
	 * Returns an array of all parent pages. includeSelf indicates if the calling page should be included.
	 * Order of the array starts with the given page and then walks upward, ending with the top level node.
	 */
	public static NSArray everyParentNode( USHierarchy anObject, boolean includeSelf ) {

		if( anObject == null )
			return NSArray.EmptyArray;

		USHierarchy h = anObject;

		NSMutableArray tempArray = new NSMutableArray();

		if( includeSelf )
			tempArray.addObject( h );

		while( h.parent() != null ) {
			h = h.parent();
			tempArray.addObject( h );
		}

		return tempArray;
	}

	/**
	 * Returns an array containing all parent pages in reverse order. includeSelf indicates if the calling page should be included.
	 */
	public static NSArray everyParentNodeReversed( USHierarchy anObject, boolean includeSelf ) {
		return ERXArrayUtilities.reverse( everyParentNode( anObject, includeSelf ) );
	}

	/**
	 * Returns the page at the specified index in the parent page hierarchy. 0 is the front page of the site, 1 is the subpage of that page etc.
	 */

	public static USHierarchy parentNodeAtLevel( USHierarchy anObject, int aLevel ) {

		try {
			NSArray anArray = everyParentNode( anObject, true );
			return (USHierarchy)anArray.objectAtIndex( anArray.count() - aLevel );
		}
		catch( Exception e ) {
			return null;
		}
	}

	/**
	 * All children, sorted using the given keyOrderArray.
	 */
	public static NSArray sortedChildren( USHierarchy anObject, NSArray<EOSortOrdering> sortOrderings ) {
		return EOSortOrdering.sortedArrayUsingKeyOrderArray( anObject.children(), sortOrderings );
	}

	/**
	 * All nodes corresponding to qualifier, sorted by sortOrderings.
	 */
	public static NSArray<? extends USHierarchy> sortedAndQualifiedSubNodes( USHierarchy anObject, NSArray<EOSortOrdering> sortOrderings, EOQualifier qualifier ) {
		NSArray<? extends USHierarchy> tempArray = sortedChildren( anObject, sortOrderings );
		return EOQualifier.filteredArrayWithQualifier( tempArray, qualifier );
	}

	/**
	 * Searches for the given keypath in the hierarchy, from the specified object and upwards
	 */
	public static Object valueInHierarchyForKeyPath( USHierarchy anObject, String keyPath ) {

		NSArray<USHierarchy> everyParentNode = everyParentNode( anObject, true );

		for( USHierarchy nextLevel : everyParentNode ) {
			//			Object returnValue = nextLevel.valueForKeyPath( keyPath );
			Object returnValue = NSKeyValueCodingAdditions.Utility.valueForKeyPath( nextLevel, keyPath );

			if( returnValue != null ) {
				return returnValue;
			}
		}

		return null;
	}
}