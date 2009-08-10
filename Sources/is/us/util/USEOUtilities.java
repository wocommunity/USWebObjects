package is.us.util;

import is.us.wo.util.USC;

import java.util.Enumeration;

import org.slf4j.*;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import com.webobjects.foundation.NSComparator.ComparisonException;

import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.qualifiers.ERXAndQualifier;

/**
 * EOF related utility classes.
 * 
 * @author Hugi Þórðarson
 */

public class USEOUtilities {

	private static final Logger logger = LoggerFactory.getLogger( USEOUtilities.class );

	private static final String STRING_CLASS_NAME = "java.lang.String";
	private static final String INTEGER_CLASS_NAME = "java.lang.Number";
	private static final String TIMESTAMP_CLASS_NAME = "com.webobjects.foundation.NSTimestamp";

	/**
	 * No instances created, ever.
	 */
	private USEOUtilities() {}

	/**
	 * Accepts an attribute name and a list of space-separated values and constructs an EOOrQualifier.
	 * 
	 * @param attributeName name of the attribute to qualify against
	 * @param valueString space-separated string og values to match
	 * @return EOOrQualifier based on the attribute name and values to match
	 * 
	 * @author Hugi Þórðarson
	 */
	public static EOQualifier qualifierMatchingAnyValue( String attributeName, String valueString ) {
		NSArray values = NSArray.componentsSeparatedByString( valueString, USC.SPACE );

		Enumeration e = values.objectEnumerator();

		NSMutableArray qualArray = new NSMutableArray();

		while( e.hasMoreElements() ) {
			String s = (String)e.nextElement();
			qualArray.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, s ) );
		}

		return new EOOrQualifier( qualArray );
	}

	/**
	 * Fetches an object matching the key-value pair.
	 * Yeah, yeah, I know EOUtilities offers the same functionality, but I'm a control freak.
	 * 
	 * FIXME: Genericize.
	 */
	public static EOEnterpriseObject objectMatchingKeyAndValue( EOEditingContext ec, String entityName, String attributeName, Object value ) {

		if( value == null )
			return null;

		EOQualifier q = new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, null );
		fs.setFetchLimit( 1 );
		NSArray<EOEnterpriseObject> fetched = ec.objectsWithFetchSpecification( fs );

		if( USArrayUtilities.arrayHasObjects( fetched ) )
			return fetched.objectAtIndex( 0 );

		return null;
	}

	/**
	 * Fetches an object matching the key-value pair.
	 * Yeah, yeah, I know EOUtilities offers the same functionality, but I'm a control freak.
	 * 
	 * FIXME: Genericize.
	 */
	public static <T> T objectMatchingKeyAndValue( EOEditingContext ec, Class<T> clazz, String attributeName, Object value ) {

		if( value == null )
			return null;

		EOQualifier q = new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( clazz.getSimpleName(), q, null );
		fs.setFetchLimit( 1 );
		NSArray<EOEnterpriseObject> fetched = ec.objectsWithFetchSpecification( fs );

		if( USArrayUtilities.arrayHasObjects( fetched ) )
			return (T)fetched.objectAtIndex( 0 );

		return null;
	}

	/**
	 * Fetches all objects for the named entity into the editing context and sorts in ascending order by the sortKey attribute.
	 * 
	 * @deprecated use allObjectsForEntityClassSortedByKey instead
	 */
	public static NSArray allObjectsForEntitySortedByKey( EOEditingContext ec, String entityName, String sortKey ) {
		EOSortOrdering s = new EOSortOrdering( sortKey, EOSortOrdering.CompareAscending );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, null, new NSArray<EOSortOrdering>( s ) );
		return ec.objectsWithFetchSpecification( fs );
	}

	/**
	 * Fetches all objects for the named entity into the editing context and sorts in ascending order by the sortKey attribute.
	 */
	public static <E> NSArray<E> allObjectsForEntityClassSortedByKey( EOEditingContext ec, Class<E> entityClass, String sortKey ) {
		EOFetchSpecification fs = new EOFetchSpecification( entityClass.getSimpleName(), null, null );

		if( sortKey != null ) {
			EOSortOrdering s = new EOSortOrdering( sortKey, EOSortOrdering.CompareAscending );
			fs.setSortOrderings( new NSArray<EOSortOrdering>( s ) );
		}

		return ec.objectsWithFetchSpecification( fs );
	}

	/**
	 * Creates a qualifier that qualifies between two dates (from and including) the first one
	 * and to and NOT including the second one.
	 *
	 * @param keypath The keypath to create the qualifier for.
	 * @param dateFrom First date. If null, assumes infinity in to the future.
	 * @param dateFrom Last date. If null, assumes infinity in to the past.
	 *
	 * @author Hugi Þórðarson
	 */
	public static EOQualifier qualifierBetweenDates( String keypath, NSTimestamp dateFrom, NSTimestamp dateTo ) {
		NSMutableArray<EOQualifier> qualArr = new NSMutableArray<EOQualifier>();

		if( dateFrom != null )
			qualArr.addObject( new EOKeyValueQualifier( keypath, EOQualifier.QualifierOperatorGreaterThanOrEqualTo, dateFrom ) );

		if( dateTo != null )
			qualArr.addObject( new EOKeyValueQualifier( keypath, EOQualifier.QualifierOperatorLessThan, dateTo ) );

		return new ERXAndQualifier( qualArr );
	}

	/**
	 * Cleans out every record in a database table.
	 * 
	 * Returns the number of rows deleted.
	 */
	public static int deleteAllRecordsForEntityNamed( EOEditingContext ec, String entityName ) {

		NSArray<EOEnterpriseObject> a = EOUtilities.objectsForEntityNamed( ec, entityName );
		int count = a.count();

		for( EOEnterpriseObject eo : a ) {
			ec.deleteObject( eo );
		}

		ec.saveChanges();

		return count;
		/*
		 * 
		 * Action is performed through the database channel, so you don't need to save after using this.
		EOEntity myEntity = EOUtilities.entityNamed( ec, entityName );

		EOAttribute attribute = myEntity.attributes().objectAtIndex( 0 );

		EOQualifier qualifier = qualifierForAttribute( attribute );

		EODatabaseContext databaseContext = EODatabaseContext.registeredDatabaseContextForModel( myEntity.model(), ec );

		EOAdaptorChannel adaptorChannel = databaseContext.availableChannel().adaptorChannel();

		// delete row 
		if( !adaptorChannel.isOpen() ) {
			adaptorChannel.openChannel();
		}

		int deletedRowsCount = adaptorChannel.deleteRowsDescribedByQualifier( qualifier, myEntity );

		if( adaptorChannel.isOpen() ) {
			adaptorChannel.closeChannel();
		}

		return deletedRowsCount;
		*/
	}

	/**
	 * TODO: Comment.
	 */
	private static EOQualifier qualifierForAttribute( EOAttribute attribute ) {

		String attributeName = attribute.name();
		String valueClass = attribute.className();

		if( valueClass.equals( STRING_CLASS_NAME ) ) { // String might have value(*) or nothing(null)
			NSMutableArray a = new NSMutableArray();
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorLike, USC.STAR ) );
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorEqual, null ) );
			return new EOOrQualifier( a );
		}

		if( valueClass.equals( INTEGER_CLASS_NAME ) ) {
			NSMutableArray a = new NSMutableArray();
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorGreaterThan, new Integer( 99 ) ) );
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorLessThan, new Integer( 100 ) ) );
			return new EOAndQualifier( a );
		}

		if( valueClass.equals( TIMESTAMP_CLASS_NAME ) ) {
			NSMutableArray a = new NSMutableArray();
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorGreaterThan, new NSTimestamp() ) );
			a.addObject( new EOKeyValueQualifier( attributeName, EOQualifier.QualifierOperatorLessThan, new NSTimestamp() ) );
			return new EOAndQualifier( a );
		}

		return null;
	}

	/**
	 * Fetches the most recent record matching the given type and value.
	 * 
	 * @author Hugi Þórðarson
	 */
	public static EOGenericRecord mostRecentRecord( EOEditingContext ec, String entityName, String keyPath, String value, String timestampAttributeName ) {
		NSArray sortOrderings = new NSArray( new EOSortOrdering( timestampAttributeName, EOSortOrdering.CompareDescending ) );
		EOQualifier q = new EOKeyValueQualifier( keyPath, EOQualifier.QualifierOperatorEqual, value );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, sortOrderings );
		fs.setFetchLimit( 1 );
		NSArray a = ec.objectsWithFetchSpecification( fs );
		return USArrayUtilities.arrayHasObjects( a ) ? (EOGenericRecord)a.objectAtIndex( 0 ) : null;
	}

	/**
	 * @deprecated use ERXEOControlUtilities.objectCountWithQualifier
	 */
	public static Number objectCountWithFetchSpecification( EOEditingContext ec, EOFetchSpecification fs ) {
		return ERXEOControlUtilities.objectCountWithQualifier( ec, fs.entityName(), fs.qualifier() );
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
		NSMutableArray allQualifier = new NSMutableArray();

		for( NSDictionary pkDictionary : rawRows ) {
			EOQualifier q = EOQualifier.qualifierToMatchAllValues( pkDictionary );
			allQualifier.addObject( q );
		}

		EOQualifier q = new EOOrQualifier( allQualifier );
		EOFetchSpecification fs = new EOFetchSpecification( entityName, q, null );
		NSArray eos = ec.objectsWithFetchSpecification( fs );
		return eos;
	}

	/**
	 * TODO: Missing comments
	 */
	public static <E> NSArray<E> fetchObjects( EOEditingContext editingContext, Class<E> entityClass, String key, NSArray<String> objectArray ) {

		if( !USArrayUtilities.arrayHasObjects( objectArray ) )
			return NSArray.EmptyArray;

		Enumeration<String> e = objectArray.objectEnumerator();
		NSMutableArray<E> resultArray = new NSMutableArray<E>();

		while( e.hasMoreElements() ) {
			E o = (E)objectMatchingKeyAndValue( editingContext, entityClass.getSimpleName(), key, e.nextElement() );

			if( o != null )
				resultArray.addObject( o );
		}

		return resultArray;
	}

	/**
	 * Returns the letters of the alphabet that words in the given key for the given entity start with in uppercase.
	 * 
	 * TODO: Use raw rows.
	 */
	public static NSArray<String> firstLettersForKeyPathInEntity( EOEditingContext ec, String keyPath, String entityName ) {

		EOFetchSpecification fs = new EOFetchSpecification( entityName, null, null );

		NSArray<EOEnterpriseObject> a = ec.objectsWithFetchSpecification( fs );

		NSMutableSet<String> set = new NSMutableSet<String>();

		for( EOEnterpriseObject nextObject : a ) {
			Object value = nextObject.valueForKeyPath( keyPath );

			if( value instanceof String && ((String)value).length() > 0 ) {
				String firstLetter = ((String)value).substring( 0, 1 ).toUpperCase();
				set.addObject( firstLetter );
			}
		}

		try {
			return set.allObjects().sortedArrayUsingComparator( USGenericComparator.IcelandicAscendingComparator );
		}
		catch( ComparisonException e ) {
			logger.error( "Could not sort set", e );
			return set.allObjects();
		}
	}
}