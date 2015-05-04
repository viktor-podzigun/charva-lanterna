/* class JCheckBox
 *
 * Copyright (C) 2001  R M Pitman
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

import charva.awt.ColorScheme;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.event.KeyEvent;


/**
 * An implementation of a checkbox - an object that is always in one of two
 * states (SELECTED or DESELECTED) and which displays its state to the user.
 */
public class JCheckBox extends AbstractButton {

    /**
     * The default constructor creates a deselected checkbox with an
     * empty label
     */
    public JCheckBox() {
        this("", false);
    }

    /**
     * Use this constructor when you want to initialize the label
     */
    public JCheckBox(String text) {
        this(text, false);
    }

    /**
     * Use this constructor when you want to set both the label and the value
     */
    public JCheckBox(String label, boolean value) {
        super.setText(label);
        super.setSelected(value);
    }

    /**
     * Return the size of the text field. Overrides the method in the
     * Component superclass.
     */
    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public int getWidth() {
        Insets insets = super.getInsets();
        return super.getText().length() + 4 + insets.left + insets.right;
    }

    public int getHeight() {
        Insets insets = super.getInsets();
        return 1 + insets.top + insets.bottom;
    }

    public Dimension getMinimumSize() {
        return this.getSize();
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
    
        selectedColor = colors.getColor(ColorScheme.BUTTON);
    }

    public void paint(Graphics g) {
        // draw the border if it exists
        super.paint(g);

        // draw this button's label
        paintText(g, (isSelected() ? "[x] " : "[ ] ") + getText());
    }

    protected void processKeyEvent(KeyEvent ke) {
        // First call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        final int key = ke.getKeyCode();
        
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

        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
            if (!super.isEnabled()) {
                return;
            }

            // change the state and post an ItemEvent
            setSelected(!isSelected());
        }
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        // get the absolute origin of this component
        Point  origin = getLocationOnScreen();
        Insets insets = super.getInsets();
        
        SwingUtilities.windowForComponent(this).setCursor(
                origin.addOffset(1 + insets.left, insets.top));
    }
}
