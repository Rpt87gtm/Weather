package com.weather;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MyThread extends Thread{
    private String url;
    private Weather weather;
    public MyThread(String url) {
        this.url = url;
    }
    @Override
    public void run() {
        try {
            URL weather_url = new URL(url);
            InputStream stream = (InputStream) weather_url.getContent();
            Gson gson = new Gson();
            weather = gson.fromJson(new InputStreamReader(stream), Weather.class);
        } catch (IOException e) {}
    }

    public Weather getWeather() {
        return weather;
    }
}
