package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import graphics.Panel;
import plotting.Plotter;

import javax.swing.JPanel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import simulation.Magnet;
import simulation.World;

public class Panel extends JPanel {

    private static final long serialVersionUID = 1L;

    private World world;
    private PlotOverlay overlay;
    
    private int width, height;
    
    private boolean paused = false;
    private int activeBox = -1;
    
    public Panel(World world, int width, int height) {
        this.world = world;
        this.width = width;
        this.height = height;
        
        overlay = new PlotOverlay();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        // clear
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);
        
        drawSimulation(g);
        
        // overlay
        overlay.draw(g, width, 0, activeBox);
    }
    
    public void drawSimulation(Graphics g) {
        int offsetX = width / 2;
        int offsetY = height / 2;
        
        // draw arm
        g.setColor(Color.GRAY);
        g.fillOval((int) world.getArmX() - 10 + offsetX, (int) world.getArmY() - 10 + offsetY, 20, 20);
        
        // draw magnets
        for (Magnet m : world.getMagnets()) {
            g.setColor(m.getCoef() < 0 ? Color.BLUE : Color.RED);
            g.fillOval(m.getXPos() - 10 + offsetX, m.getYPos() - 10 + offsetY, 20, 20);
        }
        
        // draw home (where the arm returns to)
        g.setColor(Color.BLACK);
        g.fillOval((int) world.getHomeX() - 5 + offsetX, (int) world.getHomeY() - 5 + offsetY, 10, 10);
    }
    
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // check if clicking a box
        int inputBoxes[][] = overlay.getBoxCoords();
        for (int i = 0; i < inputBoxes.length; ++i) {
            if (mouseX >= inputBoxes[i][0]
                    && mouseX <= inputBoxes[i][0] + inputBoxes[i][2]
                    && mouseY >= inputBoxes[i][1]
                    && mouseY <= inputBoxes[i][1] + inputBoxes[i][3]) {
                activeBox = i;
                return;
            }
        }
        activeBox = -1;
        
        // check if in overlay panel
        if (mouseX >= width - overlay.getWidth() - PlotOverlay.X_OFFSET
                && mouseX <= width - PlotOverlay.X_OFFSET
                && mouseY >= PlotOverlay.Y_OFFSET
                && mouseY <= overlay.getHeight() + PlotOverlay.Y_OFFSET) {
            return;
        }
        
        // check if we are deleting a previous magnet
        ArrayList<Magnet> magnets = world.getMagnets();
        for (int i = 0; i < magnets.size(); ++i) {
            if (dist(
                    mouseX - width / 2,
                    mouseY - height / 2,
                    magnets.get(i).getXPos(),
                    magnets.get(i).getYPos()) < Magnet.radius) {
                magnets.remove(i);
                return;
            }
        }
        
        // otherwise add a new magnet
        world.addMagnet(new Magnet(
                mouseX - width / 2, 
                mouseY - height / 2, 
                e.getButton() == 1 ? -world.getDefaultCoef() : world.getDefaultCoef()));
    }
    
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (activeBox == -1) {
            switch (keyCode) {
                // reset arm
                case KeyEvent.VK_R:
                    world.resetWorld();
                    return;
                // clear magnets
                case KeyEvent.VK_C:
                    world.clearMagnets();
                    return;
                // pause
                case KeyEvent.VK_SPACE:
                    paused = !paused;
                    return;
                // save
                case KeyEvent.VK_S:
                    saveMagnets();
                    return;
                // load
                case KeyEvent.VK_L:
                    loadMagnets();
                    return;
                // plot
                case KeyEvent.VK_P:
                    System.out.println("plotting...");
                    Plotter.plot(world.getMagnets(), 
                            overlay.getMinX(), overlay.getMaxX(),
                            overlay.getMinY(), overlay.getMaxY(),
                            overlay.getResX(), overlay.getResY());
                    return;
                // quit
                case KeyEvent.VK_Q:
                    System.exit(0);
            }
        } else {
            // reset active box on escape
            if (keyCode == KeyEvent.VK_ESCAPE) {
                activeBox = -1;
                return;
            }

            overlay.updateBox(activeBox, keyCode);
        }
    }
    
    private void saveMagnets() {
        String file = overlay.getFile();

        // generate a file name if none provided
        if (file.length() == 0) {
            file = Magnet.magnetHash(world.getMagnets());
        }

        Magnet.saveMagnets(world.getMagnets(), file);
    }
    
    private void loadMagnets() {
        String file = overlay.getFile();
        if (file.length() == 0) {
            System.out.println("No file name specified");
            return;
        }

        ArrayList<Magnet> magnets = Magnet.loadMagnets(file);
        if (magnets != null) {
            world.setMagnets(magnets);
        }
    }
    
    private double dist(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public boolean getPaused() { return paused; }
}
