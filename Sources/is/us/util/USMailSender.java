package is.us.util;

import is.us.wo.util.USC;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOApplication;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.javamail.ERMailDataAttachment;
import er.javamail.ERMailDeliveryPlainText;

/**
 * USMailSender is a very simple class for sending email.
 * 
 * @author Hugi Þórðarson
 */

public class USMailSender {

	private static final Logger logger = LoggerFactory.getLogger( USMailSender.class );

	private static final Random RANDOM = new Random();

	/**
	 * We don't want no instances, da dumm, dee dumm.
	 */
	private USMailSender() {}

	/**
	 * Composes an email message with the given parameters and sends it
	 * immediately. You can specify either a plaintext message body, an HTML
	 * message body or both.
	 * 
	 * Uses the default mail server specified in the SoloWeb system's settings.
	 * 
	 * @param from The senders email address
	 * @param to An NSArray containing the recipients' email addresses as
	 *        strings.
	 * @param cc An NSArray containing the cc'd recipients' email addresses as
	 *        strings.
	 * @param bcc An NSArray containing the bcc'd recipients' email addresses as
	 *        strings.
	 * @param subject The message subject
	 * @param plainTextBody The message's plain text body.
	 * @param htmlBody The message's HTML body.
	 * @param attachmentFilePaths An NSArray of Strings containing paths to
	 *        files to attach to the letter.
	 */
	public static void composeEmailWithAlternateTextAndAttachments( String from, NSArray<String> to, NSArray<String> cc, NSArray<String> bcc, String subject, String plaintextBody, String htmlBody, NSArray<String> attachmentFilePaths ) {

		try {
			Properties defaultProperties = new Properties();
			defaultProperties.put( "mail.smtp.host", WOApplication.application().SMTPHost() );
			javax.mail.Session defaultMailSession = javax.mail.Session.getDefaultInstance( defaultProperties, null );
			MimeMessage msg = new MimeMessage( defaultMailSession );
			msg.setSentDate( new Date() );

			if( from != null )
				msg.setFrom( new InternetAddress( from ) );

			if( to != null )
				msg.setRecipients( Message.RecipientType.TO, InternetAddress.parse( to.componentsJoinedByString( ", " ), false ) );

			if( cc != null )
				msg.setRecipients( Message.RecipientType.CC, InternetAddress.parse( cc.componentsJoinedByString( ", " ), false ) );

			if( bcc != null )
				msg.setRecipients( Message.RecipientType.BCC, InternetAddress.parse( bcc.componentsJoinedByString( ", " ), false ) );

			if( subject != null )
				msg.setSubject( subject, "iso-8859-1" );

			// Create the message
			MimeMultipart alt = new MimeMultipart( "alternative" );

			// Insert plain text content

			MimeBodyPart mbp = new MimeBodyPart();
			if( plaintextBody == null )
				plaintextBody = "";
			mbp.setContent( plaintextBody, "text/plain; charset=\"iso-8859-1\"" );
			alt.addBodyPart( mbp );

			// Insert HTML content
			if( USStringUtilities.stringHasValue( htmlBody ) ) {
				MimeBodyPart mbp2 = new MimeBodyPart();
				mbp2.setContent( htmlBody, "text/html; charset=\"iso-8859-1\"" );
				alt.addBodyPart( mbp2 );
			}

			if( !USArrayUtilities.arrayHasObjects( attachmentFilePaths ) ) {
				msg.setContent( alt );
			}
			else {
				MimeMultipart mixed = new MimeMultipart( "mixed" );
				MimeBodyPart wrap = new MimeBodyPart();
				wrap.setContent( alt );
				mixed.addBodyPart( wrap );

				// Insert attachment
				for( String attachmentFilePath : attachmentFilePaths ) {
					if( USStringUtilities.stringHasValue( attachmentFilePath ) ) {
						MimeBodyPart attachment = new MimeBodyPart();
						FileDataSource source = new FileDataSource( attachmentFilePath );
						attachment.setDataHandler( new DataHandler( source ) );
						attachment.setFileName( NSPathUtilities.lastPathComponent( attachmentFilePath ) );
						mixed.addBodyPart( attachment );
					}
				}

				msg.setContent( mixed );
			}

			Transport.send( msg );
		}
		catch( Exception e ) {
			logger.error( "Failed to send email", e );
		}
	}

	/**
	 * Composes an email message with the given parameters and sends it
	 * immediately. You can specify either a plaintext message body, an HTML
	 * message body or both.
	 * 
	 * Uses the default mail server specified in the SoloWeb system's settings.
	 * 
	 * @param from The senders email address
	 * @param to An NSArray containing the recipients' email addresses as
	 *        strings.
	 * @param cc An NSArray containing the cc'd recipients' email addresses as
	 *        strings.
	 * @param bcc An NSArray containing the bcc'd recipients' email addresses as
	 *        strings.
	 * @param subject The message subject
	 * @param plainTextBody The message's plain text body.
	 * @param htmlBody The message's HTML body.
	 */
	public static void composeEmail( String from, NSArray<String> to, NSArray<String> cc, NSArray<String> bcc, String subject, String plaintextBody, String htmlBody ) {
		composeEmailWithAlternateTextAndAttachments( from, to, cc, bcc, subject, plaintextBody, htmlBody, null );
	}

	/**
	 * Send mail with multiple attachments.
	 */
	public static void composeEmail( String fromEmail, NSArray<String> toAddresses, String subject, String content, NSDictionary<String, NSData> attachments ) {
		try {
			ERMailDeliveryPlainText mail = new ERMailDeliveryPlainText();
			mail.newMail();
			mail.setFromAddress( fromEmail );
			mail.setToAddresses( toAddresses );
			mail.setSubject( subject );

			if( !USStringUtilities.stringHasValue( content ) )
				content = USC.EMPTY_STRING;

			mail.setTextContent( content );

			if( attachments != null ) {
				Enumeration<String> allAttachmentNames = attachments.allKeys().objectEnumerator();

				while( allAttachmentNames.hasMoreElements() ) {
					String nextAttachmentName = allAttachmentNames.nextElement();
					mail.addAttachment( new ERMailDataAttachment( nextAttachmentName, String.valueOf( RANDOM.nextInt() ), attachments.objectForKey( nextAttachmentName ) ) );
				}
			}

			mail.sendMail();
		}
		catch( Exception e ) {
			logger.error( "Failed to send email...", e );
		}
	}

	/**
	 * Sends one copy of the email to each recipient.
	 */
	public static void sendInMultipleEmails( String from, NSArray<String> recipients, String emailSubject, String emailContent, NSDictionary<String, NSData> attachments ) {
		for( String emailAddress : recipients ) {
			composeEmail( from, new NSArray<String>( emailAddress ), emailSubject, emailContent, attachments );
		}
	}

	/**
	 * Sends one copy of the email to each recipient.
	 */

	public static void composeEmail( String fromEmail, NSArray<String> toAddresses, String subject, String content, String fileName, NSData fileData ) {

		NSDictionary<String, NSData> attachment = null;

		if( USStringUtilities.stringHasValue( fileName ) )
			attachment = new NSDictionary<String, NSData>( fileData, fileName );

		composeEmail( fromEmail, toAddresses, subject, content, attachment );
	}
}