package sigma.training.ctp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.NoActiveOrderFoundException;

@ControllerAdvice
public class ExceptionsHandler {

  @ExceptionHandler(value = {InsufficientAmountCryptoException.class})
  public ResponseEntity<Object> handleInvalidInputException(InsufficientAmountCryptoException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(value = {NoActiveOrderFoundException.class})
  public ResponseEntity<Object> handleInvalidInputException(NoActiveOrderFoundException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
