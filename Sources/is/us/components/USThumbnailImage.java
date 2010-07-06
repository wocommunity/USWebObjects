package is.us.components;

import is.us.util.*;
import is.us.util.USImageUtilities.CodecType;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;

import er.extensions.components.ERXNonSynchronizingComponent;

/**
 * Will display the image contained in an NSData object, resized to fit the specified constraints.
 * 
 * @author Hugi Thordarson
 */

public class USThumbnailImage extends ERXNonSynchronizingComponent {

	private NSData _data;

	public USThumbnailImage( WOContext context ) {
		super( context );
	}

	public String id() {
		return valueForStringBinding( "id", null );
	}

	private int maxHeight() {
		return valueForIntegerBinding( "maxHeight", 0 );
	}

	private int maxWidth() {
		return valueForIntegerBinding( "maxWidth", 0 );
	}

	/**
	 * 
	 * @return
	 */
	public NSData data() {
		if( _data == null ) {
			NSData data = (NSData)valueForBinding( "data" );

			if( maxHeight() > 0 || maxWidth() > 0 ) {
				return new NSData( USImageUtilities.createThumbnail( data.bytes(), maxWidth(), maxHeight(), 100, CodecType.JPEG ) );
			}
			else {
				_data = data;
			}
		}

		return _data;
	}
}