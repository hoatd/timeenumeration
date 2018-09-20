/**
 * 
 */
package time.enumeration;

import java.time.LocalDateTime;

/**
 * Functional interface for defining code block to be invoked for each matching date.
 * <p>
 * For example, to define a callback code block to print the date object with its matching count:
 * <pre>{@code
 * MatchingCallback matchingCallback = (matchedCount, matchedValue) -> {
 *	System.out.printf("Match#%d: %s\n", matchedCount, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
 * };	
 * }</pre>
 */

@FunctionalInterface
public interface MatchingCallback {
	/**
	 * Callback function to be invoked for matching date
	 * 
	 * @param matchedCount The 1-based counting number of current match calculates from the beginning of the enumeration
	 * @param matchedValue Current matched date object
	 */
	void onMatched(int matchedCount, LocalDateTime matchedValue);
}
