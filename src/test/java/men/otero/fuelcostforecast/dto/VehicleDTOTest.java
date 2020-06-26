package men.otero.fuelcostforecast.dto;

import men.otero.fuelcostforecast.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDTOTest {

    @Test
    public void createVehicleDTOFromVehicle(){
        Vehicle vehicle =  buildValidVehicle();

        VehicleDTO vehicleDTO = new VehicleDTO(vehicle);

        assertEquals(vehicle.getUuid(), vehicleDTO.getUuid());
        assertEquals(vehicle.getName(), vehicleDTO.getName());
        assertEquals(vehicle.getBrand(), vehicleDTO.getBrand());
        assertEquals(vehicle.getModel(), vehicleDTO.getModel());
        assertEquals(vehicle.getManufacturingDate(), vehicleDTO.getManufacturingDate());
        assertEquals(vehicle.getCityFuelEconomy(), vehicleDTO.getCityFuelEconomy());
        assertEquals(vehicle.getHighwayFuelEconomy(), vehicleDTO.getHighwayFuelEconomy());
    }

    @Test
    public void convertVehicleDTOToVehicle(){
        VehicleDTO vehicleDTO = buildValidVehicleDTO();

        Vehicle vehicle =  vehicleDTO.toVehicle();

        assertEquals(vehicleDTO.getUuid(), vehicle.getUuid());
        assertEquals(vehicleDTO.getName(), vehicle.getName());
        assertEquals(vehicleDTO.getBrand(), vehicle.getBrand());
        assertEquals(vehicleDTO.getModel(), vehicle.getModel());
        assertEquals(vehicleDTO.getManufacturingDate(), vehicle.getManufacturingDate());
        assertEquals(vehicleDTO.getCityFuelEconomy(), vehicle.getCityFuelEconomy());
        assertEquals(vehicleDTO.getHighwayFuelEconomy(), vehicle.getHighwayFuelEconomy());
    }

    private Vehicle buildValidVehicle() {
        return Vehicle.builder()
                .uuid(UUID.randomUUID())
                .name("Seller Representative 2")
                .brand("Fiat")
                .model("Uno Com Escada V12")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(21.3)
                .highwayFuelEconomy(25.1)
                .build();
    }

    private VehicleDTO buildValidVehicleDTO() {
        return VehicleDTO.builder()
                .uuid(UUID.randomUUID())
                .name("Seller Representative 1")
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(11.56)
                .highwayFuelEconomy(13.1)
                .build();
    }

}