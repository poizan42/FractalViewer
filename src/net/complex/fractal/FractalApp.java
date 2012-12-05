/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import javax.swing.*;

public class FractalApp
{
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Fraktal!");
		FractalAppPanel appPanel = new FractalAppPanel();
		window.add(appPanel);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		window.setSize(500,580);
		window.setVisible(true);
	}

}
