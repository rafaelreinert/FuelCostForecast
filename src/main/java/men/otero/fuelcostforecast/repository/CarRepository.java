package men.otero.fuelcostforecast.repository;

import men.otero.fuelcostforecast.entity.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends CrudRepository<Car, UUID> {

    List<Car> findAll();
}
