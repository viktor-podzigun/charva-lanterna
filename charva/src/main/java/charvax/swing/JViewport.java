/* class JViewport
 *
 * Copyright (C) 2001-2003  R M Pitman
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
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Point;
import charva.awt.Rectangle;
import charvax.swing.border.Border;
import charvax.swing.event.ChangeEvent;
import charvax.swing.event.ChangeListener;
import charvax.swing.event.EventListenerList;


/**
 * The JViewport class provides a scrollable window onto an underlying
 * component, whose size can be greater than the size of the JViewport.
 */
public class JViewport extends JComponent {
    
    protected EventListenerList listenerList = new EventListenerList();
    
    /**
     * True when the viewport dimensions have been determined. 
     * The default is false.
     */
    protected boolean   isViewSizeSet;

    /* Only one <code>ChangeEvent</code> is needed per
     * <code>JViewport</code> instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    private ChangeEvent changeEvent;
    
    
    /**
     * Constructs a JViewport
     */
    public JViewport() {
        setLayout(new ViewportLayout());
    }

    /**
     * Scrolls the view so that <code>Rectangle</code> within the view becomes
     * visible.
     * <p>
     * This attempts to validate the view before scrolling if the view is
     * currently not valid - <code>isValid</code> returns false. To avoid
     * excessive validation when the containment hierarchy is being created this
     * will not validate if one of the ancestors does not have a peer, or there
     * is no validate root ancestor, or one of the ancestors is not a
     * <code>Window</code>.
     * <p>
     * Note that this method will not scroll outside of the valid viewport; for
     * example, if <code>contentRect</code> is larger than the viewport,
     * scrolling will be confined to the viewport's bounds.
     * 
     * @param contentRect  the <code>Rectangle</code> to display
     * @see charva.awt.Component#isValid
     */
    public void scrollRectToVisible(Rectangle contentRect) {
        Component view = getView();
        if (view == null) {
            return;
        }

        if (!view.isValid()) {
            // If the view is not valid, validate. scrollRectToVisible
            // may fail if the view is not valid first, contentRect
            // could be bigger than invalid size.
            validateView();
        }
        
        int dx = 0, dy = 0;
        dx = positionAdjustment(getWidth(), contentRect.width, contentRect.x);
        dy = positionAdjustment(getHeight(), contentRect.height, contentRect.y);

        if (dx != 0 || dy != 0) {
            Point viewPosition = getViewPosition();
            Dimension viewSize = view.getSize();
            int startX = viewPosition.x;
            int startY = viewPosition.y;
            Dimension extent = getExtentSize();

            viewPosition.x -= dx;
            viewPosition.y -= dy;
            
            // Only constrain the location if the view is valid. If the
            // the view isn't valid, it typically indicates the view
            // isn't visible yet and most likely has a bogus size as will
            // we, and therefore we shouldn't constrain the scrolling
            if (view.isValid()) {
                boolean isLeftToRight = true;
                if (isLeftToRight) {
                    if (viewPosition.x + extent.width > viewSize.width) {
                        viewPosition.x = Math.max(0, viewSize.width
                                - extent.width);
                    } else if (viewPosition.x < 0) {
                        viewPosition.x = 0;
                    }
                } else {
                    if (extent.width > viewSize.width) {
                        viewPosition.x = viewSize.width - extent.width;
                    } else {
                        viewPosition.x = Math.max(0, Math.min(
                                viewSize.width - extent.width,
                                viewPosition.x));
                    }
                }
                
                if (viewPosition.y + extent.height > viewSize.height) {
                    viewPosition.y = Math.max(0, viewSize.height
                            - extent.height);
                } else if (viewPosition.y < 0) {
                    viewPosition.y = 0;
                }
            }
            
            if (viewPosition.x != startX || viewPosition.y != startY) {
                setViewPosition(viewPosition);
                // NOTE: How JViewport currently works with the
                // backing store is not foolproof. The sequence of
                // events when setViewPosition
                // (scrollRectToVisible) is called is to reset the
                // views bounds, which causes a repaint on the
                // visible region and sets an ivar indicating
                // scrolling (scrollUnderway). When
                // JViewport.paint is invoked if scrollUnderway is
                // true, the backing store is blitted. This fails
                // if between the time setViewPosition is invoked
                // and paint is received another repaint is queued
                // indicating part of the view is invalid. There
                // is no way for JViewport to notice another
                // repaint has occured and it ends up blitting
                // what is now a dirty region and the repaint is
                // never delivered.
                // It just so happens JTable encounters this
                // behavior by way of scrollRectToVisible, for
                // this reason scrollUnderway is set to false
                // here, which effectively disables the backing
                // store.
//                scrollUnderway = false;
            }
        }
    }

