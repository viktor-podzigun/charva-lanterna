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

package charvax.swing;

import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Insets;
import charva.awt.LayoutManager;
import charva.awt.Rectangle;


/**
 * The layout manager used by <code>JScrollPane</code>.
 * <code>JScrollPaneLayout</code> is responsible for nine components: a
 * viewport, two scrollbars, a row header, a column header, and four "corner"
 * components.
 * 
 * @see JScrollPane
 * @see JViewport
 */
public class ScrollPaneLayout implements LayoutManager, ScrollPaneConstants {

    /**
     * The scrollpane's viewport child. Default is an empty 
     * <code>JViewport</code>.
     * @see JScrollPane#setViewport
     */
    protected JViewport     viewport;

    /**
     * The scrollpane's vertical scrollbar child. Default is a
     * <code>JScrollBar</code>.
     * @see JScrollPane#setVerticalScrollBar
     */
    protected JScrollBar    vsb;

    /**
     * The scrollpane's horizontal scrollbar child. Default is a
     * <code>JScrollBar</code>.
     * @see JScrollPane#setHorizontalScrollBar
     */
    protected JScrollBar    hsb;

    /**
     * The row header child. Default is <code>null</code>.
     * @see JScrollPane#setRowHeader
     */
    protected JViewport     rowHead;

    /**
     * The column header child. Default is <code>null</code>.
     * @see JScrollPane#setColumnHeader
     */
    protected JViewport     colHead;

    /**
     * The component to display in the lower left corner. Default is
     * <code>null</code>.
     * @see JScrollPane#setCorner
     */
    protected Component     lowerLeft;

    /**
     * The component to display in the lower right corner. Default is
     * <code>null</code>.
     * @see JScrollPane#setCorner
     */
    protected Component     lowerRight;

    /**
     * The component to display in the upper left corner. Default is
     * <code>null</code>.
     * @see JScrollPane#setCorner
     */
    protected Component     upperLeft;

    /**
     * The component to display in the upper right corner. Default is
     * <code>null</code>.
     * @see JScrollPane#setCorner
     */
    protected Component     upperRight;

    /**
     * The display policy for the vertical scrollbar. The default is
     * <code>JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED</code>.
     * <p>
     * This field is obsolete, please use the <code>JScrollPane</code> field
     * instead.
     * 
     * @see JScrollPane#setVerticalScrollBarPolicy
     */
    protected int           vsbPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;

    /**
     * The display policy for the horizontal scrollbar. The default is
     * <code>JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED</code>.
     * <p>
     * This field is obsolete, please use the <code>JScrollPane</code> field
     * instead.
     * 
     * @see JScrollPane#setHorizontalScrollBarPolicy
     */
    protected int           hsbPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;

    /**
     * This method is invoked after the ScrollPaneLayout is set as the
     * LayoutManager of a <code>JScrollPane</code>. It initializes all of the
     * internal fields that are ordinarily set by
     * <code>addLayoutComponent</code>. For example:
     * 
     * <pre>
     * ScrollPaneLayout mySPLayout = new ScrollPanelLayout() {
     *     public void layoutContainer(Container p) {
     *         super.layoutContainer(p);
     *         // do some extra work here ...
     *     }
     * };
     * scrollpane.setLayout(mySPLayout):
     * </pre>
     */
    public void syncWithScrollPane(JScrollPane sp) {
        viewport    = sp.getViewport();
        vsb         = sp.getVerticalScrollBar();
        hsb         = sp.getHorizontalScrollBar();
        rowHead     = sp.getRowHeader();
        colHead     = sp.getColumnHeader();
        lowerLeft   = sp.getCorner(LOWER_LEFT_CORNER);
        lowerRight  = sp.getCorner(LOWER_RIGHT_CORNER);
        upperLeft   = sp.getCorner(UPPER_LEFT_CORNER);
        upperRight  = sp.getCorner(UPPER_RIGHT_CORNER);
        vsbPolicy   = sp.getVerticalScrollBarPolicy();
        hsbPolicy   = sp.getHorizontalScrollBarPolicy();
    }

