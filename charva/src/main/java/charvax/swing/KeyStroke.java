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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import charva.awt.event.InputEvent;
import charva.awt.event.KeyEvent;


/**
 * A KeyStroke represents a key action on the keyboard, or equivalent input
 * device. KeyStrokes can correspond to only a press or release of a particular
 * key, just as KEY_PRESSED and KEY_RELEASED KeyEvents do; alternately, they can
 * correspond to typing a specific Java character, just as KEY_TYPED KeyEvents
 * do. In all cases, KeyStrokes can specify modifiers (alt, shift, control,
 * meta, or a combination thereof) which must be present during the action for
 * an exact match.
 * <p>
 * KeyStrokes are used to define high-level (semantic) action events. Instead of
 * trapping every keystroke and throwing away the ones you are not interested
 * in, those keystrokes you care about automatically initiate actions on the
 * Components with which they are registered.
 * <p>
 * KeyStrokes are immutable, and are intended to be unique. Client code cannot
 * create a KeyStroke; a variant of <code>getKeyStroke</code> must be used
 * instead. These factory methods allow the KeyStroke implementation to cache
 * and share instances efficiently.
 */
public final class KeyStroke {

    private static Map          cache;
    private static KeyStroke    cacheKey;
    private static Map          modifierKeywords;
    
    /**
     * Maps from VK_XXX (as a String) to an Integer. This is done to avoid the
     * overhead of the reflective call to find the constant.
     */
    private static Map          vkMap;

    private char                keyChar = KeyEvent.CHAR_UNDEFINED;
    private int                 keyCode = KeyEvent.VK_UNDEFINED;
    private int                 modifiers;
    private boolean             onKeyRelease;

    
    /**
     * Constructs an <code>KeyStroke</code> with default values. The
     * default values used are: 
     * <table border>
     * <tr><td>Key Char</td><td><code>KeyEvent.CHAR_UNDEFINED</code></td></tr>
     * <tr><td>Key Code</td><td><code>KeyEvent.VK_UNDEFINED</code></td></tr>
     * <tr><td>Modifiers</td><td>none</td></tr>
     * <tr><td>On key release?</td><td><code>false</code></td></tr>
     * </table>
     * 
     * <code>KeyStroke</code>s should not be constructed by client code.
     * Use a variant of <code>getKeyStroke</code> instead.
     * 
     * @see #getKeyStroke
     */
    private KeyStroke() {
    }

    /**
     * Constructs an <code>KeyStroke</code> with the specified values.
     * <code>KeyStroke</code>s should not be constructed by client code.
     * Use a variant of <code>getKeyStroke</code> instead.
     * 
     * @param keyChar    the character value for a keyboard key
     * @param keyCode    the key code for this <code>KeyStroke</code>
     * @param modifiers  a bitwise-ored combination of any modifiers
     * @param onKeyRelease
     *            <code>true</code> if this <code>KeyStroke</code>
     *            corresponds to a key release; <code>false</code> otherwise
     * @see #getKeyStroke
     */
    private KeyStroke(char keyChar, int keyCode, int modifiers, 
            boolean onKeyRelease) {
        
        this.keyChar      = keyChar;
        this.keyCode      = keyCode;
        this.modifiers    = modifiers;
        this.onKeyRelease = onKeyRelease;
    }

    private static synchronized KeyStroke getCachedStroke(char keyChar,
            int keyCode, int modifiers, boolean onKeyRelease) {
        
        if (cache == null) {
            cache = new HashMap();
        }

        if (cacheKey == null) {
            cacheKey = new KeyStroke();
        }
        
        cacheKey.keyChar      = keyChar;
        cacheKey.keyCode      = keyCode;
        cacheKey.modifiers    = modifiers;
        cacheKey.onKeyRelease = onKeyRelease;

        KeyStroke stroke = (KeyStroke) cache.get(cacheKey);
        if (stroke == null) {
            stroke = cacheKey;
            cache.put(stroke, stroke);
            cacheKey = null;
        }

        return stroke;
    }

