import java.lang.Math;

public class ComputeFunc{
	String funcType;
	static double parameter = 0.0;

	public ComputeFunc(String funcType){
		this.funcType = funcType;
	}

	public double compute(double value){
		switch(funcType){
			case "SIN":   return Math.sin(value);
			case "COS":   return Math.cos(value);
			case "TAN":   return Math.tan(value);
			case "LN" :   return Math.log(value);
			case "EXP":   return Math.exp(value);
			case "SQRT":  return Math.sqrt(value);
			default:
				System.out.println("Error: Unexcepted function.");
				return 0.0;
		}
	}

	public double getPvalue(){
		return parameter;
	}
}