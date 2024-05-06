package br.com.guzz.swplanetapi;

import static br.com.guzz.swplanetapi.common.PlanetConstants.ALDERAAN;
import static br.com.guzz.swplanetapi.common.PlanetConstants.PLANET;
import static br.com.guzz.swplanetapi.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.guzz.swplanetapi.domain.Planet;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class PlanetIT {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createPlanet_ReturnsCreated(){
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
        assertThat(sut.getBody().getId()).isNotNull();
    }

    @Test
    public void getPlanet_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/1", Planet.class);
        
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet(){
        ResponseEntity<Planet> sut = restTemplate.getForEntity("/planets/name/Alderaan", Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(ALDERAAN);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets", Planet[].class);
        
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isNotEmpty();
        assertThat(sut.getBody()).hasSize(3);
    }

    @Test
    public void listPlanets_ByClimate_ReturnPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?climate="+TATOOINE.getClimate(), Planet[].class);
    
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void listPlanets_ByTerrain_ReturnsPlanets(){
        ResponseEntity<Planet[]> sut = restTemplate.getForEntity("/planets?terrain="+TATOOINE.getTerrain(), Planet[].class);
    
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void removePlanet_ReturnsNoContent(){
        ResponseEntity<Void> sut = restTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);
    
        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
