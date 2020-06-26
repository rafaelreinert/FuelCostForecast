package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import men.otero.fuelcostforecast.entity.Vehicle;
import men.otero.fuelcostforecast.repository.VehicleRepository;
import men.otero.fuelcostforecast.service.exception.VehicleDoesNotExistException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void get() {
        Vehicle vehicle = buildValidVehicle(UUID.randomUUID());
        Mockito.doReturn(Optional.of(vehicle)).when(vehicleRepository).findById(vehicle.getUuid());

        VehicleDTO vehicleDTO = vehicleService.get(vehicle.getUuid()).block();

        assertEquals(vehicle.getUuid(), vehicleDTO.getUuid());
        assertEquals(vehicle.getName(), vehicleDTO.getName());
        assertEquals(vehicle.getBrand(), vehicleDTO.getBrand());
        assertEquals(vehicle.getModel(), vehicleDTO.getModel());
        assertEquals(vehicle.getManufacturingDate(), vehicleDTO.getManufacturingDate());
        assertEquals(vehicle.getCityFuelEconomy(), vehicleDTO.getCityFuelEconomy());
        assertEquals(vehicle.getHighwayFuelEconomy(), vehicleDTO.getHighwayFuelEconomy());
    }

    @Test
    void getWhenFindReturnAnEmptyOptional() {
        assertThrows(VehicleDoesNotExistException.class, () -> vehicleService.get(UUID.randomUUID()).block());
    }

    @Test
    void save() {
        Vehicle vehicle = buildValidVehicle(UUID.randomUUID());
        Mockito.doReturn(vehicle).when(vehicleRepository).save(vehicle);
        VehicleDTO vehicleDTO = new VehicleDTO(vehicle);

        VehicleDTO savedVehicleDTO = vehicleService.save(vehicleDTO).block();

        assertEquals(vehicleDTO, savedVehicleDTO);
    }

    @Test
    void saveWhenItThrowARuntimeException() {
        Vehicle vehicle = buildValidVehicle(UUID.randomUUID());
        Mockito.doThrow(new RuntimeException()).when(vehicleRepository).save(vehicle);

        assertThrows(RuntimeException.class, () -> vehicleService.save(new VehicleDTO(vehicle)).block());
    }

    @Test
    void delete() {
        UUID uuid = UUID.randomUUID();
        vehicleService.delete(uuid);
    }

    @Test
    void deleteWhenItThrowAnEmptyResultDataAccessException() {
        UUID uuid = UUID.randomUUID();
        Mockito.doThrow(new EmptyResultDataAccessException(0)).when(vehicleRepository).deleteById(uuid);
        assertThrows(VehicleDoesNotExistException.class, () -> vehicleService.delete(uuid).block());
    }

    @Test
    void findAll() {
        UUID firstVehicleUUID = UUID.randomUUID();
        UUID secondVehicleUUID = UUID.randomUUID();
        UUID thirdVehicleUUID = UUID.randomUUID();
        Mockito.doReturn(Lists.newArrayList(
                buildValidVehicle(firstVehicleUUID), buildValidVehicle(secondVehicleUUID), buildValidVehicle(thirdVehicleUUID)
        )).when(vehicleRepository).findAll();

        List<VehicleDTO> allVehicleDTOS = vehicleService.findAll().collectList().block();
        assertEquals(3, allVehicleDTOS.size());
        assertEquals(firstVehicleUUID, allVehicleDTOS.get(0).getUuid());
        assertEquals(secondVehicleUUID, allVehicleDTOS.get(1).getUuid());
        assertEquals(thirdVehicleUUID, allVehicleDTOS.get(2).getUuid());
    }

    @Test
    void findAllWhenRepositoryReturnNothing() {
        Mockito.doReturn(new ArrayList()).when(vehicleRepository).findAll();

        List<VehicleDTO> allVehicleDTOS = vehicleService.findAll().collectList().block();
        assertEquals(0, allVehicleDTOS.size());
    }

    private Vehicle buildValidVehicle(UUID uuid) {
        return Vehicle.builder()
                .uuid(uuid)
                .name("Seller Representative 2")
                .brand("Fiat")
                .model("Uno Com Escada V12")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(21.3)
                .highwayFuelEconomy(25.1)
                .build();
    }
}