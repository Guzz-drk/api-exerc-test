package br.com.guzz.swplanetapi.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import static br.com.guzz.swplanetapi.common.PlanetConstants.PLANET;
import static br.com.guzz.swplanetapi.common.PlanetConstants.INVALID_PLANET;
import static br.com.guzz.swplanetapi.common.PlanetConstants.EXISTING_PLANET;
import static br.com.guzz.swplanetapi.common.PlanetConstants.PLANETS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
    
    @InjectMocks
    private PlanetService planetService;

    @Mock
    private PlanetRepository planetRepository;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        Planet sut = planetService.create(PLANET);

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException(){
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy( () -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_ByExistingId_ReturnsPlanet(){
        when(planetRepository.findById(1L)).thenReturn(Optional.of(EXISTING_PLANET));
        
        Planet sut = planetService.get(1L).get();
        
        assertThat(sut).isEqualTo(EXISTING_PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingId_ReturnsEmpty(){
        when(planetRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.get(2L);
        
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_ByExistingName_ReturnsPlanet(){
        when(planetRepository.findByName("planet")).thenReturn(Optional.of(EXISTING_PLANET));

        Optional<Planet> sut = planetService.getByName("planet");

        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(EXISTING_PLANET);
    }

    @Test
    public void getPlanet_ByUnexistingName_ReturnsEmpty(){
        when(planetRepository.findByName("planet")).thenReturn(Optional.empty());

        Optional<Planet> sut = planetService.getByName("planet");

        assertThat(sut).isEmpty();
        assertThat(sut).isNotEqualTo(EXISTING_PLANET);
    }

    @Test
    public void listPlanets_ReturnsAllPlanets(){
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getTerrain(), PLANET.getClimate()));
        when(planetRepository.findAll(query)).thenReturn(PLANETS);

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isNotEmpty();
        assertThat(sut).hasSize(2);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets(){
        when(planetRepository.findAll(any())).thenReturn(List.of());

        List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();
        assertThat(sut).hasSize(0);
    }

    @Test
    public void removePlanet_WithExistingId_DoesNotThrowAnyException(){
        assertThatCode(() -> planetService.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistingId_ThrowsException(){
        doThrow(new RuntimeException()).when(planetRepository).deleteById(1L);

        assertThatThrownBy(() -> planetService.remove(1L)).isInstanceOf(RuntimeException.class);
    }
}
