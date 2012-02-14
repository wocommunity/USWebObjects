package is.us.wo.util;

import java.util.Random;

import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * Various constants used throughout US.
 * 
 * @author Hugi Þórðarson
 */

public class USC {

	public static final String MIME_TYPE_EXCEL = "application/excel";
	public static final String MIME_TYPE_PDF = "application/pdf";
	public static final String MIME_TYPE_XML = "text/xml";
	public static final String MIME_TYPE_HTML = "text/html";
	public static final String MIME_TYPE_PNG = "image/png";
	public static final String MIME_TYPE_JPEG = "image/jpeg";
	public static final String MIME_TYPE_GIF = "image/gif";

	/**
	 * No instances created, ever.
	 */
	private USC() {}

	public static final Integer TRUE_INTEGER = new Integer( 1 );
	public static final Integer FALSE_INTEGER = new Integer( 0 );

	public static final Double TRUE_DOUBLE = new Double( 1 );
	public static final Double FALSE_DOUBLE = new Double( 0 );

	public static final Long TRUE_LONG = new Long( 1 );
	public static final Long FALSE_LONG = new Long( 0 );

	public static final String TRUE_STRING = "true";
	public static final String FALSE_STRING = "false";

	public static final String PERMNO_KEY = "permno";
	public static final String PERSIDNO_KEY = "persidno";
	public static final String REGNO_KEY = "regno";
	public static final String VIN_KEY = "vin";
	public static final String NAME_KEY = "name";
	public static final String CODE_KEY = "code";
	public static final String TRANSDATE_KEY = "transdate";
	public static final String USER_KEY = "user";

	public static final NSArray<EOSortOrdering> PERMNO_ASC_SORT_ORDERINGS = new NSArray<EOSortOrdering>( new EOSortOrdering( PERMNO_KEY, EOSortOrdering.CompareAscending ) );
	public static final NSArray<EOSortOrdering> NAME_ASC_SORT_ORDERINGS = new NSArray<EOSortOrdering>( new EOSortOrdering( NAME_KEY, EOSortOrdering.CompareCaseInsensitiveAscending ) );

	public static final Random RANDOM = new Random();

	// Skyrr communication constants
	public static final String SKYRR_DATE_FORMAT = "yyyyMMdd";
	public static final String SKYRR_DATE_FORMAT_SHORT = "yyyyMM";

	/**
	 * Key used in session for the default language.
	 */
	public static final String LANGUAGE_ICELANDIC = "Icelandic";

	/**
	 * This is used in some variables
	 */
	public static final String UNKNOWN_MUNICIPALITY_CODE = "9999";

	// Commonly used strings
	public static final String EMPTY_STRING = "";
	public static final String NULL_STRING = "null";
	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String DOT = ".";
	public static final String STAR = "*";
	public static final String TAB = "\t";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";
	public static final String QUOTE = "\"";
	public static final String SEMICOLON = ";";
	public static final NSArray<String> MONTH_NAMES = new NSArray<String>( new String[] { "janúar", "febrúar", "mars", "apríl", "maí", "júní", "júlí", "ágúst", "september", "október", "nóvember", "desember" } );
	public static final NSArray<String> WEEK_DAY_NAMES = new NSArray<String>( new String[] { "sunnudagur", "mánudagur", "þriðjudagur", "miðvikudagur", "fimmtudagur", "föstudagur", "laugardagur" } );
	public static final char[] ICELANDIC_ALPHABET = "AÁBCDÐEÉFGHIÍJKLMNOÓPQRSTUÚVWXYÝÞÆÖZaábcdðeéfghiíjklmnoópqrstuúvwxyýþæöz0123456789".toCharArray();
	public static final String US_PERSIDNO = "6608023120";

	// Framework names
	public static final String US_COMMON_COMPONENTS = "USCommonComponents";
	public static final String EKJA_FRAMEWORK = "EkjaFramework";
	public static final String APP_FRAMEWORK = "app";
	public static final String MITT_FRAMEWORK = "Mitt";

	public static final String CSS_ODD_ROW = "oddRow";
	public static final String CSS_EVEN_ROW = "evenRow";

	// Cimp
	public static final String IMPORT_CODE_1 = "1";
	public static final String IMPORT_CODE_2 = "2";
	public static final String IMPORT_CODE_3 = "3";
	public static final String IMPORT_CODE_4 = "4";
	public static final String IMPORT_CODE_5 = "5";
	public static final String IMPORT_CODE_6 = "6";
	public static final String IMPORT_CODE_7 = "7";
	public static final String IMPORT_CODE_8 = "8";
	public static final String IMPORT_CODE_9 = "9";

