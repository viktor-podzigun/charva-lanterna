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
import charva.awt.GraphicsConstants;
import charva.awt.event.ActionEvent;


/**
 * A menu item that can be selected or deselected. If selected, the menu
 * item typically appears with a checkmark next to it. If unselected or
 * deselected, the menu item appears without a checkmark.
 */
public class JCheckBoxMenuItem extends JMenuItem {

    /**
     * Constructs a new JCheckBoxMenuItem with no text
     * (the text can be set later with the setText() method)
     */
    public JCheckBoxMenuItem() {
        super();
    }

    /**
     * Constructs a new JCheckBoxMenuItem with the specified text
     */
    public JCheckBoxMenuItem(String text) {
        this(text, false);
    }

    /**
     * Creates a check box menu item with the specified text and 
     * selection state
     *
     * @param text  the text of the check box menu item.
     * @param b     the selected state of the check box menu item
     */
    public JCheckBoxMenuItem(String text, boolean b) {
        super(text);
        
        setSelected(b);
    }

    public void paint(Graphics g) {
        // draw this menu item
        super.paint(g);
        
        if (isSelected()) {
            paintChar(g, 0, GraphicsConstants.VS_TICK);
        }
    }

    public void fireActionPerformed(ActionEvent ae) {
        // Notify all the registered ActionListeners.
        super.fireActionPerformed(ae);
        
        // change the state
        setSelected(!isSelected());
    }
}
