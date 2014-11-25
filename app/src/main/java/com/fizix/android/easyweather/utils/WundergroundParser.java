package com.fizix.android.easyweather.utils;

import android.content.ContentValues;
import android.util.JsonReader;
import android.util.Log;

import com.fizix.android.easyweather.data.Contract;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class WundergroundParser {

    private static final String LOG_TAG = WundergroundParser.class.getSimpleName();

    public static List<ContentValues> parseForecast(InputStream inStream, long locationId) {
        List<ContentValues> result = null;

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inStream, "UTF-8"));

            reader.beginObject();
            while (reader.hasNext()) {
                if (reader.nextName().equals("forecast")) {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        if (reader.nextName().equals("simpleforecast")) {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                if (reader.nextName().equals("forecastday")) {
                                    result = readForecastDayArray(reader, locationId);
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "Unsupported encoding for json input stream.", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not read json from input stream.", e);
            return null;
        }

        return result;
    }

    private static List<ContentValues> readForecastDayArray(JsonReader reader, long locationId) throws IOException {
        ArrayList<ContentValues> result = new ArrayList<ContentValues>();

        reader.beginArray();
        while (reader.hasNext()) {
            ContentValues values = readForecastDay(reader);
            values.put(Contract.DayEntry.COL_LOCATION_ID, locationId);
            if (values != null)
                result.add(values);
        }
        reader.endArray();

        return result;
    }

    private static ContentValues readForecastDay(JsonReader reader) throws IOException {
        ContentValues values = new ContentValues();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("date")) {
                values.put(Contract.DayEntry.COL_DATE, readerForecastDate(reader));
            } else if (name.equals("high")) {
                values.put(Contract.DayEntry.COL_TEMP_HIGH, readForecastTemp(reader));
            } else if (name.equals("low")) {
                values.put(Contract.DayEntry.COL_TEMP_LOW, readForecastTemp(reader));
            } else if (name.equals("icon")) {
                values.put(Contract.DayEntry.COL_ICON, reader.nextString());
            } else if (name.equals("qpf_allday")) {
                values.put(Contract.DayEntry.COL_QPF_ALL_DAY, readForecastQpf(reader));
            } else if (name.equals("qpf_day")) {
                values.put(Contract.DayEntry.COL_QPF_DAY, readForecastQpf(reader));
            } else if (name.equals("qpf_night")) {
                values.put(Contract.DayEntry.COL_QPF_NIGHT, readForecastQpf(reader));
            } else if (name.equals("snow_allday")) {
                values.put(Contract.DayEntry.COL_SNOW_ALL_DAY, readForecastSnow(reader));
            } else if (name.equals("snow_day")) {
                values.put(Contract.DayEntry.COL_SNOW_DAY, readForecastSnow(reader));
            } else if (name.equals("snow_night")) {
                values.put(Contract.DayEntry.COL_SNOW_NIGHT, readForecastSnow(reader));
            } else if (name.equals("maxwind")) {
                WindData data = readForecastWindData(reader);
                if (data != null) {
                    values.put(Contract.DayEntry.COL_MAX_WIND_SPEED, data.speed);
                    values.put(Contract.DayEntry.COL_MAX_WIND_DEGREES, data.degrees);
                }
            } else if (name.equals("avewind")) {
                WindData data = readForecastWindData(reader);
                if (data != null) {
                    values.put(Contract.DayEntry.COL_AVG_WIND_SPEED, data.speed);
                    values.put(Contract.DayEntry.COL_AVG_WIND_DEGREES, data.degrees);
                }
            } else if (name.equals("avehumidity")) {
                values.put(Contract.DayEntry.COL_AVG_HUMIDITY, reader.nextInt());
            } else if (name.equals("maxhumidity")) {
                values.put(Contract.DayEntry.COL_MAX_HUMIDITY, reader.nextInt());
            } else if (name.equals("minhumidity")) {
                values.put(Contract.DayEntry.COL_MIN_HUMIDITY, reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return values;
    }

    private static String readerForecastDate(JsonReader reader) throws IOException {
        int year = 0;
        int month = 0;
        int day = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("year")) {
                year = reader.nextInt();
            } else if (name.equals("month")) {
                month = reader.nextInt();
            } else if (name.equals("day")) {
                day = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if (year == 0 || month == 0 || day == 0) {
            throw new IOException("Could not retreive full date from json object.");
        }

        return Contract.DayEntry.createDateString(year, month, day);
    }

    private static int readForecastTemp(JsonReader reader) throws IOException {
        int value = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("celsius")) {
                value = Integer.parseInt(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return value;
    }

    private static int readForecastQpf(JsonReader reader) throws IOException {
        int value = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("mm")) {
                try {
                    value = reader.nextInt();
                } catch (java.lang.IllegalStateException e) {
                    Log.w(LOG_TAG, "Could not read qpf value.");
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return value;
    }

    private static double readForecastSnow(JsonReader reader) throws IOException {
        double value = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("mm")) {
                value = Double.parseDouble(reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return value;
    }

    private static class WindData {
        public int speed;
        public int degrees;
    }

    public static WindData readForecastWindData(JsonReader reader) throws IOException {
        WindData result = new WindData();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("kph")) {
                result.speed = reader.nextInt();
            } else if (name.equals("degrees")) {
                result.degrees = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return result;
    }

}
