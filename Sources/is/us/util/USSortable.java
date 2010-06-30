package is.us.util;

/**
 * @author Hugi Þórðarson
 */

public interface USSortable {

	public static final int UP = -1;
	public static final int DOWN = 1;

	public Integer sortNumber();

	public void setSortNumber( Integer aValue );
}
