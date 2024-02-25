package com.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.weather.util.Json;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Weather {
    private static String API_KEY;
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/find?q=%s&appid=%s";



    public static void main(String[] args) {

        try {
            loadAPIKey("src/main/resources/api/conf.json");
        } catch (IOException e) {
            System.out.println("Не удалось прочитать API Key");
            return;
        }


        List<String> citiesNames = new ArrayList<>(List.of(new String[]{"Astrakhan", "Vorkuta", "Kazan", "Omsk"
                ,"Penza", "Perm", "Sochi", "Tomsk", "Chelyabinsk", "Moscow","Shelekhov","Irkutsk"}));

        List<City> cities = new ArrayList<>();
        for (String cityName:citiesNames){
            cities.add(findCityWithWeather(cityName));
        }

        Collections.sort(cities, Comparator.reverseOrder());

        System.out.println(cities);
        for (City city:cities){
            System.out.println(city.getName()+": "+city.getId()+" температура: "+ Math.round(kelvinToCelsius(city.getWeatherData().getTemp())));
        }

    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
    private static City findCityWithWeather(String cityName){
        String url = String.format(API_URL, cityName, API_KEY);
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            City city = new City();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonNode jsonNode = Json.parse(response.toString()).get("list").get(0);

                city = parseCity(jsonNode);
                city.setWeatherData(parseWeatherData(jsonNode));

            } else {
                System.out.println("Ошибка при получении данных о погоде: " + responseCode);

            }

            connection.disconnect();
            return city;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static City parseCity(JsonNode jsonNode) throws JsonProcessingException {
        City city = Json.fromJson(jsonNode,City.class);
        city.setLat(Json.fromJson(jsonNode.get("coord").get("lat"),Double.class));
        city.setLon(Json.fromJson(jsonNode.get("coord").get("lon"),Double.class));
        return city;
    }

    private static WeatherData parseWeatherData(JsonNode jsonNode) throws JsonProcessingException {
        WeatherData weatherData = Json.fromJson(jsonNode.get("main"),WeatherData.class);
        weatherData.setWindSpeed(Json.fromJson(jsonNode.get("wind").get("speed"),Double.class));
        weatherData.setWindDeg(Json.fromJson(jsonNode.get("wind").get("deg"),Double.class));
        weatherData.setRain(jsonNode.get("rain").toString());
        weatherData.setSnow(jsonNode.get("snow").toString());
        weatherData.setClouds(jsonNode.get("clouds").toString());
        return weatherData;
    }

    private static void loadAPIKey(String filePath) throws IOException {


        FileReader fileReader = new FileReader(filePath);
        StringBuffer sb = new StringBuffer();

        int i;
        while ((i = fileReader.read()) != -1){
            sb.append((char) i);
        }
        JsonNode conf = null;
        conf = Json.parse(sb.toString());
        API_KEY = conf.get("API_KEY").toString().replaceAll("\"", "");

    }
}
