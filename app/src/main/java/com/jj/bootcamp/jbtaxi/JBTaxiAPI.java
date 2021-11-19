package com.jj.bootcamp.jbtaxi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.jj.bootcamp.jbtaxi.service.JBTaxiService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JBTaxiAPI {
    private static JBTaxiAPI instance = null;
    private static JBTaxiService service;
    private static final String baseUrl = "http://121.154.88.169:8080/";

    private JBTaxiAPI() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context)
                        -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context)
                        -> LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, typeOfT, context)
                        -> LocalTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("HH:mm:ss")))
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, typeOfT, context)
                        -> new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(localDateTime)))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, typeOfT, context)
                        -> new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate)))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (localTime, typeOfT, context)
                        -> new JsonPrimitive(DateTimeFormatter.ofPattern("HH:mm:ss").format(localTime)))
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.154.88.169:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        service = retrofit.create(JBTaxiService.class);
    }

    public static JBTaxiAPI getInstance() {
        if (instance == null) {
            instance = new JBTaxiAPI();
        }
        return instance;
    }

    public JBTaxiService getService() {
        return service;
    }
}
