package men.otero.fuelcostforecast.repository;

import men.otero.fuelcostforecast.entity.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, UUID> {

    List<Vehicle> findAll();
}
