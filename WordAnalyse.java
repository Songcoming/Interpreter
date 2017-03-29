import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.lang.Math;

public class WordAnalyse{
	public static String[] tokenType = {
		/**********************************
		 * 该语言的全部关键字，即符号的类型 *
		 **********************************/
		"ORIGIN", "SCALE", "ROT", "IS","TO", "STEP", "DRAW","FOR", "FROM", 
		"T",
		"SEMICO", "L_BRACKET", "R_BRACKET", "COMMA",
		"PLUS", "MINUS", "MUL", "DIV", "POWER",
		"FUNC",	
		"CONST_ID",	
		"NONTOKEN",
		"ERRTOKEN"
	};
	public static String[] typeKey = {
		/******************************
		 *需要正则表达式匹配的类型名称*
		 ******************************/
		"COMMENT",     "WHITE_SPACE",  "SEMICO",  "L_BRACKET",
		"R_BRACKET",   "COMMA",        "PLUS",    "MINUS",
		"MUL",         "DIV",          "POWER",   "CONST_ID",
		"ID"
	};
	public static String[] typeValue = {
		/******************
		 *对应的正则表达式*
		 ******************/
		"[\\/-]{2}",  "\\s",    ";",        "\\(",
		"\\)",        ",",      "\\+",      "-",
		"\\*",        "\\/",    "\\*{2}",   "\\d+((.)\\d+)?",
		"[a-zA-Z]+"
	};


	public static TokenTab[] dictionary = {
		/******************************
		 *匹配成ID的关键字中的特殊情况*
		 ******************************/
		new TokenTab("CONST_ID",        "PI"      ,     3.1415926,  null),
		new TokenTab("CONST_ID",	    "E"       ,		2.71828,	null),		
		new TokenTab("FUNC"    ,		"SIN"     ,		0.0,		new ComputeFunc("SIN")),
		new TokenTab("FUNC"    ,		"COS"     ,		0.0,		new ComputeFunc("COS")),
		new TokenTab("FUNC"    ,		"TAN"     ,		0.0,		new ComputeFunc("TAN")),
		new TokenTab("FUNC"    ,		"LN"      ,		0.0,		new ComputeFunc("LN")),
		new TokenTab("FUNC"    ,		"EXP"     ,		0.0,		new ComputeFunc("EXP")),
		new TokenTab("FUNC"    ,		"SQRT"    ,		0.0,		new ComputeFunc("SQRT")),
		new TokenTab("ORIGIN"  ,	    "ORIGIN"  ,	    0.0,		null), 
		new TokenTab("SCALE"   ,		"SCALE"   ,	    0.0,		null),
		new TokenTab("ROT"     ,		"ROT"     ,		0.0,		null),
		new TokenTab("IS"      ,		"IS"      ,		0.0,		null),
		new TokenTab("FOR"     ,		"FOR"     ,		0.0,		null),
		new TokenTab("FROM"    ,		"FROM"    ,		0.0,		null),
		new TokenTab("TO"      ,		"TO"      ,		0.0,		null),
		new TokenTab("T"       ,		"T"       ,		0.0,		new ComputeFunc()),
		new TokenTab("STEP"    ,		"STEP"    ,		0.0,		null),
		new TokenTab("DRAW"    ,		"DRAW"    ,		0.0,		null),
	};

	static LinkedList<TokenTab> result = new LinkedList();

