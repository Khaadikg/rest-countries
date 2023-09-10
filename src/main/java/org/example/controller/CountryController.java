package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.Entities.Country;
import org.example.services.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("rest-countries.com/")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/all")
    public Set<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/region/{region}")
    public Set<Country> getCountriesByRegion(@PathVariable String region) {
        return countryService.getCountriesByRegion(region);
    }

    @GetMapping("/name/{name}")
    public Country getCountryByName(@PathVariable String name) {
        return countryService.getCountryByName(name);
    }

    @GetMapping("/capital/{capital}")
    public Country getCountryByCapital(@PathVariable String capital) {
        return countryService.getCountryByCapital(capital);
    }

    @GetMapping("/currency/{currency}")
    public Country getCountryByCurrency(@PathVariable String currency) {
        return countryService.getCountryByCurrency(currency);
    }

}
