package men.otero.fuelcostforecast.entity.computed;

import lombok.Data;
import men.otero.fuelcostforecast.dto.CarDTO;

@Data
public class CarCostForecast {

    private String name;

    private String brand;

    private String model;

    private Integer manufacturingYear;

    private Double spentFuel;

    private Double totalFuelCost;

    public CarCostForecast(final CarDTO carDTO, final Double fuelPrice,
                           final Double kiloMetersOnCity, final Double kiloMetersOnHighway) {
        this.name = carDTO.getName();
        this.brand = carDTO.getBrand();
        this.model = carDTO.getModel();
        this.manufacturingYear = (carDTO.getManufacturingDate() != null)?carDTO.getManufacturingDate().getYear():null;
        this.spentFuel = calculateTotalSpentFuel(carDTO, kiloMetersOnCity, kiloMetersOnHighway);
        this.totalFuelCost = this.spentFuel * fuelPrice;
    }

    private double calculateTotalSpentFuel(CarDTO carDTO, Double kiloMetersOnCity, Double kiloMetersOnHighway) {
        Double fuelSpentOnCity = kiloMetersOnCity / carDTO.getCityFuelEconomy();
        Double fuelSpentOnHighway = kiloMetersOnHighway / carDTO.getHighwayFuelEconomy();
        return fuelSpentOnCity + fuelSpentOnHighway;
    }


}
