/* class BoxLayout
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

import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Insets;
import charva.awt.LayoutManager;


/**
 * A concrete implementation of LayoutManager that lays out its
 * components horizontally or vertically
 */
public class BoxLayout implements LayoutManager {

    public static final int X_AXIS = 100;
    public static final int Y_AXIS = 101;
    
    private int     axis;

    
    /**
     * Creates a layout manager that will lay out its components either
     * left-to-right or top-to-bottom, as specified by the axis parameter
     *
     * @param target The container to be laid out. This parameter is not
     *               used, but is present for compatibility with the 
     *               Java SWING's BoxLayout constructor).
     * @param axis   The axis in which components will be laid out. Must be
     *               X_AXIS or Y_AXIS.
     */
    public BoxLayout(Container target, int axis) {
        if (axis != X_AXIS && axis != Y_AXIS)
            throw new IllegalArgumentException("axis: " + axis);

        this.axis = axis;
    }

    /**
     * Not used by this class.
     *
     * @param name the name of the component
     * @param comp the component
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Not used by this class.
     *
     * @param comp the component
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Calculate the minimum-size rectangle that can enclose all the
     * components in the given container
     */
    public Dimension minimumLayoutSize(Container parent) {
        Dimension size = new Dimension(0, 0);

        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];
            Dimension d = c.getMinimumSize();
            if (axis == X_AXIS) {
                size.width += d.width;
                if (d.height > size.height)
                    size.height = d.height;
            } else {
                size.height += d.height;
                if (d.width > size.width)
                    size.width = d.width;
            }
        }

        // take into account the border frame (if any)
        Insets insets = parent.getInsets();
        size.width  += insets.left + insets.right;
        size.height += insets.top + insets.bottom;
        return size;
    }

    /**
     * This is called when the size of the container has already been
     * calculated.  It just lays out the components according to the
     * specified alignment.
     */
    public void layoutContainer(Container parent) {
        Dimension containersize = parent.getSize();
        Dimension minsize       = minimumLayoutSize(parent);

        // ensure that the container is at least as large as the
        // minimum size
        if (containersize.width < minsize.width)
            containersize.width = minsize.width;
        if (containersize.height < minsize.height)
            containersize.height = minsize.height;
        
        parent.setSize(containersize);

        Component[] components = parent.getComponents();
        int hoffset = parent.getInsets().left;
        int voffset = parent.getInsets().top;
        
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];

            // get the contained container to lay itself out at its
            // preferred size
            if (c instanceof Container) {
                Container cont = (Container) c;
                cont.setSize(cont.getMinimumSize());
                cont.doLayout();
            }

            Dimension componentsize = c.getSize();
            if (axis == X_AXIS) {
                float alignment = c.getAlignmentY();
                if (alignment == Component.TOP_ALIGNMENT) {
                    voffset = parent.getInsets().top;
                } else if (alignment == Component.CENTER_ALIGNMENT) {
                    voffset = (containersize.height -
                            componentsize.height) / 2;
                } else if (alignment == Component.BOTTOM_ALIGNMENT) {
                    voffset = containersize.height -
                            parent.getInsets().bottom -
                            componentsize.height;
                }
                
                c.setLocation(hoffset, voffset);
                hoffset += componentsize.width;
            
            } else {
                float alignment = c.getAlignmentX();
                if (alignment == Component.LEFT_ALIGNMENT) {
                    hoffset = parent.getInsets().left;
                } else if (alignment == Component.CENTER_ALIGNMENT) {
                    hoffset = (containersize.width -
                            componentsize.width) / 2;
                } else if (alignment == Component.RIGHT_ALIGNMENT) {
                    hoffset = containersize.width -
                            parent.getInsets().right -
                            componentsize.width;
                }
                
                c.setLocation(hoffset, voffset);
                voffset += componentsize.height;
            }
        }
    }
}
