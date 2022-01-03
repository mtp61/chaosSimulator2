package plotting;

import java.util.ArrayList;

import simulation.Magnet;
import simulation.World;

public class Generator extends Thread {
	private int threadNum;
	private double[][] points;
	
	public double[][] output;
	
	private int maxTicks = 100000;
	
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
		int[] tickCounter = new int[numPoints];

		for (int i = 0; i < numPoints; ++i) {
			// print expected time
			if (i % 100 == 0 && i != 0) {
				long timeNow = System.currentTimeMillis();
				long timeElapsed = timeNow - startTime;
				double percentDone = ((double) i / numPoints);
				long predictedTime = (long) (timeElapsed/percentDone)-timeElapsed;
				int predictedTimeSeconds = ((int) predictedTime)/1000;
				
				System.out.printf("Thread %d: %d / %d, %ds remaining\n", threadNum, i, numPoints, predictedTimeSeconds);			
			}

			world.resetWorld(points[i][0], points[i][1], 0, 0);			
			tickCounter[i] = 0;
			
			while (true) {
				world.tick(tickrate);
				++tickCounter[i];
				
				if (tickCounter[i] > maxTicks) {
					System.out.println("error: max ticks hit");
					System.out.println(world.getArmX());
					System.out.println(world.getArmX());
					
					finalX = 1000000;
					finalY = 1000000;
					break;
				}
				
				if (world.getStopped()) {
					double totalX = 0;
					double totalY = 0;
					for (int j = 0; j < world.getPosArraySize(); ++j) {
						totalX += world.getPosArrayX()[j];
						totalY += world.getPosArrayY()[j];
					}

					finalX = totalX / world.getPosArraySize();
					finalY = totalY / world.getPosArraySize();
					
//					finalX = world.getArmX();
//					finalY = world.getArmY();

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