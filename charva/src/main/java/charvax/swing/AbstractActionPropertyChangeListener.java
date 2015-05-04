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

package charvax.swing;

import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


/**
 * A package-private PropertyChangeListener which listens for property changes
 * on an Action and updates the properties of an ActionEvent source.
 * 
 * @see AbstractButton
 */

abstract class AbstractActionPropertyChangeListener implements
        PropertyChangeListener {
    
    private static ReferenceQueue   queue;
    
    private WeakReference           target;
    private Action                  action;
    

    AbstractActionPropertyChangeListener(JComponent c, Action a) {
        super();
        setTarget(c);
        this.action = a;
    }

    public void setTarget(JComponent c) {
        if (queue == null)
            queue = new ReferenceQueue();
        
        // Check to see whether any old buttons have
        // been enqueued for GC. If so, look up their
        // PCL instance and remove it from its Action.
        OwnedWeakReference r;
        while ((r = (OwnedWeakReference) queue.poll()) != null) {
            AbstractActionPropertyChangeListener oldPCL = 
                (AbstractActionPropertyChangeListener) r.getOwner();
            
            Action oldAction = oldPCL.getAction();
            if (oldAction != null)
                oldAction.removePropertyChangeListener(oldPCL);
        }
        
        target = new OwnedWeakReference(c, queue, this);
    }

    public JComponent getTarget() {
        return (JComponent) this.target.get();
    }

    public Action getAction() {
        return action;
    }

    private static class OwnedWeakReference extends WeakReference {
        
        private Object owner;

        OwnedWeakReference(Object target, ReferenceQueue queue, Object owner) {
            super(target, queue);
            this.owner = owner;
        }

        public Object getOwner() {
            return owner;
        }
    }
}
