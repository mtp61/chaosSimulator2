package plotting;

import java.util.ArrayList;

import simulation.ListElement;
import simulation.Magnet;
import simulation.World;

public class Generator extends Thread {
	private int threadNum;
	private double[][] points;
	
	public double[][] output;
	
	private static final int maxTicks = 100000;
	
	private World world; 
	
	public Generator(double[][] points, int threadNum, ArrayList<Magnet> magnets) {
		this.points = points;
		this.threadNum = threadNum;
		
		world = new World();
		world.setMagnets(magnets);
//		world.maxForce = 1000;
//		world.setHomeX(0);
//		world.setHomeY(0);
//		world.setDefaultCoef(10);
//		world.setHomeCoef(10);
//		world.setFricition(.95);
//		world.setMaxStopDist(15);
//		world.setPosArraySize(1000);
	}
	
	public void run(){				
		int tickrate = 60;
		
		// setup output
		int numPoints = points.length;
		output = new double[numPoints][4];
		
		long startTime = System.currentTimeMillis();

		// plot points
		double finalX, finalY;
		int tickCounter;
		
		for (int i = 0; i < numPoints; ++i) {
			// print expected time
			if (i % 1000 == 0 && i != 0) {
				double secRemaining = (double) (System.currentTimeMillis() - startTime) / 1000
						* ((double) numPoints / i- 1);
                double minRemaining = Math.floor(secRemaining / 60);
                double hourRemaining = Math.floor(minRemaining / 60);
                secRemaining -= 60 * minRemaining;
                minRemaining -= 60 * hourRemaining;      
				System.out.printf("Thread %d: %d / %d, %.0f h %.0f m %.0f s remaining\n",
						threadNum, i, numPoints, hourRemaining, minRemaining, secRemaining);			
			}

			world.resetWorld(points[i][0], points[i][1], 0, 0);			
			tickCounter = 0;
			
			while (true) {
				world.tick(tickrate);
				++tickCounter;
				
				if (tickCounter > maxTicks) {
					System.out.println("error: max ticks hit");
					System.out.println(world.getArmX());
					System.out.println(world.getArmX());
					finalX = 1000000;
					finalY = 1000000;
					break;
				}
				
				if (world.getStopped()) {
					finalX = 0;
					finalY = 0;
					for (ListElement point : world.getPosArray()) {
						finalX += point.x;
						finalY += point.y;
					}

					finalX /= world.getPosArray().size();
					finalY /= world.getPosArray().size();

					break;
				}
			}
			
			// write to output TODO shouldn't need to return where the points start
			output[i][0] = points[i][0];
			output[i][1] = points[i][1];
			output[i][2] = finalX;
			output[i][3] = finalY;
		}

		System.out.printf("Thread %d finished\n", threadNum);
	}
}