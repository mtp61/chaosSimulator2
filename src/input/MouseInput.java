package input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import graphics.Panel;

public class MouseInput implements MouseListener {

    private Panel panel;
    
    public MouseInput(Panel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.panel.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
