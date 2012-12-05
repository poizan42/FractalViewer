/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex;

public class Complex
{
	public double Re, Im;
	public static final Complex zero = new Complex(0, 0);
	public static final Complex i = new Complex(0, 1);
	
	public Complex(double re, double im)
	{
		this.Re = re;
		this.Im = im;
	}
	
	@Override
	public Complex clone()
	{
		return new Complex(Re, Im);
	}
	
	public double mod()
	{
		return Math.sqrt(Re*Re+Im*Im);
	}
	
	public static double mod(Complex val)
	{
		return val.mod();
	}
	
	public double arg()
	{
		return Math.atan2(Im, Re);
	}

	public static double arg(Complex val)
	{
		return val.arg();
	}
	
	public Complex conjS()
	{
		Im = -Im;
		return this;
	}
	
	public Complex conj()
	{
		return clone().conjS();
	}
	
	public static Complex conj(Complex val)
	{
		return val.clone().conjS();
	}
	
	private String roundReal(double val, int precision)
	{
		double expval = Math.pow(10, precision);
		return Double.toString(((double)Math.round(val*expval))/expval);
	}
	
	//precision er antal decimaler
	public String toString(int precRe, int precIm)
	{
		return roundReal(Re, precRe)+(Im >= 0 ? " + " : " - ")+roundReal(Math.abs(Im), precIm)+"i";
	}
	
	public String toString(int precision)
	{
		return toString(precision, precision);	
	}
	
	@Override
	public String toString()
	{
		return Re+(Im >= 0 ? " + " : " - ")+Math.abs(Im)+"i";
	}

	// 2 +3.4i 
	// 5.01
	// 54i
	public static Complex parse(String val)
	{
		double r,i;
		// float = ([0-9]*\(.[0-9]*)?)
		// (+|-)?float((+|-)floati)?
		String s = val.replaceAll(" ", "");
		int lastPlus = s.lastIndexOf("+");
		int lastMinus = s.lastIndexOf("-");
		// Der er enten kun en reel eller kun en imaginær del
		if ((lastPlus <= 0) && (lastMinus <= 0))
		{
			if (s.charAt(s.length() -1) == 'i')
			{
				r = 0;
				i = Double.parseDouble(s.substring(0, s.length() -1));
			}
			else
			{
				r = Double.parseDouble(s);
				i = 0;
			}
		}
		// Vi kan ikke have flere operatorer i værdien
		else if ((lastPlus > 0) && (lastMinus > 0))
		{
			throw new NumberFormatException("Invalid complex value: "+val);
		}
		else
		{
			int opPos = lastPlus > 0 ? lastPlus : lastMinus;
			if (s.charAt(s.length() -1) != 'i')
				throw new NumberFormatException("Invalid complex value: "+val);
			r = Double.parseDouble(s.substring(0, opPos).trim());
			i = Double.parseDouble(s.substring(opPos, s.length() -1));
		}
		return new Complex(r,i);
	}
	
	public Complex addS(double val)
	{
		double a = Re;
		double b = Im;
		double c = val;
		//(a + bi) + c = a + c + bi
		Re = a + c;
		Im = b;
		return this;
	}
	
	public Complex add(double val)
	{
		return clone().add(val);
	}
	
	public Complex addS(Complex val)
	{
		double a = Re;
		double b = Im;
		double c = val.Re;
		double d = val.Im;
		//(a + bi) + (c + di) = a + c + (b+d)i
		Re = a + c;
		Im = b + d;
		return this;
	}
	
	public Complex add(Complex val)
	{
		return this.clone().addS(val);
	}
	
	public static Complex add(Complex val1, Complex val2)
	{
		return val1.clone().addS(val2);
	}
	
	public Complex subS(double val)
	{
		double a = Re;
		double b = Im;
		double c = val;
		//(a + bi) - c = a - c + bi
		Re = a - c;
		Im = b;
		return this;
	}
	
	public Complex sub(double val)
	{
		return clone().subS(val);
	}

