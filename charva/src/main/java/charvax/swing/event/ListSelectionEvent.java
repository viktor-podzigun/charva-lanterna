/* class ListSelectionEvent
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


/**
 * An event that characterizes a change in the current selection.
 * ListEventListeners will generally query the source of the event directly to
 * find out the status of each potentially changed row.
 */
public class ListSelectionEvent extends EventObject {
    
    private static final long serialVersionUID = -4021516031547003889L;

    private int     firstIndex;
    private int     lastIndex;

    /**
     * Construct a ListSelectionEvent
     *
     * @param source       the object that initiated this event (usually a
     *                     DefaultListSelectionModel)
     * @param firstIndex   the index of the first row whose selection status has
     *                     changed
     * @param lastIndex    the index of the last row whose selection status has
     *                     changed
     * @param isAdjusting  not used in CHARVA
     */
    public ListSelectionEvent(Object source, int firstIndex, int lastIndex, 
            boolean isAdjusting) {
        
        super(source);
        
        this.firstIndex = firstIndex;
        this.lastIndex  = lastIndex;
    }

    /**
     * Get the index of the first row that changed
     */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
     * Get the index of the last row that changed
     */
    public int getLastIndex() {
        return lastIndex;
    }

    /**
     * Returns a string that displays and identifies this
     * object's properties.
     *
     * @return a String representation of this object
     */
    public String toString() {
        return getClass().getName() 
                + "[firstIndex=" + firstIndex 
                + ",lastIndex=" + lastIndex 
                + "] on source=" + getSource();
    }
}
