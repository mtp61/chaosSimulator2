package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.lang.Math;

public class StringBox implements InputBox {

    private final int x_offset = 50;
    private final int x_offset_right = 5;
    private final int y_offset = 5;
    private final int width = 100;
    private final int height = 20;

    private final int maxLength = 20;

    private String label;
    private String value;

    public StringBox(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public void draw(Graphics g, int x, int y, boolean active) {
        g.setColor(active ? Color.GREEN : Color.WHITE);
        g.fillRect(x + x_offset, y + y_offset, width, height);

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesNewRoman", Font.PLAIN, 12));
        g.drawString(label, x + 10, y + y_offset + 15);

        String valueText = value.length() != 0 ? value : "(none)";
        int valueWidth = g.getFontMetrics().stringWidth(valueText);
        g.drawString(valueText, x + x_offset + width - valueWidth - 5, y + y_offset + 15);

        g.drawRect(x + x_offset, y + y_offset, width, height);
    }

    @Override
    public void update(int keyCode) {
        if (keyCode == KeyEvent.VK_BACK_SPACE) {
            removeChar();
        }
        // add a-z, 0-9, -
        if (keyCode == KeyEvent.VK_MINUS) {
            addChar('-');
        }
        char c = (char) keyCode;
        if (c >= 'A' && c <= 'Z') {
            addChar(Character.toLowerCase(c));
            return;
        }
        if (c >= '0' && c <= '9') {
            addChar(c);
        }
    }
    
    private void removeChar() {
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
    }

    private void addChar(char c) {
        if (value.length() < maxLength) {
            value = value + c;
        }
    }

    @Override
    public int getValueInt() {
        return 0;
    }

    @Override
    public String getValueString() {
        return value;
    }

    @Override
    public int getXOffset() {
        return x_offset;
    }

    @Override
    public int getXOffsetRight() {
        return x_offset_right;
    }

    @Override
    public int getYOffset() {
        return y_offset;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
