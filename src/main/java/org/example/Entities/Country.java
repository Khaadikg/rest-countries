package org.example.Entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
public class Country {
    private String name;
    private String official;
    private String capital;
    private Currency currency;
    private Map<String, String> languages;
    private Set<String> borders;
    private Long population;
    private String region;
    private List<String> timezones;
}
