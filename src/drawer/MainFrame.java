package drawer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class MainFrame extends JFrame implements Updateable {
	
	private static final long serialVersionUID = 1L;
	
	private MainPanel mainPanel;
	private SidePanel sidePanel;
	private TopPanel topPanel;
	
	private MainHandle handle;
	
	public MainFrame() throws Exception {
		handle = new MainHandle(this, this);
		handle.setFunction("sin(x)*sin(y)");
		handle.setContext(new Context(16));
		
		mainPanel = new MainPanel(handle, 600, 600);
		sidePanel = new SidePanel(handle, 200);
		topPanel = new TopPanel(handle, 24);
		
		add(mainPanel, BorderLayout.CENTER);
		add(sidePanel, BorderLayout.LINE_END);
		add(topPanel, BorderLayout.PAGE_START);
		pack();
		
		setTitle("Function Drawer");
		setMinimumSize(new Dimension(480, 320));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame editor = null;
				try {
					editor = new MainFrame();
					editor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void redraw() {
		mainPanel.redraw();
		sidePanel.redraw();
	}
	
	@Override
	public void update() {
		mainPanel.update();
		sidePanel.update();
	}
}
