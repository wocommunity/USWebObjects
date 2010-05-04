package is.us.components;

import is.us.util.*;
import is.us.wo.util.USC;

import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Enumeration;

import org.slf4j.*;

import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.*;
import com.webobjects.foundation.*;
import com.webobjects.foundation.NSComparator.ComparisonException;

import er.extensions.components.ERXComponent;

/**
 * Displays some runtime information about the application.
 * 
 * * All EOModels in your application, along with their connection dictionaries and associated entities.
 * * All properties in the application's environment.
 * * Executes "ps -ax" on the server to get a list of all currently running processes.
 * * Classpath
 * 
 * @author Hugi Þórðarson
 */

public class USApplicationInfo extends ERXComponent {

	private static final Logger logger = LoggerFactory.getLogger( USApplicationInfo.class );
	public EOModel currentModel;
	public EOEntity currentEntity;
	public String currentPropertyKey;
	private final NSDictionary _properties = new NSDictionary( java.lang.System.getProperties() );

	private static final String PATH_SEPARATOR = System.getProperty( "path.separator" );
	private static final int psLength = PATH_SEPARATOR.length();

	public USApplicationInfo( WOContext context ) {
		super( context );
	}

	@Override
	protected boolean useDefaultComponentCSS() {
		return true;
	}

	/**
	 * All the EOModels in your application.
	 */
	public NSArray<EOModel> allModels() {
		return EOModelGroup.defaultGroup().models();
	}

	/**
	 * @return The bundle name for the current EOModel.
	 */
	public String currentBundleName() {
		return USEOUtilities.bundleNameForEOModel( currentModel );
	}

	/**
	 * @return All property keys in the application.
	 */
	public NSArray<String> propertyKeys() throws ComparisonException {
		NSArray<String> a = _properties.allKeys();
		return a.sortedArrayUsingComparator( USGenericComparator.IcelandicAscendingComparator );
	}

	/**
	 * @return Value of the property currently being iterated over.
	 */
	public Object currentPropertyValue() {
		return _properties.objectForKey( currentPropertyKey );
	}

	/**
	 * @return The result of invoking "ps" on the server, showing the currently running processes.
	 */
	public String psString() {
		try {
			Process p = Runtime.getRuntime().exec( "ps -ax" );
			p.getOutputStream();

			InputStream in = p.getInputStream();
			String result = USStringUtilities.readStringFromInputStreamUsingEncoding( in, USC.UTF_8 );
			return result;
		}
		catch( Exception e ) {
			logger.error( "Could not invoke ps", e );
			return null;
		}
	}

	public static NSArray urlClassPath() {
		NSMutableArray result = new NSMutableArray();
		URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
		NSArray urls = new NSArray( classLoader.getURLs() );
		Enumeration en = urls.objectEnumerator();
		while( en.hasMoreElements() ) {
			result.addObject( en.nextElement() );
		}
		return new NSArray( result );
	}

	public static String reportPath( NSArray arr, String name ) {
		StringBuilder b = new StringBuilder();
		b.append( "<strong>" + name + "</strong>" );
		b.append( "\n" );
		Enumeration en = arr.objectEnumerator();

		while( en.hasMoreElements() ) {
			b.append( en.nextElement() );
			b.append( "\n" );
		}

		return b.toString();
	}

	public static String reportPath( String systemProperty ) {
		NSArray arr = ppp( systemProperty );
		return reportPath( arr, systemProperty );
	}

	public String reportPath() {
		StringBuilder b = new StringBuilder();
		b.append( "\n" );
		b.append( reportPath( "java.home" ) );
		b.append( "\n" );
		b.append( "\n" );
		b.append( reportPath( "sun.boot.class.path" ) );
		b.append( "\n" );
		b.append( reportPath( "java.ext.dirs" ) );
		b.append( "\n" );
		b.append( reportPath( "sun.boot.library.path" ) );
		b.append( "\n" );
		b.append( reportPath( "user.dir" ) );
		b.append( "\n" );
		b.append( reportPath( "java.library.path" ) );
		b.append( "\n" );
		b.append( reportPath( "java.class.path" ) );
		b.append( "\n" );

		return USStringUtilities.convertBreakString( b.toString() );
	}

	private static NSArray pp( String path ) {

		if( null == path ) {
			return NSArray.EmptyArray;
		}

		NSMutableArray result = new NSMutableArray();
		int oldloc = 0;
		int loc = 0;
		String found = null;
		int len = path.length();
		while( oldloc < len ) {
			loc = path.indexOf( PATH_SEPARATOR, oldloc );
			if( -1 == loc ) {
				loc = len;
			}
			found = path.substring( oldloc, loc );
			result.addObject( found );
			oldloc = loc + psLength;
		}
		return new NSArray( result );
	}

	private static NSArray ppp( String systemProperty ) {
		return pp( System.getProperty( systemProperty ) );
	}
}