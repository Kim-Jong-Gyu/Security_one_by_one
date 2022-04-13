package Data;

import java.io.Serializable;

public class InputData implements Serializable{
	String Command;
	Object obj;
	public String getCommand() {
		return Command;
	}
	public void setCommand(String command) {
		Command = command;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
