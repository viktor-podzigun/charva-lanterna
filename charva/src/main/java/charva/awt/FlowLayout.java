/* class FlowLayout
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

import java.util.ArrayList;


/**
 * A concrete implementation of LayoutManager that lays out its
 * components left-to-right.
 */
public class FlowLayout implements LayoutManager {
    
    public static final int LEFT    = 1;
    public static final int CENTER  = 2;
    public static final int RIGHT   = 3;

    /**
     * Alignment of components (LEFT, RIGHT or CENTER)
     */
    private int     align = CENTER;

    /**
     * Horizontal gap between components
     */
    private int     hgap = 1;

    /**
     * Vertical gap between components
     */
    private int     vgap;

    
    /**
     * Default constructor. Sets alignment to CENTER, hgap to 1,
     * and vgap to 0.
     */
    public FlowLayout() {
        this(CENTER, 1, 0);
    }

    /**
     * Use this constructor when you want to set the alignment and the
     * horizontal and vertical gaps.
     */
    public FlowLayout(int align, int hgap, int vgap) {
        this.align = align;
        this.hgap  = hgap;
        this.vgap  = vgap;
    }

    /**
     * Sets the alignment for this layout. Allowable values are
     * FlowLayout.LEFT, FlowLayout.CENTER and FlowLayout.RIGHT.
     */
    public void setAlignment(int align) {
        this.align = align;
    }

    /**
     * Gets the alignment for this layout.
     */
    public int getAlignment() {
        return align;
    }

    /**
     * Adds the specified component to the layout. Not used by this class
     * 
     * @param name  the name of the component
     * @param comp  the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout. Not used by
     * this class.
     * 
     * @param comp the component to remove
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Calculate the minimum-size rectangle that can enclose all the
     * components in the given container.
     */
    public Dimension minimumLayoutSize(Container parent) {
        int width  = 0;
        int height = 0;

        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            Dimension d = components[i].getMinimumSize();

            // make allowance for the gap between this component and the
            // previous component
            if (i != 0)
                width += hgap;

            width += d.width;
            if (d.height > height)
                height = d.height;
        }

        /* Take into account the border frame (if any).
         */
        Insets insets = parent.getInsets();
        height += insets.top + insets.bottom;
        width += insets.left + insets.right;

        return new Dimension(width, height);
    }

    /**
     * Lay out the components according to the specified alignment, hgap
     * and vgap.
     * This is called when the size of the container has already been
     * calculated.
     * It lays out the components in a row, one at a time, until it
     * determines that there is not enough space left in the row.
     * Then it moves to the next row. If there is not enough vertical
     * space in the container to lay out all of the components, it
     * removes the remaining components from the container; they don't
     * appear at all.
     */
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int availableWidth = parent.getSize().width -
                insets.left - insets.right;
        int widthLeft = availableWidth;
        int heightLeft = parent.getSize().height -
                insets.top - insets.bottom;

        int voffset = insets.top;

        Component[] components = parent.getComponents();
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < components.length; i++) {
            Component c = components[i];

            // get the contained container to lay itself out at its
            // preferred size, if it is not already laid out
            if (c instanceof Container) {
                Container cont = (Container) c;
                if (cont.isValid() == false) {
                    cont.setSize(cont.getMinimumSize());
                    cont.doLayout();
                }
            }

            // determine the width required to lay out the current
            // component (including the gap between this component and
            // the previous component)
            int requiredWidth = c.getSize().width;
            if (i != 0)
                requiredWidth += hgap;

            if (requiredWidth > widthLeft) {
                int rowHeight = 0;
                if (localArrayList.size() != 0) {
                    rowHeight = layoutRow(parent, localArrayList,
                            widthLeft, heightLeft, voffset);
                    localArrayList.clear();
                }
                
                voffset    += rowHeight + vgap;
                widthLeft   = availableWidth;
                heightLeft -= rowHeight + vgap;
            }
            widthLeft -= requiredWidth;

            // Build up a temporary list of components for this row.
            localArrayList.add(c);
        }
        
        layoutRow(parent, localArrayList, widthLeft, heightLeft, voffset);
    }

    /**
     * private function to layout a single row of components
     *
     * @return The height of the laid-out row
     */
    private int layoutRow(Container container, ArrayList components,
                          int widthleft, int heightleft, int voffset) {

        int    hoffset   = 0;
        int    rowHeight = 0;
        Insets insets    = container.getInsets();

        switch (align) {
            case LEFT:
                hoffset = insets.left;
                break;
            case CENTER:
                hoffset = insets.left + widthleft / 2;
                break;
            case RIGHT:
                hoffset = insets.left + widthleft;
                break;
        }

        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component) components.get(i);
            if (c.getSize().height > rowHeight)
                rowHeight = c.getSize().height;

            if (rowHeight > heightleft) {
                container.remove(c);	// we have run out of space
                continue;
            }

            c.setLocation(hoffset, voffset);
            hoffset += c.getSize().width + hgap;
        }
        
        return rowHeight;
    }
}
