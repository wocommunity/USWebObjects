package is.us.util;

import java.sql.*;

import org.slf4j.*;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

import er.extensions.eof.*;

/**
 * This class is for performang operations that need to be done on a lot of rows.
 * 
 * @author Hugi Þórðarson
 */

public class USMassiveOperationJDBC {

	private static final Logger logger = LoggerFactory.getLogger( USMassiveOperationJDBC.class );

	/**
	 * Applies an operation to EOs.
	 * 
	 * @param entityClass The class of the EO Entity to apply your action to.
	 * @param batchSize The number of objects to work with at a time. Defaults to 200 if no batch size is specified.
	 * @param handler Handler that is applied to each object.
	 * @param qualifier Qualifier , if you only want to apply this to a subset of the objects.
	 * 
	 * FIXME: Might need to save changes on the nested editing context's changes to the parent editing context on each batch.
	 */
	public static void start( EOEditingContext ec, Class entityClass, EOQualifier qualifier, Integer batchSize, Operation handler ) throws Exception {

		KMStopWatch taskTimer = new KMStopWatch();

		if( batchSize == null )
			batchSize = 200;

		String entityName = entityClass.getSimpleName();
		EOEntity entity = EOModelGroup.defaultGroup().entityNamed( entityName );

		NSDictionary connectionDictionary = entity.model().connectionDictionary();

		Connection source = connectionWithDictionary( connectionDictionary );
		Statement stmt = source.createStatement();

		NSArray<EOAttribute> primaryKeyAttributes = entity.primaryKeyAttributes();
		NSArray<String> primaryKeyExternalNames = (NSArray<String>)(primaryKeyAttributes).valueForKeyPath( "columnName" );

		String primaryKeyListForSQL = primaryKeyExternalNames.componentsJoinedByString( "," );

		StringBuilder sql = new StringBuilder();
		sql.append( "select " );
		sql.append( primaryKeyListForSQL );
		sql.append( " from " );
		sql.append( entity.externalName() );

		logger.debug( "SQL used for initial fetch run: " + sql );

		EOEditingContext nestedEC = leanEditingContext( ec );
		Integer totalNumberOfRows = ERXEOControlUtilities.objectCountWithQualifier( nestedEC, entityName, qualifier );
		int batchCount = totalNumberOfRows / batchSize + (totalNumberOfRows % batchSize == 0 ? 0 : 1);

		logger.debug( stringWithFormat( "Starting processing for entity {}. Will fetch {} primary key(s) and process {} batch(es) of {} object(s) each", entityName, totalNumberOfRows, batchCount, batchSize ) );

		ResultSet rows = stmt.executeQuery( sql.toString() );

		int batchNumber = 0;
		int rowNumber = 0;

		NSMutableArray batch = new NSMutableArray();

		while( rows.next() ) {
			rowNumber++;

			NSMutableDictionary pk = new NSMutableDictionary();

			for( EOAttribute attribute : primaryKeyAttributes ) {
				String pkValue = rows.getString( attribute.columnName() );
				pk.setObjectForKey( pkValue, attribute.name() );
			}

			batch.addObject( pk );

			if( rows.getRow() % batchSize == 0 ) {
				KMStopWatch batchTimer = new KMStopWatch();
				batchNumber++;

				NSArray<EOEnterpriseObject> eoBatch = convertRawRowsToEOs( nestedEC, entityName, batch );

				for( EOEnterpriseObject eo : eoBatch ) {
					handler.handleObject( eo );
				}

				batch = new NSMutableArray();
				nestedEC = leanEditingContext( ec );

				long elapsedTaskTime = taskTimer.elapsed();
				long avgBatchTime = elapsedTaskTime / batchNumber;
				long currentBatchTime = batchTimer.elapsed();
				long estRemainingTime = avgBatchTime * (batchCount - batchNumber);
				Object[] info = new Object[] { batchNumber, currentBatchTime, avgBatchTime, elapsedTaskTime, estRemainingTime };
				logger.debug( stringWithFormat( "Processed batch {} in {} ms (avg {} ms). Elapsed time {} ms, est. remaining time {} ms.", info ) );
			}
		}
		logger.debug( "finished, total time was: " + taskTimer.elapsed() );
	}

	/**
	 * Creates an editing context with as little overhead as possible. 
	 */
	private static EOEditingContext leanEditingContext( EOEditingContext parent ) {
		EOEditingContext nestedEC = ERXEC.newEditingContext( parent );
		nestedEC.setUndoManager( null );
		return nestedEC;
	}

	/**
	 * The interface that must be implemented by object handlers. 
	 */
	public static interface Operation {

		public void handleObject( Object object );
	}

	/**
	 * Replaces variable markers in originalString with the objects in the object 
	 */
	private static String stringWithFormat( String originalString, Object... objects ) {
		String VARIABLE = "\\{\\}";
		String returnString = originalString;

		for( int i = 0; i < objects.length; i++ ) {
			returnString = returnString.replaceFirst( VARIABLE, objects[i].toString() );
		}

		return returnString;
	}

	/**
	 * Converts an array of EOF raw rows to EOs.
	 * 
	 * @param ec The editingcontext to use
	 * @param entityName name of the entity to fetch an EO from
	 * @param rawRows an array of raw rows to convert to EOs.
	 */
	private static NSArray<EOEnterpriseObject> convertRawRowsToEOs( EOEditingContext ec, String entityName, NSArray<NSDictionary> rawRows ) {
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
	 * Convenience class to measure the time an operation takes. 
	 */
	public static class KMStopWatch {

		private long startTime;
		private long endTime;

		public KMStopWatch() {
			start();
		}

		public void start() {
			startTime = System.currentTimeMillis();
		}

		public void end() {
			endTime = System.currentTimeMillis();
		}

		public long elapsed() {
			return System.currentTimeMillis() - startTime;
		}
	}

	private static Connection connectionWithDictionary( NSDictionary dict ) throws SQLException {
		System.out.println( dict );
		String username = (String)dict.objectForKey( "username" );
		String password = (String)dict.objectForKey( "password" );
		String driver = (String)dict.objectForKey( "driver" );
		String url = (String)dict.objectForKey( "URL" );
		if( url == null ) {
			url = (String)dict.objectForKey( "url" );
		}
		Boolean autoCommit;
		Object autoCommitObj = dict.objectForKey( "autoCommit" );
		if( autoCommitObj instanceof String ) {
			autoCommit = Boolean.valueOf( (String)autoCommitObj );
		}
		else {
			autoCommit = (Boolean)autoCommitObj;
		}
		boolean ac = autoCommit == null ? true : autoCommit.booleanValue();
		// Boolean readOnly = (Boolean) dict.objectForKey("readOnly");
		// boolean ro = readOnly == null ? false : readOnly.booleanValue();

		try {
			// FIXME (com.frontbase.jdbc.FBJDriver)
			Class.forName( "oracle.jdbc.OracleDriver" );
		}
		catch( ClassNotFoundException e ) {
			throw new SQLException( "Could not find driver: " + driver );
		}
		Connection con = DriverManager.getConnection( url, username, password );
		DatabaseMetaData dbmd = con.getMetaData();
		logger.info( "Connection to " + dbmd.getDatabaseProductName() + " " + dbmd.getDatabaseProductVersion() + " successful." );
		con.setAutoCommit( ac );
		return con;
	}
}