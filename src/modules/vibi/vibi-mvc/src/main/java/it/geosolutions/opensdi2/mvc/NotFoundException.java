package it.geosolutions.opensdi2.mvc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "")
public class NotFoundException extends RuntimeException {
}
