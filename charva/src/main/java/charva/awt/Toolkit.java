/* class Toolkit
 *
 * Copyright (C) 2001, 2002, 2003  R M Pitman
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

import java.util.LinkedList;
import java.util.Properties;
import charva.awt.event.FocusEvent;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;


/**
 * The Toolkit class provides the interface to the window system functions.
 */
public abstract class Toolkit {

    private static Toolkit          instance;
    
    /** The terminal's default color-pair    */
    private static ColorPair        defaultColor = ColorPair.valueOf(0x70);
    
    /**
     * Window system normal color scheme
     */
    private static ColorScheme      normalColors = 
        new ColorScheme(new Properties());
    
    /**
     * Window system warning color scheme
     */
    private static ColorScheme      warningColors = 
        new ColorScheme(new Properties());
    
    private static FocusEvent       lastFocusEvent;

    /**
     * A list of visible Windows.  The first in the list is at the bottom, the
     * last is on top.
     */
    private LinkedList              windowList = new LinkedList();

    private EventQueue              evtQueue;
    

    /**
     * The constructor can only be called by the getDefaultToolkit() method,
     * making this an example of the Singleton pattern.
     */
    protected Toolkit() {
        evtQueue = EventQueue.getInstance();
    }

    /**
     * This static method instantiates a Toolkit object if it does not
     * already exist; and returns a reference to the instantiated Toolkit
     */
    public static synchronized Toolkit getDefaultToolkit() {
        if (instance == null) {
            String nm = null;
            Class cls = null;
            try {
                nm = System.getProperty("charva.toolkit");
                try {
                    cls = Class.forName(nm);
                } catch (ClassNotFoundException e) {
                    throw new Error("Toolkit not found: " + nm);
                }
                
                instance = (Toolkit) cls.newInstance();
                
            } catch (InstantiationException e) {
                throw new Error("Could not instantiate Toolkit: " + nm);
            } catch (IllegalAccessException e) {
                throw new Error("Could not access Toolkit: " + nm);
            }
        }
        
        return instance;
    }
    
    protected static synchronized void setDefaultToolkit(Toolkit toolkit) {
        if (instance != null) {
            throw new IllegalStateException("Default toolkit already created");
        }
        
        instance = toolkit;
    }
    
    /**
     * Returns current terminal screen size
     */
    public abstract Dimension getScreenSize();
    
    /**
     * Sets terminal's title.
     * 
     * @param title     new terminal's title
     */
//    public abstract void setTitle(String title);
    
    /**
     * Shows given window on the terminal screen and blocks current execution
     * until window is closed
     * 
     * @param charvaWindow  charva window to be shown
     */
    protected void showWindow(Window charvaWindow) {
        // we need to know the size of the window
        if (charvaWindow.getWidth() == 0 || charvaWindow.getHeight() == 0) {
            charvaWindow.pack();
        }
        
        // ensure it fits inside the screen
        charvaWindow.adjustLocation();
        
        TerminalWindow peer = createWindowPeer(charvaWindow);
        charvaWindow.setPeer(peer);
        peer.show();
    }

    protected abstract TerminalWindow createWindowPeer(Window charvaWindow);

    /**
     * Returns window system normal color scheme
     */
    public static ColorScheme getNormalColors() {
        return normalColors;
    }

    /**
     * Registers window system normal color scheme
     * 
     * @param colors  new color scheme
     */
    public static void setNormalColors(ColorScheme colors) {
        if (colors == null)
            throw new IllegalArgumentException("colors == null");
        
        normalColors = colors;
    }

    /**
     * Returns window system warning color scheme
     */
    public static ColorScheme getWarningColors() {
        return warningColors;
    }

    /**
     * Registers window system warning color scheme
     * 
     * @param colors  new color scheme
     */
    public static void setWarningColors(ColorScheme colors) {
        if (colors == null)
            throw new IllegalArgumentException("colors == null");
        
        warningColors = colors;
    }

    public static ColorPair getDefaultColor() {
        return defaultColor;
    }

    public static void setDefaultColor(ColorPair color) {
        if (color == null)
            throw new IllegalArgumentException("color == null");
        
        defaultColor = color;
    }
    
    public EventQueue getSystemEventQueue() {
        return evtQueue;
    }

    /**
     * Get the top window of the window stack
     */
    public Window getTopWindow() {
        synchronized (windowList) {
            return (Window) windowList.getLast();
        }
    }

    public Window[] getWindows() {
        synchronized (windowList) {
            return (Window[])windowList.toArray(new Window[windowList.size()]);
        }
    }

    /**
     * Processes a keystroke that was pressed in the currently 
     * displayed window
     *
     * @param key  the keystroke that was pressed. If it is less than
     *             256, it is a ASCII or ISO8859-1 code; otherwise 
     *             it is a function key as defined in the "VK_*" values
     */
    public void fireKeystroke(int key) {
        Window source = getTopWindow();
    
        evtQueue.postEvent(new KeyEvent(source, KeyEvent.KEY_PRESSED, 
                key, 0));
        
        if (!KeyEvent.isActionKey(key)) {
            evtQueue.postEvent(new KeyEvent(source, KeyEvent.KEY_TYPED, 
                    key, 0));
        }
    }

    protected void processIdleEvent() {
        Window.doEvents();
    }

    protected void processKeyEvent(Window srcWin, int key, int modifiers) {
        Window.doEvent(new KeyEvent(srcWin, KeyEvent.KEY_PRESSED, 
                key, modifiers));
        
        if (!KeyEvent.isActionKey(key)) {
            Window.doEvent(new KeyEvent(srcWin, KeyEvent.KEY_TYPED, 
                    key, modifiers));
        }
    }

    /**
     * Processes the mouse click event
     */
    protected void processMouseEvent(Window srcWin, int button, int clickCount, 
            int x, int y, int modifiers) {
        
        // x and y must be relative to the source component
        x -= srcWin.getX();
        y -= srcWin.getY();
        
        Window.doEvent(new MouseEvent(srcWin, MouseEvent.MOUSE_PRESSED, 
                x, y, 0, button, modifiers));
        
        Window.doEvent(new MouseEvent(srcWin, MouseEvent.MOUSE_RELEASED, 
                x, y, 0, button, modifiers));
        
        if (clickCount != 2) {
            clickCount = 1;
        }
        
        Window.doEvent(new MouseEvent(srcWin, MouseEvent.MOUSE_CLICKED, 
                x, y, clickCount, button, modifiers));
    }

    FocusEvent getLastFocusEvent() {
        return lastFocusEvent;
    }

    void setLastFocusEvent(FocusEvent ev) {
        lastFocusEvent = ev;
    }

    /**
     * Add a window to the list of displayed windows.
     * Intended to be called by the Window class only.
     */
    void addWindow(Window window) {
        synchronized (windowList) {
            windowList.add(window);
        }
    }

    /**
     * Remove a window from the list of displayed windows.
     * This is intended to be called by the Window object when it hides itself.
     */
    void removeWindow(Window window) {
        synchronized (windowList) {
            if (!windowList.remove(window)) {
                throw new RuntimeException(
                        "Trying to remove window that is not in the window's list: " 
                        + window);
            }
        }
    }

    /**
     * Returns a Vector of all the currently-displayed Windows. Note that the
     * calling thread must synchronize on the returned Vector before using or
     * modifying it, because the window list is accessed by the
     * keyboard-reading thread as well as by the event-dispatching thread.
     */
    LinkedList getWindowList() {
        return windowList;
    }
}
