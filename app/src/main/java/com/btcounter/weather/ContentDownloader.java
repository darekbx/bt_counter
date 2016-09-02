package com.btcounter.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import rx.Observable;

/**
 * Created by daba on 2016-09-02.
 */

public class ContentDownloader {

    public Observable<String> downloadObservable(String urlAddress) {
        return Observable.create((subscriber) -> {
            String contents = downloadString(urlAddress);
            if (contents != null) {
                subscriber.onNext(contents);
                subscriber.onCompleted();
            } else {
                subscriber.onError(null);
            }
        });
    }

    public String downloadString(String urlAddress) {
        BufferedReader inputStream = null;
        try {
            URL url = new URL(urlAddress);
            URLConnection connection = url.openConnection();
            connection.connect();

            inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder output = new StringBuilder(connection.getContentLength());
            String line;

            while ((line = inputStream.readLine()) != null) {
                output.append(line);
            }

            return output.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
}