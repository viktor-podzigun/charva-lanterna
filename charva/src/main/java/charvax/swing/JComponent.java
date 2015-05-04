/* class JComponent
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

import java.beans.PropertyChangeListener;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import charva.awt.ColorPair;
import charva.awt.Component;
import charva.awt.Container;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.Rectangle;
import charva.awt.Window;
import charva.awt.event.ActionEvent;
import charva.awt.event.ActionListener;
import charva.awt.event.KeyEvent;
import charvax.swing.border.Border;
import charvax.swing.event.SwingPropertyChangeSupport;


/**
 * The base class for charva.swing components.
 * <p>
 * JComponent provides a border property that implicitly defines the 
 * component's insets.
 */
public abstract class JComponent extends Container {
    
    /**
     * Constant used for <code>registerKeyboardAction</code> that
     * means that the command should be invoked when
     * the component has the focus.
     */
    public static final int WHEN_FOCUSED = 0;

    /**
     * Constant used for <code>registerKeyboardAction</code> that
     * means that the command should be invoked when the receiving
     * component is an ancestor of the focused component or is
     * itself the focused component.
     */
    public static final int WHEN_ANCESTOR_OF_FOCUSED_COMPONENT = 1;

    /**
     * Constant used for <code>registerKeyboardAction</code> that
     * means that the command should be invoked when
     * the receiving component is in the window that has the focus
     * or is itself the focused component.
     */
    public static final int WHEN_IN_FOCUSED_WINDOW = 2;

    /** Private flags **/
    private static final int FOCUS_INPUTMAP_CREATED     =  1;
    private static final int ANCESTOR_INPUTMAP_CREATED  =  2;
    private static final int WIF_INPUTMAP_CREATED       =  3;
    private static final int ACTIONMAP_CREATED          =  4;
    
    /**
     * An array of <code>KeyStroke</code>s used for
     * <code>WHEN_IN_FOCUSED_WINDOW</code> are stashed
     * in the client properties under this string.
     */
    private static final String WHEN_IN_FOCUSED_WINDOW_BINDINGS = "_WhenInFocusedWindow";

    
    /**
     * Used for <code>WHEN_FOCUSED</code> bindings
     */
    private InputMap                    focusInputMap;

    /**
     * Used for <code>WHEN_ANCESTOR_OF_FOCUSED_COMPONENT</code> bindings
     */
    private InputMap                    ancestorInputMap;

    /**
     * Used for <code>WHEN_IN_FOCUSED_WINDOW</code> bindings
     */
    private ComponentInputMap           windowInputMap;

    private ActionMap                   actionMap;

    private SwingPropertyChangeSupport  changeSupport;
    private Hashtable                   clientProperties;
    private int                         flags;
    
    protected Border                    border;
    
    
    private void setFlag(int aFlag, boolean aValue) {
        if(aValue) {
            flags |= (1 << aFlag);
        } else {
            flags &= ~(1 << aFlag);
        }
    }
    
    private boolean getFlag(int aFlag) {
        int mask = (1 << aFlag);
        return ((flags & mask) == mask);
    }
    
    public void setBorder(Border border) {
        this.border = border;
        invalidate();
        repaint();
    }

    public Border getBorder() {
        return border;
    }

    public Insets getInsets() {
        if (border != null) {
            return border.getBorderInsets(this);
        }
        
        return super.getInsets();
    }

    /**
     * Draws border around this component
     * 
     * @param g  graphics context in which drawing is performed
     */
    protected void paintBorder(Graphics g) {
        if (border != null) {
            ColorPair cp = g.getColor();
            border.paintBorder(this, g, 0, 0, getWidth(), getHeight());
            g.setColor(cp);
        }
    }
    
    public void paint(Graphics g) {
        //	int colorpair = getColorPair();

        // Blank out the area of this component, but only if this
        // component's color-pair is different than that of the
        // parent container.
//        Container parent = getParent();
//        if (parent != null && colorpair != parent.getColorPair())
//            Toolkit.getDefaultToolkit().blankBox(origin, this.getSize(), colorpair);

        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        Rectangle clipRect = g.getClipBounds();
        int clipX;
        int clipY;
        int clipW;
        int clipH;
        if (clipRect == null) {
            clipX = clipY = 0;
            clipW = getWidth();
            clipH = getHeight();
        } else {
            clipX = clipRect.x;
            clipY = clipRect.y;
            clipW = clipRect.width;
            clipH = clipRect.height;
        }

        if (clipW > getWidth()) {
            clipW = getWidth();
        }
        if (clipH > getHeight()) {
            clipH = getHeight();
        }

        if (clipRect == null) {
            g.setClip(clipX, clipY, clipW, clipH);
        }
        
        // draw the border
        paintBorder(g);
        
        paintComponents(g);
    }

