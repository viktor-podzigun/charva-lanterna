/* class JPanel
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

import charva.awt.ColorPair;
import charva.awt.Container;
import charva.awt.FlowLayout;
import charva.awt.Graphics;
import charva.awt.LayoutManager;


/**
 * JPanel is a generic lightweight container
 */
public class JPanel extends JComponent {

    
    /**
     * The default constructor sets the layout manager to FlowLayout
     */
    public JPanel() {
        this(new FlowLayout());
    }

    /**
     * Use this constructor if you want to use a layout manager other than
     * the default FlowLayout.
     */
    public JPanel(LayoutManager layout) {
        layoutMgr = layout;
    }

    /**
     * Draws the border of the panel (if there is one), plus
     * all the contained components.
     */
    public void paint(Graphics g) {
        // Blank out the area of this component, but only if this
        // component's color-pair is different than that of the
        // parent container
        ColorPair colorpair = getColor();
    	Container parent = getParent();
    	if (parent != null && !colorpair.equals(parent.getColor())) {
    	    g.setColor(colorpair);
            g.fillRect(0, 0, getWidth(), getHeight());
    	}

        // draw border and all the components contained by this container
        super.paint(g);
    }
}
