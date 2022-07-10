package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PlotOverlay {

    public static final int X_OFFSET = 20;
    public static final int Y_OFFSET = 20;
    
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int height = 0;
    
    private ArrayList<InputBox> inputs = new ArrayList<InputBox>();
    
    public PlotOverlay() {
        inputs.add(new IntBox("minX", -400));
        inputs.add(new IntBox("maxX", 400));
        inputs.add(new IntBox("minY", -400));
        inputs.add(new IntBox("maxY", 400));
        inputs.add(new IntBox("resX", 40));
        inputs.add(new IntBox("resY", 40));
        inputs.add(new IntBox("threads", 4));
        inputs.add(new StringBox("file", ""));

        calculateSize();
    }
    
    // calculate width and height based on the input boxes
    public void calculateSize() {
        int maxWidth = -1;
        int totalHeight = 0;

        for (InputBox inputBox : inputs) {
            if (inputBox.getWidth() + inputBox.getXOffset() + inputBox.getXOffsetRight() > maxWidth) {
                maxWidth = inputBox.getWidth() + inputBox.getXOffset() + inputBox.getXOffsetRight();
            }
            totalHeight += inputBox.getHeight() + 2 * inputBox.getYOffset();
        }

        width = maxWidth;
        height = totalHeight;
    }

    public void draw(Graphics g, int overlayX, int overlayY, int activeBox) {
        x = overlayX - width - X_OFFSET;
        y = overlayY + Y_OFFSET;

        // draw the outline
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // draw each box
        int runningY = y;
        for (int i = 0; i < inputs.size(); ++i) {
            InputBox inputBox = inputs.get(i);
            inputs.get(i).draw(g,
                    x,
                    runningY,
                    activeBox == i);
            runningY += inputBox.getHeight() + 2 * inputBox.getYOffset();
        }
    }

    public int[][] getBoxCoords() {
        // x y width height for each input box
        int c[][] = new int[inputs.size()][4];

        int runningY = y;
        for (int i = 0; i < inputs.size(); ++i) {
            InputBox inputBox = inputs.get(i);
            c[i][0] = x + inputBox.getXOffset();
            runningY += inputBox.getYOffset();
            c[i][1] = runningY;
            c[i][2] = inputBox.getWidth();
            c[i][3] = inputBox.getHeight();
            runningY += inputBox.getHeight() + inputBox.getYOffset();
        }

        return c;
    }

    public void updateBox(int index, int keyCode) {
        inputs.get(index).update(keyCode);
    }

    public int getMinX() {
        return inputs.get(0).getValueInt();
    }
    
    public int getMaxX() {
        return inputs.get(1).getValueInt();
    }
    
    public int getMinY() {
        return inputs.get(2).getValueInt();
    }
    
    public int getMaxY() {
        return inputs.get(3).getValueInt();
    }
    
    public int getResX() {
        return inputs.get(4).getValueInt();
    }
    
    public int getResY() {
        return inputs.get(5).getValueInt();
    }

    public int getThreads() {
        int threads = inputs.get(6).getValueInt();
        if (threads > 32) {
            return 32;
        }
        if (threads < 1) {
            return 1;
        }
        return threads;
    }

    public String getFile() {
        return inputs.get(7).getValueString();
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
