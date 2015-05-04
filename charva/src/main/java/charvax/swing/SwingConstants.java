/* interface SwingConstants
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


/**
 * A collection of constants used for positioning components on the screen
 */
public interface SwingConstants {
    
    /** 
     * The central position in an area. Used for
     * both compass-direction constants (NORTH, etc.)
     * and box-orientation constants (TOP, etc.).
     */
    public static final int CENTER  = 0;

    // 
    // Box-orientation constant used to specify locations in a box.
    //
    /** 
     * Box-orientation constant used to specify the top of a box.
     */
    public static final int TOP     = 1;
    /** 
     * Box-orientation constant used to specify the left side of a box.
     */
    public static final int LEFT    = 2;
    /** 
     * Box-orientation constant used to specify the bottom of a box.
     */
    public static final int BOTTOM  = 3;
    /** 
     * Box-orientation constant used to specify the right side of a box.
     */
    public static final int RIGHT   = 4;

    // 
    // Compass-direction constants used to specify a position.
    //
    /** 
     * Compass-direction North (up).
     */
    public static final int NORTH      = 1;
    /** 
     * Compass-direction north-east (upper right).
     */
    public static final int NORTH_EAST = 2;
    /** 
     * Compass-direction east (right).
     */
    public static final int EAST       = 3;
    /** 
     * Compass-direction south-east (lower right).
     */
    public static final int SOUTH_EAST = 4;
    /** 
     * Compass-direction south (down).
     */
    public static final int SOUTH      = 5;
    /** 
     * Compass-direction south-west (lower left).
     */
    public static final int SOUTH_WEST = 6;
    /** 
     * Compass-direction west (left).
     */
    public static final int WEST       = 7;
    /** 
     * Compass-direction north west (upper left).
     */
    public static final int NORTH_WEST = 8;

    //
    // These constants specify a horizontal or 
    // vertical orientation. For example, they are
    // used by scrollbars and sliders.
    //
    /** Horizontal orientation. Used for scrollbars and sliders. */
    public static final int HORIZONTAL = 0;
    /** Vertical orientation. Used for scrollbars and sliders. */
    public static final int VERTICAL   = 1; 

}
