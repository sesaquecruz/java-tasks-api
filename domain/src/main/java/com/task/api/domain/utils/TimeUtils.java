package com.task.api.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

public final class TimeUtils {
    private final static String timeFormat = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}Z$";
    private final static Pattern timePattern = Pattern.compile(timeFormat);

    private TimeUtils() { }

    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    public static boolean isValidInstant(String instant) {
        return timePattern.matcher(instant).matches();
    }
}
