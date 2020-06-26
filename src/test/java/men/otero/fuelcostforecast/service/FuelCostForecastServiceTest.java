package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.entity.computed.CarCostForecast;
import men.otero.fuelcostforecast.dto.CarDTO;
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
    private CarService carService;

    @InjectMocks
    private FuelCostForecastService fuelCostForecastService;

    @Test
    public void findAllOrderedByTotalFuelCost(){
        Mockito.doReturn(Flux.fromArray(new CarDTO[]{
                buildValidCarDTO("Car 1",9.42, 11.3),
                buildValidCarDTO("Car 2",7.2, 8.2),
                buildValidCarDTO("Car 3",13.9, 7.3),
                buildValidCarDTO("Car 4",13.2, 11.6)}))
                .when(carService).findAll();

        Flux<CarCostForecast> allOrderedByTotalFuelCost = fuelCostForecastService
                .findAllOrderedByTotalFuelCost(3.23, 50.0, 20.25);

        List<CarCostForecast> forecastDTOS = allOrderedByTotalFuelCost.collectList().block();

        assertEquals(4, forecastDTOS.size());
        assertEquals("Car 4", forecastDTOS.get(0).getName());
        assertEquals("Car 3", forecastDTOS.get(1).getName());
        assertEquals("Car 1", forecastDTOS.get(2).getName());
        assertEquals("Car 2", forecastDTOS.get(3).getName());
    }

    @Test
    public void findAllOrderedByTotalFuelCostWhenHasNoCars(){
        Mockito.doReturn(Flux.empty()).when(carService).findAll();

        Flux<CarCostForecast> allOrderedByTotalFuelCost = fuelCostForecastService
                .findAllOrderedByTotalFuelCost(3.23, 50.0, 20.25);

        List<CarCostForecast> forecastDTOS = allOrderedByTotalFuelCost.collectList().block();

        assertEquals(0, forecastDTOS.size());
    }

    private CarDTO buildValidCarDTO(String name,Double cityFuelEconomy, Double highwayFuelEconomy) {
        return CarDTO.builder()
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