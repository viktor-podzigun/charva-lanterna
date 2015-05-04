/* class TreeSelectionEvent
 *
 * Copyright (C) 2003  R M Pitman
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

import java.util.EventObject;
import charvax.swing.tree.TreePath;


/**
 * An event that characterizes a change in the current selection.
 * TreeSelectionListeners will generally query the source of the event
 * directly to find out the status of each potentially changed path.
 */
public class TreeSelectionEvent extends EventObject {

    private static final long serialVersionUID = 2288629009299817274L;
    
    private boolean     isNew;
    private TreePath    path;
    
    
    /**
     * Represents a change in the selection of a TreeSelectionModel.
     *
     * @param source    the object that initiated this event (usually a
     *                  DefaultTreeSelectionModel).
     * @param path      the path that has changed in the selection.
     * @param isNew     whether or not the path is new to the selection;
     *                  false means path was removed from the selection
     * @param oldLeadSelectionPath  not used
     * @param newLeadSelectionPath  not used
     */
    public TreeSelectionEvent(Object source, TreePath path, boolean isNew, 
            TreePath oldLeadSelectionPath, TreePath newLeadSelectionPath) {
        
        super(source);
        
        this.path  = path;
        this.isNew = isNew;
    }

    /**
     * Returns the path that has been added to or removed from the selection.
     */
    public TreePath getPath() {
        return path;
    }

    /**
     * Returns true if the path element has been added to the
     * selection. A return value of false means the path has been
     * removed from the selection.
     */
    public boolean isAddedPath() {
        return isNew;
    }

    public String toString() {
        return getClass().getName() 
                + "[path=" + path 
                + ",isNew=" + isNew 
                + "] on source=" + getSource();
    }
}
