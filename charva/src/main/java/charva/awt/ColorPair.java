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

package charva.awt;


/**
 * Stores two color attributes: foreground and background
 */
public final class ColorPair {

    /** Cached color values     */
    private final static ColorPair[]    colors = new ColorPair[256];
    
    private final Color     foreground;
    private final Color     background;
    

    private ColorPair(Color foreground, Color background) {
        if (foreground == null)
            throw new IllegalArgumentException("foreground == null");
        if (background == null)
            throw new IllegalArgumentException("background == null");
        
        this.foreground = foreground;
        this.background = background;
    }
    
    /**
     * Creates a color-pair corresponding to the specified foreground and 
     * background colors.
     * <p>
     * If both colors are null, the default color-pair is returned.
     */
    public static ColorPair create(Color foreground, Color background) {
        return valueOf(getColorCode(foreground, background));
    }

    /**
     * Returns foreground color of this color-pair
     */
    public Color getForeground() {
        return foreground;
    }


    /**
     * Returns background color of this color-pair
     */
    public Color getBackground() {
        return background;
    }
    
    /**
     * Computes the terminal's color code attribute from this foreground 
     * and background colors
     */
    public int getColorCode() {
        return (background.color << 4) | foreground.color;
    }

    /**
     * Computes the terminal's color code attribute corresponding to 
     * the specified foreground and background colors.
     * <p>
     * If both colors are null, the default color code is returned.
     */
    public static int getColorCode(Color foreground, Color background) {
        if (foreground == null && background == null)
            return Toolkit.getDefaultColor().getColorCode();
        
        int f = (foreground != null ? foreground.color : 0);
        int b = (background != null ? background.color : 0);
        
        return (b << 4) | f;
    }

    /**
     * Returns color-pair object from the given terminal's color-pair attribute
     * 
     * @param colorCode  a terminal's color-pair attribute
     * @return color-pair object
     */
    public static ColorPair valueOf(int colorCode) {
        colorCode = colorCode & 0xff;
        ColorPair color = colors[colorCode];
        if (color == null) {
            color = new ColorPair(
                    Color.valueOf(colorCode & 0x0f), 
                    Color.valueOf((colorCode >>> 4) & 0x0f));
            colors[colorCode] = color;
        }
        
        return color;
    }
}
