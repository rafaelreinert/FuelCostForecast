package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import men.otero.fuelcostforecast.repository.VehicleRepository;
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
class VehiclesControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    private void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    public void createAValidVehicle() {
        VehicleDTO validVehicleDTO = buildValidVehicleDTO();

        ResponseEntity<VehicleDTO> responseEntity = restTemplate.postForEntity("/vehicles", validVehicleDTO, VehicleDTO.class);

        VehicleDTO entityBody = responseEntity.getBody();
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(entityBody.getUuid());
        entityBody.setUuid(null);
        assertEquals(validVehicleDTO, entityBody);
    }

    @Test
    public void createAVehicleWithEmptyName() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setName("");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Name Can Not Be Null or Empty"));
    }

    @Test
    public void createAVehicleWithEmptyModel() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setModel("");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Model Can Not Be Null or Empty"));
    }

    @Test
    public void createAVehicleWithAFutureManufacturingDate() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setManufacturingDate(LocalDate.now().plusYears(1));

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Manufacturing Date Can Not Be In The Future"));
    }

    @Test
    public void createAVehicleWithANullManufacturingDate() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setManufacturingDate(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Manufacturing Date Can Not Be Null"));
    }

    @Test
    public void createAVehicleWithANullCityFuelEconomy() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setCityFuelEconomy(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("City Fuel Economy Can Not Be Null"));
    }

    @Test
    public void createAVehicleWithACityFuelEconomyLessThanMinimum() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setCityFuelEconomy(0.01);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("City Fuel Economy Can Not Be Less Than 0.1"));
    }

    @Test
    public void createAVehicleWithANullHighwayFuelEconomy() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setHighwayFuelEconomy(null);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Highway Fuel Economy Can Not Be Null"));
    }

    @Test
    public void createAVehicleWithAHighwayFuelEconomyLessThanMinimum() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        vehicle.setHighwayFuelEconomy(0.01);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/vehicles", vehicle, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Highway Fuel Economy Can Not Be Less Than 0.1"));
    }

    @Test
    public void getAllVehicles() {
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO(), VehicleDTO.class).getBody();
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO(), VehicleDTO.class).getBody();
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO(), VehicleDTO.class).getBody();
        restTemplate.postForEntity("/vehicles", buildValidVehicleDTO(), VehicleDTO.class).getBody();

        ResponseEntity<List> responseEntity = restTemplate.getForEntity("/vehicles/", List.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, responseEntity.getBody().size());
    }

    @Test
    public void getAValidVehicle() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        ResponseEntity<VehicleDTO> createResponseEntity = restTemplate.postForEntity("/vehicles", vehicle, VehicleDTO.class);
        UUID createdVehicleUUID = createResponseEntity.getBody().getUuid();

        ResponseEntity<VehicleDTO> responseEntity = restTemplate.getForEntity("/vehicles/" + createdVehicleUUID, VehicleDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createResponseEntity.getBody(), responseEntity.getBody());
    }

    @Test
    public void getAnInvalidVehicle() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/vehicles/" + UUID.randomUUID(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Vehicle Exists With This UUID!"));
    }

    @Test
    public void updateAValidVehicle() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        ResponseEntity<VehicleDTO> createResponseEntity = restTemplate.postForEntity("/vehicles", vehicle, VehicleDTO.class);
        VehicleDTO createdVehicle = createResponseEntity.getBody();
        UUID createdVehicleUUID = createdVehicle.getUuid();
        VehicleDTO updateVehicle = buildValidVehicleDTO();
        updateVehicle.setUuid(createdVehicleUUID);
        updateVehicle.setName("new Name");
        restTemplate.put("/vehicles/" + createdVehicleUUID, updateVehicle);

        ResponseEntity<VehicleDTO> responseEntity = restTemplate.getForEntity("/vehicles/" + createdVehicleUUID, VehicleDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updateVehicle, responseEntity.getBody());
    }

    @Test
    public void deleteAVehicle() {
        VehicleDTO vehicle = buildValidVehicleDTO();
        ResponseEntity<VehicleDTO> createResponseEntity = restTemplate.postForEntity("/vehicles", vehicle, VehicleDTO.class);
        UUID createdVehicleUUID = createResponseEntity.getBody().getUuid();

        restTemplate.delete("/vehicles/" + createdVehicleUUID);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/vehicles/" + createdVehicleUUID, String.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Vehicle Exists With This UUID!"));
    }

    @Test
    public void deleteAnInvalidVehicle() {
        ResponseEntity<String> responseEntity = restTemplate.exchange("/vehicles/" + UUID.randomUUID(), HttpMethod.DELETE, null, String.class);

        restTemplate.delete("/vehicles/" + UUID.randomUUID());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("No Vehicle Exists With This UUID!"));
    }


    private VehicleDTO buildValidVehicleDTO() {
        return VehicleDTO.builder()
                .name("Seller Representative 1")
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(11.56)
                .highwayFuelEconomy(13.1)
                .build();
    }

}