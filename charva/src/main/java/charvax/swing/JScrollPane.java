/* class JScrollPane
 *
 * Copyright (C) 2001, 2002, 2003  R M Pitman
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
import charva.awt.Component;
import charva.awt.LayoutManager;
import charvax.swing.border.AbstractBorder;
import charvax.swing.border.Border;
import charvax.swing.border.TitledBorder;
import charvax.swing.plaf.ScrollPaneUI;


/**
 * Provides a scrollable view of a component
 */
public class JScrollPane extends JComponent implements ScrollPaneConstants {

    /** 
     * The display policy for the vertical scrollbar.
     * The default is <code>JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED</code>.
     * @see #setVerticalScrollBarPolicy
     */
    protected int verticalScrollBarPolicy = VERTICAL_SCROLLBAR_AS_NEEDED;


    /**
     * The display policy for the horizontal scrollbar.
     * The default is <code>JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED</code>.
     * @see #setHorizontalScrollBarPolicy
     */
    protected int horizontalScrollBarPolicy = HORIZONTAL_SCROLLBAR_AS_NEEDED;

    /**
     * The scrollpane's vertical scrollbar child. 
     * Default is a <code>JScrollBar</code>.
     * @see #setVerticalScrollBar
     */
    protected JScrollBar    verticalScrollBar;

    /**
     * The scrollpane's horizontal scrollbar child. 
     * Default is a <code>JScrollBar</code>.
     * @see #setHorizontalScrollBar
     */
    protected JScrollBar    horizontalScrollBar;

    /** A JViewport container that holds the (single) child component   */
    protected JViewport     viewport;

    /** 
     * The row header child. Default is <code>null</code>.
     * @see #setRowHeader
     */
    protected JViewport     rowHeader;

    /** 
     * The column header child. Default is <code>null</code>.
     * @see #setColumnHeader
     */
    protected JViewport     columnHeader;

    /**
     * The component to display in the lower left corner.  
     * Default is <code>null</code>.
     * @see #setCorner
     */
    protected Component     lowerLeft;


    /**
     * The component to display in the lower right corner.  
     * Default is <code>null</code>.
     * @see #setCorner
     */
    protected Component     lowerRight;


    /**
     * The component to display in the upper left corner. 
     * Default is <code>null</code>.
     * @see #setCorner
     */
    protected Component     upperLeft;


    /**
     * The component to display in the upper right corner.  
     * Default is <code>null</code>.
     * @see #setCorner
     */
    protected Component     upperRight;
    
