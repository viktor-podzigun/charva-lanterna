/* class GridBagLayout
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
 * This is an approximation of the AWT GridBagLayout layout manager.
 * See the documentation of the AWT GridBagLayout for details.
 */
public class GridBagLayout implements LayoutManager2 {

    /**
     * This field holds the overrides to the column minimum widths.
     */
    public int[]        columnWidths;

    /**
     * This field holds the overrides to the row minimum heights.
     */
    public int[]        rowHeights;

    /**
     * This field is not used in the CHARVA package but is present to allow
     * compile-time compatibility with AWT.
     */
    public double[]     columnWeights;

    /**
     * This field is not used in the CHARVA package but is present to allow
     * compile-time compatibility with AWT.
     */
    public double[]     rowWeights;

    /**
     * As components are added, they are stored in this array
     */
    private ArrayList   components = new ArrayList();

    /**
     * As components are added, their constraint objects are stored in
     * this array
     */
    private ArrayList   constraints = new ArrayList();

    /**
     * The number of rows in the grid (calculated from all the added
     * components and their gridx, gridy, gridwidth and gridheight
     * constraints).
     */
    private int         rows;

    /**
     * The number of rows in the grid (calculated from all the added
     * components and their gridx, gridy, gridwidth and gridheight
     * constraints).
     */
    private int         columns;

    /**
     * This array holds the row heights that we calculate.
     */
    private int[]       calculatedRowHeights;

    /**
     * This array holds the columns widths that we calculate.
     */
    private int[]       calculatedColumnWidths;

    /**
     * This array holds the row weights that we calculate.
     */
    private double[]    calculatedRowWeights;

    /**
     * This array holds the column weights that we calculate.
     */
    private double[]    calculatedColumnWeights;

    private double      totalweightx = 0.0;
    private double      totalweighty = 0.0;
    
    
    public GridBagLayout() {
    }

