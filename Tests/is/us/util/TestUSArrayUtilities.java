package is.us.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestUSArrayUtilities {

	@Test
	public void searchUnsorted() {
		String[] testArr = new String[2];
		testArr[0] = "index 0";
		testArr[1] = "index 1";
		assertEquals( 1, USCollectionUtilities.searchUnsorted( testArr, "index 1" ) );
		assertEquals( -1, USCollectionUtilities.searchUnsorted( testArr, "index 2" ) );
		assertEquals( -1, USCollectionUtilities.searchUnsorted( null, "index 2" ) );
		assertEquals( -1, USCollectionUtilities.searchUnsorted( testArr, null ) );
		assertEquals( -1, USCollectionUtilities.searchUnsorted( null, null ) );
	}

	@Test
	public void resize() {
		String[] oldArr = new String[2];
		oldArr[0] = "index 0";
		oldArr[1] = "index 1";
		String[] newArr = USCollectionUtilities.resize( oldArr, 4 );

		assertEquals( 4, newArr.length );
		assertEquals( oldArr[0], "index 0" );
		assertEquals( oldArr[1], "index 1" );

		newArr = USCollectionUtilities.resize( oldArr, 0 );
		assertEquals( newArr.length, 0 );

		newArr = USCollectionUtilities.resize( oldArr, -1 );
		assertEquals( newArr.length, 0 );
	}
}
