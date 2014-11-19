package com.fizix.android.easyweather;

import android.test.InstrumentationTestCase;

import com.fizix.android.easyweather.utils.WundergroundParser;

import java.io.BufferedReader;

public class WundergroundParserTest extends InstrumentationTestCase {

    private static final String LOG_TAG = WundergroundParser.class.getSimpleName();

    public void testParseForecast() throws Throwable {
        BufferedReader reader = null;
        WundergroundParser.parseForecast(getInstrumentation().getContext().getAssets().open("query1.json"));
    }

}
