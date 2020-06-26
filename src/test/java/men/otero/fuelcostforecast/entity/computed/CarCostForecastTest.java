package men.otero.fuelcostforecast.entity.computed;

import men.otero.fuelcostforecast.dto.CarDTO;
import men.otero.fuelcostforecast.entity.computed.CarCostForecast;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarCostForecastTest {

    @Test
    public void createCarCostForecastDTO(){
        CarDTO carDTO = buildValidCarDTO(LocalDate.now());

        CarCostForecast carCostForecast = new CarCostForecast(carDTO, 3.5, 100.0, 299.9);

        assertEquals(carDTO.getName(), carCostForecast.getName());
        assertEquals(carDTO.getBrand(), carCostForecast.getBrand());
        assertEquals(carDTO.getModel(), carCostForecast.getModel());
        assertEquals(carDTO.getManufacturingDate().getYear(), carCostForecast.getManufacturingYear());
        assertEquals(31.543648802134236, carCostForecast.getSpentFuel());
        assertEquals(110.40277080746982, carCostForecast.getTotalFuelCost());
    }

    @Test
    public void createCarCostForecastDTOWhenManufacturingDateIsNull(){
        CarDTO carDTO = buildValidCarDTO(null);

        CarCostForecast carCostForecast = new CarCostForecast(carDTO, 3.5, 100.0, 299.9);

        assertEquals(carDTO.getName(), carCostForecast.getName());
        assertEquals(carDTO.getBrand(), carCostForecast.getBrand());
        assertEquals(carDTO.getModel(), carCostForecast.getModel());
        assertEquals(null, carCostForecast.getManufacturingYear());
        assertEquals(31.543648802134236, carCostForecast.getSpentFuel());
        assertEquals(110.40277080746982, carCostForecast.getTotalFuelCost());
    }

    private CarDTO buildValidCarDTO(LocalDate manufacturingDate) {
        return CarDTO.builder()
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