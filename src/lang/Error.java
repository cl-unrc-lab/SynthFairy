package lang;

public class Error {
	
	private String errorMsg;
	private Integer line;
	
	
	public Error(String msg){
		errorMsg = msg;
	}
	
	public Error(String msg, int lineNumber){
		errorMsg = msg;
		line= new Integer(lineNumber);
	}
	
	public String getCompleteErrorMsg(){
		return errorMsg.concat(" - Line :").concat(line.toString());
	}
	
	public String getErrorMsg(){
		return errorMsg;
	}


}
