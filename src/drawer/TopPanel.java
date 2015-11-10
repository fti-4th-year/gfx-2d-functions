package drawer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TopPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Handle handle;
	
	JLabel label;
	JTextField textField;
	JButton button;
	
	public TopPanel(Handle hnd, int h) {
		handle = hnd;
		
		setPreferredSize(new Dimension(100, h));
		
		setLayout(new BorderLayout(4, 4));
		
		ActionListener apply = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.setFunction(textField.getText());
			}
		};
		
		label = new JLabel("Function:");
		add(label, BorderLayout.LINE_START);
		
		textField = new JTextField("x*x - y*y");
		textField.addActionListener(apply);
		add(textField, BorderLayout.CENTER);
		
		button = new JButton("Apply");
		button.addActionListener(apply);
		add(button, BorderLayout.LINE_END);
	}
}
