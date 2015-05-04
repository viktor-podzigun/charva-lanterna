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

package charvax.swing.plaf;

import charva.awt.ColorScheme;
import charva.awt.Component;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.ActionEvent;
import charva.awt.event.FocusEvent;
import charva.awt.event.FocusListener;
import charva.awt.event.InputEvent;
import charva.awt.event.KeyEvent;
import charva.awt.event.KeyListener;
import charva.awt.event.MouseEvent;
import charva.awt.event.MouseListener;
import charvax.swing.AbstractAction;
import charvax.swing.ActionMap;
import charvax.swing.CellRendererPane;
import charvax.swing.InputMap;
import charvax.swing.JComponent;
import charvax.swing.JScrollPane;
import charvax.swing.JTable;
import charvax.swing.KeyStroke;
import charvax.swing.ListSelectionModel;
import charvax.swing.SwingUtilities;
import charvax.swing.table.TableCellRenderer;


/**
 * BasicTableUI implementation
 */
public class TableUI {

    private static InputMap     inputMap;
    private static ActionMap    actionMap;

    //
    // Instance Variables
    //

    // The JTable that is delegating the painting to this UI.
    protected JTable table;
    protected CellRendererPane rendererPane;

    // Listeners that are attached to the JTable
    protected KeyListener keyListener;
    protected FocusListener focusListener;
    protected MouseListener mouseInputListener;

    //
    // Helper class for keyboard actions
    //
    private static class NavigationalAction extends AbstractAction {

        protected int dx;
        protected int dy;
//        protected boolean toggle;
        protected boolean extend;
        protected boolean inSelection;

        protected int anchorRow;
        protected int anchorColumn;
        protected int leadRow;
        protected int leadColumn;

        protected NavigationalAction(int dx, int dy, boolean toggle,
                boolean extend, boolean inSelection) {

            this.dx = dx;
            this.dy = dy;
//            this.toggle = toggle;
            this.extend = extend;
            this.inSelection = inSelection;
        }

        private int clipToRange(int i, int a, int b) {
            return Math.min(Math.max(i, a), b - 1);
        }

        private void moveWithinTableRange(JTable table, int dx, int dy,
                boolean changeLead) {

            if (changeLead) {
                leadRow = clipToRange(leadRow + dy, 0, table.getRowCount());
                leadColumn = clipToRange(leadColumn + dx, 0,
                        table.getColumnCount());
            } else {
                anchorRow = clipToRange(anchorRow + dy, 0, table.getRowCount());
                anchorColumn = clipToRange(anchorColumn + dx, 0,
                        table.getColumnCount());
            }
        }

        private int selectionSpan(ListSelectionModel sm) {
            return sm.getMaxSelectionIndex() - sm.getMinSelectionIndex() + 1;
        }

        private int compare(int i, ListSelectionModel sm) {
            return compare(i, sm.getMinSelectionIndex(),
                    sm.getMaxSelectionIndex() + 1);
        }

        private int compare(int i, int a, int b) {
            return (i < a) ? -1 : (i >= b) ? 1 : 0;
        }

        private boolean moveWithinSelectedRange(JTable table, int dx, int dy,
                boolean ignoreCarry) {

            ListSelectionModel rsm = table.getSelectionModel();
            ListSelectionModel csm = table.getColumnSelectionModel();

            int newAnchorRow = anchorRow + dy;
            int newAnchorColumn = anchorColumn + dx;

            int rowSgn;
            int colSgn;
            int rowCount = selectionSpan(rsm);
            int columnCount = selectionSpan(csm);

            boolean canStayInSelection = (rowCount * columnCount > 1);
            if (canStayInSelection) {
                rowSgn = compare(newAnchorRow, rsm);
                colSgn = compare(newAnchorColumn, csm);
            } else {
                // If there is only one selected cell, there is no point
                // in trying to stay within the selected area. Move outside
                // the selection, wrapping at the table boundaries.
                rowCount = table.getRowCount();
                columnCount = table.getColumnCount();
                rowSgn = compare(newAnchorRow, 0, rowCount);
                colSgn = compare(newAnchorColumn, 0, columnCount);
            }

            anchorRow = newAnchorRow - rowCount * rowSgn;
            anchorColumn = newAnchorColumn - columnCount * colSgn;

            if (!ignoreCarry) {
                return moveWithinSelectedRange(table, rowSgn, colSgn, true);
            }
            return canStayInSelection;
        }

        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable) e.getSource();
            ListSelectionModel rsm = table.getSelectionModel();
            anchorRow = rsm.getAnchorSelectionIndex();
            leadRow = rsm.getLeadSelectionIndex();

