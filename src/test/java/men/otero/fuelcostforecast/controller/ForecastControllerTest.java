package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import men.otero.fuelcostforecast.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ForecastControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    private void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    public void getCostForecast() {
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 1", 10.3, 11.1), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 2", 10.1, 10.9), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 3", 13.1, 14.9), VehicleDTO.class);

        Map<String, ? extends Number> urlParamsMap = Map.of("fuelPrice", 3.4, "kiloMetersOnCity", 80.4, "kiloMetersOnHighway", 120);
        ResponseEntity<List> listResponseEntity = restTemplate.getForEntity("/forecasts/cost?fuelPrice={fuelPrice}&kiloMetersOnCity={kiloMetersOnCity}&kiloMetersOnHighway={kiloMetersOnHighway}", List.class, urlParamsMap);
        List<LinkedHashMap> responseList = listResponseEntity.getBody();

        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertEquals(3, responseList.size());
        assertEquals("vehicle 3", responseList.get(0).get("name"));
        assertEquals("Ford", responseList.get(0).get("brand"));
        assertEquals("Fiesta 1.0", responseList.get(0).get("model"));
        assertEquals(LocalDate.now().getYear(), responseList.get(0).get("manufacturingYear"));

    }

    @Test
    public void getEmptyCostForecast() {
        Map<String, ? extends Number> urlParamsMap = Map.of("fuelPrice", 3.4, "kiloMetersOnCity", 80.4, "kiloMetersOnHighway", 120);
        ResponseEntity<List> listResponseEntity = restTemplate.getForEntity("/forecasts/cost?fuelPrice={fuelPrice}&kiloMetersOnCity={kiloMetersOnCity}&kiloMetersOnHighway={kiloMetersOnHighway}", List.class, urlParamsMap);

        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertEquals(0, listResponseEntity.getBody().size());
    }

    @Test
    public void getCostForecastWithNoParam() {
        ResponseEntity<String> listResponseEntity = restTemplate.getForEntity("/forecasts/cost", String.class);

        assertEquals(HttpStatus.BAD_REQUEST, listResponseEntity.getStatusCode());
    }

    @Test
    public void testOrderInCostForecast() {
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 1", 10.3, 11.1), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 2", 10.1, 10.9), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 3", 13.1, 14.9), VehicleDTO.class);


        Map<String, ? extends Number> urlParamsMap = Map.of("fuelPrice", 3.4, "kiloMetersOnCity", 80.4, "kiloMetersOnHighway", 120);
        ResponseEntity<List> listResponseEntity = restTemplate.getForEntity("/forecasts/cost?fuelPrice={fuelPrice}&kiloMetersOnCity={kiloMetersOnCity}&kiloMetersOnHighway={kiloMetersOnHighway}", List.class, urlParamsMap);

        List<LinkedHashMap> responseList = listResponseEntity.getBody();
        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertEquals("vehicle 3", responseList.get(0).get("name"));
        assertEquals("vehicle 1", responseList.get(1).get("name"));
        assertEquals("vehicle 2", responseList.get(2).get("name"));
    }

    @Test
    public void testSpentFuelInCostForecast() {
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 1", 10.3, 11.1), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 2", 10.1, 10.9), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 3", 13.1, 14.9), VehicleDTO.class);


        Map<String, ? extends Number> urlParamsMap = Map.of("fuelPrice", 3.4, "kiloMetersOnCity", 80.4, "kiloMetersOnHighway", 120);
        ResponseEntity<List> listResponseEntity = restTemplate.getForEntity("/forecasts/cost?fuelPrice={fuelPrice}&kiloMetersOnCity={kiloMetersOnCity}&kiloMetersOnHighway={kiloMetersOnHighway}", List.class, urlParamsMap);

        List<LinkedHashMap> responseList = listResponseEntity.getBody();
        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertEquals(14.191095855320457, responseList.get(0).get("spentFuel"));
        assertEquals(18.616636053529255, responseList.get(1).get("spentFuel"));
        assertEquals(18.969570351530567, responseList.get(2).get("spentFuel"));
    }

    @Test
    public void testTotalFuelCostInCostForecast() {
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 1", 10.3, 11.1), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 2", 10.1, 10.9), VehicleDTO.class);
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO("vehicle 3", 13.1, 14.9), VehicleDTO.class);


        Map<String, ? extends Number> urlParamsMap = Map.of("fuelPrice", 3.4, "kiloMetersOnCity", 80.4, "kiloMetersOnHighway", 120);
        ResponseEntity<List> listResponseEntity = restTemplate.getForEntity("/forecasts/cost?fuelPrice={fuelPrice}&kiloMetersOnCity={kiloMetersOnCity}&kiloMetersOnHighway={kiloMetersOnHighway}", List.class, urlParamsMap);

        List<LinkedHashMap> responseList = listResponseEntity.getBody();
        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
        assertEquals(14.191095855320457 * 3.4, responseList.get(0).get("totalFuelCost"));
        assertEquals(18.616636053529255 * 3.4, responseList.get(1).get("totalFuelCost"));
        assertEquals(18.969570351530567 * 3.4, responseList.get(2).get("totalFuelCost"));
    }

    private VehicleDTO buildValidVehicleDTO(final String name, final Double cityFuelEconomy, final Double highwayFuelEconomy) {
        return VehicleDTO.builder()
                .name(name)
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(cityFuelEconomy)
                .highwayFuelEconomy(highwayFuelEconomy)
                .build();
    }

}