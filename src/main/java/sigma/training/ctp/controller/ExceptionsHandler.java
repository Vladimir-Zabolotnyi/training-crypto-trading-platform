package sigma.training.ctp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientAmountCryptoException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;

@ControllerAdvice
public class ExceptionsHandler {

  @ExceptionHandler(value = {InsufficientAmountCryptoException.class})
  public ResponseEntity<Object> handleInvalidInputException(InsufficientAmountCryptoException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler(value = {InsufficientAmountBankCurrencyException.class})
  public ResponseEntity<Object> handleInvalidInputException(InsufficientAmountBankCurrencyException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {OrderAlreadyCancelledException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderAlreadyCancelledException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {OrderAlreadyFulfilledException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderAlreadyFulfilledException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {OrderNotFoundException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderNotFoundException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }
}
