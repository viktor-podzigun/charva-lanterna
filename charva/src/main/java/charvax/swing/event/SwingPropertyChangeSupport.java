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

package charvax.swing.event;

import java.beans.PropertyChangeSupport;


/**
 * This subclass of java.beans.PropertyChangeSupport is identical
 * in functionality -- it sacrifices thread-safety (not a Swing
 * concern) for reduce memory consumption, which helps performance
 * (both big Swing concerns).  Most of the overridden methods are
 * only necessary because all of PropertyChangeSupport's instance
 * data is private, without accessor methods.
 */
public final class SwingPropertyChangeSupport extends PropertyChangeSupport {

    static final long serialVersionUID = 7162625831330845068L;
    
    
    /**
     * Constructs a SwingPropertyChangeSupport object.
     *
     * @param sourceBean  The bean to be given as the source for any events.
     */
    public SwingPropertyChangeSupport(Object sourceBean) {
        super(sourceBean);
    }
}
