package men.otero.fuelcostforecast.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import men.otero.fuelcostforecast.entity.Car;

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
public class CarDTO {

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

    public CarDTO(Car car) {
        this.uuid = car.getUuid();
        this.name = car.getName();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.manufacturingDate = car.getManufacturingDate();
        this.cityFuelEconomy = car.getCityFuelEconomy();
        this.highwayFuelEconomy = car.getHighwayFuelEconomy();
    }

    public Car toCar() {
        return Car.builder()
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
