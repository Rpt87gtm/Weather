package com.weather;

public class City implements Comparable<City>{
    private int id;
    private String name;
    private double lat;
    private double lon;

    private WeatherData weatherData;

    public City() {
    }

    public City(int id, String name, double lat, double lon, WeatherData weatherData) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.weatherData = weatherData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", weatherData=" + weatherData +
                '}';
    }

    @Override
    public int compareTo(City other) {
        double otherTemp =other.getWeatherData().getTemp();
        return Double.compare(weatherData.getTemp(),otherTemp);

    }
}
