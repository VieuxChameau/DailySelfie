package org.vieuxchameau.DailySelfie;

import android.util.Log;
import org.vieuxchameau.DailySelfie.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Selfie implements Comparable<Selfie> {
	private static final String DAY_FORMAT = "MMMM dd, yyyy";
	private static final String HOUR_FORMAT = "HH:mm";
	private static final String TAG = "SELFIE";
	private final String bitmapPath;
	private final Date date;
	private final String day;
	private final String hour;

	public Selfie(final String bitmapPath) {
		this.bitmapPath = bitmapPath;
		this.date = getDate(bitmapPath);
		this.day = formatDay();
		this.hour = formatHour();
	}

	private static Date getDate(final String bitmapPath) {
		final String fileName = Utils.getFileNameWithoutExtension(bitmapPath);
		final String fileDate = fileName.substring(0, fileName.lastIndexOf('_'));
		try {
			return new SimpleDateFormat(MainActivity.SELFIE_DATE_FORMAT).parse(fileDate);
		} catch (ParseException e) {
			Log.e(TAG, "Erreur parsing date " + fileDate, e);
			return new Date();
		}
	}

	private String formatDay() {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DAY_FORMAT);
		simpleDateFormat.setLenient(false);
		return simpleDateFormat.format(date);
	}

	private String formatHour() {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HOUR_FORMAT);
		simpleDateFormat.setLenient(false);
		return simpleDateFormat.format(date);
	}

	public String getDay() {
		return day;
	}

	public String getHour() {
		return hour;
	}

	public String getBitmapPath() {
		return bitmapPath;
	}

	@Override
	public int compareTo(final Selfie selfie) {
		if (selfie == null) {
			return 0;
		}

		return selfie.date.compareTo(date);
	}
}
