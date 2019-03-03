package kayiran.samet.task.exception;

public class IllegalTcException extends RuntimeException {

	public IllegalTcException() {
	}

	public IllegalTcException(String message) {
		super(message);
	}

	public IllegalTcException(Throwable cause) {
		super(cause);
	}

	public IllegalTcException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTcException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
