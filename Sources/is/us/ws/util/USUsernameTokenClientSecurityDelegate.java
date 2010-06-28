package is.us.ws.util;

import javax.xml.soap.SOAPElement;

import org.apache.axis.*;
import org.apache.axis.message.SOAPHeader;
import org.slf4j.*;

/**
 * For adding a WSS UsernameToken to a SOAP request header.
 * 
 * @author Hugi Þórðarson
 */

public class USUsernameTokenClientSecurityDelegate {

	private static final String HDR_PASSWORD = "Password";
	private static final String HDR_USERNAME = "Username";
	private static final String HDR_USERNAME_TOKEN = "UsernameToken";
	private static final String HDR_SECURITY = "Security";

	private static final Logger logger = LoggerFactory.getLogger( USUsernameTokenClientSecurityDelegate.class );

	/**
	 * The username to use for authentication.
	 */
	private String _username;

	/**
	 * The password to use for authentication.
	 */
	private String _password;

	/**
	 * Constructs a new security delegate. with the given username and password.
	 * 
	 * @param username The username to send.
	 * @param password the password to send.
	 */
	public USUsernameTokenClientSecurityDelegate( String username, String password ) {
		logger.debug( "Constructed delegate with username " + username );
		setUsername( username );
		setPassword( password );
	}

	public void setUsername( String _username ) {
		this._username = _username;
	}

	public String username() {
		return _username;
	}

	public void setPassword( String _password ) {
		this._password = _password;
	}

	public String password() {
		return _password;
	}

	/**
	 * Each time a WOWebServiceClient registered with this security handler invokes
	 * a remote method, this method will preprocess the message and add the required
	 * security headers.
	 * 
	 * @param mc The current Axis MessageContext.
	 */
	public void processClientRequest( MessageContext mc ) throws AxisFault {
		try {
			SOAPHeader header = (SOAPHeader)mc.getMessage().getSOAPPart().getEnvelope().getHeader();
			SOAPElement securityElement = header.addChildElement( HDR_SECURITY );
			SOAPElement usernameTokenElement = securityElement.addChildElement( HDR_USERNAME_TOKEN );
			SOAPElement usernameElement = usernameTokenElement.addChildElement( HDR_USERNAME );
			usernameElement.addTextNode( username() );
			SOAPElement passwordElement = usernameTokenElement.addChildElement( HDR_PASSWORD );
			passwordElement.addTextNode( password() );
		}
		catch( Exception e ) {
			logger.error( "Could not add security information to SOAP header", e );
		}
	}
}