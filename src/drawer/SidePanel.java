package drawer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class SidePanel extends JPanel implements Updateable {
	private static final long serialVersionUID = 1L;
	
	Handle handle;
	Legend legend;
	
	public SidePanel(Handle hnd, int w) {
		handle = hnd;
		
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(w, 100));
		setLayout(new BorderLayout());
		
		legend = new Legend(handle, 100);
		add(legend, BorderLayout.LINE_END);
		
		add(new OptionPanel(handle), BorderLayout.CENTER);
	}
	
	@Override
	public void redraw() {
		legend.redraw();
	}
	
	@Override
	public void update() {
		legend.update();
	}
}
