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

package charva.toolkit.lanterna;

import charva.awt.Graphics;
import charva.awt.Point;
import charva.awt.TerminalGraphics;
import charva.awt.TerminalWindow;
import charva.awt.Window;


/**
 * Lanterna {@link TerminalWindow} implementation.
 */
final class LanternaWindow extends TerminalWindow {

    private final LanternaToolkit   toolkit;
    
    
    public LanternaWindow(LanternaToolkit toolkit, Window charvaWindow) {
        super(charvaWindow);
        
        this.toolkit = toolkit;
    }
    
    protected Window getCharvaWindow() {
        return charvaWindow;
    }
    
    @Override
    public void init() {
        super.init();
    }
    
    protected Graphics getGraphics() {
        Graphics g = new TerminalGraphics(this);
        g.setClip(0, 0, charvaWindow.getWidth(), charvaWindow.getHeight());
        return g;
    }
    
    /**
     * Returns absolute cursor position
     */
    protected Point getCursor() {
        final int xy = toolkit.getCursorXY();
        return new Point(xy & 0xffff, (xy >>> 16) & 0xffff);
    }

    /**
     * Returns absolute cursor position
     * <p>
     * Use this overloaded version to avoid allocating a new Point object 
     * on the heap
     */
    protected Point getCursor(Point p) {
        final int xy = toolkit.getCursorXY();
        p.x = xy & 0xffff;
        p.y = (xy >>> 16) & 0xffff;
        return p;
    }

    protected void drawChar(int x, int y, int chr, int attrib) {
        toolkit.drawChar(charvaWindow.getX() + x, charvaWindow.getY() + y, 
                chr, attrib);
    }

    protected void drawString(int x, int y, String str, int attrib) {
        toolkit.drawString(charvaWindow.getX() + x, charvaWindow.getY() + y, 
                str, attrib);
    }

    protected void drawLine(int x, int y, int length, int chr, 
            int attrib, boolean isHorizontal) {
        
        toolkit.drawLine(charvaWindow.getX() + x, charvaWindow.getY() + y, 
                length, chr, attrib, isHorizontal);
    }

    protected void fillBox(int x, int y, int width, int height, int attrib) {
        toolkit.fillBox(charvaWindow.getX() + x, charvaWindow.getY() + y, 
                width, height, attrib);
    }

    protected void setCursor(int x, int y) {
        toolkit.setCursor(x, y);
    }

    protected boolean isCursorVisible() {
        return toolkit.isCursorVisible();
    }
    
    protected void setCursorVisible(boolean isVisible) {
        toolkit.setCursorVisible(isVisible);
    }
    
    protected void sync() {
        toolkit.sync();
    }

    protected void close() {
        toolkit.closeWindow(this);
    }

    protected void show() {
        toolkit.showWindow(this);
    }
}
