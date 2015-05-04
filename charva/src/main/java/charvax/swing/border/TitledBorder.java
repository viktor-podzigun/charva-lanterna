/* class TitledBorder
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

package charvax.swing.border;

import charva.awt.ColorPair;
import charva.awt.Component;
import charva.awt.Graphics;
import charva.awt.Insets;


/**
 * A class that represents a border with the specified title
 */
public class TitledBorder implements Border {

    /** Position the title in the middle of the border's top line       */
    public static final int     TOP     = 1;
    
    /** Position the title in the middle of the border's bottom line    */
    public static final int     BOTTOM  = 2;
    
    
    /** Position title text at the left side of the border line         */
    public static final int     LEFT    = 100;
    
    /** Position title text in the center of the border line            */
    public static final int     CENTER  = 101;
    
    /** Position title text at the right side of the border line        */
    public static final int     RIGHT   = 102;

    
    protected Border            border;
    protected String            title;
    protected ColorPair         titleColor;
    protected int               titlePosition;
    protected int               titleJustification;

    
    /**
     * Create a titled border with the specified border and an
     * empty title.
     */
    public TitledBorder(Border border) {
        this(border, "", LEFT, TOP, null);
    }

    /**
     * Create a TitledBorder instance with a line border and
     * the specified title, with the title text in black.
     */
    public TitledBorder(String title) {
        this(new LineBorder(null), title, LEFT, TOP, null);
    }

    /**
     * Create a TitledBorder instance with the specified border
     * and title, and with the title text in black.
     */
    public TitledBorder(Border border, String title) {
        this(border, title, LEFT, TOP, null);
    }

    public TitledBorder(Border border, String title, 
                        int titleJustification, int titlePosition) {
        
        this(border, title, titleJustification, titlePosition, null);
    }

    /**
     * Creates a TitledBorder instance with the specified border,
     * title, title-justification, title-position, title-font,
     * and title-color.
     *
     * @param border             the border
     * @param title              the title the border should display
     * @param titleJustification the title justification
     * @param titlePosition      the position of the title
     * @param titleColor         the title color
     */
    public TitledBorder(Border border, String title,
                        int titleJustification, int titlePosition,
                        ColorPair titleColor) {
        
        this.border     = border;
        this.title      = title;
        this.titleColor = titleColor;
    
        setTitleJustification(titleJustification);
        setTitlePosition(titlePosition);
    }

    public Insets getBorderInsets(Component component) {
        return border.getBorderInsets(component);
    }

    /**
     * Sets the title text
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the title text
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the color of the title text
     */
    public void setTitleColor(ColorPair titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * Returns the color of the title text
     */
    public ColorPair getTitleColor() {
        return titleColor;
    }

    /**
     * Sets the border of this titled border
     */
    public void setBorder(Border border) {
        this.border = border;
    }

    /**
     * Returns the border of the titled border
     */
    public Border getBorder() {
        return border;
    }

    /**
     * Returns the title-position of the titled border
     */
    public int getTitlePosition() {
        return titlePosition;
    }

    /**
     * Returns the title-justification of the titled border
     */
    public int getTitleJustification() {
        return titleJustification;
    }

    /**
     * Sets the title-position of the titled border.
     * @param titlePosition the position for the border
     */
    public void setTitlePosition(int titlePosition) {
        switch (titlePosition) {
        case TOP:
        case BOTTOM:
            this.titlePosition = titlePosition;
            break;
        
        default:
            throw new IllegalArgumentException(titlePosition + 
                    " is not a valid title position");
        }
    }

    /**
     * Sets the title-justification of the titled border
     * 
     * @param titleJustification the justification for the border
     */
    public void setTitleJustification(int titleJustification)       {
        switch (titleJustification) {
        case LEFT:
        case CENTER:
        case RIGHT:
            this.titleJustification = titleJustification;
            break;
        
        default:
            throw new IllegalArgumentException(titleJustification + 
                    " is not a valid title justification");
        }
    }

    public void paintBorder(Component component, Graphics g, 
            int x, int y, int width, int height) {

        // first draw the specified border 
        border.paintBorder(component, g, x, y, width, height);

        // check if title is present
        if (title == null || title.length() == 0)
            return;
        
        // Now insert the title. If the titleColor has not been set explicitly,
        // the foreground and background colors are obtained from component.
        final ColorPair colorpair;
        if (titleColor == null) {
            if (border instanceof AbstractBorder
                    && ((AbstractBorder)border).getLineColor() != null) {
                
                colorpair = 
                    ((AbstractBorder)border).getLineColor();
            } else {
                colorpair = ColorPair.create(component.getForeground(), 
                        component.getBackground());
            }
        } else {
            colorpair = titleColor;
        }
        
        Insets insets = border.getBorderInsets(component);
        int    xPos   = x + insets.left;
        int    yPos   = y + insets.top - 1;
        
        // take into account the position
        if (titlePosition == BOTTOM) {
            yPos = y + height;
            if (insets.bottom > 0) {
                yPos -= insets.bottom;
            }
        }

        int    maxLen = width - insets.left - insets.right - 2;
        int    start  = 0; 
        int    end    = title.length();
        
        // take into account the justification
        switch (titleJustification) {
        case LEFT:
            if (end >= maxLen) {
                end = maxLen;
            }
            break;
        
        case CENTER:
            if (end >= maxLen) {
                end = maxLen;
            } else {
                xPos += (maxLen / 2) - (end / 2);
            }
            break;
        
        case RIGHT:
            if (end >= maxLen) {
                start += end - maxLen;
            } else {
                xPos += maxLen - end;
            }
            break;
        }
        
        g.setColor(colorpair);
        g.drawChar(' ', xPos, yPos);
        g.drawString(title.substring(start, end), xPos + 1, yPos);
        g.drawChar(' ', xPos + 1 + end - start, yPos);
    }
}
