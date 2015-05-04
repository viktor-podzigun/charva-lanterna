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

package charvax.swing.table;

import charva.awt.Component;
import charvax.swing.JTable;


/**
 * This interface defines the method required by any object that
 * would like to be a renderer for cells in a <code>JTable</code>.
 */
public interface TableCellRenderer {

    /**
     *  Returns the component used for drawing the cell.  This method is
     *  used to configure the renderer appropriately before drawing.
     *
     * @param   table       the <code>JTable</code> that is asking the 
     *              renderer to draw; can be <code>null</code>
     * @param   value       the value of the cell to be rendered.  It is
     *              up to the specific renderer to interpret
     *              and draw the value.  For example, if
     *              <code>value</code>
     *              is the string "true", it could be rendered as a
     *              string or it could be rendered as a check
     *              box that is checked.  <code>null</code> is a
     *              valid value
     * @param   isSelected  true if the cell is to be rendered with the
     *              selection highlighted; otherwise false
     * @param   hasFocus    if true, render cell appropriately.  For
     *              example, put a special border on the cell, if
     *              the cell can be edited, render in the color used
     *              to indicate editing
     * @param   row         the row index of the cell being drawn.  When
     *              drawing the header, the value of
     *              <code>row</code> is -1
     * @param   column          the column index of the cell being drawn
     */
    Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, 
                        int row, int column);
}