    /**
     * Removes an existing component. When a new component, such as the left
     * corner, or vertical scrollbar, is added, the old one, if it exists, must
     * be removed.
     * <p>
     * This method returns <code>newC</code>. If <code>oldC</code> is not
     * equal to <code>newC</code> and is non-<code>null</code>, it will be
     * removed from its parent.
     * 
     * @param oldC  the <code>Component</code> to replace
     * @param newC  the <code>Component</code> to add
     * @return the <code>newC</code>
     */
    protected Component addSingletonComponent(Component oldC, Component newC) {
        if (oldC != null && oldC != newC)
            oldC.getParent().remove(oldC);
        
        return newC;
    }

    /**
     * Adds the specified component to the layout. The layout is identified
     * using one of:
     * <ul>
     * <li>JScrollPane.VIEWPORT
     * <li>JScrollPane.VERTICAL_SCROLLBAR
     * <li>JScrollPane.HORIZONTAL_SCROLLBAR
     * <li>JScrollPane.ROW_HEADER
     * <li>JScrollPane.COLUMN_HEADER
     * <li>JScrollPane.LOWER_LEFT_CORNER
     * <li>JScrollPane.LOWER_RIGHT_CORNER
     * <li>JScrollPane.UPPER_LEFT_CORNER
     * <li>JScrollPane.UPPER_RIGHT_CORNER
     * </ul>
     * 
     * @param s     the component identifier
     * @param comp  the the component to be added
     * 
     * @exception IllegalArgumentException
     *              if <code>s</code> is an invalid key
     */
    public void addLayoutComponent(String s, Component c) {
        if (s.equals(VIEWPORT)) {
            viewport = (JViewport) addSingletonComponent(viewport, c);
        } else if (s.equals(VERTICAL_SCROLLBAR)) {
            vsb = (JScrollBar) addSingletonComponent(vsb, c);
        } else if (s.equals(HORIZONTAL_SCROLLBAR)) {
            hsb = (JScrollBar) addSingletonComponent(hsb, c);
        } else if (s.equals(ROW_HEADER)) {
            rowHead = (JViewport) addSingletonComponent(rowHead, c);
        } else if (s.equals(COLUMN_HEADER)) {
            colHead = (JViewport) addSingletonComponent(colHead, c);
        } else if (s.equals(LOWER_LEFT_CORNER)) {
            lowerLeft = addSingletonComponent(lowerLeft, c);
        } else if (s.equals(LOWER_RIGHT_CORNER)) {
            lowerRight = addSingletonComponent(lowerRight, c);
        } else if (s.equals(UPPER_LEFT_CORNER)) {
            upperLeft = addSingletonComponent(upperLeft, c);
        } else if (s.equals(UPPER_RIGHT_CORNER)) {
            upperRight = addSingletonComponent(upperRight, c);
        } else {
            throw new IllegalArgumentException("invalid layout key " + s);
        }
    }

    /**
     * Removes the specified component from the layout.
     * 
     * @param c  the component to remove
     */
    public void removeLayoutComponent(Component c) {
        if (c == viewport) {
            viewport = null;
        } else if (c == vsb) {
            vsb = null;
        } else if (c == hsb) {
            hsb = null;
        } else if (c == rowHead) {
            rowHead = null;
        } else if (c == colHead) {
            colHead = null;
        } else if (c == lowerLeft) {
            lowerLeft = null;
        } else if (c == lowerRight) {
            lowerRight = null;
        } else if (c == upperLeft) {
            upperLeft = null;
        } else if (c == upperRight) {
            upperRight = null;
        }
    }

    /**
     * Returns the vertical scrollbar-display policy.
     * 
     * @return an integer giving the display policy
     * @see #setVerticalScrollBarPolicy
     */
    public int getVerticalScrollBarPolicy() {
        return vsbPolicy;
    }

    /**
     * Returns the horizontal scrollbar-display policy.
     * 
     * @return an integer giving the display policy
     * @see #setHorizontalScrollBarPolicy
     */
    public int getHorizontalScrollBarPolicy() {
        return hsbPolicy;
    }

    /**
     * Returns the <code>JViewport</code> object that displays the scrollable
     * contents.
     * 
     * @return the <code>JViewport</code> object that displays the scrollable
     *         contents
     * @see JScrollPane#getViewport
     */
    public JViewport getViewport() {
        return viewport;
    }