	public Complex subS(Complex val)
	{
		double a = Re;
		double b = Im;
		double c = val.Re;
		double d = val.Im;
		//(a + bi) - (c + di) = a - c + (b-d)i
		Re = a - c;
		Im = b - d;
		return this;
	}
	
	public Complex sub(Complex val)
	{
		return clone().subS(val);
	}
	
	public static Complex sub(Complex minuend, Complex subtrahend)
	{
		return minuend.clone().subS(subtrahend);
	}

	public Complex mulS(Complex val)
	{
		double a = Re;
		double b = Im;
		double c = val.Re;
		double d = val.Im;
		//(a + bi)*(c + di) = ac + adi + bic + bidi = ac - bd + (ad + bc)i 
		Re = a*c - b*d;
		Im = a*d + b*c;
		return this;
	}

	public Complex mul(Complex val)
	{
		return clone().mulS(val);
	}
	
	public static Complex mul(Complex val1, Complex val2)
	{
		return val1.clone().mulS(val2);
	}
	
	public Complex mulS(double val)
	{
		double a = Re;
		double b = Im;
		double c = val;
		//(a + bi)*c = ac + bci
		Re = a*c;
		Im = b*c;
		return this;
	}
	
	public Complex mul(double val)
	{
		return clone().mulS(val);
	}
	
	public Complex sqrS()
	{
		//(a + bi)*(a + bi) = a^2 - b^2 + 2abi
		//Re = a^2 - b^2
		//Im = 2ab
		double a = Re;
		double b = Im;
		Re = a*a - b*b;
		Im = 2*a*b;
		return this;
	}
	
	public Complex sqr()
	{
		return clone().sqrS();
	}
	
	public static Complex sqr(Complex val)
	{
		return val.sqr();
	}
	
	public Complex divS(double val)
	{
		double a = Re;
		double b = Im;
		double c = val;
		//(a + bi)/c = a/c + (b/c)i
		Re = a/c;
		Im = b/c;
		return this;
	}
	
	public Complex div(double val)
	{
		return clone().divS(val);
	}
	
	public Complex divS(Complex val)
	{
		double a = Re;
		double b = Im;
		double c = val.Re;
		double d = val.Im;
		/* (a + bi)/(c + di) = ((a + bi)*(c - di))/((c + di)*(c - di)) =
		 * (ac - adi + bci - bdi^2)/(c^2 - cdi + cdi - d^2*i^2) =
		 * (ac + bd + bci - adi)/(c^2 + d^2) =
		 * (ac + bd)/(c^2 + d^2) + ((bc - ad)/(c^2 + d^2))*i
		 denom = c^2 + d^2
		 Re = (ac + bd)/denom
		 Im = (bc - ad)/denom
		*/
		double denom = c*c + d*d;
		Re = (a*c + b*d)/denom;
		Im = (b*c - a*d)/denom;
		return this;
	}
	
	public Complex div(Complex val)
	{
		return clone().divS(val);
	}
	
	public static Complex div(Complex numerator, Complex denominator)
	{
		return numerator.clone().divS(denominator);
	}
	
	public Complex sinS()
	{
		double a = Re;
		double b = Im;
		// sin(a+bi) = (sin(a)*(e^b+1/e^b))/2 + (cos(a)*(e^b-1/e^b))/2 * i
		// expb = e^b
		// invExpb = 1/expb
		// Re = sin(a)*(expb+invExpb)/2
		// Im = cos(a)*(expb+invExpb)/2
		double expb = Math.exp(b);
		double invExpb = 1/expb;
		Re = Math.sin(a)*(expb+invExpb)/2;
		Im = Math.cos(a)*(expb-invExpb)/2;
		return this;
	}
	
	public Complex sin()
	{
		return clone().sinS();
	}
	
	public static Complex sin(Complex val)
	{
		return val.clone().sinS();
	}
	
