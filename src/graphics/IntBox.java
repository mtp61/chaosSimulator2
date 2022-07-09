package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.lang.Math;

public class IntBox implements InputBox {

    private final int x_offset = 50;
    private final int x_offset_right = 5;
    private final int y_offset = 5;
    private final int width = 60;
    private final int height = 20;

    private final int maxValue = 999999;

    private String label;
    private int value;

    public IntBox(String label, int value) {
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

        String valueText = String.format("%d", value);
        int valueWidth = g.getFontMetrics().stringWidth(valueText);
        g.drawString(valueText, x + x_offset + width - valueWidth - 5, y + y_offset + 15);

        g.drawRect(x + x_offset, y + y_offset, width, height);
    }

    @Override
    public void update(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_BACK_SPACE:
                value /= 10;
                return;
            case KeyEvent.VK_MINUS:
                value = -value;
                return;
            case KeyEvent.VK_0:
                addDigit(0);
                return;
            case KeyEvent.VK_1:
                addDigit(1);
                return;
            case KeyEvent.VK_2:
                addDigit(2);
                return;
            case KeyEvent.VK_3:
                addDigit(3);
                return;
            case KeyEvent.VK_4:
                addDigit(4);
                return;
            case KeyEvent.VK_5:
                addDigit(5);
                return;
            case KeyEvent.VK_6:
                addDigit(6);
                return;
            case KeyEvent.VK_7:
                addDigit(7);
                return;
            case KeyEvent.VK_8:
                addDigit(8);
                return;
            case KeyEvent.VK_9:
                addDigit(9);
                return;
        }
    }

    private void addDigit(int digit) {
        if (10 * Math.abs(value) > maxValue) {
            return;
        }

        value *= 10;
        value += value >= 0 ? digit : -digit;
    }

    @Override
    public int getValueInt() {
        return value;
    }

    @Override
    public String getValueString() {
        return "";
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
