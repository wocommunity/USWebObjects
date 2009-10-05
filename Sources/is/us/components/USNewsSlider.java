package is.us.components;

import com.webobjects.appserver.*;

import er.ajax.AjaxUtils;
import er.extensions.components.ERXComponent;

/**
 * Component to display news (or other info) in slide show
 *
 * @binding slides required, List of {@link USNewsSliderSlide} to display in the slide show
 * @binding slideDuration optional, the duration each slide should be displayed in seconds, default is 10.0
 * @binding fadeDuration optional, the duration of the fade effect in seconds, the default is 0.5
 * @binding stopButtonText optional, he text that should be displayed on the stop button, the default is "Stop"
 * @binding nextButtonText optional, the text that should be displayed on the "next" button, default is ">"
 * @binding prevButtonText optional, the text that should be displayed on the "previous" button, default is "<"
 *  
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 *
 */
public class USNewsSlider extends ERXComponent {

	// binding default values
	private static final String NEXT_BUTTON_DEFAULT = ">";
	private static final String PREV_BUTTON_DEFAULT = "<";
	private static final String STOP_BUTTON_DEFAULT = "Stop";
	private static final String FADE_DURATION_DEFAULT = "0.5";
	private static final String DURATION_DEFAULT = "10";

	// bindings
	private float _slideDuration;
	private float _fadeDuration;
	private String _stopButtonText;
	private String _nextButtonText;
	private String _prevButtonText;

	/** The current slide, used by the WO repetition */
	public USNewsSliderSlide currentSlide;
	/** the index of the current slid, used by the WO repetition */
	public int currentSlideIndex;

	public USNewsSlider( WOContext context ) {
		super( context );
	}

	/**
	 * @return the parameters to use in javascript when creating an instance of the slide show javascript object.
	 */
	public String params() {
		return slideDuration() + ", " + fadeDuration() + ", '" + stopButtonText() + "', '" + prevButtonText() + "', '" + nextButtonText() + "'";
	}

	/**
	 * @param slideDuration the duration each slide should be displayed in seconds
	 */
	public void setSlideDuration( float slideDuration ) {
		_slideDuration = slideDuration;
	}

	/**
	 * @return the duration each slide is displayed in seconds
	 */
	public float slideDuration() {
		_slideDuration = Float.parseFloat( valueForStringBinding( "slideDuration", DURATION_DEFAULT ) );
		return _slideDuration;
	}

	/**
	 * @param fadeDuration the duration of the fade effect in seconds
	 */
	public void setFadeDuration( float fadeDuration ) {
		_fadeDuration = fadeDuration;
	}

	/**
	 * @return the duration of the fade effect in seconds
	 */
	public float fadeDuration() {
		_fadeDuration = Float.parseFloat( valueForStringBinding( "fadeDuration", FADE_DURATION_DEFAULT ) );
		return _fadeDuration;
	}

	/**
	 * @param stopButtonText the text that should be displayed on the stop button
	 */
	public void setStopButtonText( String stopButtonText ) {
		_stopButtonText = stopButtonText;
	}

	/**
	 * @return the text that should be displayed on the stop button
	 */
	public String stopButtonText() {
		_stopButtonText = valueForStringBinding( "stopButtonText", STOP_BUTTON_DEFAULT );
		return _stopButtonText;
	}

	/**
	 * @param prevButtonText the text that should be displayed on the previous button
	 */
	public void setPrevButtonText( String prevButtonText ) {
		_prevButtonText = prevButtonText;
	}

	/**
	 * @return the text that should be displayed on the previous button
	 */
	public String prevButtonText() {
		_prevButtonText = valueForStringBinding( "prevButtonText", PREV_BUTTON_DEFAULT );
		return _prevButtonText;
	}

	/**
	 * @param nextButtonText the text that should be displayed on the next button
	 */
	public void setNextButtonText( String nextButtonText ) {
		_nextButtonText = nextButtonText;
	}

	/**
	 * @return the text that should be displayed on the next button
	 */
	public String nextButtonText() {
		_nextButtonText = (String)valueForStringBinding( "nextButtonText", NEXT_BUTTON_DEFAULT );
		return _nextButtonText;
	}

	@Override
	protected boolean useDefaultComponentCSS() {
		return true;
	}

	@Override
	protected boolean useDefaultComponentJavascript() {
		return true;
	}

	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	@Override
	public void appendToResponse( WOResponse response, WOContext context ) {
		AjaxUtils.addScriptResourceInHead( context, response, "prototype.js" );
		AjaxUtils.addScriptResourceInHead( context, response, "effects.js" );
		super.appendToResponse( response, context );
	}

}