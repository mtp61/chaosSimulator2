package graphics;

import java.awt.Graphics;

public interface InputBox {

    public void draw(Graphics g, int x, int y, boolean active);

    // adds/removes a character to the value
    public void update(int keyCode);

    // returns the value as an int or string, based on the type
    public int getValueInt();
    public String getValueString();

    public int getXOffset();
    public int getXOffsetRight();
    public int getYOffset();
    public int getWidth();
    public int getHeight();
}
