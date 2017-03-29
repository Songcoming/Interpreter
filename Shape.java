public class Shape{
	double start = 0;
	double end = 0;
	double step = 0;
	ExprNode x_ptr = null;
	ExprNode y_ptr = null;

	public Shape(double start, double end, double step, ExprNode x_ptr, ExprNode y_ptr){
		this.start = start;
		this.end = end;
		this.step = step;
		this.x_ptr = x_ptr;
		this.y_ptr = y_ptr;
	}
}