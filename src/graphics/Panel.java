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
        overlay.draw(g, width - overlay.getWidth(), 0, activeBox);
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
        switch (e.getKeyCode()) {
            // reset arm
            case KeyEvent.VK_R:
                world.resetWorld();
                break;
            // clear magnets
            case KeyEvent.VK_C:
                world.clearMagnets();
                break;
            // pause
            case KeyEvent.VK_SPACE:
                paused = !paused;
                break;
            // save
            case KeyEvent.VK_S:
                saveMagnets();
                break;
            // load
            case KeyEvent.VK_L:
                loadMagnets();
                break;
            // plot
            case KeyEvent.VK_P:
                System.out.println("plotting...");
                Plotter.plot(world.getMagnets(), 
                        overlay.getMinX(), overlay.getMaxX(),
                        overlay.getMinY(), overlay.getMaxY(),
                        overlay.getResX(), overlay.getResY());
                break;
            // overlay number
            case KeyEvent.VK_0:
                if (activeBox != -1) {
                    overlay.number(activeBox, 0);
                }
                break;
            case KeyEvent.VK_1:
                if (activeBox != -1) {
                    overlay.number(activeBox, 1);
                }				
                break;
            case KeyEvent.VK_2:
                if (activeBox != -1) {
                    overlay.number(activeBox, 2);
                }				
                break;
            case KeyEvent.VK_3:
                if (activeBox != -1) {
                    overlay.number(activeBox, 3);
                }				
                break;
            case KeyEvent.VK_4:
                if (activeBox != -1) {
                    overlay.number(activeBox, 4);
                }				
                break;
            case KeyEvent.VK_5:
                if (activeBox != -1) {
                    overlay.number(activeBox, 5);
                }				
                break;
            case KeyEvent.VK_6:
                if (activeBox != -1) {
                    overlay.number(activeBox, 6);
                }				
                break;
            case KeyEvent.VK_7:
                if (activeBox != -1) {
                    overlay.number(activeBox, 7);
                }				
                break;
            case KeyEvent.VK_8:
                if (activeBox != -1) {
                    overlay.number(activeBox, 8);
                }				
                break;
            case KeyEvent.VK_9:
                if (activeBox != -1) {
                    overlay.number(activeBox, 9);
                }				
                break;
            case KeyEvent.VK_NUMPAD0:
                if (activeBox != -1) {
                    overlay.number(activeBox, 0);
                }
                break;
            case KeyEvent.VK_NUMPAD1:
                if (activeBox != -1) {
                    overlay.number(activeBox, 1);
                }				
                break;
            case KeyEvent.VK_NUMPAD2:
                if (activeBox != -1) {
                    overlay.number(activeBox, 2);
                }				
                break;
            case KeyEvent.VK_NUMPAD3:
                if (activeBox != -1) {
                    overlay.number(activeBox, 3);
                }				
                break;
            case KeyEvent.VK_NUMPAD4:
                if (activeBox != -1) {
                    overlay.number(activeBox, 4);
                }				
                break;
            case KeyEvent.VK_NUMPAD5:
                if (activeBox != -1) {
                    overlay.number(activeBox, 5);
                }				
                break;
            case KeyEvent.VK_NUMPAD6:
                if (activeBox != -1) {
                    overlay.number(activeBox, 6);
                }				
                break;
            case KeyEvent.VK_NUMPAD7:
                if (activeBox != -1) {
                    overlay.number(activeBox, 7);
                }				
                break;
            case KeyEvent.VK_NUMPAD8:
                if (activeBox != -1) {
                    overlay.number(activeBox, 8);
                }				
                break;
            case KeyEvent.VK_NUMPAD9:
                if (activeBox != -1) {
                    overlay.number(activeBox, 9);
                }				
                break;
            // overlay negative
            case KeyEvent.VK_MINUS:
                if (activeBox != -1) {
                    overlay.negative(activeBox);;
                }
                break;
            // overlay delete
            case KeyEvent.VK_BACK_SPACE:
                if (activeBox != -1) {
                    overlay.delete(activeBox);
                }
                break;
            // overlay deselect
            case KeyEvent.VK_ESCAPE:
                activeBox = -1;
                break;
        }
    }
    
    private void saveMagnets() {
        try {
            String hash = Magnet.magnetHash(world.getMagnets()); 
            
            FileWriter f = new FileWriter(
                    String.format("magnets/%s.txt", hash));
            BufferedWriter b = new BufferedWriter(f);
            
            Gson gson = new Gson();					
            b.write(gson.toJson(world.getMagnets()));
                        
            b.close();
            f.close();
            
            System.out.println(
                    String.format("saved to \"magnets/%s.txt\"", hash));
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    private void loadMagnets() {
        try {					
            FileReader f = new FileReader("magnets/magnets.txt");
            BufferedReader b = new BufferedReader(f);
            
            Gson gson = new Gson();
            world.setMagnets(gson.fromJson(b.readLine(), 
                    new TypeToken<ArrayList<Magnet>>(){}.getType()));
            
            b.close();
            f.close();
            
            System.out.println("Loaded configuration from \"magnets/magnets.txt\"");
        } catch (FileNotFoundException err) {
            System.out.println("File not found");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    private double dist(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public boolean getPaused() { return paused; }
}
