/* class ListDataEvent
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
 * An event that characterizes a change in a list.
 */
public class ListDataEvent extends EventObject {
    
    private static final long serialVersionUID = 6379537743436017112L;
    
    public static final int CONTENTS_CHANGED    = 301;
    public static final int INTERVAL_ADDED      = 302;
    public static final int INTERVAL_REMOVED    = 303;
    
    private int     type;
    private int     index0;
    private int     index1;
    
    
    /**
     * Construct a ListDataEvent
     *
     * @param source  the object that initiated this event (usually a
     *                DefaultListSelectionModel)
     * @param type    an int specifying the type of event; must be
     *                CONTENTS_CHANGED (INTERVAL_ADDED or INTERVAL_REMOVED 
     *                are unused in CHARVA).
     * @param index0  an index specifying the bottom of a range
     * @param index1  an index specifying the top of a range
     */
    public ListDataEvent(Object source, int type, int index0, int index1) {
        super(source);
        
        this.type   = type;
        this.index0 = index0;
        this.index1 = index1;
    }

    /**
     * Returns the type of event, which is always CONTENTS_CHANGED
     */
    public int getType() {
        return type;
    }

    /**
     * Get the index of the first row that changed.
     */
    public int getIndex0() {
        return index0;
    }

    /**
     * Get the index of the last row that changed.
     */
    public int getIndex1() {
        return index1;
    }

    /**
     * Returns a string representation of this ListDataEvent. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this ListDataEvent
     */
    public String toString() {
        String typeStr;
        switch (type) {
        case CONTENTS_CHANGED:  typeStr = "CONTENTS_CHANGED";   break;
        case INTERVAL_ADDED:    typeStr = "INTERVAL_ADDED";     break;
        case INTERVAL_REMOVED:  typeStr = "INTERVAL_REMOVED";   break;
        
        default:
            typeStr = "unknown type";
        }
        return getClass().getName() + 
                "[type=" + typeStr +
                ",index0=" + index0 +
                ",index1=" + index1 + "] on source=" + getSource();
    }
}
