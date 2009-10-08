package is.us.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestUSArrayUtilities {

	@Test
	public void searchUnsorted() {
		String[] testArr = new String[2];
		testArr[0] = "index 0";
		testArr[1] = "index 1";
		assertEquals( USArrayUtilities.searchUnsorted( testArr, "index 1" ), 1 );
		assertEquals( USArrayUtilities.searchUnsorted( testArr, "index 2" ), -1 );
		assertEquals( USArrayUtilities.searchUnsorted( null, "index 2" ), -1 );
		assertEquals( USArrayUtilities.searchUnsorted( testArr, null ), -1 );
		assertEquals( USArrayUtilities.searchUnsorted( null, null ), -1 );
	}

	@Test
	public void resize() {
		String[] oldArr = new String[2];
		oldArr[0] = "index 0";
		oldArr[1] = "index 1";
		String[] newArr = USArrayUtilities.resize( oldArr, 4 );

		assertEquals( newArr.length, 4 );
		assertEquals( oldArr[0], "index 0" );
		assertEquals( oldArr[1], "index 1" );

		newArr = USArrayUtilities.resize( oldArr, 0 );
		assertEquals( newArr.length, 0 );

		newArr = USArrayUtilities.resize( oldArr, -1 );
		assertEquals( newArr.length, 0 );
	}
}
