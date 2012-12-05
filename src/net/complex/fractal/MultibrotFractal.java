/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.Complex;

public class MultibrotFractal extends Fractal
{
	private Complex startingPoint, power;
	
	public MultibrotFractal(Complex startingPoint, Complex power)
	{
		this.startingPoint = startingPoint;
		this.power = power;
	}
	
	public MultibrotFractal()
	{
		this(new Complex(0, 0), new Complex(2, 0));
	}

	public Complex getStartingPoint()
	{
		return startingPoint;
	}

	public void setStartingPoint(Complex startingPoint)
	{
		this.startingPoint = startingPoint;
	}
	
	public Complex getPower()
	{
		return power;
	}

	public void setPower(Complex power)
	{
		this.power = power;
	}

	/* mandelbrot
	 * f(z) = f(z)^2 + pos
	 * start = 0
	 */
	@Override
	public Complex calcIteration(Complex z, Complex pos)
	{
		return z.power(power).add(pos);
	}

	@Override
	public Complex getStartingPoint(Complex pos)
	{
		return startingPoint;
	}

}
