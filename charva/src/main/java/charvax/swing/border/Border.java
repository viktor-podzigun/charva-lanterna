/* interface Border
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


/**
 * Interface describing an object capable of rendering a border
 * around the edges of a component
 */
public interface Border {
    
    /**
     * Returns the insets of the border.
     */
    public Insets getBorderInsets(Component component);

    /**
     * Paints the border for the specified component with the specified
     * position and size
     *
     * @param component The component around which this border is being drawn. 
     *                  The background color of the border is obtained from the
     *                  component. If the line color of this border is also 
     *                  set to null, the foreground color of the border is also 
     *                  obtained from component
     * @param g         graphics context in which drawing is performed
     * @param x         the x coordinate of the top left corner
     * @param y         the y coordinate of the top left corner
     * @param width     the width of the border box
     * @param height    the height of the border box
     */
    public void paintBorder(Component component, Graphics g, 
            int x, int y, int width, int height);
}
