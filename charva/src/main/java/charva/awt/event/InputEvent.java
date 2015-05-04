/* class InputEvent
 *
 * Copyright (C) 2001-2003  R M Pitman
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
 * This is the superclass of KeyEvent and MouseEvent.
 */
public abstract class InputEvent extends AWTEvent {

    private static final long serialVersionUID = 1264986458960433681L;

    
    /** The shift key modifier constant                 */
    public static final int SHIFT_MASK          = 1 << 0;

    /** The control key modifier constant               */
    public static final int CTRL_MASK           = 1 << 1;

    /** The alt key modifier constant                   */
    public static final int ALT_MASK            = 1 << 2;

    /** The mouse button1 extended modifier constant.   */
    public static final int BUTTON1_DOWN_MASK   = 1 << 3;

    /** The mouse button2 extended modifier constant.   */
    public static final int BUTTON2_DOWN_MASK   = 1 << 4;

    /** The mouse button3 extended modifier constant.   */
    public static final int BUTTON3_DOWN_MASK   = 1 << 5;

    
    /**
     * The state of the modifier key at the time the input event was fired.
     */
    protected final int     modifiers;
    
    private boolean         consumed;
    
    
    public InputEvent(Component source, int id, int modifiers) {
        super(source, id);
        
        this.modifiers = modifiers;
    }

    /**
     * Returns whether or not the Shift modifier is down on this event.
     */
    public boolean isShiftDown() {
        return (modifiers & SHIFT_MASK) != 0;
    }

    /**
     * Returns whether or not the Control modifier is down on this event.
     */
    public boolean isControlDown() {
        return (modifiers & CTRL_MASK) != 0;
    }

    /**
     * Returns whether or not the Alt modifier is down on this event.
     */
    public boolean isAltDown() {
        return (modifiers & ALT_MASK) != 0;
    }

    /**
     * Returns the modifiers flag for this event.
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * Provides a way to flag the event as having been consumed,
     * so that it never reaches its destination component.
     */
    public void consume() {
        consumed = true;
    }

    /**
     * Tests if this event was already processed
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Returns a String describing the modifier key(s), such as "Shift",
     * or "Ctrl+Shift".
     *
     * @return string  a text description of the combination of modifier
     *                 keys that were held down during the event
     */
    public static String getKeyModifiersText(int modifiers) {
        StringBuffer buf = new StringBuffer();
        if ((modifiers & InputEvent.CTRL_MASK) != 0)
            buf.append("Ctrl+");
        if ((modifiers & InputEvent.ALT_MASK) != 0)
            buf.append("Alt+");
        if ((modifiers & InputEvent.SHIFT_MASK) != 0)
            buf.append("Shift+");
        
        if (buf.length() > 0)
            buf.setLength(buf.length()-1); // remove trailing '+'
        
        return buf.toString();
    }
}
