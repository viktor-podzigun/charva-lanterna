/* class Color
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

package charva.awt;


/**
 * A class used to represent the color values available on a text terminal.
 */
public final class Color {

    public static final Color BLACK         = new Color(0,     0,   0);
    public static final Color RED           = new Color(255,   0,   0);
    public static final Color GREEN         = new Color(0,   255,   0);
    public static final Color YELLOW        = new Color(255, 255,   0);
    public static final Color BLUE          = new Color(0,     0, 255);
    public static final Color MAGENTA       = new Color(255,   0, 255);
    public static final Color CYAN          = new Color(0,   255, 255);
    public static final Color WHITE         = new Color(255, 255, 255);

    public static final Color LIGHT_BLACK   = new Color(0,     0,   0, true);
    public static final Color LIGHT_RED     = new Color(255,   0,   0, true);
    public static final Color LIGHT_GREEN   = new Color(0,   255,   0, true);
    public static final Color LIGHT_YELLOW  = new Color(255, 255,   0, true);
    public static final Color LIGHT_BLUE    = new Color(0,     0, 255, true);
    public static final Color LIGHT_MAGENTA = new Color(255,   0, 255, true);
    public static final Color LIGHT_CYAN    = new Color(0,   255, 255, true);
    public static final Color LIGHT_WHITE   = new Color(255, 255, 255, true);
    
    
    final byte  color;
    
    
    /**
     * Construct a Color from the specified RGB values. Each value must
     * be in the range 0-255.
     */
    private Color(int red, int green, int blue) {
        this(red, green, blue, false);
    }
    
    private Color(int red, int green, int blue, boolean intensified) {
        red   = (red   != 0 ? 1 : 0);
        green = (green != 0 ? 1 : 0);
        blue  = (blue  != 0 ? 1 : 0);
        int intensity = (intensified ? 1 : 0);
        
        color = (byte)((intensity << 3) | (red << 2) | (green << 1) | blue);
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Color))
            return false;

        Color othercolor = (Color) obj;
        return (color == othercolor.color);
    }
    
    public int hashCode() {
        return color;
    }
    
    public String toString() {
        switch (color) {
        case 0x00: return "black";
        case 0x01: return "blue";
        case 0x02: return "green";
        case 0x03: return "cyan";
        case 0x04: return "red";
        case 0x05: return "magenta";
        case 0x06: return "yellow";
        case 0x07: return "white";

        case 0x08: return "lightBlack";
        case 0x09: return "lightBlue";
        case 0x0A: return "lightGreen";
        case 0x0B: return "lightCyan";
        case 0x0C: return "lightRed";
        case 0x0D: return "lightMagenta";
        case 0x0E: return "lightYellow";
        case 0x0F: return "lightWhite";
        
        default:
            return "Unknown";
        }
    }

    public static Color valueOf(int color) {
        switch (color) {
        case 0x00: return Color.BLACK;
        case 0x01: return Color.BLUE;
        case 0x02: return Color.GREEN;
        case 0x03: return Color.CYAN;
        case 0x04: return Color.RED;
        case 0x05: return Color.MAGENTA;
        case 0x06: return Color.YELLOW;
        case 0x07: return Color.WHITE;

        case 0x08: return Color.LIGHT_BLACK;
        case 0x09: return Color.LIGHT_BLUE;
        case 0x0A: return Color.LIGHT_GREEN;
        case 0x0B: return Color.LIGHT_CYAN;
        case 0x0C: return Color.LIGHT_RED;
        case 0x0D: return Color.LIGHT_MAGENTA;
        case 0x0E: return Color.LIGHT_YELLOW;
        case 0x0F: return Color.LIGHT_WHITE;
        
        default:
            throw new RuntimeException("Unknown color: " + color);
        }
    }
}
