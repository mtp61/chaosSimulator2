package simulation;

import java.util.ArrayList;

import plotting.Plotter;

public class CliPlot {

    private static final double DEFAULT_MIN_X = -400;
    private static final double DEFAULT_MAX_X = 400;
    private static final double DEFAULT_MIN_Y = -400;
    private static final double DEFAULT_MAX_Y = 400;
    private static final int DEFAULT_RES_X = 40;
    private static final int DEFAULT_RES_Y = 40;
    private static final boolean DEFAULT_VERBOSE = true;
    private static final int DEFAULT_THREADS = 4;

    public static void main(String[] args) {
        double minX = DEFAULT_MIN_X;
        double maxX = DEFAULT_MAX_X;
        double minY = DEFAULT_MIN_Y;
        double maxY = DEFAULT_MAX_Y;
        int resX = DEFAULT_RES_X;
        int resY = DEFAULT_RES_Y;
        boolean verbose = DEFAULT_VERBOSE;
        int threads = DEFAULT_THREADS;

        // check arguments
        if (args.length != 7 && args.length != 9) {
            printHelpMessage();
            System.exit(1);
        }

        // load magnets
        String file = args[0];
        ArrayList<Magnet> magnets = Magnet.loadMagnets(file);
        if (magnets == null) {
            System.exit(1);
        }

        // get numerical params
        try {
            minX = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("minX is a double");
            System.exit(1);
        }
        try {
            maxX = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("maxX is a double");
            System.exit(1);
        }
        try {
            minY = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("minY is a double");
            System.exit(1);
        }
        try {
            maxY = Double.parseDouble(args[4]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("maxY is a double");
            System.exit(1);
        }
        try {
            resX = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("resX is an int");
            System.exit(1);
        }
        try {
            resY = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            printHelpMessage();
            System.out.println("resY is an int");
            System.exit(1);
        }

        // get optional params
        if (args.length == 9) {
            if (args[7].toLowerCase().equals("true")) {
                verbose = true;
            } else if (args[7].toLowerCase().equals("false")) {
                verbose = false;
            } else {
                printHelpMessage();
                System.out.println("verbose is a boolean");
                System.exit(1);
            }
            try {
                threads = Integer.parseInt(args[8]);
            } catch (NumberFormatException e) {
                printHelpMessage();
                System.out.println("threads is an int");
                System.exit(1);
            }
        }

        Plotter.plot(magnets, minX, maxX, minY, maxY, resX,
                resY, file, verbose, threads);
    }

    private static void printHelpMessage() {
        System.out.println("Format: plot file minX maxX minY maxY resX resY [verbose] [threads]");
    }
}
