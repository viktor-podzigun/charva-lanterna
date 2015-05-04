/* class Component
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import charva.awt.event.AWTEvent;
import charva.awt.event.FocusEvent;
import charva.awt.event.FocusListener;
import charva.awt.event.KeyEvent;
import charva.awt.event.KeyListener;
import charva.awt.event.MouseEvent;
import charva.awt.event.MouseListener;
import charva.awt.event.PaintEvent;


/**
 * Component is the abstract superclass of all the other CHARVA widgets.
 */
public abstract class Component {

    public static final float TOP_ALIGNMENT     = 0.0f;
    public static final float CENTER_ALIGNMENT  = 0.5f;
    public static final float BOTTOM_ALIGNMENT  = 1.0f;
    public static final float LEFT_ALIGNMENT    = 0.0f;
    public static final float RIGHT_ALIGNMENT   = 1.0f;
    
    private static final String     lineSeparator = 
        System.getProperty("line.separator", "\n");
    

    private boolean         focusX;

    /**
     * The coordinates of the top-left corner of the component, relative to
     * its parent container.
     */
    protected Point         origin = new Point(0, 0);

    /**
     * A WeakReference to the Container (e.g Window, Panel or Dialog)
     * that contains us. The reason that we use a WeakReference is to
     * allow the parent to be garbage-collected when there are no more
     * strong references to it.
     */
    protected WeakReference parent;

    /**
     * This flag is true if this component can react to user input.
     */
    protected boolean       enabled = true;

    /**
     * A flag that determines whether this component should be displayed
     * (if its parent is displayed).
     * This flag is set to true by default, except for Window which is
     * initially invisible.
     *
     * @see #setVisible
     * @see #isVisible
     */
    protected boolean       visible = true;

    /**
     * A list of KeyListeners registered for this component.
     */
    protected ArrayList     keyListeners;

    /**
     * A list of FocusListeners registered for this component.
     */
    protected ArrayList     focusListeners;

    /**
     * A list of MouseListeners registered for this component.
     */
    protected ArrayList     mouseListeners;

    /**
     * A flag that is set to true when the container is laid out, and set to
     * false when a component is added or removed from the container
     * (indicating that it needs to be laid out again).
     */
    boolean                 isValid;

    /**
     * The name of this component
     */
    private String          name;

    /**
     * Color pair: foreground color and background color of this component
     */
    protected ColorPair     color;

    
    /**
     * Creates component object and initializes all properties with default 
     * values
     */
    public Component() {
    }
    
    /**
     * Shows or hides this component depending on the value of the
     * parameter <code>visible</code>
     */
    public void setVisible(boolean visible) {
        if (visible)
            show();
        else
            hide();
    }

    public void show() {
        if (!visible) {
            visible = true;
            repaint();    // post a PaintEvent
        }
    }

    public void hide() {
        if (visible) {
            visible = false;

            if (hasFocus()) {
                // try to move focus to next component
                if (!transferFocus()) {
                    throw new IllegalComponentStateException(
                            "Cannot hide component, it was the only " +
                            "focusTraversable component: " + this 
                            + ", window: " + getAncestorWindow());
                }
            }

            repaint();    // post a PaintEvent
        }
    }

    /**
     * Returns true if this component is displayed when its parent
     * container is displayed.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Determines whether this component will be displayed on the screen, 
     * if it's displayable
     * 
     * @return <code>true</code> if the component and all of its ancestors
     *          are visible, <code>false</code> otherwise
     */
    boolean isRecursivelyVisible() {
        Container parent = getParent();
        return visible && (parent == null || parent.isRecursivelyVisible());
    }

    /**
     * Determines whether this component is showing on screen. This means that
     * the component must be visible, and it must be in a container that is
     * visible and showing.
     * 
     * @return <code>true</code> if the component is showing,
     *         <code>false</code> otherwise
     * @see #setVisible
     */
    public boolean isShowing() {
        return isRecursivelyVisible() && isDisplayable();
    }

    /**
     * Draws this component in the given graphics context
     * 
     * @param g  graphics context in which drawing is performed
     */
    public abstract void paint(Graphics g);

