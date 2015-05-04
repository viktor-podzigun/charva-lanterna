/* class EmptyBorder
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


public class EmptyBorder implements Border {

    private final int   top;
    private final int   left;
    private final int   bottom;
    private final int   right;
    
    
    public EmptyBorder(Insets insets) {
        this.top    = insets.top;
        this.left   = insets.left;
        this.bottom = insets.bottom;
        this.right  = insets.right;
    }

    public EmptyBorder(int top, int left, int bottom, int right) {
        this.top    = top;
        this.left   = left;
        this.bottom = bottom;
        this.right  = right;
    }

    /**
     * Returns the insets of the border
     */
    public Insets getBorderInsets(Component component) {
        return new Insets(top, left, bottom, right);
    }

    /**
     * Paints the border for the specified component with the specified position
     * and size
     */
    public void paintBorder(Component component, Graphics g,
            int x, int y, int width, int height) {

        // does nothing, but must be here to implement the Border interface
    }
}
