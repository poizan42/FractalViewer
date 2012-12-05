/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class FractalAppPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private class OkBtnListener implements MouseListener
	{
		public void mouseClicked(MouseEvent arg0)
		{
			Complex newMinRange = new Complex(0,0);
			Complex newMaxRange = new Complex(0,0);
			newMinRange.Re = Double.parseDouble(minRangeReEdit.getText());
			newMinRange.Im = Double.parseDouble(minRangeImEdit.getText());
			newMaxRange.Re = Double.parseDouble(maxRangeReEdit.getText());
			newMaxRange.Im = Double.parseDouble(maxRangeImEdit.getText());
			fractalPanel.setRanges(newMinRange, newMaxRange);
		}

		public void mouseEntered(MouseEvent arg0){};
		public void mouseExited(MouseEvent arg0){};
		public void mousePressed(MouseEvent arg0){};
		public void mouseReleased(MouseEvent arg0){};
	}
	
	private OkBtnListener okBtnListener;
	private JPanel settingsBar, minRangePanel, maxRangePanel, lowerPart;
	private JTextField minRangeReEdit;
	private JTextField minRangeImEdit;
	private JTextField maxRangeReEdit;
	private JTextField maxRangeImEdit;
	private JButton OkBtn;
	private JPanel statusBar;
	private JLabel positionLabel;
	private JFrame window;
	private FractalPanel fractalPanel;
	
	public FractalAppPanel()
	{
		super();
		setLayout(new BorderLayout());
		fractalPanel = new FractalPanel();
		fractalPanel.addMouseListener(this);
		fractalPanel.addMouseMotionListener(this);
		add(fractalPanel, BorderLayout.CENTER);
		lowerPart = new JPanel();
		lowerPart.setLayout(new BorderLayout());
		add(lowerPart, BorderLayout.SOUTH);
		
		settingsBar = new JPanel();
		settingsBar.setLayout(new FlowLayout());
		settingsBar.setPreferredSize(new Dimension(0, 64));
		minRangePanel = new JPanel();
		minRangePanel.setLayout(new BoxLayout(minRangePanel, BoxLayout.Y_AXIS));
		JLabel minRangeLabel = new JLabel("Min range");
		minRangePanel.add(minRangeLabel);
		JPanel minRePanel = new JPanel();
		minRePanel.setLayout(new BoxLayout(minRePanel, BoxLayout.X_AXIS));
		minRangePanel.add(minRePanel);
		JLabel minReLabel = new JLabel("Re");
		minRePanel.add(minReLabel);
		minRangeReEdit = new JTextField();
		minRangeReEdit.setPreferredSize(new Dimension(150, 20));
		minRePanel.add(minRangeReEdit);
		
		JPanel minImPanel = new JPanel();
		minImPanel.setLayout(new BoxLayout(minImPanel, BoxLayout.X_AXIS));
		minRangePanel.add(minImPanel);
		JLabel minImLabel = new JLabel("Im");
		minImPanel.add(minImLabel);
		minRangeImEdit = new JTextField();
		minRangeImEdit.setPreferredSize(new Dimension(150, 20));
		minImPanel.add(minRangeImEdit);
		settingsBar.add(minRangePanel);
		
		maxRangePanel = new JPanel();
		maxRangePanel.setLayout(new BoxLayout(maxRangePanel, BoxLayout.Y_AXIS));
		JLabel maxRangeLabel = new JLabel("Max range");
		maxRangePanel.add(maxRangeLabel);
		maxRangeReEdit = new JTextField();
		maxRangeReEdit.setPreferredSize(new Dimension(150, 20));
		maxRangePanel.add(maxRangeReEdit);
		maxRangeImEdit = new JTextField();
		maxRangeImEdit.setPreferredSize(new Dimension(150, 20));
		maxRangePanel.add(maxRangeImEdit);
		settingsBar.add(maxRangePanel);
		OkBtn = new JButton("OK");
		OkBtn.setPreferredSize(new Dimension(75, 25));
		okBtnListener = new OkBtnListener();
		OkBtn.addMouseListener(okBtnListener);
		settingsBar.add(OkBtn);
		setMinMaxRangeText();
		lowerPart.add(settingsBar, BorderLayout.CENTER);

		statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		positionLabel = new JLabel();
		statusBar.add(positionLabel);
		lowerPart.add(statusBar, BorderLayout.SOUTH);
	}
	
	private int prec(double range, int p)
	{
		//	afrunder så hele området er delt op i >= 10^p og < 10^(p+1) værdipunkter
		//10   -> p-1 (log = 1)
		//1    -> p  (log = 0)
		//0.1  -> p+1 (log = -1)
		//0.05 -> p+2 (log = -1.3)
		//0.01 -> p+2 (log = -2)
		//p - floor(log10(range))
		return p - (int)Math.floor(Math.log10(range));
	}
	
	private String pointPosStr(int x, int y)
	{
		//afrunder så hele området er delt op i >= 1000 og < 10000 værdipunkter 
		int precX = prec(fractalPanel.getRangeWidth(), 3);
		int precY = prec(fractalPanel.getRangeHeight(), 3);
		return fractalPanel.pointToComplex(x, y).toString(precX, precY);
	}
	
	/*private String rangeLabelText()
	{
		int precX = prec(fractalPanel.getRangeWidth(), 2);
		int precY = prec(fractalPanel.getRangeHeight(), 2);
		String minRangeStr = fractalPanel.getMinRange().toString(precX, precY);
		String maxRangeStr = fractalPanel.getMaxRange().toString(precX, precY);
		return minRangeStr + " -> " + maxRangeStr;
	}*/
	
	private void setMinMaxRangeText()
	{
		minRangeReEdit.setText(Double.toString(fractalPanel.getMinRange().Re));
		minRangeImEdit.setText(Double.toString(fractalPanel.getMinRange().Im));
		maxRangeReEdit.setText(Double.toString(fractalPanel.getMaxRange().Re));
		maxRangeImEdit.setText(Double.toString(fractalPanel.getMaxRange().Im));
	}
	
	public void mouseDragged(MouseEvent ev)
	{
		if (!fractalPanel.isZooming())
		{
			mouseMoved(ev);
			return;
		}
		Rectangle dragRect = fractalPanel.getDragRect();
		// den mindste y værdi giver den største imaginære værdi, og vica versa 
		positionLabel.setText(pointPosStr(dragRect.x, dragRect.y + dragRect.height)+" -> "+
				pointPosStr(dragRect.x + dragRect.width, dragRect.y));
	}

	public void mouseMoved(MouseEvent ev)
	{
		positionLabel.setText(pointPosStr(ev.getX(), ev.getY()));
	}

	public void mouseClicked(MouseEvent arg0){};
	public void mouseEntered(MouseEvent arg0){};
	public void mouseExited(MouseEvent arg0){};
	public void mousePressed(MouseEvent arg0){};

	public void mouseReleased(MouseEvent ev)
	{
		setMinMaxRangeText();
		//rangeLabel.setText(rangeLabelText());
		//Rectangle dragRect = fractalPanel.getDragRect();
		//System.out.println("maxRange(FractalTest): "+pointPosStr(dragRect.x + dragRect.width, dragRect.y));
	}

	public JFrame getWindow()
	{
		return window;
	}
}
