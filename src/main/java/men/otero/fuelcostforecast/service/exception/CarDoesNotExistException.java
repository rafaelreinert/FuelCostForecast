package men.otero.fuelcostforecast.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Car Exists With This UUID!")
public class CarDoesNotExistException extends RuntimeException {
}
