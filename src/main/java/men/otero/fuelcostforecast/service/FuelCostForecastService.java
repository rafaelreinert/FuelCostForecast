package men.otero.fuelcostforecast.service;

import men.otero.fuelcostforecast.entity.computed.CarCostForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;

@Service
public class FuelCostForecastService {

    @Autowired
    private CarService carService;

    public Flux<CarCostForecast> findAllOrderedByTotalFuelCost(final Double fuelPrice,
                                                               final Double kiloMetersOnCity, final Double kiloMetersOnHighway) {
        return carService.findAll()
                .map(carDTO -> new CarCostForecast(carDTO, fuelPrice, kiloMetersOnCity, kiloMetersOnHighway))
                .sort(Comparator.comparingDouble(carCostForecast -> carCostForecast.getTotalFuelCost()));

    }


}
