package sigma.training.ctp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sigma.training.ctp.exception.CannotFulfillOwnOrderException;
import sigma.training.ctp.exception.InsufficientAmountBankCurrencyException;
import sigma.training.ctp.exception.InsufficientCurrencyAmountException;
import sigma.training.ctp.exception.OrderAlreadyCancelledException;
import sigma.training.ctp.exception.OrderAlreadyFulfilledException;
import sigma.training.ctp.exception.OrderNotFoundException;

import javax.validation.ConstraintViolationException;

import sigma.training.ctp.exception.NoActiveOrdersFoundException;
import sigma.training.ctp.exception.WalletNotFoundException;

@ControllerAdvice
public class ExceptionsHandler {

  @ExceptionHandler(value = {InsufficientCurrencyAmountException.class})
  public ResponseEntity<Object> handleInvalidInputException(InsufficientCurrencyAmountException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {InsufficientAmountBankCurrencyException.class})
  public ResponseEntity<Object> handleInvalidInputException(InsufficientAmountBankCurrencyException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {OrderAlreadyCancelledException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderAlreadyCancelledException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {OrderAlreadyFulfilledException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderAlreadyFulfilledException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {OrderNotFoundException.class})
  public ResponseEntity<Object> handleInvalidInputException(OrderNotFoundException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {WalletNotFoundException.class})
  public ResponseEntity<Object> handleInvalidInputException(WalletNotFoundException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  public ResponseEntity<Object> handleInvalidInputException(ConstraintViolationException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(value = {NoActiveOrdersFoundException.class})
  public ResponseEntity<Object> handleInvalidInputException(NoActiveOrdersFoundException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {CannotFulfillOwnOrderException.class})
  public ResponseEntity<Object> handleInvalidInputException(CannotFulfillOwnOrderException ex) {
    return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
  }
}
