package drawer;

import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.JFrame;

import function.InfixFunction;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private MainPanel panel;
	
	public MainFrame() {
		
		panel = new MainPanel(800, 600);
		
		setTitle("Function Drawer");
		add(panel);
		pack();
		
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		String expr = "(1 + 1";
		try {
			System.out.println("f(0,0) = " + new InfixFunction(expr).evaluate(0.0, 0.0));
		} catch (ParseException e) {
			System.err.println(e.getMessage() + ":");
			System.err.println(expr);
			int offset = e.getErrorOffset() + 1;
			for(int i = 0; i < expr.length(); ++i) {
				if(!Character.isWhitespace(expr.charAt(i))) {
					--offset;
					if(offset <= 0)
						break;
				}
				System.err.print('~');
			}
			System.err.println('^');
		}
		/*
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame editor = new MainFrame();
				editor.setVisible(true);
			}
		});
		*/
	}
}
