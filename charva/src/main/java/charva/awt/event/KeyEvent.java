/* class KeyEvent
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

package charva.awt.event;

import charva.awt.Component;


/**
 * An event which indicates that a keystroke occurred in an object.
 */
public class KeyEvent extends InputEvent {

    private static final long serialVersionUID = 2133533343508928128L;
    
    public static final int KEY_FIRST          = 400;
    public static final int KEY_LAST           = 402;
    public static final int KEY_TYPED          = KEY_FIRST;
    public static final int KEY_PRESSED        = 1 + KEY_FIRST;
    public static final int KEY_RELEASED       = 2 + KEY_FIRST;

    
    private int keyCode;

    
    public KeyEvent(Component source, int id, int key, int modifiers) {
        super(source, id, modifiers);
        this.keyCode = key;
    }

    /**
     * Returns the integer code for an actual key on the keyboard
     */
    public int getKeyCode() {
        return keyCode;
    }

    /**
     * Returns the character associated with the key in this event.
     * <p>
     * For example, the key-typed event for shift + "a" returns the
     * value for "A".
     *
     * @return the character defined for this key event.
     *         If no valid character exists for this key event, 
     *         <code>CHAR_UNDEFINED</code> is returned
     */
    public char getKeyChar() {
        char c = (char)keyCode;
        if (!Character.isDefined(c))
            return CHAR_UNDEFINED;
        
        return c;
    }

    /**
     * Set the key code to indicate a physical key was pressed
     */
    public void setKeyCode(int key) {
        this.keyCode = key;
    }

    /**
     * Set the keyChar value to indicate a logical character.
     *
     * @param keyChar a char corresponding to to the combination of keystrokes
     *                that make up this event.
     */
    public void setKeyChar(char keyChar) {
        this.keyCode = (int) keyChar;
    }

    /**
     * Returns true if the key is a function key or control key.
     */
    public boolean isActionKey() {
        return isActionKey(keyCode);
    }

    /**
     * Returns a parameter string identifying this event.
     * This method is useful for event logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String typeStr;
        switch (id) {
        case KEY_PRESSED:
            typeStr = "KEY_PRESSED";
            break;
        case KEY_TYPED:
            typeStr = "KEY_TYPED";
            break;
        default:
            typeStr = "unknown type";
        }

        String str = typeStr + ",keyCode=" + getKeyText(keyCode);
        if (modifiers != 0)
            str += ",modifiers=" + getKeyModifiersText(modifiers);

        return str;
    }

    /**
     * Convert the integer representation of a keystroke to an ASCII string.
     */
    public static String getKeyText(int key) {
        StringBuffer buf = new StringBuffer();

        if (key < VK_SPACE) {
            buf.append("^");
            buf.append((char) (key + 0x40));
        
        } else if (key == VK_SPACE) { buf.append("SPACE");
        } else if (key < 0x7f)      { buf.append((char) key);
        } else {
            switch (key) {
            case VK_DOWN:       buf.append("VK_DOWN");          break;
            case VK_UP:         buf.append("VK_UP");            break;
            case VK_LEFT:       buf.append("VK_LEFT");          break;
            case VK_RIGHT:      buf.append("VK_RIGHT");         break;
            case VK_HOME:       buf.append("VK_HOME");          break;
            case VK_BACK_SPACE: buf.append("VK_BACK_SPACE");    break;
            case VK_DELETE:     buf.append("VK_DELETE");        break;
            case VK_INSERT:     buf.append("VK_INSERT");        break;
            case VK_PAGE_DOWN:  buf.append("VK_PAGE_DOWN");     break;
            case VK_PAGE_UP:    buf.append("VK_PAGE_UP");       break;
            case VK_ENTER:      buf.append("VK_ENTER");         break;
            case VK_END:        buf.append("VK_END");           break;
            
            case VK_F1:
            case VK_F2:
            case VK_F3:
            case VK_F4:
            case VK_F5:
            case VK_F6:
            case VK_F7:
            case VK_F8:
            case VK_F9:
            case VK_F10:
            case VK_F11:
            case VK_F12:
            case VK_F13:
            case VK_F14:
            case VK_F15:
            case VK_F16:
            case VK_F17:
            case VK_F18:
            case VK_F19:
            case VK_F20:
                buf.append("VK_F");
                buf.append((int)(1 + key - KeyEvent.VK_F1));
                break;
        
            default:
                buf.append(Integer.toHexString(key));
                break;
            }
        }

        return buf.toString();
    }

    /**
     * Returns true if the key is a function key or cursor key.
     */
    public static boolean isActionKey(int key) {
        if (key < VK_SPACE)
            return true;
        
        switch (key) {
        case VK_ESCAPE:
        case VK_DOWN:
        case VK_UP:
        case VK_LEFT:
        case VK_RIGHT:
        case VK_BACK_SPACE:
        case VK_F1:
        case VK_F2:
        case VK_F3:
        case VK_F4:
        case VK_F5:
        case VK_F6:
        case VK_F7:
        case VK_F8:
        case VK_F9:
        case VK_F10:
        case VK_F11:
        case VK_F12:
        case VK_F13:
        case VK_F14:
        case VK_F15:
        case VK_F16:
        case VK_F17:
        case VK_F18:
        case VK_F19:
        case VK_F20:
        case VK_DELETE:
        case VK_INSERT:
        case VK_PAGE_DOWN:
        case VK_PAGE_UP:
        case VK_ENTER:
        case VK_HOME:
        case VK_END:
            return true;
        }
            
        return false;
    }

