package is.us.util;

/**
 * All sortable objects in SoloWeb implement this interface to allow for simple manipulation.
 * 
 * @author Hugi Þórðarson
 */

public interface USSortable {

	public static final int UP = -1;
	public static final int DOWN = 1;

	public Integer sortNumber();

	public void setSortNumber( Integer aValue );
}
