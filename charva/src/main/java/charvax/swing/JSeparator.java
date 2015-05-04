/* class JSeparator
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
import charva.awt.Container;
import charva.awt.Dimension;
import charva.awt.Graphics;
import charva.awt.GraphicsConstants;
import charva.awt.Insets;
import charvax.swing.border.Border;
import charvax.swing.border.DoubleLineBorder;
import charvax.swing.border.LineBorder;


/**
 * <code>JSeparator</code> provides a general purpose component for
 * implementing divider lines - most commonly used as a divider
 * between menu items that breaks them up into logical groupings.
 * Instead of using <code>JSeparator</code> directly,
 * you can use the <code>JMenu</code> or <code>JPopupMenu</code>
 * <code>addSeparator</code> method to create and add a separator.
 * <code>JSeparator</code>s may also be used elsewhere in a GUI
 * wherever a visual divider is useful.
 */
public class JSeparator extends JComponent implements SwingConstants {
    
    private int orientation = HORIZONTAL;
    
    
    /**
     * Creates a new horizontal separator
     */
    public JSeparator() {
        this(HORIZONTAL);
    }

    /**
     * Creates a new separator with the specified horizontal or
     * vertical orientation 
     *
     * @param orientation an integer specifying
     *      <code>SwingConstants.HORIZONTAL</code> or
     *          <code>SwingConstants.VERTICAL</code>
     * @exception IllegalArgumentException if <code>orientation</code>
     *      is neither <code>SwingConstants.HORIZONTAL</code> nor
     *      <code>SwingConstants.VERTICAL</code>
     */
    public JSeparator(int orientation) {
        checkOrientation(orientation);
        this.orientation = orientation;
    }

    /**
     * Returns the orientation of this separator
     * 
     * @return The value of the orientation property, one of the following
     *         constants defined in <code>SwingConstants</code>:
     *         <code>VERTICAL</code>, or <code>HORIZONTAL</code>
     * 
     * @see SwingConstants
     * @see #setOrientation
     */
    public int getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation of the separator. The default value of this property
     * is HORIZONTAL
     * 
     * @param orientation
     *            either <code>SwingConstants.HORIZONTAL</code> or
     *            <code>SwingConstants.VERTICAL</code>
     * @exception IllegalArgumentException
     *                if <code>orientation</code> is neither
     *                <code>SwingConstants.HORIZONTAL</code> nor
     *                <code>SwingConstants.VERTICAL</code>
     * 
     * @see SwingConstants
     * @see #getOrientation
     */
    public void setOrientation(int orientation) {
        if (this.orientation == orientation) {
            return;
        }
        
        checkOrientation(orientation);
        this.orientation = orientation;
        
        invalidate();
        repaint();
    }

    private void checkOrientation(int orientation) {
        switch (orientation) {
        case VERTICAL:
        case HORIZONTAL:
            break;
        
        default:
            throw new IllegalArgumentException(
                    "orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    public void paint(Graphics g) {
        ColorPair colorPair = getColor();
        Container parent    = getParent();
        if (parent != null) {
            colorPair = parent.getColor();
        }
        
        boolean hasBorder          = false;
        boolean isDoubleLineBorder = false;
        Border  parentBorder       = getParentBorder();
        
        if (parentBorder instanceof LineBorder) {
            hasBorder              = true;
            isDoubleLineBorder     = false;
        
        } else if (parentBorder instanceof DoubleLineBorder) {
            hasBorder              = true;
            isDoubleLineBorder     = true;
        }
        
        g.setColor(colorPair);
        
        if (orientation == HORIZONTAL) {
            if (hasBorder) {
                int len = getSize().width;
                g.drawChar(isDoubleLineBorder ? GraphicsConstants.VS_DBL_LSEP 
                        : GraphicsConstants.VS_LTEE, -1, 0);
                g.drawHLine(0, 0, len, GraphicsConstants.VS_HLINE);
                g.drawChar(isDoubleLineBorder ? GraphicsConstants.VS_DBL_RSEP 
                        : GraphicsConstants.VS_RTEE, len, 0);
            } else {
                g.drawHLine(0, 0, getSize().width, GraphicsConstants.VS_HLINE);
            }
        } else {
            if (hasBorder) {
                int len = getSize().height;
                g.drawChar(isDoubleLineBorder ? GraphicsConstants.VS_DBL_TSEP 
                        : GraphicsConstants.VS_TTEE, 0, -1);
                g.drawVLine(0, 0, len, GraphicsConstants.VS_VLINE);
                g.drawChar(isDoubleLineBorder ? GraphicsConstants.VS_DBL_BSEP 
                        : GraphicsConstants.VS_BTEE, 0, len);
            } else {
                g.drawVLine(0, 0, getSize().height, GraphicsConstants.VS_VLINE);
            }
        }
    }
    
    private Border getParentBorder() {
        Container parent = getParent();
        if (parent instanceof JComponent) {
            return ((JComponent)parent).getBorder();
        }
    
        if (parent instanceof JDialog) {
            return ((JDialog)parent).getBorder();
        }
    
        if (parent instanceof JFrame) {
            return ((JFrame)parent).getBorder();
        }
        
        return null;
    }

    /**
     * Determines whether this component will accept the keyboard focus
     * during keyboard traversal.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * This is never invoked.
     */
    public void requestFocus() {
    }

    public Dimension getMinimumSize() {
        return getSize();
    }

    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public int getWidth() {
        Container parent = getParent();
        Insets insets = parent.getInsets();
        return parent.getSize().width - insets.left - insets.right;
    }

    public int getHeight() {
        return 1;
    }

    /**
     * Returns a string representation of this <code>JSeparator</code>.
     * This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this <code>JSeparator</code>
     */
    protected String paramString() {
        String orientationString = (orientation == HORIZONTAL ? "HORIZONTAL"
                : "VERTICAL");

        return super.paramString() + ",orientation=" + orientationString;
    }
}
