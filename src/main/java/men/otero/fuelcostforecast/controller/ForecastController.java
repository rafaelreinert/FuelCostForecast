package men.otero.fuelcostforecast.controller;

import men.otero.fuelcostforecast.entity.computed.VehicleFuelCostForecast;
import men.otero.fuelcostforecast.service.FuelCostForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/forecasts")
public class ForecastController {

    @Autowired
    private FuelCostForecastService fuelCostForecastService;

    @GetMapping("/cost")
    public Flux<VehicleFuelCostForecast> getAllCostForecastOrderedByTotalFuelCost(
            @RequestParam final Double fuelPrice, @RequestParam final Double kiloMetersOnCity,
            @RequestParam final Double kiloMetersOnHighway) {
        return fuelCostForecastService.findAllOrderedByTotalFuelCost(fuelPrice, kiloMetersOnCity, kiloMetersOnHighway);
    }

}
