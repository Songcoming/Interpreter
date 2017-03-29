public class CaseParmPtr extends ExprNode{
	ComputeFunc ptrValue;

	public CaseParmPtr(TokenTab tab, ComputeFunc realValue){
		super(tab);
		this.ptrValue = realValue;
	}	
}