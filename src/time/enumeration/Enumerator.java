/**
 * 
 */
package time.enumeration;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.EnumMap;

import time.Utils;

/**
 * @author hoatranduy
 *
 */
public class Enumerator {
	LocalDateTime originDateTime;
	int maxNumOfMatches;
	MatchingCallback matchingCallback;
	MatchingDirection matchingDirection;
	
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
	 * @param matchingDateTime
	 * @param mactchingComponents
	 * @param matchingDirection
	 * @param maxNumOfMatches
	 * @param matchingCallback
	 */
	public Enumerator(
			LocalDateTime matchingDateTime,
			EnumMap<MatchingComponent, Integer> matchingComponents,
			MatchingDirection matchingDirection,
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
		
		assert weekdayOrdinal == null && quarter == null && weekOfMonth == null && weekOfYear == null :
			"Not implemented yet for provided weekdayOrdinal, quarter, weekOfMonth and weekOfYear";
		
		matchFirst();
	}
	
	private void matchFirst() {
		matchingDateTime = originDateTime
				.withYear(year != null ? year : originDateTime.getYear())
				.withMonth(month != null ? month : originDateTime.getMonthValue())
				.withDayOfMonth(day != null ? day : originDateTime.getDayOfMonth())
				.withHour(hour != null? hour : originDateTime.getHour())
				.withMinute(minute != null ? minute : originDateTime.getMinute());
		
		if (weekday != null) {
			if (weekdayOrdinal != null) {
				// not yet implemented
				assert weekdayOrdinal == null;
//				matchingDateTime = matchingDateTime
//						.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday))); // check this
			}
			else {
				if (matchingDirection == MatchingDirection.FORWARD) {
					matchingDateTime = matchingDateTime
							.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
				}
				else {
					matchingDateTime = matchingDateTime
							.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
				}
			}
			matchingDateTime = matchingDateTime
					.withHour(hour != null ? hour : 0)
					.withMinute(minute != null ? minute : 0);
		}
		
		if (quarter != null) {
			// not yet implemented
			assert quarter == null;
//			matchingDateTime = matchingDateTime
//					.withMonth(matchingDateTime.get(IsoFields.QUARTER_OF_YEAR) * 3)
//					.with(TemporalAdjusters.lastDayOfMonth())
//					.plusDays(1)
//					.withHour(hour != null? hour : 0)
//					.withMinute(minute != null ? minute : 0);
		}
		
		if (weekOfMonth != null) {
			// not yet implemented
			assert weekOfMonth == null;
			// locate the first day of next week of month
			//matchingDateTime.get(WeekFields.ISO.weekOfMonth())
			//System.out.println(matchingDateTime.with(WeekFields.ISO.weekOfMonth(), 1));
		}
		
		if (weekOfYear != null) {
			// not yet implemented
			assert weekOfYear == null;
		}
	}
	
	public boolean match(LocalDateTime dt) {
		if (year != null && dt.getYear() != year) return false;
		if (month != null && dt.getMonthValue() != month) return false;
		if (day != null && dt.getDayOfMonth() != day) return false;
		if (hour != null && dt.getHour() != hour) return false;
		if (minute != null && dt.getMinute() != minute) return false;
		if (weekday != null && dt.get(WeekFields.ISO.dayOfWeek()) != weekday) return false;
		if (weekday != null && weekdayOrdinal != null && Utils.getOrdinalOfWeek(dt) != weekdayOrdinal) return false;
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
				if (weekday != null) {
					if (weekdayOrdinal != null) {
						if (matchingDirection == MatchingDirection.FORWARD) {
							//newDateTime = newDateTime
							//		.with(TemporalAdjusters.dayOfWeekInMonth(weekdayOrdinal, DayOfWeek.of(weekday)));
						}
						else {
							
						}
					}
					else {
						if (matchingDirection == MatchingDirection.FORWARD) {
							newDateTime = newDateTime
									.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
						}
						else {
							newDateTime = newDateTime
									.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
						}
					}
				}
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					matchingCallback.onMatched(countOfMatches, matchingDateTime);
					enumerateByMinute();
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
				if (weekday != null) {
					if (matchingDirection == MatchingDirection.FORWARD) {
						newDateTime = newDateTime
								.with(TemporalAdjusters.next(DayOfWeek.of(weekday)));
					}
					else {
						newDateTime = newDateTime
								.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
					}
				}
				if (match(newDateTime)) {
					matchingDateTime = newDateTime;
					countOfMatches ++;
					matchingCallback.onMatched(countOfMatches, matchingDateTime);
					return enumerateByMinute(); // try enumerating by minute from the beginning of new year
				}
				else if (!enumerateByYear()) return false; // try enumerating by year
			}
			return true;
		}
		else return enumerateByYear(); // fixed month, try enumerating by year
	}
	
	private boolean enumerateByWeekdayOrDay() {
		// order: weekday > day
		if (weekday != null) {
			// try enumerating by weekday
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
					matchingCallback.onMatched(countOfMatches, matchingDateTime);
					return enumerateByMinute(); // try enumerating by minute from the beginning of new weekday
				}
				else if (!enumerateByMonth()) return false; // try enumerating by month
			}
			return true;
		}
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
						matchingCallback.onMatched(countOfMatches, matchingDateTime);
						return enumerateByMinute(); // try enumerating by minute from the beginning of new day
					}
					else if (!enumerateByMonth()) return false; // try enumerating by month
				}
				return true;
			}
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
					matchingCallback.onMatched(countOfMatches, matchingDateTime);
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
					matchingCallback.onMatched(countOfMatches, matchingDateTime);
				}
				else if (!enumerateByHour()) return false; // try enumerating by hour 
			}
			return true;
		}
		else return enumerateByHour(); // fixed minute, try enumerating by hour
	}
	
	public int enumerate() {
		if ((matchingDirection == MatchingDirection.FORWARD && matchingDateTime.isAfter(originDateTime))
				|| (matchingDirection == MatchingDirection.BACKWARD && matchingDateTime.isBefore(originDateTime))) {
			countOfMatches ++;
			matchingCallback.onMatched(countOfMatches, matchingDateTime);
		}
		enumerateByMinute();
		return this.countOfMatches;
	}
}
