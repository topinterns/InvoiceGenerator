package com.invoicgenerator;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson getGson() {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapter(Date.class, new DateDeserializer());
		Gson gson = gsonb.create();
		return gson;
	}
}
