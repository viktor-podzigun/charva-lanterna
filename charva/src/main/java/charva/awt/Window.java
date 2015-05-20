/* class Window
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

import java.util.Iterator;
import java.util.LinkedList;
import charva.awt.event.AWTEvent;
import charva.awt.event.InvocationEvent;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charva.awt.event.PaintEvent;
import charva.awt.event.PeerEvent;
import charva.awt.event.WindowEvent;
import charva.awt.event.WindowListener;


/**
 * The Window class represents a "toplevel" window with
 * no decorative frame. The window is initially invisible; you must use
 * the show() method to make it visible.
 */
public class Window extends Container {

    //private static final Log LOG = LogFactory.getLog(Window.class);

    private final Window        owner;
    private final Toolkit       toolkit;
    
    private TerminalWindow      peer;
    private LinkedList          windowListeners;
    
    
    public Window(Window owner) {
        this.owner      = owner;
        this.toolkit    = Toolkit.getDefaultToolkit();
        this.layoutMgr  = new BorderLayout();
        this.visible    = false;
    }

    /**
     * Return the Window that is the "owner" of this Window.
     */
    public Window getOwner() {
        return owner;
    }

    /**
     * Sets the location of the window relative to the specified
     * component. If the component is not currently showing,
     * or <code>c</code> is <code>null</code>, the 
     * window is centered on the screen.  If the bottom of the
     * component is offscreen, the window is displayed to the
     * right of the component.
     *
     * @param c  the component in relation to which the window's location
     *           is determined
     */
    public void setLocationRelativeTo(Component c) {
        Container root = null;
        if (c != null) {
            if (c instanceof Window) {
               root = (Container)c;
            } else {
                Container parent = c.getParent();
                for (; parent != null; parent = parent.getParent()) {
                    if (parent instanceof Window) {
                        root = parent;
                        break;
                    }
                }
            }
        }

        if ((c != null && !c.isShowing()) || root == null || !root.isShowing()) {
            Dimension paneSize = getSize();
            Dimension screenSize = toolkit.getScreenSize();
            setLocation((screenSize.width - paneSize.width) / 2,
                        (screenSize.height - paneSize.height) / 2);
        } else {
            Dimension invokerSize = c.getSize();
            Point invokerScreenLocation;

            // If this method is called directly after a call to
            // setLocation() on the "root", getLocationOnScreen()
            // may return stale results, so we walk
            // up the tree to calculate the position instead
            invokerScreenLocation = new Point(0,0);
            Component tc = c;
            while (tc != null) {
                Point tcl = tc.getLocation();
                invokerScreenLocation.x += tcl.x;
                invokerScreenLocation.y += tcl.y;
                if (tc == root) {
                    break;
                }
                tc = tc.getParent();  
            }              

            Rectangle windowBounds = getBounds();
            int dx = invokerScreenLocation.x 
                    + ((invokerSize.width-windowBounds.width) >> 1);
            int dy = invokerScreenLocation.y 
                    + ((invokerSize.height - windowBounds.height) >> 1);
            Dimension ss = toolkit.getScreenSize();

            if (dy + windowBounds.height > ss.height) {
                dy = ss.height-windowBounds.height;
                dx = invokerScreenLocation.x < (ss.width>>1) ? 
                        invokerScreenLocation.x + invokerSize.width :
                            invokerScreenLocation.x - windowBounds.width;
            }
            if (dx + windowBounds.width > ss.width) {
                dx = ss.width-windowBounds.width;
            }
            if (dx < 0) {
                dx = 0;
            }
            if (dy < 0) {
                dy = 0;
            }
            setLocation(dx, dy);
        }
    }
    
    /**
     * Register a WindowListener object for this window.
     */
    public void addWindowListener(WindowListener listener) {
        if (windowListeners == null) {
            windowListeners = new LinkedList();
        }

        windowListeners.add(listener);
    }

    /**
     * Process window events occurring on this window by dispatching them
     * to any registered WindowListener objects.
     */
    protected void processWindowEvent(WindowEvent evt) {
        if (windowListeners == null) {
            return;
        }

        Iterator iter = windowListeners.iterator();
        while (iter.hasNext()) {
            WindowListener wl = (WindowListener) iter.next();
            switch (evt.getID()) {
            case WindowEvent.WINDOW_CLOSING:
                wl.windowClosing(evt);
                break;

            case WindowEvent.WINDOW_OPENED:
                wl.windowOpened(evt);
                break;
            }
        }
    }

    public boolean isDisplayable() {
        return (peer != null);
    }