    /**
     * KEY_PRESSED events that don't map to a valid character
     * cause the keyPressed() method to return this value.
     */
    public static final char CHAR_UNDEFINED   = (char) -1;

    public static final int  VK_UNDEFINED     = -1;
    public static final int  VK_ESCAPE        = 0x1b;
    public static final int  VK_TAB           = '\t';

    // Unicode safe area... Unicode Private Use Area on 0xE000 position

    public static final int  VK_DOWN          = 0xe000;
    public static final int  VK_UP            = 0xe001;
    public static final int  VK_LEFT          = 0xe002;
    public static final int  VK_RIGHT         = 0xe003;
    public static final int  VK_HOME          = 0xe004;
    public static final int  VK_BACK_SPACE    = 0xe005;
    public static final int  VK_F1            = 0xe006;
    public static final int  VK_F2            = 0xe007;
    public static final int  VK_F3            = 0xe008;
    public static final int  VK_F4            = 0xe009;
    public static final int  VK_F5            = 0xe00a;
    public static final int  VK_F6            = 0xe00b;
    public static final int  VK_F7            = 0xe00c;
    public static final int  VK_F8            = 0xe00d;
    public static final int  VK_F9            = 0xe00e;
    public static final int  VK_F10           = 0xe00f;
    public static final int  VK_F11           = 0xe010;
    public static final int  VK_F12           = 0xe011;
    public static final int  VK_F13           = 0xe012;
    public static final int  VK_F14           = 0xe013;
    public static final int  VK_F15           = 0xe014;
    public static final int  VK_F16           = 0xe015;
    public static final int  VK_F17           = 0xe016;
    public static final int  VK_F18           = 0xe017;
    public static final int  VK_F19           = 0xe018;
    public static final int  VK_F20           = 0xe019;
    public static final int  VK_DELETE        = 0xe01a;
    public static final int  VK_INSERT        = 0xe01b;
    public static final int  VK_PAGE_DOWN     = 0xe01c;
    public static final int  VK_PAGE_UP       = 0xe01d;
    public static final int  VK_ENTER         = 0xe01e;
    public static final int  VK_END           = 0xe020;

// Unicode safe area... END

    //
    // other ASCII characters
    //
    
    public static final int  VK_SPACE         = 0x20;
    public static final int  VK_COMMA         = 0x2C;
    public static final int  VK_MINUS         = 0x2D;
    public static final int  VK_PERIOD        = 0x2E;
    public static final int  VK_SLASH         = 0x2F;
    public static final int  VK_SEMICOLON     = 0x3B;
    public static final int  VK_EQUALS        = 0x3D;

    public static final int  VK_OPEN_BRACKET  = 0x5B;
    public static final int  VK_BACK_SLASH    = 0x5C;
    public static final int  VK_CLOSE_BRACKET = 0x5D;
    
    //
    // VK_0 thru VK_9 are the same as ASCII '0' thru '9' (0x30 - 0x39)
    //
    
    public static final int  VK_0             = 0x30;
    public static final int  VK_1             = 0x31;
    public static final int  VK_2             = 0x32;
    public static final int  VK_3             = 0x33;
    public static final int  VK_4             = 0x34;
    public static final int  VK_5             = 0x35;
    public static final int  VK_6             = 0x36;
    public static final int  VK_7             = 0x37;
    public static final int  VK_8             = 0x38;
    public static final int  VK_9             = 0x39;

    //
    // VK_A thru VK_Z are the same as ASCII 'A' thru 'Z' (0x41 - 0x5A)
    //
    
    public static final int  VK_A             = 0x41;
    public static final int  VK_B             = 0x42;
    public static final int  VK_C             = 0x43;
    public static final int  VK_D             = 0x44;
    public static final int  VK_E             = 0x45;
    public static final int  VK_F             = 0x46;
    public static final int  VK_G             = 0x47;
    public static final int  VK_H             = 0x48;
    public static final int  VK_I             = 0x49;
    public static final int  VK_J             = 0x4A;
    public static final int  VK_K             = 0x4B;
    public static final int  VK_L             = 0x4C;
    public static final int  VK_M             = 0x4D;
    public static final int  VK_N             = 0x4E;
    public static final int  VK_O             = 0x4F;
    public static final int  VK_P             = 0x50;
    public static final int  VK_Q             = 0x51;
    public static final int  VK_R             = 0x52;
    public static final int  VK_S             = 0x53;
    public static final int  VK_T             = 0x54;
    public static final int  VK_U             = 0x55;
    public static final int  VK_V             = 0x56;
    public static final int  VK_W             = 0x57;
    public static final int  VK_X             = 0x58;
    public static final int  VK_Y             = 0x59;
    public static final int  VK_Z             = 0x5A;
}
