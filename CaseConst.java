public class CaseConst extends ExprNode{
	double constValue;

	public CaseConst(TokenTab tab){
		super(tab);
		this.constValue = tab.constValue;
	}	
}