    /**
     * Determines whether this component is displayable. A component is 
     * displayable when it is connected to a native screen resource.
     * <p>
     * A component is made displayable either when it is added to
     * a displayable containment hierarchy or when its containment
     * hierarchy is made displayable.
     * A containment hierarchy is made displayable when its ancestor 
     * window is either packed or made visible.
     * <p>
     * A component is made undisplayable either when it is removed from
     * a displayable containment hierarchy or when its containment hierarchy
     * is made undisplayable.  A containment hierarchy is made 
     * undisplayable when its ancestor window is disposed.
     *
     * @return <code>true</code> if the component is displayable, 
     * <code>false</code> otherwise
     * 
     * @see Container#add(Component)
     * @see Window#pack
     * @see Window#show
     * @see Container#remove(Component)
     */
    public boolean isDisplayable() {
        // Every component that has been added to a Container has a parent.
        // The Window class overrides this method because it is never added to
        // a Container.
        Container parent = getParent();
        if (parent == null)
            return false;
        
        return parent.isDisplayable();
    }

    public Point getLocation() {
        return new Point(origin);
    }

    public void setLocation(Point origin) {
        setLocation(origin.x, origin.y);
    }

    public void setLocation(int x, int y) {
        int oldX = origin.x;
        int oldY = origin.y;
        
        origin.x = x;
        origin.y = y;
        
        if (oldX != x || oldY != y)
            repaint();
    }

    /**
     * Return the absolute coordinates of this component's origin.
     * Note that Window (which is a subclass of Container)
     * has a parent value of null, but it overrides this method.
     */
    public Point getLocationOnScreen() {
        if (!isShowing()) {
            throw new IllegalComponentStateException(
                    "Component must be showing on the screen to determine its location");
        }
        
        Point p = getLocation();
        Container parent = getParent();
        while (parent != null) {
            p.x += parent.getX();
            p.y += parent.getY();
            
            parent = parent.getParent();
        }

        return p;
    }

    /**
     * Returns the current x coordinate of the component's origin.
     * This method is preferable to writing
     * <code>component.getBounds().x</code>, or
     * <code>component.getLocation().x</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current x coordinate of the component's origin
     */
    public int getX() {
        return origin.x;
    }

    /**
     * Returns the current y coordinate of the component's origin.
     * This method is preferable to writing
     * <code>component.getBounds().y</code>, or
     * <code>component.getLocation().y</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current y coordinate of the component's origin
     */
    public int getY() {
        return origin.y;
    }
    
    public abstract Dimension getSize();

    /**
     * Returns the current width of this component.
     * This method is preferable to writing
     * <code>component.getBounds().width</code>, or
     * <code>component.getSize().width</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current width of this component
     */
    public abstract int getWidth();

    /**
     * Returns the current height of this component.
     * This method is preferable to writing
     * <code>component.getBounds().height</code>, or
     * <code>component.getSize().height</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current height of this component
     */
    public abstract int getHeight();

    /**
     * Gets the mininimum size of this component.
     * 
     * @return a dimension object indicating this component's minimum size
     * 
     * @see #getPreferredSize
     * @see LayoutManager
     */
    public abstract Dimension getMinimumSize();

    /**
     * Gets the preferred size of this component.
     * 
     * @return a dimension object indicating this component's preferred size
     * 
     * @see #getMinimumSize
     * @see LayoutManager
     */
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    /**
     * Resizes this component so that it has width <code>width</code>
     * and height <code>height</code>
     * 
     * @param width   the new width of this component in pixels
     * @param height  the new height of this component in pixels
     * 
     * @see #getSize
     * @see #setBounds
     */
    public abstract void setSize(int width, int height);

    /**
     * Resizes this component so that it has width <code>d.width</code>
     * and height <code>d.height</code>
     * 
     * @param d  the dimension specifying the new size  of this component
     * 
     * @see #setSize
     * @see #setBounds
     */
    public void setSize(Dimension d) {
        if (d == null)
            throw new IllegalArgumentException("d == null");
        
        setSize(d.width, d.height);
    }
    
    /**
     * Get the bounding rectangle of this component, relative to
     * the origin of its parent Container.
     */
    public Rectangle getBounds() {
        return new Rectangle(origin, getSize());
    }

