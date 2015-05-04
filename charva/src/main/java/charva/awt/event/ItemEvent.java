/* class ItemEvent
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

package charva.awt.event;

import charva.awt.Component;


/**
 * An event which indicates that an item was selected or deselected.
 */
public class ItemEvent extends AWTEvent {
    
    private static final long serialVersionUID = -435090108278430793L;
    
    /**
     * The first number in the range of ids used for item events.
     */
    public static final int ITEM_FIRST          = 701;

    /**
     * The last number in the range of ids used for item events.
     */
    public static final int ITEM_LAST           = 701;

    /** 
     * This event id indicates that an item's state changed.
     */
    public static final int ITEM_STATE_CHANGED  = ITEM_FIRST; 
    
    /**
     * This state-change value indicates that an item was selected.
     */
    public static final int SELECTED            = 1;
    
    /** 
     * This state-change-value indicates that a selected item was deselected.
     */
    public static final int DESELECTED          = 2;

    
    /**
     * <code>stateChange</code> indicates whether the <code>item</code>
     * was selected or deselected.
     *
     * @see #getStateChange()
     */
    private int         stateChange;

    private boolean     consumed;

    /** The item affected by the event. */
    private Object      item;

    
    /**
     * Constructs an ItemEvent object
     * 
     * @param source      the object (such as a List) that originated the event
     * @param item        the object affected by the event.
     * @param stateChange an integer that indicates whether the item was
     *                    selected or deselected
     */
    public ItemEvent(Component source, Object item, int stateChange) {
    	super(source, ITEM_STATE_CHANGED);
    	
    	this.item        = item;
    	this.stateChange = stateChange;
    }

    /** 
     * Provides a way to flag the event as having been consumed,
     * so that it never reaches its destination component.
     */
    public void consume() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Get the item affected by the event.
     */
    public Object getItem() {
        return item;
    }

    /**
     * Returns the type of state change (selected or deselected).
     *
     * @return an integer that indicates whether the item was selected
     *         or deselected
     *
     * @see #SELECTED
     * @see #DESELECTED
     */
    public int getStateChange() {
        return stateChange;
    }

    /**
     * Returns a parameter string identifying this item event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;
        switch(id) {
        case ITEM_STATE_CHANGED:
            typeStr = "ITEM_STATE_CHANGED";
            break;
        default:
            typeStr = "unknown type";
        }

        String stateStr;
        switch(stateChange) {
        case SELECTED:
            stateStr = "SELECTED";
            break;
        case DESELECTED:
            stateStr = "DESELECTED";
            break;
        default:
            stateStr = "unknown type";
        }
        
        return typeStr + ",stateChange=" + stateStr + ",item=" + item;
    }
}
