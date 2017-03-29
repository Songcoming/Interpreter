import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.lang.Math;

import javax.swing.*;

public class StartDraw extends JPanel{

	LinkedList<Shape> shapeToDraw;
	Semantic semantic;
	//double parameter = 0.0;
	double rot_ang   = 0.0;
	double origin_x  = 0.0, origin_y = 0.0;
	double scale_x   = 0.0,  scale_y = 0.0;

	public StartDraw(Semantic semanticFromP) {
		this.semantic = semanticFromP;
		//System.out.println(this.semantic);
		shapeToDraw = semantic.shapeList;
		//System.out.println(semantic.shapeList);
		rot_ang = semantic.getRot();
		origin_x = semantic.getOriginX();
		origin_y = semantic.getOriginY();
		scale_x = semantic.getScaleX();
		scale_y = semantic.getScaleY();

		setBackground(Color.white);		
	}

	public void start(){
		System.out.println("***Begin to draw***");
	}

	public void paintComponent(Graphics g){
		if (shapeToDraw != null){
			for (int i = 0; i < shapeToDraw.size(); i++){
				drawLoop(shapeToDraw.get(i), g);
			}
			//g.drawLine(10, 10, 10, 10);
		}
	}

	public PointAdress calcCoord(ExprNode x_nptr, ExprNode y_nptr){
		PointAdress pa = new PointAdress();
		double local_x, local_y, temp;
		local_x = semantic.getExprValue(x_nptr);
		local_y = semantic.getExprValue(y_nptr);
		local_x *= scale_x;
		local_y *= scale_y;

		temp = local_x * Math.cos(rot_ang) + local_y * Math.sin(rot_ang);
		local_y = local_y * Math.cos(rot_ang) - local_x * Math.sin(rot_ang);
		local_x = temp;

		local_x += origin_x;
		local_y += origin_y;
		pa.setX(local_x);
		pa.setY(local_y);

		return pa;
	}

	public void drawLoop(Shape shape, Graphics g){
		for (ComputeFunc.parameter = shape.start; ComputeFunc.parameter <= shape.end; ComputeFunc.parameter += shape.step){
			PointAdress pa = calcCoord(shape.x_ptr, shape.y_ptr);
			System.out.println(pa.x +","+ pa.y);
			g.drawLine((int)pa.x, (int)pa.y, (int)pa.x, (int)pa.y);
		}
	}
}

/*class StartFrame extends JFrame{
	StartFrame(){
		 this.getContentPane().add(new StartDraw());

		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setSize(600,400);
	     this.setVisible(true);
	}
}

class StartProgram{
	public static void main(String[] args){
		new StartFrame();
	}
}*/