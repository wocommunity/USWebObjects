package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXStatelessComponent;

/**
 * Allows you to adda  Facebook comment box on your website.
 * 
 * For documentation on customization, see:
 * http://developers.facebook.com/docs/reference/plugins/comments
 * http://wiki.developers.facebook.com/index.php/Comments_Box
 * 
 * @author Hugi Thordarson
 */

public class USFBComments extends ERXStatelessComponent {

	public USFBComments( WOContext context ) {
		super( context );
	}
}