package is.us.components;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;

import er.ajax.AjaxUtils;
import er.extensions.components.ERXComponent;

/**
 * Component to display news (or other info) in slideshow-ish way
 *
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 *
 */
public class USNewsSlider extends ERXComponent {

	public NSArray<USNewsSliderSlide> slides;
	public float slideDuration = 10f;
	public float fadeDuration = 0.5f;
	public String stopButtonText = "Stop";
	public String nextButtonText = ">";
	public String prevButtonText = "<";

	public USNewsSliderSlide currentSlide;
	public int currentSlideIndex;

	public USNewsSlider( WOContext context ) {
		super( context );
	}

	/**
	 * @return true if and only if the current slide has image data
	 */
	public boolean hasImage() {
		return currentSlide.imageData() != null;
	}

	public String params() {
		return slideDuration + ", " + fadeDuration + ", '" + stopButtonText + "', '" + prevButtonText + "', '" + nextButtonText + "'";
	}

	@Override
	protected boolean useDefaultComponentCSS() {
		return true;
	}

	@Override
	protected boolean useDefaultComponentJavascript() {
		return true;
	}

	//	@Override
	//	protected com.webobjects.foundation.NSArray<String> additionalJavascriptFiles() {
	//		NSMutableArray<String> a = new NSMutableArray<String>();
	//		a.addObject( "/WebObjects/Frameworks/Ajax.framework/WebServerResources/prototype.js" );
	//		a.addObject( "/WebObjects/Frameworks/Ajax.framework/WebServerResources/effects.js" );
	//		return a;
	//	}

	@Override
	public void appendToResponse( WOResponse response, WOContext context ) {
		AjaxUtils.addScriptResourceInHead( context, response, "prototype.js" );
		AjaxUtils.addScriptResourceInHead( context, response, "effects.js" );
		super.appendToResponse( response, context );
	}

}