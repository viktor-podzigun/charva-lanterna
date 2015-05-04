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


/**
 * A <code>ComponentInputMap</code> is an <code>InputMap</code> associated
 * with a particular <code>JComponent</code>. The component is automatically
 * notified whenever the <code>ComponentInputMap</code> changes.
 * <code>ComponentInputMap</code>s are used for
 * <code>WHEN_IN_FOCUSED_WINDOW</code> bindings.
 */
public class ComponentInputMap extends InputMap {
    
    /** Component binding is created for. */
    private JComponent component;

    
    /**
     * Creates a <code>ComponentInputMap</code> associated with the specified
     * component.
     * 
     * @param component  a non-null <code>JComponent</code>
     * @throws IllegalArgumentException if <code>component</code> is null
     */
    public ComponentInputMap(JComponent component) {
        this.component = component;
        if (component == null) {
            throw new IllegalArgumentException(
                    "ComponentInputMaps must be associated with a non-null JComponent");
        }
    }

    /**
     * Sets the parent, which must be a <code>ComponentInputMap</code>
     * associated with the same component as this <code>ComponentInputMap</code>.
     * 
     * @param map  a <code>ComponentInputMap</code>
     * 
     * @throws IllegalArgumentException
     *             if <code>map</code> is not a <code>ComponentInputMap</code>
     *             or is not associated with the same component
     */
    public void setParent(InputMap map) {
        if (getParent() == map)
            return;
        
        if (map != null && (!(map instanceof ComponentInputMap) 
                || ((ComponentInputMap) map).getComponent() != getComponent())) {
            
            throw new IllegalArgumentException(
                    "ComponentInputMaps must have a parent ComponentInputMap associated with the same component");
        }
        
        super.setParent(map);
        getComponent().componentInputMapChanged(this);
    }

    /**
     * Returns the component the <code>InputMap</code> was created for.
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Adds a binding for <code>keyStroke</code> to <code>actionMapKey</code>.
     * If <code>actionMapKey</code> is null, this removes the current binding
     * for <code>keyStroke</code>.
     */
    public void put(KeyStroke keyStroke, Object actionMapKey) {
        super.put(keyStroke, actionMapKey);
        if (getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }

    /**
     * Removes the binding for <code>key</code> from this object.
     */
    public void remove(KeyStroke key) {
        super.remove(key);
        if (getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }

    /**
     * Removes all the mappings from this object.
     */
    public void clear() {
        int oldSize = size();
        super.clear();
        if (oldSize > 0 && getComponent() != null) {
            getComponent().componentInputMapChanged(this);
        }
    }
}
