/* class Rectangle
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
/**
 * A <code>Rectangle</code> specifies an area in a coordinate space that is 
 * enclosed by the <code>Rectangle</code> object's top-left point 
 * (<i>x</i>,&nbsp;<i>y</i>) 
 * in the coordinate space, its width, and its height. 
 * <p>
 * A <code>Rectangle</code> object's <code>width</code> and
 * <code>height</code> are <code>public</code> fields. The constructors 
 * that create a <code>Rectangle</code>, and the methods that can modify 
 * one, do not prevent setting a negative value for width or height. 
 * <p>
 * A <code>Rectangle</code> whose width or height is negative is considered 
 * empty. If the <code>Rectangle</code> is empty, then the  
 * <code>isEmpty</code> method returns <code>true</code>. No point can be 
 * contained by or inside an empty <code>Rectangle</code>.  The 
 * values of <code>width</code> and <code>height</code>, however, are still 
 * valid.  An empty <code>Rectangle</code> still has a location in the 
 * coordinate space, and methods that change its size or location remain 
 * valid. The behavior of methods that operate on more than one 
 * <code>Rectangle</code> is undefined if any of the participating 
 * <code>Rectangle</code> objects has a negative 
 * <code>width</code> or <code>height</code>. These methods include 
 * <code>intersects</code>, <code>intersection</code>, and 
 * <code>union</code>. 
 */
public class Rectangle {
    
    /**
     * The <i>x</i> coordinate of the <code>Rectangle</code>.
     *
     * @see #setLocation(int, int) 
     * @see #getLocation()
     */
    public int  x;

    /**
     * The <i>y</i> coordinate of the <code>Rectangle</code>.
     * 
     * @see #setLocation(int, int)
     * @see #getLocation()
     */
    public int  y;

    /**
     * The width of the <code>Rectangle</code>.
     * 
     * @see #setSize(int, int)
     * @see #getSize()
     */
    public int  width;

