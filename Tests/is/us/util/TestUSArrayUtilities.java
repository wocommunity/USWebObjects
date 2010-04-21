package is.us.util;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

public class TestUSArrayUtilities {

	@Test
	public void searchUnsorted() {
		String[] testArr = new String[2];
		testArr[0] = "index 0";
		testArr[1] = "index 1";
		assertEquals( 1, USArrayUtilities.searchUnsorted( testArr, "index 1" ) );
		assertEquals( -1, USArrayUtilities.searchUnsorted( testArr, "index 2" ) );
		assertEquals( -1, USArrayUtilities.searchUnsorted( null, "index 2" ) );
		assertEquals( -1, USArrayUtilities.searchUnsorted( testArr, null ) );
		assertEquals( -1, USArrayUtilities.searchUnsorted( null, null ) );
	}

	@Test
	public void resize() {
		String[] oldArr = new String[2];
		oldArr[0] = "index 0";
		oldArr[1] = "index 1";
		String[] newArr = USArrayUtilities.resize( oldArr, 4 );

		assertEquals( 4, newArr.length );
		assertEquals( oldArr[0], "index 0" );
		assertEquals( oldArr[1], "index 1" );

		newArr = USArrayUtilities.resize( oldArr, 0 );
		assertEquals( newArr.length, 0 );

		newArr = USArrayUtilities.resize( oldArr, -1 );
		assertEquals( newArr.length, 0 );
	}

	@Test
	public void humanReadableList() {
		String expected;
		String actual;

		expected = "Hugi";
		actual = USArrayUtilities.humanReadableList( list( "Hugi" ) );
		assertEquals( expected, actual );

		expected = "Kjartan og Strumparnir";
		actual = USArrayUtilities.humanReadableList( list( "Kjartan", "Strumparnir" ) );
		assertEquals( expected, actual );

		expected = "Hugi, Kjartan og Strumparnir";
		actual = USArrayUtilities.humanReadableList( list( "Hugi", "Kjartan", "Strumparnir" ) );
		assertEquals( expected, actual );
	}

	/**
	 * Constructs a list for testing. 
	 */
	private static List<?> list( Object... objects ) {
		ArrayList<Object> a = new ArrayList<Object>();

		for( Object o : objects ) {
			a.add( o );
		}

		return a;
	}
}
