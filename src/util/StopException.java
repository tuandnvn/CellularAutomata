package util;

public class StopException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2372288921897376705L;
	public StopException(){
		super("The cellular automata stops changing");
	}
}
