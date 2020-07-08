package util;

public class Response {
	
	
	private String message;
	private Object content;
	private Boolean success;
	
	public Response(Boolean success, String message) {
		this.message = message;
		this.success = success;
	}
	
	public Response() {
		
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	
	
	
	

}