    /**
     * Returns the <code>Component</code>'s "visible rect rectangle" - the
     * intersection of the visible rectangles for the component <code>c</code>
     * and all of its ancestors. The return value is stored in
     * <code>visibleRect</code>.
     * 
     * @param c  the component
     * @param visibleRect
     *            a <code>Rectangle</code> computed as the intersection of all
     *            visible rectangles for the component <code>c</code> and all
     *            of its ancestors -- this is the return value for this method
     * @see #getVisibleRect
     */
    static final void computeVisibleRect(Component c, Rectangle visibleRect) {
        Container p = c.getParent();
        Rectangle bounds = c.getBounds();

        if (p == null || p instanceof Window) {
            visibleRect.setBounds(0, 0, bounds.width, bounds.height);
        } else {
            computeVisibleRect(p, visibleRect);
            visibleRect.x -= bounds.x;
            visibleRect.y -= bounds.y;
            SwingUtilities.computeIntersection(0, 0, bounds.width,
                    bounds.height, visibleRect);
        }
    }

    /**
     * Returns the <code>Component</code>'s "visible rect rectangle" - the
     * intersection of the visible rectangles for this component and all of its
     * ancestors. The return value is stored in <code>visibleRect</code>.
     * 
     * @param visibleRect
     *            a <code>Rectangle</code> computed as the intersection of all
     *            visible rectangles for this component and all of its ancestors --
     *            this is the return value for this method
     * @see #getVisibleRect
     */
    public void computeVisibleRect(Rectangle visibleRect) {
        computeVisibleRect(this, visibleRect);
    }

    /**
     * Returns the <code>Component</code>'s "visible rectangle" - the
     * intersection of this component's visible rectangle:
     * 
     * <pre>
     * new Rectangle(0, 0, getWidth(), getHeight());
     * </pre>
     * 
     * and all of its ancestors' visible rectangles.
     * 
     * @return the visible rectangle
     */
    public Rectangle getVisibleRect() {
        Rectangle visibleRect = new Rectangle();
        computeVisibleRect(visibleRect);
        return visibleRect;
    }

    /**
     * Forwards the <code>scrollRectToVisible()</code> message to the
     * <code>JComponent</code>'s parent. Components that can service
     * the request, such as <code>JViewport</code>,
     * override this method and perform the scrolling.
     *
     * @param aRect the visible <code>Rectangle</code>
     * @see JViewport
     */
    public void scrollRectToVisible(Rectangle aRect) {
        Container parent;
        int dx = getX(), dy = getY();

        for (parent = getParent();
                 !(parent == null) && !(parent instanceof JComponent);
                 parent = parent.getParent()) {
            
             Rectangle bounds = parent.getBounds();
             dx += bounds.x;
             dy += bounds.y;
        }

        if (!(parent == null)) {
            aRect.x += dx;
            aRect.y += dy;

            ((JComponent)parent).scrollRectToVisible(aRect);
            aRect.x -= dx;
            aRect.y -= dy;
        }
    }

    /**
     * Returns a <code>HashTable</code> containing all of the key/value
     * "client properties" for this component. If the
     * <code>clientProperties</code> hash table doesn't previously exist, an
     * empty one (of size 2) will be created.
     * 
     * @return a small <code>Hashtable</code>
     * @see #putClientProperty
     * @see #getClientProperty
     */
    private Dictionary getClientProperties() {
        if (clientProperties == null)
            clientProperties = new Hashtable(2);
        
        return clientProperties;
    }

    /**
     * Returns the value of the property with the specified key. 
     * Only properties added with <code>putClientProperty</code> will return a 
     * non-<code>null</code>value.
     * 
     * @param key  the being queried
     * @return the value of this property or <code>null</code>
     * @see #putClientProperty
     */
    public final Object getClientProperty(Object key) {
        if (clientProperties == null)
            return null;

        return getClientProperties().get(key);
    }

