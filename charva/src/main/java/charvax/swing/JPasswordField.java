/* class JPasswordField
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

import charva.awt.ColorPair;
import charva.awt.Graphics;
import charva.awt.Insets;


/**
 * JPassword allows the editing of a single line of text; it indicates
 * that something was typed, but does not display the characters that
 * were typed.
 */
public class JPasswordField extends JTextField {

    /**
     * The character that will be set by setEchoChar, and echoed thereafter.
     * Setting this value to 0 indicates that there is no echo-char set.
     */
    private char    echoChar = '*';

    
    public JPasswordField() {
        super("");
    }

    /**
     * Use this constructor when you want to initialize the value.
     */
    public JPasswordField(String text) {
        super(text);
    }

    /**
     * Use this constructor when you want to leave the text field empty
     * but set its length.
     */
    public JPasswordField(int length) {
        super("", length);
    }

    /**
     * Use this constructor when you want to set both the initial value and the
     * length.
     */
    public JPasswordField(String text, int length) {
        super(text, length);
    }

    /**
     * Set the echo character for this password field.
     */
    public void setEchoChar(char echochar) {
        this.echoChar = echochar;
    }

    /**
     * Get the echo character for this text field.
     */
    public char getEchoChar() {
        return echoChar;
    }

    /**
     * Get the flag which indicates whether the echo character
     * has been set.
     */
    public boolean echoCharIsSet() {
        return (echoChar != 0);
    }

    /**
     * Returns the password value as an array of chars.
     */
    public char[] getPassword() {
        return getText().toCharArray();
    }

    public void paint(Graphics g) {
        // draw the border inherited from JComponent, if it exists
        super.paint(g);
        
        Insets insets = getInsets();
        g.translate(insets.left, insets.top);

        ColorPair colorPair;
        if (isEnabled() && isEditable()) {
            colorPair = color;
        } else {
            colorPair = disabledColor;
        }
        
        StringBuffer padbuf = new StringBuffer();
        for (int i = 0; i < columns; i++) {
            padbuf.append(' ');
        }
        
        String padding = padbuf.toString();

        g.setColor(colorPair);
        g.drawString(padding, 0, 0);

        // get the displayable portion of the string
        int end;
        if (document.length() > (offset + columns)) {
            end = offset + columns;
        } else {
            end = document.length();
        }

        // If the echo character is set, display echo characters instead
        // of the actual string
        StringBuffer displaybuf = new StringBuffer();
        if (echoChar != 0) {
            for (int i = 0; i < document.length(); i++) {
                displaybuf.append(echoChar);
            }
        } else {
            for (int i = 0; i < document.length(); i++) {
                displaybuf.append(' ');
            }
        }

        g.drawString(displaybuf.substring(offset, end).toString(), 0, 0);
    }

    /**
     * Returns a string representation of this <code>JPasswordField</code>.
     * This method is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JPasswordField</code>
     */
    protected String paramString() {
        return super.paramString() + ",echoChar=" + echoChar;
    }
}
