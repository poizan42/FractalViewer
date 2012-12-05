/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.Complex;

public class MandelbrotFractal extends Fractal
{
	private Complex startingPoint;
	
	public MandelbrotFractal(Complex startingPoint)
	{
		this.startingPoint = startingPoint;
	}
	
	public MandelbrotFractal()
	{
		this(new Complex(0, 0));
	}

	public Complex getStartingPoint()
	{
		return startingPoint.clone();
	}

	public void setStartingPoint(Complex startingPoint)
	{
		this.startingPoint = startingPoint;
	}

	/* mandelbrot
	 * f(z) = f(z)^2 + pos
	 * start = 0
	 */
	
	@Override
	public Complex calcIteration(Complex z, Complex pos)
	{
		return z.sqr().add(pos);
	}

	@Override
	public Complex getStartingPoint(Complex pos)
	{
		return startingPoint.clone();
	}

}
