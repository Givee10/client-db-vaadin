package com.givee.demo.client.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DatesUtil {
	//public static final String GRID_DATE_FORMAT = "%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS";
	private static final String FILE_DATE_FORMAT = "yyyy-MM-dd";
	public static final String GRID_DATE_FORMAT = "dd.MM.yyyy HH:mm";
	public static final String ONLY_DATE_FORMAT = "dd.MM.yyyy";
	public static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
	public static final String NORMAL_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
	public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final String TABLE_DATE_FORMAT = "HH:mm dd.MM.yyyy 'г.'";
	public static final String SQL_DATE_FORMAT = "YYYY-MM-DD HH24:MI:SS.US";

	public static String formatFile(LocalDateTime dateToConvert) {
		return localDateTimeToString(dateToConvert, FILE_DATE_FORMAT);
	}
	public static String formatOnlyDate(LocalDateTime dateToConvert){
		return localDateTimeToString(dateToConvert,ONLY_DATE_FORMAT);
	}

	public static String formatJSON(LocalDateTime dateToConvert) {
		return localDateTimeToString(dateToConvert, JSON_DATE_FORMAT);
	}

	public static String formatNormal(LocalDateTime dateToConvert) {
		return localDateTimeToString(dateToConvert, NORMAL_DATE_FORMAT);
	}

	public static String formatStandard(LocalDateTime dateToConvert) {
		return localDateTimeToString(dateToConvert, STANDARD_DATE_FORMAT);
	}

	public static String formatTable(LocalDateTime dateToConvert) {
		return localDateTimeToString(dateToConvert, TABLE_DATE_FORMAT);
	}

	public static String formatZoned(ZonedDateTime dateToConvert) {
		return zonedDateTimeToString(dateToConvert, NORMAL_DATE_FORMAT);
	}

	public static String formatGrid(ZonedDateTime dateToConvert) {
		return zonedDateTimeToString(dateToConvert, GRID_DATE_FORMAT);
	}

	public static LocalDateTime stringToLocal(String dateToConvert) {
		return StringUtil.isNull(dateToConvert) ? null : LocalDateTime.parse(dateToConvert, DateTimeFormatter.ofPattern(GRID_DATE_FORMAT));
	}

	public static boolean isBetween(ZonedDateTime start, ZonedDateTime end, ZonedDateTime time) {
		return time.isAfter(start) && time.isBefore(end);
	}

	public static boolean isBetween(LocalDateTime start, LocalDateTime end, LocalDateTime time) {
		return time.isAfter(start) && time.isBefore(end);
	}

	public static String getSqlBetween(LocalDateTime from, LocalDateTime to) {
		if (from == null || to == null) return null;
		return "BETWEEN " + getSqlToDate(from) + " AND " + getSqlToDate(to);
	}

	public static String getSqlToDate(LocalDateTime date) {
		if (date == null) return null;
		return "TO_TIMESTAMP('" + formatJSON(date) + "', '" + SQL_DATE_FORMAT + "')";
	}

	public static LocalDateTime setStartDateTime() {
		return setNullDateTime().minusDays(7);
	}

	public static LocalDateTime setNullDateTime() {
		return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	public static LocalDateTime setTomorrowNullTime() {
		return LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
	}

	public static ZonedDateTime setHourInDate(ZonedDateTime date, Integer hour) {
		return date.withHour(hour).withMinute(0).withSecond(0).withNano(0);
	}

	public static ZonedDateTime setCurrentDate(ZonedDateTime date) {
		ZonedDateTime now = ZonedDateTime.now();
		return ZonedDateTime.of(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(),
				date.getHour(), date.getMinute(), date.getSecond(), date.getNano(), ZoneOffset.UTC);
	}

	public static LocalDateTime convertFromUTC(LocalDateTime date) {
		return date == null ? null : date.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDateTime convertToUTC(LocalDateTime date) {
		return date == null ? null : date.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	public static ZonedDateTime localToZoned(LocalDateTime date) {
		return date == null ? null : date.atZone(ZoneOffset.UTC);
	}

	public static LocalDateTime zonedToLocal(ZonedDateTime date) {
		return date == null ? null : date.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
	}

	public static String zonedDateTimeToString(ZonedDateTime dateToConvert, String pattern) {
		return dateToConvert == null ? "" :
				dateToConvert.withZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String localDateTimeToString(LocalDateTime dateToConvert, String pattern) {
		return dateToConvert == null ? "" : dateToConvert.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parseStringDate(String dateToConvert) {
		if (StringUtil.isNull(dateToConvert)) return null;
		if (!dateToConvert.contains(".")) dateToConvert = dateToConvert.concat(".");
		String substring = dateToConvert.substring(dateToConvert.lastIndexOf(".") + 1);
		for (int i = 6; i > substring.length(); i--)
			dateToConvert = dateToConvert.concat("0");
		return LocalDateTime.parse(dateToConvert, DateTimeFormatter.ofPattern(JSON_DATE_FORMAT));
	}
}