    /**
     * Returns the <code>JScrollBar</code> object that handles horizontal
     * scrolling.
     * 
     * @return the <code>JScrollBar</code> object that handles horizontal
     *         scrolling
     * @see JScrollPane#getHorizontalScrollBar
     */
    public JScrollBar getHorizontalScrollBar() {
        return hsb;
    }

    /**
     * Returns the <code>JScrollBar</code> object that handles vertical
     * scrolling.
     * 
     * @return the <code>JScrollBar</code> object that handles vertical
     *         scrolling
     * @see JScrollPane#getVerticalScrollBar
     */
    public JScrollBar getVerticalScrollBar() {
        return vsb;
    }

    /**
     * Returns the <code>JViewport</code> object that is the row header.
     * 
     * @return the <code>JViewport</code> object that is the row header
     * @see JScrollPane#getRowHeader
     */
    public JViewport getRowHeader() {
        return rowHead;
    }

    /**
     * Returns the <code>JViewport</code> object that is the column header.
     * 
     * @return the <code>JViewport</code> object that is the column header
     * @see JScrollPane#getColumnHeader
     */
    public JViewport getColumnHeader() {
        return colHead;
    }

    /**
     * Returns the <code>Component</code> at the specified corner.
     * 
     * @param key
     *            the <code>String</code> specifying the corner
     * @return the <code>Component</code> at the specified corner, as defined
     *         in {@link ScrollPaneConstants}; if <code>key</code> is not one
     *         of the four corners, <code>null</code> is returned
     * @see JScrollPane#getCorner
     */
    public Component getCorner(String key) {
        if (key.equals(LOWER_LEFT_CORNER)) {
            return lowerLeft;
        } else if (key.equals(LOWER_RIGHT_CORNER)) {
            return lowerRight;
        } else if (key.equals(UPPER_LEFT_CORNER)) {
            return upperLeft;
        } else if (key.equals(UPPER_RIGHT_CORNER)) {
            return upperRight;
        } else {
            return null;
        }
    }

    /**
     * The minimum size of a <code>ScrollPane</code> is the size of the insets
     * plus minimum size of the viewport, plus the scrollpane's viewportBorder
     * insets, plus the minimum size of the visible headers, plus the minimum
     * size of the scrollbars whose displayPolicy isn't NEVER.
     * 
     * @param parent
     *            the <code>Container</code> that will be laid out
     * @return a <code>Dimension</code> object specifying the minimum size
     */
    public Dimension minimumLayoutSize(Container parent) {
        // sync the (now obsolete) policy fields with the JScrollPane
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Insets insets = parent.getInsets();
        //Insets insets = new Insets(0, 0, 0, 0);
        int minWidth = insets.left 
            + (insets.right > 0 ? (insets.right - 1) : insets.right);
        int minHeight = insets.top 
            + (insets.bottom > 0 ? (insets.bottom - 1) : insets.bottom);

        // if there's a viewport add its minimumSize
        if (viewport != null) {
            Dimension size = viewport.getMinimumSize();
            minWidth += size.width;
            minHeight += size.height;
        }

        // if there's a JScrollPane.viewportBorder, add its insets
//        Border viewportBorder = scrollPane.getViewportBorder();
        Insets vpbInsets = null;
//        if (viewportBorder != null) {
//            vpbInsets = viewportBorder.getBorderInsets(parent);
//            minWidth  += vpbInsets.left + vpbInsets.right;
//            minHeight += vpbInsets.top + vpbInsets.bottom;
//        }

        // if a header exists and it's visible, factor its minimum size in
        if ((rowHead != null) && rowHead.isVisible()) {
            Dimension size = rowHead.getMinimumSize();
            minWidth += size.width;
            minHeight = Math.max(minHeight, size.height);
        }

        if ((colHead != null) && colHead.isVisible()) {
            Dimension size = colHead.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            minHeight += size.height;
        }

        // if a scrollbar might appear, factor its minimum size in
        if (vsb != null && vsbPolicy != VERTICAL_SCROLLBAR_NEVER) {
            Dimension size = vsb.getMinimumSize();
            if (vpbInsets == null || vpbInsets.right < size.width) {
                minWidth += size.width;
            }
            
            minHeight = Math.max(minHeight, size.height);
        }

        if (hsb != null && hsbPolicy != VERTICAL_SCROLLBAR_NEVER) {
            Dimension size = hsb.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            
            if (vpbInsets == null || vpbInsets.bottom < size.height)
                minHeight += size.height;
        }

        return new Dimension(minWidth, minHeight);
    }

