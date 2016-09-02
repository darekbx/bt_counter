package com.btcounter.weather;

import com.btcounter.weather.WeatherConditionsController;
import com.btcounter.model.WeatherConditions;
import com.btcounter.weather.ContentDownloader;
import com.btcounter.weather.ContentParser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-02.
 */
public class ContentParserTest {

    @Test
    public void parse() throws Exception {
        ContentDownloader contentDownloader = new ContentDownloader();
        String content = contentDownloader.downloadString(WeatherConditionsController.IF_ADDRESS);

        ContentParser contentParser = new ContentParser();
        WeatherConditions weatherConditions = contentParser.parse(content);

        assertNotNull(weatherConditions);
        assertTrue(weatherConditions.temperature > -40);
        assertTrue(weatherConditions.windChill > -40);
        assertTrue(weatherConditions.pressure > 800);
        assertTrue(weatherConditions.humidity > 10);
    }

    @Test
    public void weatherConditions_from_chunks() throws Exception {
        ContentParser contentParser = new ContentParser();
        List<String> chunks = new ArrayList<>(4);
        chunks.add("21,3");
        chunks.add("21,8");
        chunks.add("1005,2");
        chunks.add("63");

        WeatherConditions weatherConditions = contentParser.weatherConditionsFromChunks(chunks);

        assertEquals(weatherConditions.temperature, 21.3d, 0d);
        assertEquals(weatherConditions.windChill, 21.8d, 0d);
        assertEquals(weatherConditions.pressure, 1005.2d, 0d);
        assertEquals(weatherConditions.humidity, 63);
    }

    @Test
    public void parse_string() throws Exception {
        ContentParser contentParser = new ContentParser();

        assertEquals(contentParser.parseDouble("21,3"), 21.3d, 0d);
        assertEquals(contentParser.parseDouble("1.3"), 1.3d, 0d);
        assertEquals(contentParser.parseDouble("-17.0"), -17.0d, 0d);
    }
}