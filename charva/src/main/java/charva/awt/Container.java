/* class Container
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

package charva.awt;

import java.util.ArrayList;
import charva.awt.event.KeyEvent;


/**
 * Container is the abstract superclass of Window and Panel.
 */
public abstract class Container extends Component {
    
    /**
     * The list of components contained within this Container
     */
    private ArrayList           components;

    /**
     * The container's size
     */
    protected Dimension         size = new Dimension(0, 0);

    /**
     * The layout manager that will be used to lay out the components
     */
    protected LayoutManager     layoutMgr;

    /**
     * The component (which may itself be a Container) inside this Container
     * that currently has the input focus (or, if the input focus is
     * currently outside this Container, the component to which focus will
     * return if and when this Container regains focus).
     */
    protected Component         currentFocus;

    /**
     * Used for caching the minimum size of this container, so that we don't
     * have to keep recalculating it. This dimension is valid only if isValid
     * is true.
     */
    protected Dimension         minimumSize;
    
    /**
     * Color scheme for this container and all its components
     */
    protected ColorScheme       currentColors;
    
    
    protected Container() {
        setColors(Toolkit.getNormalColors());
    }

    public ColorScheme getColors() {
        return currentColors;
    }
    
    /**
     * Initializes the current color scheme for this container and all its 
     * components
     */
    public void setColors(ColorScheme colors) {
        super.setColors(colors);

        currentColors = colors;
        if (components == null)
            return;
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component) components.get(i);
            // inherit the parent color scheme
            c.setColors(colors);
        }
    }
    
    /**
     * Causes this container to lay out its components.  Most programs 
     * should not call this method directly, but should invoke 
     * the <code>validate</code> method instead.
     * 
     * @see LayoutManager#layoutContainer
     * @see #setLayout
     * @see #validate
     */
    public void doLayout() {
        if (layoutMgr != null) {
            layoutMgr.layoutContainer(this);
    
            // Don't set the isValid flag if the layout manager flag
            // is an instance of LayoutManager2; the doLayout method must
            // be called every time because the parent window may have been
            // resized.
            // Instances of LayoutManager, on the other hand, are not affected
            // be resizing of the parent window.
            //if (!(layoutMgr instanceof LayoutManager2))
            //    isValid = true;
        }
    }

    public Dimension getSize() {
        return new Dimension(size);
    }

    public int getHeight() {
        return size.height;
    }

    public int getWidth() {
        return size.width;
    }

    public void setSize(Dimension size) {
        setSize(size.width, size.height);
    }

    public void setSize(int width, int height) {
        this.size.width  = width;
        this.size.height = height;
        invalidate();
    }

    public void setHeight(int height) {
        this.size.height = height;
        invalidate();
    }

    public void setWidth(int width) {
        this.size.width  = width;
        invalidate();
    }

    public Dimension getMinimumSize() {
        if (layoutMgr == null)
            return size;
        
        if (!isValid)
            minimumSize = layoutMgr.minimumLayoutSize(this);
        
        return minimumSize;
    }

    public boolean hasChildren() {
        return (components != null);
    }
    
    /**
     * Returns the number of components in this Container
     */
    public int getComponentCount() {
        if (components == null)
            return 0;
        
        return components.size();
    }

    public int getComponentIndex(Component c) {
        if (components == null)
            return -1;
        
        return components.indexOf(c);
    }

    /**
     * Returns the component at the specified index.
     */
    public Component getComponent(int n) {
        if (components == null)
            return null;
        
        return (Component) components.get(n);
    }

    protected Component getComponentAt(int x, int y, boolean checkVisibility) {
        if (components == null)
            return null;
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component) components.get(i);
            int   compX = c.getX();
            int   compY = c.getY();
            
            if ((!checkVisibility || c.isVisible()) 
                    && c.contains(x - compX, y - compY)) {
                
                if (c instanceof Container && ((Container) c).hasChildren()) {
                    // Calculate the coordinates of the point relative
                    // to the origin of the container
                    return ((Container) c).getComponentAt(x - compX, 
                            y - compY, checkVisibility);
                }
                
                return c;
            }
        }
        
        return null;
    }

    /**
     * Returns the component that contains the specified point, or null
     * if no component contains the point. The x and y coordinates of
     * the point are relative to the origin of this container.
     */
    public Component getComponentAt(Point p) {
        return getComponentAt(p.x, p.y, false);
    }

    /**
     * Returns the component that contains the specified point, or null
     * if no component contains the point. The x and y coordinates of
     * the point are relative to the origin of this container.
     */
    public Component getComponentAt(int x, int y) {
        return getComponentAt(x, y, false);
    }

    /**
     * Returns the visible component that contains the specified point,
     * or null if no visible component contains the point. The x and y
     * coordinates of the point are relative to the origin of this container.
     */
    public final Component findComponentAt(Point p) {
        return findComponentAt(p.x, p.y);
    }

    /**
     * Returns the visible component that contains the specified point,
     * or null if no visible component contains the point. The x and y
     * coordinates of the point are relative to the origin of this container.
     */
    public final Component findComponentAt(int x, int y) {
    	if (!isRecursivelyVisible())
    	    return null;
    	
    	return(getComponentAt(x, y, true));
    }

    /** 
     * Appends the specified component to the end of this container. 
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param comp  the component to be added
     * @return      the component argument
     * 
     * @see #addImpl
     * @see #validate
     */
    public Component add(Component comp) {
        addImpl(comp, null);
        return comp;
    }

    /**
     * Adds the specified component to the end of this container.
     * Also notifies the layout manager to add the component to 
     * this container's layout using the specified constraints object.
     * This is a convenience method for {@link #addImpl}.
     * <p>
     * Note: If a component has been added to a container that
     * has been displayed, <code>validate</code> must be
     * called on that container to display the new component.
     * If multiple components are being added, you can improve
     * efficiency by calling <code>validate</code> only once,
     * after all the components have been added.
     *
     * @param comp         the component to be added
     * @param constraints  an object expressing layout contraints for 
     *                     this component
     * 
     * @see #addImpl
     * @see #validate
     * @see LayoutManager
     */
    public void add(Component comp, Object constraints) {
        addImpl(comp, constraints);
    }

    /**
     * Adds the specified component to this container at the specified
     * index. This method also notifies the layout manager to add 
     * the component to this container's layout using the specified 
     * constraints object via the <code>addLayoutComponent</code>
     * method.  The constraints are
     * defined by the particular layout manager being used.  For 
     * example, the <code>BorderLayout</code> class defines five
     * constraints: <code>BorderLayout.NORTH</code>,
     * <code>BorderLayout.SOUTH</code>, <code>BorderLayout.EAST</code>,
     * <code>BorderLayout.WEST</code>, and <code>BorderLayout.CENTER</code>.
     * <p>
     * Note that if the component already exists
     * in this container or a child of this container, 
     * it is removed from that container before
     * being added to this container. 
     * <p>
     * This is the method to override if a program needs to track 
     * every add request to a container as all other add methods defer
     * to this one. An overriding method should 
     * usually include a call to the superclass's version of the method:
     * <p>
     * <blockquote>
     * <code>super.addImpl(comp, constraints)</code>
     * </blockquote>
     * 
     * @param comp         the component to be added
     * @param constraints  an object expressing layout constraints for 
     *                     this component
     * 
     * @exception IllegalArgumentException 
     *                if adding the container's parent to itself
     * @exception IllegalArgumentException 
     *                if adding a window to a container
     * 
     * @see #add(Component)       
     * @see #add(Component, java.lang.Object)       
     * @see LayoutManager
     * @see LayoutManager2
     */
    protected void addImpl(Component comp, Object constraints) {
        // Check for correct arguments: comp cannot be one of this 
        // container's parents, and comp cannot be a window
        //
        if (comp instanceof Container) {
            for (Container cn = this; cn != null; cn = cn.getParent()) {
                if (cn == comp) {
                    throw new IllegalArgumentException(
                          "Adding container's parent to itself");
                }
            }
            if (comp instanceof Window) {
                throw new IllegalArgumentException(
                       "Adding a window to a container");
            }
        }
        
        // Reparent the component
        Container compParent = comp.getParent();
        if (compParent != null){
            compParent.remove(comp);
        }
        
        if (components == null) {
            components = new ArrayList();
        }
        
        // Add the specified component to the list of components 
        // in this container
        components.add(comp);

        // Set this container as the parent of the component
        comp.setParent(this);

        if (isValid) {
            invalidate();
        }
        
        if (isDisplayable()) {
            comp.addNotify();
        }
    
        if (layoutMgr != null) {
            if (layoutMgr instanceof LayoutManager2) {
                ((LayoutManager2)layoutMgr).addLayoutComponent(
                        comp, constraints);
            
            } else if (constraints instanceof String) {
                layoutMgr.addLayoutComponent((String)constraints, comp);
            }
        }
    }

    /**
     * Removes the specified component from this container
     */
    public void remove(Component comp) {
        if (components == null) {
            return;
        }
        
        removeImpl(comp);
        
        if (currentFocus == comp) {
            currentFocus = null; // reset current focus
            currentFocus = getCurrentFocus();
        }
        
        if (isValid) {
            invalidate();
        }
    }
    
    public void removeAll() {
        if (components == null || components.size() == 0) {
            return;
        }
        
        Component[] compArr = (Component[])components.toArray(
                new Component[components.size()]);
        
        for (int i = 0; i < compArr.length; i++) {
            removeImpl(compArr[i]);
        }
    
        if (isValid) {
            invalidate();
        }
    }
    
    protected void removeImpl(Component comp) {
        if (isDisplayable()) {
            comp.removeNotify();
        }
        
        if (layoutMgr != null) {
            layoutMgr.removeLayoutComponent(comp);
        }
        
        components.remove(comp);
        comp.setParent(null);
    }

    /** 
     * Makes this Container displayable by connecting it to
     * a native screen resource.  Making a container displayable will
     * cause all of its children to be made displayable.
     * <p>
     * This method is called internally by the toolkit and should
     * not be called directly by programs.
     * 
     * @see Component#isDisplayable
     * @see #removeNotify
     */
    protected void addNotify() {
        super.addNotify();
    
        if (components == null) {
            return;
        }
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component)components.get(i);
            c.addNotify();
        }
    }

    /** 
     * Makes this Container undisplayable by removing its connection
     * to its native screen resource.  Making a container undisplayable
     * will cause all of its children to be made undisplayable.
     * <p>
     * This method is called by the toolkit internally and should
     * not be called directly by programs.
     * 
     * @see Component#isDisplayable
     * @see #addNotify
     */
    protected void removeNotify() {
        super.removeNotify();
    
        if (components == null) {
            return;
        }
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component)components.get(i);
            c.removeNotify();
        }
    }
    
    public LayoutManager getLayout() {
        return layoutMgr;
    }
    
    public void setLayout(LayoutManager layout) {
        if (layout == null) {
            throw new IllegalArgumentException("layout cannot be null");
        }
        
        layoutMgr = layout;
    }

    /**
     * Returns an array of all the components in this container.
     */
    public Component[] getComponents() {
        if (components == null) {
            return null;
        }
        
        int arraylen = components.size();
        return (Component[])components.toArray(new Component[arraylen]);
    }

    public void paint(Graphics g) {
        paintComponents(g);
    }
    
    /**
     * Paints each of the components in this container.
     * 
     * @param g  the graphics context
     * @see Component#paint
     */
    protected void paintComponents(Graphics g) {
        if (!isShowing() || components == null) {
            return;
        }
    
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp == null || !comp.isVisible()) {
                continue;
            }
            
            comp.paint(g.create(comp.getX(), comp.getY(), 
                    comp.getWidth(), comp.getHeight()));
        }
    }

    protected void processKeyEvent(KeyEvent ke) {
        // Invoke all the KeyListener callbacks that may have been registered
        // for this Container
        super.processKeyEvent(ke);
        if (ke.isConsumed())
            return;

        // Propagate the KeyEvent down to the current focus component
        // inside this container
        if (currentFocus != null)
            currentFocus.processKeyEvent(ke);
    }

    public void requestFocus() {
        if (components == null) {
            super.requestFocus();
            return;
        }
        
        Component current = getCurrentFocus();
        if (current != null)
            current.requestFocus();
    }

    /**
     * Return a reference to the (non-container) component inside this
     * Container that has the keyboard input focus (or would have it,
     * if the focus was inside this container). If no component inside
     * the container has the focus, choose the first FocusTraversable
     * component.
     *
     * @return   the Component in this container that would have the focus;
     *           never null
     * @throws IllegalComponentStateException
     *           if there is no focus-traversable component in this container
     */
    public Component getCurrentFocus() {
        if (currentFocus == null) {
            if (components == null)
                return null;
            
            // currentFocus is not yet set. Try to set it to the first
            // FocusTraversable component contained in this container
            final int count = components.size();
            for (int i = 0; i < count; i++) {
                Component c = (Component) components.get(i);
                if (c.isFocusTraversable()) {
                    currentFocus = c;
                    break;
                }
            }
        }

// Don't throw an Exception, since we can have window without focus traversable 
// components (wait dialog, for example)
//
//        if (currentFocus == null) {
//            throw new IllegalComponentStateException(
//                    "No focus-traversable components inside container");
//        }
        
        if (currentFocus instanceof Container) {
            Container c = (Container)currentFocus;
            if (c.hasChildren()) {
                return c.getCurrentFocus();
            }
        }
        
        return currentFocus;
    }

    /**
     * Sets the currentFocus to refer to the next focus-traversable component
     * in the list of contained components, and put FocusEvents on the queue,
     * one for the component that is losing the focus and one for the component
     * gaining the focus.
     *
     * @return <tt>true</tt> if current focus was changed or <tt>false</tt>
     *         otherwise
     */
    protected boolean nextFocus() {
        if (components == null)
            return false;
        
        // determine which component should get focus next
        int index = components.indexOf(currentFocus);
        if (index == -1) {
            throw new IllegalComponentStateException(
                    "Focus component not found in parent");
        }

        Component focusCandidate;

        for (; ;) {
            if (++index >= components.size()) {
                // if the focus was owned by the last component in this 
                // container, the new focus should go to the next component in 
                // the parent container, 
                // IF THERE IS A PARENT (this container may be a Window, 
                // in which case the parent is always null)
                if (getParent() != null) {
                    if (getParent().nextFocus())
                        return true;
                }
                
                // don't need to worry about infinite loops, in worst case, 
                // we should just end up where we started
                index = 0;
            }

            focusCandidate = (Component) components.get(index);

            // if the next component will not accept the focus, continue
            // trying until we get one that does
            if (focusCandidate.isFocusTraversable())
                break;
        }
        
        if (currentFocus != focusCandidate) {
            if (focusCandidate instanceof Container 
                    && ((Container) focusCandidate).hasChildren()) {
                
                ((Container) focusCandidate).firstFocus();
            }
    
            focusCandidate.requestFocus();
            return true;
        }
        
        return false;
    }

    /**
     * Sets the currentFocus to refer to the previous focus-traversable
     * component in the list of contained components, and put FocusEvents on
     * the queue, one for the component that is losing the focus and one for
     * the component gaining the focus.
     * 
     * @return <tt>true</tt> if current focus was changed or <tt>false</tt>
     *         otherwise
     */
    protected boolean previousFocus() {
        if (components == null)
            return false;
        
        // determine which component should get focus next
        int index = components.indexOf(currentFocus);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "Focus component not found in parent");
        }

        Component focusCandidate;

        for (; ;) {
            if (--index < 0) {
                // if the focus was owned by the first component in this
                // container, the new focus should go to the previous component 
                // in the parent container, 
                // IF THERE IS A PARENT (this container may be a Window, 
                // in which case the parent is always null)
                if (getParent() != null) {
                    if (getParent().previousFocus())
                        return true;
                }
                
                index = components.size() - 1;
            }

            focusCandidate = (Component) components.get(index);

            // if the next component will not accept the focus, continue
            // trying until we get one that does
            if (focusCandidate.isFocusTraversable())
                break;
        }
        
        if (currentFocus != focusCandidate) {
            if (focusCandidate instanceof Container 
                    && ((Container) focusCandidate).hasChildren()) {
                
                ((Container) focusCandidate).lastFocus();
            }
    
            focusCandidate.requestFocus();
            return true;
        }
        
        return false;
    }

    /**
     * Set this container's current keyboard focus. Called by the
     * requestFocus() method of the contained component.
     */
    public void setFocus(Component focus) {
        //TODO: Make this method package - visible
        
        currentFocus = focus;
        if (getParent() != null)
            getParent().setFocus(this);
    }

    /**
     * Return true if any of the components within this Container
     * are focus-traversable (i.e. will accept keyboard input focus when
     * TAB or SHIFT-TAB is pressed).
     */
    public boolean isFocusTraversable() {
        if (!super.isFocusTraversable())
            return false;

        if (components == null)
            return true;
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component) components.get(i);
            if (c.isFocusTraversable())
                return true;
        }
        
        return false;
    }

    /**
     * Returns current insets for this container.
     * <p>
     * The insets define how much padding to insert inside the Container,
     * to take into account the border frame (if any).
     */
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    /**
     * Sets the keyboard focus to the first component that is focusTraversable.
     * Called by the nextFocus() method when it runs out of components in
     * the current container to move the focus to.  The nextFocus() method
     * first checks that this container contains a focusTraversable component
     * before calling this.
     */
    private void firstFocus() {
        if (components == null)
            return;
        
        final int count = components.size();
        for (int i = 0; i < count; i++) {
            Component c = (Component) components.get(i);
            if (c.isFocusTraversable()) {
                if (c instanceof Container)
                    ((Container) c).firstFocus();

                currentFocus = c;
                return;
            }
        }
    }

    /**
     * Sets the keyboard focus to the last component that is focusTraversable.
     * Called by the previousFocus() method when it runs out of components in
     * the current container to move the focus to.  The previousFocus() method
     * first checks that this container contains a focusTraversable component
     * before calling this.
     */
    private void lastFocus() {
        if (components == null)
            return;
        
        for (int i = components.size() - 1; i >= 0; i--) {
            Component c = (Component) components.get(i);
            if (c.isFocusTraversable()) {
                if (c instanceof Container && ((Container) c).hasChildren())
                    ((Container) c).lastFocus();

                currentFocus = c;
                return;
            }
        }
    }

    /**
     * Validates this container and all of its contained components.
     * The programmer must call validate() on a container to cause it
     * to re-layout its contained components after components have
     * been added, removed or resized.
     */
    public void validate() {
        if (!isValid) {
            validateTree();
            isValid = true;
        }
    }

    /**
     * Recursively descends the container tree and recomputes the
     * layout for any subtrees marked as needing it (those marked as
     * invalid).
     */
    protected void validateTree() {
        if (!isValid) {
            doLayout();
            if (components != null) {
                final int ncomponents = components.size();
                for (int i = 0 ; i < ncomponents; ++i) {
                    Component comp = (Component)components.get(i);
                    if ((comp instanceof Container) 
                            && !(comp instanceof Window)
                            && !comp.isValid) {
                        
                        ((Container)comp).validateTree();
                    } else {
                        comp.validate();
                    }
                }
            }
        }
        
        isValid = true;
    }

    /**
     * Marks the container and all parents above it as needing to be laid out
     * again.
     */
    public void invalidate() {
        LayoutManager layoutMgr = this.layoutMgr;
        if (layoutMgr instanceof LayoutManager2) {
            LayoutManager2 lm = (LayoutManager2) layoutMgr;
            lm.invalidateLayout(this);
        }
        
        super.invalidate();
    }

    /**
     * Returns a string representing the state of this <code>Container</code>.
     * This method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between 
     * implementations. The returned string may be empty but may not be 
     * <code>null</code>.
     *
     * @return    the parameter string of this container
     */
    protected String paramString() {
        String str = super.paramString();
        
        LayoutManager layoutMgr = this.layoutMgr;
        if (layoutMgr != null) {
            if (!(layoutMgr instanceof LayoutManager2) && !isValid)
                str += ",invalid";

            str += ",layout=" + layoutMgr.getClass().getName();
        }
        
        return str;
    }

    /**
     * Prints a listing of this container to the specified string buffer. 
     * The listing starts at the specified indentation. 
     * 
     * @param    out      a string buffer
     * @param    indent   the number of spaces to indent
     * 
     * @see      Component#list(java.lang.StringBuffer, int)
     */
    public void list(StringBuffer out, int indent) {
        super.list(out, indent);
        ArrayList components = this.components;
        if (components != null) {
            final int count = components.size();
            for (int i = 0; i < count; i++) {
                Component comp = (Component)components.get(i);
                if (comp != null)
                    comp.list(out, indent + 1);
            }
        }
    }
}
