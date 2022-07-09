package simulation;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private double maxStopDist = 15; // 15
    private int posArraySize = 100; // 1000
    private LinkedList<ListElement> posArray = new LinkedList<ListElement>();
    private static final int checkEvery = 10; // 1
    private int tickCounter = 0;
    private int failIndex = posArraySize;
    
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
        
        posArray.clear(); 		
        tickCounter = 0;
        failIndex = posArraySize;
    }
    
    public void resetWorld() {
        this.resetWorld(this.startArmX, this.startArmY, this.startVelX, this.startVelY);
    }
    
    public void tick(double tickrate) {		
        // calculate acceleration
        double[] a = this.acceleration();
        this.velX += a[0] / tickrate;
        this.velY += a[1] / tickrate;
        
        // simple updating
        this.armX += this.velX / tickrate;
        this.armY += this.velY / tickrate;
        
        // simple friction
        this.velX *= 1 - ((1 - this.friction) / tickrate); // this is not technically right but to do so would take more expensive functions
        this.velY *= 1 - ((1 - this.friction) / tickrate);
        
        // TODO my own list (double ended queue, could store more info too) rather than default 
        
        // check if stopped
        if (++tickCounter % checkEvery == 0) {
            posArray.addLast(new ListElement(armX, armY));			
            if (tickCounter / checkEvery >= posArraySize) {	
                posArray.removeFirst();
                
                // we don't need to check if we would fail anyways
                if (++failIndex >= posArraySize) {
                    double minX = 100000;
                    double maxX = -100000;
                    double minY = 100000;
                    double maxY = -100000;
                    int counter = 0;
                    for (ListElement point : posArray) {
                        if (point.x < minX) {
                            minX = point.x;
                        } else if (point.x > maxX) {
                            maxX = point.x;
                        }
                        if (point.y < minY) {
                            minY = point.y;
                        } else if (point.y > maxY) {
                            maxY = point.y;
                        }
                        
                        if (maxX - minX >= this.maxStopDist || maxY - minY >= this.maxStopDist) {
                            failIndex = counter;
                            return;
                        }
                        
                        ++counter;
                    }

                    stopped = true;
//					this.stopped = Math.sqrt(Math.pow(maxX - minX, 2) + Math.pow(maxY - minY, 2)) 
//					< this.maxStopDist;
                }
            }
        }
    }
    
    private double[] acceleration() {		
        double sumX = 0;
        double sumY = 0;
        
        // add for home
        double deltaX = homeX - armX;
        double deltaY = homeY - armY;
        double magSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        double mag = Math.sqrt(magSquared);
//		sumX += (homeCoef / 10000) * (deltaX * mag);
//		sumY += (homeCoef / 10000) * (deltaY * mag);
        double force = Math.min(homeCoef * magSquared / 10000, maxForce);
        sumX += force * deltaX / mag;
        sumY += force * deltaY / mag;
        
        // add for magnets
        for (Magnet m : magnets) {
            deltaX = m.getXPos() - armX;
            deltaY = m.getYPos() - armY;
            magSquared = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
            mag = Math.sqrt(magSquared);
//			double mag32 = Math.pow(Math.pow(deltaX, 2) + Math.pow(deltaY, 2), 1.5);
//			sumX += (m.getCoef() * 10000) * (deltaX / mag32);
//			sumY += (m.getCoef() * 10000) * (deltaY / mag32);
            force = Math.min(m.getCoef() * 10000 / magSquared, maxForce);
            sumX += force * deltaX / mag;
            sumY += force * deltaY / mag;
        }
        
        // not too fast!
//		double accMag = Math.sqrt(Math.pow(sumX, 2) + Math.pow(sumY, 2));
//		double accMagMin = Math.min(accMag, maxForce);
//		sumX *= accMagMin / accMag;
//		sumY *= accMagMin / accMag;		
        
        // add for home
//		double deltaX = Math.abs(this.armX - this.homeX);
//		double deltaY = Math.abs(this.armY - this.homeY);
//		double angle = deltaX != 0 ? Math.atan(deltaY / deltaX) : 0;
//		double radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
//		double force = Math.min(Math.pow(radius / 100, 2) * this.homeCoef, this.maxForce);
//		double ax = force * Math.cos(angle);
//		double ay = force * Math.sin(angle);
//		
//		sumX += this.armX < this.homeX ? ax : -ax;
//		sumY += this.armY < this.homeY ? ay : -ay;
//				
//		// add for magnets
//		for (Magnet m : this.magnets) {			
//			deltaX = Math.abs(m.getXPos() - this.armX);
//			deltaY = Math.abs(m.getYPos() - this.armY);
//			angle = deltaX != 0 ? Math.atan(deltaY / deltaX) : 0;
//			radius = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
//			force = Math.min(Math.pow(radius/100, - 2) * m.getCoef(), this.maxForce);
//			ax = force * Math.cos(angle);
//			ay = force * Math.sin(angle);
//			
//			sumX += this.armX < m.getXPos() ? ax : -ax;
//			sumY += this.armY < m.getYPos() ? ay : -ay;
//		}
        
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
    public LinkedList<ListElement> getPosArray() { return posArray; }
    
    public double[] getPosArrayX() {
        double a[] = new double[posArray.size()];
        int i = 0;
        for (ListElement point : posArray) {
            a[i] = point.x;
            ++i;
        }
        return a;
    }
    
    public double[] getPosArrayY() {
        double a[] = new double[posArray.size()];
        int i = 0;
        for (ListElement point : posArray) {
            a[i] = point.y;
            ++i;
        }
        return a;
    }

    public void setArmX(double armX) { this.armX = armX; }
    public void setArmY(double armY) { this.armY = armY; }
    public void setVelX(double velX) { this.velX = velX; }
    public void setVelY(double velY) { this.velY = velY; }
    public void setStartArmX(double startArmX) { this.startArmX = startArmX; }
    public void setStartArmY(double startArmY) { this.startArmY = startArmY; }
    public void setMagnets(ArrayList<Magnet> magnets) { this.magnets = magnets; }
    public void setPosArraySize(int posArraySize) { this.posArraySize = posArraySize; }
}
