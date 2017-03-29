import java.util.LinkedList;

public class Parser{
	static WordAnalyse                 scanner;
	static Semantic                   semantic;
	static LinkedList<TokenTab>     symbleList;
	static TokenTab                      token;
	static int                 symbleCount = 0;

	public static void program(){
		while (token != null){
			statement();
			matchToken("SEMICO");
		}
	}

	public static void statement(){
		switch(token.corrType){
			case "ORIGIN":
				originStatment();
				break;
			case "SCALE":
				scaleStatment();
				break;
			case "ROT":
				rotStatement();
				break;
			case "FOR":
				forStatement();
				break;
		}
	}

	public static void originStatment(){
		ExprNode x, y;
		double dx, dy;

		matchToken("ORIGIN");
		matchToken("IS");
		matchToken("L_BRACKET");
		x = expression();
		dx = semantic.getExprValue(x);

		printSyntaxTree(x, 0);

		matchToken("COMMA");
		y = expression();
		dy = semantic.getExprValue(y);
		printSyntaxTree(y, 0);
		matchToken("R_BRACKET");

		semantic.setOrigin(dx, dy);
	}

	public static void scaleStatment(){
		ExprNode x, y;
		double dx, dy;

		matchToken("SCALE");
		matchToken("IS");
		matchToken("L_BRACKET");
		x = expression();
		dx = semantic.getExprValue(x);
		printSyntaxTree(x, 0);

		matchToken("COMMA");
		y = expression();
		dy = semantic.getExprValue(y);
		printSyntaxTree(y, 0);

		matchToken("R_BRACKET");
		semantic.setScale(dx, dy);
	}

	public static void rotStatement(){
		ExprNode rot;
		double drot;

		matchToken("ROT");
		matchToken("IS");
		rot = expression();
		drot = semantic.getExprValue(rot);
		printSyntaxTree(rot, 0);

		semantic.setRot(drot);
	}

	public static void forStatement(){
		ExprNode fromPtr, toPtr, stepPtr, x, y;
		double start, end, step;

		matchToken("FOR");
		matchToken("T");
		matchToken("FROM");
		fromPtr = expression();
		start = semantic.getExprValue(fromPtr);
		printSyntaxTree(fromPtr, 0);

		matchToken("TO");
		toPtr = expression();
		end = semantic.getExprValue(toPtr);
		printSyntaxTree(toPtr, 0);

		matchToken("STEP");
		stepPtr = expression();
		step = semantic.getExprValue(stepPtr);
		printSyntaxTree(stepPtr, 0);

		matchToken("DRAW");
		matchToken("L_BRACKET");
		x = expression();
		printSyntaxTree(x, 0);

		matchToken("COMMA");
		y = expression();
		printSyntaxTree(y, 0);

		matchToken("R_BRACKET");

		semantic.collectShape(start, end, step, x, y);
	}

	public static ExprNode expression(){
		ExprNode left;
		ExprNode right;
		ExprNode newLeft;
		TokenTab tokenTmp;

		System.out.println("#Enter expression#");
		
		left = term();
		while (token.corrType == "PLUS" || token.corrType == "MINUS"){
			tokenTmp = token;
			matchToken(tokenTmp.corrType);
			right = term();
			newLeft = new CaseOperator(tokenTmp);
			((CaseOperator) newLeft).setLeftRight(left, right);
			left = newLeft;
		}
		System.out.println("#Exit expression#");
		
		return left;
	}
                                         
	public static ExprNode term(){
		ExprNode left;
		ExprNode right;
		ExprNode newLeft;
		TokenTab tokenTmp;

		System.out.println("#Enter term#");
		
		left = factor();
		while (token.corrType == "MUL" || token.corrType == "DIV"){
			tokenTmp = token;
			matchToken(tokenTmp.corrType);
			right = factor();
			newLeft = new CaseOperator(tokenTmp);
			((CaseOperator) newLeft).setLeftRight(left, right);
			left = newLeft;
		}
		System.out.println("#Exit term#");
		
		return left;
	}

	public static ExprNode factor(){
		ExprNode left;
		ExprNode right;
		TokenTab tokenTmp;
		ExprNode node;

		System.out.println("#Enter factor#");
		
		if (token.corrType == "PLUS" || token.corrType == "MINUS"){
			tokenTmp = token;
			left = new CaseConst(new TokenTab("CONST_ID", "0", 0.0, null));
			matchToken(tokenTmp.corrType);
			right = factor();
			node = new CaseOperator(tokenTmp);
			((CaseOperator) node).setLeftRight(left, right);
		}else{
			node = component();
		}
		System.out.println("#Exit factor#");

		return node;
	}

