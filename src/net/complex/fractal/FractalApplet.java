/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import javax.swing.JApplet;

public class FractalApplet extends JApplet
{
	FractalAppPanel fractalWindow;
	
	@Override
	public void init()
	{
		fractalWindow = new FractalAppPanel();
		add(fractalWindow);
	}
	
	/*@Override
	public void start()
	{
		
	}*/
}
