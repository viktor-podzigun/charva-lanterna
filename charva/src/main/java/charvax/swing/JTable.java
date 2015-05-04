/* class JTable
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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import charva.awt.ColorPair;
import charva.awt.ColorScheme;
import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.event.KeyEvent;
import charvax.swing.event.ListSelectionEvent;
import charvax.swing.event.ListSelectionListener;
import charvax.swing.event.TableModelEvent;
import charvax.swing.event.TableModelListener;
import charvax.swing.plaf.TableUI;
import charvax.swing.table.DefaultTableCellRenderer;
import charvax.swing.table.DefaultTableModel;
import charvax.swing.table.JTableHeader;
import charvax.swing.table.TableCellRenderer;
import charvax.swing.table.TableModel;


/**
 * JTable is a user-interface component that displays data in a two-
 * dimensional table format.
 */
public class JTable extends JComponent implements TableModelListener,
        Scrollable, ListSelectionListener {

    //TODO: Implement this class

    private TableModel              model;
    private SizeSequence            columnModel;

    /** The <code>JTableHeader</code> working with the table. */
    protected JTableHeader          tableHeader;

    private Dimension               viewportSize;
    private boolean                 viewportSizeSet;

    /**
     * This instance variable determines the row that will
     * be highlighted when the table has input focus.
     */
    private int                     currentRow;

    private int                     currentColumn;

    private boolean                 columnSelectionAllowed = true;

    private boolean                 rowSelectionAllowed = true;

    protected ListSelectionModel    rowSelectionModel =
            new DefaultListSelectionModel();

    protected ListSelectionModel    columnSelectionModel =
            new DefaultListSelectionModel();
    
    /**
     * A table of objects that display the contents of a cell,
     * indexed by class as declared in <code>getColumnClass</code>
     * in the <code>TableModel</code> interface.
     */
    protected HashMap               defaultRenderersByColumnClass;
    
    protected ColorPair             selectionColor;
    protected ColorPair             highlightedColor;
    protected ColorPair             disabledColor;
    
    private TableUI                 uiComponent = new TableUI();

    
    /**
     * Default constructor
     */
    public JTable() {
        this(new DefaultTableModel(0, 0));
    }

    /**
     * Constructs a table of numRows and numColumns of empty cells
     * using a DefaultTableModel.
     */
    public JTable(int numRows, int numColumns) {
        this(new DefaultTableModel(numRows, numColumns));
    }

    /**
     * Construct a JTable from the specified data and column names, using
     * a DefaultTableModel.
     */
    public JTable(Object[][] data, Object[] columnNames) {
        this(new DefaultTableModel(data, columnNames));
    }

    /**
     * Construct a JTable with the specified data model
     */
    public JTable(TableModel model) {
        setModel(model);
        setTableHeader(new JTableHeader());
        rowSelectionModel.addListSelectionListener(this);
        createDefaultRenderers();
    }
    
    public ColorPair getSelectionColor() {
        return selectionColor;
    }
    
    public void setSelectionColor(ColorPair selectionColor) {
        this.selectionColor = selectionColor;
    }

    /**
     * Calls the <code>configureEnclosingScrollPane</code> method
     *
     * @see #configureEnclosingScrollPane
     */
    public void addNotify() {
        super.addNotify();
        configureEnclosingScrollPane();
        
        uiComponent.installUI(this);
    }

    /**
     * If this <code>JTable</code> is the <code>viewportView</code> of an
     * enclosing <code>JScrollPane</code> (the usual situation), configure
     * this <code>ScrollPane</code> by, amongst other things, installing the
     * table's <code>tableHeader</code> as the <code>columnHeaderView</code>
     * of the scroll pane. When a <code>JTable</code> is added to a
     * <code>JScrollPane</code> in the usual way, using
     * <code>new JScrollPane(myTable)</code>, <code>addNotify</code> is
     * called in the <code>JTable</code> (when the table is added to the
     * viewport). <code>JTable</code>'s <code>addNotify</code> method in
     * turn calls this method, which is protected so that this default
     * installation procedure can be overridden by a subclass.
     * 
     * @see #addNotify
     */
    protected void configureEnclosingScrollPane() {
        Container p = getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane)gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != this) {
                    return;
                }
                
                scrollPane.setColumnHeaderView(getTableHeader());
            }
        }
    }

    /**
     * Calls the <code>unconfigureEnclosingScrollPane</code> method
     *
     * @see #unconfigureEnclosingScrollPane
     */
    public void removeNotify() {
        uiComponent.uninstallUI(this);
        unconfigureEnclosingScrollPane();
        super.removeNotify();
    }

    /**
     * Reverses the effect of <code>configureEnclosingScrollPane</code>
     * by replacing the <code>columnHeaderView</code> of the enclosing
     * scroll pane with <code>null</code>. <code>JTable</code>'s
     * <code>removeNotify</code> method calls
     * this method, which is protected so that this default uninstallation
     * procedure can be overridden by a subclass.
     *
     * @see #removeNotify
     * @see #configureEnclosingScrollPane
     */
    protected void unconfigureEnclosingScrollPane() {
        Container p = getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane)gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != this) {
                    return;
                }
                
                scrollPane.setColumnHeaderView(null);
            }
        }
    }
    
    /**
     * Sets the <code>tableHeader</code> working with this <code>JTable</code> 
     * to <code>newHeader</code>.
     * It is legal to have a <code>null</code> <code>tableHeader</code>.
     *
     * @param   tableHeader  new tableHeader
     * @see     #getTableHeader
     */
    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != tableHeader) {
            JTableHeader old = this.tableHeader;
            // Release the old header
            if (old != null) {
                old.setTable(null);
            }
        
            this.tableHeader = tableHeader;
            if (tableHeader != null) {
                tableHeader.setTable(this);
            }
        }
    }

    /**
     * Returns the <code>tableHeader</code> used by this <code>JTable</code>.
     *
     * @return  the <code>tableHeader</code> used by this table
     * @see     #setTableHeader
     */
    public JTableHeader getTableHeader() {
        return tableHeader;
    }

    //
    // MouseListener interface
    //

