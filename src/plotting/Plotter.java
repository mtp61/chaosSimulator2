package plotting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import simulation.Magnet;

public abstract class Plotter {
    
    private final static int numThreads = 4;

    private final static int defaultMinX = -400;
    private final static int defaultMaxX = 400;
    private final static int defaultMinY = -400;
    private final static int defaultMaxY = 400;
    private final static int defaultResX = 40;
    private final static int defaultResY = 40;
    private final static String defaultFile = "magnets";
    private final static boolean defaultVerbose = true;

    private final static int binSize = 40;
    private final static Color[] colors = { 
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.MAGENTA, Color.ORANGE };
    
    public static void plot(ArrayList<Magnet> magnets) {
        plot(magnets, defaultMinX, defaultMaxX, defaultMinY, defaultMaxY,
                defaultResX, defaultResY, defaultFile, defaultVerbose);
    }
    
    public static void plot(ArrayList<Magnet> magnets,
            int minX, int maxX, int minY, int maxY, int resX,
            int resY, String file, boolean verbose) {
        long startTime = System.currentTimeMillis();
        
        int numPoints = resX * resY;
        double gapX = ((double) maxX - minX) / (resX - 1);
        double gapY = ((double) maxY - minY) / (resY - 1);
        
        // generate points
        double totalPoints[][] = new double[numPoints][2];
        for (int i = 0; i < resX; ++i) {
            for (int j = 0; j < resY; ++j) {
                totalPoints[i * resY + j][0] = i * gapX + minX;
                totalPoints[i * resY + j][1] = j * gapY + minY;
            }
        }
        
        // create threads 
        if (verbose) {
            System.out.printf("creating %d threads\n", numThreads);
        }
        Generator threadArray[] = new Generator[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            int numThreadPoints = numPoints / numThreads + (i < numPoints % numThreads ? 1 : 0);
            double threadPoints[][] = new double[numThreadPoints][2];
            
            for (int j = 0; j < numThreadPoints; ++j) {
                threadPoints[j][0] = totalPoints[numThreads * j + i][0];
                threadPoints[j][1] = totalPoints[numThreads * j + i][1];
            }
            
            threadArray[i] = new Generator(threadPoints, i, magnets, verbose);
            threadArray[i].start();
        }
        
        // wait for threads to finish
        for (Generator g : threadArray) {
            try {
                g.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // print time
        final long endTime = System.currentTimeMillis();
        double execTime = (double) (endTime - startTime) / 1000;
        double timePerPoint = execTime / numPoints;
        System.out.printf("Operation took: %d s, %.5f s per point\n", (int) execTime, timePerPoint);
        
        // write data
        PrintWriter writer;
        String filename = String.format("%s_%d_%d_%d_%d_%d_%d",
                file, minX, maxX, minY, maxY, resX, resY);
        try {
            writer = new PrintWriter(String.format("output/%s.txt", filename),"UTF-8");
            for (int j = 0; j < numPoints / numThreads; ++j) {
                for (int i = 0; i < numThreads; ++i) {
                    writer.println(Arrays.toString(threadArray[i].output[j]));
                }
            }
            for (int i = 0; i < numPoints % numThreads; ++i) {
                writer.println(Arrays.toString(threadArray[i].output[numPoints / numThreads]));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.printf("%d points in output/%s.txt\n", numPoints, filename);
        
        // render image
        // assign points to bins
        ArrayList<Point> bins = new ArrayList<Point>();
        int pointBins[] = new int[numPoints];
        
        double x, y;
        int binX, binY;
        boolean foundBin;
        int i = 0;
        int pointCounter = 0;
        while (true) {
            for (int j = 0; j < numThreads; ++j) {
                x = threadArray[j].output[i][2];			
                y = threadArray[j].output[i][3];
                                
                // find a bin
                foundBin = false;
                for (int k = 0; k < bins.size(); ++k) {
                    binX = bins.get(k).x;
                    binY = bins.get(k).y;
                    if (x >= binX && x <= binX + binSize && y >= binY && y <= binY + binSize) {
                        pointBins[pointCounter] = k;
                        foundBin = true;
                        break;
                    }
                }
                
                if (!foundBin) {
                    // check if we can create more bins
                    if (bins.size() < colors.length) {
                        bins.add(new Point((int) x - binSize / 2, (int) y - binSize / 2));
                        pointBins[pointCounter] = bins.size() - 1;
                    } else {
                        pointBins[pointCounter] = -1;
                    }
                }
                
                if (++pointCounter == numPoints) {
                    break;
                }
            }
            
            if (pointCounter == numPoints) {
                break;
            }
            ++i;
        }
        
        // create the image
        BufferedImage bufferedImage = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, resX, resY);
        for (int j = 0; j < numPoints; ++j) {
            if (pointBins[j] == -1) {
                continue;
            }
            
            g.setColor(colors[pointBins[j]]);
            g.drawRect(j / resY, j % resY, 1, 1);
        }
        
        // save the image
        try {
            File f = new File(String.format("plot/%s.png", filename));
            ImageIO.write(bufferedImage, "png", f);
            System.out.printf("Plot at plot/%s.png\n", filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // print info
        System.out.print("Bins at: ");
        for (Point p : bins) {
            System.out.printf("(%d, %d) ", p.x + binSize / 2, p.y + binSize / 2);
        }
        System.out.println("");
    }
}
