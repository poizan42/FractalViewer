/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.sql.Date;

import javax.swing.*;
import net.complex.*;

public class FractalPanel extends JPanel
{
	private class PainterThread implements Runnable
	{
		public Thread thread;
		public boolean signalInterrupt = false;
		
		public PainterThread()
		{
			thread = new Thread(this);
		}
		
		public boolean stop()
		{
			signalInterrupt = true;
			try
			{
				thread.join();
			}
			catch (InterruptedException e)
			{
				return false;
			}
			return true;
		}
		
		public void run()
		{
			genImg(getSize().width, getSize().height);
			painterThread = null;
		}	
		
		private void genImg(int width, int height)
		{
			if ((width <= 0) || (height <= 0))
				return;
			long startTime = System.currentTimeMillis();
			
			BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			newImg.getGraphics().setColor(Color.white);
			newImg.getGraphics().fillRect(0, 0, width, height);
			if (img != null)
				newImg.getGraphics().drawImage(img, 0, 0, width, height, Color.white, null);
			img = newImg;
			
		    double reInc = (maxRange.Re - minRange.Re)/(double)width;
		    double imInc = (maxRange.Im - minRange.Im)/(double)height;
		    Complex curVal = new Complex(minRange.Re, maxRange.Im);
		    
		    for (int y = 0; y < height; y++)
		    {
		    	curVal.Re = minRange.Re;
		    	for (int x = 0; x < width; x++)
		    	{
		    		if (signalInterrupt)
		    		{
		    			signalInterrupt = false;
		    			return;
		    		}
		    		Color pixel = calculatePixel(curVal);
		    		img.setRGB(x, y, pixel.getRGB());
		    		curVal.Re += reInc;
		    	}
		    	curVal.Im -= imInc;
		    	repaint();
		    }
		    System.out.println("rendering time: "+(System.currentTimeMillis()-startTime));
		}
	}
	
	private PainterThread painterThread;
	private Object imgUpdateLock = new Object();
	
	private Point dragStart, lastDragPoint;
	private int lastModifiers;
	private boolean isZooming, isPanning;
	
	private Complex minRange = new Complex(-2, -2);
	private Complex maxRange = new Complex(2, 2);
	private BufferedImage img;
	private int iterCount = 1000;
	private Colorizer colorization = new HueColorizer(50, (float)0.5);
	private Fractal fractal = new MandelbrotFractal(); 
	
	public FractalPanel()
	{
		enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}
	
	private Color calculatePixel(Complex pos)
	{
		if (fractal.hasEscaped(pos))
			return colorization.colorize(0);
		Complex z = fractal.getStartingPoint(pos);
		
		for (int i = 0; i < iterCount; i++)
		{
			z = fractal.calcIteration(z, pos);
			if (fractal.hasEscaped(z)) //escape! 
			{
				//int light = (i*255)/100;
				return colorization.colorize(i);
			}
		}
		
		return Color.black;
	}
	
	public Complex getMinRange()
	{
		return minRange.clone();
	}

	/*public void setMinRange(Complex minRange)
	{
		this.minRange = minRange;
	}*/

	public Complex getMaxRange()
	{
		return maxRange.clone();
	}

	/*public void setMaxRange(Complex maxRange)
	{
		this.maxRange = maxRange;
	}*/

	public int getIterCount()
	{
		return iterCount;
	}

	public void setIterCount(int iterCount)
	{
		this.iterCount = iterCount;
	}

	public Colorizer getColorization()
	{
		return colorization;
	}

	public void setColorization(Colorizer colorization)
	{
		this.colorization = colorization;
	}

	public Fractal getFractal()
	{
		return fractal;
	}

	public void setFractal(Fractal fractal)
	{
		this.fractal = fractal;
	}
 
	public double getRangeWidth()
	{
		return maxRange.Re - minRange.Re;
	}
	
	public double getRangeHeight()
	{
		return maxRange.Im - minRange.Im;
	}
	
	private void updateImg()
	{
		synchronized(imgUpdateLock)
		{
			if (painterThread != null)
				painterThread.stop();

			painterThread = new PainterThread();
			painterThread.thread.start();
		}
	}
	
	@Override
	public synchronized void paintComponent(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		if (img == null)
			updateImg();
		else
		{
			int deltaX, deltaY;
			if (isPanning && (lastDragPoint != null))
			{
				deltaX = lastDragPoint.x - dragStart.x;
				deltaY = lastDragPoint.y - dragStart.y;
			}
			else
			{
				deltaX = deltaY = 0;
			}
			g.drawImage(img, deltaX, deltaY, getSize().width, getSize().height, Color.white, this);
			if ((getSize().width != img.getWidth()) || (getSize().height != img.getHeight()))
				updateImg();
		}
		Rectangle dragRect = getDragRect();
		if (dragRect != null)
		{
			g.setColor(Color.gray);
			g.drawRect(dragRect.x, dragRect.y, dragRect.width, dragRect.height);
		}
	}

	public Complex pointToComplex(int x, int y)
	{
		//System.out.println("pointToComplex: x: "+x+", y: "+y);
		double xScale = getRangeWidth()/(double)getWidth();
		double yScale = getRangeHeight()/(double)getHeight();
		return new Complex(minRange.Re + x*xScale, maxRange.Im - y*yScale);
	}
	
