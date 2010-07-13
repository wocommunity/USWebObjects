package is.us.util;

import is.us.wo.util.USC;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.*;
import org.w3c.dom.*;

import com.webobjects.appserver.WOApplication;
import com.webobjects.foundation.*;

/**
 * Various utility methods.
 *  
 * @author Hugi Þórðarson
 */

public class USUtilities {

	private static final Logger logger = LoggerFactory.getLogger( USUtilities.class );

	/**
	 * A shared instance of random shared by many applications. 
	 */
	public static Random RANDOM = new Random();

	/**
	 * No instances created, ever.
	 */
	private USUtilities() {}

	/**
	 * Returns true if the given number equals 1
	 */
	public static boolean numberIsTrue( Number number ) {
		return number != null && number.intValue() == 1;
	}

	/**
	 * Finds out the value of Object and attempts to coerce it's value to a boolean.
	 * 
	 * Returns true if:
	 * - it's a boolean with the value of true
	 * - it's string with value of "true" (case insensitive)
	 * - If it's an integer larger than zero.
	 */
	public static boolean booleanFromObject( Object o ) {

		if( isNull( o ) ) {
			return false;
		}

		if( o instanceof Boolean ) {
			return ((Boolean)o).booleanValue();
		}

		if( o instanceof String ) {
			if( ((String)o).toLowerCase().equals( USC.TRUE_STRING ) ) {
				return true;
			}

			if( ((String)o).toLowerCase().equals( USC.FALSE_STRING ) ) {
				return false;
			}
		}

		Integer i = integerFromObject( o );

		if( i == null ) {
			return false;
		}

		if( i.intValue() > 0 ) {
			return true;
		}

		return false;
	}

	/**
	 * Finds out the value of Object and attempts to coerce it's value to an Integer
	 */
	public static Integer integerFromObject( Object o ) {

		if( isNull( o ) ) {
			return null;
		}

		try {
			if( o instanceof Number ) {
				return ((Number)o).intValue();
			}

			if( o instanceof String ) {
				return new Integer( ((String)o) );
			}
		}
		catch( Exception e ) {
			logger.warn( "Could not coerce the value to an integer: " + o, e );
		}

		return null;
	}

	/**
	 * Finds out the value of Object and attempts to coerce it's value to an Integer
	 */
	public static Double doubleFromObject( Object o ) {

		if( isNull( o ) ) {
			return null;
		}

		try {
			if( o instanceof Number ) {
				return ((Number)o).doubleValue();
			}
		}
		catch( Exception e ) {
			logger.warn( "Could not convert to double from object: " + o, e );
		}

		return null;
	}

	/**
	 * Attempts to coerce an object value to a BigDecimal. returns null if unsuccessful.
	 */
	public static BigDecimal bigDecimalFromObject( Object o ) {

		if( isNull( o ) ) {
			return null;
		}

		return new BigDecimal( Float.valueOf( o.toString() ) );
	}

	/**
	 * @param object The object to check
	 * @return true if an object is null, or NSKeyValueCoding.NullValue.
	 */
	private static final boolean isNull( Object object ) {
		return (object == null) || (object instanceof NSKeyValueCoding.Null);
	}

	/**
	 * Finds out the value of Object and attempts to coerce it's value to a String
	 */
	public static String stringFromObject( Object o ) {

		if( isNull( o ) ) {
			return null;
		}

		return o.toString();
	}

	/**
	 * Prints the stacktrace for a Trowable to a string. 
	 */
	public static String stackTraceAsString( Throwable t ) {
		StringWriter sw = new StringWriter();
		t.printStackTrace( new PrintWriter( sw ) );
		return sw.toString();
	}

	/**
	 * Creates a pretty stack trace for HTML display. 
	 */
	public static String stackTraceAsHTMLString( Throwable t ) {

		if( t == null ) {
			return null;
		}

		StackTraceElement[] stack = t.getStackTrace();
		StringBuilder sb = new StringBuilder();

		for( int i = 0; i < stack.length; i++ ) {
			sb.append( stack[i].toString() + "<br>\n" );
		}

		sb.append( "<br>" );
		sb.append( "<h3>Caused by: " + t.getCause() + "</h3>" );

		for( int i = 0; i < stack.length; i++ ) {
			sb.append( stack[i].toString() + "<br>\n" );
		}

		return sb.toString();
	}

	/**
	 * Transforms a W3 Document to it's (xml) String representation.
	 */
	public static String convertDOMDocumentToString( Document doc ) throws TransformerException {
		DOMSource domSource = new DOMSource( doc );
		return convertDOMDocumentToString( domSource );
	}

	/**
	 * Transforms a W3 Node to it's (xml) String representation.
	 */
	public static String convertDOMDocumentToString( Node node ) throws TransformerException {
		DOMSource domSource = new DOMSource( node );
		return convertDOMDocumentToString( domSource );
	}

