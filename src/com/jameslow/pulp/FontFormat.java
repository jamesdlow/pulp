package com.jameslow.pulp;

import java.awt.*;

public class FontFormat {
	public enum Align {LEFT, RIGHT, CENTER, JUSTIFIED};	
	public Font font = new Font("Arial", Font.PLAIN, 12);
	public Color color = Color.BLACK;
	public Align align = Align.LEFT;
	public boolean display = true;
}
