package men.otero.fuelcostforecast.entity.computed;

import lombok.Data;
import men.otero.fuelcostforecast.dto.VehicleDTO;

@Data
public class VehicleFuelCostForecast {

    private String name;

    private String brand;

    private String model;

    private Integer manufacturingYear;

    private Double spentFuel;

    private Double totalFuelCost;

    public VehicleFuelCostForecast(final VehicleDTO vehicleDTO, final Double fuelPrice,
                                   final Double kiloMetersOnCity, final Double kiloMetersOnHighway) {
        this.name = vehicleDTO.getName();
        this.brand = vehicleDTO.getBrand();
        this.model = vehicleDTO.getModel();
        this.manufacturingYear = (vehicleDTO.getManufacturingDate() != null)? vehicleDTO.getManufacturingDate().getYear():null;
        this.spentFuel = calculateTotalSpentFuel(vehicleDTO, kiloMetersOnCity, kiloMetersOnHighway);
        this.totalFuelCost = this.spentFuel * fuelPrice;
    }

    private double calculateTotalSpentFuel(VehicleDTO vehicleDTO, Double kiloMetersOnCity, Double kiloMetersOnHighway) {
        Double fuelSpentOnCity = kiloMetersOnCity / vehicleDTO.getCityFuelEconomy();
        Double fuelSpentOnHighway = kiloMetersOnHighway / vehicleDTO.getHighwayFuelEconomy();
        return fuelSpentOnCity + fuelSpentOnHighway;
    }


}
