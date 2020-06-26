package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.dto.CarDTO;
import men.otero.fuelcostforecast.entity.Car;
import men.otero.fuelcostforecast.repository.CarRepository;
import men.otero.fuelcostforecast.service.exception.CarDoesNotExistException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void get() {
        Car car = buildValidCar(UUID.randomUUID());
        Mockito.doReturn(Optional.of(car)).when(carRepository).findById(car.getUuid());

        CarDTO carDTO = carService.get(car.getUuid()).block();

        assertEquals(car.getUuid(),carDTO.getUuid());
        assertEquals(car.getName(),carDTO.getName());
        assertEquals(car.getBrand(),carDTO.getBrand());
        assertEquals(car.getModel(),carDTO.getModel());
        assertEquals(car.getManufacturingDate(),carDTO.getManufacturingDate());
        assertEquals(car.getCityFuelEconomy(),carDTO.getCityFuelEconomy());
        assertEquals(car.getHighwayFuelEconomy(),carDTO.getHighwayFuelEconomy());
    }

    @Test
    void getWhenFindReturnAnEmptyOptional() {
        assertThrows(CarDoesNotExistException.class,() -> carService.get(UUID.randomUUID()).block());
    }

    @Test
    void save() {
        Car car = buildValidCar(UUID.randomUUID());
        Mockito.doReturn(car).when(carRepository).save(car);
        CarDTO carDTO = new CarDTO(car);

        CarDTO savedCarDTO = carService.save(carDTO).block();

        assertEquals(carDTO, savedCarDTO);
    }

    @Test
    void saveWhenItThrowARuntimeException() {
        Car car = buildValidCar(UUID.randomUUID());
        Mockito.doThrow(new RuntimeException()).when(carRepository).save(car);

        assertThrows(RuntimeException.class, () -> carService.save(new CarDTO(car)).block());
    }

    @Test
    void delete() {
        UUID uuid = UUID.randomUUID();
        carService.delete(uuid);
    }

    @Test
    void deleteWhenItThrowAnEmptyResultDataAccessException() {
        UUID uuid = UUID.randomUUID();
        Mockito.doThrow(new EmptyResultDataAccessException(0)).when(carRepository).deleteById(uuid);
        assertThrows(CarDoesNotExistException.class, () -> carService.delete(uuid).block());
    }

    @Test
    void findAll() {
        UUID firstCarUUID = UUID.randomUUID();
        UUID secondCarUUID = UUID.randomUUID();
        UUID thirdCarUUID = UUID.randomUUID();
        Mockito.doReturn(Lists.newArrayList(
                buildValidCar(firstCarUUID), buildValidCar(secondCarUUID), buildValidCar(thirdCarUUID)
        )).when(carRepository).findAll();

        List<CarDTO> allCarDTOs = carService.findAll().collectList().block();
        assertEquals(3,allCarDTOs.size());
        assertEquals(firstCarUUID,allCarDTOs.get(0).getUuid());
        assertEquals(secondCarUUID,allCarDTOs.get(1).getUuid());
        assertEquals(thirdCarUUID,allCarDTOs.get(2).getUuid());
    }

    @Test
    void findAllWhenRepositoryReturnNothing() {
        Mockito.doReturn(new ArrayList()).when(carRepository).findAll();

        List<CarDTO> allCarDTOs = carService.findAll().collectList().block();
        assertEquals(0,allCarDTOs.size());
    }

    private Car buildValidCar(UUID uuid) {
        return Car.builder()
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