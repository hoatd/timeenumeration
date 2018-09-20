package time.enumeration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @author hoatranduy
 *
 */
class EnumeratorTest {
	private void printMatchingComponents(
			LocalDateTime dt, 
			MatchingDirection matchingDirection, 
			int countOfMatches, 
			EnumMap<MatchingComponent, Integer> matchingComponents) {
		Integer year = matchingComponents.get(MatchingComponent.YEAR);
		Integer month = matchingComponents.get(MatchingComponent.MONTH);
		Integer day = matchingComponents.get(MatchingComponent.DAY);
		Integer hour = matchingComponents.get(MatchingComponent.HOUR);
		Integer minute = matchingComponents.get(MatchingComponent.MINUTE);
		Integer weekday = matchingComponents.get(MatchingComponent.WEEKDAY);
		Integer weekdayOrdinal = matchingComponents.get(MatchingComponent.WEEKDAY_ORDINAL);
		Integer quarter = matchingComponents.get(MatchingComponent.QUARTER);
		Integer weekOfMonth = matchingComponents.get(MatchingComponent.WEEK_OF_MONTH);
		Integer weekOfYear = matchingComponents.get(MatchingComponent.WEEK_OF_YEAR);
		
		System.out.printf("Date:%s\nDirection:%s\nCount:%d\nPatterns:\n\tYear:%s\n\tMonth:%s\n\tDay:%s\n\tHour:%s\n\tMinute:%s\n\tWeekday:%s\n\tWeekdayOrdinal:%s\n\tQuarter:%s\n\tWeekOfMonth:%s\n\tWeekOfYear:%s\n",
				DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(dt),
				matchingDirection,
				countOfMatches,
				year != null ? year : "N/A",
				month != null ? month : "N/A",
				day != null ? day : "N/A",
				hour != null ? hour : "N/A",
				minute != null ? minute : "N/A",
				weekday != null ? weekday : "N/A",
				weekday != null && weekdayOrdinal != null ? weekdayOrdinal : "N/A",
				quarter != null ? quarter : "N/A",
				weekOfMonth != null ? weekOfMonth : "N/A",
				weekOfYear != null ? weekOfYear : "N/A");
	}

	@Test
	void testEnumerate1() {
		System.out.println("testEnumerate1 - Occurrences on a yearly interval of:");
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 4), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		matchingComponents.put(MatchingComponent.MONTH, 9);
		matchingComponents.put(MatchingComponent.DAY, 4);
		matchingComponents.put(MatchingComponent.HOUR, 11);
		matchingComponents.put(MatchingComponent.MINUTE, 6);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			};
		Enumerator enumerator = new Enumerator(
				matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
				matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}

	@Test
	void testEnumerate2() {
		System.out.println("testEnumerate2 - The date of every Monday in September 2018 of:");
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 4), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		matchingComponents.put(MatchingComponent.YEAR, 2018);
		matchingComponents.put(MatchingComponent.MONTH, 9);
		matchingComponents.put(MatchingComponent.MINUTE, 30);
		matchingComponents.put(MatchingComponent.WEEKDAY, 1);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			}; 
		Enumerator enumerator = new Enumerator(
				matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
				matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}
	
	@Test
	void testEnumerate3() {
		System.out.println("testEnumerate3");
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 4), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		//matchingComponents.put(MatchingComponent.YEAR, 2018);
		matchingComponents.put(MatchingComponent.MONTH, 9);
		matchingComponents.put(MatchingComponent.HOUR, 1);
		matchingComponents.put(MatchingComponent.MINUTE, 30);
		matchingComponents.put(MatchingComponent.WEEKDAY, 1);
		matchingComponents.put(MatchingComponent.WEEKDAY_ORDINAL, 2);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			}; 
		Enumerator enumerator = new Enumerator(
				matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
				matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}
	
	@Test
	void testEnumerate4() {
		System.out.println("testEnumerate4");
		// Test for weekOfMonth
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 14), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		//matchingComponents.put(MatchingComponent.YEAR, 2018);
		matchingComponents.put(MatchingComponent.MONTH, 9);
		matchingComponents.put(MatchingComponent.HOUR, 1);
		matchingComponents.put(MatchingComponent.MINUTE, 30);
		matchingComponents.put(MatchingComponent.WEEK_OF_MONTH, 2);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			}; 
		Enumerator enumerator = new Enumerator(
				matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
				matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}
	
	@Test
	void testEnumerate5() {
		System.out.println("testEnumerate5");
		// Test for weekOfYear
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 14), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		//matchingComponents.put(MatchingComponent.YEAR, 2018);
		matchingComponents.put(MatchingComponent.HOUR, 1);
		matchingComponents.put(MatchingComponent.MINUTE, 30);
		matchingComponents.put(MatchingComponent.WEEK_OF_YEAR, 40);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			}; 
		Enumerator enumerator = new Enumerator(
					matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
					matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}
	
	@Test
	void testEnumerate6() {
		System.out.println("testEnumerate6");
		// Test for quarter
		LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 14), LocalTime.of(11, 06));
		EnumMap<MatchingComponent, Integer> matchingComponents = 
				new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
		//matchingComponents.put(MatchingComponent.YEAR, 2018);
		matchingComponents.put(MatchingComponent.HOUR, 1);
		matchingComponents.put(MatchingComponent.MINUTE, 30);
		matchingComponents.put(MatchingComponent.QUARTER, 3);
		//matchingComponents.put(MatchingComponent.WEEK_OF_MONTH, 2);
		matchingComponents.put(MatchingComponent.WEEKDAY, 1);
		matchingComponents.put(MatchingComponent.WEEKDAY_ORDINAL, 2);
		MatchingDirection matchingDirection = MatchingDirection.FORWARD;
		int maxNumOfMatches = 200;
		List<LocalDateTime> matchedDateTimes = new ArrayList<LocalDateTime>();
		MatchingCallback matchingCallback = (matchedCount, matchedValue)-> {
			matchedDateTimes.add(matchedValue);
			System.out.printf("Match#%d: %s\n", matchedCount,
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
			}; 
		Enumerator enumerator = new Enumerator(
				matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches,
				matchingCallback);
		printMatchingComponents(matchingDateTime, matchingDirection, maxNumOfMatches, matchingComponents);
		int countOfMatches = enumerator.enumerate();
		System.out.printf("Total of matched dates was %d\n", countOfMatches);
	}
}
