package simulation;

import java.util.ArrayList;

public class World {

	// setup
	private double startArmX = 400;
	private double startArmY = 400;
	private double armX = startArmX;
	private double armY = startArmY;
	private double startVelX = 0;
	private double startVelY = 0;
	private double velX = startVelX;
	private double velY = startVelY;
	private double homeX = 0;
	private double homeY = 0;
	
	// simulation coefficients
	private double defaultCoef = 10;
	private double homeCoef = 10;
	private double friction = .95;
	private double maxForce = 1000.0;

	// stopping
	private boolean stopped = false;
	private double maxStopDist = 15;
	private int posArraySize = 1000;
	private double[] posArrayX = new double[posArraySize];
	private double[] posArrayY = new double[posArraySize];
	
	private ArrayList<Magnet> magnets = new ArrayList<Magnet>();
	
	public World() {
		this.resetWorld();
	}
	
	public void resetWorld(double armX, double armY, double velX, double velY) {
		this.armX = armX;
		this.armY = armY;
		this.velX = velX;
		this.velY = velY;
		this.stopped = false;
		
		// initialize pos array
		for (int i = 0; i < this.posArraySize; ++i) {
			this.posArrayX[i] = i * 10;
			this.posArrayY[i] = i * 10;
		}
	}
	
	public void resetWorld() {
		this.resetWorld(this.startArmX, this.startArmY, this.startVelX, this.startVelY);
	}
	
	public void tick(double tickrate) {
		// TODO is this efficient??
		
		// calculate acceleration
		double[] a = this.acceleration();
		this.velX += a[0] / tickrate;
		this.velY += a[1] / tickrate;
		
		// simple updating
		this.armX += this.velX / tickrate;
		this.armY += this.velY / tickrate;
		
		// simple friction
		this.velX *= 1 - ((1 - this.friction) / tickrate); // TODO this is not right
		this.velY *= 1 - ((1 - this.friction) / tickrate);
		
		// check if stopped
		double maxX = this.posArrayX[0];
		double minX = this.posArrayX[0];
		double maxY = this.posArrayY[0];
		double minY = this.posArrayY[0];
		// TODO this is inefficient 
		for (int i = 0; i < this.posArraySize - 1; ++i) { // update vel array
			this.posArrayX[this.posArraySize - i - 1] = this.posArrayX[this.posArraySize - i - 2];
			this.posArrayY[this.posArraySize - i - 1] = this.posArrayY[this.posArraySize - i - 2];
			if (this.posArrayX[this.posArraySize - 1 - i] > maxX) {
				maxX = this.posArrayX[this.posArraySize - i - 1];
			} else if (this.posArrayX[this.posArraySize - i - 1] < minX) {
				minX = this.posArrayX[this.posArraySize - i - 1];
			}
			if (this.posArrayY[this.posArraySize - i - 1] > maxY) {
				maxY = this.posArrayY[this.posArraySize - i - 1];
			} else if (this.posArrayY[this.posArraySize - i - 1] < minY) {
				minY = this.posArrayY[this.posArraySize - i - 1];
			}
		}

		this.posArrayX[0] = this.armX;
		this.posArrayY[0] = this.armY;
		
		double dist = Math.sqrt(Math.pow(maxX - minX, 2) + Math.pow(maxY - minY, 2));
		
		this.stopped = dist < this.maxStopDist;
	}
	
	private double[] acceleration() {		
		double sumX = 0;
		double sumY = 0;
		
		// add for home
		double deltaX = Math.abs(this.armX - this.homeX);
		double deltaY = Math.abs(this.armY - this.homeY);
		double angle = deltaX != 0 ? Math.atan(deltaY / deltaX) : 0;
		double radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
		double force = Math.min(Math.pow(radius / 100, 2) * this.homeCoef, this.maxForce);
		double ax = force * Math.cos(angle);
		double ay = force * Math.sin(angle);
		
		sumX += this.armX < this.homeX ? ax : -ax;
		sumY += this.armY < this.homeY ? ay : -ay;
				
		// add for magnets
		for (Magnet m : this.magnets) {			
			deltaX = Math.abs(m.getXPos() - this.armX);
			deltaY = Math.abs(m.getYPos() - this.armY);
			angle = deltaX != 0 ? Math.atan(deltaY / deltaX) : 0;
			radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
			force = Math.min(Math.pow(radius/100, - 2) * m.getCoef(), this.maxForce);
			ax = force * Math.cos(angle);
			ay = force * Math.sin(angle);
			
			sumX += this.armX < m.getXPos() ? ax : -ax;
			sumY += this.armY < m.getYPos() ? ay : -ay;
		}
		
		return new double[] {sumX, sumY};
	}
	
	public void addMagnet(Magnet magnet) {
		this.magnets.add(magnet);
	}
	
	public void clearMagnets() {
		this.magnets.clear();
	}
	
	public double getArmX() { return armX; }
	public double getArmY() { return armY; }
	public double getStartArmX() { return startArmX; }
	public double getStartArmY() { return startArmY; }
	public double getVelX() { return velX; }
	public double getVelY() { return velY; }
	public double getHomeX() { return homeX; }
	public double getHomeY() { return homeY; }
	public double getDefaultCoef() { return defaultCoef; }
	public ArrayList<Magnet> getMagnets() { return magnets; }
	public boolean getStopped() { return stopped; }
	public int getPosArraySize() { return posArraySize; }
	public double[] getPosArrayX() { return posArrayX; }
	public double[] getPosArrayY() { return posArrayY; }
	
	public void setArmX(double armX) { this.armX = armX; }
	public void setArmY(double armY) { this.armY = armY; }
	public void setVelX(double velX) { this.velX = velX; }
	public void setVelY(double velY) { this.velY = velY; }
	public void setStartArmX(double startArmX) { this.startArmX = startArmX; }
	public void setStartArmY(double startArmY) { this.startArmY = startArmY; }
	public void setMagnets(ArrayList<Magnet> magnets) { this.magnets = magnets; }
	public void setPosArraySize(int posArraySize) { this.posArraySize = posArraySize; }
}
