package is.us.wo.util;

import is.us.util.USStringUtilities;

import java.io.*;
import java.net.InetAddress;
import java.util.zip.GZIPOutputStream;

import org.slf4j.*;

import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

import er.extensions.foundation.*;

/**
 * Fetches various information from HTTP-headers. 
 * 
 * @author Hugi Þórðarson 
 */

public class USHTTPUtilities {

	public static final String MIME_TYPE_EXCEL = "application/excel";
	public static final String MIME_TYPE_PDF = "application/pdf";
	public static final String MIME_TYPE_XML = "text/xml";
	public static final String MIME_TYPE_HTML = "text/html";
	public static final String MIME_TYPE_PNG = "image/png";
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	public static final String MIME_TYPE_GIF = "image/gif";

	private static final String UTF_8 = "UTF-8";

	private static final Logger logger = LoggerFactory.getLogger( USHTTPUtilities.class );

	public static final String HEADER_CONTENT_TYPE = "content-type";
	public static final String HEADER_SET_COOKIE = "set-cookie";
	public static final String HEADER_COOKIE = "cookie";
	public static final String HEADER_REFERER = "referer";
	public static final String HEADER_HOST_DEFAULT = "host";
	private static final String HEADER_REMOTE_HOST = "remote_host";
	private static final String HEADER_REMOTE_ADDR = "remote_addr";
	private static final String HEADER_REMOTE_USER = "remote_user";
	private static final String HEADER_WEBOBJECTS_REMOTE_ADDR = "x-webobjects-remote-addr";
	private static final String HEADER_HOST_IIS = "http_host";
	private static final String HEADER_REDIRECT_LOCATION = "location";
	private static final String HEADER_CONTENT_LENGTH = "content-length";
	private static final String HEADER_CONTENT_ENCODING = "content-encoding";
	private static final String HEADER_REDIRECT_URL = "redirect_url";
	private static final String HEADER_ACCEPT_ENCODING = "accept-encoding";
	private static final String HEADER_PRAGMA = "pragma";
	private static final String HEADER_CACHE_CONTROL = "cache-Control";
	private static final String HEADER_CONTENT_DISPOSITION = "content-Disposition";
	private static final String HEADER_USER_AGENT = "user-agent";
	private static final String HEADER_ACCEPT_RANGES = "Accept-Ranges";
	private static final String HEADER_ACCEPT_HEADER = "Accept-Header";
	private static final String HEADER_EXPIRES = "expires";
	private static final String HEADER_CONTENT_INLINE = "inline";
	private static final String HEADER_CONTENT_ATTACHMENT = "attachment";

	private static final String DEFAULT_MIME_TYPE = "octet/stream";
	private static final String CONTENT_ENCODING_GZIP = "gzip";
	private static final String UNTITLED_FILENAME = "Untitled";

	/**
	 * Fetches the IP-address from a WORequest.
	 */
	public static String ipAddressFromRequest( WORequest request ) {
		String host = null;

		if( host == null ) {
			host = request.headerForKey( HEADER_REMOTE_HOST );
			if( host != null )
				return host;

			host = request.headerForKey( HEADER_REMOTE_ADDR );
			if( host != null )
				return host;

			host = request.headerForKey( HEADER_REMOTE_USER );
			if( host != null )
				return host;

			host = request.headerForKey( HEADER_WEBOBJECTS_REMOTE_ADDR );
			if( host != null )
				return host;
		}
		return null;
	}

	/**
	 * Returns the referer header.
	 */
	public static String referer( WORequest request ) {
		return request.headerForKey( HEADER_REFERER );
	}

	/**
	 * Indicates if the given url is secure
	 */
	public static final boolean isSecure( String url ) {
		if( url == null )
			return false;

		if( url.startsWith( "https" ) )
			return true;

		return false;
	}

	/**
	 * This method creates a WOResponse with a temporary (302) redirect to the specified URL
	 *
	 * @param targetURL The URL to redirect to
	 */
	public static WOResponse redirectTemporary( String targetURL ) {
		WOResponse w = new WOResponse();

		w.setHeader( targetURL, HEADER_REDIRECT_LOCATION );
		w.setStatus( 302 );
		w.setHeader( MIME_TYPE_HTML, HEADER_CONTENT_TYPE );
		w.setHeader( "0", HEADER_CONTENT_LENGTH );

		return w;
	}

