/**
 * 
 */
package time.enumeration;

/**
 * Matching directions that can be used in the enumeration.
 * <p>
 * Support forward and backward direction for corresponding future and past date enumerating:
 * <ul>
 * 	<li>{@link #FORWARD},</li>
 * 	<li>{@link #BACKWARD}</li>
 * </ul> 
 */
public enum MatchingDirection {
	/**
	 * Forward direction for matching dates in the future
	 */
	FORWARD,
	/**
	 * Backward direction for matching dates in the past
	 */
	BACKWARD 
}
