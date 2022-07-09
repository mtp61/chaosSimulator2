package graphics;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import input.KeyInput;
import input.MouseInput;
import simulation.World;

public class Renderer {

    private Panel panel;
    private JFrame frame;	
    private KeyInput keyInput;
    private MouseInput mouseInput;
    
    private int startWidth = 800;
    private int startHeight = 800;
        
    public Renderer(World world) {	
        // create the Panel and JFrame
        this.panel = new Panel(world, startWidth, startHeight);
        this.frame = new JFrame();
        
        // setup the JFrame
        this.frame.add(this.panel);
        this.frame.setSize(startWidth, startHeight);
        this.frame.setResizable(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                panel.setWidth(frame.getWidth());
                panel.setHeight(frame.getHeight());
            }
        });

        frame.setVisible(true);
        
        // setup the panel
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        
        // setup the input
        this.keyInput = new KeyInput(this.panel);
        this.panel.addKeyListener(this.keyInput);
        this.mouseInput = new MouseInput(this.panel);
        this.panel.addMouseListener(this.mouseInput);
    }
    
    // render
    public void render() {
        this.panel.repaint();
    }
    
    public Panel getPanel() { return panel; }
}
