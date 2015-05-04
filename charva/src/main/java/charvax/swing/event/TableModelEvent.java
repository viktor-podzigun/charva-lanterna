/* class TableModelEvent
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

package charvax.swing.event;

import java.util.EventObject;
import charvax.swing.table.TableModel;


/**
 * This event is fired when the data in a table changes.
 */
public class TableModelEvent extends EventObject {

    private static final long serialVersionUID = -2531870181440642983L;

    /** Identifies the removal of rows or columns.      */
    public static final int DELETE      = 1;
    
    /** Identifies the addtion of new rows or columns.  */
    public static final int INSERT      = 2;
    
    /** Identifies a change to existing data.           */
    public static final int UPDATE      = 3;

    
    /** Identifies the header row.                      */
    public static final int HEADER_ROW  = -1;

    /** Specifies all columns in a row or rows.         */
    public static final int ALL_COLUMNS = -1;
    
    
    private int     firstRow;
    private int     lastRow;
    private int     column;
    private int     type;
    
    
    /**
     * All row data has changed; listeners should discard all state
     * and re-query the TableModel.
     */
    public TableModelEvent(TableModel source) {
        this(source, 0, source.getRowCount() - 1, ALL_COLUMNS, UPDATE);
    }

    /**
     * This row of data has been updated.
     */
    public TableModelEvent(TableModel source, int row) {
        this(source, row, row, ALL_COLUMNS, UPDATE);
    }

    /**
     * The data in rows [firstRow, lastRow] have been updated.
     */
    public TableModelEvent(TableModel source, int firstRow, int lastRow) {
        this(source, firstRow, lastRow, ALL_COLUMNS, UPDATE);
    }

    /**
     * The cells in the specified column in rows [firstRow, lastRow]
     * have been updated.
     */
    public TableModelEvent(TableModel source, int firstRow, int lastRow, 
            int column) {
        
        this(source, firstRow, lastRow, column, UPDATE);
    }

    public TableModelEvent(TableModel source, int firstRow, int lastRow, 
            int column, int type) {
        
        super(source);
        
        this.firstRow = firstRow;
        this.lastRow  = lastRow;
        this.column   = column;
        this.type     = type;
    }

    /**
     * Get the index of the first row that changed
     */
    public int getFirstRow() {
        return firstRow;
    }

    /**
     * Get the index of the last row that changed
     */
    public int getLastRow() {
        return lastRow;
    }

    /**
     *  Returns the column for the event.  If the return
     *  value is ALL_COLUMNS; it means every column in the specified
     *  rows changed.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the type of event - one of INSERT, UPDATE or DELETE
     */
    public int getType() {
        return type;
    }

    public String toString() {
        String typeStr;
        switch (type) {
        case DELETE:    typeStr = "DELETE"; break;
        case INSERT:    typeStr = "INSERT"; break;
        case UPDATE:    typeStr = "UPDATE"; break;
        
        default:
            typeStr = "unknown type";
        }
        
        return getClass().getName() 
                + "[" + typeStr 
                + ",firstRow=" + firstRow 
                + ",lastRow=" + lastRow
                + ",column=" + column
                + "] on source=" + getSource();
    }
}