	public static final NSArray<String> USED_VEHICLE_IMPORT_CODES = new NSArray<String>( new String[] { IMPORT_CODE_1, IMPORT_CODE_3, IMPORT_CODE_6, IMPORT_CODE_7, IMPORT_CODE_8, IMPORT_CODE_9 } );
	public static final NSArray<String> NEW_VEHICLE_IMPORT_CODES = new NSArray<String>( new String[] { IMPORT_CODE_2, IMPORT_CODE_4, IMPORT_CODE_5 } );

	// Cint 
	public static final String INSPTYPE_L = "L";
	public static final String INSPTYPE_LS = "LS";
	public static final String INSPTYPE_ADALSKODUN = "A";
	public static final String INSPTYPE_ADALSKODUN_LAST_YEAR = "X";
	public static final String INSPTYPE_SKRANINGARSKODUN = "S";

	public static final String ERROR_EMAIL_ADDRESS = "kerfisvilla@us.is";
	public static final String TD_EMAIL_ADDRESS = "td@us.is";
	public static final String KEYRSLUR_EMAIL_ADDRESS = "keyrslur@us.is";

	public static final String UTF_8 = "UTF-8";
	public static final String ISO_8859_1 = "ISO-8859-1";

	// Cpls - Plate status codes
	public static final String PLATE_STATUS_ORDERED = "01";
	public static final String PLATE_STATUS_IN_PRODUCTION = "02";
	public static final String PLATE_STATUS_RECEIVED = "03";
	public static final String PLATE_STATUS_IN_STORAGE = "04";
	public static final String PLATE_STATUS_ON_VEHICLE = "05";
	public static final String PLATE_STATUS_DESTROYED = "06";
	public static final String PLATE_STATUS_RENTED = "08";
	public static final String PLATE_STATUS_LOST = "10";
	public static final String PLATE_STATUS_REDIRECTED = "11";

	// Cret.code
	public static final String REGTYPE_CUSTOMS = "TO";
	public static final String REGTYPE_PREREGISTERED = "FO";
	public static final String REGTYPE_NEWREGISTERED = "NY";
	public static final String REGTYPE_REREGISTERED = "EN";
	public static final String REGTYPE_DEREGISTERED = "A";
	public static final String REGTYPE_RECYCLED = "M";

	// Cret.subcode
	public static final String REG_SUBTYPE_OTHER = "A";
	public static final String REG_SUBTYPE_COUNTRY = "B";
	public static final String REG_SUBTYPE_OOORDER = "O";
	public static final String REG_SUBTYPE_LOST = "T";
	public static final String REG_SUBTYPE_DAM = "V";
	public static final String REG_SUBTYPE_AUTHOR = "Y";
	public static final String REG_SUBTYPE_GENERAL = "A";
	public static final String REG_SUBTYPE_RECYCLE = "U";
	public static final String REG_SUBTYPE_UNKNOWN = "ZZ";

	// Crgr
	public static final String REGGROUP_PRIVATE_NUMBERS = "N2";
	public static final String REGGROUP_PRIVATE_NUMBERS_LIGHTWEIGHT = "N10";
	public static final String REGGROUP_TRIAL_NUMBERS = "N9";

	// Cstn
	public static final String STATION_NUMBERS_REGISTRARS = "R";
	public static final String STATION_NUMBERS_INSPECTORS = "S";

	// Cfue
	public static final String FUEL_NO_ENGINE = "0"; //	Vélarlaus
	public static final String FUEL_GASOLINE = "1"; //	Bensín
	public static final String FUEL_DIESEL = "2"; //	Dísel
	public static final String FUEL_ELECTRIC = "3"; //	Rafmagn
	public static final String FUEL_GAS = "4"; //	Gas
	public static final String FUEL_HYDROGEN = "5"; //	Vetni
	public static final String FUEL_GASOLINE_AND_ELECTRIC = "6";//	Bensín/Rafmagn
	public static final String FUEL_OTHER = "7"; //	Annað
	public static final String FUEL_METHAN = "8"; //	Metan
	public static final String FUEL_UNKNOWN = "9"; //	Óþekkt
	public static final String FUEL_CAN_NOT_BE_USED = "10";//	Má ekki nota

	// Plin
	public static final String PLATE_DEPOSIT = "I"; //	Innlögn
	public static final String PLATE_WITHDRAWAL = "A"; //	úttekt

}