    /**
     * The height of the <code>Rectangle</code>.
     * 
     * @see #setSize(int, int)
     * @see #getSize()
     */
    public int  height;

    
    /**
     * Constructs a new <code>Rectangle</code> whose top-left corner is at
     * (0,&nbsp;0) in the coordinate space, and whose width and height are both
     * zero.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new <code>Rectangle</code>, initialized to match the
     * values of the specified <code>Rectangle</code>.
     * 
     * @param r   the <code>Rectangle</code> from which to copy initial values
     *            to a newly constructed <code>Rectangle</code>
     */
    public Rectangle(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top-left corner is at
     * (0,&nbsp;0) in the coordinate space, and whose width and height are
     * specified by the arguments of the same name.
     * 
     * @param width
     *            the width of the <code>Rectangle</code>
     * @param height
     *            the height of the <code>Rectangle</code>
     */
    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top-left corner is
     * specified by the {@link Point} argument, and whose width and height are
     * specified by the {@link Dimension} argument.
     * 
     * @param p   a <code>Point</code> that is the top-left corner of the
     *            <code>Rectangle</code>
     * @param d   a <code>Dimension</code>, representing the width and height
     *            of the <code>Rectangle</code>
     */
    public Rectangle(Point p, Dimension d) {
        this(p.x, p.y, d.width, d.height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top-left corner is the
     * specified <code>Point</code>, and whose width and height are both
     * zero.
     * 
     * @param p   a <code>Point</code> that is the top left corner of the
     *            <code>Rectangle</code>
     */
    public Rectangle(Point p) {
        this(p.x, p.y, 0, 0);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top left corner is
     * (0,&nbsp;0) and whose width and height are specified by the
     * <code>Dimension</code> argument.
     * 
     * @param d  a <code>Dimension</code>, specifying width and height
     */
    public Rectangle(Dimension d) {
        this(0, 0, d.width, d.height);
    }

    /**
     * Constructs a new <code>Rectangle</code> whose top-left corner is
     * specified as (<code>x</code>,&nbsp;<code>y</code>) and whose
     * width and height are specified by the arguments of the same name.
     * 
     * @param x,&nbsp;y
     *            the specified coordinates
     * @param width
     *            the width of the <code>Rectangle</code>
     * @param height
     *            the height of the <code>Rectangle</code>
     */
    public Rectangle(int x, int y, int width, int height) {
        setBounds(x, y, width, height);
    }

    /**
     * Gets the bounding <code>Rectangle</code> of this <code>Rectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>getBounds</code> method of {@link Component}.
     * 
     * @return a new <code>Rectangle</code>, equal to the bounding 
     *         <code>Rectangle</code> for this <code>Rectangle</code>
     * 
     * @see charva.awt.Component#getBounds
     * @see #setBounds(Rectangle)
     * @see #setBounds(int, int, int, int)
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }   

    /**
     * Sets the bounding <code>Rectangle</code> of this <code>Rectangle</code> 
     * to match the specified <code>Rectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * 
     * @param r the specified <code>Rectangle</code>
     * 
     * @see #getBounds
     * @see charva.awt.Component#setBounds(charva.awt.Rectangle)
     */
    public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    /**
     * Sets the bounding <code>Rectangle</code> of this 
     * <code>Rectangle</code> to the specified 
     * <code>x</code>, <code>y</code>, <code>width</code>, 
     * and <code>height</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     * 
     * @param x,&nbsp;y  the new coordinates for the top-left
     *                   corner of this <code>Rectangle</code>
     * @param width      the new width for this <code>Rectangle</code>
     * @param height     the new height for this <code>Rectangle</code>
     * 
     * @see #getBounds
     * @see charva.awt.Component#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int width, int height) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }   

    /**
     * Returns the location of this <code>Rectangle</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>getLocation</code> method of <code>Component</code>.
     * 
     * @return the <code>Point</code> that is the top-left corner of this
     *         <code>Rectangle</code>.

     * @see #setLocation(Point)
     * @see #setLocation(int, int)
     */
    public Point getLocation() {
        return new Point(x, y);
    }

    /**
     * Moves this <code>Rectangle</code> to the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * 
     * @param p   the <code>Point</code> specifying the new location for this
     *            <code>Rectangle</code>

     * @see #getLocation
     */
    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    /**
     * Moves this <code>Rectangle</code> to the specified location.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     * 
     * @param x,&nbsp;y
     *            the coordinates of the new location
     * 
     * @see #getLocation
     * @see charva.awt.Component#setLocation(int, int)
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates this <code>Rectangle</code> the indicated distance, to the
     * right along the x coordinate axis, and downward along the y coordinate
     * axis.
     * 
     * @param x   the distance to move this <code>Rectangle</code> along the x
     *            axis
     * @param y   the distance to move this <code>Rectangle</code> along the y
     *            axis
     * 
     * @see charva.awt.Rectangle#setLocation(int, int)
     * @see charva.awt.Rectangle#setLocation(charva.awt.Point)
     */
    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Gets the size of this <code>Rectangle</code>, represented by the
     * returned <code>Dimension</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method of <code>Component</code>.
     * 
     * @return a <code>Dimension</code>, representing the size of this
     *         <code>Rectangle</code>.
     * 
     * @see charva.awt.Component#getSize
     * @see #setSize(Dimension)
     * @see #setSize(int, int)
     */
    public Dimension getSize() {
        return new Dimension(width, height);
    }

    /**
     * Sets the size of this <code>Rectangle</code> to match the specified
     * <code>Dimension</code>.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method of <code>Component</code>.
     * 
     * @param d the new size for the <code>Dimension</code> object
     * 
     * @see charva.awt.Component#setSize(charva.awt.Dimension)
     * @see #getSize
     */
    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    /**
     * Sets the size of this <code>Rectangle</code> to the specified width and
     * height.
     * <p>
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method of <code>Component</code>.
     * 
     * @param width  the new width for this <code>Rectangle</code>
     * @param height the new height for this <code>Rectangle</code>
     * 
     * @see charva.awt.Component#setSize(int, int)
     * @see #getSize
     */
    public void setSize(int width, int height) {
        this.width  = width;
        this.height = height;
    }

    /**
     * Checks whether or not this <code>Rectangle</code> contains the
     * specified <code>Point</code>
     * 
     * @param p  the <code>Point</code> to test
     * 
     * @return <code>true</code> if the <code>Point</code> 
     *         (<i>x</i>,&nbsp;<i>y</i>) is inside this <code>Rectangle</code>; 
     *         <code>false</code> otherwise
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    /**
     * Checks whether or not this <code>Rectangle</code> entirely contains the
     * specified <code>Rectangle</code>
     * 
     * @param r  the specified <code>Rectangle</code>
     * 
     * @return <code>true</code> if the <code>Rectangle</code> is contained
     *         entirely inside this <code>Rectangle</code>;
     *         <code>false</code> otherwise.
     */
    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    /**
     * Adds a point, specified by the integer arguments <code>newx</code> and
     * <code>newy</code>, to this <code>Rectangle</code>. The resulting
     * <code>Rectangle</code> is the smallest <code>Rectangle</code> that
     * contains both the original <code>Rectangle</code> and the specified
     * point.
     * <p>
     * After adding a point, a call to <code>contains</code> with the added
     * point as an argument does not necessarily return <code>true</code>.
     * The <code>contains</code> method does not return <code>true</code>
     * for points on the right or bottom edges of a <code>Rectangle</code>.
     * Therefore, if the added point falls on the right or bottom edge of the
     * enlarged <code>Rectangle</code>, <code>contains</code> returns
     * <code>false</code> for that point.
     * 
     * @param newx,&nbsp;newy  the coordinates of the new point
     */
    public void add(int newx, int newy) {
        int x1 = Math.min(x, newx);
        int x2 = Math.max(x + width, newx);
        int y1 = Math.min(y, newy);
        int y2 = Math.max(y + height, newy);
        x      = x1;
        y      = y1;
        width  = x2 - x1;
        height = y2 - y1;
    }

    /**
     * Adds the specified <code>Point</code> to this <code>Rectangle</code>.
     * The resulting <code>Rectangle</code> is the smallest
     * <code>Rectangle</code> that contains both the original
     * <code>Rectangle</code> and the specified <code>Point</code>.
     * <p>
     * After adding a <code>Point</code>, a call to <code>contains</code>
     * with the added <code>Point</code> as an argument does not necessarily
     * return <code>true</code>. The <code>contains</code> method does not
     * return <code>true</code> for points on the right or bottom edges of a
     * <code>Rectangle</code>. Therefore if the added <code>Point</code>
     * falls on the right or bottom edge of the enlarged <code>Rectangle</code>,
     * <code>contains</code> returns <code>false</code> for that
     * <code>Point</code>.
     * 
     * @param pt  the new <code>Point</code> to add to this
     *            <code>Rectangle</code>
     */
    public void add(Point pt) {
        add(pt.x, pt.y);
    }

    /**
     * Adds a <code>Rectangle</code> to this <code>Rectangle</code>. The
     * resulting <code>Rectangle</code> is the union of the two rectangles.
     * 
     * @param r  the specified <code>Rectangle</code>
     */
    public void add(Rectangle r) {
        int x1 = Math.min(x, r.x);
        int x2 = Math.max(x + width, r.x + r.width);
        int y1 = Math.min(y, r.y);
        int y2 = Math.max(y + height, r.y + r.height);
        x      = x1;
        y      = y1;
        width  = x2 - x1;
        height = y2 - y1;
    }

    /**
     * Checks whether this <code>Rectangle</code> entirely contains the
     * <code>Rectangle</code> at the specified location (<i>X</i>,&nbsp;<i>Y</i>)
     * with the specified dimensions (<i>W</i>,&nbsp;<i>H</i>).
     * 
     * @param X,&nbsp;Y
     *            the specified coordinates
     * @param W
     *            the width of the <code>Rectangle</code>
     * @param H
     *            the height of the <code>Rectangle</code>
     * 
     * @return <code>true</code> if the <code>Rectangle</code> specified by 
     *         (<i>X</i>,&nbsp;<i>Y</i>,&nbsp;<i>W</i>,&nbsp;<i>H</i>)
     *         is entirely enclosed inside this <code>Rectangle</code>;
     *         <code>false</code> otherwise.
     */
    public boolean contains(int X, int Y, int W, int H) {
        int w = this.width;
        int h = this.height;
        if ((w | h | W | H) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        
        // Note: if any dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y)
            return false;
        
        w += x;
        W += X;
        if (W <= X) {
            // X+W overflowed or W was zero, return false if...
            // either original w or W was zero or
            // x+w did not overflow or
            // the overflowed x+w is smaller than the overflowed X+W
            if (w >= x || W > w)
                return false;
        } else {
            // X+W did not overflow and W was not zero, return false if...
            // original w was zero or
            // x+w did not overflow and x+w is smaller than X+W
            if (w >= x && W > w)
                return false;
        }
        
        h += y;
        H += Y;
        
        if (H <= Y) {
            if (h >= y || H > h)
                return false;
        } else {
            if (h >= y && H > h)
                return false;
        }
        
        return true;
    }

    /**
     * Checks whether or not this <code>Rectangle</code> contains the point at
     * the specified location (<i>x</i>,&nbsp;<i>y</i>)
     * 
     * @param X,&nbsp;Y  the specified coordinates
     * 
     * @return <code>true</code> if the point (<i>x</i>,&nbsp;<i>y</i>) is
     *         inside this <code>Rectangle</code>; <code>false</code>
     *         otherwise.
     */
    public boolean contains(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        
        // Note: if either dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y)
            return false;
        
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) && (h < y || h > Y));
    }

    /**
     * Determines whether or not this <code>Rectangle</code> and the specified
     * <code>Rectangle</code> intersect. Two rectangles intersect if their
     * intersection is nonempty.
     * 
     * @param r  the specified <code>Rectangle</code>
     * @return <code>true</code> if the specified <code>Rectangle</code> and
     *         this <code>Rectangle</code> intersect; <code>false</code>
     *         otherwise.
     */
    public boolean intersects(Rectangle r) {
        int tw = this.width;
        int th = this.height;
        int rw = r.width;
        int rh = r.height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0)
            return false;
        
        int tx = this.x;
        int ty = this.y;
        int rx = r.x;
        int ry = r.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        // overflow || intersect
        return ((rw < rx || rw > tx) && (rh < ry || rh > ty)
                && (tw < tx || tw > rx) && (th < ty || th > ry));
    }

    /**
     * Tests if the interior of this <code>Rectangle</code> 
     * intersects the interior of a specified set of rectangular 
     * coordinates.
     * 
     * @param x,&nbsp;y  the coordinates of the upper left corner
     *                   of the specified set of rectangular coordinates
     * @param w          the width of the specified set of rectangular
     *                   coordinates
     * @param h          the height of the specified set of rectangular
     *                   coordinates
     * @return <code>true</code> if this <code>Rectangle</code>
     *         intersects the interior of a specified set of rectangular
     *         coordinates; <code>false</code> otherwise
     */
    public boolean intersects(int x, int y, int w, int h) {
        if (isEmpty() || w <= 0 || h <= 0) {
            return false;
        }
        int x0 = this.x;
        int y0 = this.y;
        return (x + w > x0 
                && y + h > y0 
                && x < x0 + this.width 
                && y < y0 + this.height);
    }

    /**
     * Computes the intersection of this <code>Rectangle</code> with the
     * specified <code>Rectangle</code>. Returns a new <code>Rectangle</code>
     * that represents the intersection of the two rectangles. If the two
     * rectangles do not intersect, the result will be an empty rectangle.
     * 
     * @param r  the specified <code>Rectangle</code>
     * @return the largest <code>Rectangle</code> contained in both the
     *         specified <code>Rectangle</code> and in this
     *         <code>Rectangle</code>; or if the rectangles do not intersect,
     *         an empty rectangle.
     */
    public Rectangle intersection(Rectangle r) {
        int  tx1 = this.x;
        int  ty1 = this.y;
        int  rx1 = r.x;
        int  ry1 = r.y;
        long tx2 = tx1;
        tx2 += this.width;
        
        long ty2 = ty1;
        ty2 += this.height;
        long rx2 = rx1;
        rx2 += r.width;
        long ry2 = ry1;
        ry2 += r.height;
        
        if (tx1 < rx1)
            tx1 = rx1;
        if (ty1 < ry1)
            ty1 = ry1;
        if (tx2 > rx2)
            tx2 = rx2;
        if (ty2 > ry2)
            ty2 = ry2;
        
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE)
            tx2 = Integer.MIN_VALUE;
        if (ty2 < Integer.MIN_VALUE)
            ty2 = Integer.MIN_VALUE;
        
        return new Rectangle(tx1, ty1, (int) tx2, (int) ty2);
    }

    /**
     * Computes the union of this <code>Rectangle</code> with the specified
     * <code>Rectangle</code>. Returns a new <code>Rectangle</code> that
     * represents the union of the two rectangles
     * 
     * @param r  the specified <code>Rectangle</code>
     * @return the smallest <code>Rectangle</code> containing both the
     *         specified <code>Rectangle</code> and this
     *         <code>Rectangle</code>.
     */
    public Rectangle union(Rectangle r) {
        int x1 = Math.min(x, r.x);
        int x2 = Math.max(x + width, r.x + r.width);
        int y1 = Math.min(y, r.y);
        int y2 = Math.max(y + height, r.y + r.height);
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * Determines whether or not this <code>Rectangle</code> is empty. A
     * <code>Rectangle</code> is empty if its width or its height is less than
     * or equal to zero.
     * 
     * @return <code>true</code> if this <code>Rectangle</code> is empty;
     *         <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return (width <= 0 || height <= 0);
    }

    /**
     * Returns the hashcode for this <code>Rectangle</code>
     * 
     * @return the hashcode for this <code>Rectangle</code>
     */
    public int hashCode() {
        long bits = x;
        bits += ((long)y) * 37;
        bits += ((long)width) * 43;
        bits += ((long)height) * 47;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Checks whether two rectangles are equal.
     * <p>
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Rectangle</code> object that has the
     * same top-left corner, width, and height as this <code>Rectangle</code>.
     * 
     * @param obj the <code>Object</code> to compare with this
     *            <code>Rectangle</code>
     * @return <code>true</code> if the objects are equal; <code>false</code>
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle r = (Rectangle) obj;
            return (x == r.x 
                    && y == r.y 
                    && width == r.width 
                    && height == r.height);
        }
        
        return super.equals(obj);
    }

    /**
     * Returns a clone of this rectangle
     */
    public Object clone() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Returns a <code>String</code> representing this 
     * <code>Rectangle</code> and its values
     * 
     * @return a <code>String</code> representing this <code>Rectangle</code> 
     *         object's coordinate and size values
     */
    public String toString() {
        return getClass().getName() + "[x=" + x + ",y=" + y 
                + ",width=" + width + ",height=" + height + "]";
    }
}
