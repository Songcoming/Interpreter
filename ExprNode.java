class ExprNode{
	String tokenType;
	String oriInput;

	public ExprNode(TokenTab tab){
		this.tokenType = tab.corrType;
		this.oriInput = tab.oriInput;
	}
}