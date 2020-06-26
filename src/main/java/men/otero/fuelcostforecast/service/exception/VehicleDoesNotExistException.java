package men.otero.fuelcostforecast.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Vehicle Exists With This UUID!")
public class VehicleDoesNotExistException extends RuntimeException {
}
