package kayiran.samet.task.exception;

import lombok.Data;

@Data
public class CustomeErrorResponse {

	private int status;
	private String message;
	private long timeStamp;
	
	public CustomeErrorResponse(int status, String message, long timeStamp) {
		this.status = status;
		this.message = message;
		this.timeStamp = timeStamp;
	}
	
	
	public CustomeErrorResponse() {
		
	}

	
}







