package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import men.otero.fuelcostforecast.repository.VehicleRepository;
import men.otero.fuelcostforecast.service.exception.VehicleDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Mono<VehicleDTO> get(UUID uuid) {
        return Mono.fromCallable(() -> vehicleRepository.findById(uuid).map(VehicleDTO::new)
                .orElseThrow(VehicleDoesNotExistException::new));
    }

    public Mono<VehicleDTO> save(VehicleDTO vehicleDTO) {
        return Mono.fromCallable(() -> new VehicleDTO(vehicleRepository.save(vehicleDTO.toVehicle())));
    }

    public Mono<Void> delete(UUID uuid) {
        return Mono.create((s) -> {
            try {
                vehicleRepository.deleteById(uuid);
            } catch (EmptyResultDataAccessException e) {
                s.error(new VehicleDoesNotExistException());
            }
            s.success();
        });
    }

    public Flux<VehicleDTO> findAll() {
        return Flux.fromStream(vehicleRepository.findAll().stream()
                .map(VehicleDTO::new));
    }

}
