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

package charvax.swing;

import charva.awt.Graphics;
import charva.awt.Toolkit;
import charva.awt.Window;
import charva.awt.event.AWTEvent;
import charva.awt.event.MouseEvent;


/**
 * Pop-up window that is used for pop-up components: JPopupMenu, JComboBox, etc.
 */
class JPopupWindow extends Window {

    private boolean     hasShadow = true;
    
    
    JPopupWindow(Window owner) {
        super(owner);
    }
    
    public void setShadow(boolean enable) {
        hasShadow = enable;
    }
    
    public boolean hasShadow() {
        return hasShadow;
    }
    
    public void paint(Graphics g) {
        g.setColor(getColor());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw all the contained components
        super.paint(g);
    }
    
    protected void processEvent(AWTEvent evt) {
        final int id = evt.getID();
        
        if (id >= MouseEvent.MOUSE_FIRST && id <= MouseEvent.MOUSE_LAST) {
            MouseEvent me = (MouseEvent)evt;

            // If the mouse has been clicked outside the pop-up window
            if (!contains(me.getX(), me.getY()) 
                    && id == MouseEvent.MOUSE_CLICKED 
                    && me.getClickCount() == 1
                    && me.getButton() == MouseEvent.BUTTON1) {
                
                // close pop-up window
                if (checkCloseByMouse(me)) {
                    close();
                }
                
                if (me.isConsumed()) {
                    return;
                }

                // send this mouse event to the owner window
                Window owner = getOwner();
                if (owner != null) {
                    int screenX = me.getX() + getX();
                    int screenY = me.getY() + getY();
                    Toolkit term = Toolkit.getDefaultToolkit();
                    term.getSystemEventQueue().postEvent(
                            new MouseEvent(owner, me.getID(), 
                                    screenX - owner.getX(), 
                                    screenY - owner.getY(), 
                                    me.getClickCount(), me.getButton(), 
                                    me.getModifiers()));
                }
                
                return;
            }
        }
        
        super.processEvent(evt);
    }
    
    protected boolean checkCloseByMouse(MouseEvent me) {
        return true;
    }
    
    public void close() {
        hide();
    }
}
