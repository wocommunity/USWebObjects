package is.us.components;

import is.us.util.USUtilities;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.*;

import er.extensions.components.ERXNonSynchronizingComponent;

/**
 * Displays a status bar.
 * 
 * @author Hugi Þórðarson
 * 
 * FIXME: Use doubles rather than ints (for accuracy)
 * FIXME: Automate calculation of percentage value (value and maxValue)
 * FIXME: If 100%, there's a tiny space after the full bar.
 */

public class USPercentBar extends ERXNonSynchronizingComponent {

	public USPercentBar( WOContext context ) {
		super( context );
	}

	/**
	 * Maximum value 
	 */
	public double maxValue() {
		Object o = valueForBinding( "maxValue" );
		return USUtilities.doubleFromObject( o );
	}

	/**
	 * Actual value 
	 */
	public double value() {
		Object o = valueForBinding( "value" );
		return USUtilities.doubleFromObject( o );
	}

	/**
	 * We try to retrieve a percentage value. If not specified, we calculate it. 
	 */
	public int percent() {
		Object o = valueForBinding( "percent" );

		if( o != null )
			return USUtilities.integerFromObject( o );

		return (int)(value() / maxValue());
	}

	/**
	 * Total width of table. 
	 */
	public int width() {
		Object o = valueForBinding( "width" );
		return USUtilities.integerFromObject( o );
	}

	/**
	 * Total width of bar. 
	 */
	public int totalWidth() {
		return width();
	}

	/**
	 * Width of fill bar 
	 */
	public int barWidth() {
		return width() * percent() / 100;
	}

	/**
	 * Width of not filled space. 
	 */
	public int remainingWidth() {
		return totalWidth() - barWidth();
	}

	@Override
	protected NSArray<String> additionalCSSFiles() {
		NSMutableArray<String> a = new NSMutableArray<String>();
		a.addObject( "css/USPercentBar.css" );
		return a;
	}
}