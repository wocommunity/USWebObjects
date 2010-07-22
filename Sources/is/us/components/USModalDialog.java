package is.us.components;

import is.us.util.USStringUtilities;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

import er.ajax.AjaxUtils;
import er.extensions.components.ERXComponent;

/**
 * USModalDialog is a modal dialog window based on ModalBox (see below for link).
 * Its similar to Wonders AjaxModalDialog but it does not use ajax to get the content
 * but instead the content of the dialog is in-lined between its elements and rendered 
 * where it is placed on the page.
 * 
 * @see <a href="http://okonet.ru/projects/modalbox/"/>Modalbox Page</a>
 * @see <a href="http://code.google.com/p/modalbox/">Google Group</a>
 * 
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 * @reviewedby Logi Helgu at Nov 16, 2009( see JIRA issue INN-748 )
 */
public class USModalDialog extends ERXComponent {

	private String _title;
	private NSData _linkIcon;
	private String _linkIconMime;
	private String _linkString;
	private String _id = "usmdg";
	private int _height = 0;
	private int _width = 0;
	private boolean _showLink = true;
	private boolean _showOpen = false;

	public USModalDialog( WOContext context ) {
		super( context );
	}

	/**
	 * @return the string used for the link that triggers the dialog to open.
	 */
	public String linkString() {
		return _linkString;
	}

	/**
	 * Sets the string used used for the link that triggers the dialog to open.
	 * @param linkString the links string
	 */
	public void setLinkString( String linkString ) {
		_linkString = linkString;
	}

	/**
	 * @return the id of the element containing the components content
	 */
	public String id() {
		return _id;
	}

	/**
	 * Sets the id of the element containing the components content<br />
	 * You only need to set this if you have multiple modal dialogs on the page
	 * or the default value ("usdmg") conflicts with the id of another element on the page
	 * @param id the id for the element
	 */
	public void setId( String id ) {
		_id = id;
	}

	/**
	 * @return the title of the dialog
	 */
	public String title() {
		if( USStringUtilities.stringHasValue( _title ) ) {
			return _title;
		}
		return "";
	}

	/**
	 * Sets the title of the dialog
	 * @param title the title of the dialog
	 */
	public void setTitle( String title ) {
		_title = title;
	}

	/**
	 * @return the icon used with the link
	 */
	public NSData linkIcon() {
		return _linkIcon;
	}

	/**
	 * Sets the icon to use with the link
	 * @param linkIcon the icon to use with the link
	 */
	public void setLinkIcon( NSData linkIcon ) {
		_linkIcon = linkIcon;
	}

	/**
	 * @return the mime type set for the icon image 
	 */
	public String linkIconMime() {
		if( USStringUtilities.stringHasValue( _linkIconMime ) ) {
			return _linkIconMime;
		}
		return "image/png";
	}

	/**
	 * Sets the mime type for the icon image. The default is "image/png"
	 * @param linkIconMime the icons mime type
	 */
	public void setLinkIconMime( String linkIconMime ) {
		_linkIconMime = linkIconMime;
	}

	/**
	 * @return the desired height of the dialog
	 */
	public int height() {
		return _height;
	}

	/**
	 * Sets the height of the dialog. If not set the dialog will automatically
	 * size to fit the content. See ModalBox documentation
	 * @param height the desired height of the dialog
	 */
	public void setHeight( int height ) {
		_height = height;
	}

	/**
	 * @return the desired width of the dialog 
	 */
	public int width() {
		return _width;
	}

	/**
	 * Sets the width of the dialog. If not set the dialog will automatically
	 * size to fit the content. See ModalBox documentation
	 * @param width the desired width of the dialog
	 */
	public void setWidth( int width ) {
		_width = width;
	}

	public boolean showLink() {
		return _showLink;
	}

	public void setShowLink( boolean showLink ) {
		_showLink = showLink;
	}

	public boolean showOpen() {
		return _showOpen;
	}

	public void setShowOpen( boolean showOpen ) {
		_showOpen = showOpen;
	}

	public String divDisplayStyle() {
		return showOpen() ? "display:display" : "display:none";
	}

	/**
	 * @return the java script used to open the dialog
	 */
	public String openDialogJS() {
		StringBuilder params = new StringBuilder();

		params.append( "title: '" + title() + "'" );
		if( height() > 0 ) {
			params.append( ", height: '" + height() + "'" );
		}
		if( width() > 0 ) {
			params.append( ", width: '" + width() + "'" );
		}

		return "Modalbox.show($('" + id() + "'), {" + params.toString() + "});";
	}

	@Override
	protected NSArray<String> additionalCSSFiles() {
		// Using the css provided by modalbox
		return new NSMutableArray<String>( new String[] { "modalbox.css" } );
	}

	@Override
	protected NSArray<String> additionalJavascriptFiles() {
		// using the js provided by modalbox
		return new NSMutableArray<String>( new String[] { "modalbox.js" } );
	}

	@Override
	public void appendToResponse( WOResponse response, WOContext context ) {
		// The prototype js files are in the AJAX framework, so we need to use 
		// the method from AjaxUtils to load them correctly
		AjaxUtils.addScriptResourceInHead( context, response, "prototype.js" );
		AjaxUtils.addScriptResourceInHead( context, response, "effects.js" );
		super.appendToResponse( response, context );
	}

}