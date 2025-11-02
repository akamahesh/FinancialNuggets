package com.maheshbhatt.financialnuggets.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateParser {
    private static final Pattern MONTH_DATE_YEAR =
            Pattern.compile("(?i)\\b(January|February|March|April|May|June|July|August|September|October|November|December)\\s+\\d{1,2},\\s*\\d{4}\\b");


    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d, uuuu", Locale.ENGLISH);

    private DateParser() {
    }

    private static Optional<LocalDate> parseLocalDate(String title) {
        if (title == null) return Optional.empty();
        Matcher m = MONTH_DATE_YEAR.matcher(title);
        if (m.find()) {
            String datePart = m.group().trim();
            try {
                return Optional.of(LocalDate.parse(datePart, FORMATTER));
            } catch (DateTimeParseException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static LocalDate parseLocalDateOrThrow(String title) {
        return parseLocalDate(title)
                .orElseThrow(() -> new IllegalArgumentException("No parsable date found in: " + title));
    }

}
