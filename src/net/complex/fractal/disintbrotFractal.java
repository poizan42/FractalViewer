/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.Complex;

public class DisintbrotFractal extends Fractal
{
	private Complex startingPoint, power;
	
	public DisintbrotFractal(Complex startingPoint, Complex power)
	{
		this.startingPoint = startingPoint;
		this.power = power;
	}
	
	public DisintbrotFractal()
	{
		this(new Complex(0, 0), new Complex(2, 0));
	}

	public Complex getStartingPoint()
	{
		return startingPoint.clone();
	}

	public void setStartingPoint(Complex startingPoint)
	{
		this.startingPoint = startingPoint;
	}
	
	public Complex getPower()
	{
		return power.clone();
	}

	public void setPower(Complex power)
	{
		this.power = power;
	}

	/*@Override
	public boolean hasEscaped(Complex z)
	{
		return z.mod() > 3;
	}*/
	
	/* disint-brot
	 * z = tan(z^@power)^@power + c
	 * start = 0
	 */
	@Override
	public Complex calcIteration(Complex z, Complex pos)
	{
		//return Complex.tan(z.power(power)).power(power).add(pos);
		if ((power.Re == 2) && (power.Im == 0))
			return z.sqrS().tanS().sqrS().addS(pos);
		else
			return z.powerS(power).tanS().powerS(power).addS(pos);
	}

	@Override
	public Complex getStartingPoint(Complex pos)
	{
		return startingPoint.clone();
	}

}
