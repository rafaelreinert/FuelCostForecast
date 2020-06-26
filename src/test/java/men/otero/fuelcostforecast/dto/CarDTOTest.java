package men.otero.fuelcostforecast.dto;

import men.otero.fuelcostforecast.entity.Car;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarDTOTest {

    @Test
    public void createCarDTOFromCar(){
        Car car =  buildValidCar();

        CarDTO carDTO = new CarDTO(car);

        assertEquals(car.getUuid(),carDTO.getUuid());
        assertEquals(car.getName(),carDTO.getName());
        assertEquals(car.getBrand(),carDTO.getBrand());
        assertEquals(car.getModel(),carDTO.getModel());
        assertEquals(car.getManufacturingDate(),carDTO.getManufacturingDate());
        assertEquals(car.getCityFuelEconomy(),carDTO.getCityFuelEconomy());
        assertEquals(car.getHighwayFuelEconomy(),carDTO.getHighwayFuelEconomy());
    }

    @Test
    public void convertCarDTOToCar(){
        CarDTO carDTO = buildValidCarDTO();

        Car car =  carDTO.toCar();

        assertEquals(carDTO.getUuid(),car.getUuid());
        assertEquals(carDTO.getName(),car.getName());
        assertEquals(carDTO.getBrand(),car.getBrand());
        assertEquals(carDTO.getModel(),car.getModel());
        assertEquals(carDTO.getManufacturingDate(),car.getManufacturingDate());
        assertEquals(carDTO.getCityFuelEconomy(),car.getCityFuelEconomy());
        assertEquals(carDTO.getHighwayFuelEconomy(),car.getHighwayFuelEconomy());
    }

    private Car buildValidCar() {
        return Car.builder()
                .uuid(UUID.randomUUID())
                .name("Seller Representative 2")
                .brand("Fiat")
                .model("Uno Com Escada V12")
                .manufacturingDate(LocalDate.now())
                .cityFuelEconomy(21.3)
                .highwayFuelEconomy(25.1)
                .build();
    }

    private CarDTO buildValidCarDTO() {
        return CarDTO.builder()
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