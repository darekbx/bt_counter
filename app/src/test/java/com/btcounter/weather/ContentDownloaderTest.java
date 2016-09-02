package com.btcounter.weather;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by daba on 2016-09-02.
 */

public class ContentDownloaderTest {

    private CountDownLatch lock = new CountDownLatch(1);
    private String receivedContent;

    @Test
    public void download_string() {
        ContentDownloader contentDownloader = new ContentDownloader();

        String content = contentDownloader.downloadString(WeatherConditionsController.IF_ADDRESS);

        assertNotNull(content);
        assertTrue(content.length() > 100);
        assertTrue(content.contains("Last conditions") || content.contains("Ostatni pomiar"));
    }

    @Test
    public void download_observable() throws InterruptedException {
        ContentDownloader contentDownloader = new ContentDownloader();

        contentDownloader
                .downloadObservable(WeatherConditionsController.IF_ADDRESS)
                .subscribe(s -> {
                    receivedContent = s;
                    lock.countDown();
                });

        lock.await();

        assertNotNull(receivedContent);
        assertTrue(receivedContent.length() > 10);
    }
}