    /**
     * Ascends the <code>Viewport</code>'s parents stopping when a component
     * is found that returns <code>true</code> to <code>isValidateRoot</code>.
     * If all the <code>Component</code>'s parents are visible,
     * <code>validate</code> will then be invoked on it. The
     * <code>RepaintManager</code> is then invoked with
     * <code>removeInvalidComponent</code>. This is the synchronous version
     * of a <code>revalidate</code>.
     */
    private void validateView() {
        // Find the Validate Root of this component
        Component validateRoot = SwingUtilities.windowForComponent(this);
        
        // If no validateRoot, nothing to validate from, 
        // make sure all ancestors are displayable.
        if (validateRoot == null || !validateRoot.isDisplayable()) {
            return;
        }

        // Validate the root
        validateRoot.validate();
    }

     /*  Used by the scrollRectToVisible method to determine the
      *  proper direction and amount to move by. The integer variables are named
      *  width, but this method is applicable to height also. The code assumes that
      *  parentWidth/childWidth are positive and childAt can be negative.
      */
    private int positionAdjustment(int parentWidth, int childWidth, 
            int childAt) {

        //   +-----+
        //   | --- |     No Change
        //   +-----+
        if (childAt >= 0 && childWidth + childAt <= parentWidth) {
            return 0;
        }

        //   +-----+
        //  ---------   No Change
        //   +-----+
        if (childAt <= 0 && childWidth + childAt >= parentWidth) {
            return 0;
        }

        //   +-----+          +-----+
        //   |   ----    ->   | ----|
        //   +-----+          +-----+
        if (childAt > 0 && childWidth <= parentWidth)    {
            return -childAt + parentWidth - childWidth;
        }

        //   +-----+             +-----+
        //   |  --------  ->     |--------
        //   +-----+             +-----+
        if (childAt >= 0 && childWidth >= parentWidth)   {
            return -childAt;
        }

        //   +-----+          +-----+
        // ----    |     ->   |---- |
        //   +-----+          +-----+
        if (childAt <= 0 && childWidth <= parentWidth)   {
            return -childAt;
        }

        //   +-----+             +-----+
        //-------- |      ->   --------|
        //   +-----+             +-----+
        if (childAt < 0 && childWidth >= parentWidth)    {
            return -childAt + parentWidth - childWidth;
        }

        return 0;
    }

    /**
     * The viewport "scrolls" its child (called the "view") by the
     * normal parent/child clipping (typically the view is moved in
     * the opposite direction of the scroll).  A non-<code>null</code> border,
     * or non-zero insets, isn't supported, to prevent the geometry
     * of this component from becoming complex enough to inhibit
     * subclassing.  To create a <code>JViewport</code> with a border,
     * add it to a <code>JPanel</code> that has a border.
     * <p>Note:  If <code>border</code> is non-<code>null</code>, this
     * method will throw an exception as borders are not supported on
     * a <code>JViewPort</code>.
     *
     * @param border the <code>Border</code> to set
     * @exception IllegalArgumentException this method is not implemented
     */
    public final void setBorder(Border border) {
        if (border != null) {
            throw new IllegalArgumentException(
                    "JViewport.setBorder() not supported");
        }
    }

