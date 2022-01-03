package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PlotOverlay {

	private static final int BOX_GAP = 30;
	
	public static final int X_OFFSET = 35;
	public static final int Y_OFFSET = 20;
	
	private int width = 150;
	private int height = 250;
	
	private int x = 0;
	private int y = 0;
	
	private ArrayList<InputBox> inputs = new ArrayList<InputBox>();
	
	public PlotOverlay() {
		inputs.add(new InputBox("minX", -400));
		inputs.add(new InputBox("maxX", 400));
		inputs.add(new InputBox("minY", -400));
		inputs.add(new InputBox("maxY", 400));
		inputs.add(new InputBox("resX", 40));
		inputs.add(new InputBox("resY", 40));
	}
	
	public void draw(Graphics g, int x, int y, int activeBox) {
		this.x = x - X_OFFSET;
		this.y = y + Y_OFFSET;
		
		g.setColor(Color.BLACK);
		g.drawRect(this.x, this.y, width, height);
		for (int i = 0; i < inputs.size(); ++i) {
			inputs.get(i).draw(g, this.x, this.y + BOX_GAP * i, activeBox == i);
		}
	}

	public int[][] getBoxCoords() {
		int c[][] = new int[inputs.size()][4];  // x y width height for each input box
		
		for (int i = 0; i < inputs.size(); ++i) {
			c[i][0] = x + InputBox.X_OFFSET;
			c[i][1] = y + BOX_GAP * i + InputBox.Y_OFFSET;
			c[i][2] = InputBox.WIDTH;
			c[i][3] = InputBox.HEIGHT;
		}	
		
		return c;
	}
	
	public void number(int activeBox, int n) {
		inputs.get(activeBox).number(n);
	}
	
	public void negative(int activeBox) {
		inputs.get(activeBox).negative();

	}
	
	public void delete(int activeBox) {
		inputs.get(activeBox).delete();
	}
	
	public int getMinX() {
		return inputs.get(0).getValue();
	}
	
	public int getMaxX() {
		return inputs.get(1).getValue();
	}
	
	public int getMinY() {
		return inputs.get(2).getValue();
	}
	
	public int getMaxY() {
		return inputs.get(3).getValue();
	}
	
	public int getResX() {
		return inputs.get(4).getValue();
	}
	
	public int getResY() {
		return inputs.get(5).getValue();
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
