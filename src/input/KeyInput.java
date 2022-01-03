package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import graphics.Panel;

public class KeyInput implements KeyListener {
	
	private Panel panel;
	
	private boolean[] keys;
	
	public KeyInput(Panel panel) {
		this.panel = panel;
		
		// initialize keys array
		this.keys = new boolean[256];
		for (int i = 0; i < 256; ++i) {
			keys[i] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!keys[e.getKeyCode()]) {
			panel.keyPressed(e);	
		}
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;		
	}
}