	public Complex sinhS()
	{
		//sinh(z) = -i*sin(i*z)
		//(a+bi)*i = -b+ai
		double a = Re;
		double b = Im;
		Re = -b;
		Im = a;
		sinS();
		a = Re;
		b = Im;
		Re = -b;
		Im = a;
		return this;
	}
	
	public Complex sinh()
	{
		return clone().sinhS();
	}
	
	public static Complex sinh(Complex val)
	{
		return val.clone().sinhS();
	}

	public Complex cosS()
	{
		double a = Re;
		double b = Im;
		// sin(a+bi) = (cos(a)*(e^b+1/e^b))/2 - (sin(a)*(e^b-1/e^b))/2 * i
		// expb = e^b
		// invExpb = 1/expb
		// Re = cos(a)*(expb+invExpb)/2
		// Im = -sin(a)*(expb+invExpb)/2
		double expb = Math.exp(b);
		double invExpb = 1/expb;
		Re = Math.cos(a)*(expb+invExpb)/2;
		Im = -Math.sin(a)*(expb-invExpb)/2;
		return this;
	}
	
	public Complex cos()
	{
		return clone().cosS();
	}
	
	public static Complex cos(Complex val)
	{
		return val.clone().cosS();
	}
	
	public Complex coshS()
	{
		//cosh(z) = cos(i*z)
		//(a+bi)*i = -b+ai
		double a = Re;
		double b = Im;
		Re = -b;
		Im = a;
		return cosS();
	}
	
	public Complex cosh()
	{
		return clone().coshS();
	}
	
	public static Complex cosh(Complex val)
	{
		return val.clone().coshS();
	}
	
	public Complex tanS()
	{
		double a = Re;
		double b = Im;
		double expb = Math.exp(b);
		double invExpb = 1/expb;
		double s = Math.sin(a);
		double c = Math.cos(a);
		double x = expb + invExpb;
		double y = expb - invExpb;
		double denom = c*c*x*x+s*s*y*y; 
		
		Re = 4*s*c/denom;
		Im = x*y/denom;
		return this;
		
		//return Complex.div(this.sin(), this.cos());
	}
	
	public Complex tan()
	{
		return clone().tanS();
	}
	
	public static Complex tan(Complex val)
	{
		return val.clone().tan();
	}
	
	public Complex expS()
	{
		double a = Re;
		double b = Im;
		// e^(a+bi) = e^a*e^bi = e^a*(cos(b)+i*sin(b)) = cos(b)*e^a+i*sin(b)*e^a
		// expa = e^a
		// Re = cos(b)*expa
		// Im = sin(b)*expa
		double expa = Math.exp(a);
		Re = Math.cos(b)*expa;
		Im = Math.sin(b)*expa;
		return this;
	}

	public Complex exp()
	{
		return clone().expS();
	}
	
	public static Complex exp(Complex val)
	{
		return val.exp();
	}
	
	// beregner den komplekse naturlige logaritme
	public Complex lnS()
	{
		// ln(r*e^(i*theta)) = ln(r) + i*theta
		// Re = ln(r)
		// Im = theta
		double r = mod();
		double theta = arg();
		Re = Math.log(r);
		Im = theta;
		return this;
	}
	
	public Complex ln()
	{
		return clone().lnS();
	}
	
	public static Complex ln(Complex val)
	{
		return val.clone().lnS();
	}
	
	//beregner this^val
	public Complex powerS(Complex val)
	{
		// z^p = e^(p*ln(z))
		Complex z = this;
		Complex p = val;
		//vi lader 0^0 = 1
		if ((p.Im == 0) && (p.Re == 0)) //z^0 = 1
			return new Complex(1, 0);
		if ((z.Im == 0) && (z.Re == 0)) //0^p = 0
			return new Complex(0, 0);
		
		return z.lnS().mulS(p).expS();
		//return exp(mul(p, ln(z)));
	}
	
	public Complex power(Complex val)
	{
		return this.clone().powerS(val);
	}
	
	public static Complex power(Complex base, Complex exponent)
	{
		return base.clone().powerS(exponent);
	}
}
