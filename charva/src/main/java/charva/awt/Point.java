/* class Point
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
 * A point representing a location in (x, y) coordinate space, specified
 * in integer precision.
 */
public class Point implements Cloneable {

    public int  x;
    public int  y;
    
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Point addOffset(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

    public Point addOffset(Dimension d) {
        return new Point(this.x + d.width, this.y + d.height);
    }

    public Point addOffset(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point subtractOffset(Point p) {
        return new Point(this.x - p.x, this.y - p.y);
    }

    public Point subtractOffset(Dimension d) {
        return new Point(this.x - d.width, this.y - d.height);
    }

    public Point subtractOffset(int x, int y) {
        return new Point(this.x - x, this.y - y);
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }   

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }   

    /**
     * Translates this point by dx along the x axis and by dy along
     * the y axis.
     */
    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Translates this point by the x and y values given in "point"
     */
    public void translate(Point point) {
        x += point.x;
        y += point.y;
    }

    /**
     * Returns true if this point is inside the specified rectangle
     */
    public boolean isInside(Rectangle rect) {
        return rect.contains(this);
    }

    /**
     * Returns true is this Point is equal to the specified Point.
     */
    public boolean equals(Point other) {
        return (x == other.x && y == other.y);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj instanceof Point)
            return equals((Point)obj);
        
        return false;
    }

    public int hashCode() {
        long bits = x;
        bits ^= ((long)y) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y + "]";
    }

    public Object clone() {
        return new Point(this);
    }
}
