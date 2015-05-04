/* class Insets
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
 * The Insets class specifies the blank space that must be left around
 * the inside of the edges of a Container.  The space can be used for
 * a border, a title, or other items (such as a scroll-bar).
 */
public class Insets {
    
    public int      top;
    public int      left;
    public int      bottom;
    public int      right;
    
    
    public Insets(int top, int left, int bottom, int right) {
        this.top    = top;
        this.left   = left;
        this.bottom = bottom;
        this.right  = right;
    }

    public String toString() {
        return "[" + top + ", " + left + ", " + bottom + ", " + right + "]";
    }
}
