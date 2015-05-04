/* class DefaultListModel
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


/**
 * Default list model implementation.
 * <p>
 * It stores all data in the ArrayList container.
 */
public class DefaultListModel extends AbstractListModel implements ListModel {

    private ArrayList list = new ArrayList();
    
    
    /**
     * Default constructor
     */
    public DefaultListModel() {
        super();
    }

    /**
     * Returns the value at the specified index
     */
    public Object getElementAt(int index) {
        return list.get(index);
    }

    /**
     * Returns the length of the list
     */
    public int getSize() {
        return list.size();
    }

    /**
     * Clear the contents of the list
     */
    public void clear() {
        int size = list.size();
        list.clear();
        super.fireIntervalRemoved(this, 0, size);
    }

    /**
     * Returns true if the specified element is in the list
     */
    public boolean contains(Object elem) {
        return list.contains(elem);
    }

    /**
     * Inserts the specified object at the specified position in this list
     */
    public void add(int index, Object elem) {
        list.add(index, elem);
        super.fireIntervalAdded(this, index, index);
    }

    /**
     * Add the specified object to the end of the list
     */
    public void addElement(Object obj) {
        list.add(obj);
        super.fireIntervalAdded(this, list.size(), list.size());
    }

    /**
     * Deletes the component at the specified index
     */
    public void removeElementAt(int index) {
        list.remove(index);
        super.fireIntervalRemoved(this, index, index);
    }

    /**
     * Removes all elements from this list and sets its size to zero
     */
    public void removeAllElements() {
        int size = list.size();
        list.clear();
        super.fireIntervalRemoved(this, 0, size);
    }

    /**
     * Returns the index of the first occurrence of the specified object
     */
    public int indexOf(Object elem) {
        return list.indexOf(elem);
    }
}