	public static ExprNode component(){
		ExprNode left;
		ExprNode right;
		ExprNode newLeft;
		TokenTab tokenTmp;

		System.out.println("#Enter component#");
		
		left = atom();
		while (token.corrType == "POWER"){
			tokenTmp = token;
			matchToken(tokenTmp.corrType);
			right = component();
			newLeft = new CaseOperator(tokenTmp);
			((CaseOperator) newLeft).setLeftRight(left, right);
			left = newLeft;
		}
		System.out.println("#Exit component#");
		
		return left;
	}

	public static ExprNode atom(){
		ExprNode child;
		ExprNode node = null;
		TokenTab tokenTmp;

		System.out.println("#Enter atom#");
		
		tokenTmp = token;
		switch(tokenTmp.corrType){
			case "CONST_ID":
				matchToken(tokenTmp.corrType);
				node = new CaseConst(tokenTmp);
				break;
			case "T":
				matchToken(tokenTmp.corrType);
				node = new CaseParmPtr(tokenTmp, tokenTmp.pointer);
				break;
			case "FUNC":
				matchToken(tokenTmp.corrType);
				matchToken("L_BRACKET");
				child = expression();
				matchToken("R_BRACKET");
				node = new CaseFunc(tokenTmp, tokenTmp.pointer, child);
				break;
			case "L_BRACKET":
				matchToken(tokenTmp.corrType);
				node = expression();
				matchToken("R_BRACKET");
				break;
		}
		System.out.println("#Exit atom#");
		
		return node;
	}

	public static boolean initWordAnalyse(){
		scanner = new WordAnalyse("D:\\Trashes\\code_wrongword.txt");
		symbleList = scanner.result;

		if (symbleList != null){
			return true;

		}else{
			return false;
		}
	}

	public static boolean initSemantic(){
		semantic = new Semantic();
		if (semantic == null){
			return false;
		}else{
			return true;
		}
	}

	public void getToken(){
		if (symbleCount < symbleList.size()){
			token = symbleList.get(symbleCount);
			//symbleCount++;
		}
	}

	public static void matchToken(String symble){		
		if (token.corrType == symble){
			System.out.println("#match token: " + token.oriInput + "#");
			symbleCount++;
			if (symbleCount < symbleList.size()){
				token = symbleList.get(symbleCount);
			}else{
				token = null;
			}
		}else{
			System.out.println("Error in line " + token.lineNumber + ": Unexpected token '" + token.oriInput + "'.");
			System.exit(-1);
		}
	}

	public static void printSyntaxTree(ExprNode root, int indent){
		String before = "";
		int indentOfThis = indent;
		ExprNode rootOfThis;
		//CaseConst rootConst;
		CaseOperator rootOperator;
		CaseFunc rootFunc;
		//CaseParmPtr rootPtr;
		
		rootOfThis = root;
				
		for (int i = 0; i < indent; i++){
			before += "\t";
		}
		
		System.out.println(before + rootOfThis.oriInput);
		indentOfThis++;
		
		switch(rootOfThis.tokenType){
		case "CONST_ID":
			//rootConst = (CaseConst)rootOfThis;
			break;
		case "T":
			//rootPtr = (CaseParmPtr)rootOfThis;
			break;
		case "FUNC":
			rootFunc = (CaseFunc)rootOfThis;
			printSyntaxTree(rootFunc.child, indentOfThis);
			break;
		default:
			rootOperator = (CaseOperator)rootOfThis;
			printSyntaxTree(rootOperator.left, indentOfThis);
			printSyntaxTree(rootOperator.right, indentOfThis);
			break;
		}
	}

	public Parser(){
		if (initWordAnalyse()){
			if (initSemantic()){
				getToken();
				program();	
			}else{
				System.out.println("Error: Open sourse file faild (01).");
			}
	    }else{
	    	System.out.println("Error: Open sourse file faild (02).");
	    }
	}

	public static void main(String[] arg){
		new Parser();
		//System.out.println(semantic.shapeList);
		new StartFrame(semantic);
	}
}