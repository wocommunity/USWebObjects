package is.us.components;

import is.us.wo.util.USC;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

/**
 * For editing an array of objects.
 * 
 * @author Hugi Þórðarson
 */

public class USPlistEditorArray extends USPlistEditorAbstract {

	public int index;
	public NSMutableArray selectedArray;
	public String currentObjectType;
	public String selectedObjectType;

	public USPlistEditorArray( WOContext context ) {
		super( context );
	}

	public Object currentObject() {
		return selectedArray.objectAtIndex( index );
	}

	public void setCurrentObject( Object newObject ) {
		if( newObject != null )
			selectedArray.replaceObjectAtIndex( newObject, index );
	}

	public WOActionResults addElement() {

		if( selectedObjectType.equals( TYPE_STRING ) )
			selectedArray.addObject( USC.EMPTY_STRING );

		else if( selectedObjectType.equals( TYPE_DICTIONARY ) )
			selectedArray.addObject( new NSMutableDictionary() );

		else if( selectedObjectType.equals( TYPE_ARRAY ) )
			selectedArray.addObject( new NSMutableArray() );

		return null;
	}

	public WOActionResults removeElement() {
		selectedArray.removeObjectAtIndex( index );
		return null;
	}
}