	/**
	 * This method creates a WOResponse with a permanent (301) redirect to the specified URL
	 *
	 * @param targetURL The URL to redirect to
	 */
	public static WOResponse redirectPermanent( String targetURL ) {
		WOResponse w = new WOResponse();

		w.setHeader( targetURL, HEADER_REDIRECT_LOCATION );
		w.setStatus( 301 );
		w.setHeader( MIME_TYPE_HTML, HEADER_CONTENT_TYPE );
		w.setHeader( "0", HEADER_CONTENT_LENGTH );

		return w;
	}

	/**
	 * Creates a new WOContext with an empty WORequest.
	 * Can be used to create WOContexts for unit tests. 
	 */
	public static WOContext createWOContext( String method, String url ) {
		WORequest worequest = new WORequest( method, url, "HTTP/1.0", NSDictionary.EmptyDictionary, NSData.EmptyData, NSDictionary.EmptyDictionary );
		return new WOContext( worequest );
	}

	/**
	 * Attempts a reverse DNS-lookup of the given IP-address string.
	 */
	public static String lookupIP( String ipAddress ) {
		try {
			return InetAddress.getByName( ipAddress ).getHostName();
		}
		catch( Exception e ) {
			logger.error( "Failed to look up IP-address: " + ipAddress, e );
			return null;
		}
	}

	/**
	 * Attempts to decode a referer string and get the host name from it.
	 */
	public static String hostFromURL( String url ) {
		try {
			int beginningIndex = url.indexOf( "//" );
			int endIndex = url.indexOf( "/", beginningIndex + 2 );
			return url.substring( beginningIndex + 2, endIndex );
		}
		catch( Exception e ) {
			logger.error( "Failed to get host from url: " + url, e );
			return null;
		}
	}

	/**
	 * Attempts to decode a referer string and get the domain from it.
	 */
	public static String domainFromURL( String url ) {
		try {
			NSArray<String> a = NSArray.componentsSeparatedByString( hostFromURL( url ), "." );
			a = a.subarrayWithRange( new NSRange( a.count() - 2, 2 ) );
			return a.componentsJoinedByString( "." );
		}
		catch( Exception e ) {
			logger.error( "Failed to get host from url: " + url, e );
			return null;
		}
	}

	/**
	 * Attempts to decode a top level domain
	 */
	public static String domain( WORequest request ) {
		return domainStringFromHostString( host( request ) );
	}

	/**
	 * Gets the top level domain from a host string. 
	 */
	public static String domainStringFromHostString( String hostString ) {

		if( hostString == null ) {
			return null;
		}

		int i = hostString.lastIndexOf( "." );

		if( i > -1 ) {
			i = hostString.lastIndexOf( ".", i - 1 );

			if( i > -1 ) {
				return hostString.substring( i + 1, hostString.length() );
			}
			else {
				return hostString;
			}
		}

		return null;
	}

	/**
	 * An adaptor-agnostic way of determining the requested host name.
	 */
	public static String host( WORequest request ) {
		String domain = request.headerForKey( HEADER_HOST_DEFAULT );

		if( !USStringUtilities.stringHasValue( domain ) )
			domain = request.headerForKey( HEADER_HOST_IIS );

		if( domain == null )
			return null;

		return domain.toLowerCase();
	}

	/**
	 * Returns the user agent string of the guest.
	 */
	public static String userAgent( WORequest r ) {
		return r.headerForKey( HEADER_USER_AGENT );
	}

	/**
	 * Indicates if the requesting user agent supports Gzip response compression
	 */
	public static boolean contentEncodingGzip( WOResponse response ) {
		String s = contentEncoding( response );
		return CONTENT_ENCODING_GZIP.equals( s );
	}

	/**
	 * Indicates if the requesting user agent supports Gzip response compression
	 */
	public static boolean contentTypeHTML( WOResponse response ) {
		String s = contentType( response );
		return s != null && s.contains( MIME_TYPE_HTML );
	}

	/**
	 * Indicates if the content type of this response is either text or javascript.
	 */
	public static boolean contentTypeTextOrJavascript( WOResponse response ) {
		String s = contentType( response );
		return s != null && (s.contains( "text/" ) || s.contains( "javascript" ));
	}

