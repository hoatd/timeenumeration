# timeenumeration
Enumerating dates in the future by matching components: year, quarter, month, day, hour, minute, weekOfMonth, weekOfYear, weekday, weekdayOrdinal

# Java 8, 9 or 10

# Input
- a date object
- the date matching components
- how many dates to calculate
- a code block to trigger with each calculated date

# Output
Matching dates in the future

# Date matching components:
- Support patterns: year, quarter, month, day, hour, minute, weekOfMonth, weekOfYear, weekday, weekdayOrdinal
- Second is currently ignored, but all other date components are variable if nothing else is specified by the date matching components.
- Monday is the first day of the week (weekday = 1) and Sunday is the end of the week (weekday = 7).
- The weekdayOrdinal is only valid in combination with weekday. The 2nd Thursday in October can be described as month: 10, weekday: 4, weekdayOrdinal: 2

# Example 1
Calculate the next 5 occurrences on a yearly interval:

## Input:
- date: 04.09.2018 11:06
- matching components: day: 4, month: 9, hour: 11, minute: 6
- count: 2
- a code block that prints the date for example

## Output:
- 04.09.2019 11:06
- 04.09.2020 11:06

# Example 2
Calculate the date of every Monday in September 2018

## Input:
- date: 04.09.2018 11:06
- matching components: year: 2018, month: 9, minute: 30, weekday: 1
- count: 200

## Output:
- 10.09.2018 00:30
- 10.09.2018 01:30
- 10.09.2018 02:30
- …
- 10.09.2018 23:30
- 17.09.2018 00:30
- …
- 24.09.2018 23:30
