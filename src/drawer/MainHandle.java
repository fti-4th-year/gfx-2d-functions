package drawer;

import java.awt.Color;
import java.awt.Font;
import java.text.ParseException;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import function.Function;
import function.InfixFunction;

public class MainHandle implements Handle {
	
	private Function function;
	private Context context;
	
	private JFrame frame;
	private Updateable scene;
	
	public MainHandle(JFrame f, Updateable s) {
		frame = f;
		scene = s;
	}
	
	@Override
	public Context getContext() {
		return context;
	}
	
	@Override
	public void setContext(Context ctx) {
		context = ctx;
	}
	
	@Override
	public Function getFunction() {
		return function;
	}
	
	@Override
	public void setFunction(Function f) {
		function = f;
	}

	@Override
	public void setFunction(String expr) {
		try {
			setFunction(new InfixFunction(expr));
		} catch (ParseException e) {
			String msg;
			JLabel[] labels = new JLabel[3];
			
			msg = e.getMessage() + ":";
			labels[0] = new JLabel(msg);
			
			msg = expr;
			labels[1] = new JLabel(msg);
			
			msg = "";
			int offset = e.getErrorOffset() + 1;
			for(int i = 0; i < expr.length(); ++i) {
				if(!Character.isWhitespace(expr.charAt(i))) {
					--offset;
					if(offset <= 0)
						break;
				}
				msg += '~';
			}
			msg += '^';
			labels[2] = new JLabel(msg);
			
			Font font = new Font(Font.MONOSPACED, Font.PLAIN, 16);
			labels[0].setFont(font);
			labels[1].setFont(font);
			labels[2].setFont(font);
			
			JOptionPane.showMessageDialog(frame, labels, "Parse error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setColor(int n, int color) {
		context.colors[n] = color;
	}
	
	public void selectColor(int n) {
		Color color = JColorChooser.showDialog(frame, "Choose color", new Color(context.colors[n]));
		if(color != null) {
			context.colors[n] = color.getRGB();
		}
	}
	
	public void setLevelCount(int n) {
		context.setNum(n);
	}
	
	public void showAbout() {
		JLabel[] labels = new JLabel[7];
		labels[0] = new JLabel("Use mouse wheel to zoom.");
		labels[1] = new JLabel("Click on a legeng to select color.");
		labels[2] = new JLabel("Enter function in infix form and press 'Enter'.");
		labels[3] = new JLabel("You can use: ");
		labels[4] = new JLabel("variables: x, y");
		labels[5] = new JLabel("operators: '+', '-', '*', '/', '^'");
		labels[6] = new JLabel("functions: sin, cos, tan, asin, acos, atan, exp, sinh, cosh, tanh, log");
		JOptionPane.showMessageDialog(frame, labels, "About", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void update() {
		scene.update();
	}
}
