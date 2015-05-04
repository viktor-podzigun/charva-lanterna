/* class AbstractListModel
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

package charvax.swing;

import java.util.ArrayList;
import java.util.Iterator;
import charvax.swing.event.ListDataEvent;
import charvax.swing.event.ListDataListener;


/**
 * The abstract base class for classes that implement the ListModel interface
 */
public abstract class AbstractListModel implements ListModel {

    /**
     * A list of ListDataListeners registered for this object
     */
    protected ArrayList listeners;

    
    /**
     * Register an ListDataListener object.
     */
    public void addListDataListener(ListDataListener listener) {
        if (listeners == null) {
            listeners = new ArrayList();
        }

        listeners.add(listener);
    }

    /**
     * Remove the specified ListDataListener from the list of listeners
     * that will be notified
     */
    public void removeListDataListener(ListDataListener listener) {
        if (listeners == null) {
            return;
        }

        listeners.remove(listener);
    }

    /**
     * Subclasses of AbstractListModel must call this method <b>after</b> the
     * contents of one or more elements of the list has changed
     */
    public void fireContentsChanged(Object source, int index0, int index1) {
        if (listeners == null) {
            return;
        }

        ListDataEvent event = new ListDataEvent(this,
                ListDataEvent.CONTENTS_CHANGED, index0, index1);

        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            ListDataListener listener = (ListDataListener) iter.next();
            listener.contentsChanged(event);
        }
    }

    /**
     * Subclasses of AbstractListModel must call this method <b>after</b>
     * one or more elements of the list has been removed from the model
     */
    public void fireIntervalRemoved(Object source, int index0, int index1) {
        if (listeners == null) {
            return;
        }

        ListDataEvent event = new ListDataEvent(this,
                ListDataEvent.INTERVAL_REMOVED, index0, index1);

        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            ListDataListener listener = (ListDataListener) iter.next();
            listener.intervalRemoved(event);
        }
    }

    /**
     * Subclasses of AbstractListModel must call this method <b>after</b>
     * one or more elements of the list has been added to the model
     */
    public void fireIntervalAdded(Object source, int index0, int index1) {
        if (listeners == null) {
            return;
        }

        ListDataEvent event = new ListDataEvent(this,
                ListDataEvent.INTERVAL_ADDED, index0, index1);

        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            ListDataListener listener = (ListDataListener) iter.next();
            listener.intervalAdded(event);
        }
    }
}
