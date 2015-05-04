/* class DefaultComboBoxModel
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

package charvax.swing;

import java.util.ArrayList;


/**
 * The default model for combo boxes.
 */
public class DefaultComboBoxModel extends AbstractListModel
        implements MutableComboBoxModel {
    
    private ArrayList   list = new ArrayList();

    private Object      selectedItem;
    
    
    /**
     * Default constructor
     */
    public DefaultComboBoxModel() {
        super();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with an array
     * of objects
     */
    public DefaultComboBoxModel(Object[] items) {
        super();
        for (int i = 0; i < items.length; i++)
            list.add(items[i]);
        
        if (getSize() > 0)
            selectedItem = list.get(0);
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
     * Returns true if the specified element is in the list
     */
    public boolean contains(Object elem) {
        return list.contains(elem);
    }

    /**
     * Returns the index of the first occurrence of the specified object
     */
    public int indexOf(Object elem) {
        return list.indexOf(elem);
    }

    /**
     * Returns the selected item
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * Sets the selected item
     */
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }

    /**
     * Add the specified object to the end of the list
     */
    public void addElement(Object obj) {
        list.add(obj);
        super.fireContentsChanged(this, list.size(), list.size());
        if (getSize() == 1)
            selectedItem = obj;
    }

    /**
     * Deletes the component at the specified index
     */
    public void removeElementAt(int index) {
        if (selectedItem == list.get(index)) {
            // if the selected item is being removed, replace it with the
            // item above it, unless the item being removed is first in the
            // list
            if (index == 0)
                selectedItem = (getSize() == 1) ? null : list.get(1);
            else
                selectedItem = list.get(index - 1);
        }

        list.remove(index);
        super.fireIntervalRemoved(this, index, index);
    }

    /**
     * Removes all elements from this list and sets its size to zero
     */
    public void removeAllElements() {
        int size = list.size();
        list.clear();
        selectedItem = null;
        super.fireIntervalRemoved(this, 0, size - 1);
    }


    /**
     * Inserts an item at the specified index
     */
    public void insertElementAt(Object obj, int index) {
        list.add(index, obj);
        super.fireIntervalAdded(this, index, index);
        if (getSize() == 1)
            selectedItem = obj;
    }

    /**
     * Removes the specified object from the model
     */
    public void removeElement(Object obj) {
        int index = list.indexOf(obj);
        if (index >= 0)
            this.removeElementAt(index);
    }
}
