public class TokenTab{
	public String corrType;
	public String oriInput;
	public double constValue;
	public ComputeFunc pointer;
	public int lineNumber;

	public TokenTab(String corrType, String oriInput, double constValue, ComputeFunc pointer){
		this.corrType = corrType;
		this.oriInput = oriInput;
		this.constValue = constValue;
		this.pointer = pointer;
	}

	public void addLineNumber(int lineNumber){
		this.lineNumber = lineNumber;
	}
}