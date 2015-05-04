/* class JList
 *
 * Copyright (C) 2003  R M Pitman
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

/*
 * Modified Jul 15, 2003 by Tadpole Computer, Inc.
 * Modifications Copyright 2003 by Tadpole Computer, Inc.
 *
 * Modifications are hereby licensed to all parties at no charge under
 * the same terms as the original.
 *
 * Fixed case where visible row count was larger that the total number
 * of rows in the model.
 */

package charvax.swing;

import java.util.ArrayList;
import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.KeyEvent;
import charvax.swing.event.ListSelectionListener;
import charvax.swing.plaf.ListUI;


/**
 * A component that allows the user to select one or more objects from 
 * a list
 * <p>
 * The JList does not provide scrolling capability. The JList is normally 
 * inserted into a JScrollPane to provide scrolling.
 */
public class JList extends JComponent implements Scrollable {

    private int                     visibleRows    = 5;
    private int                     columns        = 10;
    
    private ListUI                  listUI         = new ListUI();

    /**
     * The ListSelectionModel used by this JList.
     */
    protected ListSelectionModel    selectionModel =
            new DefaultListSelectionModel();

    /**
     * The ListModel that holds the items that are displayed by
     * this JList.
     */
    protected ListModel             listModel;

    protected ColorPair             selectedColor;
    protected ColorPair             highlightedColor;
    protected ColorPair             disabledColor;
    
    
    /**
     * Constructs a JList with 5 rows, 10 columns wide.
     */
    public JList() {
        setModel(new DefaultListModel());
    }

    /**
     * Construct a JList that displays the elements in the specified
     * non-null model.
     */
    public JList(ListModel model) {
        setModel(model);
    }

    /**
     * Construct a JList containing the items in the specified array.
     */
    public JList(Object[] items) {
        setListData(items);
    }

    public void addNotify() {
        super.addNotify();
        
        listUI.installUI(this);
    }
    
    public void removeNotify() {
        super.removeNotify();
        
        listUI.uninstallUI(this);
    }
    
    /**
     * Constructs a ListModel from an array of Objects and then applies
     * setModel to it.
     */
    public void setListData(Object[] listData) {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < listData.length; i++)
            model.addElement(listData[i]);
        
        setModel(model);
    }

    /**
     * Sets the model that represents the "contents" of the list, and
     * clears the selection.
     */
    public void setModel(ListModel model) {
        if (model == null)
            throw new IllegalArgumentException("model must be non null");
        
        ListModel oldValue = listModel;
        listModel = model;
        firePropertyChange("model", oldValue, listModel);
        clearSelection();
    }

    /**
     * Returns the data model that holds the list of items displayed by this
     * JList.
     */
    public ListModel getModel() {
        return listModel;
    }

    /**
     * Set the maximum number of rows that can be displayed at a time
     * by the JScrollPane which contains this JList.
     */
    public void setVisibleRowCount(int rows) {
        if (rows < 1)
            throw new IllegalArgumentException("rows: " + rows);
        
        visibleRows = rows;
    }

    public int getVisibleRowCount() {
        return visibleRows;
    }

    /**
     * Sets the number of columns INSIDE the list
     */
    public void setColumns(int columns) {
        if (columns < 1)
            throw new IllegalArgumentException("columns: " + columns);
        
        this.columns = columns;
        invalidate();
        repaint();
    }

