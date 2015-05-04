/* class DefaultTableModel
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
import charvax.swing.SwingUtilities;


/**
 * This is an implementation of the TableModel interface that uses an
 * ArrayList of ArrayLists to store the cell values.
 */
public class DefaultTableModel extends AbstractTableModel {
    
    private int         rows;
    private int         columns;

    /**
     * A vector of vectors of data values. Each vector in the dataVector
     * represents a row of data.
     */
    private ArrayList   dataVector;

    /* A vector of column names
     */
    private ArrayList   columnNames;

    /**
     * A Vector of column widths
     */
    private ArrayList   columnWidths;
    
    
    /**
     * Constructs a DefaultTableModel with the specified number of
     * rows and columns, and with cell values of null.
     */
    public DefaultTableModel(int rows, int columns) {
        this.rows    = rows;
        this.columns = columns;

        // Create empty arrays
        setDataVector(rows, columns);
    }

    /**
     * Constructs a DefaultTableModel and initialises the table by passing
     * data and columnNames to the setDataVector method.
     */
    public DefaultTableModel(Object[][] data, Object[] columnNames) {
        this.rows    = data.length;
        this.columns = columnNames.length;
        setDataVector(data, columnNames);
    }

    /**
     * Get the number of columns in the model.
     */
    public int getColumnCount() {
        return columns;
    }

    /**
     * Get the name of the specified column.
     */
    public String getColumnName(int column) {
        if (columnNames == null) {
            char heading = (char) (0x41 + column);
            return String.valueOf(heading);
        }
        
        return columnNames.get(column).toString();
    }

    /**
     * Get the width of the specified column.
     */
    public int getColumnWidth(int column) {
        return ((Integer) columnWidths.get(column)).intValue();
    }

    /**
     * Get the number of rows in the model.
     */
    public int getRowCount() {
        return rows;
    }

    /**
     * Returns an attribute value for the cell at (rowIndex, columnIndex)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataVector == null)
            return null;

        ArrayList rowVector = (ArrayList) dataVector.get(rowIndex);
        return rowVector.get(columnIndex);
    }

    /**
     * Sets the attribute value for the cell at position (row, column).
     */
    public void setValueAt(Object value, int row, int column) {
        ArrayList rowVector = (ArrayList) dataVector.get(row);
        rowVector.set(column, value);

        /* Recalculate the column width for the affected column.
         */
        int columnwidth = 3;	    // default width
        if (columnNames != null) {
            Object header = columnNames.get(column);
            columnwidth = header.toString().length() + 2;
        }
        
        for (int j = 0; j < rows; j++) {
            Object data = getValueAt(j, column);
            if (data != null) {
                int width = data.toString().length();
                if (width > columnwidth)
                    columnwidth = width;
            }
        }
        
        columnWidths.set(column, SwingUtilities.valueOf(columnwidth));
        fireTableCellUpdated(row, column);
    }

    /**
     * Replaces the values in the dataVector instance variable with the
     * values in the data array. The first index is the row index, the
     * second index is the column index.
     * The columnNames array supplies the new column names.
     */
    public void setDataVector(Object[][] data, Object[] columnNames) {
        rows       = data.length;
        dataVector = new ArrayList(rows);
        
        for (int i = 0; i < rows; i++) {
            columns = data[i].length;
            ArrayList rowVector = new ArrayList(columns);
            dataVector.add(rowVector);
            for (int j = 0; j < columns; j++)
                rowVector.add(data[i][j]);
        }

        // set up the column-name  and column-width vectors

        this.columnNames  = new ArrayList(columns);
        this.columnWidths = new ArrayList(columns);
        for (int i = 0; i < columns; i++) {
            this.columnNames.add(columnNames[i]);

            int columnwidth = columnNames[i].toString().length() + 2;
            for (int j = 0; j < rows; j++) {
                int width = getValueAt(j, i).toString().length();
                if (width > columnwidth)
                    columnwidth = width;
            }
            
            columnWidths.add(SwingUtilities.valueOf(columnwidth));
        }
    }

    /**
     * Set up an empty data vector with the specified number of rows
     * and columns.
     */
    public void setDataVector(int rows, int columns) {
        dataVector = new ArrayList(rows);
        for (int i = 0; i < rows; i++) {
            ArrayList rowVector = new ArrayList(columns);
            rowVector.ensureCapacity(columns);
            dataVector.add(rowVector);
        }

        columnWidths = new ArrayList(columns);
        for (int i = 0; i < columns; i++) {
            int width;
            if (columnNames == null)
                width = 3;
            else
                width = columnNames.get(i).toString().length();

            columnWidths.add(SwingUtilities.valueOf(width));
        }
    }
}
