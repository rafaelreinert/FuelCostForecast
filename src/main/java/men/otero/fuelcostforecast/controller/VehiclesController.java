package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.dto.VehicleDTO;
import men.otero.fuelcostforecast.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
public class VehiclesController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public Flux<VehicleDTO> findAll() {
        return vehicleService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<VehicleDTO> create(@Validated @RequestBody final VehicleDTO vehicleDTO) {
        vehicleDTO.setUuid(null);
        return vehicleService.save(vehicleDTO);
    }

    @GetMapping("/{uuid}")
    public Mono<VehicleDTO> get(@PathVariable final UUID uuid) {
        return vehicleService.get(uuid);
    }

    @PutMapping("/{uuid}")
    public Mono<VehicleDTO> update(@PathVariable final UUID uuid, @Validated @RequestBody final VehicleDTO vehicleDTO) {
        vehicleDTO.setUuid(uuid);
        return vehicleService.save(vehicleDTO);
    }

    @DeleteMapping("/{uuid}")
    public Mono<Void> delete(@PathVariable final UUID uuid) {
        return vehicleService.delete(uuid);
    }

}