    /**
     * Returns a shared instance of an <code>KeyStroke</code> that
     * represents a <code>KEY_TYPED</code> event for the specified character.
     * 
     * @param keyChar  the character value for a keyboard key
     * @return an <code>KeyStroke</code> object for that key
     */
    public static KeyStroke getKeyStroke(char keyChar) {
        return getCachedStroke(keyChar, KeyEvent.VK_UNDEFINED, 0, false);
    }

    /**
     * Returns a shared instance of an <code>KeyStroke</code>, given a
     * Character object and a set of modifiers. Note that the first parameter is
     * of type Character rather than char. This is to avoid inadvertent clashes
     * with calls to <code>getKeyStroke(int keyCode, int modifiers)</code>.
     * 
     * @param keyChar    the Character object for a keyboard character
     * @param modifiers  a bitwise-ored combination of any modifiers
     * @return an <code>KeyStroke</code> object for that key
     * @throw IllegalArgumentException if <code>keyChar</code> is
     *        <code>null</code>
     * 
     * @see charva.awt.event.InputEvent
     */
    public static KeyStroke getKeyStroke(Character keyChar, int modifiers) {
        if (keyChar == null) {
            throw new IllegalArgumentException("keyChar cannot be null");
        }
        return getCachedStroke(keyChar.charValue(), KeyEvent.VK_UNDEFINED,
                modifiers, false);
    }

    /**
     * Returns a shared instance of an <code>KeyStroke</code>, given a
     * numeric key code and a set of modifiers, specifying whether the key is
     * activated when it is pressed or released.
     * <p>
     * The "virtual key" constants defined in
     * <code>charva.awt.event.KeyEvent</code> can be used to specify the key
     * code.
     * 
     * @param keyCode    an int specifying the numeric code for a keyboard key
     * @param modifiers  a bitwise-ored combination of any modifiers
     * @param onKeyRelease
     *            <code>true</code> if the <code>KeyStroke</code> should
     *            represent a key release; <code>false</code> otherwise
     * @return an KeyStroke object for that key
     * 
     * @see charva.awt.event.KeyEvent
     * @see charva.awt.event.InputEvent
     */
    public static KeyStroke getKeyStroke(int keyCode, int modifiers,
            boolean onKeyRelease) {
        return getCachedStroke(KeyEvent.CHAR_UNDEFINED, keyCode, modifiers,
                onKeyRelease);
    }

    /**
     * Returns a shared instance of an <code>KeyStroke</code>, given a
     * numeric key code and a set of modifiers. The returned
     * <code>KeyStroke</code> will correspond to a key press.
     * <p>
     * The "virtual key" constants defined in
     * <code>charva.awt.event.KeyEvent</code> can be used to specify the key
     * code.
     * 
     * @param keyCode    an int specifying the numeric code for a keyboard key
     * @param modifiers  a bitwise-ored combination of any modifiers
     * @return an <code>KeyStroke</code> object for that key
     * 
     * @see charva.awt.event.KeyEvent
     * @see charva.awt.event.InputEvent
     */
    public static KeyStroke getKeyStroke(int keyCode, int modifiers) {
        return getCachedStroke(KeyEvent.CHAR_UNDEFINED, keyCode, modifiers,
                false);
    }

    /**
     * Returns an <code>KeyStroke</code> which represents the stroke which
     * generated a given <code>KeyEvent</code>.
     * <p>
     * This method obtains the key char from a <code>KeyTyped</code> event,
     * and the key code from a <code>KeyPressed</code> or
     * <code>KeyReleased</code> event. The <code>KeyEvent</code> modifiers
     * are obtained for all three types of <code>KeyEvent</code>.
     * 
     * @param anEvent
     *            the <code>KeyEvent</code> from which to obtain the
     *            <code>KeyStroke</code>
     * @return the <code>KeyStroke</code> that precipitated the event
     */
    public static KeyStroke getKeyStrokeForEvent(KeyEvent anEvent) {
        int id = anEvent.getID();
        switch (id) {
        case KeyEvent.KEY_PRESSED:
        case KeyEvent.KEY_RELEASED:
            return getCachedStroke(KeyEvent.CHAR_UNDEFINED, 
                    anEvent.getKeyCode(), anEvent.getModifiers(),
                    (id == KeyEvent.KEY_RELEASED));
        case KeyEvent.KEY_TYPED:
            return getCachedStroke(anEvent.getKeyChar(), KeyEvent.VK_UNDEFINED,
                    anEvent.getModifiers(), false);
        default:
            // Invalid ID for this KeyEvent
            return null;
        }
    }

