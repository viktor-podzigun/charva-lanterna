/* class AWTEvent
 *
 * Copyright (C) 2001, 2002  R M Pitman
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

package charva.awt.event;

import charva.awt.Component;


/**
 * This is the base class for all the CHARVA user-interface events.
 * It encapsulates information about the event.
 */
public abstract class AWTEvent extends java.util.EventObject {

    private static final long serialVersionUID = -6898638811949513591L;

    /**
     * The maximum value for reserved AWT event IDs. Programs defining
     * their own event IDs should use IDs greater than this value.
     */
    public final static int RESERVED_ID_MAX = 1200;
    
    protected final int id;

    
    /**
     * Construct an event with the specified source and ID
     *
     * @param source  The component that emitted this event
     * @param id      Identifies the event type
     */
    public AWTEvent(Object source, int id) {
        super(source);
        
        this.id = id;
    }

    public int getID() {
        return id;
    }
    
    /**
     * Returns a string representing the state of this <code>Event</code>.
     * This method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between 
     * implementations. The returned string may be empty but may not be 
     * <code>null</code>.
     * 
     * @return  a string representation of this event
     */
    public String paramString() {
        return "";
    }

    /**
     * Returns a String representation of this object.
     */
    public String toString() {
        String srcName = null;
        if (source instanceof Component)
            srcName = ((Component)source).getName();
        
        return getClass().getName() + "[" + paramString() + "] on " +
                (srcName != null ? srcName : source);
    }

    //
    // We may define other event types later
    //
/*
    public static final int WINDOW_FIRST            = 200;
    public static final int WINDOW_LAST             = 201;
    public static final int WINDOW_CLOSING          = WINDOW_FIRST;
    public static final int WINDOW_OPENED           = 1 + WINDOW_FIRST;
    
    public static final int KEY_FIRST               = 400;
    public static final int KEY_LAST                = 402;
    public static final int KEY_TYPED               = KEY_FIRST;
    public static final int KEY_PRESSED             = 1 + KEY_FIRST;
    public static final int KEY_RELEASED            = 2 + KEY_FIRST;

    public static final int MOUSE_FIRST             = 500;
    public static final int MOUSE_LAST              = 502;
    public static final int MOUSE_CLICKED           = MOUSE_FIRST;
    public static final int MOUSE_PRESSED           = 1 + MOUSE_FIRST;
    public static final int MOUSE_RELEASED          = 2 + MOUSE_FIRST;
    
    public static final int ITEM_FIRST              = 701;
    public static final int ITEM_LAST               = 701;
    public static final int ITEM_STATE_CHANGED      = ITEM_FIRST;
    
    public static final int PAINT_FIRST             = 800;
    public static final int PAINT_LAST              = 801;
    public static final int PAINT                   = PAINT_FIRST;
    
    public static final int PEER_EVENT              = 2;
    
    public static final int ACTION_PERFORMED        = 1001;
    
    public static final int FOCUS_FIRST             = 1004;
    public static final int FOCUS_LAST              = 1005;
    public static final int FOCUS_GAINED            = FOCUS_FIRST;
    public static final int FOCUS_LOST              = 1 + FOCUS_FIRST;
    
    public static final int INVOCATION_FIRST        = 1200;
    public static final int INVOCATION_DEFAULT      = INVOCATION_FIRST;
    public static final int INVOCATION_LAST         = INVOCATION_DEFAULT;
    
*/
}
