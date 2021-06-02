package sigma.training.ctp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHelper {

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<Object> handleInvalidInputException(IllegalArgumentException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);

  }
}
