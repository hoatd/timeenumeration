/**
 * 
 */
package time.enumeration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.EnumMap;


/**
 * Represents the Enumerator model for date matching enumeration. Matching components are
 * one or more of: {@code year, quarter, month, day, hour, minute, weekOfMonth, weekOfYear, weekday,
 * weekdayOrdinal}. The direction of matching is either backward or forward. It decides
 * matched dates are occurred in the past or future from a given date.
 * <p>
 * For example, list maximum {@code 200} dates of every {@code Monday}in
 * {@code September 2018} at each minute {@code 30}th after {@code 04/09/2018 11:06}:
 * <pre>{@code
 * // create the date object
 * LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 4), LocalTime.of(11, 06));
 * // matching dates in the future by forward direction
 * MatchingDirection matchingDirection = MatchingDirection.FORWARD;
 * // create a collection of matching components as EnumMap 
 * EnumMap<MatchingComponent, Integer> matchingComponents = new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
 * // define matching component for year, month, day, minute, and weekday by their corresponding value
 * matchingComponents.put(MatchingComponent.YEAR, 2018);
 * matchingComponents.put(MatchingComponent.MONTH, 9);
 * matchingComponents.put(MatchingComponent.DAY, 4);
 * matchingComponents.put(MatchingComponent.MINUTE, 30);
 * matchingComponents.put(MatchingComponent.WEEKDAY, 1);
 * // Maximum of matches is 200
 * int maxNumOfMatches = 200;
 * // Define code block that prints the matched date object
 * MatchingCallback matchingCallback = (matchedCount, matchedValue) -> {
 *	System.out.printf("Match#%d: %s\n", matchedCount, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
 * };
 * // Create a enumerator object with desired parameters 
 * Enumerator enumerator = new Enumerator(matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches, matchingCallback);
 * // Do enumerating and get the actual number of matches
 * int countOfMatches = enumerator.enumerate();
 * // Print total of matches 
 * System.out.printf("Total of matched dates are %d\n", countOfMatches);
 * }</pre>
 */
public class Enumerator {
	LocalDateTime originDateTime;
	MatchingDirection matchingDirection;
	int maxNumOfMatches;
	MatchingCallback matchingCallback;
	
	LocalDateTime matchingDateTime;
	int countOfMatches;
	
	Integer year;
	Integer month;
	Integer day;
	Integer hour;
	Integer minute;
	Integer weekday;
	Integer weekdayOrdinal;
	Integer quarter;
	Integer weekOfMonth;
	Integer weekOfYear;

	/**
	 * Constructs a Enumerator object with a date object, direction of enumeration,
	 * matching components, maximum number of matches, and a code block to be triggered
	 * for each match.
	 * 
	 * @param matchingDateTime A date object
	 * @param matchingDirection A direction of matching enumeration
	 * @param matchingComponents Matching components
	 * @param maxNumOfMatches Maximum number of matching dates
	 * @param matchingCallback A code block to trigger with each calculated date
	 */
	public Enumerator(
			LocalDateTime matchingDateTime,
			MatchingDirection matchingDirection,
			EnumMap<MatchingComponent, Integer> matchingComponents,
			int maxNumOfMatches,
			MatchingCallback matchingCallback) {
		
		this.originDateTime = matchingDateTime;
		this.matchingDirection = matchingDirection;
		this.maxNumOfMatches = maxNumOfMatches;
		this.matchingCallback = matchingCallback;
		
		year = matchingComponents.get(MatchingComponent.YEAR);
		month = matchingComponents.get(MatchingComponent.MONTH);
		day = matchingComponents.get(MatchingComponent.DAY);
		hour = matchingComponents.get(MatchingComponent.HOUR);
		minute = matchingComponents.get(MatchingComponent.MINUTE);
		weekday = matchingComponents.get(MatchingComponent.WEEKDAY);
		weekdayOrdinal = matchingComponents.get(MatchingComponent.WEEKDAY_ORDINAL);
		quarter = matchingComponents.get(MatchingComponent.QUARTER);
		weekOfMonth = matchingComponents.get(MatchingComponent.WEEK_OF_MONTH);
		weekOfYear = matchingComponents.get(MatchingComponent.WEEK_OF_YEAR);
		
		matchFirst();
	}
	
	private static int getOrdinalOfWeek(LocalDateTime dt) {
		/* determine the sequential ordinal number of a weekday.
		 * i.e. if a Friday is the first or 3rd Friday of a month.
		 * take the day of month, subtract 1, divide by 7, then add 1.
		 * The first seven days of the month are always the first (Tuesday,
		 * Wednesday, ...) whatever day of the week the actual 1st of the month is.
		 * */
	    return ((dt.getDayOfMonth() - 1) / 7) + 1;
	}
	
