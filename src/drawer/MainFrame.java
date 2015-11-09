package drawer;

import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.JFrame;

import function.Function;
import function.InfixFunction;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private MainPanel panel;
	
	public MainFrame() {
		Function func = null;
		String expr = "x*x - y*y";
		try {
			func = new InfixFunction(expr);
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
		
		panel = new MainPanel(800, 600);
		panel.setFunction(func);
		panel.renderColorMap();
		panel.renderContourLines();
		
		setTitle("Function Drawer");
		add(panel);
		pack();
		
		setResizable(false);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame editor = new MainFrame();
				editor.setVisible(true);
			}
		});
	}
}
