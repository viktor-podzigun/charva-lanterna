/* class JButton
 *
 * Copyright (C) 2001-2003  R M Pitman
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

/*
 * Modified Jul 14, 2003 by Tadpole Computer, Inc.
 * Modifications Copyright 2003 by Tadpole Computer, Inc.
 *
 * Modifications are hereby licensed to all parties at no charge under
 * the same terms as the original.
 *
 * Modified to allow JButton to properly support color properties.
 */

package charvax.swing;

import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Toolkit;
import charva.awt.event.ItemEvent;
import charva.awt.event.KeyEvent;


/**
 * An implementation of a "pushbutton" with a text label
 */
public class JButton extends AbstractButton {

    /**
     * Creates a button with an empty label
     */
    public JButton() {
        super("");
    }

    public JButton(String text) {
        super(text);
    }
    
    public JButton(Action action) {
        super(action);
    }
    
    /**
     * Return the size of the button. The button is always one line
     * high, and two columns wider than the label, plus the size
     * of the border (if any).
     */
    public Dimension getMinimumSize() {
        Insets insets = getInsets();
        String text = getText();
        int textLen = (text != null ? text.length() : 0);
        int width = textLen + 2 + insets.left + insets.right;
        int height = 1 + insets.top + insets.bottom;
        
        return new Dimension(width, height);
    }

    public void paint(Graphics g) {
        // draw border if it exists
        super.paint(g);
        
        // draw this button's label
        paintText(g, " " + getText() + " ");
    }

    /**
     * Processes key events occurring on this object by dispatching them
     * to any registered KeyListener objects.
     */
    protected void processKeyEvent(KeyEvent ke) {
        // First call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed()) {
            return;
        }

        int key = ke.getKeyCode();
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown()) 
                || key == KeyEvent.VK_UP 
                || key == KeyEvent.VK_LEFT) {
            
            transferFocusBackward();
            return;
        }

        if (key == KeyEvent.VK_TAB 
                || key == KeyEvent.VK_DOWN 
                || key == KeyEvent.VK_RIGHT) {
            
            transferFocus();
            return;
        }
        
        // Post an ItemEvent if ENTER was pressed (but only if the
        // button is not disabled)
        if (isEnabled() 
                && (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE)) {
            
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new ItemEvent(this, this, ItemEvent.SELECTED));
        }
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        // get the absolute origin of this component
        Point  origin = getLocationOnScreen();
        Insets insets = getInsets();
        
        SwingUtilities.windowForComponent(this).setCursor(
                origin.addOffset(1 + insets.left, 0 + insets.top));
    }
}
