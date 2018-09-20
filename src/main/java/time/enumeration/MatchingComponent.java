package time.enumeration;

/**
 * Matching components or time patterns that can be used in the enumeration.
 * <p>
 * Available matching components:
 * <ul>
 * 	<li>{@link #YEAR},</li>
 * 	<li>{@link #MONTH},</li>
 * 	<li>{@link #DAY},</li>
 * 	<li>{@link #HOUR},</li>
 * 	<li>{@link #MINUTE},</li>
 * 	<li>{@link #WEEKDAY},</li>
 * 	<li>{@link #WEEKDAY_ORDINAL},</li>
 * 	<li>{@link #QUARTER},</li>
 * 	<li>{@link #WEEK_OF_MONTH},</li>
 * 	<li>{@link #WEEK_OF_YEAR},</li>
 * </ul>
 */
public enum MatchingComponent {
	/**
	 * Year pattern
	 */
	YEAR,
	/**
	 * Month pattern
	 */
	MONTH,
	/**
	 * Day pattern
	 */
	DAY,
	/**
	 * Hour pattern
	 */
	HOUR,
	/**
	 * Minute pattern
	 */
	MINUTE,
	/**
	 * Weekday pattern
	 */
	WEEKDAY,
	/**
	 * Weekday ordinal pattern
	 */
	WEEKDAY_ORDINAL,
	/**
	 * Quarter pattern
	 */
	QUARTER,
	/**
	 * Week of month pattern
	 */
	WEEK_OF_MONTH,
	/**
	 * Week on year pattern
	 */
	WEEK_OF_YEAR;
}
