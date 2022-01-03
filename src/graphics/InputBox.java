package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class InputBox {

	public static final int X_OFFSET = 50;
	public static final int Y_OFFSET = 5;
	public static final int WIDTH = 60;
	public static final int HEIGHT = 20;
		
	private String label;
	private int value;
	
	public InputBox(String label, int value) {
		this.label = label;
		this.value = value;
	}
	
	public void draw(Graphics g, int x, int y, boolean active) {
		g.setColor(active ? Color.GREEN : Color.WHITE);
		g.fillRect(x + X_OFFSET, y + Y_OFFSET, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesNewRoman", Font.PLAIN, 12));
		g.drawString(label, x + 10, y + Y_OFFSET + 15);
	
		String valueText = String.format("%d", value);
		int valueWidth = g.getFontMetrics().stringWidth(valueText);
		g.drawString(valueText, x + X_OFFSET + WIDTH - valueWidth - 5, y + Y_OFFSET + 15);
		
		g.drawRect(x + X_OFFSET, y + Y_OFFSET, WIDTH, HEIGHT);
	}
	
	public void number(int n) {
		value *= 10;
		value += value >= 0 ? n : -n; 
	}
	
	public void negative() {
		value = -value;
	}
	
	public void delete() {
		value /= 10;
	}
	
	public int getValue() { return value; }
}