    /**
     * Parses a string and returns an <code>KeyStroke</code>. The string
     * must have the following syntax:
     * 
     * <pre>
     *    &lt;modifiers&gt;* (&lt;typedID&gt; | &lt;pressedReleasedID&gt;)
     * 
     *    modifiers := shift | control | ctrl | meta | alt | button1 | button2 | button3
     *    typedID := typed &lt;typedKey&gt;
     *    typedKey := string of length 1 giving Unicode character.
     *    pressedReleasedID := (pressed | released) key
     *    key := KeyEvent key code name, i.e. the name following &quot;VK_&quot;.
     * </pre>
     * 
     * If typed, pressed or released is not specified, pressed is assumed. Here
     * are some examples:
     * 
     * <pre>
     *     &quot;INSERT&quot; =&gt; getKeyStroke(KeyEvent.VK_INSERT, 0);
     *     &quot;control DELETE&quot; =&gt; getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK);
     *     &quot;alt shift X&quot; =&gt; getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK);
     *     &quot;alt shift released X&quot; =&gt; getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK, true);
     *     &quot;typed a&quot; =&gt; getKeyStroke('a');
     * </pre>
     * 
     * @param s
     *            a String formatted as described above
     * @return an <code>KeyStroke</code> object for that String
     * @throw IllegalArgumentException if <code>s</code> is <code>null</code>,
     *        or is formatted incorrectly
     */
    public static KeyStroke getKeyStroke(String s) {
        if (s == null) {
            throw new IllegalArgumentException("String cannot be null");
        }

        final String errmsg = "String formatted incorrectly";
        StringTokenizer st = new StringTokenizer(s, " ");

        int     mask     = 0;
        boolean released = false;
        boolean typed    = false;
        boolean pressed  = false;

        if (modifierKeywords == null) {
            synchronized (KeyStroke.class) {
                if (modifierKeywords == null) {
                    Map uninitializedMap = new HashMap(8, 1.0f);
                    uninitializedMap.put("shift", SwingUtilities.valueOf(
                            InputEvent.SHIFT_MASK));
                    uninitializedMap.put("control", SwingUtilities.valueOf(
                            InputEvent.CTRL_MASK));
                    uninitializedMap.put("ctrl", SwingUtilities.valueOf(
                            InputEvent.CTRL_MASK));
                    uninitializedMap.put("alt", SwingUtilities.valueOf(
                            InputEvent.ALT_MASK));
                    uninitializedMap.put("button1", SwingUtilities.valueOf(
                            InputEvent.BUTTON1_DOWN_MASK));
                    uninitializedMap.put("button2", SwingUtilities.valueOf(
                            InputEvent.BUTTON2_DOWN_MASK));
                    uninitializedMap.put("button3", SwingUtilities.valueOf(
                            InputEvent.BUTTON3_DOWN_MASK));
                    
                    modifierKeywords = Collections.synchronizedMap(
                            uninitializedMap);
                }
            }
        }

        int count = st.countTokens();

        for (int i = 1; i <= count; i++) {
            String token = st.nextToken();

            if (typed) {
                if (token.length() != 1 || i != count) {
                    throw new IllegalArgumentException(errmsg);
                }
                return getCachedStroke(token.charAt(0), KeyEvent.VK_UNDEFINED,
                        mask, false);
            }

            if (pressed || released || i == count) {
                if (i != count) {
                    throw new IllegalArgumentException(errmsg);
                }

                String keyCodeName = "VK_" + token;
                int keyCode = getVKValue(keyCodeName);

                return getCachedStroke(KeyEvent.CHAR_UNDEFINED, keyCode, mask,
                        released);
            }

            if (token.equals("released")) {
                released = true;
                continue;
            }
            if (token.equals("pressed")) {
                pressed = true;
                continue;
            }
            if (token.equals("typed")) {
                typed = true;
                continue;
            }

            Integer tokenMask = (Integer) modifierKeywords.get(token);
            if (tokenMask != null) {
                mask |= tokenMask.intValue();
            } else {
                throw new IllegalArgumentException(errmsg);
            }
        }

        throw new IllegalArgumentException(errmsg);
    }

