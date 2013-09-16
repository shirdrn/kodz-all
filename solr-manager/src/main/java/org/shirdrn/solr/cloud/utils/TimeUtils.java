package org.shirdrn.solr.cloud.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	public static Date getDateBefore(int unit, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(unit, amount);
		return calendar.getTime();
	}
	
	public static String format(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String format(long date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date(date));
	}
}
