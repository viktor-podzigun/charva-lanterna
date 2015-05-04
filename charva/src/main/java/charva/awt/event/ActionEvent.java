/* class ActionEvent
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


/**
 * An event which indicates that a component-defined action (typically
 * the pressing of a button) occurred.
 */
public class ActionEvent extends AWTEvent {
    
    private static final long serialVersionUID  = -6035843858574053930L;
    
    /**
     * The first number in the range of ids used for action events.
     */
    public static final int ACTION_FIRST        = 1001;

    /**
     * The last number in the range of ids used for action events.
     */
    public static final int ACTION_LAST         = 1001;

    /**
     * This event id indicates that a meaningful action occured.
     */
    public static final int ACTION_PERFORMED    = ACTION_FIRST;

    /** The shift key modifier constant     */
    public static final int SHIFT_MASK          = InputEvent.SHIFT_MASK;

    /** The control key modifier constant   */
    public static final int CTRL_MASK           = InputEvent.CTRL_MASK;

    /** The alt key modifier constant       */
    public static final int ALT_MASK            = InputEvent.ALT_MASK;

    
    /**
     * The nonlocalized string that gives more details of what actually caused
     * the event. This information is very specific to the component that fired
     * it.
     */
    private String  actionCommand;
    
    private int     modifiers;

    
    public ActionEvent(Object source, String command) {
        this(source, command, 0);
    }

    public ActionEvent(Object source, String command, int modifiers) {
    	super(source, ACTION_PERFORMED);
    	
    	this.actionCommand = command;
    	this.modifiers     = modifiers;
    }

    /**
     * Returns the command string associated with this action. This string
     * allows a component to specify one of several commands, depending on
     * its state.
     */
    public String getActionCommand() {
        return actionCommand;
    }

    public int getModifiers() {
        return modifiers;
    }

    /**
     * Returns a parameter string identifying this action event.
     * This method is useful for event-logging and for debugging.
     * 
     * @return a string identifying the event and its associated command 
     */
    public String paramString() {
        String typeStr;
        switch (id) {
        case ACTION_PERFORMED:
            typeStr = "ACTION_PERFORMED";
            break;

        default:
            typeStr = "unknown type";
        }

        return typeStr + ",cmd=" + actionCommand;
    }
}