	public static boolean isAlpha(char a){
		/**
		 * @param a 待检测字符
		 * @return boolean value 检查结果
		 */

		if ((a >= 'a' && a <= 'z') || (a >='A' && a <= 'Z')){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isNumber(char a){
		/**
		 * @param a 待检测字符
		 * @return boolean value 检查结果
		 */

		if ((a >= '0' && a <= '9') || a == '.'){
			return true;
		}else{
			return false;
		}
	}

	public static String matchReExp(String inputStr){
		/**
		 * @param inputStr 待检测字符串
		 * @return typeKey 得到的大致类型
		 */

		int i;
		int flag = 0;
		
		for (i = 0; i < typeValue.length; i++){
			Pattern reg = Pattern.compile(typeValue[i]);//对每种类型进行正则匹配
			Matcher mtc = reg.matcher(inputStr);
			if (mtc.matches()){//如果匹配成功
				flag = 1;//设置标记
				break;
			}
		}		
		if (flag == 0){
			System.out.println("Bad symble!");
			return "";
		}else{
			return typeKey[i];
		}
	}

	public static void metCorrectType(String inputId, int lineNumber){
		/**
		 * @param inputId 已验证是ID类型的字符串
		 * @param lineNumber 文件中该行的行号
		 */
		Pattern reg = null;
		Matcher mtc = null;
		TokenTab tab = null;
		int flag = 0;

		/*OutputStreamWriter write = new OutputStreamWriter(
					new FileOutputStream(f));
				BufferedWriter bfw = new BufferedWriter(write);*/

		for (TokenTab c : dictionary){ //遍历ID中的特殊情况
			reg = Pattern.compile(c.oriInput);//正则匹配
			mtc = reg.matcher(inputId);

			if (mtc.lookingAt()){ //如果在字符串中发现了能匹配的串
				c.addLineNumber(lineNumber);//为对象的行号属性赋值
				System.out.println(c.corrType + '\t' + '"' + c.oriInput + '"' + '\t' + c.constValue + '\t'+ c.pointer + '\t' + c.lineNumber);
				result.add(c);
				//bfw.write(c.corrType + '\t' + '"' + c.oriInput + '"' + '\t' + c.constValue + '\t'+ c.pointer + '\t' + c.lineNumber);
				flag = 1;
				inputId = mtc.replaceFirst(""); //将已经匹配的部分消去
				break;
			}
		}

		if (flag == 0){//如果没有匹配上，则为真正的id类型
			System.out.println("Error in line " + lineNumber + ": Unexpected token.");
			//tab = new TokenTab("ID", inputId.substring(mtc.start(), mtc.end()), 0.0, null);
			//tab.addLineNumber(lineNumber);
			//System.out.println(tab.corrType + '\t' + '"' + tab.oriInput + '"' + '\t' + tab.constValue + '\t'+ tab.pointer + '\t' + tab.lineNumber);
			//result.add(tab);
			//return tab;
		}
		if ((!inputId.equals("")) && flag == 1){//如果匹配过后，字符串中还有未匹配的串，则递归，继续进行匹配
			metCorrectType(inputId, lineNumber);
		}
	}

	public static void outputSymb(String inputTab, String oriInput, int lineNumber){
		/**
		 * @param inputTab 输入符号的类型
		 * @param oriInput 输入符号
		 * @param lineNumber 行号
		 */
		TokenTab tab = null;
		switch(inputTab){
			case "COMMENT":
				tab = new TokenTab("COMMENT",  oriInput,  0.0,  null);				
				break;
			case "WHITE_SPACE":
				tab = new TokenTab("WHITE_SPACE",  oriInput,  0.0,  null);
				break;
			case "SEMICO":
				tab = new TokenTab("SEMICO",  oriInput,  0.0,  null);
				break;
			case "L_BRACKET":
				tab = new TokenTab("L_BRACKET",  oriInput,  0.0,  null);
				break;
			case "R_BRACKET":
				tab = new TokenTab("R_BRACKET",  oriInput,  0.0,  null);
				break;
			case "COMMA":
				tab = new TokenTab("COMMA",  oriInput,  0.0,  null);
				break;
			case "PLUS":
				tab = new TokenTab("PLUS",  oriInput,  0.0,  null);
				break;
			case "MINUS":
				tab = new TokenTab("MINUS",  oriInput,  0.0,  null);
				break;
			case "MUL":
				tab = new TokenTab("MUL",  oriInput,  0.0,  null);
				break;
			case "DIV":
				tab = new TokenTab("DIV",  oriInput,  0.0,  null);
				break;
			case "POWER":
				tab = new TokenTab("POWER",  oriInput,  0.0,  null);
				break;
			case "CONST_ID":
				tab = new TokenTab("CONST_ID",  oriInput,  0.0,  null);
				break;
			case "ID":
				tab = new TokenTab("ID",  oriInput,  0.0,  null);
				break;
		}
		tab.addLineNumber(lineNumber);
		System.out.println(tab.corrType + '\t' + '"' + tab.oriInput + '"' + '\t' + tab.constValue + '\t'+ tab.pointer + '\t' + tab.lineNumber);
		result.add(tab);
	}

	public static void readAndWrite(String filePath){
		/**
		 * @param filePath 文件路径
		 */
		File f = new File(filePath);
		char[] codeArray = null;
		String codeTmp = "";
		String s;
		String code;
		TokenTab tab;
		int lineNumber = 0;

		try{
			if (f.isFile() && f.exists()){
				InputStreamReader read = new InputStreamReader(
					new FileInputStream(f));
				BufferedReader bfr = new BufferedReader(read);
				while((code = bfr.readLine()) != null){ //按行读取
					lineNumber++; //行号自增
					Pattern reg = Pattern.compile("\\s"); //去掉空格
					Matcher mtc = reg.matcher(code);
					code = mtc.replaceAll("");

					codeArray = code.toCharArray();
					int i = 0;
					while (i < codeArray.length){
						codeTmp = "";
						if (isAlpha(codeArray[i])){ //检测是否为字母
							do{
								codeTmp += codeArray[i]; //将所有相连的字母提取出，直到下一位不再是字母
								i++;
							}while(isAlpha(codeArray[i]));
						}else if(isNumber(codeArray[i])){ //检测是否为数字
							do{
								codeTmp += codeArray[i];//将所有相连的数字和小数点提取出，直到下一位不再是数字或小数点
								i++;
							}while (isNumber(codeArray[i]));
						}else if (codeArray[i] == '*' && codeArray[i + 1] == '*'){ //以下为连续两位相同的情况
							codeTmp = "**";
							i += 2;
						}else if (codeArray[i] == '-' && codeArray[i + 1] == '-'){
							codeTmp = "--";
							i += 2;
						}else if (codeArray[i] == '/' && codeArray[i + 1] == '/'){
							codeTmp = "//";
							i += 2;
						}else{
							codeTmp += codeArray[i];
							i++;
						}

						if ((s = matchReExp(codeTmp)).equals("ID"))	{//如果初步类型为ID
							metCorrectType(codeTmp, lineNumber); //进行分类细化
						}else if (s.equals("CONST_ID")){//如果初步类型是常量
							tab = new TokenTab("CONST_ID", codeTmp, Double.parseDouble(codeTmp), null);
							tab.addLineNumber(lineNumber);
							result.add(tab);
							System.out.println(tab.corrType + '\t' + '"' + tab.oriInput + '"' + '\t' + tab.constValue + '\t'+ tab.pointer + '\t' + tab.lineNumber);
						}else{ //如果初步类型是各种符号
							outputSymb(s, codeTmp, lineNumber);
						}											
					}
				}			
			}else{
				System.out.println("Error: Cannot find this files.")
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public WordAnalyse(String fileP){
		readAndWrite(fileP);
	}
	
	/*public static void main(String[] args){
		readAndWrite("D:\\Trashes\\codes.txt");
	}*/
}