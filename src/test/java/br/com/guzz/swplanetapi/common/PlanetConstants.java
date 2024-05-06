package br.com.guzz.swplanetapi.common;

import java.util.ArrayList;
import java.util.List;

import br.com.guzz.swplanetapi.domain.Planet;

public class PlanetConstants {
    
    public static final Planet PLANET = Planet.builder()
    .name("name")
    .climate("climate")
    .terrain("terrain")
    .build();

    public static final Planet INVALID_PLANET = Planet.builder()
    .name("")
    .climate("")
    .terrain("")
    .build();

    public static final Planet EXISTING_PLANET = Planet.builder()
    .id(1L)
    .name("name")
    .climate("climate")
    .terrain("terrain")
    .build();

    public static final List<Planet> PLANETS = List.of(PLANET, EXISTING_PLANET);

    public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "Alderaan", "temperate", "grasslands, mountains");
    public static final Planet YAVINIV = new Planet(3L, "Yavin IV", "temperate, tropical", "jungle, rainforest");
    
    public static final List<Planet> REAL_PLANETS = new ArrayList<>(){
        {
            add(ALDERAAN);
            add(TATOOINE);
            add(YAVINIV);
        }
    };
}
