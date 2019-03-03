package kayiran.samet.task.exception;

public class FieldValidationException extends RuntimeException {

	public FieldValidationException() {
	}

	public FieldValidationException(String message) {
		super(message);
	}

	public FieldValidationException(Throwable cause) {
		super(cause);
	}

	public FieldValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