	/**
	 * Indicates if the content type of this response is either text or javascript.
	 */
	public static boolean contentTypeJavascript( WOResponse response ) {
		String s = contentType( response );
		return s != null && s.contains( "javascript" );
	}

	/**
	 * Indicates if the browser initiating the given request can handle gzip compressed content. 
	 */
	public static boolean supportsGzip( WORequest request ) {
		String s = request.headerForKey( HEADER_ACCEPT_ENCODING );
		return s != null && s.indexOf( CONTENT_ENCODING_GZIP ) > -1;
	}

	/**
	 * WO is very strict when parsing cookies and a cookie with no value
	 * can result in no cookies being available.
	 * see: http://osdir.com/ml/web.webobjects.devel/2002-04/msg00764.html
	 */
	public static void removeNullCookies( WORequest request ) {
		String cookieHeader = request.headerForKey( HEADER_COOKIE );
		StringBuffer fixedCookieHeader = new StringBuffer();

		if( cookieHeader != null ) {
			String[] cookies = cookieHeader.split( ";" );
			for( String cookie : cookies ) {
				String[] pair = cookie.split( "=" );

				//filter out cookies with no value
				if( pair.length == 2 && pair[1] != null && !pair[1].equals( "" ) )
					fixedCookieHeader.append( pair[0] + "=" + pair[1] + ";" );
			}
			request.setHeader( fixedCookieHeader.toString(), HEADER_COOKIE );
		}
	}

	/**
	 * Makes a filename cross-platform and cross browser friendly. 
	 */
	public static String makeFilenameURLFriendly( String fileName, String extension ) {

		if( USStringUtilities.stringHasValue( fileName ) ) {
			if( fileName.length() > 100 )
				fileName = fileName.substring( 0, 100 );

			fileName = USStringUtilities.replace( fileName, "/", "_" );
			fileName = USStringUtilities.replace( fileName, "\\", "_" );
			fileName = USStringUtilities.replace( fileName, "\"", "_" );
			fileName = USStringUtilities.replace( fileName, ":", "_" );
		}
		else
			fileName = "Untitled";

		if( USStringUtilities.stringHasValue( extension ) )
			if( !fileName.toLowerCase().endsWith( extension.toLowerCase() ) )
				fileName = fileName + "." + extension;

		return fileName;
	}

	/**
	 * If the WO app is used as a 404 handler, this method returns the requested URL (that failed). 
	 */
	public static String redirectURL( WORequest r ) {
		return r.headerForKey( HEADER_REDIRECT_URL );
	}

	/**
	 * If the WO app is used as a 404 handler, this method returns the requested URL (that failed). 
	 */
	public static String contentType( WOResponse r ) {
		return r.headerForKey( HEADER_CONTENT_TYPE );
	}

	/**
	 * Returns the value of the content length header. 
	 */
	public static String contentLength( WOResponse r ) {
		return r.headerForKey( HEADER_CONTENT_LENGTH );
	}

	/**
	 * If the WO app is used as a 404 handler, this method returns the requested URL (that failed). 
	 */
	public static void fixContentLengthHeader( WOResponse r ) {
		NSData content = r.content();

		if( content != null ) {
			int contentLength = content.length();
			r.setHeader( String.valueOf( contentLength ), HEADER_CONTENT_LENGTH );
		}
	}

	/**
	 * FIXME: Document streams
	 */
	public static void setContent( WOResponse response, NSData content ) {

		if( content == null )
			content = NSData.EmptyData;

		response.setContent( content );
		int contentLength = content.length();
		response.setHeader( String.valueOf( contentLength ), HEADER_CONTENT_LENGTH );
	}

	/**
	 * If the WO app is used as a 404 handler, this method returns the requested URL (that failed). 
	 */
	public static String contentEncoding( WOResponse r ) {
		return r.headerForKey( HEADER_CONTENT_ENCODING );
	}

	public static WOResponse responseWithDataAndMimeType( String filename, NSData data, String mimeType ) {
		return responseWithDataAndMimeType( filename, data, mimeType, false );
	}

