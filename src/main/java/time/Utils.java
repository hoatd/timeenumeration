package time;

import java.time.LocalDateTime;

/**
 * @author hoatranduy
 *
 */
public class Utils {
	public static int getOrdinalOfWeek(LocalDateTime dt) {
		/* determine the sequential ordinal number of a weekday.
		 * i.e. if a Friday is the first or 3rd Friday of a month.
		 * take the day of month, subtract 1, divide by 7, then add 1.
		 * The first seven days of the month are always the first (Tuesday,
		 * Wednesday, ...) whatever day of the week the actual 1st of the month is.
		 * */
	    return ((dt.getDayOfMonth() - 1) / 7) + 1;
	}
}
