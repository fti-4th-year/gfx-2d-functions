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
	
	Handle handler;
	
	JSpinner spinner;
	JButton rainbow;
	
	JCheckBox contour;
	JCheckBox color;
	JCheckBox grad;
	JCheckBox dither;
	
	public OptionPanel(Handle hnd) {
		handler = hnd;
		
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 1));
		
		JPanel numPanel = new JPanel();
		numPanel.setBackground(Color.WHITE);
		
		numPanel.add(new JLabel("Num:"));
		
		spinner = new JSpinner(new SpinnerNumberModel(8, 2, 64, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				handler.setLevelCount(((Integer) spinner.getValue()).intValue());
				handler.update();
			}
		});
		numPanel.add(spinner);
		
		rainbow = new JButton("Rainbow");
		rainbow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.setLevelCount(7);
				spinner.setValue(new Integer(7));
				Context c = handler.getContext();
				c.colors[0] = 0xFF0000;
				c.colors[1] = 0xFFFF00;
				c.colors[2] = 0x00FF00;
				c.colors[3] = 0x00FFFF;
				c.colors[4] = 0x0000FF;
				c.colors[5] = 0xFF00FF;
				c.colors[6] = 0xFF0000;
				handler.update();
			}
		});
		numPanel.add(rainbow);
		
		add(numPanel);
		
		JPanel optPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		optPanel.setBackground(Color.WHITE);
		
		contour = new JCheckBox("Contour", true);
		contour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.getContext().flagContour = contour.isSelected();
				handler.update();
			}
		});
		optPanel.add(contour);
		
		color = new JCheckBox("Color", true);
		color.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.getContext().flagColor = color.isSelected();
				handler.update();
			}
		});
		optPanel.add(color);
		
		grad = new JCheckBox("Gradient");
		grad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.getContext().flagGrad = grad.isSelected();
				handler.update();
			}
		});
		optPanel.add(grad);
		
		dither = new JCheckBox("Dither");
		dither.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.getContext().flagDither = dither.isSelected();
				handler.update();
			}
		});
		optPanel.add(dither);
		
		add(optPanel);
	}
}
