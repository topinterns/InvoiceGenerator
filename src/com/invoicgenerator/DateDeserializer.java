package com.invoicgenerator;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class DateDeserializer implements JsonDeserializer<Date> {

	private TimeZone lTimeZone = null;

	public DateDeserializer() {};

	public DateDeserializer(TimeZone pTimeZone) {
		lTimeZone = pTimeZone;
	}

	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String lDateString = json.getAsJsonPrimitive().getAsString();
		StackTraceElement[] stackTrace = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy k:mm:ss z"); // Wed, 19 Jan 2005 09:30:00 GMT
		Date myNewDate = null;
		if (lTimeZone != null) {
			dateFormat.setTimeZone(this.lTimeZone);
		}

		if (lDateString != null && lDateString.length() > 0) {
			try {
				myNewDate = dateFormat.parse(lDateString);
			} catch (ParseException e) {
				try {
					dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					if (lTimeZone != null) {
						dateFormat.setTimeZone(this.lTimeZone);
					}
					myNewDate = dateFormat.parse(lDateString);
				} catch (Exception excep) {
					stackTrace = excep.getStackTrace();
					try {
						Calendar lCal = Calendar.getInstance();
						lCal.setTimeInMillis(new Long(lDateString));
						myNewDate = new Date(lCal.getTimeInMillis());
					} catch (Exception excep1) {
						stackTrace = excep1.getStackTrace();
					}
				}
			}
		}
		return myNewDate;
	}
}