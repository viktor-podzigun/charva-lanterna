/* class SwingUtilities
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

import java.lang.reflect.InvocationTargetException;
import charva.awt.Component;
import charva.awt.EventQueue;
import charva.awt.IllegalComponentStateException;
import charva.awt.Point;
import charva.awt.Rectangle;
import charva.awt.Window;
import charva.awt.event.ActionEvent;
import charva.awt.event.KeyEvent;
import charva.awt.event.MouseEvent;
import charvax.swing.plaf.UIResource;


/**
 * A collection of utility methods for Swing
 */
public class SwingUtilities {

    /**
     * Causes the runnable.run() method to be executed asynchronously on the AWT
     * event dispatching thread.
     */
    public static void invokeLater(Runnable runnable) {
        EventQueue.invokeLater(runnable);
    }

    public static void invokeAndWait(final Runnable doRun)
            throws InterruptedException, InvocationTargetException {
        
        EventQueue.invokeAndWait(doRun);
    }

    /**
     * Returns the first ancestor Window of the given component or null if
     * component is not contained inside a Window.
     */
    public static Window windowForComponent(Component c) {
        for (Component p = c; p != null; p = p.getParent()) {
            if (p instanceof Window)
                return (Window)p;
        }
        
        return null;
    }

    /**
     * Returns true if the current thread is the event dispatching thread
     */
    // public static boolean isEventDispatchThread() {
    // return ("event dispatcher".equals(Thread.currentThread().getName()));
    // }

    /**
     * Returns index of the first occurrence of <code>mnemonic</code>
     * within string <code>text</code>. Matching algorithm is not
     * case-sensitive.
     *
     * @param text     the text to search through, may be null
     * @param mnemonic the mnemonic to find the character for
     * @return         index into the string if exists, otherwise -1
     */
    static int findDisplayedMnemonicIndex(String text, int mnemonic) {
        if (text == null || mnemonic == '\0')
            return -1;

        char uc = Character.toUpperCase((char)mnemonic);
        char lc = Character.toLowerCase((char)mnemonic);

        int uci = text.indexOf(uc);
        int lci = text.indexOf(lc);

        if (uci == -1)
            return lci;
        
        if (lci == -1)
            return uci;
        
        return (lci < uci) ? lci : uci;
    }

    /**
     * Cache to avoid Integer objects reallocation for values between 
     * 0 and 255 (inclusive).
     * <p>
     * The cache is initialized on first usage.
     */
    private static class IntegerCache {
        static final int        high;
        static final Integer[]  cache;

        static {
            final int low = 0;
            
            high  = 255;
            cache = new Integer[(high - low) + 1];
            
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);
        }

