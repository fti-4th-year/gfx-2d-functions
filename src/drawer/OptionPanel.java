package drawer;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Handle handle;
	
	JButton about;
	JSpinner levels;
	JSpinner gridNumX, gridNumY;
	
	JCheckBox contour;
	JCheckBox cursor;
	JCheckBox grid;
	JCheckBox color;
	JCheckBox gradient;
	JCheckBox dither;
	
	JButton rainbow;
	
	public OptionPanel(Handle hnd) {
		handle = hnd;
		
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 1));
		
		JPanel numPanel = new JPanel();
		numPanel.setBackground(Color.WHITE);
		
		about = new JButton("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.showAbout();
			}
		});
		numPanel.add(about);
		
		numPanel.add(new JLabel("Levels:"));
		levels = new JSpinner(new SpinnerNumberModel(handle.getContext().num, 2, 64, 1));
		levels.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				handle.setLevelCount(((Integer) levels.getValue()).intValue());
				handle.update();
			}
		});
		numPanel.add(levels);
		
		numPanel.add(new JLabel("Grid size:"));
		gridNumX = new JSpinner(new SpinnerNumberModel(handle.getContext().grid.numX, 1, 256, 1));
		gridNumX.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				handle.getContext().grid.numX = (((Integer) gridNumX.getValue()).intValue());
				handle.update();
			}
		});
		numPanel.add(gridNumX);
		gridNumY = new JSpinner(new SpinnerNumberModel(handle.getContext().grid.numY, 1, 256, 1));
		gridNumY.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				handle.getContext().grid.numY = (((Integer) gridNumY.getValue()).intValue());
				handle.update();
			}
		});
		numPanel.add(gridNumY);
		
		add(numPanel);
		
		JPanel optPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		optPanel.setBackground(Color.WHITE);
		
		optPanel.add(new JLabel("Options:"));
		
		contour = new JCheckBox("Contour", true);
		contour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.contour = contour.isSelected();
				handle.update();
			}
		});
		optPanel.add(contour);
		
		cursor = new JCheckBox("Cursor", true);
		cursor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.cursor = cursor.isSelected();
				handle.update();
			}
		});
		optPanel.add(cursor);
		
		grid = new JCheckBox("Grid");
		grid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.grid = grid.isSelected();
				handle.update();
			}
		});
		optPanel.add(grid);
		
		color = new JCheckBox("Color", true);
		color.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.color = color.isSelected();
				handle.update();
			}
		});
		optPanel.add(color);
		
		gradient = new JCheckBox("Gradient");
		gradient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.gradient = gradient.isSelected();
				handle.update();
			}
		});
		optPanel.add(gradient);
		
		dither = new JCheckBox("Dither");
		dither.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.getContext().flags.dither = dither.isSelected();
				handle.update();
			}
		});
		optPanel.add(dither);
		
		add(optPanel);
		
		JPanel effects = new JPanel(new FlowLayout(FlowLayout.LEADING));
		effects.setBackground(Color.WHITE);
		
		effects.add(new JLabel("Effects:"));
		
		rainbow = new JButton("Rainbow");
		rainbow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle.setLevelCount(7);
				levels.setValue(new Integer(7));
				Context c = handle.getContext();
				c.colors[0] = 0xFF0000;
				c.colors[1] = 0xFFFF00;
				c.colors[2] = 0x00FF00;
				c.colors[3] = 0x00FFFF;
				c.colors[4] = 0x0000FF;
				c.colors[5] = 0xFF00FF;
				c.colors[6] = 0xFF0000;
				handle.update();
			}
		});
		effects.add(rainbow);
		
		add(effects);
	}
}
