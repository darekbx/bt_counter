package com.btcounter.weather;

import com.btcounter.model.WeatherConditions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by daba on 2016-09-02.
 */

public class ContentParser {

    public WeatherConditions parse(String content) {
        final String sectionBegin = "font-size:14pt";
        final String sectionEnd = "font-size:12pt";
        final String valueStart = "font-size:24pt";
        final int offset = 22;

        int sectionBeginIndex = content.indexOf(sectionBegin);
        int sectionEndIndex = content.indexOf(sectionEnd, sectionBeginIndex);
        String section = content.substring(sectionBeginIndex, sectionEndIndex);
        int start = 0;

        List<String> chunks = new ArrayList<>(8);
        while ((start = section.indexOf(valueStart, start)) > 0) {
            int end = section.indexOf(' ',  start);
            String value = section.substring(start + offset, end);
            chunks.add(value);
            start += (end - start);
        }

        return weatherConditionsFromChunks(chunks);
    }

    public WeatherConditions weatherConditionsFromChunks(List<String> chunks) {
        WeatherConditions weatherConditions = new WeatherConditions();
        weatherConditions.temperature = parseDouble(chunks.get(0));
        weatherConditions.windChill = parseDouble(chunks.get(1));
        weatherConditions.pressure = parseDouble(chunks.get(2));
        weatherConditions.humidity = Integer.parseInt(chunks.get(3));
        return weatherConditions;
    }

    public double parseDouble(String value) {
        if (value.indexOf(',') > 0) {
            value = value.replace(',', '.');
        }
        return Double.parseDouble(value);
    }
}