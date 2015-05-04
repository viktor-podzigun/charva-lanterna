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
import charva.awt.Insets;


/**
 * Base class for Border implementations
 */
public abstract class AbstractBorder implements Border {

    protected ColorPair         lineColor;
    protected Insets            insets;

    
    protected AbstractBorder(ColorPair color, Insets insets) {
        if (insets == null)
            throw new NullPointerException("insets");
        
        this.lineColor = color;
        this.insets    = insets;
    }

    public ColorPair getLineColor() {
        return lineColor;
    }

    public void setLineColor(ColorPair colorPair) {
        lineColor = colorPair;
    }

    /**
     * Returns the insets of the border
     */
    public Insets getBorderInsets(Component component) {
        return new Insets(insets.top, insets.left, insets.bottom, insets.right);
    }
}