    /**
     * Calculate the geometry for the specified list of Components,
     * and return the size of the rectangle that encloses all the
     * Components.
     */
    public Dimension minimumLayoutSize(Container parent) {
        // First work out the dimensions of the grid (i.e. the number of
        // rows and columns).  We do this by iterating through all the
        // added components, and inspecting their gridx, gridy, gridwidth and
        // gridheight constraints.
        rows    = 0;
        columns = 0;
        
        final int constraintsCount = constraints.size();
        for (int i = 0; i < constraintsCount; i++) {
            GridBagConstraints gbc = (GridBagConstraints) constraints.get(i);
            if (gbc.gridx + gbc.gridwidth > columns)
                columns = gbc.gridx + gbc.gridwidth;

            if (gbc.gridy + gbc.gridheight > rows)
                rows = gbc.gridy + gbc.gridheight;
        }

        // now that we know the number of rows and columns, we can create
        // arrays to hold the row heights and column widths

        calculatedRowHeights    = new int[rows];
        calculatedRowWeights    = new double[rows];
        calculatedColumnWidths  = new int[columns];
        calculatedColumnWeights = new double[columns];

        /* Create a pair of arrays, each the same length as the number of
         * contained components, to hold the "width-left" and "height-left"
         * values for each component.
         */
        final int componentsCount = components.size();
        int width_left[]  = new int[componentsCount];
        int height_left[] = new int[componentsCount];

        for (int i = 0; i < componentsCount; i++) {
            Component c = (Component) components.get(i);
            GridBagConstraints gbc = (GridBagConstraints) constraints.get(i);

            // calculate the minimum width & height required for this
            // component
            Insets    insets  = gbc.insets;
            Dimension minsize = c.getMinimumSize();
            
            width_left[i]  = minsize.width + insets.left + insets.right;
            height_left[i] = minsize.height + insets.top + insets.bottom;
        }

        // now iterate through all the rows and allocate heights to each row

        for (int row = 0; row < rows; row++) {
            // Iterate through the constraints and find those whose bottom edge
            // is in the current row. The one with the maximum height_left
            // value determines the height of this row.
            calculatedRowHeights[row] = 0;
            final int constraintsSize = constraints.size();
            for (int i = 0; i < constraintsSize; i++) {
                GridBagConstraints gbc = (GridBagConstraints)constraints.get(i);

                if (row == gbc.gridy + gbc.gridheight - 1) {
                    // this component's bottom edge is in the current row
                    if (height_left[i] > calculatedRowHeights[row])
                        calculatedRowHeights[row] = height_left[i];
                }
            }

            // now that we have calculated the height of this row, subtract
            // this row-height from the height_left value of each component
            // which extends to or below this row
            for (int i = 0; i < constraintsSize; i++) {
                GridBagConstraints gbc = (GridBagConstraints)constraints.get(i);

                if (row >= gbc.gridy && row < gbc.gridy + gbc.gridheight)
                    height_left[i] -= calculatedRowHeights[row];
            }
        }

        // now iterate through all the columns and allocate widths to each
        // column
        for (int column = 0; column < columns; column++) {
            // Iterate through the constraints and find those whose right edge
            // is in the current column. The one with the maximum width_left
            // value determines the width of this column
            calculatedColumnWidths[column] = 0;
            final int constraintsSize = constraints.size();
            for (int i = 0; i < constraintsSize; i++) {
                GridBagConstraints gbc = (GridBagConstraints)constraints.get(i);

                if (column == gbc.gridx + gbc.gridwidth - 1) {
                    // this component's right edge is in the current column
                    if (width_left[i] > calculatedColumnWidths[column])
                        calculatedColumnWidths[column] = width_left[i];
                }
            }

            // Now that we have calculated the width of this column, subtract
            // this column-width from the width_left value of each component
            // which extends to or to the right of this column
            for (int i = 0; i < constraintsSize; i++) {
                GridBagConstraints gbc = (GridBagConstraints)constraints.get(i);

                if (column >= gbc.gridx && column < gbc.gridx + gbc.gridwidth)
                    width_left[i] -= calculatedColumnWidths[column];
            }
        }

        /* Iterate through all the components and calculate the row
         * and column weights.
         */
        final int constraintsSize = constraints.size();
        for (int c = 0; c < constraintsSize; c++) {
            GridBagConstraints gbc = (GridBagConstraints)constraints.get(c);

            for (int i = gbc.gridx; i < gbc.gridx + gbc.gridwidth; i++) {
                if (gbc.weightx > calculatedColumnWeights[i])
                    calculatedColumnWeights[i] = gbc.weightx;
            }

            for (int i = gbc.gridy; i < gbc.gridy + gbc.gridheight; i++) {
                if (gbc.weighty > calculatedRowWeights[i])
                    calculatedRowWeights[i] = gbc.weighty;
            }
        }

        // now just add up all the column widths and row heights to find
        // the minimum size of the container
        Insets insets      = parent.getInsets();
        int    totalwidth  = insets.left + insets.right;
        int    totalheight = insets.top + insets.bottom;

        totalweighty = totalweightx = 0.0;
        for (int i = 0; i < columns; i++) {
            totalwidth += calculatedColumnWidths[i];
            totalweightx += calculatedColumnWeights[i];
        }

        for (int i = 0; i < rows; i++) {
            totalheight  += calculatedRowHeights[i];
            totalweighty += calculatedRowWeights[i];
        }

        return new Dimension(totalwidth, totalheight);
    }

