package is.us.components;

import is.us.wo.util.USC;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

/**
 * SWDictionaryEditor allows for graphical editing of property lists.
 *
 * @author Hugi Thordarson
 */

public class USPlistEditorDictionary extends USPlistEditorAbstract {

	/**
	 * The dictionary to work with.
	 */
	public Object selectedDictionary;

	/**
	* the name of a new key to add
	 */
	public String newKey;

	/**
	 * The key currently being iterated over
	 */
	public String currentKey;

	public String currentObjectType;
	public String selectedObjectType;

	public USPlistEditorDictionary( WOContext context ) {
		super( context );
	}

	/**
	    * Returns all key values in the dictionary being edited.
	 */
	public NSArray allKeys() {
		if( selectedDictionary != null )
			return ((NSMutableDictionary)selectedDictionary).allKeys();

		return NSArray.EmptyArray;
	}

	/**
	    * The object coresponding to currentKey.
	 */
	public Object currentItem() {
		return ((NSMutableDictionary)selectedDictionary).valueForKey( currentKey );
	}

	/**
	    * Sets the value of the object identified by currentKey
	 *
	 * @param newValue The value to set
	 */
	public void setCurrentItem( Object newValue ) {
		if( newValue != null )
			((NSMutableDictionary)selectedDictionary).takeValueForKey( newValue, currentKey );
		else
			((NSMutableDictionary)selectedDictionary).takeValueForKey( "", currentKey );
	}

	/**
	 * Adds a new key to the selected dictionary with the value of newKey
	 */
	public WOActionResults addSetting() {
		if( newKey != null ) {

			if( selectedObjectType.equals( TYPE_STRING ) )
				((NSMutableDictionary)selectedDictionary).takeValueForKey( USC.EMPTY_STRING, newKey );

			else if( selectedObjectType.equals( TYPE_DICTIONARY ) )
				((NSMutableDictionary)selectedDictionary).takeValueForKey( new NSMutableDictionary(), newKey );

			else if( selectedObjectType.equals( TYPE_ARRAY ) )
				((NSMutableDictionary)selectedDictionary).takeValueForKey( new NSMutableArray(), newKey );

			newKey = null;
		}

		return null;
	}

	/**
	 * Removes the object identified by currentKey from the dictionary
	 */
	public WOActionResults removeSetting() {
		((NSMutableDictionary)selectedDictionary).removeObjectForKey( currentKey );
		return null;
	}

	/**
	 * Makes a check if the current object is a dictionary or a String
	 */
	public boolean isDictionary() {
		return selectedDictionary instanceof NSDictionary;
	}

	public boolean isArray() {
		return selectedDictionary instanceof NSArray;
	}

	public boolean isString() {
		return selectedDictionary instanceof String;
	}
}