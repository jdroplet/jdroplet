package jdroplet.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTime {
	private Calendar calender;

	public static final DateTime MinValue = new DateTime(0);

	public DateTime() {
		this(2000, 0, 1, 0, 0, 0);
	}

	public DateTime(Date date) {
		this.calender = Calendar.getInstance();
		this.calender.setTime(date);
	}

	public DateTime(int year, int month, int day) {
		this(year, month, day, 0, 0, 0);
	}
	
	public DateTime(long t) {
		this.calender = Calendar.getInstance();
		this.calender.setTimeInMillis(t);
	}

	public DateTime(int year, int month, int date, int hourOfDay, int minute,
			int second) {
		this.calender = Calendar.getInstance();
		this.calender.set(year, month, date, hourOfDay, minute, second);
	}

	public static DateTime now() {
		return new DateTime(new Date());
	}

	public static long diff(DateTime t1, DateTime t2) {
		return diff(t1.getDate(), t2.getDate());
	}

	public static long diff(Date t1, Date t2) {
		return (t1.getTime() - t2.getTime()) / 1000;
	}

	public static DateTime parse(String time) {
		return parse(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static boolean isCurrentDay(DateTime d1, DateTime d2) {
		return d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDay() == d2.getDay();
	}
	
	public static DateTime parse(String time, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date;
		try {
			date = format.parse(time);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("DateTime parse error"
					+ ex.getMessage());
		}
		return new DateTime(date);
	}

	public DateTime addYears(int amount) {
		return add(Calendar.YEAR, amount);
	}

	public DateTime addMonths(int amount) {
		return add(Calendar.MONTH, amount);
	}

	public DateTime addWeeks(int amount) {
		return add(Calendar.WEEK_OF_YEAR, amount);
	}

	public DateTime addDays(int amount) {
		return add(Calendar.DAY_OF_MONTH, amount);
	}

	public DateTime addHours(int amount) {
		return add(Calendar.HOUR_OF_DAY, amount);
	}

	public DateTime addMinutes(int amount) {
		return add(Calendar.MINUTE, amount);
	}

	public DateTime addSeconds(int amount) {
		return add(Calendar.SECOND, amount);
	}

	public DateTime addMilliseconds(int amount) {
		return add(Calendar.MILLISECOND, amount);
	}

	private DateTime add(int calendarField, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(this.calender.getTime());
		c.add(calendarField, amount);
		return new DateTime(c.getTime());
	}

	public Date getDate() {
		return calender.getTime();
	}

	public long getTime() {
		return getDate().getTime();
	}

	public int getYear() {
		return calender.get(Calendar.YEAR);
	}
	
	public void setYear(int value) {
		calender.set(Calendar.YEAR, value);
	}

	public int getMonth() {
		return calender.get(Calendar.MONTH);
	}
	
	public void setMonth(int value) {
		calender.set(Calendar.MONTH, value);
	}

	public int getDay() {
		return calender.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setDay(int value) {
		calender.set(Calendar.DAY_OF_MONTH, value);
	}

	public int getWeekOfMonth() {
		return calender.get(Calendar.WEEK_OF_MONTH);
	}

	public int getWeekOfYear() {
		return calender.get(Calendar.WEEK_OF_YEAR);
	}

	public int getDayOfYear() {
		return calender.get(Calendar.DAY_OF_YEAR);
	}

	public int getDayOfWeek() {
		return calender.get(Calendar.DAY_OF_WEEK);
	}

	public int getHour() {
		return calender.get(Calendar.HOUR_OF_DAY);
	}
	
	public void setHour(int value) {
		calender.set(Calendar.HOUR_OF_DAY, value);
	}

	public int getMinute() {
		return calender.get(Calendar.MINUTE);
	}
	
	public void setMinute(int value) {
		calender.set(Calendar.MINUTE, value);
	}

	public int getSecond() {
		return calender.get(Calendar.SECOND);
	}
	
	public void setSecond(int value) {
		calender.set(Calendar.SECOND, value);
	}

	public int getMillisecond() {
		return calender.get(Calendar.MILLISECOND);
	}
	
	public void setMillisecond(int value) {
		calender.set(Calendar.MILLISECOND, value);
	}

	public boolean after(DateTime other) {
		return getDate().after(other.getDate());
	}
	
	public boolean between(DateTime start, DateTime end) {
		return this.getDate().after(start.getDate()) && end.after(this);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof DateTime)) {
			return false;
		}
		DateTime other = (DateTime) obj;
		return this.getDate().equals(other.getDate());
	}

	public String toString() {
		return toString("yyyy-MM-dd HH:mm:ss");
	}

	public String toString(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(pattern);

		return format.format(calender.getTime());
	}
	
	public static Date getFirstDayOfWeek(Date date) {
		return getFirstDayOfWeek(date, Calendar.MONDAY);
	}
	
	public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
		Calendar c = Calendar.getInstance();
		
		
		c.setFirstDayOfWeek(firstDayOfWeek);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		return c.getTime();
	}

	public static Date getFirstDateOf(Date date) {
		Calendar c = Calendar.getInstance();//获取当前日期

		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);

		return c.getTime();
	}

	public static Date getLastDateOf(Date date) {
		Calendar c = Calendar.getInstance();//获取当前日期

		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);

		return c.getTime();
	}

	public static Date getMonthOf(Date date, int dep) {
		Calendar c = Calendar.getInstance();//获取当前日期

		c.setTime(date);
		c.add(Calendar.MONTH, dep);
		c.set(Calendar.DAY_OF_MONTH, 1);

		return c.getTime();
	}
	
	public static String toString(Date date) {
		return toString(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String toString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(pattern);

		return format.format(date);
	}
	
	public static long getTime(Date date) {
		return date.getTime();
	}
	
	public static Date getEndOf(Date date) {
		Calendar c = Calendar.getInstance();

		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		return c.getTime();
	}
	
	public static Date getBeginOf(Date date) {
		Calendar c = Calendar.getInstance();

		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		
		return c.getTime();
	}

}