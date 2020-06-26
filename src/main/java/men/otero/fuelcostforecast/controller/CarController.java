package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.dto.CarDTO;
import men.otero.fuelcostforecast.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public Flux<CarDTO> findAll() {
        return carService.findAll();
    }

    @PostMapping
    public Mono<CarDTO> createCar(@Validated @RequestBody final CarDTO carDTO) {
        carDTO.setUuid(null);
        return carService.save(carDTO);
    }

    @GetMapping("/{uuid}")
    public Mono<CarDTO> get(@PathVariable final UUID uuid) {
        return carService.get(uuid);
    }

    @PutMapping("/{uuid}")
    public Mono<CarDTO> updateCar(@PathVariable final UUID uuid, @Validated @RequestBody final CarDTO carDTO) {
        carDTO.setUuid(uuid);
        return carService.save(carDTO);
    }

    @DeleteMapping("/{uuid}")
    public Mono<Void> delete(@PathVariable final UUID uuid) {
        return carService.delete(uuid);
    }

}
