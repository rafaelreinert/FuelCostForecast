package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.entity.computed.VehicleFuelCostForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Service
public class FuelCostForecastService {

    @Autowired
    private VehicleService vehicleService;

    public Flux<VehicleFuelCostForecast> findAllOrderedByTotalFuelCost(final Double fuelPrice,
                                                                       final Double kiloMetersOnCity, final Double kiloMetersOnHighway) {
        return vehicleService.findAll()
                .map(vehicleDTO -> new VehicleFuelCostForecast(vehicleDTO, fuelPrice, kiloMetersOnCity, kiloMetersOnHighway))
                .sort(Comparator.comparingDouble(vehicleFuelCostForecast -> vehicleFuelCostForecast.getTotalFuelCost()));

    }


}
