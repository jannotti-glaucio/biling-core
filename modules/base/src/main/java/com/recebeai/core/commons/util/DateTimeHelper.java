package tech.jannotti.billing.core.commons.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateTimeHelper {

    private static final String UTC_ZONE_ID = "UTC";

    public static final String LOCAL_DATE_WITHOUT_DAY = "yyyy_MM";

    public static LocalDate getNowDate() {
        LocalDate date = LocalDate.now();
        return date;
    }

    public static LocalDateTime getNowDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime;
    }

    public static LocalDateTime getNowDateTime(String zoneId) {
        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of(zoneId));
        return dateTime;
    }

    public static LocalDateTime getNowUTCDateTime() {
        return getNowDateTime(UTC_ZONE_ID);
    }

    public static Long getNowLongDateTime() {
        return System.currentTimeMillis();
    }

    public static LocalDate getNowDatePlusDays(int days) {
        LocalDate date = getNowDate();
        return date.plusDays(days);
    }

    public static LocalDate getNowDateMinusDays(int days) {
        LocalDate date = getNowDate();
        return date.minusDays(days);
    }

    public static LocalDate getNowDatePlusMonths(int months) {
        LocalDate date = getNowDate();
        return date.plusMonths(months);
    }

    public static LocalDate getNowDateMinusMonths(int months) {
        LocalDate date = getNowDate();
        return date.minusMonths(months);
    }

    public static LocalDate getDate(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return date;
    }

    public static LocalDate getDate(int year, int month) {
        return getDate(year, month, 1);
    }

    public static boolean beforeToday(LocalDate date) {
        LocalDate nowDate = getNowDate();
        return date.isBefore(nowDate);
    }

    public static long toUnixTimestamp(LocalDateTime dateTime) {

        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        long unixTimestamp = instant.toEpochMilli() / 1000;
        return unixTimestamp;
    }

    public static String format(Temporal date, DateTimeFormatter dateTimeFormat) {
        if (date == null)
            return null;

        return dateTimeFormat.format(date);
    }

    public static String format(Temporal date, String format) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(format);
        return format(date, dateTimeFormat);
    }

    public static String formatToIsoTimestamp(LocalDateTime dateTime) {
        return format(dateTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String formatToIsoDate(LocalDate date) {
        return format(date, DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate parseDate(String date, String format) {
        if (StringUtils.isBlank(date))
            return null;

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(date, dateTimeFormat);
    }

    public static LocalDateTime parseDateTime(String date, String format) {
        if (StringUtils.isBlank(date))
            return null;

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, dateTimeFormat);
    }

    public static LocalDate parseFromIsoDate(String date) {
        if (StringUtils.isBlank(date))
            return null;

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE;
        return LocalDate.parse(date, dateTimeFormat);
    }

    public static LocalDateTime parseFromIsoTimestamp(String date) {
        if (StringUtils.isBlank(date))
            return null;

        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(date, dateTimeFormat);
    }

    public static Calendar toCalendar(LocalDate localDate) {
        if (localDate == null)
            return null;

        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(instant));
        return calendar;
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null)
            return null;

        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;

        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null)
            return null;

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

}