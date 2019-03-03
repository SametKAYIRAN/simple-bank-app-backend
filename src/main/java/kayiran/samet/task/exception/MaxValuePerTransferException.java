package kayiran.samet.task.exception;

public class MaxValuePerTransferException extends RuntimeException {

	public MaxValuePerTransferException() {
	}

	public MaxValuePerTransferException(String message) {
		super(message);
	}

	public MaxValuePerTransferException(Throwable cause) {
		super(cause);
	}

	public MaxValuePerTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public MaxValuePerTransferException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