	public Complex pointToComplex(Point val)
	{
		return pointToComplex(val.x, val.y);
	}
	
	public Point complexToPoint(Complex val)
	{
		double xScale = getRangeWidth()/(double)getWidth();
		double yScale = getRangeHeight()/(double)getHeight();
		return new Point((int)Math.round((val.Re - minRange.Re)/xScale),
				(int)Math.round((maxRange.Im - val.Im)/yScale));
	}
	
	private void zoomImg(int xMin, int yMin, int xMax, int yMax)
	{
		BufferedImage newImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		newImg.getGraphics().setColor(Color.black);
		newImg.getGraphics().fillRect(0, 0, getWidth(), getHeight());
		newImg.getGraphics().drawImage(img, 0, 0, getWidth(), getHeight(), xMin, yMin, xMax, yMax, null);
		img = newImg;
	}
	
	public void zoom(int xMin, int yMin, int xMax, int yMax)
	{
		synchronized(imgUpdateLock)
		{
			if (painterThread != null)
				painterThread.stop();
			
			/* pointToComplex bruger minRange og maxRange til
			 * at konvertere punktet til et komplekst tal,
			 * så vi må ikke sætte minRange før maxRange også er blevet beregnet.
			 */ 
			Complex newMinRange = pointToComplex(xMin, yMax);
			maxRange = pointToComplex(xMax, yMin);
			minRange = newMinRange;
			zoomImg(xMin, yMin, xMax, yMax);
			updateImg();
		}
	}
	
	public void setRanges(Complex newMinRange, Complex newMaxRange)
	{
		synchronized(imgUpdateLock)
		{
			if (painterThread != null)
				painterThread.stop();
			
			Point min = complexToPoint(newMinRange);
			Point max = complexToPoint(newMaxRange);
			
			zoomImg(min.x, max.y, max.x, min.y);			
			maxRange = newMaxRange;
			minRange = newMinRange;
			updateImg();
		}
	}
	
	private Rectangle getDragRect(Point dest, int modifiers)
	{
		if (!isZooming)
			return null;
		
		int x, y, width, height;

		/*x = Math.min(dragStart.x, dest.x);
		y = Math.min(dragStart.y, dest.y);*/
		Complex startVal = pointToComplex(dragStart);
		Complex endVal = pointToComplex(dest);
		/*width = dest.x - dragStart.x;
		height = dest.y - dragStart.y;*/
		Complex valDiff = Complex.sub(endVal, startVal);
		
		if (Math.abs(valDiff.Re) > Math.abs(valDiff.Im))
			valDiff.Im = valDiff.Im > 0 ? Math.abs(valDiff.Re) : -Math.abs(valDiff.Re);
		else
			valDiff.Re = valDiff.Re > 0 ? Math.abs(valDiff.Im) : -Math.abs(valDiff.Im);
		
		endVal = startVal.add(valDiff);
		Point endPoint = complexToPoint(endVal);
		width = endPoint.x - dragStart.x;
		height = endPoint.y - dragStart.y;
			
		x = width < 0 ? dragStart.x + width : dragStart.x;
		y = height < 0 ? dragStart.y + height : dragStart.y;
		width = Math.abs(width);
		height = Math.abs(height);
			
		return new Rectangle(x, y, width, height);
	}
	
	public Rectangle getDragRect()
	{
		return getDragRect(lastDragPoint, lastModifiers);
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent event)
	{
		if (event.getID() == MouseEvent.MOUSE_DRAGGED)
		{
			lastDragPoint = event.getPoint();
			repaint();
		}
			
		super.processMouseMotionEvent(event);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent event)
	{
		if ((event.getID() == MouseEvent.MOUSE_PRESSED))
		{
			dragStart = lastDragPoint = event.getPoint();
			if (event.getButton() == MouseEvent.BUTTON1)
				isZooming = true;
			else if (event.getButton() == MouseEvent.BUTTON3)
				isPanning = true;
		}
		else if ((event.getID() == MouseEvent.MOUSE_RELEASED) && 
				 (event.getX() != dragStart.x) &&
				 (event.getY() != dragStart.y))
		{
			if (isZooming && (event.getButton() == MouseEvent.BUTTON1))
			{
				Rectangle dragRect = getDragRect(event.getPoint(), event.getModifiersEx());
				isZooming = false;
				zoom(dragRect.x, dragRect.y, dragRect.x + dragRect.width, dragRect.y + dragRect.height);
			}
			else if (isPanning && (event.getButton() == MouseEvent.BUTTON3))
			{
				System.out.println("pan ended");
				isPanning = false;
				int deltaX = dragStart.x - event.getX();
				int deltaY = dragStart.y - event.getY();
				zoom(deltaX, deltaY, getWidth() + deltaX, getHeight() + deltaY);
			}
			dragStart = null;
		}
		
		super.processMouseEvent(event);
	}

	public boolean isZooming()
	{
		return isZooming;
	}

	public boolean isPanning()
	{
		return isPanning;
	}
}
