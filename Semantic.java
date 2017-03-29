import java.util.LinkedList;

public class Semantic{

	double parameter = 0.0;
	double rot_ang   = 0.0;
	double origin_x  = 0.0, origin_y = 0.0;
	double scale_x   = 0.0,  scale_y = 0.0;
	LinkedList<Shape> shapeList = new LinkedList();

	public double getExprValue(ExprNode root){
		CaseOperator    caseRoot;
		CaseFunc        funcRoot;
		CaseConst      constRoot;
		CaseParmPtr      ptrRoot;

		if (root == null){
			return 0.0;
		}

		switch (root.tokenType){
			case "PLUS":
				caseRoot = (CaseOperator) root;
				return getExprValue(caseRoot.left) + getExprValue(caseRoot.right);
			case "MINUS":
				caseRoot = (CaseOperator) root;
				return getExprValue(caseRoot.left) - getExprValue(caseRoot.right);
			case "MUL":
				caseRoot = (CaseOperator) root;
				return getExprValue(caseRoot.left) * getExprValue(caseRoot.right);
			case "DIV":
				caseRoot = (CaseOperator) root;
				return getExprValue(caseRoot.left) / getExprValue(caseRoot.right);
			case "FUNC":
				funcRoot = (CaseFunc) root;
				return funcRoot.computeValue(getExprValue(funcRoot.child));
			case "CONST_ID":
				constRoot = (CaseConst) root;
				return constRoot.constValue;
			case "T":
				ptrRoot = (CaseParmPtr) root;
				return ptrRoot.ptrValue.getPvalue();
			default:
				return 0.0;
		}
	}

	public void collectShape(double start, double end, double step, ExprNode x_ptr, ExprNode y_ptr){
		Shape sp = new Shape(start, end, step, x_ptr, y_ptr);
		//System.out.println(sp);
		shapeList.add(sp);
	}

	public void setOrigin(double oriX, double oriY){
		origin_x = oriX;
		origin_y = oriY;
	}

	public void setScale(double scaX, double scaY){
		scale_x = scaX;
		scale_y = scaY;
	}

	public void setRot(double rot){
		rot_ang = rot;
	}

	public double getOriginX(){
		return origin_x;
	}

	public double getOriginY(){
		return origin_y;
	}

	public double getScaleX(){
		return scale_x;
	}

	public double getScaleY(){
		return scale_y;
	}

	public double getRot(){
		return rot_ang;
	}
}