package kayiran.samet.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestCustomeExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(FieldValidationException exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(ResourceNotFoundException exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
		
	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(IllegalTcException exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(InsufficientBalanceException exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(MaxValuePerTransferException exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	 	
	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(LimitExceededMaxExchangePerDay exc) {

		// create CustomeErrorResponse
		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
				System.currentTimeMillis());

		// return ResponseEntity
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	
	// Add another exception handler ... to catch any exception (catch all)
	@ExceptionHandler
	public ResponseEntity<CustomeErrorResponse> handleException(Exception exc) {

		// create CustomeErrorResponse

		CustomeErrorResponse error = new CustomeErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage() + "samet",
				System.currentTimeMillis());

		// return ResponseEntity

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
