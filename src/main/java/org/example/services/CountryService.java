package org.example.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.Entities.Country;
import org.example.Entities.Currency;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private static final Set<Country> countries = init();
    public Set<Country> getAllCountries() {
        return countries;
    }

    public Set<Country> getCountriesByRegion(String region) {
        return countries.stream()
                .filter(x -> x.getRegion().equalsIgnoreCase(region))
                .collect(Collectors.toSet());
    }

    public Country getCountryByName(String name) {
        return countries.stream()
                .filter(x -> x.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public Country getCountryByCapital(String capital) {
        return countries.stream()
                .filter(x -> x.getCapital().equalsIgnoreCase(capital))
                .findFirst().orElse(null);
    }

    public Country getCountryByCurrency(String currency) {
        return countries.stream()
                .filter(x -> x.getCurrency().getName().equalsIgnoreCase(currency)
                        || x.getCurrency().getSymbol().equalsIgnoreCase(currency)
                        || x.getCurrency().getCode().equalsIgnoreCase(currency)
                )
                .findFirst().orElse(null);
    }

    private static Set<Country> init() {
        Set<Country> countries = new HashSet<>();
        String jsonString;
        try(InputStream in=Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("rest-countries.json"))
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readValue(in, JsonNode.class);
            jsonString = mapper.writeValueAsString(jsonNode);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String capital = (!object.getJSONArray("capital").isEmpty())
                    ? object.getJSONArray("capital").get(0).toString() : "NoCapital";

            countries.add(
                    Country.builder()
                            .name(object.getJSONObject("name").getString("common"))
                            .capital(capital)
                            .official(object.getJSONObject("name").getString("official"))
                            .population(object.getLong("population"))
                            .currency(getCurrency(object))
                            .languages(getLanguages(object))
                            .region(object.getString("region"))
                            .timezones(getTimeZones(object))
                            .borders(getBorders(object))
                            .build()
            );
        }
        return countries;
    }

    public static Currency getCurrency(JSONObject mainObject) {
        boolean isArray = false;
        try {
            mainObject.getJSONArray("currencies");
        } catch (JSONException e) {
            isArray = true;
        }
        if (isArray) {
            JSONObject object = mainObject.getJSONObject("currencies");
            String code = object.keys().next();
            String name = object.getJSONObject(code).getString("name");
            String symbol = object.getJSONObject(code).getString("symbol");
            return Currency.builder()
                    .name(name)
                    .symbol(symbol)
                    .code(code)
                    .build();
        }
        return new Currency("NoCODE", "NoNAME", "NoSYMBOL");
    }

    public static Map<String, String> getLanguages(JSONObject mainObject) {
        Map<String, String> languages = new HashMap<>();
        JSONObject object = mainObject.getJSONObject("languages");
        Iterator<String> keys= object.keys();
        while (keys.hasNext()) {
            String keyValue = (String)keys.next();
            String valueString = object.getString(keyValue);
            languages.put(keyValue, valueString);
        }
        return languages;
    }

    public static List<String> getTimeZones(JSONObject mainObject) {
        List<String> timezones = new ArrayList<>();
        JSONArray jsonArray = mainObject.getJSONArray("timezones");
        for (int i = 0; i < jsonArray.length(); i++) {
            timezones.add(jsonArray.getString(i));
        }
        return timezones;
    }

    public static Set<String> getBorders(JSONObject mainObject) {
        Set<String> borders = new HashSet<>();
        JSONArray jsonArray = mainObject.getJSONArray("borders");
        for (int i = 0; i < jsonArray.length(); i++) {
            borders.add(jsonArray.getString(i));
        }
        return borders;
    }

}
