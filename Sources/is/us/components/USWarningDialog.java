package is.us.components;

import java.util.*;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

/**
 * Component to display a list of warning/error messages
 * 
 * @binding messages required, List of messages to display.
 * @binding type optional, The type of messages "error" or "warning". "warning" is default.
 * 
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 * @reviewedby Logi Helgu at Oct 12, 2009( see JIRA issue INN-733 )
 */
public class USWarningDialog extends ERXComponent {

	private static final String TYPE_WARNING = "warning";
	private static final String TYPE_ERROR = "error";

	private List<String> _messages;
	private String _type = TYPE_WARNING;

	public String currentMessage;

	public USWarningDialog( WOContext context ) {
		super( context );
	}

	@Override
	protected boolean useDefaultComponentCSS() {
		return true;
	}

	/**
	 * @return the messages to display
	 */
	public List<String> messages() {
		if( _messages == null ) {
			_messages = new ArrayList<String>();
		}

		return _messages;
	}

	/**
	 * @param messages the messages to display
	 */
	public void setMessages( List<String> messages ) {
		_messages = messages;
	}

	/**
	 * @return the type of messages
	 */
	public String type() {
		return _type;
	}

	/**
	 * @param type the type of messages
	 */
	public void setType( String type ) {
		_type = type;
	}

	/**
	 * @return the css class name to use for the messages
	 */
	public String className() {
		return type().equalsIgnoreCase( TYPE_ERROR ) ? "errorMessages" : "warningMessages";
	}
}