    /**
     * Returns the integer constant for the KeyEvent.VK field named
     * <code>key</code>. This will throw an
     * <code>IllegalArgumentException</code> if <code>key</code> is not a
     * valid constant.
     */
    private static int getVKValue(String key) {
        if (vkMap == null) {
            vkMap = Collections.synchronizedMap(new HashMap());
        }

        Integer value = (Integer) vkMap.get(key);

        if (value == null) {
            int keyCode = 0;
            final String errmsg = "String formatted incorrectly";

            try {
                keyCode = KeyEvent.class.getField(key).getInt(KeyEvent.class);
            } catch (NoSuchFieldException nsfe) {
                throw new IllegalArgumentException(errmsg);
            } catch (IllegalAccessException iae) {
                throw new IllegalArgumentException(errmsg);
            }
            
            value = SwingUtilities.valueOf(keyCode);
            vkMap.put(key, value);
        }
        return value.intValue();
    }

    /**
     * Returns the character for this <code>KeyStroke</code>.
     * 
     * @return a char value
     * @see #getKeyStroke(char)
     */
    public final char getKeyChar() {
        return keyChar;
    }

    /**
     * Returns the numeric key code for this <code>KeyStroke</code>.
     * 
     * @return an int containing the key code value
     * @see #getKeyStroke(int,int)
     */
    public final int getKeyCode() {
        return keyCode;
    }

    /**
     * Returns the modifier keys for this <code>KeyStroke</code>.
     * 
     * @return an int containing the modifiers
     * @see #getKeyStroke(int,int)
     */
    public final int getModifiers() {
        return modifiers;
    }

    /**
     * Returns whether this <code>KeyStroke</code> represents a key
     * release.
     * 
     * @return <code>true</code> if this <code>KeyStroke</code>
     *         represents a key release; <code>false</code> otherwise
     * @see #getKeyStroke(int,int,boolean)
     */
    public final boolean isOnKeyRelease() {
        return onKeyRelease;
    }

    /**
     * Returns the type of <code>KeyEvent</code> which corresponds to this
     * <code>KeyStroke</code>.
     * 
     * @return <code>KeyEvent.KEY_PRESSED</code>,
     *         <code>KeyEvent.KEY_TYPED</code>, or
     *         <code>KeyEvent.KEY_RELEASED</code>
     * @see charva.awt.event.KeyEvent
     */
    public final int getKeyEventType() {
        if (keyCode == KeyEvent.VK_UNDEFINED)
            return KeyEvent.KEY_TYPED;
        
        return (onKeyRelease ? KeyEvent.KEY_RELEASED : KeyEvent.KEY_PRESSED);
    }

    /**
     * Returns a numeric value for this object that is likely to be unique,
     * making it a good choice as the index value in a hash table.
     * 
     * @return an int that represents this object
     */
    public int hashCode() {
        return (((int) keyChar) + 1) * (2 * (keyCode + 1)) * (modifiers + 1)
                + (onKeyRelease ? 1 : 2);
    }

    /**
     * Returns true if this object is identical to the specified object.
     * 
     * @param anObject  the Object to compare this object to
     * @return true if the objects are identical
     */
    public final boolean equals(Object anObject) {
        if (anObject instanceof KeyStroke) {
            KeyStroke ks = (KeyStroke) anObject;
            return (ks.keyChar == keyChar && ks.keyCode == keyCode
                    && ks.onKeyRelease == onKeyRelease && ks.modifiers == modifiers);
        }
        return false;
    }

    /**
     * Returns a string that displays and identifies this object's properties.
     * 
     * @return a String representation of this object
     */
    public String toString() {
        if (keyCode == KeyEvent.VK_UNDEFINED) {
            return "keyChar " + KeyEvent.getKeyModifiersText(modifiers)
                    + keyChar;
        } else {
            return "keyCode " + KeyEvent.getKeyModifiersText(modifiers)
                    + KeyEvent.getKeyText(keyCode)
                    + (onKeyRelease ? "-R" : "-P");
        }
    }
}
