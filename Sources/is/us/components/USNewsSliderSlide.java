package is.us.components;

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

	public USNewsSliderSlide( String heading, String text, String link, byte[] imageData ) {
		_heading = heading;
		_text = text;
		_link = link;
		_imageData = imageData;
	}

	public String heading() {
		return _heading;
	}

	public String text() {
		return _text;
	}

	public String link() {
		return _link;
	}

	public byte[] imageData() {
		return _imageData;
	}

}
