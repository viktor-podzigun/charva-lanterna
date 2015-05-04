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

package charvax.swing.border;

import charva.awt.ColorPair;
import charva.awt.Component;
import charva.awt.Graphics;
import charva.awt.Insets;


/**
 * Double line border implementation
 */
public class DoubleLineBorder extends AbstractBorder {

    public DoubleLineBorder(ColorPair color) {
        super(color, new Insets(1, 1, 1, 1));
    }

    public DoubleLineBorder(ColorPair color, Insets insets) {
        super(color, insets);
    }

    public void paintBorder(Component component, Graphics g, 
            int x, int y, int width, int height) {

        // if the border color has not been set explicitly,
        // the foreground and background colors are obtained from component
        final ColorPair colorpair;
        if (lineColor == null) {
            colorpair = ColorPair.create(component.getForeground(), 
                    component.getBackground());
        } else {
            colorpair = lineColor;
        }
        
        g.setColor(colorpair);
        
        Insets insets = this.insets;
        g.drawDoubleRect(
                x + insets.left - 1, 
                y + insets.top - 1, 
                width - insets.left - insets.right + 2, 
                height - insets.top - insets.bottom + 2);
    }
}
