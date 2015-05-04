/* class JTableHeader
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

import charva.awt.ColorPair;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charvax.swing.JComponent;
import charvax.swing.JTable;


/**
 * The JTableHeader class is used by the ScrollPane for drawing the
 * column headers of a table. It shares the TableModel of its companion
 * JTable object
 */
public class JTableHeader extends JComponent {
    
    /** 
     * The table for which this object is the header;
     * the default is <code>null</code>.
     */
    protected JTable        table;


    public JTableHeader() {
    }

    /**
     * Sets the table associated with this header
     * 
     * @param table  the new table
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * Returns the table associated with this header
     * 
     * @return the <code>table</code> property
     */
    public JTable getTable() {
        return table;
    }

    public boolean isFocusTraversable() {
        return false;
    }

    public void requestFocus() {
    }

    public void paint(Graphics g) {
        // get the absolute origin of this component
        TableModel model     = table.getModel();
        final int  columns   = model.getColumnCount();
        ColorPair  colorpair = getColor();

        // start by blanking out the table area and drawing the box
        // around the table
        g.setColor(colorpair);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawRect(0, 0, getWidth(), getHeight());

        // fill in the table headings
        int x = 1;
        for (int i = 0; i < columns; i++) {
            String name = model.getColumnName(i);
            g.drawChar(' ', x, 0);
            g.drawString(name, x + 1, 0);
            g.drawChar(' ', x + 1 + name.length(), 0);
            x += getColumnWidth(i) + 1;
        }

        // now draw the vertical lines that divide the columns
        x = getColumnWidth(0) + 1;
        for (int i = 0; i < columns - 1; i++) {
            g.drawChar(GraphicsConstants.VS_TTEE, x, 0); // top tee
            x += getColumnWidth(i + 1) + 1;
        }
    }

    /**
     * We pretend that the table header is two rows in height so that the
     * box gets drawn correctly.
     */
    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public Dimension getMinimumSize() {
        return getSize();
    }

    public int getWidth() {
        int columns = table.getModel().getColumnCount();
        int width = 1;
        for (int i = 0; i < columns; i++)
            width += getColumnWidth(i) + 1;
        
        return width;
    }

    public int getHeight() {
        return 2;
    }

    private int getColumnWidth(int column) {
        TableModel model = table.getModel();
        
        // calculate the column width for the specified column
        int columnwidth = model.getColumnName(column).length() + 2;

        for (int j = 0; j < model.getRowCount(); j++) {
            Object value = model.getValueAt(j, column);
            if (value != null) {
                int width = value.toString().length();
                if (width > columnwidth)
                    columnwidth = width;
            }
        }
        
        return columnwidth;
    }
}
