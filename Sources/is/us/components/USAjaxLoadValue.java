package is.us.components;

import com.webobjects.appserver.WOContext;

import er.extensions.appserver.ERXWOContext;
import er.extensions.components.ERXNonSynchronizingComponent;

/**
 * Monitors a given property and stops when it is not null and updates the component content
 * 
 * @binding id the id of the component, if not set a unique id will be used.
 * @binding propertyToMonitor the property to monitor
 * @binding showLoadingIndicator optional if true then a loading indicator is displayed when the content is not loaded, true as a default.
 * 
 */
public class USAjaxLoadValue extends ERXNonSynchronizingComponent {

	//private static Logger logger = Logger.getLogger( USAjaxLoadValue.class );

	private String _id;
	private boolean _showLoadingIndicator = true;

	public USAjaxLoadValue( WOContext context ) {
		super( context );
	}

	/**
	 * @return the id to use for the component
	 */
	public String id() {
		if( _id == null ) {
			_id = (String)valueForBinding( "id" );
			if( _id == null ) {
				_id = ERXWOContext.safeIdentifierName( context(), true );
			}
		}
		return _id;
	}

	/**
	 * @return if a loading indicator should be displayed when the content is not yet available
	 */
	public boolean showLoadingIndicator() {
		if( valueForBinding( "showLoadingIndicator" ) != null ) {
			_showLoadingIndicator = (Boolean)valueForBinding( "showLoadingIndicator" );
		}
		return _showLoadingIndicator;
	}

	/**
	 * @return if the loading indicator is to be displayed, showLoadingIndicator() and the content is not yet available
	 */
	public boolean loadingIndicator() {
		return showLoadingIndicator() && (propertyToMonitor() == null);
	}

	/**
	 * @return the property to monitor
	 */
	public Object propertyToMonitor() {
		return valueForBinding( "propertyToMonitor" );
	}

	/**
	 * @return a cache key to use for the ping component
	 */
	public String cacheKey() {
		return System.nanoTime() + "";
	}

	/**
	 * @return true if the content is loaded and the component should stop monitoring the property
	 */
	public boolean stop() {
		return propertyToMonitor() != null;
	}
}