/*
 * Copyright (c) 2007 Kasper Fabæch Brandt
 * All rights reserved
 * */

package net.complex.fractal;

import java.awt.Color;

public class HueColorizer extends Colorizer {

	float scale, start, saturation, brightness;
	
	public HueColorizer(float scale, float start, float saturation, float brightness)
	{
		this.scale = scale;
		this.start = start;
		this.saturation = saturation;
		this.brightness = brightness;
	}
	
	public HueColorizer(float scale, float start)
	{
		this(scale, start, 1, 1);
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getStart() {
		return start;
	}

	public void setStart(float start) {
		this.start = start;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}

	@Override
	public Color colorize(int iterCount)
	{
		return Color.getHSBColor((((float)iterCount)/scale + start), saturation, brightness);
	}

}
