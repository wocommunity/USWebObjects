package is.us.components;

//TODO: needs review  JIRA VEF-494
/**
 * Class that contains data for a slide for the news slider component
 * 
 * @author Atli Páll Hafsteinsson <atlip@us.is>
 *
 */
public class USNewsSliderSlide {

	private String _heading;
	private String _text;
	private String _link;
	private byte[] _imageData;

	/**
	 * @param heading the news story heading
	 * @param text the news story text (short)
	 * @param link the link to the full news story
	 * @param imageData the image to display with the news story
	 */
	public USNewsSliderSlide( String heading, String text, String link, byte[] imageData ) {
		_heading = heading;
		_text = text;
		_link = link;
		_imageData = imageData;
	}

	/**
	 * @return the news story heading
	 */
	public String heading() {
		return _heading;
	}

	/**
	 * @return the news story text
	 */
	public String text() {
		return _text;
	}

	/**
	 * @return the link to the full news story
	 */
	public String link() {
		return _link;
	}

	/**
	 * @return the image to display with the news story
	 */
	public byte[] imageData() {
		return _imageData;
	}

}
