package kayiran.samet.task.exception;

public class LimitExceededMaxExchangePerDay extends RuntimeException {

	public LimitExceededMaxExchangePerDay() {
	}

	public LimitExceededMaxExchangePerDay(String message) {
		super(message);
	}

	public LimitExceededMaxExchangePerDay(Throwable cause) {
		super(cause);
	}

	public LimitExceededMaxExchangePerDay(String message, Throwable cause) {
		super(message, cause);
	}

	public LimitExceededMaxExchangePerDay(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
