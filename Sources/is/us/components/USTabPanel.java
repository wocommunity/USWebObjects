package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXStringUtilities;

/**
 * @author Hugi Þórðarson
 */

public class USTabPanel extends ERXComponent {

	private static final String ACTIVE_TAB_CLASSNAME = "swactivetab";
	private static final String INACTIVE_TAB_CLASSNAME = "swinactivetab";

	public String currentTab;
	public Object selectedTab;
	public String submitActionName;

	public USTabPanel( WOContext context ) {
		super( context );
	}

	public NSArray<String> tabs() {
		return (NSArray<String>)valueForBinding( "tabs" );
	}

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

	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	public boolean hasMultipleTabs() {
		return tabs().count() > 1;
	}

	private String _uniqueID;

	public String uniqueID() {
		if( _uniqueID == null ) {
			_uniqueID = "TabPanel_" + ERXStringUtilities.replaceStringByStringInString( ".", "_", context().elementID() );
		}

		return _uniqueID;
	}
}