    /**
     * Lays out the scrollpane. The positioning of components depends on the
     * following constraints:
     * <ul>
     * <li> The row header, if present and visible, gets its preferred width and
     * the viewport's height.
     * 
     * <li> The column header, if present and visible, gets its preferred height
     * and the viewport's width.
     * 
     * <li> If a vertical scrollbar is needed, i.e. if the viewport's extent
     * height is smaller than its view height or if the
     * <code>displayPolicy</code> is ALWAYS, it's treated like the row header
     * with respect to its dimensions and is made visible.
     * 
     * <li> If a horizontal scrollbar is needed, it is treated like the column
     * header (see the paragraph above regarding the vertical scrollbar).
     * 
     * <li> If the scrollpane has a non-<code>null</code>
     * <code>viewportBorder</code>,
     * then space is allocated for that.
     * 
     * <li> The viewport gets the space available after accounting for the
     * previous constraints.
     * 
     * <li> The corner components, if provided, are aligned with the ends of the
     * scrollbars and headers. If there is a vertical scrollbar, the right
     * corners appear; if there is a horizontal scrollbar, the lower corners
     * appear; a row header gets left corners, and a column header gets upper
     * corners.
     * </ul>
     * 
     * @param parent  the <code>Container</code> to lay out
     */
    public void layoutContainer(Container parent) {
        // sync the (now obsolete) policy fields with the JScrollPane
        JScrollPane scrollPane = (JScrollPane) parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Rectangle availR = scrollPane.getBounds();
        availR.x = availR.y = 0;

        Insets insets = parent.getInsets();
        //Insets insets = new Insets(0, 0, 0, 0);
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left 
            + (insets.right > 0 ? (insets.right - 1) : insets.right);
        availR.height -= insets.top 
            + (insets.bottom > 0 ? (insets.bottom - 1) : insets.bottom);

        boolean leftToRight = true;
        
        // If there's a visible column header remove the space it needs from the
        // top of availR. The column header is treated as if it were fixed
        // height, arbitrary width.
        Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);

        if ((colHead != null) && (colHead.isVisible())) {
            int colHeadHeight = Math.min(availR.height, 
                    colHead.getPreferredSize().height);
            colHeadR.height = colHeadHeight;
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }

        // If there's a visible row header remove the space it needs from the
        // left or right of availR. The row header is treated as if it were
        // fixed width, arbitrary height.
        Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);

        if ((rowHead != null) && (rowHead.isVisible())) {
            int rowHeadWidth = Math.min(availR.width, 
                    rowHead.getPreferredSize().width);
            rowHeadR.width = rowHeadWidth;
            availR.width -= rowHeadWidth;
            if (leftToRight) {
                rowHeadR.x = availR.x;
                availR.x += rowHeadWidth;
            } else {
                rowHeadR.x = availR.x + availR.width;
            }
        }

        // If there's a JScrollPane.viewportBorder, remove the space it 
        // occupies for availR.
//        Border viewportBorder = scrollPane.getViewportBorder();
        Insets vpbInsets;
//        if (viewportBorder != null) {
//            vpbInsets = viewportBorder.getBorderInsets(parent);
//            availR.x += vpbInsets.left;
//            availR.y += vpbInsets.top;
//            availR.width -= vpbInsets.left + vpbInsets.right;
//            availR.height -= vpbInsets.top + vpbInsets.bottom;
//        } else {
            vpbInsets = new Insets(0, 0, 0, 0);
