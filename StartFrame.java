import javax.swing.*;

public class StartFrame extends JFrame{
	public StartFrame(Semantic semantic ){
		 this.getContentPane().add(new StartDraw(semantic));

		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setSize(600,400);
	     this.setVisible(true);
	}
	
	/*public static void main(String args){
		new StartFrame(Semantic semantic);
	}*/
}