	/**
	 * Creates a WOResponse containing the given data.
	 */
	public static WOResponse responseWithDataAndMimeType( String filename, byte[] bytes, String mimeType ) {

		NSData data = NSData.EmptyData;

		if( bytes != null )
			data = new NSData( bytes );

		return responseWithDataAndMimeType( filename, data, mimeType );
	}

	/**
	 * Creates a WOResponse containing the given string, encoded in UTF-8.
	 */
	public static WOResponse responseWithDataAndMimeType( String filename, String string, String mimeType, boolean forceDownload ) {

		if( !USStringUtilities.stringHasValue( filename ) )
			filename = UNTITLED_FILENAME;

		NSData data = NSData.EmptyData;

		if( data != null ) {
			try {
				data = new NSData( string.getBytes( UTF_8 ) );
			}
			catch( UnsupportedEncodingException e ) {
				logger.debug( "Attempted to convert string to unsupported encoding", e );
			}
		}

		if( mimeType == null )
			mimeType = DEFAULT_MIME_TYPE;

		String disposition = HEADER_CONTENT_INLINE;

		if( forceDownload ) {
			disposition = HEADER_CONTENT_ATTACHMENT;
		}

		WOResponse response = new WOResponse();
		response.setHeader( mimeType + "; charset=UTF-8", HEADER_CONTENT_TYPE );
		response.setHeader( data.length() + "", HEADER_CONTENT_LENGTH );
		response.setHeader( data.length() + "", HEADER_ACCEPT_HEADER );
		response.setHeader( data.length() + "", HEADER_ACCEPT_RANGES );
		response.setHeader( disposition + ";filename=\"" + filename + "\"", HEADER_CONTENT_DISPOSITION );
		response.removeHeadersForKey( HEADER_CACHE_CONTROL );
		response.removeHeadersForKey( HEADER_PRAGMA );
		response.removeHeadersForKey( HEADER_EXPIRES );
		response.removeHeadersForKey( HEADER_EXPIRES );
		response.setContent( data );
		return response;
	}

	/**
	 * Creates a WOResponse containing the given data.
	 */
	public static WOResponse responseWithDataAndMimeType( String filename, NSData data, String mimeType, boolean forceDownload ) {

		if( !USStringUtilities.stringHasValue( filename ) )
			filename = UNTITLED_FILENAME;

		if( data == null )
			data = NSData.EmptyData;

		if( mimeType == null )
			mimeType = DEFAULT_MIME_TYPE;

		String disposition = HEADER_CONTENT_INLINE;

		if( forceDownload ) {
			disposition = HEADER_CONTENT_ATTACHMENT;
		}

		WOResponse response = new WOResponse();
		response.setHeader( mimeType, HEADER_CONTENT_TYPE );
		response.setHeader( data.length() + "", HEADER_CONTENT_LENGTH );
		response.setHeader( data.length() + "", HEADER_ACCEPT_HEADER );
		response.setHeader( data.length() + "", HEADER_ACCEPT_RANGES );
		response.setHeader( disposition + ";filename=\"" + filename + "\"", HEADER_CONTENT_DISPOSITION );
		response.removeHeadersForKey( HEADER_CACHE_CONTROL );
		response.removeHeadersForKey( HEADER_PRAGMA );
		response.removeHeadersForKey( HEADER_EXPIRES );
		response.removeHeadersForKey( HEADER_EXPIRES );
		response.setContent( data );
		return response;
	}

	/**
	 * Gzip compresses the content of the given response and modifies headers accordingly.
	 */
	public static void compressResponse( WOResponse response ) {
		logger.debug( "compressing response" );
		try {
			ByteArrayInputStream in = response.content().stream();
			ERXRefByteArrayOutputStream byteStream = new ERXRefByteArrayOutputStream();

			GZIPOutputStream gzipStream = new GZIPOutputStream( byteStream );

			byte[] buf = new byte[1024];
			int len;
			int totalSizeBefore = 0;

			while( (len = in.read( buf )) > 0 ) {
				totalSizeBefore += len;
				gzipStream.write( buf, 0, len );
			}

			in.close();

			gzipStream.finish();
			gzipStream.close();

			logger.debug( "Response size before compression: " + totalSizeBefore );
			logger.debug( "Response size after compression: " + byteStream.size() );

			NSData compressedData = byteStream.toNSData();
			response.setContent( compressedData );
			response.setHeader( CONTENT_ENCODING_GZIP, HEADER_CONTENT_ENCODING );
			response.setHeader( String.valueOf( compressedData.length() ), HEADER_CONTENT_LENGTH );
		}
		catch( Exception e ) {
			logger.error( "Failed to compress response", e );
		}
	}