    private ScrollPaneUI    uiComponent = new ScrollPaneUI();

    
    /**
     * Creates an empty (no viewport view) <code>JScrollPane</code>
     * where both horizontal and vertical scrollbars appear when needed.
     */
    public JScrollPane() {
        this(null, VERTICAL_SCROLLBAR_AS_NEEDED, 
                HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Creates a <code>JScrollPane</code> that displays the
     * contents of the specified
     * component, where both horizontal and vertical scrollbars appear
     * whenever the component's contents are larger than the view.
     * 
     * @param view       the component to be displayed. This component must
     *                   implement the Scrollable interface.
     * 
     * @see #setViewportView
     */
    public JScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_AS_NEEDED, 
                HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Creates an empty (no viewport view) <code>JScrollPane</code>
     * with specified 
     * scrollbar policies. The available policy settings are listed at 
     * {@link #setVerticalScrollBarPolicy} and
     * {@link #setHorizontalScrollBarPolicy}.
     * 
     * @see #setViewportView
     * 
     * @param vsbPolicy an integer that specifies the vertical
     *      scrollbar policy
     * @param hsbPolicy an integer that specifies the horizontal
     *      scrollbar policy
     */
    public JScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }

    /**
     * Creates a <code>JScrollPane</code> that displays the view component 
     * in a viewport whose view position can be controlled with a pair of 
     * scrollbars.
     * <p>
     * The scrollbar policies specify when the scrollbars are displayed, 
     * For example, if <code>vsbPolicy</code> is
     * <code>VERTICAL_SCROLLBAR_AS_NEEDED</code>
     * then the vertical scrollbar only appears if the view doesn't fit
     * vertically. The available policy settings are listed at 
     * {@link #setVerticalScrollBarPolicy} and
     * {@link #setHorizontalScrollBarPolicy}.
     * 
     * @param view       the component to be displayed. This component must
     *                   implement the Scrollable interface.
     * @param vsbPolicy  an integer that specifies the vertical
     *                   scrollbar policy
     * @param hsbPolicy  an integer that specifies the horizontal
     *                   scrollbar policy
     * 
     * @see #setViewportView
     */
    public JScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        setLayout(new ScrollPaneLayout());
        setVerticalScrollBarPolicy(vsbPolicy);
        setHorizontalScrollBarPolicy(hsbPolicy);
        setViewport(createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
        
        if (view != null)
            setViewportView(view);
    }

    public void add(Component component, Object constraint) {
        add(null);
    }
    
    public Component add(Component component) {
        throw new UnsupportedOperationException(
                "Cannot add components directly to a JScrollPane");
    }
    
    public void addNotify() {
        super.addNotify();
        
        uiComponent.installUI(this);
    }
    
    public void removeNotify() {
        super.removeNotify();
        
        uiComponent.uninstallUI(this);
    }
    
    /**
     * Sets the layout manager for this <code>JScrollPane</code>. This method
     * overrides <code>setLayout</code> in <code>charva.awt.Container</code>
     * to ensure that only <code>LayoutManager</code>s which are subclasses
     * of <code>ScrollPaneLayout</code> can be used in a
     * <code>JScrollPane</code>. If <code>layout</code> is non-null, this
     * will invoke <code>syncWithScrollPane</code> on it.
     * 
     * @param layout  the specified layout manager
     * 
     * @exception ClassCastException 
     *                if layout is not a <code>ScrollPaneLayout</code>
     * 
     * @see charva.awt.Container#getLayout
     * @see charva.awt.Container#setLayout
     */
    public void setLayout(LayoutManager layout) {
        if (layout instanceof ScrollPaneLayout) {
            super.setLayout(layout);
            ((ScrollPaneLayout) layout).syncWithScrollPane(this);
        } else if (layout == null) {
            super.setLayout(layout);
        } else {
            throw new ClassCastException(
                    "layout of JScrollPane must be a ScrollPaneLayout");
        }
    }

    /**
     * Returns the vertical scroll bar policy value.
     * 
     * @return the <code>verticalScrollBarPolicy</code> property
     * 
     * @see #setVerticalScrollBarPolicy
     */
    public int getVerticalScrollBarPolicy() {
        return verticalScrollBarPolicy;
    }

    /**
     * Determines when the vertical scrollbar appears in the scrollpane. Legal
     * values are:
     * <ul>
     * <li>JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
     * <li>JScrollPane.VERTICAL_SCROLLBAR_NEVER
     * <li>JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
     * </ul>
     * 
     * @param policy  one of the three values listed above
     * @exception IllegalArgumentException
     *                if <code>policy</code> is not one of the legal values
     *                shown above
     * 
     * @see #getVerticalScrollBarPolicy
     */
    public void setVerticalScrollBarPolicy(int policy) {
        switch (policy) {
        case VERTICAL_SCROLLBAR_AS_NEEDED:
        case VERTICAL_SCROLLBAR_NEVER:
        case VERTICAL_SCROLLBAR_ALWAYS:
            break;
        default:
            throw new IllegalArgumentException(
                    "invalid verticalScrollBarPolicy");
        }

        verticalScrollBarPolicy = policy;
        invalidate();
        repaint();
    }

    /**
     * Returns the horizontal scroll bar policy value.
     * 
     * @return the <code>horizontalScrollBarPolicy</code> property
     * @see #setHorizontalScrollBarPolicy
     */
    public int getHorizontalScrollBarPolicy() {
        return horizontalScrollBarPolicy;
    }

    /**
     * Determines when the horizontal scrollbar appears in the scrollpane. The
     * options are:
     * <ul>
     * <li>JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
     * <li>JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
     * <li>JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
     * </ul>
     * 
     * @param policy  one of the three values listed above
     * @exception IllegalArgumentException
     *                if <code>policy</code> is not one of the legal values
     *                shown above
     * 
     * @see #getHorizontalScrollBarPolicy
     */
    public void setHorizontalScrollBarPolicy(int policy) {
        switch (policy) {
        case HORIZONTAL_SCROLLBAR_AS_NEEDED:
        case HORIZONTAL_SCROLLBAR_NEVER:
        case HORIZONTAL_SCROLLBAR_ALWAYS:
            break;
        default:
            throw new IllegalArgumentException(
                    "invalid horizontalScrollBarPolicy");
        }
        
        horizontalScrollBarPolicy = policy;
        invalidate();
        repaint();
    }

    /**
     * Returns a <code>JScrollPane.ScrollBar</code> by default. Subclasses may
     * override this method to force <code>ScrollPaneUI</code> implementations
     * to use a <code>JScrollBar</code> subclass. Used by
     * <code>ScrollPaneUI</code> implementations to create the horizontal
     * scrollbar.
     * 
     * @return a <code>JScrollBar</code> with a horizontal orientation
     * 
     * @see JScrollBar
     */
    public JScrollBar createHorizontalScrollBar() {
        return new JScrollBar(JScrollBar.HORIZONTAL);
    }

    /**
     * Returns the horizontal scroll bar that controls the viewport's horizontal
     * view position.
     * 
     * @return the <code>horizontalScrollBar</code> property
     * @see #setHorizontalScrollBar
     */
    public JScrollBar getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    /**
     * Adds the scrollbar that controls the viewport's horizontal view position
     * to the scrollpane. This is usually unnecessary, as
     * <code>JScrollPane</code> creates horizontal and vertical scrollbars by
     * default.
     * 
     * @param horizontalScrollBar  the horizontal scrollbar to be added
     * 
     * @see #createHorizontalScrollBar
     * @see #getHorizontalScrollBar
     * 
     * @beaninfo expert: true bound: true description: The horizontal scrollbar.
     */
    public void setHorizontalScrollBar(JScrollBar horizontalScrollBar) {
        this.horizontalScrollBar = horizontalScrollBar;
        super.add(horizontalScrollBar, HORIZONTAL_SCROLLBAR);

        invalidate();
        repaint();
    }

    /**
     * Returns a <code>JScrollPane.ScrollBar</code> by default. Subclasses may
     * override this method to force <code>ScrollPaneUI</code> implementations
     * to use a <code>JScrollBar</code> subclass. Used by
     * <code>ScrollPaneUI</code> implementations to create the vertical
     * scrollbar.
     * 
     * @return a <code>JScrollBar</code> with a vertical orientation
     * 
     * @see JScrollBar
     */
    public JScrollBar createVerticalScrollBar() {
        return new JScrollBar(JScrollBar.VERTICAL);
    }

    /**
     * Returns the vertical scroll bar that controls the viewports vertical view
     * position.
     * 
     * @return the <code>verticalScrollBar</code> property
     * @see #setVerticalScrollBar
     */
    public JScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
    }

    /**
     * Adds the scrollbar that controls the viewports vertical view position to
     * the scrollpane. This is usually unnecessary, as <code>JScrollPane</code>
     * creates vertical and horizontal scrollbars by default.
     * 
     * @param verticalScrollBar  the new vertical scrollbar to be added
     * 
     * @see #createVerticalScrollBar
     * @see #getVerticalScrollBar
     */
    public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
        this.verticalScrollBar = verticalScrollBar;
        super.add(verticalScrollBar, VERTICAL_SCROLLBAR);
        
        invalidate();
        repaint();
    }

    /**
     * Returns a new <code>JViewport</code> by default. 
     * Used to create the
     * viewport (as needed) in <code>setViewportView</code>,
     * <code>setRowHeaderView</code>, and <code>setColumnHeaderView</code>.
     * Subclasses may override this method to return a subclass of 
     * <code>JViewport</code>.
     *
     * @return a new <code>JViewport</code>
     */
    protected JViewport createViewport() {
        return new JViewport();
    }

    /**
     * Returns the current <code>JViewport</code>
     *
     * @return the <code>viewport</code> property
     * 
     * @see #setViewport
     */
    public JViewport getViewport() {
        return viewport;
    }

    /**
     * Removes the old viewport (if there is one); forces the
     * viewPosition of the new viewport to be in the +x,+y quadrant;
     * syncs up the row and column headers (if there are any) with the
     * new viewport; and finally syncs the scrollbars and
     * headers with the new viewport.
     * <p>
     * Most applications will find it more convenient to use 
     * <code>setViewportView</code>
     * to add a viewport and a view to the scrollpane.
     * 
     * @param viewport the new viewport to be used; if viewport is
     *      <code>null</code>, the old viewport is still removed
     *      and the new viewport is set to <code>null</code>
     * @see #createViewport
     * @see #getViewport
     * @see #setViewportView
     */
    protected void setViewport(JViewport viewport) {
        JViewport old = getViewport();
        this.viewport = viewport;
        if (viewport != null) {
            super.add(viewport, VIEWPORT);
        }
        else if (old != null) {
            remove(old);
        }
    
        invalidate();
        repaint();
    }

    /**
     * Creates a viewport if necessary and then sets its view.  Applications
     * that don't provide the view directly to the <code>JScrollPane</code>
     * constructor
     * should use this method to specify the scrollable child that's going
     * to be displayed in the scrollpane. For example:
     * <pre>
     * JScrollPane scrollpane = new JScrollPane();
     * scrollpane.setViewportView(myBigComponentToScroll);
     * </pre>
     * Applications should not add children directly to the scrollpane.
     *
     * @param view the component to add to the viewport
     * @see #setViewport
     * @see JViewport#setView
     */
    public void setViewportView(Component view) {
        if (viewport == null)
            setViewport(new JViewport());
        
        getViewport().setView(view);
    }

    /**
     * Returns the row header
     * 
     * @return the <code>rowHeader</code> property
     * @see #setRowHeader
     */
    public JViewport getRowHeader() {
        return rowHeader;
    }

    /**
     * Removes the old rowHeader, if it exists.  If the new rowHeader
     * isn't <code>null</code>, syncs the y coordinate of its
     * viewPosition with
     * the viewport (if there is one) and then adds it to the scrollpane.
     * <p>
     * Most applications will find it more convenient to use 
     * <code>setRowHeaderView</code>
     * to add a row header component and its viewport to the scrollpane.
     * 
     * @param rowHeader the new row header to be used; if <code>null</code>
     *      the old row header is still removed and the new rowHeader
     *      is set to <code>null</code>
     * @see #getRowHeader
     * @see #setRowHeaderView
     */
    protected void setRowHeader(JViewport rowHeader) {
        JViewport old = getRowHeader();
        this.rowHeader = rowHeader; 
        if (rowHeader != null) {
            super.add(rowHeader, ROW_HEADER);
        }
        else if (old != null) {
            remove(old);
        }
    
        invalidate();
        repaint();
    }


    /**
     * Creates a row-header viewport if necessary, sets
     * its view and then adds the row-header viewport
     * to the scrollpane.  For example:
     * <pre>
     * JScrollPane scrollpane = new JScrollPane();
     * scrollpane.setViewportView(myBigComponentToScroll);
     * scrollpane.setRowHeaderView(myBigComponentsRowHeader);
     * </pre>
     *
     * @param view the component to display as the row header
     * 
     * @see #setRowHeader
     * @see JViewport#setView
     */
    public void setRowHeaderView(Component view) {
        if (getRowHeader() == null)
            setRowHeader(new JViewport());
        
        getRowHeader().setView(view);
    }

    /**
     * Returns the column header
     * 
     * @return the <code>columnHeader</code> property
     * 
     * @see #setColumnHeader
     */
    public JViewport getColumnHeader() {
        return columnHeader;
    }

    /**
     * Removes the old columnHeader, if it exists.  If the new columnHeader
     * isn't <code>null</code>, sync the x coordinate of the its viewPosition 
     * with the viewport (if there is one) and then add it to the scrollpane.
     * <p>
     * Most applications will find it more convenient to use 
     * <code>setRowHeaderView</code>
     * to add a row header component and its viewport to the scrollpane.
     * 
     * @see #getColumnHeader
     * @see #setColumnHeaderView
     */
    protected void setColumnHeader(JViewport columnHeader) {
        JViewport old = getColumnHeader();
        this.columnHeader = columnHeader;   
        if (columnHeader != null) {
            super.add(columnHeader, COLUMN_HEADER);
        }
        else if (old != null) {
            remove(old);
        }
    
        invalidate();
        repaint();
    }

    /**
     * Creates a column-header viewport if necessary, sets
     * its view, and then adds the column-header viewport
     * to the scrollpane.  For example:
     * <pre>
     * JScrollPane scrollpane = new JScrollPane();
     * scrollpane.setViewportView(myBigComponentToScroll);
     * scrollpane.setColumnHeaderView(myBigComponentsColumnHeader);
     * </pre>
     * 
     * @param view the component to display as the column header
     * 
     * @see #setColumnHeader
     * @see JViewport#setView
     */
    public void setColumnHeaderView(Component view) {
        if (getColumnHeader() == null)
            setColumnHeader(new JViewport());
        
        getColumnHeader().setView(view);
    }

    /**
     * Returns the component at the specified corner. The <code>key</code>
     * value specifying the corner is one of:
     * <ul>
     * <li>JScrollPane.LOWER_LEFT_CORNER
     * <li>JScrollPane.LOWER_RIGHT_CORNER
     * <li>JScrollPane.UPPER_LEFT_CORNER
     * <li>JScrollPane.UPPER_RIGHT_CORNER
     * <li>JScrollPane.LOWER_LEADING_CORNER
     * <li>JScrollPane.LOWER_TRAILING_CORNER
     * <li>JScrollPane.UPPER_LEADING_CORNER
     * <li>JScrollPane.UPPER_TRAILING_CORNER
     * </ul>
     * 
     * @param key  one of the values as shown above
     * 
     * @return one of the components listed below or <code>null</code> if
     *         <code>key</code> is invalid:
     *         <ul>
     *         <li>lowerLeft
     *         <li>lowerRight
     *         <li>upperLeft
     *         <li>upperRight
     *         </ul>
     * 
     * @see #setCorner
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
     * Adds a child that will appear in one of the scroll panes corners, if
     * there's room. For example with both scrollbars showing (on the right and
     * bottom edges of the scrollpane) the lower left corner component will be
     * shown in the space between ends of the two scrollbars. Legal values for
     * the <b>key</b> are:
     * <ul>
     * <li>JScrollPane.LOWER_LEFT_CORNER
     * <li>JScrollPane.LOWER_RIGHT_CORNER
     * <li>JScrollPane.UPPER_LEFT_CORNER
     * <li>JScrollPane.UPPER_RIGHT_CORNER
     * <li>JScrollPane.LOWER_LEADING_CORNER
     * <li>JScrollPane.LOWER_TRAILING_CORNER
     * <li>JScrollPane.UPPER_LEADING_CORNER
     * <li>JScrollPane.UPPER_TRAILING_CORNER
     * </ul>
     * <p>
     * Although "corner" doesn't match any beans property signature,
     * <code>PropertyChange</code> events are generated with the property name
     * set to the corner key.
     * 
     * @param key     identifies which corner the component will appear in
     * @param corner  one of the following components:
     *            <ul>
     *            <li>lowerLeft
     *            <li>lowerRight
     *            <li>upperLeft
     *            <li>upperRight
     *            </ul>
     * 
     * @exception IllegalArgumentException
     *                if corner key is invalid
     */
    public void setCorner(String key, Component corner) {
        if (key.equals(LOWER_LEFT_CORNER)) {
            lowerLeft = corner;
        } else if (key.equals(LOWER_RIGHT_CORNER)) {
            lowerRight = corner;
        } else if (key.equals(UPPER_LEFT_CORNER)) {
            upperLeft = corner;
        } else if (key.equals(UPPER_RIGHT_CORNER)) {
            upperRight = corner;
        } else {
            throw new IllegalArgumentException("invalid corner key");
        }
        
        super.add(corner, key);
        invalidate();
        repaint();
    }

//    /**
//     * Overrides the corresponding method in Container
//     */
//    public void setSize(int width, int height) {
//        super.setSize(width, height);
//        
//        Dimension size = new Dimension(width, height);
//
//        if (border != null) {
//            Insets borderInsets = border.getBorderInsets(this);
//            size.height -= (borderInsets.top + borderInsets.bottom);
//            size.width -= (borderInsets.left + borderInsets.right);
//        }
//
//        // Set the size of the viewport(s) as well
//        setViewportExtents(size);
//    }
//
//    /**
//     * Overrides the minimumSize() method of Container.
//     */
//    public Dimension getMinimumSize() {
//        Dimension size = new Dimension();
//        Component view = getViewport().getView();
//        if (view instanceof Scrollable) {
//            Scrollable s = (Scrollable) view;
//            size.setSize(s.getPreferredScrollableViewportSize());
//        } else {
//            size.setSize(view.getSize());
//        }
//
//        // Set the size of the viewport(s) as well
//        setViewportExtents(size);
//
//        if (border != null) {
//            Insets borderInsets = border.getBorderInsets(this);
//            size.height += (borderInsets.top + borderInsets.bottom);
//            size.width += (borderInsets.left + borderInsets.right);
//        }
//
//        return size;
//    }

//    /**
//     * Called by a Scrollable object such as JTable or JList, when its state
//     * changes in such a way that it may need to be scrolled.
//     */
//    public void scroll(ScrollEvent e) {
//        Scrollable scrollable = e.getScrollable();
//
//        int direction = e.getDirection();
//
//        // "limit" gives the row and column of the view component that
//        // must appear just inside the JViewport after scrolling
//        Point limit = e.getLimit();
//
//        // determine the value of "limit" relative to the top left
//        // corner of the JScrollPane
//        Point viewportLocation = getViewport().getLocation();
//        limit.translate(viewportLocation.x, viewportLocation.y);
//        limit.translate(scrollable.getLocation());
//
//        // get the bounding rectangle of the child viewport, relative to
//        // the top left corner of the JScrollPane
//        Rectangle bounds = viewport.getBounds();
////        Dimension viewportExtent = viewport.getExtentSize();
//
//        Point viewPosition = viewport.getViewPosition();
//
//        // if the limit is inside the viewport, the component
//        // doesn't need to be scrolled
//        
//        // first do the left/right scrolling
//        if (limit.x >= (bounds.x + bounds.width)) {
//            if ((direction == ScrollEvent.LEFT 
//                    || direction == ScrollEvent.UP_LEFT 
//                    || direction == ScrollEvent.DOWN_LEFT)) {
//
//                viewPosition.x -= (limit.x - (bounds.x + bounds.width - 1));
//            
//            } else if (direction == ScrollEvent.RIGHT 
//                    || direction == ScrollEvent.UP_RIGHT 
//                    || direction == ScrollEvent.DOWN_RIGHT) {
//
//                viewPosition.x += (bounds.x - limit.x);
//            }
//        } else if (limit.x < bounds.x) {
//            if (direction == ScrollEvent.RIGHT 
//                    || direction == ScrollEvent.UP_RIGHT 
//                    || direction == ScrollEvent.DOWN_RIGHT) {
//
//                viewPosition.x += (bounds.x - limit.x);
//            
//            } else if (direction == ScrollEvent.LEFT 
//                    || direction == ScrollEvent.UP_LEFT 
//                    || direction == ScrollEvent.DOWN_LEFT) {
//                
//                viewPosition.x -= (limit.x - (bounds.x + bounds.width - 1));
//            }
//        }
//
//        // now do the up/down scrolling
//        if (limit.y < bounds.y 
//                && (direction == ScrollEvent.DOWN 
//                        || direction == ScrollEvent.DOWN_LEFT 
//                        || direction == ScrollEvent.DOWN_RIGHT)) {
//
//            viewPosition.y += (bounds.y - limit.y);
//        
//        } else if (limit.y >= (bounds.y + bounds.height) 
//                && (direction == ScrollEvent.UP 
//                        || direction == ScrollEvent.UP_LEFT 
//                        || direction == ScrollEvent.UP_RIGHT)) {
//
//            viewPosition.y -= (limit.y - (bounds.y + bounds.height - 1));
//        }
//
//        if (rowHeader != null)
//            rowHeader.setViewPosition(viewPosition);
//        
//        if (columnHeader != null)
//            columnHeader.setViewPosition(viewPosition);
//        
//        viewport.setViewPosition(viewPosition);
//
//        repaint();
//        
//        Window ancestor = getAncestorWindow();
//
//        // Ensure the cursor is within the viewport (if the component
//        // contained within the viewport is offset a long way to the left,
//        // the cursor position can get scrambled)
//        Point cursor         = ancestor.getCursor();
//        Point viewportOrigin = viewport.getLocationOnScreen();
//        if (cursor.x < viewportOrigin.x || cursor.y < viewportOrigin.y) {
//            if (cursor.x < viewportOrigin.x)
//                cursor.x = viewportOrigin.x;
//            if (cursor.y < viewportOrigin.y)
//                cursor.y = viewportOrigin.y;
//            
//            ancestor.setCursor(cursor);
//        }
//    }

//    public void draw(TerminalWindow t) {
//        // get the absolute origin of this component
//        Point origin = getLocationOnScreen();
//        Insets borderInsets;
//        if (border != null)
//            borderInsets = border.getBorderInsets(this);
//        else
//            borderInsets = new Insets(0, 0, 0, 0);
//
//        int       colorpair = getColorCode();
//        Dimension size      = getSize();
//        
//        if (border != null) {
//            border.paintBorder(this, t, 
//                    origin.x, origin.y, size.width, size.height);
//        }
//
//        // if the child component is larger than the viewport - 
//        // draw scrollbars
//
//        // the size of the component displayed within the viewport
//        Dimension childSize    = getViewport().getViewSize();
//        Point     viewPosition = getViewport().getViewPosition();
//
//        // the size of the viewport
//        Dimension extentSize   = getViewport().getExtentSize();
///*
//        if (childSize.height > extentSize.height) {
//            verticalScrollBar.setVisible(true);
//            verticalScrollBar.setMaximum(childSize.height);
//            
//            int scrollbar_height = (extentSize.height * extentSize.height)
//                    / childSize.height;
//
//            // Round the height upwards to the nearest integer
//            if (((extentSize.height * extentSize.height)
//                    % childSize.height) != 0)
//                scrollbar_height++;
//
//            int scrollbar_offset = (-1 * viewPosition.y * extentSize.height)
//                            / childSize.height;
//
//            verticalScrollBar.setValue(scrollbar_offset);
//            verticalScrollBar.setVisibleAmount(scrollbar_height);
//            
//        } else {
//            verticalScrollBar.setVisible(false);
//        }
//
//        if (childSize.width > extentSize.width) {
//            horizontalScrollBar.setVisible(true);
//            horizontalScrollBar.setMaximum(childSize.width);
//            
//            int scrollbar_width = (extentSize.width * extentSize.width)
//                    / childSize.width;
//
//            // Round the width upwards to the nearest integer
//            if (((extentSize.width * extentSize.width)
//                    % childSize.width) != 0)
//                scrollbar_width++;
//
//            int scrollbar_offset = (-1 * viewPosition.x * extentSize.width)
//                            / childSize.width;
//
//            horizontalScrollBar.setValue(scrollbar_offset);
//            horizontalScrollBar.setVisibleAmount(scrollbar_width);
//            
//        } else {
//            horizontalScrollBar.setVisible(false);
//        }
//*/
//        //verticalScrollBar.setVisible(true);
//        //horizontalScrollBar.setVisible(true);
//        
//        // draw the viewports by calling the draw() method
//        // of the Container class
//        super.draw(t);
///*
//        if (childSize.height > extentSize.height) {
//            int scrollbar_height = (extentSize.height * extentSize.height)
//                    / childSize.height;
//
//            // Round the height upwards to the nearest integer
//            if (((extentSize.height * extentSize.height)
//                    % childSize.height) != 0)
//                scrollbar_height++;
//
//            int scrollbar_offset = (-1 * viewPosition.y * extentSize.height)
//                            / childSize.height;
//
//            for (int i = 0; i < extentSize.height; i++) {
//                t.setCursor(origin.addOffset(
//                        borderInsets.left + extentSize.width,
//                        borderInsets.top + i));
//                
//                if (i >= scrollbar_offset 
//                        && i < (scrollbar_offset + scrollbar_height)) {
//                    
//                    t.addChar('+', colorpair);
//                }
//            }
//        }
//
//        if (childSize.width > extentSize.width) {
//            int scrollbar_width = (extentSize.width * extentSize.width)
//                    / childSize.width;
//
//            // Round the width upwards to the nearest integer
//            if (((extentSize.width * extentSize.width)
//                    % childSize.width) != 0)
//                scrollbar_width++;
//
//            int scrollbar_offset = (-1 * viewPosition.x * extentSize.width)
//                            / childSize.width;
//
//            for (int i = 0; i < extentSize.width; i++) {
//                t.setCursor(origin.addOffset(borderInsets.left + i,
//                        borderInsets.top + extentSize.height));
//                
//                if (i >= scrollbar_offset 
//                        && i < (scrollbar_offset + scrollbar_width)) {
//                    
//                    t.addChar('+', colorpair);
//                }
//            }
//        }*/
//    }

    public void setBorder(Border border) {
        super.setBorder(border);
        
        ColorPair colorPair = null;
        if (border instanceof AbstractBorder) {
            colorPair = ((AbstractBorder)border).getLineColor();
        
        } else if (border instanceof TitledBorder) {
            Border b = ((TitledBorder)border).getBorder();
            if (b instanceof AbstractBorder)
                colorPair = ((AbstractBorder)b).getLineColor();
        }
        
        if (colorPair != null && verticalScrollBar != null)
            verticalScrollBar.setColor(colorPair);
    
        if (colorPair != null && horizontalScrollBar != null)
            horizontalScrollBar.setColor(colorPair);
    }
    
//    /**
//     * Adds a border around the viewport
//     */
//    public void setViewportBorder(Border border) {
//        //setBorder(border);
//    }
//
//    /**
//     * Returns a reference to the border around the JScrollPane's viewport
//     */
//    public Border getViewportBorder() {
//        return null;//getBorder();
//    }
//
//    /**
//     * Sets the size of the visible part of the view
//     */
//    private void setViewportExtents(Dimension size) {
//        viewport.setExtentSize(size);
//    }

    /**
     * Returns a string representation of this <code>JScrollPane</code>.
     * This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JScrollPane</code>.
     */
    protected String paramString() {
        String viewportBorderString = (border != null ? 
                border.toString() : "");
        String viewportString = (viewport != null ? viewport.toString() : "");
        String verticalScrollBarPolicyString;
        
        if (verticalScrollBarPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
            verticalScrollBarPolicyString = "VERTICAL_SCROLLBAR_AS_NEEDED";
        } else if (verticalScrollBarPolicy == VERTICAL_SCROLLBAR_NEVER) {
            verticalScrollBarPolicyString = "VERTICAL_SCROLLBAR_NEVER";
        } else if (verticalScrollBarPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
            verticalScrollBarPolicyString = "VERTICAL_SCROLLBAR_ALWAYS";
        } else
            verticalScrollBarPolicyString = "";
        
        String horizontalScrollBarPolicyString;
        if (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
            horizontalScrollBarPolicyString = "HORIZONTAL_SCROLLBAR_AS_NEEDED";
        } else if (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
            horizontalScrollBarPolicyString = "HORIZONTAL_SCROLLBAR_NEVER";
        } else if (horizontalScrollBarPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
            horizontalScrollBarPolicyString = "HORIZONTAL_SCROLLBAR_ALWAYS";
        } else
            horizontalScrollBarPolicyString = "";
        
        String horizontalScrollBarString = (horizontalScrollBar != null ? 
                horizontalScrollBar.toString() : "");
        String verticalScrollBarString = (verticalScrollBar != null ? 
                verticalScrollBar.toString() : "");
        String columnHeaderString = (columnHeader != null ? 
                columnHeader.toString() : "");
        String rowHeaderString = (rowHeader != null ? 
                rowHeader.toString() : "");
        String lowerLeftString = (lowerLeft != null ? 
                lowerLeft.toString() : "");
        String lowerRightString = (lowerRight != null ? 
                lowerRight.toString() : "");
        String upperLeftString = (upperLeft != null ? 
                upperLeft.toString() : "");
        String upperRightString = (upperRight != null ? 
                upperRight.toString() : "");

        return super.paramString()
                + ",viewport=" + viewportString
                + ",horizontalScrollBar=" + horizontalScrollBarString
                + ",horizontalScrollBarPolicy=" + horizontalScrollBarPolicyString 
                + ",verticalScrollBar=" + verticalScrollBarString
                + ",verticalScrollBarPolicy=" + verticalScrollBarPolicyString
                + ",columnHeader=" + columnHeaderString
                + ",rowHeader=" + rowHeaderString
                + ",upperLeft=" + upperLeftString 
                + ",upperRight=" + upperRightString
                + ",lowerLeft=" + lowerLeftString 
                + ",lowerRight=" + lowerRightString
                + ",viewportBorder=" + viewportBorderString;
    }
}
