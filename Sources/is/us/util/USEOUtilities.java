package is.us.util;

import is.us.wo.util.USC;

import java.util.Enumeration;

import org.slf4j.*;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

import er.extensions.qualifiers.ERXAndQualifier;

/**
 * EOF related utility classes.
 * 
 * @author Hugi Þórðarson
 */

public class USEOUtilities {

	private static final Logger logger = LoggerFactory.getLogger( USEOUtilities.class );

	/**
	 * No instances created, ever.
	 */
	private USEOUtilities() {}

	/**
	 * Accepts an attribute name and a list of space-separated values and constructs an EOOrQualifier.
	 * 
	 * @param attributeName name of the attribute to qualify against
	 * @param valueString space-separated string of values to match
	 * @return EOOrQualifier based on the attribute name and values to match
	 */
	public static EOQualifier qualifierMatchingAnyValue( String attributeName, String valueString ) {
		NSArray<String> values = NSArray.componentsSeparatedByString( valueString, USC.SPACE );

		Enumeration<String> e = values.objectEnumerator();

		NSMutableArray<EOQualifier> qualArray = new NSMutableArray<EOQualifier>();

		while( e.hasMoreElements() ) {
			String s = e.nextElement();
			qualArray.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, s ) );
		}

		return new EOOrQualifier( qualArray );
	}

	/**
	 * Fetches an object matching the key-value pair.
	 */
	public static EOEnterpriseObject objectMatchingKeyAndValue( EOEditingContext ec, String entityName, String attributeName, Object value ) {

		if( value == null ) {
			return null;
		}

		EOQualifier q = new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, null );
		fs.setFetchLimit( 1 );
		NSArray<EOEnterpriseObject> fetched = ec.objectsWithFetchSpecification( fs );

		if( USArrayUtilities.arrayHasObjects( fetched ) ) {
			return fetched.objectAtIndex( 0 );
		}

		return null;
	}

	/**
	 * Fetches an object matching the key-value pair.
	 * Yeah, yeah, I know EOUtilities offers the same functionality, but I'm a control freak.
	 */
	public static <T> T objectMatchingKeyAndValue( EOEditingContext ec, Class<T> clazz, String attributeName, Object value ) {

		if( value == null ) {
			return null;
		}

		EOQualifier q = new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( clazz.getSimpleName(), q, null );
		fs.setFetchLimit( 1 );
		NSArray<EOEnterpriseObject> fetched = ec.objectsWithFetchSpecification( fs );

		if( USArrayUtilities.arrayHasObjects( fetched ) ) {
			return (T)fetched.objectAtIndex( 0 );
		}

		return null;
	}

	/**
	 * Fetches all objects for the named entity into the editing context and sorts in ascending order by the sortKey attribute.
	 */
	public static NSArray allObjectsForEntitySortedByKey( EOEditingContext ec, String entityName, String sortKey ) {
		EOSortOrdering s = new EOSortOrdering( sortKey, EOSortOrdering.CompareAscending );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, null, new NSArray<EOSortOrdering>( s ) );
		return ec.objectsWithFetchSpecification( fs );
	}

	/**
	 * Fetches all objects for the named entity into the editing context and sorts in ascending order by the sortKey attribute.
	 */
	public static <E> NSArray<E> allObjectsForEntitySortedByKey( EOEditingContext ec, Class<E> entityClass, String sortKey ) {
		EOFetchSpecification fs = new EOFetchSpecification( entityClass.getSimpleName(), null, null );

		if( sortKey != null ) {
			EOSortOrdering s = new EOSortOrdering( sortKey, EOSortOrdering.CompareAscending );
			fs.setSortOrderings( new NSArray<EOSortOrdering>( s ) );
		}

		return ec.objectsWithFetchSpecification( fs );
	}

	/**
	 * Creates a qualifier that qualifies between two dates:
	 * - from and including the first one
	 * - up to and NOT including the second one.
	 *
	 * @param keypath The keypath to create the qualifier for.
	 * @param dateFrom First date. If null, assumes infinity in to the future.
	 * @param dateFrom Last date. If null, assumes infinity in to the past.
	 */
	public static EOQualifier qualifierBetweenDates( String keypath, NSTimestamp dateFrom, NSTimestamp dateTo ) {
		NSMutableArray<EOQualifier> qualArr = new NSMutableArray<EOQualifier>();

		if( dateFrom != null ) {
			qualArr.addObject( new EOKeyValueQualifier( keypath, EOQualifier.QualifierOperatorGreaterThanOrEqualTo, dateFrom ) );
		}

		if( dateTo != null ) {
			qualArr.addObject( new EOKeyValueQualifier( keypath, EOQualifier.QualifierOperatorLessThan, dateTo ) );
		}

		return new ERXAndQualifier( qualArr );
	}

	/**
	 * Cleans out every record in a database table. Returns the number of rows deleted.
	 */
	public static int deleteObjectsForEntityNamed( EOEditingContext ec, String entityName ) {

		NSArray<EOEnterpriseObject> a = EOUtilities.objectsForEntityNamed( ec, entityName );
		int count = a.count();

		for( EOEnterpriseObject eo : a ) {
			ec.deleteObject( eo );
		}

		return count;
	}

	/**
	 * Fetches the most recent record matching the given type and value.
	 */
	public static EOGenericRecord mostRecentRecord( EOEditingContext ec, String entityName, String keyPath, String value, String timestampAttributeName ) {
		NSArray<EOSortOrdering> sortOrderings = new NSArray<EOSortOrdering>( new EOSortOrdering( timestampAttributeName, EOSortOrdering.CompareDescending ) );
		EOQualifier q = new EOKeyValueQualifier( keyPath, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, sortOrderings );
		fs.setFetchLimit( 1 );
		NSArray a = ec.objectsWithFetchSpecification( fs );
		return USArrayUtilities.arrayHasObjects( a ) ? (EOGenericRecord)a.objectAtIndex( 0 ) : null;
	}

	/**
	 * Attempts to resolve which framework/bundle an EOModel belongs to. 
	 */
	public static String bundleNameForEOModel( EOModel model ) {

		if( model == null )
			return null;

		NSArray<EOEntity> entities = model.entities();

		if( !USArrayUtilities.arrayHasObjects( entities ) )
			return null;

		EOEntity entity = entities.lastObject();
		String className = entity.className();

		try {
			Class<?> clazz = Class.forName( className );

			if( "EOGenericRecord".equals( clazz.getSimpleName() ) ) {
				return null;
			}

			NSBundle bundle = NSBundle.bundleForClass( clazz );

			if( bundle != null ) {
				return bundle.name();
			}
		}
		catch( ClassNotFoundException e ) {
			logger.error( "No bundle found for class: " + className, e );
		}
		return null;
	}

	/**
	 * TODO: Missing comments
	 *  
	 * @param ec
	 * @param entityName
	 * @param rawRows
	 * @return
	 */
	public static NSArray<EOEnterpriseObject> convertRawRowsToEOs( EOEditingContext ec, String entityName, NSArray<NSDictionary> rawRows ) {
		NSMutableArray<EOQualifier> allQualifier = new NSMutableArray<EOQualifier>();

		for( NSDictionary pk : rawRows ) {
			EOQualifier q = EOQualifier.qualifierToMatchAllValues( pk );
			allQualifier.addObject( q );
		}

		EOQualifier q = new EOOrQualifier( allQualifier );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, null );
		NSArray<EOEnterpriseObject> eos = ec.objectsWithFetchSpecification( fs );
		return eos;
	}

	/**
	 * TODO: Missing comments
	 */
	public static <E> NSArray<E> fetchObjects( EOEditingContext editingContext, Class<E> entityClass, String key, NSArray<String> objectArray ) {

		if( !USArrayUtilities.arrayHasObjects( objectArray ) ) {
			return NSArray.emptyArray();
		}

		Enumeration<String> e = objectArray.objectEnumerator();
		NSMutableArray<E> resultArray = new NSMutableArray<E>();

		while( e.hasMoreElements() ) {
			E o = (E)objectMatchingKeyAndValue( editingContext, entityClass.getSimpleName(), key, e.nextElement() );

			if( o != null ) {
				resultArray.addObject( o );
			}
		}

		return resultArray;
	}

	/**
	 * Returns the letters of the alphabet that words in the given key for the given entity start with in uppercase.
	 */
	public static NSArray<String> firstLettersForKeyPathInEntity( EOEditingContext ec, String keyPath, String entityName ) {

		EOFetchSpecification fs = new EOFetchSpecification( entityName, null, null );
		fs.setFetchesRawRows( true );
		fs.setRawRowKeyPaths( new NSArray<String>( keyPath ) );

		NSArray<NSDictionary> fetched = ec.objectsWithFetchSpecification( fs );

		NSMutableSet<String> set = new NSMutableSet<String>();

		for( NSDictionary nextObject : fetched ) {
			Object value = nextObject.valueForKeyPath( keyPath );

			if( value instanceof String && ((String)value).length() > 0 ) {
				String firstLetter = ((String)value).substring( 0, 1 ).toUpperCase();
				set.addObject( firstLetter );
			}
		}

		return USArrayUtilities.sortedArrayUsingIcelandicComparator( set.allObjects() );
	}
}