        private IntegerCache() {}
    }

    /**
     * Returns a <tt>Integer</tt> instance representing the specified
     * <tt>int</tt> value.
     * <p>
     * If a new <tt>Integer</tt> instance is not required, this method
     * should generally be used in preference to the constructor
     * {@link #Integer(int)}, as this method is likely to yield
     * significantly better space and time performance by caching
     * frequently requested values.
     *
     * @param  i an <code>int</code> value
     * @return a <tt>Integer</tt> instance representing <tt>i</tt>
     */
    public static Integer valueOf(int i) {
        if(i >= 0 && i <= IntegerCache.high)
            return IntegerCache.cache[i];
        
        return new Integer(i);
    }

    /**
     * Invokes <code>actionPerformed</code> on <code>action</code> if
     * <code>action</code> is enabled (and non null). The command for the
     * ActionEvent is determined by:
     * <ol>
     *   <li>If the action was registered via
     *       <code>registerKeyboardAction</code>, then the command string
     *       passed in (null will be used if null was passed in).
     *   <li>Action value with name Action.ACTION_COMMAND_KEY, unless null.
     *   <li>String value of the KeyEvent, unless <code>getKeyChar</code>
     *       returns KeyEvent.CHAR_UNDEFINED..
     * </ol>
     * This will return true if <code>action</code> is non-null and
     * actionPerformed is invoked on it.
     */
    public static boolean notifyAction(Action action, KeyStroke ks,
            KeyEvent event, Object sender, int modifiers) {
        
        if (action == null || !action.isEnabled())
            return false;
        
        Object commandO;
        boolean stayNull;

        // Get the command object.
        commandO = action.getValue(Action.ACTION_COMMAND_KEY);
        if (commandO == null && (action instanceof JComponent.ActionStandin)) {
            // ActionStandin is used for historical reasons to support
            // registerKeyboardAction with a null value.
            stayNull = true;
        } else {
            stayNull = false;
        }

        // Convert it to a string.
        String command;

        if (commandO != null) {
            command = commandO.toString();
        } else if (!stayNull && event.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
            command = String.valueOf(event.getKeyChar());
        } else {
            // Do null for undefined chars, or if registerKeyboardAction
            // was called with a null.
            command = null;
        }
        
        action.actionPerformed(new ActionEvent(sender, command, modifiers));
        return true;
    }

    /**
     * Convenience method to change the UI InputMap for <code>component</code>
     * to <code>uiInputMap</code>. If <code>uiInputMap</code> is null, this
     * removes any previously installed UI InputMap.
     */
    public static void replaceUIInputMap(JComponent component, int type,
            InputMap uiInputMap) {
        
        InputMap map = component.getInputMap(type);
        while (map != null) {
            InputMap parent = map.getParent();
            if (parent == null || (parent instanceof UIResource)) {
                map.setParent(uiInputMap);
                return;
            }
            
            map = parent;
        }
    }

    /**
     * Convenience method to change the UI ActionMap for <code>component</code>
     * to <code>uiActionMap</code>. If <code>uiActionMap</code> is null,
     * this removes any previously installed UI ActionMap.
     */
    public static void replaceUIActionMap(JComponent component,
            ActionMap uiActionMap) {
        
        ActionMap map = component.getActionMap();
        while (map != null) {
            ActionMap parent = map.getParent();
            if (parent == null || (parent instanceof UIResource)) {
                map.setParent(uiActionMap);
                return;
            }
            
            map = parent;
        }
    }

    /**
     * Returns true if the <code>e</code> is a valid KeyEvent to use in
     * processing the key bindings associated with JComponents.
     */
    static boolean isValidKeyEventForKeyBindings(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
            int mod = e.getModifiers();
            if (((mod & ActionEvent.ALT_MASK) != 0) &&
                ((mod & ActionEvent.CTRL_MASK) == 0)) {
                // filter out typed "alt-?" keys, but not those created
                // with AltGr, and not control characters
                return false;
            }
        }
        return true;
    }

    /**
     * Convenience to calculate the intersection of two rectangles
     * without allocating a new rectangle.
     * If the two rectangles don't intersect, 
     * then the returned rectangle begins at (0,0)
     * and has zero width and height.
     *
     * @param x       the X coordinate of the first rectangle's top-left point
     * @param y       the Y coordinate of the first rectangle's top-left point
     * @param width   the width of the first rectangle
     * @param height  the height of the first rectangle
     * @param dest    the second rectangle
     *
     * @return <code>dest</code>, modified to specify the intersection
     */
    public static Rectangle computeIntersection(int x, int y, int width, 
            int height, Rectangle dest) {
        
        int x1 = (x > dest.x) ? x : dest.x;
        int x2 = ((x+width) < (dest.x + dest.width)) ? 
                (x+width) : (dest.x + dest.width);
        
        int y1 = (y > dest.y) ? y : dest.y;
        int y2 = ((y + height) < (dest.y + dest.height) ? 
                (y+height) : (dest.y + dest.height));

        dest.x      = x1;
        dest.y      = y1;
        dest.width  = x2 - x1;
        dest.height = y2 - y1;

        // If rectangles don't intersect, return zero'd intersection.
        if (dest.width < 0 || dest.height < 0)
            dest.x = dest.y = dest.width = dest.height = 0;

        return dest;
    }

    /**
     * Convert a point from a component's coordinate system to
     * screen coordinates.
     *
     * @param p  a Point object (converted to the new coordinate system)
     * @param c  a Component object
     */
    public static void convertPointToScreen(Point p, Component c) {
        int x, y;

        do {
            if (c instanceof JComponent) {
                x = ((JComponent) c).getX();
                y = ((JComponent) c).getY();
            
            } else if (c instanceof Window) {
                try {
                    Point pp = c.getLocationOnScreen();
                    x = pp.x;
                    y = pp.y;
                } catch (IllegalComponentStateException icse) {
                    x = c.getX();
                    y = c.getY();
                }
            } else {
                x = c.getX();
                y = c.getY();
            }

            p.x += x;
            p.y += y;

            if (c instanceof Window)
                break;
            
            c = c.getParent();
        
        } while (c != null);
    }

    /**
     * Convert a point from a screen coordinates to a component's 
     * coordinate system
     *
     * @param p  a Point object (converted to the new coordinate system)
     * @param c  a Component object
     */
    public static void convertPointFromScreen(Point p, Component c) {
        int x, y;

        do {
            if (c instanceof JComponent) {
                x = ((JComponent) c).getX();
                y = ((JComponent) c).getY();
            
            } else if (c instanceof Window) {
                try {
                    Point pp = c.getLocationOnScreen();
                    x = pp.x;
                    y = pp.y;
                } catch (IllegalComponentStateException icse) {
                    x = c.getX();
                    y = c.getY();
                }
            } else {
                x = c.getX();
                y = c.getY();
            }

            p.x -= x;
            p.y -= y;

            if (c instanceof Window)
                break;
            
            c = c.getParent();
        
        } while (c != null);
    }

    /**
     * Convert a <code>aPoint</code> in <code>source</code> coordinate
     * system to <code>destination</code> coordinate system. If
     * <code>source></code>is null,<code>aPoint</code> is assumed to be in
     * <code>destination</code>'s root component coordinate system. If
     * <code>destination</code>is null, <code>aPoint</code> will be
     * converted to <code>source</code>'s root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are null,
     * return <code>aPoint</code> without any conversion.
     */
    public static Point convertPoint(Component source, Point aPoint,
            Component destination) {
        
        if (source == null && destination == null)
            return aPoint;
        
        return convertPoint(source, aPoint.x, aPoint.y, destination);
    }

    /**
     * Convert the point <code>(x,y)</code> in <code>source</code>
     * coordinate system to <code>destination</code> coordinate system. If
     * <code>source></code>is null,<code>(x,y)</code> is assumed to be in
     * <code>destination</code>'s root component coordinate system. If
     * <code>destination</code>is null, <code>(x,y)</code> will be
     * converted to <code>source</code>'s root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are null,
     * return <code>(x,y)</code> without any conversion.
     */
    public static Point convertPoint(Component source, int x, int y,
            Component destination) {
        
        Point p = new Point(x, y);
        if (source == null && destination == null)
            return p;
        
        if (source == null) {
            source = windowForComponent(destination);
            if (source == null) {
                throw new Error(
                    "Source component not connected to component tree hierarchy");
            }
        }
        
        convertPointToScreen(p, source);
        if (destination == null) {
            destination = windowForComponent(source);
            if (destination == null) {
                throw new Error(
                    "Destination component not connected to component tree hierarchy");
            }
        }
        
        convertPointFromScreen(p, destination);
        return p;
    }

    /**
     * Convert the rectangle <code>aRectangle</code> in <code>source</code>
     * coordinate system to <code>destination</code> coordinate system. If
     * <code>source></code>is null,<code>aRectangle</code> is assumed to
     * be in <code>destination</code>'s root component coordinate system. If
     * <code>destination</code>is null, <code>aRectangle</code> will be
     * converted to <code>source</code>'s root component coordinate system.
     * If both <code>source</code> and <code>destination</code> are null,
     * return <code>aRectangle</code> without any conversion.
     */
    public static Rectangle convertRectangle(Component source,
            Rectangle aRectangle, Component destination) {
        
        Point point = convertPoint(source, 
                aRectangle.x, aRectangle.y, destination);
        return new Rectangle(point.x, point.y, aRectangle.width,
                aRectangle.height);
    }

    /**
     * Returns true if the mouse event specifies the left mouse button.
     *
     * @param anEvent  a MouseEvent object
     * @return true if the left mouse button was active
     */
    public static boolean isLeftMouseButton(MouseEvent anEvent) {
//        return ((anEvent.getModifiers() 
//                & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK);
        return (anEvent.getButton() == MouseEvent.BUTTON1);
    }

    /**
     * Returns true if the mouse event specifies the middle mouse button.
     * 
     * @param anEvent
     *            a MouseEvent object
     * @return true if the middle mouse button was active
     */
    public static boolean isMiddleMouseButton(MouseEvent anEvent) {
//        return ((anEvent.getModifiers() 
//                & InputEvent.BUTTON2_DOWN_MASK) == InputEvent.BUTTON2_DOWN_MASK);
        return (anEvent.getButton() == MouseEvent.BUTTON2);
    }

    /**
     * Returns true if the mouse event specifies the right mouse button.
     *
     * @param anEvent  a MouseEvent object
     * @return true if the right mouse button was active
     */
    public static boolean isRightMouseButton(MouseEvent anEvent) {
//        return ((anEvent.getModifiers() 
//                & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK);
        return (anEvent.getButton() == MouseEvent.BUTTON3);
    }
}
