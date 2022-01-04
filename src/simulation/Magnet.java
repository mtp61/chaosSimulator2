package simulation;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;

public class Magnet implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final double radius = 10.0;
	
	private int x;
	private int y;
	private double c;
	
	public Magnet(int x, int y, double c) {
		this.x = x;
		this.y = y;
		this.c = c;
	}
	
	public static String magnetHash(ArrayList<Magnet> magnets) {
		Gson gson = new Gson();
		String hash = Integer.toHexString(gson.toJson(magnets).hashCode()); 
		return hash.substring(0, Math.min(6, hash.length()));
	}
	
	public int getXPos() { return x; }
	public int getYPos() { return y; }
	public double getCoef() { return c; }

	public void setxPos(int x) { this.x = x; }
	public void setyPos(int y) { this.y = y; }
	public void setCoef(double c) { this.c = c; }
}
