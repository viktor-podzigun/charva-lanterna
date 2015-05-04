/* class CompoundBorder
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

package charvax.swing.border;

import charva.awt.Component;
import charva.awt.Graphics;
import charva.awt.Insets;


public class CompoundBorder implements Border {

    private Border  outsideBorder;
    private Border  insideBorder;
    
    
    public CompoundBorder(Border outsideBorder, Border insideBorder) {
        this.outsideBorder = outsideBorder;
        this.insideBorder  = insideBorder;
    }

    /**
     * Returns the insets of the compound border
     */
    public Insets getBorderInsets(Component component) {
        Insets outside = outsideBorder.getBorderInsets(component);
        Insets inside  = insideBorder.getBorderInsets(component);
        
        return new Insets(outside.top + inside.top, outside.left + inside.left,
                outside.bottom + inside.bottom, outside.right + inside.right);
    }

    /**
     * Paints the border for the specified component with the specified position
     * and size.
     * <p>
     * Actually the component parameter is unused but is here for
     * compatibility with Swing.
     */
    public void paintBorder(Component component, Graphics g,
            int x, int y, int width, int height) {

        outsideBorder.paintBorder(component, g, x, y, width,
                height);

        // now paint the inside border, making allowance for the space already
        // used by the outside border
        Insets outer = outsideBorder.getBorderInsets(component);
        insideBorder.paintBorder(component, g, x + outer.left, y
                + outer.top, width - outer.left - outer.right, height
                - outer.top - outer.bottom);
    }
}
