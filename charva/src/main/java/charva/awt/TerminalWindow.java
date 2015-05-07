/* Copyright (C) 2015 charva-lanterna
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charva.awt;


/**
 * Abstract terminal window.
 * <p>
 * Used to perform drawing. Subclasses should implement all the details.
 */
public abstract class TerminalWindow {
    
    protected final Window      charvaWindow;
    
    
    protected TerminalWindow(Window charvaWindow) {
        this.charvaWindow = charvaWindow;
    }
    
    protected abstract Graphics getGraphics();
    
    protected abstract void drawChar(int x, int y, int chr, int attrib);

    protected abstract void drawString(int x, int y, String str, int attrib);

    protected abstract void drawLine(int x, int y, int length, int chr, 
            int attrib, boolean isHorizontal);

    /**
     * Fills out a box using the specified rectangular area and 
     * color attributes
     *
     * @param x         the x coordinate
     * @param y         the y coordinate
     * @param width     the width
     * @param height    the height
     * @param attrib    the color attributes (foreground + background)
     */
    protected abstract void fillBox(int x, int y, int width, int height, 
            int attrib);

    protected abstract void setCursor(int x, int y);
    
    protected abstract Point getCursor();
    
    protected abstract Point getCursor(Point p);
    
    protected abstract boolean isCursorVisible();
    
    protected abstract void setCursorVisible(boolean isVisible);
    
    protected abstract void sync();

    protected abstract void close();
    
    /**
     * Shows window on the terminal screen and blocks current execution
     * until window is closed.
     */
    protected abstract void show();
    
    protected void init() {
        charvaWindow.showInternal();
    }

    protected void paint() {
        // validate first, in case invalidate() was called
        charvaWindow.validate();

        Graphics g = getGraphics();
        charvaWindow.paint(g);
    }

    protected void processIdleEvent() {
        Toolkit.getDefaultToolkit().processIdleEvent();
    }

    protected void processKeyEvent(int key, int modifiers) {
        Toolkit.getDefaultToolkit().processKeyEvent(charvaWindow, 
                key, modifiers);
    }

    protected void processMouseEvent(int button, int clickCount, int x, int y, 
            int modifiers) {
        
        Toolkit.getDefaultToolkit().processMouseEvent(charvaWindow, 
                button, clickCount, x, y, modifiers);
    }
}