//    public Dimension getSize() {
//        return new Dimension(getWidth(), getHeight());
//    }
//
//    public int getWidth() {
//        Insets insets = super.getInsets();
//        return columns + insets.left + insets.right;
//    }
//
//    public int getHeight() {
//        Insets insets = super.getInsets();
//        return listModel.getSize() + insets.top + insets.bottom;
//    }

    /**
     * Returns the size of the viewport needed to display visibleRows rows.
     */
    public Dimension getPreferredScrollableViewportSize() {
        //return new Dimension(getWidth(), getVisibleRowCount());
    
        Insets insets = getInsets();
        int dx = insets.left + insets.right;
        int dy = insets.top + insets.bottom;

        int visibleRowCount = getVisibleRowCount();
        int fixedCellWidth = columns;
        int fixedCellHeight = 1;

        if (fixedCellWidth > 0 && fixedCellHeight > 0) {
            int width = fixedCellWidth + dx;
            int height = (visibleRowCount * fixedCellHeight) + dy;
            return new Dimension(width, height);
        }
        
        if (getModel().getSize() > 0) {
            int width = getPreferredSize().width;
            int height;
            Rectangle r = getCellBounds(0, 0);
            if (r != null) {
                height = (visibleRowCount * r.height) + dy;
            } else {
                // Will only happen if UI null, shouldn't matter what we return
                height = 1;
            }
            
            return new Dimension(width, height);
        }

        return new Dimension(fixedCellWidth, fixedCellHeight * visibleRowCount);
    }

    private void checkScrollableParameters(Rectangle visibleRect,
            int orientation) {
        
        if (visibleRect == null)
            throw new IllegalArgumentException("visibleRect must be non-null");
        
        switch (orientation) {
        case SwingConstants.VERTICAL:
        case SwingConstants.HORIZONTAL:
            break;
        default:
            throw new IllegalArgumentException(
                    "orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        checkScrollableParameters(visibleRect, orientation);
        if (orientation == SwingConstants.VERTICAL) {
            int inc = visibleRect.height;
            /* Scroll Down */
            if (direction > 0) {
                // last cell is the lowest left cell
                int last = locationToIndex(new Point(visibleRect.x,
                        visibleRect.y + visibleRect.height - 1));
                if (last != -1) {
                    Rectangle lastRect = getCellBounds(last, last);
                    if (lastRect != null) {
                        inc = lastRect.y - visibleRect.y;
                        if ((inc == 0) && (last < getModel().getSize() - 1)) {
                            inc = lastRect.height;
                        }
                    }
                }
            }
            /* Scroll Up */
            else {
                int newFirst = locationToIndex(new Point(visibleRect.x,
                        visibleRect.y - visibleRect.height));
                int first = getFirstVisibleIndex();
                if (newFirst != -1) {
                    if (first == -1) {
                        first = locationToIndex(visibleRect.getLocation());
                    }
                    Rectangle newFirstRect = getCellBounds(newFirst, newFirst);
                    Rectangle firstRect = getCellBounds(first, first);
                    if ((newFirstRect != null) && (firstRect != null)) {
                        while ((newFirstRect.y + visibleRect.height < firstRect.y
                                + firstRect.height)
                                && (newFirstRect.y < firstRect.y)) {
                            newFirst++;
                            newFirstRect = getCellBounds(newFirst, newFirst);
                        }
                        inc = visibleRect.y - newFirstRect.y;
                        if ((inc <= 0) && (newFirstRect.y > 0)) {
                            newFirst--;
                            newFirstRect = getCellBounds(newFirst, newFirst);
                            if (newFirstRect != null) {
                                inc = visibleRect.y - newFirstRect.y;
                            }
                        }
                    }
                }
            }
            
            return inc;
        }
        
        return visibleRect.width;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        checkScrollableParameters(visibleRect, orientation);
        return 1;
    }
    
    /**
     * Returns true if this <code>JList</code> is displayed in a 
     * <code>JViewport</code> and the viewport is wider than
     * <code>JList</code>'s preferred width; otherwise returns false.
     *
     * @return true if viewport is wider than the <code>JList</code>'s
     *              preferred width, otherwise false
     * @see Scrollable#getScrollableTracksViewportWidth
     */
    public boolean getScrollableTracksViewportWidth() {
        if (getParent() instanceof JViewport)
            return (((JViewport)getParent()).getWidth() > getPreferredSize().width);
        
        return false;
    }

    /**
     * Returns true if this <code>JList</code> is displayed in a 
     * <code>JViewport</code> and the viewport is taller than
     * <code>JList</code>'s preferred height; otherwise returns false.
     *
     * @return true if viewport is taller than <code>Jlist</code>'s
     *              preferred height, otherwise false
     * @see Scrollable#getScrollableTracksViewportHeight
     */
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport)
            return (((JViewport)getParent()).getHeight() > getPreferredSize().height);
        
        return false;
    }
    
    /**
     * Returns the index of the first visible cell. The cell considered to be
     * "first" depends on the list's <code>componentOrientation</code>
     * property. If the orientation is horizontal left-to-right, then the first
     * visible cell is in the list's upper-left corner. If the orientation is
     * horizontal right-to-left, then the first visible cell is in the list's
     * upper-right corner. If nothing is visible or the list is empty, a -1 is
     * returned. Note that the returned cell may only be partially visible.
     * 
     * @return the index of the first visible cell
     * @see #getLastVisibleIndex
     * @see JComponent#getVisibleRect
     */
    public int getFirstVisibleIndex() {
        Rectangle r = getVisibleRect();
        int first;
        boolean isLeftToRight = true;
        if (isLeftToRight) {
            first = locationToIndex(r.getLocation());
        } else {
            first = locationToIndex(new Point((r.x + r.width) - 1, r.y));
        }
        if (first != -1) {
            Rectangle bounds = getCellBounds(first, first);
            if (bounds != null) {
                SwingUtilities.computeIntersection(r.x, r.y, r.width, r.height,
                        bounds);
                if (bounds.width == 0 || bounds.height == 0) {
                    first = -1;
                }
            }
        }
        return first;
    }

    /**
     * Returns the index of the last visible cell. The cell considered to be
     * "last" depends on the list's <code>componentOrientation</code>
     * property. If the orientation is horizontal left-to-right, then the last
     * visible cell is in the JList's lower-right corner. If the orientation is
     * horizontal right-to-left, then the last visible cell is in the JList's
     * lower-left corner. If nothing is visible or the list is empty, a -1 is
     * returned. Note that the returned cell may only be partially visible.
     * 
     * @return the index of the last visible cell
     * @see #getFirstVisibleIndex
     * @see JComponent#getVisibleRect
     */
    public int getLastVisibleIndex() {
        boolean leftToRight = true;
        Rectangle r = getVisibleRect();
        Point lastPoint;
        if (leftToRight) {
            lastPoint = new Point((r.x + r.width) - 1, (r.y + r.height) - 1);
        } else {
            lastPoint = new Point(r.x, (r.y + r.height) - 1);
        }
        int location = locationToIndex(lastPoint);

        if (location != -1) {
            Rectangle bounds = getCellBounds(location, location);

            if (bounds != null) {
                SwingUtilities.computeIntersection(r.x, r.y, r.width, r.height,
                        bounds);
                if (bounds.width == 0 || bounds.height == 0) {
                    // Try the lower left corner, and then go across checking
                    // each cell.
                    Point visibleLL = new Point(r.x, lastPoint.y);
                    int last;
                    int llIndex = -1;
                    int lrIndex = location;
                    location = -1;

                    do {
                        last = llIndex;
                        llIndex = locationToIndex(visibleLL);

                        if (llIndex != -1) {
                            bounds = getCellBounds(llIndex, llIndex);
                            if (llIndex != lrIndex && bounds != null
                                    && bounds.contains(visibleLL)) {
                                location = llIndex;
                                visibleLL.x = bounds.x + bounds.width + 1;
                                if (visibleLL.x >= lastPoint.x) {
                                    // Past visible region, bail.
                                    last = llIndex;
                                }
                            } else {
                                last = llIndex;
                            }
                        }
                    } while (llIndex != -1 && last != llIndex);
                }
            }
        }
        
        return location;
    }

    /**
     * Scrolls the viewport to make the specified cell completely visible.
     * Note, for this method to work, the <code>JList</code> must be
     * displayed within a <code>JViewport</code>.
     *
     * @param index  the index of the cell to make visible
     * @see JComponent#scrollRectToVisible
     * @see #getVisibleRect
     */
    public void ensureIndexIsVisible(int index) {
        Rectangle cellBounds = getCellBounds(index, index);
        if (cellBounds != null) {
            scrollRectToVisible(cellBounds);
        }
//        if (!(getParent() instanceof JViewport))
//            return;
//
//        if (index < 0) {
//            index = 0;
//        } else if (index > listModel.getSize() - 1) {
//            index = listModel.getSize() - 1;
//        }
//
//        // It seems reasonable to assume that the "current row" should
//        // be set to the index that is being made visible.
//        currentRow = index;
    }

    /**
     * Convert a point in <code>JList</code> coordinates to the closest index
     * of the cell at that location. To determine if the cell actually
     * contains the specified location use a combination of this method and
     * <code>getCellBounds</code>.  Returns -1 if the model is empty.
     *
     * @param location the coordinates of the cell, relative to
     *          <code>JList</code>
     * @return an integer -- the index of the cell at the given location, or -1.
     */
    public int locationToIndex(Point location) {
        return listUI.locationToIndex(this, location);
    }

    /**
     * Returns the origin of the specified item in <code>JList</code>
     * coordinates. Returns <code>null</code> if <code>index</code> isn't valid.
     *
     * @param index the index of the <code>JList</code> cell
     * @return the origin of the index'th cell
     */
    public Point indexToLocation(int index) {
        return listUI.indexToLocation(this, index);
    }

    /**
     * Returns the bounds of the specified range of items in <code>JList</code>
     * coordinates. Returns <code>null</code> if index isn't valid.
     *
     * @param index0  the index of the first <code>JList</code> cell in the range
     * @param index1  the index of the last <code>JList</code> cell in the range
     * @return the bounds of the indexed cells in pixels
     */
    public Rectangle getCellBounds(int index0, int index1) {
        return listUI.getCellBounds(this, index0, index1);
    }

    /**
     * Sets the selection model of the JList to an implementation of the
     * ListSelectionModel interface.
     */
    public void setSelectionModel(ListSelectionModel selectionModel) {
        if (selectionModel == null) {
            throw new IllegalArgumentException(
                    "selectionModel must be non null");
        }

        ListSelectionModel oldValue = this.selectionModel;
        this.selectionModel = selectionModel;
        firePropertyChange("selectionModel", oldValue, selectionModel);
    }

    /**
     * Returns the list's implementation of ListSelectionModel.
     */
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    /**
     * Register an ListSelectionListener object for this component.
     * The listener is notified each time a change to the selection occurs.
     */
    public void addListSelectionListener(ListSelectionListener il) {
        selectionModel.addListSelectionListener(il);
    }

    /**
     * Remove the specified ListSelectionListener from the list of listeners
     * that will be notified when the selection changes.
     */
    public void removeListSelectionListener(ListSelectionListener listener) {
        selectionModel.removeListSelectionListener(listener);
    }

    /**
     * Get the first selected index, or -1 if there is no selected index.
     */
    public int getSelectedIndex() {
        return selectionModel.getMinSelectionIndex();
    }

    /**
     * Returns an array of the selected indices. The indices are
     * sorted in increasing index order.
     */
    public int[] getSelectedIndices() {
        ArrayList objects = new ArrayList();
        if (!selectionModel.isSelectionEmpty()) {
            int first = selectionModel.getMinSelectionIndex();
            int last = selectionModel.getMaxSelectionIndex();
            for (int i = first; i <= last; i++) {
                if (selectionModel.isSelectedIndex(i))
                    objects.add(SwingUtilities.valueOf(i));
            }
        }

        int[] values = new int[objects.size()];
        for (int i = 0; i < values.length; i++)
            values[i] = ((Integer) objects.get(i)).intValue();
        
        return values;
    }

    /**
     * Get the first selected item on this list, or <code>null</code>
     * if the selection is empty.
     * <p>
     * Returns <tt>null</tt> if there are no selected items.
     */
    public Object getSelectedValue() {
        int index = selectionModel.getMinSelectionIndex();
        if (index == -1)
            return null;

        return listModel.getElementAt(index);
    }

    /**
     * Returns an array of the selected values. The objects are sorted 
     * in increasing index order
     */
    public Object[] getSelectedValues() {
        ArrayList objects = new ArrayList();
        if (!selectionModel.isSelectionEmpty()) {
            int first = selectionModel.getMinSelectionIndex();
            int last = selectionModel.getMaxSelectionIndex();
            for (int i = first; i <= last; i++) {
                if (selectionModel.isSelectedIndex(i))
                    objects.add(listModel.getElementAt(i));
            }
        }
        
        return objects.toArray();
    }

    /**
     * Select the item at the specified index. Note that this method
     * does not redraw the JList.
     */
    public void setSelectedIndex(int index) {
        selectionModel.setSelectionInterval(index, index);
    }

    /**
     * Sets the selection to be the set union between the current
     * selection and the specified interval between index0 and index1
     * (inclusive).
     */
    public void addSelectionInterval(int index0, int index1) {
        selectionModel.addSelectionInterval(index0, index1);
    }

    /**
     * Clears the selection. After this <code>isSelectionEmpty()</code>
     * will return true.
     */
    public void clearSelection() {
        selectionModel.clearSelection();
    }

    /**
     * Selects the specified interval.  Both the <code>anchor</code>
     *  and <code>lead</code> indices are included.  It's not
     * necessary for <code>anchor</code> to be less than <code>lead</code>.
     * This is a convenience method that just delegates to the 
     * <code>selectionModel</code>.
     * The <code>DefaultListSelectionModel</code> implementation 
     * will do nothing if either <code>anchor</code> or
     * <code>lead</code> are -1.
     * If <code>anchor</code> or <code>lead</code> are less than -1,
     * <code>IndexOutOfBoundsException</code> is thrown.
     *
     * @param anchor the first index to select
     * @param lead the last index to select
     * @exception IndexOutOfBoundsException if either <code>anchor</code>
     *    or <code>lead</code> are less than -1
     * @see ListSelectionModel#setSelectionInterval
     * @see #addSelectionInterval
     * @see #removeSelectionInterval
     * @see #addListSelectionListener
     */
    public void setSelectionInterval(int anchor, int lead) {
        getSelectionModel().setSelectionInterval(anchor, lead);
    }

    /**
     * Sets the selection to be the set difference of the specified interval
     * and the current selection.  Both the <code>index0</code> and
     * <code>index1</code> indices are removed.  It's not necessary for
     * <code>index0</code> to be less than <code>index1</code>.
     * This is a convenience method that just delegates to the 
     * <code>selectionModel</code>.
     * The <code>DefaultListSelectionModel</code> implementation 
     * will do nothing if either <code>index0</code> or
     * <code>index1</code> are -1.
     * If <code>index0</code> or <code>index1</code> are less than -1,
     * <code>IndexOutOfBoundsException</code> is thrown.
     *
     * @param index0 the first index to remove from the selection
     * @param index1 the last index to remove from the selection
     * @exception IndexOutOfBoundsException if either <code>index0</code>
     *    or <code>index1</code> are less than -1
     * @see ListSelectionModel#removeSelectionInterval
     * @see #setSelectionInterval
     * @see #addSelectionInterval
     * @see #addListSelectionListener
     */
    public void removeSelectionInterval(int index0, int index1) {
        getSelectionModel().removeSelectionInterval(index0, index1);
    }

    /**
     * Returns the first index argument from the most recent 
     * <code>addSelectionModel</code> or <code>setSelectionInterval</code> call.
     * This is a convenience method that just delegates to the
     * <code>selectionModel</code>.
     *
     * @return the index that most recently anchored an interval selection
     * @see ListSelectionModel#getAnchorSelectionIndex
     * @see #addSelectionInterval
     * @see #setSelectionInterval
     * @see #addListSelectionListener
     */
    public int getAnchorSelectionIndex() {
        return getSelectionModel().getAnchorSelectionIndex();
    }

    /**
     * Returns the second index argument from the most recent
     * <code>addSelectionInterval</code> or <code>setSelectionInterval</code>
     * call.
     * This is a convenience method that just  delegates to the 
     * <code>selectionModel</code>.
     *
     * @return the index that most recently ended a interval selection
     * @see ListSelectionModel#getLeadSelectionIndex
     * @see #addSelectionInterval
     * @see #setSelectionInterval
     * @see #addListSelectionListener
     */
    public int getLeadSelectionIndex() {
        return getSelectionModel().getLeadSelectionIndex();
    }

    /**
     * Sets the data model's <code>isAdjusting</code> property to true,
     * so that a single event will be generated when all of the selection
     * events have finished (for example, when the mouse is being
     * dragged over the list in selection mode).
     *
     * @param b the boolean value for the property value
     * @see ListSelectionModel#setValueIsAdjusting
     */
    public void setValueIsAdjusting(boolean b) {
        getSelectionModel().setValueIsAdjusting(b);
    }

    /**
     * Returns the value of the data model's <code>isAdjusting</code> property.
     * This value is true if multiple changes are being made.
     *
     * @return true if multiple selection-changes are occurring, as
     *         when the mouse is being dragged over the list
     * @see ListSelectionModel#getValueIsAdjusting
     */
    public boolean getValueIsAdjusting() {
        return getSelectionModel().getValueIsAdjusting();
    }

    /**
     * Returns the lowest selected item index.
     */
    public int getMinSelectionIndex() {
        return selectionModel.getMinSelectionIndex();
    }

    /**
     * Returns the highest selected item index.
     */
    public int getMaxSelectionIndex() {
        return selectionModel.getMaxSelectionIndex();
    }

    /**
     * Sets the flag that determines whether this list allows multiple
     * selections.
     *
     * @param mode the selection mode. Allowed values are:<p>
     *             <ul>
     *             <li> ListSelectionModel.SINGLE_SELECTION. Only one list index
     *             can be selected at a time.
     *             <li> ListSelectionModel.MULTIPLE_INTERVAL_SELECTION. Any 
     *             number of list items can be selected simultaneously.
     *             </ul>
     */
    public void setSelectionMode(int mode) {
        selectionModel.setSelectionMode(mode);
        if (mode == ListSelectionModel.SINGLE_SELECTION)
            selectionModel.clearSelection();
    }

    /**
     * Determines whether this list allows multiple selections.
     */
    public int getSelectionMode() {
        return selectionModel.getSelectionMode();
    }

    /**
     * Determines if the specified item in this scrolling list is selected.
     */
    public boolean isSelectedIndex(int index) {
        return selectionModel.isSelectedIndex(index);
    }

    public Dimension getMinimumSize() {
//        // calculate the minimum number of columns that will contain all the
//        // items in the list
//        columns = 1;
//        for (int i = 0; i < listModel.getSize(); i++) {
//            String c = listModel.getElementAt(i).toString();
//            if (c.length() > columns)
//                columns = c.length();
//        }
//
//        // Take into account the border inherited from the JComponent
//        // superclass
//        Insets insets = getInsets();
//        return new Dimension(columns + insets.left + insets.right,
//                visibleRows + insets.top + insets.bottom);
        int lastRow = getModel().getSize() - 1;
        if (lastRow < 0) {
            return new Dimension(0, 0);
        }

        Insets insets = getInsets();
        int width = columns + insets.left + insets.right;
        int height;

        Rectangle bounds = getCellBounds(lastRow, lastRow);
        if (bounds != null) {
            height = bounds.y + bounds.height + insets.bottom;
        } else {
            height = 0;
        }
        
        return new Dimension(width, height);
    }

    public void requestFocus() {
//        if (!hasFocus() 
//                && getSelectionMode() == ListSelectionModel.SINGLE_SELECTION 
//                && getSelectedIndex() == -1 
//                && listModel.getSize() > 0) {
//                
//            setSelectedIndex(currentRow);
//        }
        
        // generate the FOCUS_GAINED event
        super.requestFocus();

        int lead = getLeadSelectionIndex();
        if (lead != -1) {
            ensureIndexIsVisible(lead);
            
            // get the absolute origin of this component
            Point  origin = getParent().getLocationOnScreen();
            Insets insets = getInsets();
            origin.translate(insets.left, insets.top + getLocation().y);
            
            SwingUtilities.windowForComponent(this).setCursor(
                    origin.addOffset(0, lead));
        }
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
        
        color            = colors.getColor(ColorScheme.LIST);
        selectedColor    = colors.getColor(ColorScheme.LIST_SELECTED);
        highlightedColor = colors.getColor(ColorScheme.LIST_HIGHLIGHTED);
        disabledColor    = colors.getColor(ColorScheme.LIST_DISABLED);
    }
    
    // TODO: Implement drawing within clipping rectangle
    
    public void paint(Graphics g) {
        // Draw the border if it exists
        super.paint(g);
        
        Rectangle paintBounds = g.getClipBounds();
        //System.out.println("paintBounds: " + paintBounds);

        // blank out free space
        final ColorPair listColor = (enabled ? getColor() : disabledColor);
        
        g.setColor(listColor);
        g.fillRect(paintBounds.x, paintBounds.y, 
                paintBounds.width, paintBounds.height);

        ListModel dataModel = getModel();
        int size = dataModel.getSize();
        if (size == 0) {
            return;
        }

        // determine how many rows we need to paint
        int maxY         = paintBounds.y + paintBounds.height;
        int leadIndex    = getLeadSelectionIndex();
        int rowIncrement = 1;
        
        int startRow = locationToIndex(new Point(paintBounds.x, paintBounds.y));
        int endRow   = startRow + getVisibleRowCount();
        int index    = startRow;
        
        Rectangle rowBounds = getCellBounds(index, index);
        //System.out.println("rowBounds: " + rowBounds);
        if (rowBounds == null) {
            // Not valid, bail!
            return;
        }
        
        StringBuffer blanks = new StringBuffer();
        for (int j = 0; j < columns; j++) {
            blanks.append(' ');
        }
        
        String emptyRow = blanks.toString();
        
        while (startRow < endRow && rowBounds.y < maxY && index < size) {
            g.setClip(rowBounds.x, rowBounds.y, rowBounds.width,
                    rowBounds.height);
            g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width,
                    paintBounds.height);
            paintCell(g, index, rowBounds, dataModel, emptyRow, leadIndex);
            
            rowBounds.y += rowBounds.height;
            index += rowIncrement;
            startRow++;
        }
    }
    
    private void paintCell(Graphics g, int i, Rectangle rowBounds, 
            ListModel dataModel, String emptyRow, int leadIndex) {
        
        ColorPair color;
        if (isEnabled()) {
            color = getColor();
            if (i == leadIndex && hasFocus()) {
                color = selectedColor;
            }

            if (isSelectedIndex(i)) {
                color = ColorPair.create(
                        highlightedColor.getForeground(), 
                        color.getBackground());
            }
        } else {
            color = disabledColor;
        }
        
        String    item = listModel.getElementAt(i).toString();
        final int len  = item.length();
        
        g.setColor(color);

        if (len < columns) {
            g.drawString(item, rowBounds.x, rowBounds.y);
            g.drawString(emptyRow.substring(len), rowBounds.x + len, rowBounds.y);
        
        } else if (len > columns) {
            g.drawString(item.substring(0, columns), rowBounds.x, rowBounds.y);
        } else {
            g.drawString(item, rowBounds.x, rowBounds.y);
        }
    }
    
    protected void processKeyEvent(KeyEvent ke) {
        // First call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed()) {
            return;
        }

        final int key = ke.getKeyCode();
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown())) {
            transferFocusBackward();
            return;
        }

        if (key == KeyEvent.VK_TAB) {
            transferFocus();
            return;
        }
    }
    
    protected void selectByKeyboard() {
        doSelect();
    }

    private void doSelect() {
        // Pressing INSERT or right-clicking on a row selects/deselects
        // the current row. If the list is empty, ignore the keystroke.
        if (listModel.getSize() == 0) {
            return;
        }
        
        repaint();
    }
}