    /**
     * Causes this Window to be sized to fit the preferred sizes and
     * layouts of its contained components.
     */
    public void pack() {
        setSize(getMinimumSize());
        
        // this will lay out all of the contained components (i.e. children) 
        // and their descendants, and set the isValid flag of all descendants
        validate();
    }
    
    /**
     * Returns absolute cursor position
     */
    public Point getCursor() {
        if (peer == null) {
            throw new IllegalComponentStateException(
                "Peer window is not initialized");
        }
        
        return peer.getCursor();
    }
    
    /**
     * Sets absolute cursor position
     */
    public void setCursor(Point p) {
        if (peer == null) {
            throw new IllegalComponentStateException(
                "Peer window is not initialized");
        }
        
        peer.setCursor(p.x, p.y);
    }

    public boolean hasShadow() {
        return true;
    }
    
    void setPeer(TerminalWindow peer) {
        if (peer == null) {
            throw new NullPointerException("peer");
        }
        
        this.peer = peer;
    }

    void showInternal() {
        LinkedList winlist = toolkit.getWindowList();
        synchronized (winlist) {
            if (peer == null) {
                throw new IllegalComponentStateException(
                    "Peer window is not initialized, probably bug in the "
                        + "TerminalWindow.showWindow() implementation");
            }
            
            visible = true;
            toolkit.addWindow(this);
            addNotify();

            repaint();

            toolkit.getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_OPENED));
        }
    }
    
    /**
     * Lay out the contained components, draw the window and its contained
     * components, and then read input events off the EventQueue and send
     * them to the component that has the input focus.
     */
    public void show() {
        if (visible) {
            return;    // this window is already visible
        }
        
        toolkit.showWindow(this);
    }

    /**
     * Hide this window and all of its contained components.
     * This is done by putting a WINDOW_CLOSING event onto the queue.
     */
    public void hide() {
        if (!visible) {
            //LOG.warn("Trying to hide window, that is already hidden: " + this);
            System.out.println("WARN: Trying to hide window, that is already hidden: " + this);
            return;
        }

        visible = false;
        
        // Post peer close action onto the AWT queue, thus requesting
        // stop processing events in this window
        toolkit.getSystemEventQueue().postEvent(
                new PeerEvent(this, PeerEvent.ACT_CLOSE));
    }

    private static void closeInternal(Window w) {
        LinkedList winlist = w.toolkit.getWindowList();
        synchronized (winlist) {
            if (w != w.toolkit.getTopWindow()) {
                throw new IllegalComponentStateException(
                        "Trying to close window which is not top window: " + w);
            }

            w.removeNotify();
            
            // Remove this window from the list of those displayed
            w.toolkit.removeWindow(w);
            w.peer.close();
            w.peer = null;
            
            if (winlist.size() > 0) {
                Window top = w.toolkit.getTopWindow();
                top.requestFocus();
            
            } else {
                // clear the event queue if no more windows
                EventQueue evtQueue = 
                    Toolkit.getDefaultToolkit().getSystemEventQueue();
                
                while (!evtQueue.isEmpty()) {
                    AWTEvent evt = evtQueue.getNextEvent();
                    try {
                        if (evt instanceof InvocationEvent)
                            ((InvocationEvent) evt).dispatch();
                    
                    } catch (Exception x) {
                        x.printStackTrace();
                    }
                }
                
                evtQueue.clear();
            }
        }
    }
    
    /**
     * Draw all the components in this window, and request the keyboard focus.
     */
    public void paint(Graphics g) {
        super.paint(g);
        requestFocus();
    }

    /**
     * Process an event off the event queue.  This method can be extended by
     * subclasses of Window to deal with application-specific events.
     */
    protected void processEvent(AWTEvent evt) {
        final int id = evt.getID();

        if (id >= KeyEvent.KEY_FIRST && id <= KeyEvent.KEY_LAST) {
            KeyEvent ke = (KeyEvent)evt;
            fireKeyEvent(ke);
            if (ke.isConsumed()) {
                return;
            }
            
            // get the (non-Container) component within the source window
            // that generated the keystroke
            Component source = getCurrentFocus();
            while (source instanceof Container 
                    && ((Container)source).hasChildren()) {
                
                source = ((Container)source).getCurrentFocus();
            }
            
            if (source != null && source != this) {
                source.processEvent(evt);
                return;
            }
        } else if (id >= MouseEvent.MOUSE_FIRST && id <= MouseEvent.MOUSE_LAST) {
            MouseEvent me = (MouseEvent)evt;
            final int   x = me.getX();
            final int   y = me.getY();

            // If the mouse has been pressed outside the top-level window
            if (!contains(x, y)) {
                return;
            }

            fireMouseEvent(me);
            if (me.isConsumed()) {
                return;
            }
            
            Component comp = findComponentAt(x, y);
            if (comp != null && comp != this) {
                Point compLocation = comp.getLocationOnScreen();
                int screenX = x + getX();
                int screenY = y + getY();
                comp.processEvent(
                        new MouseEvent(comp, me.getID(), 
                                screenX - compLocation.x, 
                                screenY - compLocation.y, 
                                me.getClickCount(), me.getButton(), 
                                me.getModifiers()));
                return;
            }
        }

        super.processEvent(evt);
    }

    static void doEvents() {
        //System.out.println("doEvents");
        
        EventQueue evtQueue = 
            Toolkit.getDefaultToolkit().getSystemEventQueue();
        
        boolean next = true;
        while (next && !evtQueue.isEmpty()) {
            next = doEvent(evtQueue.getNextEvent());
        }
    }

    static boolean doEvent(AWTEvent evt) {
        Object source = evt.getSource();
        final int  id = evt.getID();
        
//if (source instanceof Component) {
//    StringBuffer out = new StringBuffer();
//    ((Component)source).list(out, 2);
//    
//    System.out.println("doEvent: " + evt 
//            + "\n  components tree: " + out.toString());
//} else {
//    System.out.println("doEvent: " + evt);
//}

        if (id >= WindowEvent.WINDOW_FIRST && id <= WindowEvent.WINDOW_LAST) {
            // call listeners
            WindowEvent we = (WindowEvent)evt;
            we.getWindow().processWindowEvent(we);
            return true;
        }
        
        Window ancestor;
        
        switch (id) {
        case PaintEvent.PAINT:
            // we have to draw the window rather than just the affected
            // component, because setVisible(false) may have been set on
            // the component
            ancestor = ((Component)source).getAncestorWindow();
            if (ancestor.peer != null && ancestor.isVisible()) {
                LinkedList winlist = ancestor.toolkit.getWindowList();
                synchronized (winlist) {
                    // ignore windows that are stacked below this ancestor
                    Iterator iter = winlist.iterator();
                    boolean belowAncestor = true;
                    while (iter.hasNext()) {
                        Window w = (Window)iter.next();
                        if (w == ancestor) {
                            belowAncestor = false;
                        }

                        // paint all windows above including this ancestor
                        if (!belowAncestor) {
                            w.peer.paint();
                        }
                    }
                }

                // Post peer sync action onto the AWT queue, thus requesting
                // a refresh of the physical screen
                ancestor.toolkit.getSystemEventQueue().postEvent(
                        new PeerEvent(ancestor, PeerEvent.ACT_SYNC));
            }
            break;
            
        case PeerEvent.PEER_EVENT:
            PeerEvent peerEvent = (PeerEvent)evt;
            final int actionId  = peerEvent.getActionId();
            ancestor = ((Component)source).getAncestorWindow();
            if (actionId == PeerEvent.ACT_CLOSE) {
                closeInternal(ancestor);
                
                // stop processing events in the window
                return false;
            }
            
            if (actionId != PeerEvent.ACT_SYNC) {
                throw new RuntimeException("Unknown actionId: " + actionId);
            }
            
            if (ancestor.peer != null && ancestor.isVisible()) {
                ancestor.peer.sync();
            }
            break;

        case InvocationEvent.INVOCATION_DEFAULT:
            try {
                ((InvocationEvent)evt).dispatch();
        
            } catch (Exception x) {
                x.printStackTrace();
            }
            break;
        
        default:
            // It is a KeyEvent, MouseEvent, ItemEvent,
            // FocusEvent or a custom type of event
            ((Component)source).processEvent(evt);
            break;
        }
        
        return true;
    }

    /**
     * Returns the same location as getLocation() method returns.
     */
    public Point getLocationOnScreen() {
        return getLocation();
    }

    /**
     * A Window component will not receive input focus during keyboard focus
     * traversal using Tab and Shift-Tab.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * Adjust the position of the window so that it fits inside the screen.
     */
    public void adjustLocation() {
        Dimension dim = toolkit.getScreenSize();
        int bottom = origin.y + getHeight();
        if (bottom > dim.height) {
            origin.y -= bottom - dim.height;
        }

        if (origin.y < 0) {
            origin.y = 0;
        }

        int right = origin.x + getWidth();
        if (right > dim.width) {
            origin.x -= right - dim.width;
        }

        if (origin.x < 0) {
            origin.x = 0;
        }
    }
}
