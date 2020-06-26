package men.otero.fuelcostforecast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import men.otero.fuelcostforecast.entity.Vehicle;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private UUID uuid;

    @NotBlank(message = "Name Can Not Be Null or Empty")
    private String name;

    @NotBlank(message = "Brand Can Not Be Null or Empty")
    private String brand;

    @NotBlank(message = "Model Can Not Be Null or Empty")
    private String model;

    @NotNull(message = "Manufacturing Date Can Not Be Null")
    @PastOrPresent(message = "Manufacturing Date Can Not Be In The Future")
    private LocalDate manufacturingDate;

    @NotNull(message = "City Fuel Economy Can Not Be Null")
    @DecimalMin(value = "0.1", message = "City Fuel Economy Can Not Be Less Than 0.1")
    private Double cityFuelEconomy;

    @NotNull(message = "Highway Fuel Economy Can Not Be Null")
    @DecimalMin(value = "0.1", message = "Highway Fuel Economy Can Not Be Less Than 0.1")
    private Double highwayFuelEconomy;

    public VehicleDTO(Vehicle vehicle) {
        this.uuid = vehicle.getUuid();
        this.name = vehicle.getName();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.manufacturingDate = vehicle.getManufacturingDate();
        this.cityFuelEconomy = vehicle.getCityFuelEconomy();
        this.highwayFuelEconomy = vehicle.getHighwayFuelEconomy();
    }

    public Vehicle toVehicle() {
        return Vehicle.builder()
                .uuid(this.uuid)
                .name(this.name)
                .brand(this.brand)
                .model(this.model)
                .manufacturingDate(this.manufacturingDate)
                .cityFuelEconomy(this.cityFuelEconomy)
                .highwayFuelEconomy(this.highwayFuelEconomy)
                .build();
    }


}
