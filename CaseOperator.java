class CaseOperator extends ExprNode{
	ExprNode left;
	ExprNode right;

	public CaseOperator(TokenTab tab){
		super(tab);		
	}

	public void setLeftRight(ExprNode left, ExprNode right){
		this.left = left;
		this.right = right;	
	}
}