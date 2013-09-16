package org.shirdrn.kodz.inaction.hbase.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatetimeUtils {

	@SuppressWarnings("deprecation")
	public static Date getDatetime(int howManyDays, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, howManyDays);
		Date beforeDate = calendar.getTime();
		beforeDate.setHours(date.getHours());
		beforeDate.setMinutes(date.getMinutes());
		beforeDate.setSeconds(date.getSeconds());
		return beforeDate;
	}
	
	public static Date getDatetime(int howManyDays) {
		return getDatetime(howManyDays, new Date());
	}
	
	public static Date getDatetime(String dateString, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static String formatDatetime(int howManyDays, Date date, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, howManyDays);
		Date beforeDate = calendar.getTime();
		beforeDate.setHours(date.getHours());
		beforeDate.setMinutes(date.getMinutes());
		beforeDate.setSeconds(date.getSeconds());
		return formatDateTime(beforeDate, format);
	}
	
	public static String formatDateTime(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		String dateString = df.format(date);
		return dateString;
	}
}
