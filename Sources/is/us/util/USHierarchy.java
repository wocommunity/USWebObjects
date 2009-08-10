package is.us.util;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

/**
* USHierarchy can be implemented by any class to take advantage of the utility methods in USHierarcyUtilities.
*
* @author Hugi Þórðarson
*/

public interface USHierarchy<E extends USHierarchy> extends EOEnterpriseObject {

	/**
	 * This node's parent node
	 */
	public E parent();

	/**
	 * This node's child nodes
	 */
	public NSArray<E> children();
}