    public void setBounds(Rectangle bounds) {
        if (bounds == null)
            throw new IllegalArgumentException("bounds == null");
        
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void setBounds(Point topleft, Dimension size) {
        if (topleft == null)
            throw new IllegalArgumentException("topleft == null");
        if (size == null)
            throw new IllegalArgumentException("size == null");
        
        setBounds(topleft.x, topleft.y, size.width, size.height);
    }

    public void setBounds(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    /**
     * Checks whether this component "contains" the specified point,
     * where the point's <i>x</i> and <i>y</i> coordinates are defined
     * to be relative to the coordinate system of this component.
     * 
     * @param p  the point
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Checks whether this component "contains" the specified point,
     * where <code>x</code> and <code>y</code> are defined to be
     * relative to the coordinate system of this component.
     * 
     * @param x  the <i>x</i> coordinate of the point
     * @param y  the <i>y</i> coordinate of the point
     */
    public boolean contains(int x, int y) {
        return (x >= 0 && x < getWidth() && y >= 0 && y < getHeight());
    }

    /**
     * Set the parent container of this component. This is intended to
     * be called by Container objects only.
     * <p>
     * NOTE: we use a WeakReference so that the parent can be garbage-
     * collected when there are no more strong references to it.
     */
    public void setParent(Container container) {
        parent = new WeakReference(container);

        if (container != null) {
            // inherit the parent color scheme
            setColors(container.getColors());
        }
    }

    /**
     * Get the parent container of this component. Can return null if the
     * component has no parent.
     */
    public Container getParent() {
        if (parent == null)
            return null;

        // note that parent is a WeakReference
        return (Container) parent.get();
    }

    /**
     * Notifies this component that it now has a parent ancestor window
     */
    protected void addNotify() {
    }
    
    /**
     * Notifies this component that it no longer has a ancestor window.
     * <p>
     * This isn't strictly correct. The event shouldn't be
     * fired until *after* the parent is set to null.  But
     * we only get notified before that happens.
     */
    protected void removeNotify() {
    }
    
    /**
     * Register a KeyListener object for this component.
     */
    public void addKeyListener(KeyListener kl) {
        if (keyListeners == null)
            keyListeners = new ArrayList();
        
        if (!keyListeners.contains(kl))
            keyListeners.add(kl);
    }

    /**
     * Unregister a KeyListener
     */
    public void removeKeyListener(KeyListener kl) {
        if (keyListeners != null)
            keyListeners.remove(kl);
    }
    
    /**
     * Register a FocusListener object for this component.
     */
    public void addFocusListener(FocusListener fl) {
        if (focusListeners == null)
            focusListeners = new ArrayList();
        
        if (!focusListeners.contains(fl))
            focusListeners.add(fl);
    }

    /**
     * Unregister a FocusListener
     */
    public void removeFocusListener(FocusListener fl) {
        if (focusListeners != null)
            focusListeners.remove(fl);
    }
    
    /**
     * Register a MouseListener object for this component.
     */
    public void addMouseListener(MouseListener ml) {
        if (mouseListeners == null)
            mouseListeners = new ArrayList();
        
        if (!mouseListeners.contains(ml))
            mouseListeners.add(ml);
    }

    /**
     * Unregister a MouseListener
     */
    public void removeMouseListener(MouseListener ml) {
        if (mouseListeners != null)
            mouseListeners.remove(ml);
    }
    
    /**
     * Process events that are implemented by all components.
     * This can be overridden by subclasses, to handle custom events.
     */
    protected void processEvent(AWTEvent evt) {
        if (evt instanceof KeyEvent) {
            KeyEvent ke = (KeyEvent) evt;

            // Find the ancestor Window that contains the component that
            // generated the keystroke.
            // Then we call the processKeyEvent method
            // of the ancestor Window, which calls the same method in its
            // current-focus container, and so on, until the KeyEvent
            // gets down to the component that generated the keystroke.
            // This allows KeyEvents to be processed by outer enclosing
            // containers, then by inner containers, and finally by the
            // component that generated the KeyEvent.

            this.getAncestorWindow().processKeyEvent(ke);
        
        } else if (evt instanceof FocusEvent) {
            processFocusEvent((FocusEvent) evt);
        
        } else if (evt instanceof MouseEvent) {

            MouseEvent e = (MouseEvent) evt;
//	    if (e.getModifiers() != MouseEvent.MOUSE_PRESSED)
//		return;

            processMouseEvent(e);
        }
    }

    /**
     * Invoke all the KeyListener callbacks that may have been registered
     * for this component. The KeyListener objects may modify the
     * keycodes, and can also set the "consumed" flag. The KeyListeners are
     * invoked in last to first order.
     */
    public void fireKeyEvent(KeyEvent ke) {
        if (keyListeners != null) {
            for (int i = keyListeners.size() - 1; i >= 0; i--) {

                KeyListener kl = (KeyListener) keyListeners.get(i);
                if (ke.getID() == KeyEvent.KEY_PRESSED)
                    kl.keyPressed(ke);
                else if (ke.getID() == KeyEvent.KEY_TYPED)
                    kl.keyTyped(ke);

                if (ke.isConsumed())
                    break;
            }
        }
    }
    
    /**
     * Processes key events occurring on this component by dispatching them
     * to any registered KeyListener objects
     */
    protected void processKeyEvent(KeyEvent ke) {
        fireKeyEvent(ke);
    }

    /**
     * First invoke all the MouseListeners that have been registered for
     * this component. If any component sets the "consumed" flag, the method
     * returns. The MouseListeners are invoked in last-to-first order.
     */
    public void fireMouseEvent(MouseEvent e) {
        if (mouseListeners != null) {
            for (int i = mouseListeners.size() - 1; i >= 0; i--) {
                
                MouseListener ml = (MouseListener) mouseListeners.get(i);
                if (e.getID() == MouseEvent.MOUSE_PRESSED)
                    ml.mousePressed(e);
                else if (e.getID() == MouseEvent.MOUSE_RELEASED)
                    ml.mouseReleased(e);
                else if (e.getID() == MouseEvent.MOUSE_CLICKED)
                    ml.mouseClicked(e);

                if (e.isConsumed())
                    return;
            }
        }
    }
    
    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this component.
     */
    protected void processMouseEvent(MouseEvent e) {
        // First invoke all the MouseListeners that have been registered for
        // this component. If any component sets the "consumed" flag, the method
        // returns. The MouseListeners are invoked in last-to-first order.
        fireMouseEvent(e);

        // The default for a left-mouse-button-press is to request the focus;
        // this is overridden by components such as buttons.
        if (e.getButton() == MouseEvent.BUTTON1 
                && e.getID() == MouseEvent.MOUSE_CLICKED 
                && e.getClickCount() == 1
                && this.isFocusTraversable()) {

            requestFocus();
        }
    }

    /**
     * Invoke all the FocusListener callbacks that may have been registered
     * for this component.
     */
    protected void processFocusEvent(FocusEvent fe) {
        if (focusListeners != null) {
            Iterator iter = focusListeners.iterator();
            while (iter.hasNext()) {
                FocusListener fl = (FocusListener) iter.next();
                if (fe.getID() == FocusEvent.FOCUS_GAINED)
                    fl.focusGained(fe);
                else
                    fl.focusLost(fe);
            }
        }
    }

    /**
     * Get the Window that contains this component.
     */
    Window getAncestorWindow() {
        Container ancestor;
        Container nextancestor;

        if (this instanceof Window)
            return (Window) this;

        for (ancestor = getParent();
             !(ancestor instanceof Window);
             ancestor = nextancestor) {

            if (ancestor == null)
                return null;

            if ((nextancestor = ancestor.getParent()) == null)
                return null;
        }
        
        return (Window) ancestor;
    }

    /**
     * Transfers the focus to the next component, as though this Component 
     * were the focus owner
     * 
     * @return <tt>true</tt> if current focus was changed or <tt>false</tt>
     *         otherwise
     */
    public boolean transferFocus() {
        return getParent().nextFocus();
    }

    /**
     * Transfers the focus to the previous component, as though this Component
     * were the focus owner
     * 
     * @return <tt>true</tt> if current focus was changed or <tt>false</tt>
     *         otherwise
     */
    public boolean transferFocusBackward() {
        return getParent().previousFocus();
    }

    /**
     * This method should be invoked by all subclasses of Component
     * which override this method; because this method generates the
     * FOCUS_GAINED event when the component gains the keyboard focus.
     */
    public void requestFocus() {
        boolean temporary = false;
        
        // generate the FOCUS_GAINED only if the component does not
        // already have the focus

        Window ancestor = getAncestorWindow();
        if (ancestor == null) {
            throw new IllegalComponentStateException(
                    "Cannot requestFocus before the component is added "
                    + "to the window: " + this);
        }
        
        Component currentFocus  = ancestor.getCurrentFocus();
        Window    sourcewin     = Toolkit.getDefaultToolkit().getTopWindow();
        Component currentFocusX = sourcewin.getCurrentFocus();
        
        if (currentFocus != this || (currentFocusX == this && !focusX)) {
            focusX = true;
            EventQueue evtQueue =
                    Toolkit.getDefaultToolkit().getSystemEventQueue();
            FocusEvent evt;
            FocusEvent lastEvt = Toolkit.getDefaultToolkit().getLastFocusEvent();

            if (lastEvt != null) {
                currentFocus = (Component) lastEvt.getSource();
                if (currentFocus != null 
                        && currentFocus.getAncestorWindow() != null 
                        && currentFocus.getAncestorWindow().isEnabled() 
                        && currentFocus.getAncestorWindow() != ancestor) {
                    
                    temporary = true;
                } else {
                    temporary = false;
                }
                
                evt = new FocusEvent(FocusEvent.FOCUS_LOST, 
                        currentFocus, temporary, this);
                evtQueue.postEvent(evt);
            
            } else {
                currentFocus = null;
            }

            evt = new FocusEvent(FocusEvent.FOCUS_GAINED, 
                    this, temporary, currentFocus);
            evtQueue.postEvent(evt);

//	    if (getParent() != null)
            getParent().setFocus(this);

            repaint();
        }
    }

    /**
     * This is provided for compatibility with Swing
     *
     * @return true, always.
     */
    public boolean requestFocusInWindow() {
        requestFocus();
        return true;
    }

    /**
     * Returns <code>true</code> if this <code>Component</code> is the 
     *    focus owner.
     *
     * @return <code>true</code> if this <code>Component</code> is the 
     *     focus owner; <code>false</code> otherwise
     */
    public boolean isFocusOwner() {
        return hasFocus();
    }

    /**
     * Returns true if this Component has the keyboard input focus.
     */
    public boolean hasFocus() {
        // Modified 19-Feb-02 by rgittens to handle null ancestor.
        Window ancestor = getAncestorWindow();
        if (ancestor == null)
            return false;

        return (ancestor.getCurrentFocus() == this);
    }

    /**
     * Indicates whether this component can be traversed using Tab or
     * Shift-Tab keyboard focus traversal. If this
     * method returns "false" it can still request focus using requestFocus(),
     * but it will not automatically be assigned focus during keyboard focus
     * traversal.
     */
    public boolean isFocusTraversable() {
        return (enabled && isRecursivelyVisible());
    }

    public boolean isRequestFocusEnabled() {
        return isFocusTraversable();
    }
    
    /**
     * Return true if this component is totally obscured by one or more
     * windows that are stacked above it.
     */
    public boolean isTotallyObscured() {
        Rectangle bounds  = getBounds();
        Window   ancestor = getAncestorWindow();

        LinkedList windowList = Toolkit.getDefaultToolkit().getWindowList();
        boolean    obscured   = false;
        synchronized (windowList) {

            // Ignore windows that are stacked below this component's
            // ancestor.
            Iterator iter = windowList.iterator();
            while (iter.hasNext()) {
                Window w = (Window)iter.next();
                if (w == ancestor)
                    break;
            }
            
            if (iter.hasNext())
                iter.next();

            // Return true if any of the overlying windows totally obscures
            // this component.
            while (iter.hasNext()) {
                Window w = (Window)iter.next();
                Rectangle windowRect = w.getBounds();
                if (bounds.equals(windowRect.intersection(bounds))) {
                    obscured = true;
                    break;
                }
            }
        }
        return obscured;
    }

    /**
     * Returns the alignment along the X axis.  This indicates how the
     * component would like to be aligned relative to ther components.
     * 0 indicates left-aligned, 0.5 indicates centered and 1 indicates
     * right-aligned.
     */
    public float getAlignmentX() {
        return CENTER_ALIGNMENT;
    }

    /**
     * Returns the alignment along the Y axis.  This indicates how the
     * component would like to be aligned relative to ther components.
     * 0 indicates top-aligned, 0.5 indicates centered and 1 indicates
     * bottom-aligned.
     */
    public float getAlignmentY() {
        return CENTER_ALIGNMENT;
    }

    /**
     * Returns this component's color code from its foreground
     * and background colors.
     * <p>
     * If both colors are null, the default color code is returned.
     * 
     * @return this component's color code
     */
    public int getColorCode() {
        return color.getColorCode();
    }
    
    /**
     * Returns this component's color
     */
    public ColorPair getColor() {
        return color;
    }
    
    /**
     * Returns the foreground color of this component
     */
    public Color getForeground() {
        return color.getForeground();
    }

    /**
     * Returns the background color of this component
     */
    public Color getBackground() {
        return color.getBackground();
    }

    /**
     * Sets this component's color
     */
    public void setColor(ColorPair newColor) {
        this.color = newColor;
    }
    
    /**
     * Sets the foreground color of this component
     */
    public void setForeground(Color foregroundColor) {
        if (foregroundColor == null)
            throw new IllegalArgumentException("foregroundColor == null");
        
        color = ColorPair.create(foregroundColor, color.getBackground());
    }

    /**
     * Sets the background color of this component
     */
    public void setBackground(Color backgroundColor) {
        if (backgroundColor == null)
            throw new IllegalArgumentException("backgroundColor == null");
        
        color = ColorPair.create(color.getForeground(), backgroundColor);
    }

    /**
     * Initializes the current color scheme for this component.
     * <p>
     * Subclasses can override this method to update component's colors.
     */
    public void setColors(ColorScheme colors) {
        if (colors == null)
            throw new IllegalArgumentException("colors == null");
        
        color = colors.getColor(ColorScheme.DIALOG);
    }
    
    /**
     * Enables this component to react to user input. Components
     * are enabled by default.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        // if this component is already displayed, generate a PaintEvent
        // and post it onto the queue
        repaint();
    }

    /**
     * Determine whether this component can react to user input.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Marks the component and all parents above it as needing to be laid out
     * again. This method is overridden by Container.
     */
    public void invalidate() {
        isValid = false;
        Container parent = getParent();
        if (parent != null)
            parent.invalidate();
    }

    /**
     * Ensures that this component is laid out correctly.
     * This method is primarily intended to be used on instances of
     * Container. The default implementation does nothing; it is
     * overridden by Container.
     */
    public void validate() {
        isValid = true;
    }

    /**
     * Causes this component to be repainted as soon as possible
     * (this is done by posting a RepaintEvent onto the system queue).
     */
    public void repaint() {
        if (!isDisplayable())
            return;

        PaintEvent evt = new PaintEvent(this, getBounds());
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        queue.postEvent(evt);
    }

    /**
     * Determines whether this component has a valid layout.  A component
     * is valid when it is correctly sized and positioned within its
     * parent container and all its children (in the case of a Container)
     * are also valid. The default implementation returns true; this method
     * is overridden by Container.
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Sets the name of the component
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the component
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representing the state of this component. This 
     * method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between 
     * implementations. The returned string may be empty but may not be 
     * <code>null</code>.
     * 
     * @return  a string representation of this component's state
     */
    protected String paramString() {
        String thisName = getName();
        Point pos = getLocation();
        Dimension size = getSize();
        String str = (thisName != null ? 
                    thisName : Integer.toHexString(
                            System.identityHashCode(this))) 
                + "," + pos.x + "," + pos.y
                + "," + size.width + "x" + size.height;
        if (!visible)
            str += ",hidden";
        
        if (!enabled)
            str += ",disabled";
        
        return str;
    }

    /**
     * Returns a string representation of this component and its values.
     * 
     * @return a string representation of this component
     */
    public String toString() {
        return getClass().getName() + "[" + paramString() + "]";
    }

    /**
     * Prints a listing of this component to the specified string buffer.
     * 
     * @param out  a string buffer
     */
    public void list(StringBuffer out) {
        list(out, 0);
    }

    /**
     * Prints out a list, starting at the specified indentation, to the
     * specified string buffer.
     * 
     * @param out     a string buffer
     * @param indent  number of spaces to indent
     */
    public void list(StringBuffer out, int indent) {
        for (int i = 0; i < indent; i++)
            out.append(" ");
        
        out.append(this).append(lineSeparator);
    }
}
