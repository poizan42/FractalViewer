/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex;

import java.text.ParseException;
import java.io.*;

public class ComplexTest
{
	private static BufferedReader console;
	
	private static Complex loadNum() throws IOException
	{
		Complex val;
		String s;
		System.out.print("number: ");
		s = console.readLine();
		try
		{
			val = Complex.parse(s);
		}
		catch (NumberFormatException e)
		{
			return new Complex(0,0);
		}
		return val;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException, IOException
	{
		InputStreamReader stdin = new InputStreamReader(System.in); 
		console = new BufferedReader(stdin); 
		Complex a = new Complex(0,0);
		Complex b = new Complex(0,0);
		Complex res = new Complex(0,0);
		String s;
		while (true)
		{
			System.out.print(": ");
			s = console.readLine();
			if (s.equals("quit"))
			{
				return;
			}
			else if (s.equals("stat"))
			{
				System.out.println("a: "+a.toString());
				System.out.println("b: "+b.toString());
				System.out.println("res: "+res.toString());
			}
			else if (s.equals("loada"))
			{
				a = loadNum();
				System.out.println("a: "+a.toString());
			}
			else if (s.equals("loadb"))
			{
				b = loadNum();
				System.out.println("b: "+b.toString());
			}
			else if (s.equals("storea"))
			{
				a = res;
				System.out.println("a: "+a.toString());
			}
			else if (s.equals("storeb"))
			{
				b = res;
				System.out.println("b: "+b.toString());
			}
			else if (s.equals("add"))
			{
				res = a.add(b);
				System.out.println(res.toString());
			}
			else if (s.equals("sub"))
			{
				res = a.sub(b);
				System.out.println(res.toString());
			}
			else if (s.equals("mul"))
			{
				res = a.mul(b);
				System.out.println(res.toString());
			}
			else if (s.equals("div"))
			{
				res = a.div(b);
				System.out.println(res.toString());
			}
			else if (s.equals("sina"))
			{
				res = a.sin();
				System.out.println(res.toString());
			}
			else if (s.equals("sinb"))
			{
				res = b.sin();
				System.out.println(res.toString());
			}
			else if (s.equals("cosa"))
			{
				res = a.cos();
				System.out.println(res.toString());
			}
			else if (s.equals("cosb"))
			{
				res = b.cos();
				System.out.println(res.toString());
			}
			else if (s.equals("expa"))
			{
				res = a.exp();
				System.out.println(res.toString());
			}
			else if (s.equals("expb"))
			{
				res = b.exp();
				System.out.println(res.toString());
			}
			else if (s.equals("power"))
			{
				res = a.power(b);
				System.out.println(res.toString());
			}
		}
	}

}
