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

package charva.awt.event;


/**
 * An abstract adapter class for receiving keyboard focus events. The methods in
 * this class are empty. This class exists as convenience for creating listener
 * objects.
 * <p>
 * Extend this class to create a FocusEvent listener and override the methods
 * for the events of interest. (If you implement the FocusListener interface,
 * you have to define all of the methods in it. This abstract class defines null
 * methods for them all, so you can only have to define methods for events you
 * care about.)
 * <p>
 * Create a listener object using the extended class and then register it with a
 * component using the component's addFocusListener method. When the component
 * gains or loses the keyboard focus, the relevant method in the listener object
 * is invoked, and the FocusEvent is passed to it.
 */
public abstract class FocusAdapter implements FocusListener {

    /**
     * Invoked when a component gains the keyboard focus
     * 
     * @param fe  current focus event object
     */
    public void focusGained(FocusEvent fe) {

    }

    /**
     * Invoked when a component loses the keyboard focus
     * 
     * @param fe  current focus event object
     */
    public void focusLost(FocusEvent fe) {

    }
}
