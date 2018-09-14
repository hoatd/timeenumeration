/**
 * 
 */
package time.enumeration;

import java.time.LocalDateTime;

/**
 * @author hoatranduy
 *
 */

@FunctionalInterface
public interface MatchingCallback {
	void onMatched(int matchCount, LocalDateTime matchDateTime);
}
