/* class LayoutManager
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

package charva.awt;


/**
 * Defines the interface for classes that know how to lay out Containers.
 */
public interface LayoutManager {
    
    /**
     * If the layout manager uses a per-component string,
     * adds the component <code>comp</code> to the layout,
     * associating it with the string specified by <code>name</code>.
     * 
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp);

    /**
     * Removes the specified component from the layout
     * 
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp);
    
    /**
     * Calculates the minimum size dimensions for the specified 
     * container, given the components it contains
     * 
     * @param parent the component to be laid out
     */
    public Dimension minimumLayoutSize(Container parent);

    /**
     * Lays out the specified container
     * 
     * @param parent the container to be laid out 
     */
    public void layoutContainer(Container parent);
}