//        }

        // At this point availR is the space available for the viewport and
        // scrollbars. rowHeadR is correct except for its height and y and
        // colHeadR is correct except for its width and x. Once we're through
        // computing the dimensions of these three parts we can go back and set
        // the dimensions of rowHeadR.height, rowHeadR.y, colHeadR.width,
        // colHeadR.x and the bounds for the corners.
        // 
        // We'll decide about putting up scrollbars by comparing the viewport
        // views preferred size with the viewports extent size (generally just
        // its size). Using the preferredSize is reasonable because layout
        // proceeds top down - so we expect the viewport to be laid out next.
        // And we assume that the viewports layout manager will give the view
        // it's preferred size. One exception to this is when the view
        // implements Scrollable and
        // Scrollable.getViewTracksViewport{Width,Height} methods return true.
        // If the view is tracking the viewports width we don't bother with a
        // horizontal scrollbar, similarly if view.getViewTracksViewport(Height)
        // is true we don't bother with a vertical scrollbar.
        
        Component view = (viewport != null) ? viewport.getView() : null;
        Dimension viewPrefSize = (view != null) ? 
                view.getPreferredSize() : new Dimension(0, 0);

        Dimension extentSize = (viewport != null) ? 
                availR.getSize() : new Dimension(0, 0);

        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        boolean isEmpty = (availR.width < 0 || availR.height < 0);
        Scrollable sv;
        
        // Don't bother checking the Scrollable methods if there is no room
        // for the viewport, we aren't going to show any scrollbars in this
        // case anyway.
        if (!isEmpty && view instanceof Scrollable) {
            sv = (Scrollable) view;
            viewTracksViewportWidth  = sv.getScrollableTracksViewportWidth();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        } else {
            sv = null;
        }

        // If there's a vertical scrollbar and we need one, allocate space for
        // it (we'll make it visible later). A vertical scrollbar is considered
        // to be fixed width, arbitrary height.
        Rectangle vsbR = new Rectangle(0, availR.y, 0, 0);

        boolean vsbNeeded;
        if (isEmpty) {
            vsbNeeded = false;
        } else if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
            vsbNeeded = true;
        } else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
            vsbNeeded = false;
        } else { // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
            vsbNeeded = !viewTracksViewportHeight
                    && (viewPrefSize.height > extentSize.height);
        }

        if (vsb != null && vsbNeeded) {
            adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            extentSize = availR.getSize();
        }

        // If there's a horizontal scrollbar and we need one, allocate space for
        // it (we'll make it visible later). A horizontal scrollbar is
        // considered to be fixed height, arbitrary width.

        Rectangle hsbR = new Rectangle(availR.x, 0, 0, 0);
        boolean hsbNeeded;
        if (isEmpty) {
            hsbNeeded = false;
        } else if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
            hsbNeeded = true;
        } else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
            hsbNeeded = false;
        } else { // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
            hsbNeeded = !viewTracksViewportWidth
                    && (viewPrefSize.width > extentSize.width);
        }

        if ((hsb != null) && hsbNeeded) {
            adjustForHSB(true, availR, hsbR, vpbInsets);

            // If we added the horizontal scrollbar then we've implicitly
            // reduced the vertical space available to the viewport. As a
            // consequence we may have to add the vertical scrollbar, if that
            // hasn't been done so already. Of course we don't bother with any
            // of this if the vsbPolicy is NEVER.
            if (vsb != null && !vsbNeeded
                    && vsbPolicy != VERTICAL_SCROLLBAR_NEVER) {

                extentSize = availR.getSize();
                vsbNeeded  = viewPrefSize.height > extentSize.height;
                if (vsbNeeded)
                    adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            }
        }

        // Set the size of the viewport first, and then recheck the Scrollable
        // methods. Some components base their return values for the Scrollable
        // methods on the size of the Viewport, so that if we don't ask after
        // resetting the bounds we may have gotten the wrong answer.
        if (viewport != null) {
            viewport.setBounds(availR);

            if (sv != null) {
                extentSize = availR.getSize();

                boolean oldHSBNeeded = hsbNeeded;
                boolean oldVSBNeeded = vsbNeeded;
                viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
                viewTracksViewportHeight = 
                    sv.getScrollableTracksViewportHeight();
                
                if (vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
                    boolean newVSBNeeded = !viewTracksViewportHeight
                            && (viewPrefSize.height > extentSize.height);
                    
                    if (newVSBNeeded != vsbNeeded) {
                        vsbNeeded = newVSBNeeded;
                        adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets,
                                leftToRight);
                        extentSize = availR.getSize();
                    }
                }
                
                if (hsb != null && hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                    boolean newHSBbNeeded = !viewTracksViewportWidth
                            && (viewPrefSize.width > extentSize.width);
                    
                    if (newHSBbNeeded != hsbNeeded) {
                        hsbNeeded = newHSBbNeeded;
                        adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
                        if ((vsb != null) && !vsbNeeded
                                && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                            extentSize = availR.getSize();
                            vsbNeeded = viewPrefSize.height > extentSize.height;

                            if (vsbNeeded) {
                                adjustForVSB(true, availR, vsbR, vpbInsets,
                                        leftToRight);
                            }
                        }
                    }
                }
                
                if (oldHSBNeeded != hsbNeeded || oldVSBNeeded != vsbNeeded) {
                    viewport.setBounds(availR);
                    // You could argue that we should recheck the
                    // Scrollable methods again until they stop changing,
                    // but they might never stop changing, so we stop here
                    // and don't do any additional checks.
                }
            }
        }

        // We now have the final size of the viewport: availR. Now fixup the
        // header and scrollbar widths/heights.
        vsbR.height     = availR.height + vpbInsets.top;
        hsbR.width      = availR.width + vpbInsets.left;
        rowHeadR.height = availR.height + vpbInsets.top;
        rowHeadR.y      = availR.y - vpbInsets.top;
        colHeadR.width  = availR.width + vpbInsets.left;
        colHeadR.x      = availR.x - vpbInsets.left;

        // Set the bounds of the remaining components. The scrollbars are made
        // invisible if they're not needed.
        if (rowHead != null)
            rowHead.setBounds(rowHeadR);

        if (colHead != null)
            colHead.setBounds(colHeadR);

        if (vsb != null) {
            if (vsbNeeded) {
                vsb.setVisible(true);
                vsb.setBounds(vsbR);
            } else {
                vsb.setVisible(false);
            }
        }

        if (hsb != null) {
            if (hsbNeeded) {
                hsb.setVisible(true);
                hsb.setBounds(hsbR);
            } else {
                hsb.setVisible(false);
            }
        }

        if (lowerLeft != null) {
            lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, hsbR.y,
                    leftToRight ? rowHeadR.width : vsbR.width, hsbR.height);
        }

        if (lowerRight != null) {
            lowerRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, hsbR.y,
                    leftToRight ? vsbR.width : rowHeadR.width, hsbR.height);
        }

        if (upperLeft != null) {
            upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x, colHeadR.y,
                    leftToRight ? rowHeadR.width : vsbR.width, colHeadR.height);
        }

        if (upperRight != null) {
            upperRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x, colHeadR.y,
                    leftToRight ? vsbR.width : rowHeadR.width, colHeadR.height);
        }
    }

    /**
     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
     * the vertical scrollbar is needed (<code>wantsVSB</code>). The
     * location of the vsb is updated in <code>vsbR</code>, and the viewport
     * border insets (<code>vpbInsets</code>) are used to offset the vsb.
     * This is only called when <code>wantsVSB</code> has changed, eg you
     * shouldn't invoke adjustForVSB(true) twice.
     */
    private void adjustForVSB(boolean wantsVSB, Rectangle available,
            Rectangle vsbR, Insets vpbInsets, boolean leftToRight) {
        
        int oldWidth = vsbR.width;
        if (wantsVSB) {
            int vsbWidth = Math.max(0, Math.min(
                    vsb.getPreferredSize().width, available.width));

            available.width -= vsbWidth;
            vsbR.width = vsbWidth;

            if (leftToRight) {
                vsbR.x = available.x + available.width + vpbInsets.right;
            } else {
                vsbR.x = available.x - vpbInsets.left;
                available.x += vsbWidth;
            }
        } else {
            available.width += oldWidth;
        }
    }

    /**
     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
     * the horizontal scrollbar is needed (<code>wantsHSB</code>). The
     * location of the hsb is updated in <code>hsbR</code>, and the viewport
     * border insets (<code>vpbInsets</code>) are used to offset the hsb.
     * This is only called when <code>wantsHSB</code> has changed, eg you
     * shouldn't invoked adjustForHSB(true) twice.
     */
    private void adjustForHSB(boolean wantsHSB, Rectangle available,
            Rectangle hsbR, Insets vpbInsets) {
        
        int oldHeight = hsbR.height;
        if (wantsHSB) {
            int hsbHeight = Math.max(0, Math.min(available.height, 
                    hsb.getPreferredSize().height));

            available.height -= hsbHeight;
            hsbR.y = available.y + available.height + vpbInsets.bottom;
            hsbR.height = hsbHeight;
        } else {
            available.height += oldHeight;
        }
    }
}