            ListSelectionModel csm = table.getColumnSelectionModel();
            anchorColumn = csm.getAnchorSelectionIndex();
            leadColumn = csm.getLeadSelectionIndex();

            // Unfortunately, this strategy introduces bugs because
            // of the asynchronous nature of requestFocus() call below.
            // Introducing a delay with invokeLater() makes this work
            // in the typical case though race conditions then allow
            // focus to disappear altogether. The right solution appears
            // to be to fix requestFocus() so that it queues a request
            // for the focus regardless of who owns the focus at the
            // time the call to requestFocus() is made. The optimisation
            // to ignore the call to requestFocus() when the component
            // already has focus may ligitimately be made as the
            // request focus event is dequeued, not before.

            // boolean wasEditingWithFocus = table.isEditing() &&
            // table.getEditorComponent().isFocusOwner();

            if (!inSelection) {
                moveWithinTableRange(table, dx, dy, extend);
                if (!extend) {
                    table.changeSelection(anchorRow, anchorColumn, false, extend);
                } else {
                    table.changeSelection(leadRow, leadColumn, false, extend);
                }
            } else {
                if (moveWithinSelectedRange(table, dx, dy, false)) {
                    table.changeSelection(anchorRow, anchorColumn, true, true);
                } else {
                    table.changeSelection(anchorRow, anchorColumn, false, false);
                }
            }

