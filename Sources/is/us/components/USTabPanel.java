package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXNonSynchronizingComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * A simple tab panel.
 * 
 * @author Hugi Þórðarson
 */

public class USTabPanel extends ERXNonSynchronizingComponent {

	/**
	 * CSS class for active tab button.
	 */
	private static final String ACTIVE_TAB_CLASSNAME = "swactivetab";

	/**
	 * CSS class for inactive tab button.
	 */
	private static final String INACTIVE_TAB_CLASSNAME = "swinactivetab";

	/**
	 * Tab currently being iterated over.
	 */
	public String currentTab;

	/**
	 * Currently selected tab.
	 */
	public String selectedTab;

	/**
	 * Unique identifier for this component 
	 */
	private String _uniqueID;

	public USTabPanel( WOContext context ) {
		super( context );
	}

	/**
	 * @return Tab names
	 */
	public NSArray<String> tabs() {
		return (NSArray<String>)valueForBinding( "tabs" );
	}

	/**
	 * @return
	 */
	public String submitActionName() {
		return (String)valueForBinding( "submitActionName" );
	}

	public WOActionResults selectTab() {

		setSelectedTab( currentTab );

		if( submitActionName() != null )
			return performParentAction( submitActionName() );

		return null;
	}

	public String currentStyleClass() {
		return currentTab.equals( selectedTab() ) ? ACTIVE_TAB_CLASSNAME : INACTIVE_TAB_CLASSNAME;
	}

	public void setSelectedTab( String newSelectedTab ) {
		selectedTab = newSelectedTab;
		setValueForBinding( selectedTab, "selectedTab" );
	}

	public Object selectedTab() {

		setSelectedTab( (String)valueForBinding( "selectedTab" ) );

		if( selectedTab == null ) {
			setSelectedTab( tabs().objectAtIndex( 0 ) );
		}

		return selectedTab;
	}

	/**
	 * @return True if we have more than 1 tab.
	 */
	public boolean hasMultipleTabs() {
		return tabs().count() > 1;
	}

	/**
	 * Unique identifier for this component 
	 */
	public String uniqueID() {
		if( _uniqueID == null ) {
			_uniqueID = "TabPanel_" + ERXStringUtilities.replaceStringByStringInString( ".", "_", context().elementID() );
		}

		return _uniqueID;
	}
}