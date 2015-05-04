/* class BorderLayout
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

package charva.awt;


/**
 * A concrete implementation of LayoutManager that lays out its components
 * around its edges. The components are laid out according to their preferred
 * sizes and the constraints of the container's size.
 * <p>
 * The NORTH and SOUTH components may be stretched horizontally; 
 * the WEST and EAST components may be stretched vertically; 
 * the CENTER component may stretch both horizontally and vertically to fill 
 * any space left over.
 */
public class BorderLayout implements LayoutManager2 {
    
    public static final String NORTH    = "North";
    public static final String SOUTH    = "South";
    public static final String EAST     = "East";
    public static final String WEST     = "West";
    public static final String CENTER   = "Center";
    

    private Component   north;
    private Component   south;
    private Component   west;
    private Component   east;
    private Component   center;

    
    public BorderLayout() {
    }

    public void addLayoutComponent(Component component, Object constraint) {
        String constr = (String) constraint;
        
        // Special case: treat null the same as CENTER
        if (constr == null)
            constr = CENTER;

        if (constr.equals(NORTH))
            north = component;
        else if (constr.equals(SOUTH))
            south = component;
        else if (constr.equals(WEST))
            west = component;
        else if (constr.equals(EAST))
            east = component;
        else if (constr.equals(CENTER))
            center = component;
        else {
            throw new IllegalArgumentException(
                    "Cannot add to layout: unknown constraint: " + constr);
        }
    }
    
    /**
     * @deprecated  replaced by <code>addLayoutComponent(Component, Object)</code>.
     */
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, name);
    }

    /**
     * Removes the specified component from this border layout. This method is
     * called when a container calls its <code>remove</code> or
     * <code>removeAll</code> methods. Most applications do not call this
     * method directly.
     * 
     * @param comp  the component to be removed
     * 
     * @see charva.awt.Container#remove(charva.awt.Component)
     */
    public void removeLayoutComponent(Component comp) {
        if (comp == center) {
            center = null;
        } else if (comp == north) {
            north = null;
        } else if (comp == south) {
            south = null;
        } else if (comp == east) {
            east = null;
        } else if (comp == west) {
            west = null;
        }
    }

    /**
     * Calculate the minimum-size rectangle that can enclose all the components
     * in the given container
     */
    public Dimension minimumLayoutSize(Container parent) {
        int       width      = 0;
        int       height     = 0;
        Dimension northSize  = null;
        Dimension eastSize   = null;
        Dimension southSize  = null;
        Dimension westSize   = null;
        Dimension centerSize = null;

        // calculate the minimum height

        if (west != null) {
            westSize = west.getMinimumSize();
            height   = westSize.height;
            width    = westSize.width;
        }

        if (center != null) {
            centerSize = center.getMinimumSize();
            if (centerSize.height > height)
                height = centerSize.height;
            width += centerSize.width;
        }

        if (east != null) {
            eastSize = east.getMinimumSize();
            if (eastSize.height > height)
                height = eastSize.height;
            width += eastSize.width;
        }

        if (north != null) {
            northSize = north.getMinimumSize();
            height += northSize.height;
            if (northSize.width > width)
                width = northSize.width;
        }

        if (south != null) {
            southSize = south.getMinimumSize();
            height += southSize.height;
            if (southSize.width > width)
                width = southSize.width;
        }

        Insets insets = parent.getInsets();
        height += insets.top + insets.bottom;
        width += insets.left + insets.right;

        return new Dimension(width, height);
    }

    /**
     * This is called when the size of the container has already been set. It
     * just lays out the components according to the specified alignment, hgap
     * and vgap.
     */
    public void layoutContainer(Container parent) {
        Dimension size   = parent.getSize();
        Insets    insets = parent.getInsets();

        // Expand all the containers that are in this container. In the AWT
        // BorderLayout, other components such as buttons are expanded too, but
        // that is not practical with text components.

        expandContainers(parent);

        // now lay out the components inside the container

        int availableHeight = size.height - insets.top - insets.bottom;
        int availableWidth  = size.width - insets.left - insets.right;
        int northbottom     = insets.top;
        int westright       = insets.left;

        if (north != null) {
            int padding = size.width - north.getSize().width;
            north.setLocation(padding / 2, insets.top);
            availableHeight -= north.getSize().height;
            northbottom += north.getSize().height;
        }

        if (south != null) {
            int padding = size.width - south.getSize().width;
            south.setLocation(padding / 2, size.height - insets.bottom
                    - south.getSize().height);
            availableHeight -= south.getSize().height;
        }

        if (west != null) {
            west.setLocation(insets.left, northbottom
                    + (availableHeight - west.getSize().height) / 2);
            availableWidth -= west.getSize().width;
            westright += west.getSize().width;
        }

        if (east != null) {
            east.setLocation(
                    size.width - insets.right - east.getSize().width,
                    northbottom + (availableHeight - east.getSize().height) / 2);
            availableWidth -= east.getSize().width;
        }

        if (center != null) {
            center.setLocation(westright
                    + (availableWidth - center.getSize().width) / 2,
                    northbottom + (availableHeight - center.getSize().height)
                            / 2);
        }
    }

    /**
     * Invalidates the layout, indicating that if the layout manager has cached
     * information it should be discarded.
     */
    public void invalidateLayout(Container target) {

    }

    /**
     * Expand all the containers that are inside the specified container
     */
    private void expandContainers(Container container) {
        Dimension size            = container.getSize();
        Insets    insets          = container.getInsets();
        int       availableHeight = size.height - insets.top - insets.bottom;
        int       availableWidth  = size.width - insets.left - insets.right;

        if (north != null) {
            if (north instanceof Container) {
                ((Container) north).setWidth(availableWidth);
                ((Container) north).setHeight(north.getMinimumSize().height);
                ((Container) north).doLayout();
            }
            availableHeight -= north.getSize().height;
        }

        if (south != null) {
            if (south instanceof Container) {
                ((Container) south).setWidth(availableWidth);
                ((Container) south).setHeight(south.getMinimumSize().height);
                ((Container) south).doLayout();
            }
            availableHeight -= south.getSize().height;
        }

        if (west != null) {
            if (west instanceof Container) {
                ((Container) west).setWidth(west.getMinimumSize().width);
                ((Container) west).setHeight(availableHeight);
                ((Container) west).doLayout();
            }
            availableWidth -= west.getSize().width;
        }

        if (east != null) {
            if (east instanceof Container) {
                ((Container) east).setWidth(east.getMinimumSize().width);
                ((Container) east).setHeight(availableHeight);
                ((Container) east).doLayout();
            }
            availableWidth -= east.getSize().width;
        }

        if (center != null && center instanceof Container) {
            ((Container) center).setSize(availableWidth, availableHeight);
            ((Container) center).doLayout();
        }
    }
}
