package simulation;

import graphics.Renderer;

public class Main {

    private static final long framerate = 60;
    private static final double tickrate = 60.0;
    private static final long ticksPerRender = 10;
    
    public static void main(String[] args) {
        World world = new World();
        Renderer renderer = new Renderer(world);
        
        // render forever
        long timePerFrame = 1000L / framerate;
        long time, wait; 
        while (true) {
            time = System.currentTimeMillis();
            
            if (!renderer.getPanel().getPaused()) {
                for (int i = 0; i < ticksPerRender; ++i) {
                    world.tick(tickrate);
                }
            }
            renderer.render();
            
            wait = timePerFrame - (System.currentTimeMillis() - time);
            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1); // if sleep is interrupted just close the program stupid java
                }
            }
        }
    }
}
