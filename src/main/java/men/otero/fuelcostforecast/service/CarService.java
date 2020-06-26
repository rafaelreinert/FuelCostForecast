package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.dto.CarDTO;
import men.otero.fuelcostforecast.repository.CarRepository;
import men.otero.fuelcostforecast.service.exception.CarDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Mono<CarDTO> get(UUID uuid) {
        return Mono.fromCallable(() -> carRepository.findById(uuid).map(CarDTO::new)
                .orElseThrow(CarDoesNotExistException::new));
    }

    public Mono<CarDTO> save(CarDTO carDTO) {
        return Mono.fromCallable(() -> new CarDTO(carRepository.save(carDTO.toCar())));
    }

    public Mono<Void> delete(UUID uuid) {
        return Mono.create((s) -> {
            try {
                carRepository.deleteById(uuid);
            } catch (EmptyResultDataAccessException e) {
                s.error(new CarDoesNotExistException());
            }
            s.success();
        });
    }

    public Flux<CarDTO> findAll() {
        return Flux.fromStream(carRepository.findAll().stream()
                .map(CarDTO::new));
    }

}
