package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.entity.computed.VehicleFuelCostForecast;
import men.otero.fuelcostforecast.dto.VehicleDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class FuelCostForecastServiceTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private FuelCostForecastService fuelCostForecastService;

    @Test
    public void findAllOrderedByTotalFuelCost(){
        Mockito.doReturn(Flux.fromArray(new VehicleDTO[]{
                buildValidVehicleDTO("Vehicle 1",9.42, 11.3),
                buildValidVehicleDTO("Vehicle 2",7.2, 8.2),
                buildValidVehicleDTO("Vehicle 3",13.9, 7.3),
                buildValidVehicleDTO("Vehicle 4",13.2, 11.6)}))
                .when(vehicleService).findAll();

        Flux<VehicleFuelCostForecast> allOrderedByTotalFuelCost = fuelCostForecastService
                .findAllOrderedByTotalFuelCost(3.23, 50.0, 20.25);

        List<VehicleFuelCostForecast> forecastDTOS = allOrderedByTotalFuelCost.collectList().block();

        assertEquals(4, forecastDTOS.size());
        assertEquals("Vehicle 4", forecastDTOS.get(0).getName());
        assertEquals("Vehicle 3", forecastDTOS.get(1).getName());
        assertEquals("Vehicle 1", forecastDTOS.get(2).getName());
        assertEquals("Vehicle 2", forecastDTOS.get(3).getName());
    }

    @Test
    public void findAllOrderedByTotalFuelCostWhenHasNoVehicles(){
        Mockito.doReturn(Flux.empty()).when(vehicleService).findAll();

        Flux<VehicleFuelCostForecast> allOrderedByTotalFuelCost = fuelCostForecastService
                .findAllOrderedByTotalFuelCost(3.23, 50.0, 20.25);

        List<VehicleFuelCostForecast> forecastDTOS = allOrderedByTotalFuelCost.collectList().block();

        assertEquals(0, forecastDTOS.size());
    }

    private VehicleDTO buildValidVehicleDTO(String name, Double cityFuelEconomy, Double highwayFuelEconomy) {
        return VehicleDTO.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .brand("Ford")
                .model("Fiesta 1.0")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(cityFuelEconomy)
                .highwayFuelEconomy(highwayFuelEconomy)
                .build();
    }

}