    /**
     * Adds an arbitrary key/value "client property" to this component.
     * <p>
     * The <code>get/putClientProperty</code> methods provide access to a
     * small per-instance hashtable. Callers can use get/putClientProperty to
     * annotate components that were created by another module. For example, a
     * layout manager might store per child constraints this way. For example:
     * 
     * <pre>
     * componentA.putClientProperty(&quot;to the left of&quot;, componentB);
     * </pre>
     * 
     * If value is <code>null</code> this method will remove the property.
     * Changes to client properties are reported with
     * <code>PropertyChange</code> events. The name of the property (for the
     * sake of PropertyChange events) is <code>key.toString()</code>.
     * <p>
     * The <code>clientProperty</code> dictionary is not intended to support
     * large scale extensions to JComponent nor should be it considered an
     * alternative to subclassing when designing a new component.
     * 
     * @param key    the new client property key
     * @param value  the new client property value; if <code>null</code> this
     *               method will remove the property
     * 
     * @see #getClientProperty
     */
    public final void putClientProperty(Object key, Object value) {
        Object oldValue = getClientProperties().get(key);

        if (value != null) {
            getClientProperties().put(key, value);
        
        } else if (oldValue != null) {
            getClientProperties().remove(key);
        }
    }

    /**
     * Supports reporting bound property changes.  If <code>oldValue</code>
     * and <code>newValue</code> are not equal and the
     * <code>PropertyChangeEvent</code> listener list isn't empty,
     * then fire a <code>PropertyChange</code> event to each listener.
     * This method has an overloaded method for each primitive type.  For
     * example, here's how to write a bound property set method whose
     * value is an integer:
     * <pre>
     * public void setFoo(int newValue) {
     *     int oldValue = foo;
     *     foo = newValue;
     *     firePropertyChange("foo", oldValue, newValue);
     * }
     * </pre>
     *
     * @param propertyName  the programmatic name of the property
     *      that was changed
     * @param oldValue  the old value of the property (as an Object)
     * @param newValue  the new value of the property (as an Object)
     * @see java.beans.PropertyChangeSupport
     */
    protected void firePropertyChange(String propertyName, Object oldValue, 
            Object newValue) {
        
        if (changeSupport != null)
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Reports a bound property change.
     *
     * @param propertyName the programmatic name of the property
     *      that was changed
     * @param oldValue the old value of the property (as a boolean)
     * @param oldValue the old value of the property (as a boolean)
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, boolean oldValue, 
            boolean newValue) {
        
        if (changeSupport != null && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, 
                    Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    /**
     * Reports a bound property change.
     *
     * @param propertyName the programmatic name of the property
     *      that was changed
     * @param oldValue the old value of the property (as an int)
     * @param newValue the new value of the property (as an int)
     * @see #firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    public void firePropertyChange(String propertyName, int oldValue, 
            int newValue) {
        
        if (changeSupport != null && (oldValue != newValue)) {
            changeSupport.firePropertyChange(propertyName, 
                    SwingUtilities.valueOf(oldValue), 
                    SwingUtilities.valueOf(newValue));
        }
    }

    /**
     * Adds a <code>PropertyChangeListener</code> to the listener list.
     * The listener is registered for all properties.
     * <p>
     * A <code>PropertyChangeEvent</code> will get fired in response
     * to setting a bound property, such as <code>setFont</code>,
     * <code>setBackground</code>, or <code>setForeground</code>.
     * <p>
     * Note that if the current component is inheriting its foreground,
     * background, or font from its container, then no event will be
     * fired in response to a change in the inherited property.
     *
     * @param listener  the <code>PropertyChangeListener</code> to be added
     */
    public synchronized void addPropertyChangeListener(
            PropertyChangeListener listener) {
        
        if (changeSupport == null)
            changeSupport = new SwingPropertyChangeSupport(this);
        
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> from the listener list.
     * This removes a <code>PropertyChangeListener</code> that was registered
     * for all properties.
     *
     * @param listener  the <code>PropertyChangeListener</code> to be removed
     */
    public synchronized void removePropertyChangeListener(
            PropertyChangeListener listener) {
        
        if (changeSupport != null)
            changeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Notifies this component that it now has a parent component. When this
     * method is invoked, the chain of parent components is set up with
     * <code>KeyboardAction</code> event listeners.
     */
    public void addNotify() {
        super.addNotify();

        registerWithKeyboardManager(false);
    }

    /**
     * Notifies this component that it no longer has a parent component. When
     * this method is invoked, any <code>KeyboardAction</code>s set up in the
     * the chain of parent components are removed.
     */
    public void removeNotify() {
        super.removeNotify();

        unregisterWithKeyboardManager();
    }

    /**
     * Registers any bound <code>WHEN_IN_FOCUSED_WINDOW</code> actions with
     * the <code>KeyboardManager</code>. If <code>onlyIfNew</code> is true
     * only actions that haven't been registered are pushed to the
     * <code>KeyboardManager</code>; otherwise all actions are pushed to the
     * <code>KeyboardManager</code>.
     * 
     * @param onlyIfNew
     *            if true, only actions that haven't been registered are pushed
     *            to the <code>KeyboardManager</code>
     */
    private void registerWithKeyboardManager(boolean onlyIfNew) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW, false);
        KeyStroke[] strokes;
        Hashtable registered = (Hashtable) getClientProperty(
                WHEN_IN_FOCUSED_WINDOW_BINDINGS);

        if (inputMap != null) {
            // Push any new KeyStrokes to the KeyboardManager.
            strokes = inputMap.allKeys();
            if (strokes != null) {
                for (int counter = strokes.length - 1; counter >= 0; counter--) {
                    if (!onlyIfNew || registered == null
                            || registered.get(strokes[counter]) == null) {
                        registerWithKeyboardManager(strokes[counter]);
                    }
                    if (registered != null) {
                        registered.remove(strokes[counter]);
                    }
                }
            }
        } else {
            strokes = null;
        }
        // Remove any old ones.
        if (registered != null && registered.size() > 0) {
            Enumeration keys = registered.keys();

            while (keys.hasMoreElements()) {
                KeyStroke ks = (KeyStroke) keys.nextElement();
                unregisterWithKeyboardManager(ks);
            }
            registered.clear();
        }
        // Updated the registered Hashtable.
        if (strokes != null && strokes.length > 0) {
            if (registered == null) {
                registered = new Hashtable(strokes.length);
                putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, registered);
            }
            for (int counter = strokes.length - 1; counter >= 0; counter--) {
                registered.put(strokes[counter], strokes[counter]);
            }
        } else {
            putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, null);
        }
    }

