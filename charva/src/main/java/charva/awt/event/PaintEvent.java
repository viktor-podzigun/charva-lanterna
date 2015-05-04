/* class PaintEvent
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
import charva.awt.Rectangle;


/**
 * An event which is used to ensure that painting of components is
 * serialized, i.e that it occurs in the main event-processing loop.
 */
public class PaintEvent extends AWTEvent {
    
    private static final long serialVersionUID = 5526132885512279589L;
    
    /**
     * Marks the first integer id for the range of paint event ids.
     */    
    public static final int PAINT_FIRST     = 800;

    /**
     * Marks the last integer id for the range of paint event ids.
     */
    public static final int PAINT_LAST      = 801;

    /**
     * The paint event type.  
     */
    public static final int PAINT           = PAINT_FIRST;
    
    
    /**
     * This is the rectangle that represents the area on the source
     * component that requires a repaint.
     * This rectangle should be non null.
     *
     * @serial
     * @see charva.awt.Rectangle
     * @see #getUpdateRect()
     */
    private Rectangle   updateRect;

    
    public PaintEvent(Component source, Rectangle rect) {
    	super(source, PAINT);
    	this.updateRect = rect;
    }

    /**
     * Returns the rectangle representing the area that needs to be
     * repainted in response to this event.
     */
    public Rectangle getUpdateRect() {
        return new Rectangle(updateRect);
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
        case PAINT:
            typeStr = "PAINT";
            break;
        default:
            typeStr = "unknown type";
        }
        
        return typeStr + ",updateRect=" + (updateRect != null ? 
                updateRect.toString() : "null");
    }
}