	/**
	 * Really, really cleans up the response.
	 * 
	 * WARNING: Experimental! Use at your own risk.
	 */
	public static void ultraCleanResponse( WOResponse response ) {
		String s = response.contentString();
		s = ERXStringUtilities.removeCharacters( s, USC.LF );
		s = ERXStringUtilities.removeCharacters( s, "\r" );
		s = ERXStringUtilities.removeCharacters( s, "\t" );
		response.setContent( s );
		response.setHeader( String.valueOf( response.content().length() ), HEADER_CONTENT_LENGTH );
	}

	/**
	 * 404 
	 */
	public static WOResponse response404() {
		WOResponse r = new WOResponse();
		r.setStatus( 404 );
		return r;
	}

	/**
	 * 500 
	 */
	public static WOResponse response500() {
		WOResponse r = new WOResponse();
		r.setStatus( 500 );
		return r;
	}

	/**
	 * Attempts to determine if a request originates with a human.
	 * 
	 * TODO: Check out list of bots from Google or Yahoo.
	 */
	public static boolean isHuman( String userAgentString ) {

		if( USStringUtilities.stringHasValue( userAgentString ) ) {
			userAgentString = userAgentString.toLowerCase();

			if( userAgentString.indexOf( "bot" ) > 0 )
				return false;

			if( userAgentString.indexOf( "feed" ) > 0 )
				return false;

			if( userAgentString.indexOf( "apple-pubsub" ) > 0 )
				return false;

			if( userAgentString.indexOf( "slurp" ) > 0 )
				return false;

			if( userAgentString.indexOf( "bloglines" ) > 0 )
				return false;
		}

		return true;
	}

	/**
	 * Attempts to determine if a request originates with a bot.
	 * 
	 * TODO: Check out list of bots from Google or Yahoo.
	 */
	public static boolean isBot( String userAgentString ) {
		String u = userAgentString;
		return c( u, "google" ) || c( u, "rss" ) || c( u, "bot" ) || c( u, "yahoo" ) || c( u, "feed" ) || c( u, "reader" );
	}

	/**
	 * Returns true if the strings are not null and the buffer contains the substring.
	 */
	private static boolean c( String buffer, String substring ) {

		if( buffer == null || substring == null ) {
			return false;
		}

		return buffer.toLowerCase().contains( substring.toLowerCase() );
	}

	/**
	 * Creates an xml-typed response and inserts the content string.
	 * 
	 * @param contentString The Content of the response.
	 */
	public static WOResponse createXMLResponseWithContent( String contentString ) {
		WOResponse r = new WOResponse();
		r.setHeader( USC.MIME_TYPE_XML, HEADER_CONTENT_TYPE );
		r.setContent( contentString );
		return r;
	}

	/**
	 * After dispatchRequest has been fired, the "cookie" header in WOResponse has already been set.
	 * If we make changes to cookies after that, we need to set the header manually. 
	 * 
	 * @param response The response to modify.
	 */
	public static void resetCookieHeaderInResponse( WOResponse response ) {

		NSMutableArray<String> cookieHeaderStrings = new NSMutableArray<String>();

		for( WOCookie cookie : response.cookies() ) {
			cookieHeaderStrings.addObject( cookie.headerString() );
		}

		response.setHeaders( cookieHeaderStrings, HEADER_SET_COOKIE );
	}

	/**
	 * Creates a WOResponse containing the given PDF-data in a PDF-file with the given name.
	 * You do not have to specify the ".pdf"-extension to the file name (the method till appends that to the name for you).
	 */
	public static WOResponse pdfResponseWithData( String filename, NSData data ) {

		if( filename == null )
			filename = "Untitled.pdf";

		if( !filename.toLowerCase().endsWith( ".pdf" ) )
			filename = filename + ".pdf";

		return responseWithDataAndMimeType( filename, data, USC.MIME_TYPE_PDF );
	}
}