    /**
     * Set the positions of the contained components
     */
    public void layoutContainer(Container parent) {
        Insets    insets       = parent.getInsets();
        Dimension size         = parent.getSize();
        Dimension minsize      = minimumLayoutSize(parent);
        int       extraColumns = size.width - minsize.width;
        int       extraRows    = size.height - minsize.height;

        final int count = components.size();
        for (int c = 0; c < count; c++) {
            Component comp = (Component) components.get(c);
            GridBagConstraints gbc = (GridBagConstraints)constraints.get(c);

            // calculate the boundaries of the grid cell that this
            // component occupies
            int left = insets.left;
            if (totalweightx == 0.0)
                left += extraColumns / 2;

            for (int i = 0; i < gbc.gridx; i++) {
                left += calculatedColumnWidths[i];
                if (totalweightx != 0.0) {
                    left += (extraColumns * calculatedColumnWeights[i]) 
                            / totalweightx;
                }
            }

            int right = left;
            for (int i = 0; i < gbc.gridwidth; i++) {
                right += calculatedColumnWidths[gbc.gridx + i];
                if (totalweightx != 0.0) {
                    right += (extraColumns * calculatedColumnWeights[gbc.gridx + i]) 
                            / totalweightx;
                }
            }

            int top = insets.top;
            if (totalweighty == 0.0)
                top += extraRows / 2;

            for (int i = 0; i < gbc.gridy; i++) {
                top += calculatedRowHeights[i];
                if (totalweighty != 0.0) {
                    top += (extraRows * calculatedRowWeights[i]) 
                            / totalweighty;
                }
            }

            int bottom = top;
            for (int i = 0; i < gbc.gridheight; i++) {
                bottom += calculatedRowHeights[gbc.gridy + i];
                if (totalweighty != 0.0) {
                    bottom += (extraRows * calculatedRowWeights[gbc.gridy + i]) 
                            / totalweighty;
                }
            }

            if (comp instanceof Container) {
                Container cont = (Container) comp;

                // get the contained container to lay itself out at its
                // preferred size, if it is not already laid out
                if (cont.isValid() == false)
                    cont.setSize(cont.getMinimumSize());

                switch (gbc.fill) {
                    case GridBagConstraints.NONE:
                        break;

                    case GridBagConstraints.HORIZONTAL:
                        cont.setWidth(right - left);
                        break;

                    case GridBagConstraints.VERTICAL:
                        cont.setHeight(bottom - top);
                        break;

                    case GridBagConstraints.BOTH:
                        cont.setSize(right - left, bottom - top);
                        break;

                    default:
                        throw new IllegalArgumentException("Invalid fill parameter");
                }
                cont.doLayout();
            }

            // calculate the x position of the component's origin 
            // (i.e. top left corner)
            int cx = 0;
            switch (gbc.anchor) {
                case GridBagConstraints.WEST:
                case GridBagConstraints.NORTHWEST:
                case GridBagConstraints.SOUTHWEST:
                    cx = left + gbc.insets.left;
                    break;

                case GridBagConstraints.EAST:
                case GridBagConstraints.NORTHEAST:
                case GridBagConstraints.SOUTHEAST:
                    cx = right - gbc.insets.right - comp.getSize().width;
                    break;

                case GridBagConstraints.CENTER:
                case GridBagConstraints.NORTH:
                case GridBagConstraints.SOUTH:
                    cx = (left + gbc.insets.left) +
                            (right - gbc.insets.right);
                    cx -= comp.getSize().width;
                    cx = cx / 2;
                    break;

                default:
                    throw new IllegalArgumentException("Invalid anchor paremeter");
            }

            // calculate the y position of the component's origin 
            // (i.e. top left corner)
            int cy = 0;
            switch (gbc.anchor) {
                case GridBagConstraints.NORTH:
                case GridBagConstraints.NORTHWEST:
                case GridBagConstraints.NORTHEAST:
                    cy = top + gbc.insets.top;
                    break;

                case GridBagConstraints.SOUTH:
                case GridBagConstraints.SOUTHWEST:
                case GridBagConstraints.SOUTHEAST:
                    cy = bottom - gbc.insets.bottom - comp.getSize().height;
                    break;

                case GridBagConstraints.CENTER:
                case GridBagConstraints.WEST:
                case GridBagConstraints.EAST:
                    cy = (top + gbc.insets.top) +
                            (bottom - gbc.insets.bottom);
                    cy -= comp.getSize().height;
                    cy = cy / 2;
            }

            comp.setLocation(cx, cy);
        }
    }

    public void addLayoutComponent(Component component, Object constraint) {
        components.add(component);

        /* Make a copy of the constraints object passed to us, so that the
         * caller can re-use it for other components.
         */
        if (!(constraint instanceof GridBagConstraints)) {
            throw new IllegalArgumentException(
                    "Expected constraint to be an instance of GridBagConstraints, " +
                    "but was instance of " + constraint.getClass().getName());
        }

        GridBagConstraints constr = (GridBagConstraints) constraint;
        GridBagConstraints newc = new GridBagConstraints();
        newc.gridx      = constr.gridx;
        newc.gridy      = constr.gridy;
        newc.gridwidth  = constr.gridwidth;
        newc.gridheight = constr.gridheight;
        newc.weightx    = constr.weightx;
        newc.weighty    = constr.weighty;
        newc.anchor     = constr.anchor;
        newc.fill       = constr.fill;
        newc.insets     = new Insets(constr.insets.top, constr.insets.left, 
                constr.insets.bottom, constr.insets.right);
        newc.ipadx      = constr.ipadx;
        newc.ipady      = constr.ipady;
        
        constraints.add(newc);
    }

    /**
     * Adds the specified component with the specified name to the layout
     * 
     * @param name  the name of the component
     * @param comp  the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from this layout.
     * <p>
     * Most applications do not call this method directly.
     * 
     * @param comp  the component to be removed.
     * 
     * @see charva.awt.Container#remove(charva.awt.Component)
     */
    public void removeLayoutComponent(Component comp) {
        //TODO: implement me
    }

    /**
     * Invalidates the layout, indicating that if the layout manager has cached
     * information it should be discarded
     */
    public void invalidateLayout(Container target) {

    }
}
