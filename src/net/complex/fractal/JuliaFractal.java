/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.Complex;

public class JuliaFractal extends Fractal
{
	Complex c;

	public JuliaFractal(Complex c)
	{
		this.c = c;
	}
	
	public JuliaFractal()
	{
		this(new Complex(-1.25, 0));
	}

	public Complex getC()
	{
		return c;
	}

	public void setC(Complex c)
	{
		this.c = c;
	}
	
	@Override
	public Complex calcIteration(Complex z, Complex pos)
	{
		return z.sqr().add(c);
	}

	@Override
	public Complex getStartingPoint(Complex pos)
	{
		return pos;
	}

}
