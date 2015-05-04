/* class JMenuItem
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

package charvax.swing;

import charva.awt.ColorScheme;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Point;


/**
 * An implementation of an item in a menu
 */
public class JMenuItem extends AbstractButton {

    
    /**
     * Create a menu item without text
     */
    public JMenuItem() {
    }

    /**
     * Create a menu item with the specified text
     * 
     * @param text  the label to be displayed in this menu item
     */
    public JMenuItem(String text) {
        setText(text);
        setActionCommand(text);
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
    
        color         = colors.getColor(ColorScheme.MENU);
        selectedColor = colors.getColor(ColorScheme.MENU_SELECTED);
        mnemonicColor = colors.getColor(ColorScheme.MENU_MNEMONIC);
        disabledColor = colors.getColor(ColorScheme.MENU_DISABLED);
    }

    public void paint(Graphics g) {
        StringBuffer sb = new StringBuffer();
        sb.append(' ').append(' ').append(getText());
        final int width = getWidth();
        while (sb.length() < width)
            sb.append(' ');
        
        // draw this menu item
        paintText(g, sb.toString());
    }

    public Dimension getMinimumSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public Dimension getSize() {
        return getMinimumSize();
    }
    
    public int getWidth() {
        int width = super.getWidth();
        if (width == 0)
            return getText().length() + 4;
        
        return width;
    }

    public int getHeight() {
        return 1;
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        if (isShowing()) {
            // get the absolute origin of this component
            Point origin = getLocationOnScreen();
            SwingUtilities.windowForComponent(this).setCursor(origin);
        }
    }
}