//    /**
//     * JTable implements the MouseListener interface so that rows and columns
//     * can be selected with a single mouse click instead of the cursor keys. A
//     * double-click has the same effect as pressing ENTER.
//     * 
//     * @param e  the MouseEvent object
//     */
//    public void mouseClicked(MouseEvent e) {
//        if (e.getClickCount() == 1) {
//            // calculate which column the mouse-click was in
//            int x = 0;
//            int columnSelectedByMouse = 0;
//            int xMouseOffset = e.getX() - 1;
//            for (int i = 0; i < model.getColumnCount(); i++) {
//                x += columnWidths[i] + 1;
//                if (xMouseOffset >= x)
//                    columnSelectedByMouse++;
//                else
//                    break;
//            }
//            
//            currentColumn = columnSelectedByMouse;
//            currentRow    = e.getY() - 1;
//
//        } else if (e.getClickCount() == 2) {
//            if (getColumnSelectionAllowed())
//                selectCurrentColumn();
//
//            if (getRowSelectionAllowed())
//                selectCurrentRow();
//        }
//
//        repaint();
//    }

    /**
     * Sets the data model to the specified TableModel and registers with it
     * as a listener for events from the model.
     */
    public void setModel(TableModel model) {
        TableModel oldModel = this.model;
        this.model = model;
        updateColumnModel();
        model.addTableModelListener(this);
        
        if (oldModel != null) {
            oldModel.removeTableModelListener(this);
        }
    }

    public TableModel getModel() {
        return model;
    }

    /**
     * Returns the number of rows in this table's model.
     * @return the number of rows in this table's model
     *
     * @see #getColumnCount
     */
    public int getRowCount() {
        return getModel().getRowCount();
    }

    /**
     * Returns the number of columns in the column model. Note that this may
     * be different from the number of columns in the table model.
     *
     * @return  the number of columns in the table
     * @see #getRowCount()
     */
    public int getColumnCount() {
        return getModel().getColumnCount();
    }

    /**
     * Returns the name of the column appearing in the view at
     * column position <code>column</code>.
     *
     * @param  column    the column in the view being queried
     * @return the name of the column at position <code>column</code>
            in the view where the first column is column 0
     */
    public String getColumnName(int column) {
        return getModel().getColumnName(column);
    }

    public int getColumnWidth(int column) {
        return columnModel.getSize(column);
    }

    /**
     * Returns the type of the column appearing in the view at
     * column position <code>column</code>.
     *
     * @param   column   the column in the view being queried
     * @return the type of the column at position <code>column</code>
     *      in the view where the first column is column 0
     */
    public Class getColumnClass(int column) {
        return getModel().getColumnClass(column);
    }

    public void setValueAt(Object object, int row, int column) {
        model.setValueAt(object, row, column);
    }

    public Object getValueAt(int row, int column) {
        return model.getValueAt(row, column);
    }

    /**
     * This method implements the TableModelListener interface;
     * it is invoked when this table's TableModel generates a
     * TableModelEvent.
     */
    public void tableChanged(TableModelEvent evt) {
        if (evt.getFirstRow() == TableModelEvent.HEADER_ROW 
                && evt.getLastRow() == TableModelEvent.HEADER_ROW) {
            
            updateColumnModel();
        }
        
        repaint();
    }

    public void requestFocus() {
        // generate the FOCUS_GAINED event
        super.requestFocus();

        // get the absolute origin of this component
        Point origin = getLocationOnScreen();

        // calculate the x position of the cursor
        int x = 1;
        for (int i = 0; i < currentColumn; i++) {
            x += columnModel.getSize(i) + 1;
        }

        // ensure that the new cursor position is not off the screen (which
        // it can be if the JTable is in a JViewport)
        Point newCursor = origin.addOffset(x, currentRow + 1);
        if (newCursor.x < 0) {
            newCursor.x = 0;
        }
        
        if (newCursor.y < 0) {
            newCursor.y = 0;
        }
        
        SwingUtilities.windowForComponent(this).setCursor(newCursor);
    }

    public void setColors(ColorScheme colors) {
        super.setColors(colors);
        
        //uiComponent.setColors(colors);
        
        color            = colors.getColor(ColorScheme.LIST);
        selectionColor    = colors.getColor(ColorScheme.LIST_SELECTED);
        highlightedColor = colors.getColor(ColorScheme.LIST_HIGHLIGHTED);
        disabledColor    = colors.getColor(ColorScheme.LIST_DISABLED);
    }
    
    public ColorPair getGridColor() {
        return color;
    }
    
    public boolean getShowHorizontalLines() {
        return false;
    }
    
    public boolean getShowVerticalLines() {
        return true;
    }
    
    // TODO: Implement drawing within clipping rectangle
    
    public void paint(Graphics g) {
        // Draw the border if it exists
        super.paint(g);
        
        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        ColorPair color = (enabled ? getColor() : disabledColor);
        
        int rows    = model.getRowCount();
        int columns = model.getColumnCount();

        // start by blanking out the table area and drawing the box
        // around the table
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawRect(0, 0, getWidth(), getHeight());

        // now fill in the table headings
        int x = 1;
        for (int i = 0; i < columns; i++) {
            g.drawChar(' ', x, 0);
            String name = model.getColumnName(i);
            g.drawString(model.getColumnName(i), x + 1, 0);
            g.drawChar(' ', x + 1 + name.length(), 0);
            x += columnModel.getSize(i) + 1;
        }

        // now draw the vertical lines that divide the columns
        if (model.getColumnCount() != 0) {
            x = columnModel.getSize(0) + 1;
            for (int i = 0; i < columns - 1; i++) {
                g.drawChar(GraphicsConstants.VS_TTEE, x, 0);        // top tee
                g.drawVLine(x + 1, 1, rows, GraphicsConstants.VS_VLINE);
                g.drawChar(GraphicsConstants.VS_BTEE, x, rows + 1); // bottom tee
                
                x += columnModel.getSize(i + 1) + 1;
            }
        }

        // now draw the contents of the cells
        x = 1;
        boolean hasFocus = hasFocus();
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                ColorPair currColor = color;
                if (row == currentRow && hasFocus) {
                    currColor = selectionColor;
                }
    
                if (isRowSelected(row) || isColumnSelected(column)) {
                    currColor = ColorPair.create(
                            highlightedColor.getForeground(), 
                            currColor.getBackground());
                }
                
                Object value = model.getValueAt(row, column);
                if (value != null) {
                    g.setColor(currColor);
                    if (row == currentRow && hasFocus) {
                        g.drawHLine(x, row + 1, columnModel.getSize(column), ' ');
                    }
                    
                    g.drawString(value.toString(), x, row + 1);
                }
            }
            
            x += columnModel.getSize(column) + 1;
        }
    }

    private int getStepSize() {
        Container parent = getParent();
        if (parent instanceof JViewport) {
            JViewport vport = (JViewport) parent;
            return vport.getExtentSize().height - 1;
        }
        
        return 5;
    }

    /**
     * Processes key events occurring on this object
     */
    protected void processKeyEvent(KeyEvent ke) {
        // first call all KeyListener objects that may have been registered
        // for this component
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        final int key = ke.getKeyCode();
        
        if ((key == KeyEvent.VK_TAB && ke.isShiftDown())) {
            transferFocusBackward();
            return;
        }

        if (key == KeyEvent.VK_TAB) {
            transferFocus();
            return;
        }
        
        // allow no action if table empty
        if (model.getRowCount() == 0 || model.getColumnCount() == 0) {
            return;
        }

        if (key == KeyEvent.VK_UP) {
            if (currentRow == 0) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentRow--;
                int x = 0;
                for (int i = 0; i < currentColumn; i++) {
                    x += columnModel.getSize(i) + 1;
                }
            }
        } else if (key == KeyEvent.VK_PAGE_UP) {
            if (currentRow == 0) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentRow -= getStepSize();
                if (currentRow < 0)
                    currentRow = 0;
                
                int x = 0;
                for (int i = 0; i < currentColumn; i++) {
                    x += columnModel.getSize(i) + 1;
                }
            }
        } else if (key == KeyEvent.VK_PAGE_DOWN) {
            if (currentRow == model.getRowCount() - 1) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentRow += getStepSize();
                if (currentRow > model.getRowCount() - 1)
                    currentRow = model.getRowCount() - 1;
                
                int x = 0;
                for (int i = 0; i < currentColumn; i++)
                    x += columnModel.getSize(i) + 1;
            }
        } else if (key == KeyEvent.VK_DOWN) {
            if (currentRow == model.getRowCount() - 1) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentRow++;
                int x = 0;
                for (int i = 0; i < currentColumn; i++)
                    x += columnModel.getSize(i) + 1;
            }
        } else if (key == KeyEvent.VK_LEFT) {
            if (currentColumn == 0) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentColumn--;
                
                int x = 0;
                for (int i = 0; i < currentColumn; i++)
                    x += columnModel.getSize(i) + 1;
            }
        } else if (key == KeyEvent.VK_RIGHT) {
            if (currentColumn == model.getColumnCount() - 1) {
//                SwingUtilities.windowForComponent(this).getTerminal().beep();
            } else {
                currentColumn++;
                
                int x = 0;
                for (int i = 0; i <= currentColumn; i++)
                    x += columnModel.getSize(i) + 1;
            }
        } else if (key == KeyEvent.VK_HOME) {
            int x = 0;
            for (int i = 0; i < currentColumn; i++)
                x += columnModel.getSize(i) + 1;

        } else if (key == KeyEvent.VK_END) {
            int x = 0;
            for (int i = 0; i <= currentColumn; i++)
                x += columnModel.getSize(i) + 1;

        } else if (key == KeyEvent.VK_ENTER) {
            if (getColumnSelectionAllowed())
                selectCurrentColumn();

            if (getRowSelectionAllowed())
                selectCurrentRow();

            repaint();
        }

        if (!(getParent() instanceof JViewport)) {
            repaint();
//            draw(0);
//            requestFocus();
//            super.requestSync();
        }
    }

    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public Dimension getMinimumSize() {
        return this.getSize();
    }

    public int getWidth() {
        int columns = model.getColumnCount();
        int width = 1;
        for (int i = 0; i < columns; i++) {
            width += columnModel.getSize(i) + 1;
        }

        return width;
    }

    public int getHeight() {
        return model.getRowCount();
    }

    public void setPreferredScrollableViewportSize(Dimension size) {
        viewportSize    = size;
        viewportSizeSet = true;
    }

    public Dimension getPreferredScrollableViewportSize() {
        if (viewportSizeSet) {
            return new Dimension(viewportSize);
        }
        
        return getMinimumSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        // TODO Auto-generated method stub
        return 0;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect,
            int orientation, int direction) {
        
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * Returns false which indicates that the width of the viewport does not 
     * determine the width of this component.
     *
     * @return false
     * @see Scrollable#getScrollableTracksViewportWidth
     */
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    /**
     * Returns false to indicate that the height of the viewport does not
     * determine the height of this component.
     *
     * @return false
     * @see Scrollable#getScrollableTracksViewportHeight
     */
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    /**
     * Creates default cell renderers for objects, numbers, doubles, dates,
     * booleans, and icons.
     * @see javax.swing.table.DefaultTableCellRenderer
     *
     */
    protected void createDefaultRenderers() {
        defaultRenderersByColumnClass = new HashMap();

        // Objects
        setDefaultRenderer(Object.class, new charvax.swing.table.DefaultTableCellRenderer());

        // Numbers
        setDefaultRenderer(Number.class, new NumberRenderer());

        // Doubles and Floats
        setDefaultRenderer(Float.class, new DoubleRenderer());
        setDefaultRenderer(Double.class, new DoubleRenderer());

        // Dates
        setDefaultRenderer(Date.class, new DateRenderer());

        // Booleans
        setDefaultRenderer(Boolean.class, new BooleanRenderer());
    }

    /*
     * Default Renderers
     */
    
    static class NumberRenderer extends DefaultTableCellRenderer {
        
        public NumberRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    static class DoubleRenderer extends NumberRenderer {
        
        NumberFormat formatter;
        
        public DoubleRenderer() {
            super();
        }
    
        public void setValue(Object value) {
            if (formatter == null) {
                formatter = NumberFormat.getInstance();
            }
            setText((value == null) ? "" : formatter.format(value));
        }
    }

    static class DateRenderer extends DefaultTableCellRenderer {
        
        DateFormat formatter;
        
        public DateRenderer() {
            super();
        }
    
        public void setValue(Object value) {
            if (formatter==null) {
                formatter = DateFormat.getDateInstance();
            }
            
            setText((value == null) ? "" : formatter.format(value));
        }
    }

    static class BooleanRenderer extends JCheckBox implements TableCellRenderer {
        
        public BooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                               boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (isSelected) {
                setColor(table.getSelectionColor());
            
            } else {
                setColor(table.getColor());
            }
            
            setSelected((value != null && ((Boolean)value).booleanValue()));
            return this;
        }
    }

    /**
     * Sets a default cell renderer to be used if no renderer has been set in
     * a <code>TableColumn</code>. If renderer is <code>null</code>,
     * removes the default renderer for this column class.
     *
     * @param  columnClass     set the default cell renderer for this columnClass
     * @param  renderer        default cell renderer to be used for this columnClass
     * @see     #getDefaultRenderer
     */
    public void setDefaultRenderer(Class columnClass, TableCellRenderer renderer) {
        if (renderer != null) {
            defaultRenderersByColumnClass.put(columnClass, renderer);
        }
        else {
            defaultRenderersByColumnClass.remove(columnClass);
        }
    }

    /**
     * Returns the cell renderer to be used when no renderer has been set in a
     * <code>TableColumn</code>. During the rendering of cells the renderer
     * is fetched from a <code>Hashtable</code> of entries according to the
     * class of the cells in the column. If there is no entry for this
     * <code>columnClass</code> the method returns the entry for the most
     * specific superclass. The <code>JTable</code> installs entries for
     * <code>Object</code>, <code>Number</code>, and <code>Boolean</code>,
     * all of which can be modified or replaced.
     * 
     * @param columnClass  return the default cell renderer for this columnClass
     * @return the renderer for this columnClass
     * 
     * @see #setDefaultRenderer
     * @see #getColumnClass
     */
    public TableCellRenderer getDefaultRenderer(Class columnClass) {
        if (columnClass == null) {
            return null;
        }
        
        Object renderer = defaultRenderersByColumnClass.get(columnClass);
        if (renderer != null) {
            return (TableCellRenderer)renderer;
        }
        
        return getDefaultRenderer(columnClass.getSuperclass());
    }

    /**
     * Returns an appropriate renderer for the cell specified by this row and
     * column. If the <code>TableColumn</code> for this column has a non-null
     * renderer, returns that.  If not, finds the class of the data in
     * this column (using <code>getColumnClass</code>)
     * and returns the default renderer for this type of data.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to provide renderers so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param row       the row of the cell to render, where 0 is the first row
     * @param column    the column of the cell to render,
     *          where 0 is the first column
     * @return the assigned renderer; if <code>null</code>
     *          returns the default renderer
     *          for this type of object
     * @see charvax.swing.table.DefaultTableCellRenderer
     * @see #setDefaultRenderer
     */
    public TableCellRenderer getCellRenderer(int row, int column) {
        //TableColumn tableColumn = getColumnModel().getColumn(column);
        //TableCellRenderer renderer = tableColumn.getCellRenderer();
        //if (renderer == null) {
            return getDefaultRenderer(getColumnClass(column));
        //}
        
        //return renderer;
    }

    /**
     * Prepares the renderer by querying the data model for the
     * value and selection state
     * of the cell at <code>row</code>, <code>column</code>.
     * Returns the component (may be a <code>Component</code>
     * or a <code>JComponent</code>) under the event location.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to prepare renderers so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param renderer  the <code>TableCellRenderer</code> to prepare
     * @param row       the row of the cell to render, where 0 is the first row
     * @param column    the column of the cell to render,
     *          where 0 is the first column
     * @return          the <code>Component</code> under the event location
     */
    public Component prepareRenderer(TableCellRenderer renderer, int row,
            int column) {
        
        Object value = getValueAt(row, column);
        boolean isSelected = isCellSelected(row, column);
        boolean rowIsAnchor = (getSelectionModel().getAnchorSelectionIndex() == row);
        boolean colIsAnchor = 
            (getColumnSelectionModel().getAnchorSelectionIndex() == column);
        boolean hasFocus = (rowIsAnchor && colIsAnchor) && isFocusOwner();

        return renderer.getTableCellRendererComponent(this, value, isSelected,
                hasFocus, row, column);
    }

    /**
     * Sets the table's row selection model and registers for notifications from
     * the new selection model.
     */
    public void setSelectionModel(ListSelectionModel model) {
        rowSelectionModel = model;
        rowSelectionModel.addListSelectionListener(this);
    }

    /**
     * Returns the table's row selection model.
     */
    public ListSelectionModel getSelectionModel() {
        return rowSelectionModel;
    }

    /**
     * Sets the table's selection mode to allow selection of either single
     * rows and/or columns, or multiple rows and/or columns.
     *
     * @param mode the selection mode. Allowable values are
     *             ListSelectionModel.SINGLE_SELECTION and
     *             ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     */
    public void setSelectionMode(int mode) {
        rowSelectionModel.setSelectionMode(mode);
        columnSelectionModel.setSelectionMode(mode);
    }
    
    public ListSelectionModel getColumnSelectionModel() {
        return columnSelectionModel;
    }

    /**
     * Returns the table's row/column selection mode.
     */
    public int getSelectionMode() {
        return rowSelectionModel.getSelectionMode();
    }

    /**
     * Set whether selection of columns is allowed
     */
    public void setColumnSelectionAllowed(boolean allowed) {
        columnSelectionAllowed = allowed;
    }

    /**
     * Returns true if columns can be selected; otherwise false
     */
    public boolean getColumnSelectionAllowed() {
        return columnSelectionAllowed;
    }

    /**
     * Set whether selection of rows is allowed
     */
    public void setRowSelectionAllowed(boolean allowed) {
        rowSelectionAllowed = allowed;
    }

    /**
     * Returns true if rows can be selected; otherwise false
     */
    public boolean getRowSelectionAllowed() {
        return rowSelectionAllowed;
    }

    /**
     * Adds the columns from <code>index0</code> to <code>index1</code>,
     * inclusive, to the current selection
     */
    public void addColumnSelectionInterval(int index0, int index1) {
        columnSelectionModel.addSelectionInterval(index0, index1);
    }

    /**
     * Adds the rows from <code>index0</code> to <code>index1</code>,
     * inclusive, to the current selection
     */
    public void addRowSelectionInterval(int index0, int index1) {
        rowSelectionModel.addSelectionInterval(index0, index1);
    }

    /**
     * Selects the columns from <code>index0</code> to <code>index1</code>,
     * inclusive
     */
    public void setColumnSelectionInterval(int index0, int index1) {
        columnSelectionModel.setSelectionInterval(index0, index1);
    }

    /**
     * Selects the rows from <code>index0</code> to <code>index1</code>,
     * inclusive
     */
    public void setRowSelectionInterval(int index0, int index1) {
        rowSelectionModel.setSelectionInterval(index0, index1);
    }

    /**
     * Returns the index of the first selected row, or -1 if
     * no row is selected
     */
    public int getSelectedRow() {
        return rowSelectionModel.getMinSelectionIndex();
    }

    /**
     * Returns the number of selected rows
     */
    public int getSelectedRowCount() {
        int min = rowSelectionModel.getMinSelectionIndex();
        if (min == -1) {
            return 0;
        }

        int max = rowSelectionModel.getMaxSelectionIndex();
        int j = 0;
        for (int i = min; i <= max; i++) {
            if (rowSelectionModel.isSelectedIndex(i)) {
                j++;
            }
        }
        
        return j;
    }

    /**
     * Returns an array of the indices of all selected rows
     */
    public int[] getSelectedRows() {
        int rowCount = getSelectedRowCount();
        if (rowCount == 0) {
            return new int[0];
        }

        int[] array = new int[rowCount];
        int min = rowSelectionModel.getMinSelectionIndex();
        int max = rowSelectionModel.getMaxSelectionIndex();
        int j = 0;
        for (int i = min; i <= max; i++) {
            if (rowSelectionModel.isSelectedIndex(i)) {
                array[j++] = i;
            }
        }
        
        return array;
    }

    /**
     * Returns the index of the first selected column, or -1 if
     * no column is selected
     */
    public int getSelectedColumn() {
        return columnSelectionModel.getMinSelectionIndex();
    }

    /**
     * Returns the number of selected columns
     */
    public int getSelectedColumnCount() {
        int min = columnSelectionModel.getMinSelectionIndex();
        if (min == -1) {
            return 0;
        }

        int max = columnSelectionModel.getMaxSelectionIndex();
        int j = 0;
        for (int i = min; i <= max; i++) {
            if (columnSelectionModel.isSelectedIndex(i)) {
                j++;
            }
        }
        
        return j;
    }

    /**
     * Returns an array of the indices of all selected columns
     */
    public int[] getSelectedColumns() {
        int columnCount = getSelectedColumnCount();
        if (columnCount == 0) {
            return new int[0];
        }

        int[] array = new int[columnCount];
        int min = columnSelectionModel.getMinSelectionIndex();
        int max = columnSelectionModel.getMaxSelectionIndex();
        int j = 0;
        for (int i = min; i <= max; i++) {
            if (columnSelectionModel.isSelectedIndex(i)) {
                array[j++] = i;
            }
        }
        
        return array;
    }

    /**
     * Returns true if the row with the specified index is selected
     */
    public boolean isRowSelected(int row) {
        return rowSelectionModel.isSelectedIndex(row);
    }

    /**
     * Returns true if the column with the specified index is selected
     */
    public boolean isColumnSelected(int column) {
        return columnSelectionModel.isSelectedIndex(column);
    }

    /**
     * Returns true if the cell at the specified position is selected.
     * 
     * @param row     the row being queried
     * @param column  the column being queried
     * 
     * @return true if the cell at index <code>(row, column)</code> is
     *         selected, where the first row and first column are at index 0
     * @exception IllegalArgumentException
     *                if <code>row</code> or <code>column</code> are not in
     *                the valid range
     */
    public boolean isCellSelected(int row, int column) {
        if (!getRowSelectionAllowed() && !getColumnSelectionAllowed()) {
            return false;
        }
        
        return (!getRowSelectionAllowed() || isRowSelected(row))
                && (!getColumnSelectionAllowed() || isColumnSelected(column));
    }

    private void changeSelectionModel(ListSelectionModel sm, int index,
            boolean toggle, boolean extend, boolean selected) {
        if (extend) {
            if (toggle) {
                sm.setAnchorSelectionIndex(index);
            } else {
                sm.setLeadSelectionIndex(index);
            }
        } else {
            if (toggle) {
                if (selected) {
                    sm.removeSelectionInterval(index, index);
                } else {
                    sm.addSelectionInterval(index, index);
                }
            } else {
                sm.setSelectionInterval(index, index);
            }
        }
    }

    /**
     *  Selects all rows, columns, and cells in the table.
     */
    public void selectAll() {
        if (getRowCount() > 0 && getColumnCount() > 0) {
            setRowSelectionInterval(0, getRowCount() - 1);
            setColumnSelectionInterval(0, getColumnCount() - 1);
        }
    }

    /**
     * Deselects all selected columns and rows.
     */
    public void clearSelection() {
        getColumnSelectionModel().clearSelection();
        getSelectionModel().clearSelection();
    }

    /**
     * Updates the selection models of the table, depending on the state of the
     * two flags: <code>toggle</code> and <code>extend</code>. All changes
     * to the selection that are the result of keyboard or mouse events received
     * by the UI are channeled through this method so that the behavior may be
     * overridden by a subclass.
     * <p>
     * This implementation uses the following conventions:
     * <ul>
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>:
     * <em>false</em>. Clear the previous selection and ensure the new cell
     * is selected.
     * <li> <code>toggle</code>: <em>false</em>, <code>extend</code>:
     * <em>true</em>. Extend the previous selection to include the specified
     * cell.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>:
     * <em>false</em>. If the specified cell is selected, deselect it. If it
     * is not selected, select it.
     * <li> <code>toggle</code>: <em>true</em>, <code>extend</code>:
     * <em>true</em>. Leave the selection state as it is, but move the anchor
     * index to the specified location.
     * </ul>
     * 
     * @param rowIndex     affects the selection at <code>row</code>
     * @param columnIndex  affects the selection at <code>column</code>
     * @param toggle       see description above
     * @param extend       if true, extend the current selection
     * 
     */
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle,
            boolean extend) {
        
        ListSelectionModel rsm = getSelectionModel();
        ListSelectionModel csm = getColumnSelectionModel();

        // Check the selection here rather than in each selection model.
        // This is significant in cell selection mode if we are supposed
        // to be toggling the selection. In this case it is better to
        // ensure that the cell's selection state will indeed be changed.
        // If this were done in the code for the selection model it
        // might leave a cell in selection state if the row was
        // selected but the column was not - as it would toggle them both.
        boolean selected = isCellSelected(rowIndex, columnIndex);

        changeSelectionModel(csm, columnIndex, toggle, extend, selected);
        changeSelectionModel(rsm, rowIndex, toggle, extend, selected);

        // Scroll after changing the selection as blit scrolling is immediate,
        // so that if we cause the repaint after the scroll we end up painting
        // everything!
        //if (getAutoscrolls()) {
            Rectangle cellRect = getCellRect(rowIndex, columnIndex, false);
            if (cellRect != null) {
                scrollRectToVisible(cellRect);
            }
        //}
    }

    /**
     * Returns the height of a table row, in pixels.
     * The default row height is 1.
     *
     * @return  the height in pixels of a table row
     */
    public int getRowHeight() {
        return 1;
    }