            /*
             * if (wasEditingWithFocus) { table.editCellAt(anchorRow,
             * anchorColumn); final Component editorComp =
             * table.getEditorComponent(); if (editorComp != null) {
             * SwingUtilities.invokeLater(new Runnable() { public void run() {
             * editorComp.requestFocus(); } }); } }
             */
        }
    }

    private static class PagingAction extends NavigationalAction {

        private boolean forwards;
        private boolean vertically;
        private boolean toLimit;

        private PagingAction(boolean extend, boolean forwards,
                boolean vertically, boolean toLimit) {

            super(0, 0, false, extend, false);

            this.forwards = forwards;
            this.vertically = vertically;
            this.toLimit = toLimit;
        }

        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable) e.getSource();
            if (toLimit) {
                if (vertically) {
                    int rowCount = table.getRowCount();
                    this.dx = 0;
                    this.dy = forwards ? rowCount : -rowCount;
                } else {
                    int colCount = table.getColumnCount();
                    this.dx = forwards ? colCount : -colCount;
                    this.dy = 0;
                }
            } else {
                if (!(table.getParent().getParent() instanceof JScrollPane)) {
                    return;
                }

                Dimension delta = table.getParent().getSize();
                ListSelectionModel sm = (vertically ?
                        table.getSelectionModel()
                        : table.getColumnSelectionModel());

                int start = (extend ? sm.getLeadSelectionIndex()
                        : sm.getAnchorSelectionIndex());

                if (vertically) {
                    Rectangle r = table.getCellRect(start, 0, true);
                    r.y += forwards ? delta.height : -delta.height;
                    this.dx = 0;
                    int newRow = table.rowAtPoint(r.getLocation());
                    if (newRow == -1 && forwards) {
                        newRow = table.getRowCount();
                    }
                    this.dy = newRow - start;
                } else {
                    Rectangle r = table.getCellRect(0, start, true);
                    r.x += forwards ? delta.width : -delta.width;
                    int newColumn = table.columnAtPoint(r.getLocation());
                    if (newColumn == -1 && forwards) {
                        newColumn = table.getColumnCount();
                    }
                    this.dx = newColumn - start;
                    this.dy = 0;
                }
            }
            super.actionPerformed(e);
        }
    }

    /**
     * Action to invoke <code>selectAll</code> on the table.
     */
    private static class SelectAllAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            JTable table = (JTable) e.getSource();
            table.selectAll();
        }
    }

    //
    // The Table's focus listener
    //

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug. This
     * class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of BasicTableUI.
     */
    public class FocusHandler implements FocusListener {

        private void repaintAnchorCell() {
            int rc = table.getRowCount();
            int cc = table.getColumnCount();
            int ar = table.getSelectionModel().getAnchorSelectionIndex();
            int ac = table.getColumnSelectionModel().getAnchorSelectionIndex();
            if (ar < 0 || ar >= rc || ac < 0 || ac >= cc) {
                return;
            }

            table.repaint();
        }

        public void focusGained(FocusEvent e) {
            repaintAnchorCell();
        }

        public void focusLost(FocusEvent e) {
            repaintAnchorCell();
        }
    }

    //
    // The Table's mouse and mouse motion listeners
    //

    /**
     * This inner class is marked &quot;public&quot; due to a compiler bug. This
     * class should be treated as a &quot;protected&quot; inner class.
     * Instantiate it only within subclasses of BasicTableUI.
     */
    public class MouseInputHandler implements MouseListener {

        private boolean selectedOnPress;

        // The Table's mouse listener methods.

        public void mouseClicked(MouseEvent e) {
        }

        private void setValueIsAdjusting(boolean flag) {
            table.getSelectionModel().setValueIsAdjusting(flag);
            table.getColumnSelectionModel().setValueIsAdjusting(flag);
        }

        private boolean shouldIgnore(MouseEvent e) {
            return e.isConsumed()
                    || (!(SwingUtilities.isLeftMouseButton(e)
                            && table.isEnabled()));
        }

        public void mousePressed(MouseEvent e) {
            if (e.isConsumed()) {
                selectedOnPress = false;
                return;
            }
            selectedOnPress = true;
            adjustFocusAndSelection(e);
        }

        void adjustFocusAndSelection(MouseEvent e) {
            if (shouldIgnore(e)) {
                return;
            }

            Point p = new Point(e.getX(), e.getY());
            int row = table.rowAtPoint(p);
            int column = table.columnAtPoint(p);
            // The autoscroller can generate drag events outside the Table's
            // range.
            if ((column == -1) || (row == -1)) {
                return;
            }

            if (table.isRequestFocusEnabled()) {
                table.requestFocus();
            }

            boolean adjusting = (e.getID() == MouseEvent.MOUSE_PRESSED);
            setValueIsAdjusting(adjusting);
            table.changeSelection(row, column, e.isControlDown(),
                    e.isShiftDown());
        }

        public void mouseReleased(MouseEvent e) {
            if (selectedOnPress) {
                if (shouldIgnore(e)) {
                    return;
                }

                setValueIsAdjusting(false);
            } else {
                adjustFocusAndSelection(e);
            }
        }
    }

    //
    // Factory methods for the Listeners
    //

    /**
     * Creates the key listener for handling keyboard navigation in the JTable.
     */
    protected KeyListener createKeyListener() {
        return null;
    }

    /**
     * Creates the focus listener for handling keyboard navigation in the
     * JTable.
     */
    protected FocusListener createFocusListener() {
        return new FocusHandler();
    }

    /**
     * Creates the mouse listener for the JTable.
     */
    protected MouseListener createMouseInputListener() {
        return new MouseInputHandler();
    }

    //
    // The installation/uninstall procedures and support
    //

