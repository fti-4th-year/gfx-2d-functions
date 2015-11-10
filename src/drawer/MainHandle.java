package drawer;

import java.awt.Font;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import function.Function;
import function.InfixFunction;

public class MainHandle implements Handle {
	
	private JFrame frame;
	private MainPanel mainPanel;
	
	public MainHandle(JFrame frame, MainPanel mp) {
		mainPanel = mp;
	}
	
	@Override
	public void setFunction(Function f) {
		mainPanel.setFunction(f);
		mainPanel.update();
		mainPanel.repaint();
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
			
			JOptionPane.showMessageDialog(frame, labels);
		}
	}
}
