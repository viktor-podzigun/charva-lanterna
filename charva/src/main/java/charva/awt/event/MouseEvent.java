/* class MouseEvent
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
 * An event which encapsulates information about a mouse-click.
 */
public class MouseEvent extends InputEvent {

    private static final long serialVersionUID = 6745844653289801923L;
    
    public static final int MOUSE_FIRST        = 500;
    public static final int MOUSE_LAST         = 502;
    public static final int MOUSE_CLICKED      = MOUSE_FIRST;
    public static final int MOUSE_PRESSED      = 1 + MOUSE_FIRST;
    public static final int MOUSE_RELEASED     = 2 + MOUSE_FIRST;
    
    public static final int BUTTON1            = 1;
    public static final int BUTTON2            = 2;
    public static final int BUTTON3            = 3;
    
    /**
     * The mouse event's x coordinate.
     * The x value is relative to the component that fired the event.
     *
     * @see #getX()
     */
    protected int   x;
    
    /**
     * The mouse event's y coordinate.
     * The y value is relative to the component that fired the event.
     *
     * @see #getY()
     */
    protected int   y;

    /**
     * Indicates which of the mouse buttons has changed state.
     *
     * The only legal values are the following constants:
     * <code>BUTTON1</code>,
     * <code>BUTTON2</code> or
     * <code>BUTTON3</code>.

     * @see #getButton()
     */
    protected int   button;

    /**
     * Indicates the number of quick consecutive clicks of
     * a mouse button.
     * clickCount will be valid for only three mouse events:
     * <br>
     * <code>MOUSE_CLICKED</code>,
     * <code>MOUSE_PRESSED</code> and
     * <code>MOUSE_RELEASED</code>.
     * For the above, the <code>clickCount</code> will be at least 1. 
     * For all other events the count will be 0.
     *
     * @see #getClickCount()
     */
    protected int   clickCount;
    
    
    /**
     * Constructs a <code>MouseEvent</code> object with the
     * specified source component, type, modifiers, coordinates, 
     * and click count.
     * <p>
     * Note that passing in an invalid <code>id</code> results in
     * unspecified behavior.
     *
     * @param source       the <code>Component</code> that originated the event
     * @param id           the integer that identifies the event
     * @param x            the horizontal x coordinate for the mouse location
     * @param y            the vertical y coordinate for the mouse location
     * @param clickCount   the number of mouse clicks associated with event
     * @param button       which of the mouse buttons has changed state:
     *                      <code>BUTTON1</code>,
     *                      <code>BUTTON2</code> or
     *                      <code>BUTTON3</code>.
     * @param modifiers    the modifier keys down during event (e.g. shift, 
     *                     ctrl, alt)
     * 
     * @exception IllegalArgumentException if if an invalid <code>id</code> or 
     *                     <code>button</code> value is passed in.
     */
    public MouseEvent(Component source, int id, int x, int y, 
                      int clickCount, int button, int modifiers) {

        super(source, id, modifiers);
        
        if (id < MOUSE_FIRST || id > MOUSE_LAST)
            throw new IllegalArgumentException("Invalid id value: " + id);
        if (button < BUTTON1 || button > BUTTON3)
            throw new IllegalArgumentException("Invalid button value: " + button);
        
        this.x          = x;
        this.y          = y;
        this.clickCount = clickCount;
        this.button     = button;
    }

    /**
     * Returns the horizontal x position of the event relative to the 
     * source component.
     *
     * @return x  an integer indicating horizontal position relative to
     *            the component
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertical y position of the event relative to the
     * source component.
     *
     * @return y  an integer indicating vertical position relative to
     *            the component
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the number of mouse clicks associated with this event.
     *
     * @return integer value for the number of clicks
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * Returns which of the mouse buttons has changed state.
     *
     * @returns one of the following constants:
     * <code>BUTTON1</code>,
     * <code>BUTTON2</code> or
     * <code>BUTTON3</code>.
     */
    public int getButton() {
        return button;
    }

    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String str;
        switch (id) {
        case MOUSE_PRESSED:
            str = "MOUSE_PRESSED";
            break;
        case MOUSE_RELEASED:
            str = "MOUSE_RELEASED";
            break;
        case MOUSE_CLICKED:
            str = "MOUSE_CLICKED";
            break;
        default:
            str = "unknown type";
        }

        str += ",(" + x + "," + y + ")" + ",button=" + button;
        if (getModifiers() != 0)
            str += ",modifiers=" + getKeyModifiersText(modifiers);

        return str + ",clickCount=" + clickCount;
    }
}
