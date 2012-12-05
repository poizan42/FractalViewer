/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import net.complex.*;

public abstract class Fractal
{
	public boolean hasEscaped(Complex z)
	{
		return z.mod() > 2;
	}
	
	public abstract Complex getStartingPoint(Complex pos);
	
	public abstract Complex calcIteration(Complex z, Complex pos);
}
