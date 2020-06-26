package men.otero.fuelcostforecast.entity.computed;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleFuelCostForecastTest {

    @Test
    public void createVehicleCostForecast() {
        VehicleDTO vehicleDTO = buildValidVehicleDTO(LocalDate.now());

        VehicleFuelCostForecast vehicleFuelCostForecast = new VehicleFuelCostForecast(vehicleDTO, 3.5, 100.0, 299.9);

        assertEquals(vehicleDTO.getName(), vehicleFuelCostForecast.getName());
        assertEquals(vehicleDTO.getBrand(), vehicleFuelCostForecast.getBrand());
        assertEquals(vehicleDTO.getModel(), vehicleFuelCostForecast.getModel());
        assertEquals(vehicleDTO.getManufacturingDate().getYear(), vehicleFuelCostForecast.getManufacturingYear());
        assertEquals(31.543648802134236, vehicleFuelCostForecast.getSpentFuel());
        assertEquals(110.40277080746982, vehicleFuelCostForecast.getTotalFuelCost());
    }

    @Test
    public void createVehicleCostForecastWhenManufacturingDateIsNull() {
        VehicleDTO vehicleDTO = buildValidVehicleDTO(null);

        VehicleFuelCostForecast vehicleFuelCostForecast = new VehicleFuelCostForecast(vehicleDTO, 3.5, 100.0, 299.9);

        assertEquals(vehicleDTO.getName(), vehicleFuelCostForecast.getName());
        assertEquals(vehicleDTO.getBrand(), vehicleFuelCostForecast.getBrand());
        assertEquals(vehicleDTO.getModel(), vehicleFuelCostForecast.getModel());
        assertEquals(null, vehicleFuelCostForecast.getManufacturingYear());
        assertEquals(31.543648802134236, vehicleFuelCostForecast.getSpentFuel());
        assertEquals(110.40277080746982, vehicleFuelCostForecast.getTotalFuelCost());
    }

    private VehicleDTO buildValidVehicleDTO(LocalDate manufacturingDate) {
        return VehicleDTO.builder()
                .uuid(UUID.randomUUID())
                .name("Seller Representative 1")
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(manufacturingDate)
                .cityFuelEconomy(11.56)
                .highwayFuelEconomy(13.1)
                .build();
    }

}