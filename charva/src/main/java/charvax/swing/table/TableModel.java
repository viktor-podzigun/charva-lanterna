/* interface TableModel
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

import charvax.swing.event.TableModelEvent;
import charvax.swing.event.TableModelListener;


/**
 * The TableModel interface specifies the methods that the JTable class will use
 * to interrogate a tabular data model.
 */
public interface TableModel {
    
    /**
     * Adds a listener that will be notified each time the data model changes
     */
    public void addTableModelListener(TableModelListener l);

    /**
     * Removes the specified listener from the list of listeners
     */
    public void removeTableModelListener(TableModelListener l);

    /**
     * Get the number of columns in the model
     */
    public int getColumnCount();

    /**
     * Get the name of the specified column
     */
    public String getColumnName(int column);

    /**
     * Get the number of rows in the model
     */
    public int getRowCount();

    /**
     * Returns the most specific superclass for all the cell values 
     * in the column.  This is used by the <code>JTable</code> to set up a 
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass(int columnIndex);

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param   rowIndex    the row whose value to be queried
     * @param   columnIndex the column whose value to be queried
     * 
     * @return  true if the cell is editable
     * @see #setValueAt
     */
    public boolean isCellEditable(int rowIndex, int columnIndex);

    /**
     * Returns an attribute value for the cell at (rowIndex, columnIndex)
     */
    public Object getValueAt(int rowIndex, int columnIndex);

    /**
     * Sets the attribute value for the cell at position (row, column)
     */
    public void setValueAt(Object value, int row, int column);

    public void fireTableChanged(TableModelEvent e);
}
