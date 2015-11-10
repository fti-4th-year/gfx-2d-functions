package drawer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.JFrame;

import function.Function;
import function.InfixFunction;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private MainPanel mainPanel;
	private SidePanel sidePanel;
	private TopPanel topPanel;
	
	private MainHandle handle;
	
	public MainFrame() {
		Function func = null;
		try {
			func = new InfixFunction("x*x - y*y");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		mainPanel = new MainPanel(600, 600);
		mainPanel.setFunction(func);
		
		handle = new MainHandle(this, mainPanel);
		
		sidePanel = new SidePanel(200);
		
		topPanel = new TopPanel(handle, 24);
		
		setTitle("Function Drawer");
		add(mainPanel, BorderLayout.CENTER);
		add(sidePanel, BorderLayout.LINE_END);
		add(topPanel, BorderLayout.PAGE_START);
		pack();
		
		setMinimumSize(new Dimension(480, 320));
		
		// setResizable(false);
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