	private void matchFirst() {
		matchingDateTime = originDateTime
				.withYear(year != null ? year : originDateTime.getYear())
				.withMonth(month != null ? month : originDateTime.getMonthValue())
				.withDayOfMonth(day != null ? day : originDateTime.getDayOfMonth())
				.withHour(hour != null? hour : originDateTime.getHour())
				.withMinute(minute != null ? minute : originDateTime.getMinute());
		
		if (quarter != null) {
			LocalDateTime newDateTime = LocalDateTime
					.of(LocalDate.of(matchingDateTime.getYear(), 1, 1), LocalTime.of(0, 0))
					.plusMonths(3 * (quarter - 1));
			if (matchingDirection == MatchingDirection.FORWARD) {
				while(!newDateTime.isAfter(originDateTime)) {
					newDateTime = newDateTime.plusDays(1);
				}
			}
			else {
				while(!newDateTime.isBefore(originDateTime)) {
					newDateTime = newDateTime.plusDays(-1);
				}
			}
			matchingDateTime = newDateTime
					.withHour(hour != null? hour : 0)
					.withMinute(minute != null ? minute : 0);
		}
		
		if (weekOfYear != null) {
			LocalDateTime newDateTime = matchingDateTime
					.with(WeekFields.ISO.weekOfYear(), weekOfYear) // locate week of year
					.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
			if (matchingDirection == MatchingDirection.FORWARD) {
				while(!newDateTime.isAfter(originDateTime)) {
					newDateTime = newDateTime.plusDays(1);
				}
			}
			else {
				while(!newDateTime.isBefore(originDateTime)) {
					newDateTime = newDateTime.plusDays(-1);
				}
			}
			matchingDateTime = newDateTime
					.withHour(hour != null? hour : 0)
					.withMinute(minute != null ? minute : 0);
		}
		else if (weekOfMonth != null) {
			LocalDateTime newDateTime = matchingDateTime
					.with(WeekFields.ISO.weekOfMonth(), weekOfMonth) // locate week of month
					.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
			if (matchingDirection == MatchingDirection.FORWARD) {
				while(!newDateTime.isAfter(originDateTime)) {
					newDateTime = newDateTime.plusDays(1);
				}
			}
			else {
				while(!newDateTime.isBefore(originDateTime)) {
					newDateTime = newDateTime.plusDays(-1);
				}
			}
			matchingDateTime = newDateTime
					.withHour(hour != null? hour : 0)
					.withMinute(minute != null ? minute : 0);
		} 
		
		if (weekday != null) {
			if (weekdayOrdinal != null) {
				LocalDateTime newDateTime = matchingDateTime
						.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
				if (matchingDirection == MatchingDirection.FORWARD) {
					while(!newDateTime.isAfter(originDateTime)) {
						newDateTime = newDateTime
								.plusMonths(1)
								.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
					}
				}
				else {
					while(!newDateTime.isBefore(originDateTime)) {
						newDateTime = newDateTime
								.plusMonths(-1)
								.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
					}
				}
				matchingDateTime = newDateTime;
			} // end of if (weekdayOrdinal != null) {
			else {
				if (matchingDirection == MatchingDirection.FORWARD) {
					if (matchingDateTime.isAfter(originDateTime)) {
						matchingDateTime = matchingDateTime
								.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(weekday)));
					}
					else {
						matchingDateTime = matchingDateTime
								.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
					}
				}
				else {
					if (matchingDateTime.isBefore(originDateTime)) {
						matchingDateTime = matchingDateTime
								.with(TemporalAdjusters.previousOrSame(DayOfWeek.of(weekday)));
					}
					else {
						matchingDateTime = matchingDateTime
								.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
					}
				}
			}
			matchingDateTime = matchingDateTime
					.withHour(hour != null ? hour : 0)
					.withMinute(minute != null ? minute : 0);
		} // end of if (weekday != null) {
	}
	
	/**
	 * Check if the given date object matches provided matching components
	 * 
	 * @param dt A date object
	 * @return True if match, otherwise False 
	 */
	public boolean match(LocalDateTime dt) {
		if (year != null && dt.getYear() != year) return false;
		if (month != null && dt.getMonthValue() != month) return false;
		if (day != null && dt.getDayOfMonth() != day) return false;
		if (hour != null && dt.getHour() != hour) return false;
		if (minute != null && dt.getMinute() != minute) return false;
		if (weekday != null && dt.get(WeekFields.ISO.dayOfWeek()) != weekday) return false;
		if (weekday != null && weekdayOrdinal != null && getOrdinalOfWeek(dt) != weekdayOrdinal) return false;
		if (quarter != null && dt.get(IsoFields.QUARTER_OF_YEAR) != quarter) return false;
		if (weekOfMonth != null && dt.get(WeekFields.ISO.weekOfMonth()) != weekOfMonth) return false;
		if (weekOfYear != null && dt.get(WeekFields.ISO.weekOfYear()) != weekOfYear) return false;
		return true;
	}
	
	private boolean enumerateByYear() {
		if (year == null) {
			while (countOfMatches < maxNumOfMatches) {
				LocalDateTime newDateTime = matchingDateTime
						.plusYears(matchingDirection == MatchingDirection.FORWARD ? 1 : -1)
						.withMonth(month != null ? month : 1)
						.withDayOfMonth(day != null ? day : 1)
						.withHour(hour != null ? hour : 0)
						.withMinute(minute != null ? minute : 0);
				
				if (quarter != null) {
					newDateTime = LocalDateTime
							.of(LocalDate.of(newDateTime.getYear(), 1, 1), LocalTime.of(0, 0))
							.plusMonths(3 * (quarter - 1));
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				
				if (weekOfYear != null) {
					newDateTime = newDateTime
							.with(WeekFields.ISO.weekOfYear(), weekOfYear) // locate week of year
							.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				else if (weekOfMonth != null) {
					newDateTime = newDateTime
							.with(WeekFields.ISO.weekOfMonth(), weekOfMonth) // locate week of month
							.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				
				if (weekday != null) {
					if (weekdayOrdinal != null) {
						newDateTime = newDateTime
								.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
						if (matchingDirection == MatchingDirection.FORWARD) {
							while(!newDateTime.isAfter(matchingDateTime)) {
								newDateTime = newDateTime
										.plusMonths(1)
										.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
							}
						}
						else {
							while(!newDateTime.isBefore(matchingDateTime)) {
								newDateTime = newDateTime
										.minusMonths(1)
										.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
							}
						}
					}
					else {
						if (matchingDirection == MatchingDirection.FORWARD) {
							if (newDateTime.isAfter(matchingDateTime)) {
								newDateTime = newDateTime
										.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(weekday)));
							}
							else {
								newDateTime = newDateTime
										.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
							}
						}
						else {
							if (newDateTime.isBefore(matchingDateTime)) {
								newDateTime = newDateTime
										.with(TemporalAdjusters.previousOrSame(DayOfWeek.of(weekday)));
							}
							else {
								newDateTime = newDateTime
										.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
							}
						}
					}
				}
				
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
					return enumerateByMinute(); // try enumerating by minute from the beginning of new year
				}
				else return false; // no more time for matching
			}
			return true;
		}
		else return false; // fixed year, no more time for matching
	}
	
	private boolean enumerateByMonth() {
		if (month == null) {
			while (countOfMatches < maxNumOfMatches) {
				LocalDateTime newDateTime = matchingDateTime
						.plusMonths(matchingDirection == MatchingDirection.FORWARD ? 1 : -1)
						.withDayOfMonth(day != null ? day : 1)
						.withHour(hour != null ? hour : 0)
						.withMinute(minute != null ? minute : 0);
				
				if (quarter != null) {
					newDateTime = LocalDateTime
							.of(LocalDate.of(newDateTime.getYear(), 1, 1), LocalTime.of(0, 0))
							.plusMonths(3 * (quarter - 1));
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				
				if (weekOfYear != null) {
					newDateTime = newDateTime
							.with(WeekFields.ISO.weekOfYear(), weekOfYear) // locate week of year
							.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				else if (weekOfMonth != null) {
					newDateTime = newDateTime
							.with(WeekFields.ISO.weekOfMonth(), weekOfMonth) // locate week of month
							.with(WeekFields.ISO.dayOfWeek(), 1); // locate first day of week
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(1);
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime.plusDays(-1);
						}
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
				}
				
				if (weekday != null) {
					if (weekdayOrdinal != null) {
						newDateTime = newDateTime
								.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
						if (matchingDirection == MatchingDirection.FORWARD) {
							while(!newDateTime.isAfter(matchingDateTime)) {
								newDateTime = newDateTime
										.plusMonths(1)
										.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
							}
						}
						else {
							while(!newDateTime.isBefore(matchingDateTime)) {
								newDateTime = newDateTime
										.plusMonths(-1)
										.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
							}
						}
					}
					else {
						if (matchingDirection == MatchingDirection.FORWARD) {
							if (newDateTime.isAfter(matchingDateTime)) {
								newDateTime = newDateTime
										.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(weekday)));
							}
							else {
								newDateTime = newDateTime
										.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
							}
						}
						else {
							if (newDateTime.isBefore(matchingDateTime)) {
								newDateTime = newDateTime
										.with(TemporalAdjusters.previousOrSame(DayOfWeek.of(weekday)));
							}
							else {
								newDateTime = newDateTime
										.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
							}
						}
					}
				}
				
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
					return enumerateByMinute(); // try enumerating by minute from the beginning of new year
				}
				else if (!enumerateByYear()) return false; // try enumerating by year
			}
			return true;
		}
		else return enumerateByYear(); // fixed month, try enumerating by year
	}
	
	private boolean enumerateByWeekdayOrDay() {
		if (weekday != null) {
			if (weekdayOrdinal != null) { // try enumerating by weekday & weekday ordinal
				while (countOfMatches < maxNumOfMatches) {
					LocalDateTime newDateTime = matchingDateTime
							.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
					if (matchingDirection == MatchingDirection.FORWARD) {
						while(!newDateTime.isAfter(matchingDateTime)) {
							newDateTime = newDateTime
									.plusMonths(1)
									.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
						}
					}
					else {
						while(!newDateTime.isBefore(matchingDateTime)) {
							newDateTime = newDateTime
									.minusMonths(1)
									.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
						}
					}
					if (match(newDateTime)) {
						matchingDateTime = newDateTime;
						countOfMatches ++;
						if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
						return enumerateByMinute(); // try enumerating by minute from the beginning of new weekday
					}
					else if (!enumerateByMonth()) return false; // try enumerating by month
				}
				return true;
			}
			else { // try enumerating by weekday
				while (countOfMatches < maxNumOfMatches) {
					LocalDateTime newDateTime = null;
					if (matchingDirection == MatchingDirection.FORWARD) {
						newDateTime = matchingDateTime
								.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
					}
					else {
						newDateTime = matchingDateTime
								.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
					}
					newDateTime = newDateTime
							.withHour(hour != null? hour : 0)
							.withMinute(minute != null ? minute : 0);
					if (match(newDateTime)) {
						matchingDateTime = newDateTime;
						countOfMatches ++;
						if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
						return enumerateByMinute(); // try enumerating by minute from the beginning of new weekday
					}
					else if (!enumerateByMonth()) return false; // try enumerating by month
				}
				return true;
			}
		} // end of if (weekday != null) {
		else {
			// try enumerating by Day
			if (day == null) {
				while (countOfMatches < maxNumOfMatches) {
					LocalDateTime newDateTime = matchingDateTime
							.plusDays(matchingDirection == MatchingDirection.FORWARD ? 1 : -1)
							.withHour(hour != null ? hour : 0)
							.withMinute(minute != null ? minute : 0);
					if (match(newDateTime)) {
						matchingDateTime = newDateTime;
						countOfMatches ++;
						if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
						return enumerateByMinute(); // try enumerating by minute from the beginning of new day
					}
					else if (!enumerateByMonth()) return false; // try enumerating by month
				}
				return true;
			} // end of if (day == null) {
			else return enumerateByMonth(); // fixed day, try enumerating by month
		}
	}
	
	private boolean enumerateByHour() {
		if (hour == null) { 
			while (countOfMatches < maxNumOfMatches) {
				LocalDateTime newDateTime = matchingDateTime
						.plusHours(matchingDirection == MatchingDirection.FORWARD ? 1 : -1)
						.withMinute(minute != null ? minute : 0);
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
					return enumerateByMinute(); // try enumerating by minute from the beginning of new hour
				}
				else if (!enumerateByWeekdayOrDay()) return false; // try enumerating by day or weekday
			}
			return true;
		}
		else return enumerateByWeekdayOrDay(); // fixed hour, try enumerating by day or weekday
	}
	
	private boolean enumerateByMinute() {
		if (minute == null) {
			while (countOfMatches < maxNumOfMatches) {
				LocalDateTime newDateTime = matchingDateTime
						.plusMinutes(matchingDirection == MatchingDirection.FORWARD ? 1 : -1);
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
				}
				else if (!enumerateByHour()) return false; // try enumerating by hour 
			}
			return true;
		}
		else return enumerateByHour(); // fixed minute, try enumerating by hour
	}
	
	/**
	 * Enumerate matching dates until reaches maximum number of matches or no more match found. 
	 * 
	 * @return Number of matched dates 
	 */
	public int enumerate() {
		if ((matchingDirection == MatchingDirection.FORWARD && matchingDateTime.isAfter(originDateTime))
				|| (matchingDirection == MatchingDirection.BACKWARD && matchingDateTime.isBefore(originDateTime))) {
			countOfMatches ++;
			if (matchingCallback != null) matchingCallback.onMatched(countOfMatches, matchingDateTime);
		}
		enumerateByMinute();
		return this.countOfMatches;
	}
}