//    public static ComponentUI createUI(JComponent c) {
//        return new TableUI();
//    }

    // Installation

    public void installUI(JComponent c) {
        table = (JTable) c;

        rendererPane = new CellRendererPane();
        table.add(rendererPane);

        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    /**
     * Initialize JTable properties, e.g. font, foreground, and background. The
     * font, foreground, and background properties are only set if their current
     * value is either null or a UIResource, other properties are set if the
     * current value is null.
     *
     * @see #installUI
     */
    protected void installDefaults() {
    }

    /**
     * Attaches listeners to the JTable.
     */
    protected void installListeners() {
        focusListener = createFocusListener();
        keyListener = createKeyListener();
        mouseInputListener = createMouseInputListener();

        table.addFocusListener(focusListener);
        table.addKeyListener(keyListener);
        table.addMouseListener(mouseInputListener);
    }

    /**
     * Register all keyboard actions on the JTable.
     */
    protected void installKeyboardActions() {
        ActionMap map = getActionMap();

        SwingUtilities.replaceUIActionMap(table, map);
        InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        SwingUtilities.replaceUIInputMap(table,
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);
    }

    InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
            if (inputMap == null) {
                inputMap = createInputMap();
            }

            return inputMap;
        }
        return null;
    }

    ActionMap getActionMap() {
        if (actionMap == null) {
            actionMap = createActionMap();
        }
        return actionMap;
    }

    InputMap createInputMap() {
        InputMap map = new UIInputMap();

//        map.put("selectNextColumn", new NavigationalAction(1, 0, false, false,
//                false));
//        map.put("selectPreviousColumn", new NavigationalAction(-1, 0, false,
//                false, false));
//        map.put("selectNextRow", new NavigationalAction(0, 1, false, false,
//                false));
//        map.put("selectPreviousRow", new NavigationalAction(0, -1, false,
//                false, false));
//
//        map.put("selectNextColumnExtendSelection", new NavigationalAction(1, 0,
//                false, true, false));
//        map.put("selectPreviousColumnExtendSelection", new NavigationalAction(
//                -1, 0, false, true, false));
//        map.put("selectNextRowExtendSelection", new NavigationalAction(0, 1,
//                false, true, false));
//        map.put("selectPreviousRowExtendSelection", new NavigationalAction(0,
//                -1, false, true, false));
//
//        map.put("scrollUpChangeSelection", new PagingAction(false, false, true,
//                false));
//        map.put("scrollDownChangeSelection", new PagingAction(false, true,
//                true, false));
//        map.put("selectFirstColumn",
//                new PagingAction(false, false, false, true));
//        map.put("selectLastColumn", new PagingAction(false, true, false, true));
//
//        map.put("scrollUpExtendSelection", new PagingAction(true, false, true,
//                false));
//        map.put("scrollDownExtendSelection", new PagingAction(true, true, true,
//                false));
//        map.put("selectFirstColumnExtendSelection", new PagingAction(true,
//                false, false, true));
//        map.put("selectLastColumnExtendSelection", new PagingAction(true, true,
//                false, true));
//
//        map.put("selectFirstRow", new PagingAction(false, false, true, true));
//        map.put("selectLastRow", new PagingAction(false, true, true, true));
//
//        map.put("selectFirstRowExtendSelection", new PagingAction(true, false,
//                true, true));
//        map.put("selectLastRowExtendSelection", new PagingAction(true, true,
//                true, true));

        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK),
                "selectNextColumnCell");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK),
                "selectPreviousColumnCell");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK),
                "selectNextRowCell");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK),
                "selectPreviousRowCell");

        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK),
                "selectAll");

        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                "scrollLeftChangeSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                "scrollRightChangeSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_MASK),
                "scrollLeftExtendSelection");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK),
                "scrollRightExtendSelection");

        return map;
    }

    ActionMap createActionMap() {
        ActionMap map = new UIActionMap();

        map.put("selectNextColumn", new NavigationalAction(1, 0, false, false,
                false));
        map.put("selectPreviousColumn", new NavigationalAction(-1, 0, false,
                false, false));
        map.put("selectNextRow", new NavigationalAction(0, 1, false, false,
                false));
        map.put("selectPreviousRow", new NavigationalAction(0, -1, false,
                false, false));

        map.put("selectNextColumnExtendSelection", new NavigationalAction(1, 0,
                false, true, false));
        map.put("selectPreviousColumnExtendSelection", new NavigationalAction(
                -1, 0, false, true, false));
        map.put("selectNextRowExtendSelection", new NavigationalAction(0, 1,
                false, true, false));
        map.put("selectPreviousRowExtendSelection", new NavigationalAction(0,
                -1, false, true, false));

        map.put("scrollUpChangeSelection", new PagingAction(false, false, true,
                false));
        map.put("scrollDownChangeSelection", new PagingAction(false, true,
                true, false));
        map.put("selectFirstColumn",
                new PagingAction(false, false, false, true));
        map.put("selectLastColumn", new PagingAction(false, true, false, true));

        map.put("scrollUpExtendSelection", new PagingAction(true, false, true,
                false));
        map.put("scrollDownExtendSelection", new PagingAction(true, true, true,
                false));
        map.put("selectFirstColumnExtendSelection", new PagingAction(true,
                false, false, true));
        map.put("selectLastColumnExtendSelection", new PagingAction(true, true,
                false, true));

        map.put("selectFirstRow", new PagingAction(false, false, true, true));
        map.put("selectLastRow", new PagingAction(false, true, true, true));

        map.put("selectFirstRowExtendSelection", new PagingAction(true, false,
                true, true));
        map.put("selectLastRowExtendSelection", new PagingAction(true, true,
                true, true));

        map.put("selectNextColumnCell", new NavigationalAction(1, 0, true,
                false, true));
        map.put("selectPreviousColumnCell", new NavigationalAction(-1, 0, true,
                false, true));
        map.put("selectNextRowCell", new NavigationalAction(0, 1, true, false,
                true));
        map.put("selectPreviousRowCell", new NavigationalAction(0, -1, true,
                false, true));

        map.put("selectAll", new SelectAllAction());

        boolean isLeftToRight = true;
        if (isLeftToRight) {
            map.put("scrollLeftChangeSelection", new PagingAction(false, false,
                    false, false));
            map.put("scrollRightChangeSelection", new PagingAction(false, true,
                    false, false));
            map.put("scrollLeftExtendSelection", new PagingAction(true, false,
                    false, false));
            map.put("scrollRightExtendSelection", new PagingAction(true, true,
                    false, false));
        } else {
            map.put("scrollLeftChangeSelection", new PagingAction(false, true,
                    false, false));
            map.put("scrollRightChangeSelection", new PagingAction(false,
                    false, false, false));
            map.put("scrollLeftExtendSelection", new PagingAction(true, true,
                    false, false));
            map.put("scrollRightExtendSelection", new PagingAction(true, false,
                    false, false));
        }
        return map;
    }

    // Uninstallation

    public void uninstallUI(JComponent c) {
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();

        table.remove(rendererPane);
        rendererPane = null;
        table = null;
    }

    protected void uninstallDefaults() {
    }

    protected void uninstallListeners() {
        table.removeFocusListener(focusListener);
        table.removeKeyListener(keyListener);
        table.removeMouseListener(mouseInputListener);

        focusListener = null;
        keyListener = null;
        mouseInputListener = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(table,
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null);
        SwingUtilities.replaceUIActionMap(table, null);
    }

    //
    // Size Methods
    //

    private Dimension createTableSize(long width) {
        int height = 0;
        int rowCount = table.getRowCount();
        if (rowCount > 0 && table.getColumnCount() > 0) {
            Rectangle r = table.getCellRect(rowCount - 1, 0, true);
            height = r.y + r.height;
        }
        // Width is always positive. The call to abs() is a workaround for
        // a bug in the 1.1.6 JIT on Windows.
        long tmp = Math.abs(width);
        if (tmp > Integer.MAX_VALUE) {
            tmp = Integer.MAX_VALUE;
        }
        return new Dimension((int) tmp, height);
    }

    /**
     * Return the minimum size of the table. The minimum height is the row
     * height times the number of rows. The minimum width is the sum of the
     * minimum widths of each column.
     */
    public Dimension getMinimumSize(JComponent c) {
        long width = 0;
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            width = width + table.getColumnWidth(i);
        }
        return createTableSize(width);
    }

    /**
     * Return the preferred size of the table. The preferred height is the row
     * height times the number of rows. The preferred width is the sum of the
     * preferred widths of each column.
     */
    public Dimension getPreferredSize(JComponent c) {
        return getMinimumSize(c);
    }

    /**
     * Return the maximum size of the table. The maximum height is the row
     * heighttimes the number of rows. The maximum width is the sum of the
     * maximum widths of each column.
     */
    public Dimension getMaximumSize(JComponent c) {
        return getMinimumSize(c);
    }

    public void setColors(ColorScheme colors) {

    }

    //
    // Paint methods and support
    //

    /**
     * Paint a representation of the <code>table</code> instance that was set
     * in installUI().
     */
    public void paint(Graphics g, JComponent c) {
        if (table.getRowCount() <= 0 || table.getColumnCount() <= 0) {
            return;
        }
        Rectangle clip = g.getClipBounds();
        Point upperLeft = clip.getLocation();
        Point lowerRight = new Point(clip.x + clip.width - 1, clip.y
                + clip.height - 1);
        int rMin = table.rowAtPoint(upperLeft);
        int rMax = table.rowAtPoint(lowerRight);
        // This should never happen.
        if (rMin == -1) {
            rMin = 0;
        }
        // If the table does not have enough rows to fill the view we'll get -1.
        // Replace this with the index of the last row.
        if (rMax == -1) {
            rMax = table.getRowCount() - 1;
        }

        boolean isLeftToRight = true;
        int cMin = table.columnAtPoint(isLeftToRight ? upperLeft : lowerRight);
        int cMax = table.columnAtPoint(isLeftToRight ? lowerRight : upperLeft);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get
        // -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = table.getColumnCount() - 1;
        }

        // Paint the grid.
        paintGrid(g, rMin, rMax, cMin, cMax);

        // Paint the cells.
        paintCells(g, rMin, rMax, cMin, cMax);
    }

    /*
     * Paints the grid lines within <I>aRect</I>, using the grid color set with
     * <I>setGridColor</I>. Paints vertical lines if <code>getShowVerticalLines()</code>
     * returns true and paints horizontal lines if <code>getShowHorizontalLines()</code>
     * returns true.
     */
    private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax) {
        g.setColor(table.getGridColor());

        Rectangle minCell = table.getCellRect(rMin, cMin, true);
        Rectangle maxCell = table.getCellRect(rMax, cMax, true);
        Rectangle damagedArea = minCell.union(maxCell);

        if (table.getShowHorizontalLines()) {
            int rowHeight = table.getRowHeight();
            int tableWidth = damagedArea.x + damagedArea.width;
            int y = damagedArea.y;
            for (int row = rMin; row <= rMax; row++) {
                y += rowHeight;
                g.drawHLine(damagedArea.x, y - 1, tableWidth - 1);
            }
        }
        if (table.getShowVerticalLines()) {
            int tableHeight = damagedArea.y + damagedArea.height;
            int x;
            boolean isLeftToRight = true;
            if (isLeftToRight) {
                x = damagedArea.x;
                for (int column = cMin; column <= cMax; column++) {
                    int w = table.getColumnWidth(column);
                    x += w;
                    g.drawVLine(x - 1, 0, tableHeight - 1);
                }
            } else {
                x = damagedArea.x + damagedArea.width;
                for (int column = cMin; column < cMax; column++) {
                    int w = table.getColumnWidth(column);
                    x -= w;
                    g.drawVLine(x - 1, 0, tableHeight - 1);
                }
                x -= table.getColumnWidth(cMax);
                g.drawVLine(x, 0, tableHeight - 1);
            }
        }
    }

    private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
        int columnMargin = 1;//table.getColumnMargin();
        Rectangle cellRect;
        int columnWidth;
        boolean isLeftToRight = true;

        if (isLeftToRight) {
            for (int row = rMin; row <= rMax; row++) {
                cellRect = table.getCellRect(row, cMin, false);
                for (int column = cMin; column <= cMax; column++) {
                    columnWidth = table.getColumnWidth(column);
                    cellRect.width = columnWidth - columnMargin;
                    paintCell(g, cellRect, row, column);
                    cellRect.x += columnWidth;
                }
            }
        } else {
            for (int row = rMin; row <= rMax; row++) {
                cellRect = table.getCellRect(row, cMin, false);
                columnWidth = table.getColumnWidth(cMin);
                cellRect.width = columnWidth - columnMargin;
                paintCell(g, cellRect, row, cMin);

                for (int column = cMin + 1; column <= cMax; column++) {
                    columnWidth = table.getColumnWidth(column);
                    cellRect.width = columnWidth - columnMargin;
                    cellRect.x -= columnWidth;
                    paintCell(g, cellRect, row, column);
                }
            }
        }

        // Remove any renderers that may be left in the rendererPane.
        rendererPane.removeAll();
    }

    private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component component = table.prepareRenderer(renderer, row, column);
        rendererPane.paintComponent(g, component, table, cellRect.x,
                cellRect.y, cellRect.width, cellRect.height, true);
    }
}