	/**
	 * Transforms a W3 DOMSource to it's (xml) String representation. 
	 */
	public static String convertDOMDocumentToString( DOMSource domSource ) throws TransformerException {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult( writer );
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform( domSource, result );
		return writer.toString();
	}

	/**
	 * 
	 */
	public static NSArray<String> stringToArrayOfTrimmedStrings( String string ) {
		NSArray<String> a = NSArray.componentsSeparatedByString( string, "\n" );

		if( USArrayUtilities.arrayHasObjects( a ) ) {
			NSMutableArray<String> a2 = new NSMutableArray<String>();

			Enumeration<String> e = a.objectEnumerator();

			while( e.hasMoreElements() ) {
				String s = e.nextElement();

				if( USStringUtilities.stringHasValue( s ) ) {
					a2.addObject( s.trim() );
				}
			}

			return a2;
		}

		return NSArray.emptyArray();
	}

	/**
	 * Fetches the path on the file system for a named resource. 
	 */
	public static String fileSystemPathforResourceNamed( String aResourceName, String aFrameworkName, NSArray<String> aLanguagesList ) {
		URL url = WOApplication.application().resourceManager().pathURLForResourceNamed( aResourceName, aFrameworkName, aLanguagesList );
		String urlString = url.toExternalForm();
		return urlString.substring( 5, urlString.length() );
	}

	/**
	 * Returns a new dictionary from the serialized property list,
	 * or an empty dictionary if the string is null.
	 */
	public static NSMutableDictionary dictionaryFromString( String s ) {

		if( USStringUtilities.stringHasValue( s ) ) {
			return ((NSDictionary)NSPropertyListSerialization.propertyListFromString( s )).mutableClone();
		}

		return new NSMutableDictionary();
	}

	/**
	 * Fetches a key for the given value in the dictionary. 
	 */
	public static String keyForValueFromStringDictionary( String value, NSDictionary<String, String> dictionary ) {
		NSArray<String> a = dictionary.allKeysForObject( value );

		if( USArrayUtilities.arrayHasObjects( a ) ) {
			return a.objectAtIndex( 0 );
		}

		return null;
	}

	/**
	 * Checks for equality of objects in a null safe manner.
	 */
	public static boolean eq( Object o1, Object o2 ) {
		if( o1 == null && o2 == null ) {
			return true;
		}

		if( o1 != null ) {
			return o1.equals( o2 );
		}

		return false;
	}

	/**
	 * Puts a value into a <code>Map</code> if none of the parameters are null
	 */
	public static void putIfNotNull( Map<String, Object> map, String key, Object value ) {
		if( value != null && key != null && map != null ) {
			map.put( key, value );
		}
	}

	/**
	 * Adds a value into a <code>List</code> if none of the parameters are null
	 */
	public static void addIfNotNull( List<Object> array, Object value ) {
		if( array != null && value != null ) {
			array.add( value );
		}
	}

	/**
	 * Generates a string representation of all accessible fields in an object.
	 */
	public static String fieldsToString( Object o ) {

		Class<?> clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();

		StringBuilder b = new StringBuilder();

		for( Field f : fields ) {
			b.append( f.getName() );
			b.append( ": " );

			try {
				if( f.isAccessible() ) {
					b.append( f.get( o ) );
				}
			}
			catch( IllegalArgumentException e ) {
				e.printStackTrace();
			}
			catch( IllegalAccessException e ) {
				e.printStackTrace();
			}

			b.append( " - " );
		}

		return b.toString();
	}

	/**
	 * Reads data from the named resource and converts it to a string using UTF-8.
	 * 
	 * If no framework name is specified, reads from the "app" bundle by default.
	 */
	public static String stringFromResource( String resourceName, String frameworkName ) {
		return stringFromResource( resourceName, frameworkName, null );
	}

	/**
	 * Reads data from the named resource and converts it to a string using UTF-8.
	 * 
	 * If no framework name is specified, reads from the "app" bundle by default.
	 */
	public static String stringFromResource( String resourceName, String frameworkName, String language ) {

		if( frameworkName == null ) {
			frameworkName = "app";
		}

		if( language == null ) {
			language = "Icelandic";
		}

		byte[] templateData = WOApplication.application().resourceManager().bytesForResourceNamed( resourceName, frameworkName, new NSArray<String>( language ) );
		String template = null;

		try {
			template = new String( templateData, USC.UTF_8 );
		}
		catch( UnsupportedEncodingException e ) {
			logger.debug( "Could not read string form resource", e );
		}

		return template;
	}

	/**
	 * Set the values for all keys in the target object.
	 */
	public static void takeValuesForKeyPaths( NSKeyValueCoding targetObject, NSDictionary<String, Object> parameters ) {

		for( String key : parameters.allKeys() ) {
			targetObject.takeValueForKey( parameters.objectForKey( key ), key );
		}
	}
}