    /**
     * Unregisters all the previously registered
     * <code>WHEN_IN_FOCUSED_WINDOW</code> <code>KeyStroke</code> bindings.
     */
    private void unregisterWithKeyboardManager() {
        Hashtable registered = (Hashtable) getClientProperty(
                WHEN_IN_FOCUSED_WINDOW_BINDINGS);

        if (registered != null && registered.size() > 0) {
            Enumeration keys = registered.keys();

            while (keys.hasMoreElements()) {
                KeyStroke ks = (KeyStroke) keys.nextElement();
                unregisterWithKeyboardManager(ks);
            }
        }
        putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, null);
    }

    /**
     * Invoked from <code>ComponentInputMap</code> when its bindings change.
     * If <code>inputMap</code> is the current <code>windowInputMap</code>
     * (or a parent of the window <code>InputMap</code>) the
     * <code>KeyboardManager</code> is notified of the new bindings.
     * 
     * @param inputMap
     *            the map containing the new bindings
     */
    void componentInputMapChanged(ComponentInputMap inputMap) {
        InputMap km = getInputMap(WHEN_IN_FOCUSED_WINDOW, false);
        while (km != inputMap && km != null) {
            km = (ComponentInputMap) km.getParent();
        }
        
        if (km != null)
            registerWithKeyboardManager(false);
    }

    private void registerWithKeyboardManager(KeyStroke aKeyStroke) {
        KeyboardManager.getCurrentManager().registerKeyStroke(aKeyStroke, this);
    }

    private void unregisterWithKeyboardManager(KeyStroke aKeyStroke) {
        KeyboardManager.getCurrentManager().unregisterKeyStroke(aKeyStroke,
                this);
    }

    /**
     * Processes any key events that the component itself recognizes. This is
     * called after the focus manager and any interested listeners have been
     * given a chance to steal away the event. This method is called only if the
     * event has not yet been consumed. This method is called prior to the
     * keyboard UI logic.
     * <p>
     * This method is implemented to do nothing. Subclasses would normally
     * override this method if they process some key events themselves. If the
     * event is processed, it should be consumed.
     */
    protected void processComponentKeyEvent(KeyEvent e) {
    }

    /**
     * Overrides <code>processKeyEvent</code> to process events.
     */
    protected void processKeyEvent(KeyEvent e) {
        // This gives the key event listeners a crack at the event
        super.processKeyEvent(e);

        // give the component itself a crack at the event
        if (!e.isConsumed())
            processComponentKeyEvent(e);

        if (e.isConsumed())
            return;

        if (processKeyBindings(e, e.getID() == KeyEvent.KEY_PRESSED))
            e.consume();
    }

    /**
     * Invoked to process the key bindings for <code>ks</code> as the result
     * of the <code>KeyEvent</code> <code>e</code>. This obtains the
     * appropriate <code>InputMap</code>, gets the binding, gets the action
     * from the <code>ActionMap</code>, and then (if the action is found and
     * the component is enabled) invokes <code>notifyAction</code> to notify
     * the action.
     * 
     * @param ks         the <code>KeyStroke</code> queried
     * @param e          the <code>KeyEvent</code>
     * @param condition  one of the following values:
     *            <ul>
     *            <li>JComponent.WHEN_FOCUSED
     *            <li>JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     *            <li>JComponent.WHEN_IN_FOCUSED_WINDOW
     *            </ul>
     * @param pressed    true if the key is pressed
     * @return           true if there was a binding to an action, 
     *                   and the action was enabled
     */
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        
        InputMap map = getInputMap(condition, false);
        ActionMap am = getActionMap(false);

        if (map != null && am != null && isEnabled()) {
            Object binding = map.get(ks);
            Action action = (binding == null) ? null : am.get(binding);
            if (action != null) {
                return SwingUtilities.notifyAction(action, ks, e, this, 
                        e.getModifiers());
            }
        }
        return false;
    }

    /**
     * This is invoked as the result of a <code>KeyEvent</code> that was not
     * consumed by the <code>FocusManager</code>, <code>KeyListeners</code>,
     * or the component. It will first try <code>WHEN_FOCUSED</code> bindings,
     * then <code>WHEN_ANCESTOR_OF_FOCUSED_COMPONENT</code> bindings, and
     * finally <code>WHEN_IN_FOCUSED_WINDOW</code> bindings.
     * 
     * @param e        the unconsumed <code>KeyEvent</code>
     * @param pressed  true if the key is pressed
     * @return true if there is a key binding for <code>e</code>
     */
    boolean processKeyBindings(KeyEvent e, boolean pressed) {
        if (!SwingUtilities.isValidKeyEventForKeyBindings(e))
            return false;
        
        // Get the KeyStroke
        KeyStroke ks;

        if (e.getID() == KeyEvent.KEY_TYPED) {
            ks = KeyStroke.getKeyStroke(e.getKeyChar());
        } else {
            ks = KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers(),
                    (pressed ? false : true));
        }

        // Do we have a key binding for e?
        if (processKeyBinding(ks, e, WHEN_FOCUSED, pressed))
            return true;

        // We have no key binding. Let's try the path from our parent to the
        // window excluded. We store the path components so we can avoid asking
        // the same component twice.
        Container parent = this;
        while (parent != null && !(parent instanceof Window)) {
            if (parent instanceof JComponent) {
                if (((JComponent) parent).processKeyBinding(ks, e,
                        WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, pressed)) {
                    
                    return true;
                }
            }
            
            parent = parent.getParent();
        }

        // No components between the focused component and the window is
        // actually interested by the key event. Let's try the other JComponent
        // in this window.
        if (parent != null) {
            return JComponent.processKeyBindingsForAllComponents(e, parent,
                    pressed);
        }
        return false;
    }

    static boolean processKeyBindingsForAllComponents(KeyEvent e,
            Container container, boolean pressed) {
        
        if (KeyboardManager.getCurrentManager().fireKeyboardAction(e,
                pressed, container)) {
            return true;
        }
        
        return false;
    }

    /**
     * Sets the <code>InputMap</code> to use under the condition
     * <code>condition</code> to <code>map</code>. A <code>null</code>
     * value implies you do not want any bindings to be used, even from the UI.
     * This will not reinstall the UI <code>InputMap</code> (if there was
     * one). <code>condition</code> has one of the following values:
     * <ul>
     * <li><code>WHEN_IN_FOCUSED_WINDOW</code>
     * <li><code>WHEN_FOCUSED</code>
     * <li><code>WHEN_ANCESTOR_OF_FOCUSED_COMPONENT</code>
     * </ul>
     * If <code>condition</code> is <code>WHEN_IN_FOCUSED_WINDOW</code> and
     * <code>map</code> is not a <code>ComponentInputMap</code>, an
     * <code>IllegalArgumentException</code> will be thrown. Similarly, if
     * <code>condition</code> is not one of the values listed, an
     * <code>IllegalArgumentException</code> will be thrown.
     * 
     * @param condition one of the values listed above
     * @param map       the <code>InputMap</code> to use for the given condition
     * @exception IllegalArgumentException
     *                if <code>condition</code> is
     *                <code>WHEN_IN_FOCUSED_WINDOW</code> and <code>map</code>
     *                is not an instance of <code>ComponentInputMap</code>;
     *                or if <code>condition</code> is not one of the legal
     *                values specified above
     */
    public final void setInputMap(int condition, InputMap map) {
        switch (condition) {
        case WHEN_IN_FOCUSED_WINDOW:
            if (map != null && !(map instanceof ComponentInputMap)) {
                throw new IllegalArgumentException(
                        "WHEN_IN_FOCUSED_WINDOW InputMaps must be of type ComponentInputMap");
            }
            windowInputMap = (ComponentInputMap) map;
            setFlag(WIF_INPUTMAP_CREATED, true);
            registerWithKeyboardManager(false);
            break;
        case WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
            ancestorInputMap = map;
            setFlag(ANCESTOR_INPUTMAP_CREATED, true);
            break;
        case WHEN_FOCUSED:
            focusInputMap = map;
            setFlag(FOCUS_INPUTMAP_CREATED, true);
            break;
        default:
            throw new IllegalArgumentException(
                    "condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
        }
    }

    /**
     * Returns the <code>InputMap</code> that is used during
     * <code>condition</code>.
     * 
     * @param condition one of WHEN_IN_FOCUSED_WINDOW, WHEN_FOCUSED,
     *            WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     * @return the <code>InputMap</code> for the specified
     *         <code>condition</code>
     */
    public final InputMap getInputMap(int condition) {
        return getInputMap(condition, true);
    }

    /**
     * Returns the <code>InputMap</code> that is used when the component has
     * focus. This is convenience method for
     * <code>getInputMap(WHEN_FOCUSED)</code>.
     * 
     * @return the <code>InputMap</code> used when the component has focus
     */
    public final InputMap getInputMap() {
        return getInputMap(WHEN_FOCUSED, true);
    }

    /**
     * Sets the <code>ActionMap</code> to <code>am</code>. This does not
     * set the parent of the <code>am</code> to be the <code>ActionMap</code>
     * from the UI (if there was one), it is up to the caller to have done this.
     * 
     * @param am  the new <code>ActionMap</code>
     */
    public final void setActionMap(ActionMap am) {
        actionMap = am;
        setFlag(ACTIONMAP_CREATED, true);
    }

    /**
     * Returns the <code>ActionMap</code> used to determine what
     * <code>Action</code> to fire for particular <code>KeyStroke</code>
     * binding. The returned <code>ActionMap</code>, unless otherwise set,
     * will have the <code>ActionMap</code> from the UI set as the parent.
     * 
     * @return the <code>ActionMap</code> containing the key/action bindings
     */
    public final ActionMap getActionMap() {
        return getActionMap(true);
    }

    /**
     * Returns the <code>InputMap</code> to use for condition
     * <code>condition</code>. If the <code>InputMap</code> hasn't been
     * created, and <code>create</code> is true, it will be created.
     * 
     * @param condition
     *            one of the following values:
     *            <ul>
     *            <li>JComponent.FOCUS_INPUTMAP_CREATED
     *            <li>JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     *            <li>JComponent.WHEN_IN_FOCUSED_WINDOW
     *            </ul>
     * @param create
     *            if true, create the <code>InputMap</code> if it is not
     *            already created
     * @return the <code>InputMap</code> for the given <code>condition</code>;
     *         if <code>create</code> is false and the <code>InputMap</code>
     *         hasn't been created, returns <code>null</code>
     * @exception IllegalArgumentException
     *                if <code>condition</code> is not one of the legal values
     *                listed above
     */
    final InputMap getInputMap(int condition, boolean create) {
        switch (condition) {
        case WHEN_FOCUSED:
            if (getFlag(FOCUS_INPUTMAP_CREATED)) {
                return focusInputMap;
            }
            // Hasn't been created yet.
            if (create) {
                InputMap km = new InputMap();
                setInputMap(condition, km);
                return km;
            }
            break;
        case WHEN_ANCESTOR_OF_FOCUSED_COMPONENT:
            if (getFlag(ANCESTOR_INPUTMAP_CREATED)) {
                return ancestorInputMap;
            }
            // Hasn't been created yet.
            if (create) {
                InputMap km = new InputMap();
                setInputMap(condition, km);
                return km;
            }
            break;
        case WHEN_IN_FOCUSED_WINDOW:
            if (getFlag(WIF_INPUTMAP_CREATED)) {
                return windowInputMap;
            }
            // Hasn't been created yet.
            if (create) {
                ComponentInputMap km = new ComponentInputMap(this);
                setInputMap(condition, km);
                return km;
            }
            break;
        
        default:
            throw new IllegalArgumentException(
                    "condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
        }
        return null;
    }

    /**
     * Finds and returns the appropriate <code>ActionMap</code>.
     * 
     * @param create
     *            if true, create the <code>ActionMap</code> if it is not
     *            already created
     * @return the <code>ActionMap</code> for this component; if the
     *         <code>create</code> flag is false and there is no current
     *         <code>ActionMap</code>, returns <code>null</code>
     */
    final ActionMap getActionMap(boolean create) {
        if (getFlag(ACTIONMAP_CREATED))
            return actionMap;
        
        // Hasn't been created.
        if (create) {
            ActionMap am = new ActionMap();
            setActionMap(am);
            return am;
        }
        
        return null;
    }

    /**
     * Returns a string representation of this <code>JComponent</code>.
     * This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JComponent</code>
     */
    protected String paramString() {
        return super.paramString() + ",alignX=" + getAlignmentX() 
                + ",alignY=" + getAlignmentY() 
                + (border != null ? ",border=" + border.toString() : "");
    }


    /**
     * <code>ActionStandin</code> is used as a standin for
     * <code>ActionListeners</code> that are added via
     * <code>registerKeyboardAction</code>.
     */
    final class ActionStandin implements Action {
        
        private final ActionListener    actionListener;
        private final String            command;
        
        // This will be non-null if actionListener is an Action.
        private final Action            action;

        
        ActionStandin(ActionListener actionListener, String command) {
            this.actionListener = actionListener;
            if (actionListener instanceof Action) {
                this.action = (Action) actionListener;
            } else {
                this.action = null;
            }
            this.command = command;
        }

        public Object getValue(String key) {
            if (key != null) {
                if (key.equals(Action.ACTION_COMMAND_KEY)) {
                    return command;
                }
                if (action != null) {
                    return action.getValue(key);
                }
                if (key.equals(NAME)) {
                    return "ActionStandin";
                }
            }
            return null;
        }

        public boolean isEnabled() {
            if (actionListener == null) {
                // This keeps the old semantics where
                // registerKeyboardAction(null) would essentialy remove
                // the binding. We don't remove the binding from the
                // InputMap as that would still allow parent InputMaps
                // bindings to be accessed.
                return false;
            }
            if (action == null) {
                return true;
            }
            return action.isEnabled();
        }

        public void actionPerformed(ActionEvent ae) {
            if (actionListener != null) {
                actionListener.actionPerformed(ae);
            }
        }

        // We don't allow any values to be added.
        public void putValue(String key, Object value) {
        }

        // Does nothing, our enabledness is determiend from our asociated
        // action.
        public void setEnabled(boolean b) {
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }
    }

}
