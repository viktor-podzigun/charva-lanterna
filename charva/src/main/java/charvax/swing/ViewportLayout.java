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

import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.LayoutManager;
import charva.awt.Point;


/**
 * The default layout manager for <code>JViewport</code>.
 * <code>ViewportLayout</code> defines a policy for layout that should be
 * useful for most applications. The viewport makes its view the same size as
 * the viewport, however it will not make the view smaller than its minimum
 * size. As the viewport grows the view is kept bottom justified until the
 * entire view is visible, subsequently the view is kept top justified.
 */
public class ViewportLayout implements LayoutManager {
    
    /**
     * Adds the specified component to the layout. Not used by this class.
     * 
     * @param name  the name of the component
     * @param c  the the component to be added
     */
    public void addLayoutComponent(String name, Component c) {
    }

    /**
     * Removes the specified component from the layout. Not used by this class.
     * 
     * @param c  the component to remove
     */
    public void removeLayoutComponent(Component c) {
    }

    /**
     * Returns the minimum dimensions needed to layout the components contained
     * in the specified target container.
     * 
     * @param parent  the component which needs to be laid out
     * @return a <code>Dimension</code> object containing the minimum
     *         dimensions
     */
    public Dimension minimumLayoutSize(Container parent) {
        Component view = ((JViewport) parent).getView();
        if (view == null)
            return new Dimension(1, 1);
        
        if (view instanceof Scrollable)
            return ((Scrollable) view).getPreferredScrollableViewportSize();
        
        return view.getPreferredSize();
    }

    public void layoutContainer(Container parent) {
        JViewport  vp   = (JViewport) parent;
        Component  view = vp.getView();
        Scrollable scrollableView = null;

        if (view == null) {
            return;
        }
        
        if (view instanceof Scrollable) {
            scrollableView = (Scrollable) view;
        }

        /*
         * All of the dimensions below are in view coordinates, except vpSize
         * which we're converting.
         */

        //Insets insets = vp.getInsets();
        Dimension viewPrefSize = view.getPreferredSize();
        Dimension vpSize = vp.getSize();
        Dimension extentSize = vpSize;//vp.toViewCoordinates(vpSize);
        Dimension viewSize = new Dimension(viewPrefSize);

        if (scrollableView != null) {
            if (scrollableView.getScrollableTracksViewportWidth()) {
                viewSize.width = vpSize.width;
            }
            if (scrollableView.getScrollableTracksViewportHeight()) {
                viewSize.height = vpSize.height;
            }
        }

        Point viewPosition = vp.getViewPosition();
        boolean isLeftToRight = true;

        /*
         * If the new viewport size would leave empty space to the right of the
         * view, right justify the view or left justify the view when the width
         * of the view is smaller than the container.
         */
        if (scrollableView == null || vp.getParent() == null
                || isLeftToRight/*vp.getParent().getComponentOrientation().isLeftToRight()*/) {
            
            if ((viewPosition.x + extentSize.width) > viewSize.width) {
                viewPosition.x = Math.max(0, viewSize.width - extentSize.width);
            }
        } else {
            if (extentSize.width > viewSize.width) {
                viewPosition.x = viewSize.width - extentSize.width;
            } else {
                viewPosition.x = Math.max(0, Math.min(viewSize.width
                        - extentSize.width, viewPosition.x));
            }
        }

        /*
         * If the new viewport size would leave empty space below the view,
         * bottom justify the view or top justify the view when the height of
         * the view is smaller than the container.
         */
        if ((viewPosition.y + extentSize.height) > viewSize.height) {
            viewPosition.y = Math.max(0, viewSize.height - extentSize.height);
        }

        /*
         * If we haven't been advised about how the viewports size should change
         * wrt to the viewport, i.e. if the view isn't an instance of
         * Scrollable, then adjust the views size as follows.
         * 
         * If the origin of the view is showing and the viewport is bigger than
         * the views preferred size, then make the view the same size as the
         * viewport.
         */
        if (scrollableView == null) {
            if ((viewPosition.x == 0) && (vpSize.width > viewPrefSize.width)) {
                viewSize.width = vpSize.width;
            }
            if ((viewPosition.y == 0) && (vpSize.height > viewPrefSize.height)) {
                viewSize.height = vpSize.height;
            }
        }
        vp.setViewPosition(viewPosition);
        vp.setViewSize(viewSize);
    }
}
