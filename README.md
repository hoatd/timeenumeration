# timeenumeration
Enumerating dates in the past or future that match time patterns

# Java 8, 9 or 10

# Input
- A date object as starting time of the enumeration
- Direction for enumerating from give date object: forward for matching dates in future or backward for matching dates in the past
- Time patterns or matching components: year, quarter, month, day, hour, minute, weekOfMonth, weekOfYear, weekday, weekdayOrdinal
- Maximum number of dates to calculate
- A code block to be triggered with each matched date

# Output
Matching dates in the future

# Time patterns/ matching components
- Support patterns: year, quarter, month, day, hour, minute, weekOfMonth, weekOfYear, weekday, weekdayOrdinal
- Second is currently ignored, but all other date components are variable if nothing else is specified by the date matching components.
- Monday is the first day of the week (weekday = 1) and Sunday is the end of the week (weekday = 7).
- The weekdayOrdinal is only valid in combination with weekday. The 2nd Thursday in October can be described as month: 10, weekday: 4, weekdayOrdinal: 2

# Example 1
Calculate the next 5 occurrences on a yearly interval

## Input:
- date object: 04.09.2018 11:06
- matching components: day: 4, month: 9, hour: 11, minute: 6
- number of matches: 2
- code block that prints the matched date

## Output:
- 04.09.2019 11:06
- 04.09.2020 11:06

# Example 2
Calculate the date of every Monday in September 2018

## Input:
- date object: 04.09.2018 11:06
- matching components: year: 2018, month: 9, minute: 30, weekday: 1
- number of matches: 200
- code block that prints the matched date

## Output:
- 10.09.2018 00:30
- 10.09.2018 01:30
- 10.09.2018 02:30
- …
- 10.09.2018 23:30
- 17.09.2018 00:30
- …
- 24.09.2018 23:30

# USAGE
For example, list maximum 200 dates of every Mondayin September 2018 at each minute 30th after 04/09/2018 11:06:
```
// create the date object
LocalDateTime matchingDateTime = LocalDateTime.of(LocalDate.of(2018, 9, 4), LocalTime.of(11, 06));
// matching dates in the future by forward direction
MatchingDirection matchingDirection = MatchingDirection.FORWARD;
// create a collection of matching components as EnumMap 
EnumMap<MatchingComponent, Integer> matchingComponents = new EnumMap<MatchingComponent, Integer>(MatchingComponent.class);
// define matching component for year, month, day, minute, and weekday by their corresponding value
matchingComponents.put(MatchingComponent.YEAR, 2018);
matchingComponents.put(MatchingComponent.MONTH, 9);
matchingComponents.put(MatchingComponent.DAY, 4);
matchingComponents.put(MatchingComponent.MINUTE, 30);
matchingComponents.put(MatchingComponent.WEEKDAY, 1);
// Maximum of matches is 200
int maxNumOfMatches = 200;
// Define code block that prints the matched date object
MatchingCallback matchingCallback = (matchedCount, matchedValue) -> {
    System.out.printf("Match#%d: %s\n", matchedCount, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(matchedValue));
};
// Create a enumerator object with desired parameters 
Enumerator enumerator = new Enumerator(matchingDateTime, matchingDirection, matchingComponents, maxNumOfMatches, matchingCallback);
// Do enumerating and get the actual number of matches
int countOfMatches = enumerator.enumerate();
// Print total of matches 
System.out.printf("Total of matched dates are %d\n", countOfMatches);
```