//
// Cover methods for various models and helper methods
//

     /**
      * Returns the index of the column that <code>point</code> lies in,
      * or -1 if the result is not in the range
      * [0, <code>getColumnCount()</code>-1].
      *
      * @param   point   the location of interest
      * @return  the index of the column that <code>point</code> lies in,
      *      or -1 if the result is not in the range
      *      [0, <code>getColumnCount()</code>-1]
      * @see     #rowAtPoint
      */
     public int columnAtPoint(Point point) {
         int x = point.x;
         boolean isLeftToRight = true;
         if (!isLeftToRight) {
             x = getWidth() - x;
         }
         
         int result = columnModel.getIndex(x);
         if (result < 0 || result >= getRowCount()) {
             return -1;
         }
         
         return result;
     }

    /**
     * Returns the index of the row that <code>point</code> lies in,
     * or -1 if the result is not in the range
     * [0, <code>getRowCount()</code>-1].
     *
     * @param   point   the location of interest
     * @return  the index of the row that <code>point</code> lies in,
     *          or -1 if the result is not in the range
     *          [0, <code>getRowCount()</code>-1]
     * @see     #columnAtPoint
     */
    public int rowAtPoint(Point point) {
        int y = point.y;
        int result = y / getRowHeight();
        if (result < 0 || result >= getRowCount()) {
            return -1;
        }
        
        return result;
    }

    /**
     * Returns a rectangle for the cell that lies at the intersection of
     * <code>row</code> and <code>column</code>.
     * If <code>includeSpacing</code> is true then the value returned
     * has the full height and width of the row and column
     * specified. If it is false, the returned rectangle is inset by the
     * intercell spacing to return the true bounds of the rendering or
     * editing component as it will be set during rendering.
     * <p>
     * If the column index is valid but the row index is less
     * than zero the method returns a rectangle with the
     * <code>y</code> and <code>height</code> values set appropriately
     * and the <code>x</code> and <code>width</code> values both set
     * to zero. In general, when either the row or column indices indicate a
     * cell outside the appropriate range, the method returns a rectangle
     * depicting the closest edge of the closest cell that is within
     * the table's range. When both row and column indices are out
     * of range the returned rectangle covers the closest
     * point of the closest cell.
     * <p>
     * In all cases, calculations that use this method to calculate
     * results along one axis will not fail because of anomalies in
     * calculations along the other axis. When the cell is not valid
     * the <code>includeSpacing</code> parameter is ignored.
     *
     * @param   row                   the row index where the desired cell
     *                                is located
     * @param   column                the column index where the desired cell
     *                                is located in the display; this is not
     *                                necessarily the same as the column index
     *                                in the data model for the table; the
     *                                convertColumnIndexToView(int)
     *                                method may be used to convert a data
     *                                model column index to a display
     *                                column index
     * @param   includeSpacing        if false, return the true cell bounds -
     *                                computed by subtracting the intercell
     *                    spacing from the height and widths of
     *                    the column and row models
     *
     * @return  the rectangle containing the cell at location
     *          <code>row</code>,<code>column</code>
     */
    public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
        Rectangle r = new Rectangle();
        boolean valid = true;
        boolean isLeftToRight = true;
        if (row < 0) {
            // y = height = 0;
            valid = false;
        } else if (row >= getRowCount()) {
            r.y = getHeight();
            valid = false;
        } else {
            r.height = getRowHeight();
            r.y = row * r.height;
        }

        if (column < 0) {
            if (!isLeftToRight) {
                r.x = getWidth();
            }
            // otherwise, x = width = 0;
            valid = false;
        } else if (column >= getColumnCount()) {
            if (isLeftToRight) {
                r.x = getWidth();
            }
            // otherwise, x = width = 0;
            valid = false;
        } else {
            if (isLeftToRight) {
                for (int i = 0; i < column; i++) {
                    r.x += columnModel.getSize(i);
                }
            } else {
                for (int i = getColumnCount() - 1; i > column; i--) {
                    r.x += columnModel.getSize(i);
                }
            }
            r.width = columnModel.getSize(column);
        }

        if (valid && !includeSpacing) {
            int rm = 0;//getRowMargin();
            int cm = 0;//getColumnModel().getColumnMargin();
            // This is not the same as grow(), it rounds differently.
            r.setBounds(r.x + cm / 2, r.y + rm / 2, 
                    r.width - cm, r.height - rm);
        }
        return r;
    }

    /**
     * This method is invoked when the row selection changes
     */
    public void valueChanged(ListSelectionEvent e) {
        repaint();
    }

    private void updateColumnModel() {
        final int columnCount = model.getColumnCount();
        columnModel = new SizeSequence(columnCount);
        
        for (int column = 0; column < columnCount; column++) {
            // calculate the column width for the specified column
            int width = model.getColumnName(column).length() + 2;
            
            columnModel.setSize(column, width);
        }
    }

    private void selectCurrentColumn() {
        if (columnSelectionModel.isSelectedIndex(currentColumn)) {
            columnSelectionModel.removeSelectionInterval(
                    currentColumn, currentColumn);
        } else {
            int selectionMode = rowSelectionModel.getSelectionMode();

            // the column is not currently selected; select it.
            if (selectionMode == ListSelectionModel.SINGLE_SELECTION) {
                columnSelectionModel.setSelectionInterval(
                        currentColumn, currentColumn);
            } else {
                columnSelectionModel.addSelectionInterval(
                        currentColumn, currentColumn);
            }
        }
    }

    private void selectCurrentRow() {
        if (rowSelectionModel.isSelectedIndex(currentRow)) {
            rowSelectionModel.removeSelectionInterval(
                    currentRow, currentRow);
        } else {
            int selectionMode = rowSelectionModel.getSelectionMode();

            // the row is not currently selected; select it.
            if (selectionMode == ListSelectionModel.SINGLE_SELECTION) {
                rowSelectionModel.setSelectionInterval(
                        currentRow, currentRow);
            } else {
                rowSelectionModel.addSelectionInterval(
                        currentRow, currentRow);
            }
        }
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public int getCurrentColumn() {
        return currentColumn;
    }
}
