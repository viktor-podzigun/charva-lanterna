/* class JLabel
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

import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.Insets;
import charva.awt.event.KeyEvent;


/**
 * A display area for a short text string.
 * <p>
 * A label cannot react to keyboard events and cannot receive the keyboard 
 * focus.
 */
public class JLabel extends JComponent implements SwingConstants {

    private String      text;
    private int         horizontalAlignment;

    
    /**
     * Construct an empty label
     */
    public JLabel() {
        this("", LEFT);
    }

    /**
     * Construct a JLabel with the specified text
     */
    public JLabel(String text) {
        this(text, LEFT);
    }

    /**
     * Use this constructor if you want to set the alignment to something
     * other than left-aligned.
     */
    public JLabel(String text, int horizontalAlignment) {
        this.text = text;
        setHorizontalAlignment(horizontalAlignment);
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        switch (horizontalAlignment) {
        case LEFT:      break;
        case CENTER:    break;
        case RIGHT:     break;
            
        default:
            throw new IllegalArgumentException("horizontalAlignment: " 
                    + horizontalAlignment);
        }
    
        this.horizontalAlignment = horizontalAlignment;
    }
    
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    public float getAlignmentX() {
        switch (horizontalAlignment) {
        case LEFT:      return LEFT_ALIGNMENT;
        case CENTER:    return CENTER_ALIGNMENT;
        case RIGHT:     return RIGHT_ALIGNMENT;
            
        default:
            return super.getAlignmentX();
        }
    }
    
    public Dimension getMinimumSize() {
        int textLen = (text == null ? 0 : text.length());
        Insets insets = super.getInsets();
        
        int width  = textLen + insets.left + insets.right;
        int height = 1 + insets.top + insets.bottom;
        
        return new Dimension(width, height);
    }

    public void paint(Graphics g) {
        // draw the border if it exists
        super.paint(g);

        Insets insets = super.getInsets();
        g.translate(insets.left, insets.top);

        int widthleft = 0;
        int textLen = (text == null ? 0 : text.length());
        int width = getWidth();
        if (width > textLen) {
            widthleft = width - textLen;
        }
        
        int hoffset = 0; // left
        switch (horizontalAlignment) {
        case CENTER:
            hoffset = widthleft / 2;
            break;
        case RIGHT:
            hoffset = widthleft;
            break;
        }

        g.setColor(getColor());
        g.drawString(text, hoffset, 0);
    }

    public String getText() {
        return text;
    }

    public void setText(String label) {
        String oldText = this.text;
        text = label;

        if (oldText == null || !oldText.equals(text)) {
            invalidate();
            repaint();
        }
    }

    /**
     * This component will not receive focus when Tab or Shift-Tab is pressed.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * The JLabel class ignores key events. A JLabel should never
     * have input focus anyway.
     */
    protected void processKeyEvent(KeyEvent ke) {
    }

    /**
     * The JLabel component never gets the keyboard input focus.
     */
    public void requestFocus() {
    }

    /**
     * Returns a string representation of this JLabel. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this JLabel.
     */
    protected String paramString() {
        String alignmentStr;
        switch (horizontalAlignment) {
        case LEFT:   alignmentStr = "LEFT";      break;
        case CENTER: alignmentStr = "CENTER";    break;
        case RIGHT:  alignmentStr = "RIGHT";     break;
            
        default:
            alignmentStr = "unknown";
        }
    
        return super.paramString() + ",alignment=" + alignmentStr 
                + ",text=" + text;
    }
}
