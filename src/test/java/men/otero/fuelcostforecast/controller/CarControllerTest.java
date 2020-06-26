package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.dto.CarDTO;
import men.otero.fuelcostforecast.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    private void setUp() {
        carRepository.deleteAll();
    }

    @Test
    public void createAValidCar() {
        CarDTO validCarDTO = buildValidCarDTO();

        ResponseEntity<CarDTO> responseEntity = restTemplate.postForEntity("/cars", validCarDTO, CarDTO.class);

        CarDTO entityBody = responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(entityBody.getUuid());
        entityBody.setUuid(null);
        assertEquals(validCarDTO, entityBody);
    }

    @Test
    public void createACarWithEmptyName() {
        CarDTO car = buildValidCarDTO();
        car.setName("");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Name Can Not Be Null or Empty"));
    }

    @Test
    public void createACarWithEmptyModel() {
        CarDTO car = buildValidCarDTO();
        car.setModel("");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Model Can Not Be Null or Empty"));
    }

    @Test
    public void createACarWithAFutureManufacturingDate() {
        CarDTO car = buildValidCarDTO();
        car.setManufacturingDate(LocalDate.now().plusYears(1));

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Manufacturing Date Can Not Be In The Future"));
    }

    @Test
    public void createACarWithANullManufacturingDate() {
        CarDTO car = buildValidCarDTO();
        car.setManufacturingDate(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Manufacturing Date Can Not Be Null"));
    }

    @Test
    public void createACarWithANullCityFuelEconomy() {
        CarDTO car = buildValidCarDTO();
        car.setCityFuelEconomy(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("City Fuel Economy Can Not Be Null"));
    }

    @Test
    public void createACarWithACityFuelEconomyLessThanMinimum() {
        CarDTO car = buildValidCarDTO();
        car.setCityFuelEconomy(0.01);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("City Fuel Economy Can Not Be Less Than 0.1"));
    }

    @Test
    public void createACarWithANullHighwayFuelEconomy() {
        CarDTO car = buildValidCarDTO();
        car.setHighwayFuelEconomy(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Highway Fuel Economy Can Not Be Null"));
    }

    @Test
    public void createACarWithAHighwayFuelEconomyLessThanMinimum() {
        CarDTO car = buildValidCarDTO();
        car.setHighwayFuelEconomy(0.01);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/cars", car, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Highway Fuel Economy Can Not Be Less Than 0.1"));
    }

    @Test
    public void getAllCars() {
        restTemplate.postForEntity("/cars", buildValidCarDTO(), CarDTO.class).getBody();
        restTemplate.postForEntity("/cars", buildValidCarDTO(), CarDTO.class).getBody();
        restTemplate.postForEntity("/cars", buildValidCarDTO(), CarDTO.class).getBody();
        restTemplate.postForEntity("/cars", buildValidCarDTO(), CarDTO.class).getBody();

        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/cars/", List.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, responseEntity.getBody().size());
    }

    @Test
    public void getAValidCar() {
        CarDTO car = buildValidCarDTO();
        ResponseEntity<CarDTO> createResponseEntity = restTemplate.postForEntity("/cars", car, CarDTO.class);
        UUID createdCarUUID = createResponseEntity.getBody().getUuid();

        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/cars/" + createdCarUUID, CarDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createResponseEntity.getBody(), responseEntity.getBody());
    }

    @Test
    public void getAnInvalidCar() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/cars/" + UUID.randomUUID(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Car Exists With This UUID!"));
    }

    @Test
    public void updateAValidCar() {
        CarDTO car = buildValidCarDTO();
        ResponseEntity<CarDTO> createResponseEntity = restTemplate.postForEntity("/cars", car, CarDTO.class);
        CarDTO createdCar = createResponseEntity.getBody();
        UUID createdCarUUID = createdCar.getUuid();
        CarDTO updateCar = buildValidCarDTO();
        updateCar.setUuid(createdCarUUID);
        updateCar.setName("new Name");
        restTemplate.put("/cars/" + createdCarUUID, updateCar);

        ResponseEntity<CarDTO> responseEntity = restTemplate.getForEntity("/cars/" + createdCarUUID, CarDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updateCar, responseEntity.getBody());
    }

    @Test
    public void deleteACar() {
        CarDTO car = buildValidCarDTO();
        ResponseEntity<CarDTO> createResponseEntity = restTemplate.postForEntity("/cars", car, CarDTO.class);
        UUID createdCarUUID = createResponseEntity.getBody().getUuid();

        restTemplate.delete("/cars/" + createdCarUUID);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/cars/" + createdCarUUID, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Car Exists With This UUID!"));
    }

    @Test
    public void deleteAnInvalidCar() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/cars/" + UUID.randomUUID(), HttpMethod.DELETE, null, String.class);

        restTemplate.delete("/cars/" + UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Car Exists With This UUID!"));
    }


    private CarDTO buildValidCarDTO() {
        return CarDTO.builder()
                .name("Seller Representative 1")
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(11.56)
                .highwayFuelEconomy(13.1)
                .build();
    }

}