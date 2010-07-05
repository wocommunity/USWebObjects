package is.us.util;

import static org.junit.Assert.assertEquals;
import is.us.wo.util.USHTTPUtilities;

import org.junit.Test;

import com.webobjects.appserver.WORequest;

/**
 * @author Hugi Þórðarson
 */

public class TestUSHTTPUtilities {

	@Test
	public void testipAddressFromRequest() {
		String testAddress = "0.0.0.0";

		WORequest r = fakeRequest();
		r.setHeader( testAddress, "remote_addr" );

		assertEquals( testAddress, USHTTPUtilities.ipAddressFromRequest( r ) );
	}

	private static WORequest fakeRequest() {
		return new WORequest( "GET", "", "HTTP/1.1", null, null, null );
	}

	@Test
	public void domainStringFromHostString() {
		String s = null;

		s = USHTTPUtilities.domainStringFromHostString( "www.us.is" );
		System.out.println( s );

		s = USHTTPUtilities.domainStringFromHostString( "us.is" );
		System.out.println( s );

	}
}