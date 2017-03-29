public class CaseFunc extends ExprNode{
	ComputeFunc funcPtr;
	ExprNode child;

	public CaseFunc(TokenTab tab, ComputeFunc funcPtr, ExprNode child){
		super(tab);	
		this.funcPtr = funcPtr;
		this.child = child;
	}

	public double computeValue(double child){
		return funcPtr.compute(child);
	}
}