package men.otero.fuelcostforecast.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(description = "List all vehicles", responses = {
            @ApiResponse(responseCode = "200", description = "stream All Vehicles") })
    @GetMapping
    public Flux<VehicleDTO> findAll() {
        return vehicleService.findAll();
    }

    @Operation(description = "Create a new vehicle", responses = {
            @ApiResponse(responseCode = "201", description = "Vehicle Created") })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<VehicleDTO> create(@Validated @RequestBody final VehicleDTO vehicleDTO) {
        vehicleDTO.setUuid(null);
        return vehicleService.save(vehicleDTO);
    }

    @Operation(description = "Get the vehicle by UUID", responses = {
        @ApiResponse(responseCode = "200", description = "Vehicle requested") })
    @GetMapping("/{uuid}")
    public Mono<VehicleDTO> get(@PathVariable final UUID uuid) {
        return vehicleService.get(uuid);
    }

    @Operation(description = "Update the vehicle", responses = {
            @ApiResponse(responseCode = "200", description = "Vehicle Updated") })
    @PutMapping("/{uuid}")
    public Mono<VehicleDTO> update(@PathVariable final UUID uuid, @Validated @RequestBody final VehicleDTO vehicleDTO) {
        vehicleDTO.setUuid(uuid);
        return vehicleService.save(vehicleDTO);
    }

    @Operation(description = "Delete the vehicle", responses = {
            @ApiResponse(responseCode = "200", description = "Vehicle Deleted") })
    @DeleteMapping("/{uuid}")
    public Mono<Void> delete(@PathVariable final UUID uuid) {
        return vehicleService.delete(uuid);
    }

}
