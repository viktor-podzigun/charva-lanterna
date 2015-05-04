/* class AbstractTableModel
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

package charvax.swing.table;

import java.util.ArrayList;
import charvax.swing.event.TableModelEvent;
import charvax.swing.event.TableModelListener;


/**
 * This abstract class provides default implementations for most of the methods
 * in the TableModel class. It takes care of the management of listeners, and
 * provides some convenience methods for generating TableModeEvents and
 * dispatching them to the listeners.
 * <p>
 * To implement a concrete TableModel as a subclass of AbstractTableModel, 
 * you only need to provide implementations of
 * the following methods:
 * <pre>
 * public int getRowCount();
 * public int getColumnCount();
 * public Object getValueAt(int row, int column);
 * </pre>
 */
public abstract class AbstractTableModel implements TableModel {
    
    protected ArrayList listeners = new ArrayList();
    
    
    /**
     * Adds a listener that will be notified each time the data model
     * changes.
     */
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    /**
     * Removes the specified listener from the list of listeners.
     */
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    /**
     *  Returns a default name for the column using spreadsheet conventions:
     *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     *  returns an empty string.
     *
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int column) {
        String result = "";
        for (; column >= 0; column = column / 26 - 1) {
            result = (char)((char)(column%26)+'A') + result;
        }
        return result;
    }

    /**
     * Returns a column given its name.
     * Implementation is naive so this should be overridden if
     * this method is to be called often. This method is not
     * in the <code>TableModel</code> interface and is not used by the
     * <code>JTable</code>.
     *
     * @param columnName string containing name of column to be located
     * @return the column with <code>columnName</code>, or -1 if not found
     */
    public int findColumn(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equals(getColumnName(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     *  @param columnIndex  the column being queried
     *  @return the Object.class
     */
    public Class getColumnClass(int columnIndex) {
        return Object.class;
    }

    /**
     *  Returns false.  This is the default implementation for all cells.
     *
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return false
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * This empty implementation is provided so that users don't have to
     * provide their own implementation if their table is not editable.
     */
    public void setValueAt(Object value, int row, int column) {
    }

    /**
     * Forwards the specified event to all TableModelListeners that
     * registered themselves as listeners for this TableModel.
     */
    public void fireTableChanged(TableModelEvent evt) {
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.size() - 1; i >= 0; i--) {
            TableModelListener l = (TableModelListener) listeners.get(i);
            l.tableChanged(evt);
        }
    }

    /**
     * Notifies all listeners that the value at [row, column] has been
     * updated.
     */
    public void fireTableCellUpdated(int row, int column) {
        TableModelEvent evt = new TableModelEvent(this, row, row, column);
        fireTableChanged(evt);
    }

    /**
     * Notifies all listeners that all cell values in the table may have
     * changed.
     */
    public void fireTableDataChanged() {
        TableModelEvent evt = new TableModelEvent(this, 0, getRowCount() - 1, 
                TableModelEvent.ALL_COLUMNS);
        fireTableChanged(evt);
    }

    /**
     * Notifies all listeners that rows in the range [firstRow, lastRow],
     * inclusive, have been deleted.
     */
    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        TableModelEvent evt = new TableModelEvent(this, firstRow, lastRow, 
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.DELETE);
        fireTableChanged(evt);
    }

    /**
     * Notifies all listeners that rows in the range [firstRow, lastRow],
     * inclusive, have been inserted.
     */
    public void fireTableRowsInserted(int firstRow, int lastRow) {
        TableModelEvent evt = new TableModelEvent(this, firstRow, lastRow, 
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT);
        fireTableChanged(evt);
    }
}