    /**
     * Returns the insets (border) dimensions as (0,0,0,0), since borders
     * are not supported on a <code>JViewport</code>.
     *
     * @return a <code>Rectange</code> of zero dimension and zero origin
     * @see #setBorder
     */
    public final Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }
    
    /**
     * Sets the bounds of this viewport.  If the viewport's width
     * or height has changed, fire a <code>StateChanged</code> event.
     *
     * @param x left edge of the origin
     * @param y top edge of the origin
     * @param w width in pixels
     * @param h height in pixels
     *
     * @see JComponent#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        boolean sizeChanged = (getWidth() != w) || (getHeight() != h);
        super.setBounds(x, y, w, h);
        
        if (sizeChanged) {
            fireStateChanged();
        }
    }

    /**
     * Adds a <code>ChangeListener</code> to the list that is
     * notified each time the view's
     * size, position, or the viewport's extent size has changed.
     *
     * @param listener the <code>ChangeListener</code> to add
     * @see #removeChangeListener
     * @see #setViewPosition
     * @see #setViewSize
     * @see #setExtentSize
     */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
     * Removes a <code>ChangeListener</code> from the list that's notified each
     * time the views size, position, or the viewports extent size
     * has changed.
     *
     * @param listener the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
     * Notifies all <code>ChangeListeners</code> when the views
     * size, position, or the viewports extent size has changed.
     *
     * @see #addChangeListener
     * @see #removeChangeListener
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                
                ((ChangeListener)listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * Set the JViewport's one child
     */
    public void setView(Component view) {
        /* Remove the viewport's existing children, if any.
         * Note that removeAll() isn't used here because it
         * doesn't call remove() (which JViewport overrides).
         */
        int n = getComponentCount();
        for(int i = n - 1; i >= 0; i--) {
            remove(getComponent(i));
        }

        if (view != null) {
            add(view);
            isViewSizeSet = false;
        }
    }

    /**
     * Returns the JViewport's one child
     */
    public Component getView() {
        try {
            return getComponent(0);
        
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns a rectangle whose origin is <code>getViewPosition</code>
     * and size is <code>getExtentSize</code>.
     * This is the visible part of the view, in view coordinates.
     *
     * @return a <code>Rectangle</code> giving the visible part of
     *      the view using view coordinates.
     */
    public Rectangle getViewRect() {
        return new Rectangle(getViewPosition(), getExtentSize());
    }

    public void paint(Graphics g) {
        Point p = getViewPosition();
        //System.out.println("viewPosition: " + p);
        Dimension d = getExtentSize();
        g.setClip(p.x, p.y, d.width, d.height);
        
        super.paint(g);
    }

    /**
     * Returns the view coordinates that appear in the upper left
     * hand corner of the viewport, or 0,0 if there's no view.
     *
     * @return a <code>Point</code> object giving the upper left coordinates
     */
    public Point getViewPosition() {
        Component view = getView();
        if (view != null) {
            Point p = view.getLocation();
            p.x = -p.x;
            p.y = -p.y;
            return p;
        }

        return new Point(0, 0);
    }

    /**
     * Sets the view coordinates that appear in the upper left
     * hand corner of the viewport, does nothing if there's no view.
     *
     * @param p  a <code>Point</code> object giving the upper left coordinates
     */
    public void setViewPosition(Point p) {
        Component view = getView();
        if (view == null)
            return;

        int oldX, oldY, x = p.x, y = p.y;

        /*
         * Collect the old x,y values for the views location and do the song and
         * dance to avoid allocating a Rectangle object if we don't have to.
         */
        if (view instanceof JComponent) {
            JComponent c = (JComponent) view;
            oldX = c.getX();
            oldY = c.getY();
        } else {
            Rectangle r = view.getBounds();
            oldX = r.x;
            oldY = r.y;
        }

        /*
         * The view scrolls in the opposite direction to mouse movement.
         */
        int newX = -x;
        int newY = -y;

        if ((oldX != newX) || (oldY != newY)) {
            // This calls setBounds(), and then repaint().
            view.setLocation(newX, newY);
            fireStateChanged();
        }
    }

    /**
     * If the view's size hasn't been explicitly set, return the
     * preferred size, otherwise return the view's current size.
     * If there is no view, return 0,0.
     *
     * @return a <code>Dimension</code> object specifying the size of the view
     */
    public Dimension getViewSize() {
        Component view = getView();
        if (view == null) {
            return new Dimension(0, 0);
        }

        if (isViewSizeSet) {
            return view.getSize();
        }

        return view.getPreferredSize();
    }

    /**
     * Sets the size of the view.  A state changed event will be fired.
     *
     * @param newSize a <code>Dimension</code> object specifying the new
     *      size of the view
     */
    public void setViewSize(Dimension newSize) {
        Component view = getView();
        if (view != null) {
            Dimension oldSize = view.getSize();
            if (!newSize.equals(oldSize)) {
                view.setSize(newSize);
                isViewSizeSet = true;
                fireStateChanged();
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(getLocation(), getExtentSize());
    }

    /**
     * Sets the size of the visible part of the view using view coordinates.
     *
     * @param newExtent  a <code>Dimension</code> object specifying
     *      the size of the view
     */
    public void setExtentSize(Dimension newExtent) {
        Dimension oldExtent = getExtentSize();
        if (!newExtent.equals(oldExtent)) {
            setSize(newExtent.width, newExtent.height);
            fireStateChanged();
        }
    }

    /**
     * Returns the size of the visible part of the view
     */
    public Dimension getExtentSize() {
        return getSize();
    }

    /**
     * Returns a string representation of this <code>JViewport</code>.
     * This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JViewport</code>
     */
    protected String paramString() {
        String isViewSizeSetString = (isViewSizeSet ? "true" : "false");
        return super.paramString() + ",isViewSizeSet=" + isViewSizeSetString;
    }
}
